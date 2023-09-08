package Presentation;

import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;

import Model.Spotify;
import Model.Data;


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
    private static int m = 1000;
    private static int select_archive;

    private static boolean grupo_arq = false;// true = arquivo 1 e 2 | false = arquivo 2 e 3

    private static boolean select_arq_1_2 = true;//true = arquivo 1 | false = arquivo 2

    private static boolean select_arq_3_4 = true;//true = arquivo 3 | false = arquivo 4

    private static boolean aux_distribuicao_1 = false;
    private static boolean aux_distribuicao_2 = false;

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
        try{

            if(raf.readUTF().equals("{.}")){
                byte[] aux = new byte[raf.readInt()];
                raf.read(aux);
                retorno.fromByteArray(aux);
            }else{
                int aux = raf.readInt();
                raf.seek(raf.getFilePointer()+aux);
                retorno = readData();
            }
        }catch(EOFException e){
            retorno = new Spotify("track_null", "music_null", "singer_null", new Data(), 1, "genres_null", (short)1000000000);
        }
        return retorno;
    }

    // pega 100 registros // m=100 
    private static Spotify[] getData()throws IOException{
        Spotify[] tmp = new Spotify[m];
        if(raf.getFilePointer() <= 4){
            raf.seek(0);
            raf.readShort();
        }
        int j = 0;
        for(int i = 0;i < m;i++){ tmp[i] = readData(); j = i;}
        
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

    //perdendo dados, indo até 9800, total = 9998
    public static void writeArchive_Distribuicao(){
        try{
            raf.seek(0);
        }catch(IOException e){}

            
        while(true){
            try{
                Spotify[] aux1 = getData();
                Spotify[] aux2 = getData();
                quicksort(aux1, 0, m-1);
                quicksort(aux2, 0, m-1);

                //escrever no arquivo 1
                
                for(int i = 0; i < m;i++){
                    if(aux1[i].getId() == (short)1000000000){
                        aux_distribuicao_1 = true;
                        i = m;//break
                    }else{
                        rafArq1.writeInt(aux1[i].toByteArray().length);
                        rafArq1.write(aux1[i].toByteArray());
                    }
                }

                //escrever no arquivo2

                for(int i = 0; i < m;i++){
                    if(aux2[i].getId() == (short)1000000000){
                        aux_distribuicao_2 = true;
                        i = m;//break
                    }else{
                        rafArq2.writeInt(aux2[i].toByteArray().length);
                        rafArq2.write(aux2[i].toByteArray());
                    }
                }
                System.out.println("LUKETA");

                if(aux_distribuicao_1 == true && aux_distribuicao_2 == true){
                    System.out.println("acabou");
                    break;
                }
                    
            }catch(EOFException e){
                System.out.println("EOF");
                break;
            }catch(IOException e){
                System.out.println("fim do getData");
                break;
            }
        
        }
    }   
    
    //ordenação em memoria secundaria

    public static void intercalao(){
        byte[] reg_1; Spotify registro1 = new Spotify();
        while(m < 5000){
            aux_intercalacao_3_4();
            if(m > 5000) break;
            aux_intercalacao_1_2();
            if(m > 5000) break;
            System.out.println(m);
        }
        
        try{
                RandomAccessFile raf_ordenado = new RandomAccessFile("../archive//ordenacao//arq1.db", "rw");

                switch(select_archive){
                    case 1: 
                        try{
                            while(true){

                                rafArq1.seek(0);
                                reg_1 = new byte[rafArq1.readInt()];
                                rafArq1.read(reg_1);
                                registro1.fromByteArray(reg_1);
                                raf_ordenado.writeInt(registro1.toByteArray().length);
                                raf_ordenado.write(registro1.toByteArray());
                            }
                        }catch(IOException e){}                 
    
                    break;
                    case 2:
                        try{
                            while(true){

                                rafArq2.seek(0);
                                reg_1 = new byte[rafArq2.readInt()];
                                rafArq2.read(reg_1);
                                registro1.fromByteArray(reg_1);
                                raf_ordenado.writeInt(registro1.toByteArray().length);
                                raf_ordenado.write(registro1.toByteArray());
                            }
                        }catch(IOException e){}
    
                    break;
                    case 3:
                        try{
                            while(true){

                                rafArq3.seek(0);
                                reg_1 = new byte[rafArq3.readInt()];
                                rafArq3.read(reg_1);
                                registro1.fromByteArray(reg_1);
                                raf_ordenado.writeInt(registro1.toByteArray().length);
                                raf_ordenado.write(registro1.toByteArray());
                            }
                        }catch(IOException e){}

                    break;
                    case 4:
                        try{
                            while(true){

                                rafArq4.seek(0);
                                reg_1 = new byte[rafArq4.readInt()];
                                rafArq4.read(reg_1);
                                registro1.fromByteArray(reg_1);
                                raf_ordenado.writeInt(registro1.toByteArray().length);
                                raf_ordenado.write(registro1.toByteArray());
                            }
                        }catch(IOException e){}
                    break;

                }

                raf_ordenado.close();
            }catch(IOException e){
            }
    }


    public static void aux_intercalacao_3_4(){
        System.out.println("entrou34");
        Spotify registro1 = new Spotify(); Spotify registro2 = new Spotify(); int aux_reg1 = 0; int aux_reg2 = 0; 
        byte[] reg_1; byte[] reg_2;

        try{
            rafArq1.seek(0);
            rafArq2.seek(0);
            rafArq3.seek(0);
            rafArq4.seek(0);
        }catch(IOException e){}

        select_arq_3_4 = true;
        try{
            while(true){
                if(select_arq_3_4){
                    
                    aux_reg1 = 0; aux_reg2 = 0;
                    try{
                    
                        while(aux_reg1 < m && aux_reg2 < m){
                            reg_1 = new byte[rafArq1.readInt()];
                            rafArq1.read(reg_1);
                            registro1.fromByteArray(reg_1);
                    
                            //System.out.println(registro1.getId());
                    
                            reg_2 = new byte[rafArq2.readInt()];
                            rafArq2.read(reg_2);
                            registro2.fromByteArray(reg_2);
                    
                            //System.out.println(registro2.getId());

                            if(registro1.getId() < registro2.getId()){
                                rafArq3.writeInt(reg_1.length);
                                rafArq3.write(reg_1);
                                aux_reg1++;
                            }else{
                                rafArq3.writeInt(reg_2.length);
                                rafArq3.write(reg_2);
                                aux_reg2++;
                            }
                        }

                        if(aux_reg1 == m){
                            for(int i = aux_reg2;i < m;i++){
                                reg_2 = new byte[rafArq2.readInt()];
                                rafArq2.read(reg_2);
                                rafArq3.writeInt(reg_2.length);
                                rafArq3.write(reg_2);
                            }
                        }else{

                            for(int i = aux_reg1;i < m;i++){
                                reg_1 = new byte[rafArq1.readInt()];
                                rafArq1.read(reg_1);
                                rafArq3.writeInt(reg_1.length);
                                rafArq3.write(reg_1);
                            }
                        }
                        select_arq_3_4=false;
                    }catch(EOFException e){
                        try{
                            select_archive = 3;

                            if(rafArq1.read() == -1){
                                while(true){
                                    reg_2 = new byte[rafArq2.readInt()];
                                    rafArq2.read(reg_2);
                                    rafArq3.writeInt(reg_2.length);
                                    rafArq3.write(reg_2);
                                }
                            }
                            if(rafArq2.read() == -1){
                                rafArq1.seek(rafArq1.getFilePointer()-1);
                                while(true){
                                    reg_1 = new byte[rafArq1.readInt()];
                                    rafArq1.read(reg_1);
                                    rafArq3.writeInt(reg_1.length);
                                    rafArq3.write(reg_1);
                                }   
                
                            }
                        }catch(IOException f){
                            System.out.println("IOE");
                            m*=2;
                            select_archive = 3;
                            System.out.println("FIM");
                            break;
                        }
                    }
                }else{
                    
                    aux_reg1 = 0; aux_reg2 = 0;
                    try{
                    
                        while(aux_reg1 < m && aux_reg2 < m){
                            reg_1 = new byte[rafArq1.readInt()];
                            rafArq1.read(reg_1);
                            registro1.fromByteArray(reg_1);
                    
                            //System.out.println(registro1.getId());
                    
                            reg_2 = new byte[rafArq2.readInt()];
                            rafArq2.read(reg_2);
                            registro2.fromByteArray(reg_2);
                    
                            //System.out.println(registro2.getId());

                            if(registro1.getId() < registro2.getId()){
                                rafArq4.writeInt(reg_1.length);
                                rafArq4.write(reg_1);
                                aux_reg1++;
                            }else{
                                rafArq4.writeInt(reg_2.length);
                                rafArq4.write(reg_2);
                                aux_reg2++;
                            }
                        }

                        if(aux_reg1 == m){
                            for(int i = aux_reg2;i < m;i++){
                                reg_2 = new byte[rafArq2.readInt()];
                                rafArq2.read(reg_2);
                                rafArq4.writeInt(reg_2.length);
                                rafArq4.write(reg_2);
                            }
                        }else{
                            for(int i = aux_reg1;i < m;i++){
                                reg_1 = new byte[rafArq1.readInt()];
                                rafArq1.read(reg_1);
                                rafArq4.writeInt(reg_1.length);
                                rafArq4.write(reg_1);
                            }
                        }
                        select_arq_3_4 = true;
                    }catch(EOFException e){
                        try{
                            select_archive = 4;
                            
                            if(rafArq1.read() == -1){
                                while(true){
                                    reg_2 = new byte[rafArq2.readInt()];
                                    rafArq2.read(reg_2);
                                    rafArq4.writeInt(reg_2.length);
                                    rafArq4.write(reg_2);
                                }
                            }
                            if(rafArq2.read() == -1){
                                rafArq1.seek(rafArq1.getFilePointer()-1);
                                while(true){
                                    reg_1 = new byte[rafArq1.readInt()];
                                    rafArq1.read(reg_1);
                                    rafArq4.writeInt(reg_1.length);
                                    rafArq4.write(reg_1);
                                }   
                
                            }
                        
                        }catch(IOException f){
                            System.out.println("IOE");
                            m*=2;
                            select_archive = 4;
                            System.out.println("FIM");
                            break;
                        }
                    }
                }
        
            }  
        
        }catch(IOException f){
            m*=2;
            System.out.println("IOE");
        }
    }

    public static void aux_intercalacao_1_2(){
        System.out.println("entrou12");
        Spotify registro1 = new Spotify(); Spotify registro2 = new Spotify(); int aux_reg1 = 0; int aux_reg2 = 0; 
        byte[] reg_1; byte[] reg_2;

        try{
            rafArq1.seek(0);
            rafArq2.seek(0);
            rafArq3.seek(0);
            rafArq4.seek(0);
        }catch(IOException e){}

        select_arq_1_2 = true;
        try{
            while(true){
                if(select_arq_1_2){
                    
                    aux_reg1 = 0; aux_reg2 = 0;
                    try{
                    
                        while(aux_reg1 < m && aux_reg2 < m){
                            reg_1 = new byte[rafArq3.readInt()];
                            rafArq3.read(reg_1);
                            registro1.fromByteArray(reg_1);
                    
                            //System.out.println(registro1.getId());
                    
                            reg_2 = new byte[rafArq4.readInt()];
                            rafArq4.read(reg_2);
                            registro2.fromByteArray(reg_2);
                    
                            //System.out.println(registro2.getId());

                            if(registro1.getId() < registro2.getId()){
                                rafArq1.writeInt(reg_1.length);
                                rafArq1.write(reg_1);
                                aux_reg1++;
                            }else{
                                rafArq1.writeInt(reg_2.length);
                                rafArq1.write(reg_2);
                                aux_reg2++;
                            }
                        }

                        if(aux_reg1 == m){
                            for(int i = aux_reg2;i < m;i++){
                                reg_2 = new byte[rafArq4.readInt()];
                                rafArq4.read(reg_2);
                                rafArq1.writeInt(reg_2.length);
                                rafArq1.write(reg_2);
                            }
                        }else{
                            for(int i = aux_reg1;i < m;i++){
                                reg_1 = new byte[rafArq3.readInt()];
                                rafArq3.read(reg_1);
                                rafArq1.writeInt(reg_1.length);
                                rafArq1.write(reg_1);
                            }
                        }
                        select_arq_1_2=false;
                    }catch(EOFException e){
                        try{
                            select_archive = 1;
                            if(rafArq3.read() == -1){
                                while(true){
                                    reg_2 = new byte[rafArq4.readInt()];
                                    rafArq4.read(reg_2);
                                    rafArq1.writeInt(reg_2.length);
                                    rafArq1.write(reg_2);
                                }
                            }
                            if(rafArq4.read() == -1){
                                rafArq3.seek(rafArq3.getFilePointer()-1);
                                while(true){
                                    reg_1 = new byte[rafArq3.readInt()];
                                    rafArq3.read(reg_1);
                                    rafArq1.writeInt(reg_1.length);
                                    rafArq1.write(reg_1);
                                }   
                
                            }
                        }catch(IOException f){
                            m*=2;
                            select_archive = 1;
                            System.out.println("FIM");
                            break;
                        }
                    }
                }else{
                    
                    aux_reg1 = 0; aux_reg2 = 0;
                    try{
                    
                        while(aux_reg1 < m && aux_reg2 < m){
                            if(m > 5000) break;
                            reg_1 = new byte[rafArq3.readInt()];
                            rafArq3.read(reg_1);
                            registro1.fromByteArray(reg_1);
                    
                            //System.out.println(registro1.getId());
                    
                            reg_2 = new byte[rafArq4.readInt()];
                            rafArq4.read(reg_2);
                            registro2.fromByteArray(reg_2);
                    
                            //System.out.println(registro2.getId());

                            if(registro1.getId() < registro2.getId()){
                                rafArq2.writeInt(reg_1.length);
                                rafArq2.write(reg_1);
                                aux_reg1++;
                            }else{
                                rafArq2.writeInt(reg_2.length);
                                rafArq2.write(reg_2);
                                aux_reg2++;
                            }
                        }

                        if(aux_reg1 == m){
                            for(int i = aux_reg2;i < m;i++){
                                reg_2 = new byte[rafArq4.readInt()];
                                rafArq4.read(reg_2);
                                rafArq2.writeInt(reg_2.length);
                                rafArq2.write(reg_2);
                            }
                        }else{
                            for(int i = aux_reg1;i < m;i++){
                                reg_1 = new byte[rafArq3.readInt()];
                                rafArq3.read(reg_1);
                                rafArq2.writeInt(reg_1.length);
                                rafArq2.write(reg_1);
                            }
                        }
                        select_arq_1_2 = true;
                    }catch(EOFException e){
                        select_archive = 2;
                        try{
                            
                            if(rafArq3.read() == -1){
                                while(true){
                                    reg_2 = new byte[rafArq4.readInt()];
                                    rafArq4.read(reg_2);
                                    rafArq2.writeInt(reg_2.length);
                                    rafArq2.write(reg_2);
                                }
                            }
                            if(rafArq4.read() == -1){
                                rafArq3.seek(rafArq3.getFilePointer()-1);
                                while(true){
                                    reg_1 = new byte[rafArq3.readInt()];
                                    rafArq3.read(reg_1);
                                    rafArq2.writeInt(reg_1.length);
                                    rafArq2.write(reg_1);
                                }   
                
                            }
                        
                        }catch(IOException f){
                            m*=2;
                            select_archive = 2;
                            System.out.println("FIM");
                            break;
                        }
                    }
                }
        
            }  
        
        }catch(IOException f){
            m*=2;
            System.out.println("IOE");
        }
    }




    //IGNORAR
    public static void ordenacao_memsecundaria(){
        Spotify registro1 = new Spotify(); Spotify registro2 = new Spotify(); int aux_reg1 = 0; int aux_reg2 = 0; 
        
        // m - multiplicar por 2 para ir aumentando a quantidade q é pega no arquivo
        
        byte[] reg_1; byte[] reg_2;
        
        
        //grupo_arq = false;        // true = arquivo 1 e 2 | false = arquivo 2 e 3

        //select_arq_1_2 = true;    //true = arquivo 1 | false = arquivo 2

        //select_arq_3_4 = true;    //true = arquivo 3 | false = arquivo 4

        select_arq_1_2 = true;
        
        select_arq_3_4 = true;

     
        try{

            rafArq1.seek(0);
            rafArq2.seek(0);
            rafArq3.seek(0);
            rafArq4.seek(0);
            System.out.println("passou por aqui");


            while(m < 5000){

                if(grupo_arq){

                    if(select_arq_1_2){
                        //escrever no arq1
                        try{

                            while(aux_reg1 < m && aux_reg2 < m){
                                reg_1 = new byte[rafArq3.readInt()];
                                rafArq3.read(reg_1);
                                registro1.fromByteArray(reg_1);
                                reg_2 = new byte[rafArq4.readInt()];
                                rafArq4.read(reg_2);
                                registro2.fromByteArray(reg_2);
                                
                                if(registro1.getId() < registro2.getId()){
                                    
                                    rafArq1.writeInt(registro1.toByteArray().length);
                                    rafArq1.write(registro1.toByteArray());
                                    aux_reg1++;
                                    rafArq4.seek((rafArq4.getFilePointer()-(long)(4 +reg_2.length)));
                                    
                                }else{
                                    
                                    rafArq1.writeInt(registro2.toByteArray().length);
                                    rafArq1.write(registro2.toByteArray());
                                    aux_reg2++;
                                    rafArq3.seek((rafArq3.getFilePointer()-(long)(4 +reg_1.length)));
                                    
                                }
                                
                            }
                            
                            if(aux_reg1 == m){
                                for(int i = aux_reg2; i < m; i++){
                                    
                                    reg_2 = new byte[rafArq4.readInt()];
                                    rafArq4.read(reg_2);
                                    registro2.fromByteArray(reg_2);
                                    rafArq1.writeInt(registro2.toByteArray().length);
                                    rafArq1.write(registro2.toByteArray());
                                    
                                }
                            }else{
                                for(int i = aux_reg1; i < m; i++){
                                    
                                    reg_1 = new byte[rafArq3.readInt()];
                                    rafArq3.read(reg_1);
                                    registro1.fromByteArray(reg_1);
                                    rafArq1.writeInt(registro1.toByteArray().length);
                                    rafArq1.write(registro1.toByteArray());
                                    
                                }
                                
                            }
                            
                        }catch(EOFException e){
                            if(rafArq3.read() == -1){
                                try{
                                    while(true){
                                        reg_2 = new byte[rafArq4.readInt()];
                                        rafArq4.read(reg_2);
                                        registro2.fromByteArray(reg_2);
                                        rafArq1.writeInt(registro2.toByteArray().length);
                                        rafArq1.write(registro2.toByteArray());
                                    }
                                }catch(EOFException f){
                                    m*=2;
                                    grupo_arq = false;
                                    ordenacao_memsecundaria();
                                    select_archive = 1;
                                }catch(IOException f){
                                    m*=2;
                                    grupo_arq = false;
                                    ordenacao_memsecundaria();
                                    select_archive = 1;
                                }
                            }

                            if(rafArq4.read() == -1){
                                try{
                                    rafArq3.seek(rafArq3.getFilePointer()-1);
                                    rafArq1.writeInt(registro1.toByteArray().length);
                                    rafArq1.write(registro1.toByteArray());
                                    
                                    while(true){
                                        reg_1 = new byte[rafArq3.readInt()];
                                        rafArq3.read(reg_1);
                                        registro1.fromByteArray(reg_1);
                                        rafArq1.writeInt(registro1.toByteArray().length);
                                        rafArq1.write(registro1.toByteArray());
                                    }
                                }catch(EOFException f){
                                    m*=2;
                                    grupo_arq = false;
                                    ordenacao_memsecundaria();
                                    select_archive = 1;
                                }catch(IOException f){
                                    m*=2;
                                    grupo_arq = false;
                                    ordenacao_memsecundaria();
                                    select_archive = 1;
                                }
                            }
                            
                        }
                            
                            
                    }else{
                    //escrever no arq2
                        try{

                            while(aux_reg1 < m && aux_reg2 < m){
                                reg_1 = new byte[rafArq3.readInt()];
                                rafArq3.read(reg_1);
                                registro1.fromByteArray(reg_1);
                                reg_2 = new byte[rafArq4.readInt()];
                                rafArq4.read(reg_2);
                                registro2.fromByteArray(reg_2);
                                
                                if(registro1.getId() < registro2.getId()){
                                    
                                    rafArq2.writeInt(registro1.toByteArray().length);
                                    rafArq2.write(registro1.toByteArray());
                                    aux_reg1++;
                                    rafArq4.seek((rafArq4.getFilePointer()-(long)(4 +reg_2.length)));
                                    
                                }else{
                                    
                                    rafArq2.writeInt(registro2.toByteArray().length);
                                    rafArq2.write(registro2.toByteArray());
                                    aux_reg2++;
                                    rafArq3.seek((rafArq3.getFilePointer()-(long)(4 +reg_1.length)));
                                    
                                }
                                
                            }
                            
                            if(aux_reg1 == m){
                                for(int i = aux_reg2; i < m; i++){
                                    
                                    reg_2 = new byte[rafArq4.readInt()];
                                    rafArq4.read(reg_2);
                                    registro2.fromByteArray(reg_2);
                                    rafArq2.writeInt(registro2.toByteArray().length);
                                    rafArq2.write(registro2.toByteArray());
                                    
                                }
                            }else{
                                for(int i = aux_reg1; i < m; i++){
                                    
                                    reg_1 = new byte[rafArq3.readInt()];
                                    rafArq3.read(reg_1);
                                    registro1.fromByteArray(reg_1);
                                    rafArq2.writeInt(registro1.toByteArray().length);
                                    rafArq2.write(registro1.toByteArray());
                                    
                                }
                                
                            }
                            
                        }catch(EOFException e){
                            if(rafArq3.read() == -1){
                                try{
                                    while(true){
                                        reg_2 = new byte[rafArq4.readInt()];
                                        rafArq4.read(reg_2);
                                        registro2.fromByteArray(reg_2);
                                        rafArq2.writeInt(registro2.toByteArray().length);
                                        rafArq2.write(registro2.toByteArray());
                                    }
                                }catch(EOFException f){
                                    m*=2;
                                    grupo_arq = false;
                                    ordenacao_memsecundaria();
                                    select_archive = 2;
                                }catch(IOException f){
                                    m*=2;
                                    grupo_arq = false;
                                    ordenacao_memsecundaria();
                                    select_archive = 2;
                                }
                            }
                            if(rafArq4.read() == -1){
                                try{
                                    rafArq3.seek(rafArq1.getFilePointer()-1);
                                    rafArq2.writeInt(registro1.toByteArray().length);
                                    rafArq2.write(registro1.toByteArray());
                                    
                                    while(true){
                                        reg_1 = new byte[rafArq3.readInt()];
                                        rafArq3.read(reg_1);
                                        registro1.fromByteArray(reg_1);
                                        rafArq2.writeInt(registro1.toByteArray().length);
                                        rafArq2.write(registro1.toByteArray());
                                    }
                                }catch(EOFException f){
                                    m*=2;
                                    grupo_arq = false;
                                    ordenacao_memsecundaria();
                                    select_archive = 2;
                                }catch(IOException f){
                                    m*=2;
                                    grupo_arq = false;
                                    ordenacao_memsecundaria();
                                    select_archive = 2;
                                }
                            }
                            

                        }
                            
                    }
                }else{

                    if(select_arq_3_4){
                        
                        //escrever no arq3
                        
                        try{
                            while(aux_reg1 < m && aux_reg2 < m){
                                reg_1 = new byte[rafArq1.readInt()];
                                rafArq1.read(reg_1);
                                registro1.fromByteArray(reg_1);
                                reg_2 = new byte[rafArq2.readInt()];
                                rafArq2.read(reg_2);
                                registro2.fromByteArray(reg_2);
                                
                                if(registro1.getId() < registro2.getId()){

                                    System.out.println("3_a");
                                    
                                    rafArq3.writeInt(registro1.toByteArray().length);
                                    rafArq3.write(registro1.toByteArray());
                                    aux_reg1++;
                                    rafArq2.seek((rafArq2.getFilePointer()-(long)(4 +reg_2.length)));
                                    
                                }else{
                                   
                                    System.out.println("3_b");

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
                        }catch(EOFException e){
                            System.out.println("excep3");

                            if(rafArq1.read() == -1){
                                try{
                                    while(true){
                                        reg_2 = new byte[rafArq2.readInt()];
                                        rafArq2.read(reg_2);
                                        registro2.fromByteArray(reg_2);
                                        rafArq3.writeInt(registro2.toByteArray().length);
                                        rafArq3.write(registro2.toByteArray());
                                    }
                                }catch(EOFException f){
                                    m*=2;
                                    grupo_arq = true;
                                    ordenacao_memsecundaria();
                                    select_archive = 3;
                                }catch(IOException f){
                                    m*=2;
                                    grupo_arq = true;
                                    ordenacao_memsecundaria();
                                    select_archive = 3;
                                }
                            }
                            if(rafArq2.read() == -1){
                                try{
                                    rafArq1.seek(rafArq1.getFilePointer()-1);
                                    rafArq3.writeInt(registro1.toByteArray().length);
                                    rafArq3.write(registro1.toByteArray());
                                    
                                    while(true){
                                        reg_1 = new byte[rafArq1.readInt()];
                                        rafArq1.read(reg_1);
                                        registro1.fromByteArray(reg_1);
                                        rafArq3.writeInt(registro1.toByteArray().length);
                                        rafArq3.write(registro1.toByteArray());
                                    }
                                }catch(EOFException f){
                                    m*=2;
                                    grupo_arq = true;
                                    ordenacao_memsecundaria();
                                    select_archive = 3;
                                }catch(IOException f){
                                    m*=2;
                                    grupo_arq = true;
                                    ordenacao_memsecundaria();
                                    select_archive = 3;
                                }
                                
                            }
                            
                        }
                        select_arq_3_4 = false;
                    }else{

                        //escrever no arq4
                        try{

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
                        }catch(EOFException e){
                            System.out.println("excep4");
                            System.out.println(rafArq1.getFilePointer());
                            if(rafArq1.read() == -1){
                                try{
                                    System.out.println("Moreira");
                                    while(true){
                                        reg_2 = new byte[rafArq2.readInt()];
                                        rafArq2.read(reg_2);
                                        registro2.fromByteArray(reg_2);
                                        rafArq4.writeInt(registro2.toByteArray().length);
                                        rafArq4.write(registro2.toByteArray());
                                    }
                                }catch(EOFException f){
                                    m*=2;
                                    grupo_arq = true;
                                    ordenacao_memsecundaria();
                                    select_archive = 4;
                                }catch(IOException f){
                                    m*=2;
                                    grupo_arq = true;
                                    ordenacao_memsecundaria();
                                    select_archive = 4;
                                }
                            if(rafArq2.read() == -1){
                                try{
                                    rafArq1.seek(rafArq1.getFilePointer()-1);
                                    System.out.println(rafArq1.getFilePointer());
                                    rafArq4.writeInt(registro1.toByteArray().length);
                                    rafArq4.write(registro1.toByteArray());
                                    
                                    while(rafArq1.read() != -1){
                                        //System.out.println("lucas");
                                        rafArq1.seek(rafArq1.getFilePointer()-1);
                                        reg_1 = new byte[rafArq1.readInt()];
                                        rafArq1.read(reg_1);
                                        registro1.fromByteArray(reg_1);
                                        //System.out.println(registro1.getId());
                                        rafArq4.writeInt(registro1.toByteArray().length);
                                        rafArq4.write(registro1.toByteArray());
                                    }
                                }catch(EOFException f){
                                    System.out.println("aqui");
                                    m*=2;
                                    grupo_arq = true;
                                    ordenacao_memsecundaria();
                                    select_archive = 4;
                                }catch(IOException f){
                                    System.out.println("aqui");
                                    m*=2;
                                    grupo_arq = true;
                                    ordenacao_memsecundaria();
                                    select_archive = 4;
                                }
                                System.out.println(rafArq1.getFilePointer());
                                System.out.println("saiu");
                            }
                        }
                        select_arq_3_4 = true;
                    }
                }
            }
            System.out.println(select_archive);



            try{
                RandomAccessFile raf_ordenado = new RandomAccessFile("../archive//ordenacao//arq1.db", "rw");

                switch(select_archive){
                    case 1: 
                        try{
                            while(true){

                                rafArq1.seek(0);
                                reg_1 = new byte[rafArq1.readInt()];
                                rafArq1.read(reg_1);
                                registro1.fromByteArray(reg_1);
                                raf_ordenado.writeInt(registro1.toByteArray().length);
                                raf_ordenado.write(registro1.toByteArray());
                            }
                        }catch(IOException e){}                 
    
                    break;
                    case 2:
                        try{
                            while(true){

                                rafArq2.seek(0);
                                reg_1 = new byte[rafArq2.readInt()];
                                rafArq2.read(reg_1);
                                registro1.fromByteArray(reg_1);
                                raf_ordenado.writeInt(registro1.toByteArray().length);
                                raf_ordenado.write(registro1.toByteArray());
                            }
                        }catch(IOException e){}
    
                    break;
                    case 3:
                        try{
                            while(true){

                                rafArq3.seek(0);
                                reg_1 = new byte[rafArq3.readInt()];
                                rafArq3.read(reg_1);
                                registro1.fromByteArray(reg_1);
                                raf_ordenado.writeInt(registro1.toByteArray().length);
                                raf_ordenado.write(registro1.toByteArray());
                            }
                        }catch(IOException e){}

                    break;
                    case 4:
                        try{
                            while(true){

                                rafArq4.seek(0);
                                reg_1 = new byte[rafArq4.readInt()];
                                rafArq4.read(reg_1);
                                registro1.fromByteArray(reg_1);
                                raf_ordenado.writeInt(registro1.toByteArray().length);
                                raf_ordenado.write(registro1.toByteArray());
                            }
                        }catch(IOException e){}
                    break;

                }

            }catch(IOException e){
                System.out.println("a");
            }

            
            } 
            
        }catch(IOException e){
        }
    }


    public void printArq3(){
        Spotify aux = new Spotify();
        byte[] a;
        try{
        rafArq3.seek(0);
        for(int i = 0; i < 100;i++){

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
        byte[] c;
        try{
            rafArq1.seek(0);
            rafArq2.seek(0);
        }catch(IOException e){}
        try{

            while(true){
                try{
                c = new byte[rafArq2.readInt()];
                rafArq2.read(c);
                

                b = new byte[rafArq1.readInt()];
                rafArq1.read(b);
                Spotify a = new Spotify();
                a.fromByteArray(c);
                System.out.println(a.getId());
                //}catch(IOException e){
                  //  System.out.println("lucas");
                }catch(EOFException e){
                    if(rafArq2.read() == -1){
                        System.out.println("talissa");
                    }
                    if(rafArq1.read() == -1){
                        System.out.println("almeida");
                        
                        Spotify a = new Spotify();
                        //a.fromByteArray();
                        System.out.println(a.getId());
                        raf.seek(0);
                        System.out.println(raf.readShort());
                    }

                    break;
                }
            }
        }catch(IOException e){
            System.out.println("moreira");
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