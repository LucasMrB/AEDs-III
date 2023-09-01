package Presentation;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedList;
import Model.Spotify;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.EOFException;

public class Crud {
    // lapide = ' {.} ' arquivo valido || ' {.} ' arquivo nao valido
    private static RandomAccessFile raf; // atributo de classe, serve para movimentar o ponteiro 
    private static byte[] lapideValida = new byte[6];
    private static byte[] lapideNaoValida = new byte[6];
    private static long final_arc;

    //inicializar o raf
    public Crud(){
        lapideValida = toByteArray("{.}");
        lapideNaoValida = toByteArray("{*}");
        try{
            raf = new RandomAccessFile("../archive/archive_crud", "rw");
        }catch(FileNotFoundException e){
            System.out.println(e.getMessage());
        }

    }

    private byte[] toByteArray(String a){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try{

            dos.writeUTF(a);
        }catch(IOException e){}
        return baos.toByteArray();
    }

    // carga do crud
    public static void create(LinkedList<Spotify> spotify_list){
        //char Lapide = '¬';  
        spotify_list.forEach(data ->{
            try{

                long aux = raf.getFilePointer();
                if(aux == 0){
                    raf.writeShort(data.getId());
                    raf.write(lapideValida);
                    raf.writeInt(data.toByteArray().length);
                    raf.write(data.toByteArray());
                }else{
                    raf.seek(0);
                    raf.writeShort(data.getId());
                    raf.seek(aux);
                    raf.write(lapideValida);
                    raf.writeInt(data.toByteArray().length);
                    raf.write(data.toByteArray());
                }

            
            }catch(IOException e){System.out.println(e.getMessage());}

        });

    }

    //inserir
    public static void create2(Spotify date){
        try{
            if(read(date.getId()).length == 0){
                while(raf.read() != -1){}
                raf.write(lapideValida);
                raf.write(date.toByteArray().length);
                raf.write(date.toByteArray());


            }
        }catch(IOException e){
        }
    }

    // pegar posição no arq de bit, auxiliar
    private static long getPointerId(short id){
        long retorno = 0;
        try{
            raf.seek(0);
            raf.readShort();

            while(true){
                if(raf.readUTF().equals("{.}")){
                    int byteLength = raf.readInt();
                    if(raf.readShort() == id){
                        retorno = raf.getFilePointer()-6;   //-6 (2bytes short + 4bytes int)
                        break;
                    } else {
                        raf.seek(raf.getFilePointer()-2);   //-2bytes short para pular registro
                        raf.seek(raf.getFilePointer()+byteLength);
                    }
                }else{
                    raf.seek(raf.getFilePointer()+raf.readInt());
                }
            }

        }catch(IOException e){
            // se não achar, ira vir p ca
            System.out.println("nao encontrado");
            return 0;
        }
        return retorno;
    }
     
    //ler arquivo
    public static byte[] read(short id)throws IOException{
        byte[] aux = new byte[0];
        raf.seek(getPointerId(id));
        if(raf.getFilePointer() != 0){
            aux = new byte[raf.readInt()];
            raf.read(aux);
        }
        return aux;
    }
    
    //deletar arquivo
    public static byte[] delete(short id)throws IOException{
        byte[] aux = new byte[0];
        raf.seek(getPointerId(id));
        if(raf.getFilePointer() != 0){
            raf.seek(raf.getFilePointer()-5);
            raf.write(lapideNaoValida);
            aux = new byte[raf.readInt()];
            raf.read(aux);
        }
        return aux; //retorna arquivo deletado (em bytes, converter na main se quiser printar)
    }

    //upload arquivo (nome)
    public static void update(short id, String nome)throws IOException{
        raf.seek(getPointerId(id));
        if(raf.getFilePointer()!= 0){
            long save = raf.getFilePointer();       // salvar inicio do arquivo
            byte aux2[] = new byte[raf.readInt()];  // tamanho do vetor de bytes
            raf.read(aux2);
            Spotify auxClass = new Spotify();
            auxClass.fromByteArray(aux2);
            auxClass.setSinger(nome);
            raf.seek(save);

            System.out.println(aux2.length);
            System.out.println(auxClass.toByteArray().length);

            if(auxClass.toByteArray().length <= aux2.length){
                raf.writeInt(auxClass.toByteArray().length);
                raf.write(auxClass.toByteArray());
            }else{
                raf.seek(raf.getFilePointer()-5);
                raf.write(lapideNaoValida);
                create2(auxClass);    
            }
        }

    }
}
    

    /*
     
    public static byte[] read(short id){
        byte[] aux = new byte[0];
        try{
            c           raf.seek(0);
            raf.readShort();
            
            while(true){
                
                if(raf.readUTF().equals("¬")){
                    int byteLength = raf.readInt();
                    if(raf.readShort() == id){
                        raf.seek(raf.getFilePointer()-2);
                        aux = new byte[byteLength];
                        raf.read(aux);
                        break;
                    } else {
                        raf.seek(raf.getFilePointer()-2);
                        raf.seek(raf.getFilePointer()+byteLength);
                    }
                }else{
                    raf.seek(raf.getFilePointer()+raf.readInt());
                }
            }

        }catch(IOException e){
            System.out.println("nao encontrado");
            // se não achar, ira vir p ca
        }
        return aux;
    }
    */