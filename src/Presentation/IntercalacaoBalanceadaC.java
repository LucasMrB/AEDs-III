package Presentation;

import java.io.IOException;
import java.io.RandomAccessFile;

import Model.Spotify;

/* 
 * Intercalacao Balanceada Comum 
 * m = 100 (registros parametrizavel) //n = 2 caminhos (n*2 = 4)
 * 
*/


public class IntercalacaoBalanceadaC {
    private static RandomAccessFile raf;
    private static RandomAccessFile rafArq1;
    private static RandomAccessFile rafArq2;
    private static RandomAccessFile rafArq3;
    private static RandomAccessFile rafArq4;
    public IntercalacaoBalanceadaC(){
        try{
            raf = new RandomAccessFile("../archive//archive_crud", "r");

            rafArq1 = new RandomAccessFile("../archive//ordenacao//arq1.db","rw");
            rafArq2 = new RandomAccessFile("../archive//ordenacao//arq2.db","rw");
            rafArq3 = new RandomAccessFile("../archive//ordenacao//arq3.db","rw");
            rafArq4 = new RandomAccessFile("../archive//ordenacao//arq4.db","rw");

        }catch(IOException e ){
            System.out.println("error ao abrir algum arquivo");
        }
    }

    // lê uma classe/registro
    private static Spotify readData()throws IOException{
        Spotify retorno = new Spotify();
        if(raf.readUTF().equals("{.}")){
            byte[] aux = new byte[raf.readInt()];
            raf.read(aux);
            retorno.fromByteArray(aux);
        }else{
            int aux = raf.readInt();
            raf.seek(raf.getFilePointer()+aux);
            retorno = readData();
        }
        return retorno;
    }

    // pega 100 registros // m=100 
    private static Spotify[] getData()throws IOException{
        Spotify[] tmp = new Spotify[100];
        if(raf.getFilePointer() <= 4){
            raf.seek(0);
            raf.readShort();
        }
        int j = 0;
        try{
            for(int i = 0;i<100;i++){ tmp[i] = readData(); j = i;}
        }catch(NullPointerException e){
            for(int i = j; i < 100;i++){tmp[j] = new Spotify(null, null, null, null, i, null, null); tmp[j].setID((short)1000000000);}
        }
        
        return tmp;
    }    

    //swap_auxQuickSort
    private static void swap(Spotify[] array, int num1, int num2){
        Spotify tmp = array[num1];
        array[num1] = array[num2];
        array[num2] = tmp;
    }

    //AuxQuickSort
    private static int partition(Spotify[] array, int low, int high){
        int pivo = array[high].getId();
        int i = (low-1);

        for(int j = low; j <= high-1; j++){
            if(array[j].getId() < pivo){
                i++;
                swap(array, i,j);
            }
        }
        swap(array, (i+1),high);
        return (i+1);

    }
    //quicksort 
    private static void quicksort(Spotify[] arr, int low, int high){
        if(low < high){
            int pi = partition(arr,low,high);
            quicksort(arr, low, pi-1);
            quicksort(arr, pi+1, high);
        }
    }

    public static void writeArchive_Distribuicao(){
            
        while(true){
            try{
                Spotify[] aux1 = getData();
                Spotify[] aux2 = getData();
                quicksort(aux1, 0, 99);
                quicksort(aux2, 0, 99);

                //escrever no arquivo 1
                
                for(int i = 0; i <100;i++){
                    if(aux1[i].getId() == (short)1000000000){
                        i = 200;
                    }else{
                        rafArq1.writeInt(aux1[i].toByteArray().length);
                        rafArq1.write(aux1[i].toByteArray());
                    }
                }

                //escrever no arquivo2

                for(int i = 0; i < 100;i++){
                    if(aux2[i].getId() == (short)1000000000){
                        i = 200;
                    }else{
                        rafArq2.writeInt(aux2[i].toByteArray().length);
                        rafArq2.write(aux2[i].toByteArray());
                    }
                }
                    
            }catch(IOException e){
                System.out.println("fim do getData");
                break;
            }
        }
        
    }
    
    //ordenação em memoria secundaria
    /*
    */
    
    public static void ordenacao_memsecundaria(){
        int m = 100; Spotify registro1 = new Spotify(); Spotify registro2 = new Spotify(); int aux_reg1 = 0; int aux_reg2 = 0; 
        
        boolean grupo_arq = false;// true = arquivo 1 e 2 | false = arquivo 2 e 3
     
        byte[] reg_1; byte[] reg_2;



        boolean select_arq_3_4 = true;//true = arquivo 3 | false = arquivo 4
     
        try{
            rafArq1.seek(0);
            rafArq2.seek(0);
            rafArq3.seek(0);
            rafArq4.seek(0);

                if(select_arq_3_4 == true){

                    while(aux_reg1 < m && aux_reg2 < m){
                        
                        reg_1 = new byte[rafArq1.readInt()];
                        rafArq1.read(reg_1);
                        registro1.fromByteArray(reg_1);
                        reg_2 = new byte[rafArq2.readInt()];
                        rafArq2.read(reg_2);
                        registro2.fromByteArray(reg_2);

                        if(registro1.getId() < registro2.getId()){

                            rafArq3.writeInt(registro1.toByteArray().length);
                            rafArq3.write(registro1.toByteArray());
                            aux_reg1++;
                            rafArq2.seek((rafArq2.getFilePointer()-(long)(4 +reg_2.length)));

                        }else{

                            rafArq3.writeInt(registro2.toByteArray().length);
                            rafArq3.write(registro2.toByteArray());
                            aux_reg2++;
                            rafArq1.seek((rafArq1.getFilePointer()-(long)(4 +reg_1.length)));
                        
                        }
                    
                    }

                    if(aux_reg1 == m){
                        for(int i = aux_reg2; i < m; i++){

                            reg_2 = new byte[rafArq2.readInt()];
                            rafArq2.read(reg_2);
                            registro2.fromByteArray(reg_2);
                            rafArq3.writeInt(registro2.toByteArray().length);
                            rafArq3.write(registro2.toByteArray());

                        }
                    }else{
                        for(int i = aux_reg1; i < m; i++){

                            reg_1 = new byte[rafArq1.readInt()];
                            rafArq1.read(reg_1);
                            registro1.fromByteArray(reg_1);
                            rafArq3.writeInt(registro1.toByteArray().length);
                            rafArq3.write(registro1.toByteArray());
                        
                        }

                    }
                    select_arq_3_4 = false;
                }else{
                    while(aux_reg1 < m && aux_reg2 < m){
                        
                        reg_1 = new byte[rafArq1.readInt()];
                        rafArq1.read(reg_1);
                        registro1.fromByteArray(reg_1);
                        reg_2 = new byte[rafArq2.readInt()];
                        rafArq2.read(reg_2);
                        registro2.fromByteArray(reg_2);

                        if(registro1.getId() < registro2.getId()){

                            rafArq4.writeInt(registro1.toByteArray().length);
                            rafArq4.write(registro1.toByteArray());
                            aux_reg1++;
                            rafArq2.seek((rafArq2.getFilePointer()-(long)(4 +reg_2.length)));

                        }else{

                            rafArq4.writeInt(registro2.toByteArray().length);
                            rafArq4.write(registro2.toByteArray());
                            aux_reg2++;
                            rafArq1.seek((rafArq1.getFilePointer()-(long)(4 +reg_1.length)));
                        
                        }
                    
                    }

                    if(aux_reg1 == m){
                        for(int i = aux_reg2; i < m; i++){

                            reg_2 = new byte[rafArq2.readInt()];
                            rafArq2.read(reg_2);
                            registro2.fromByteArray(reg_2);
                            rafArq4.writeInt(registro2.toByteArray().length);
                            rafArq4.write(registro2.toByteArray());

                        }
                    }else{
                        for(int i = aux_reg1; i < m; i++){

                            reg_1 = new byte[rafArq1.readInt()];
                            rafArq1.read(reg_1);
                            registro1.fromByteArray(reg_1);
                            rafArq4.writeInt(registro1.toByteArray().length);
                            rafArq4.write(registro1.toByteArray());
                        
                    }

                }
                select_arq_3_4 = true;
            }
                
                    
        }catch(IOException e){}
    }


    public void printArq3(){
        Spotify aux = new Spotify();
        byte[] a;
        try{
        rafArq3.seek(0);
        for(int i = 0; i <200;i++){

                    a = new byte[rafArq3.readInt()];
                    rafArq3.read(a);
                    aux.fromByteArray(a);
                    System.out.println(aux.getId());
                }
            }catch(IOException e){
            }
    }


    //printar algum arquivo para saber o q acontece
    public void printarq(){
        byte b[];
        try{
            rafArq1.seek(0);
        }catch(IOException e){}
        while(true){
            try{
                b = new byte[rafArq1.readInt()];
                rafArq1.read(b);
                Spotify a = new Spotify();
                a.fromByteArray(b);

                System.out.println(a.getId());

                

            }catch(IOException e){}
        }
    }
    

    public static void teste(){
        byte[] a; Spotify aux = new Spotify();
        try{
            rafArq1.seek(0);
            a = new byte[rafArq1.readInt()];
            rafArq1.read(a);
            aux.fromByteArray(a);
            aux.write_data();

            System.out.println(a.length);
            rafArq1.seek(rafArq1.getFilePointer()-(long)((long)4 +(long)a.length));
            System.out.println(rafArq1.getFilePointer());
        }catch(IOException e){}
    }


}