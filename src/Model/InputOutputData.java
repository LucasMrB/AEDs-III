package Model;

import java.io.*;

public class InputOutputData {
    private FileOutputStream arq;
    private DataOutputStream dos;
    private FileInputStream arq2;
    private DataInputStream dis;
    
    //construtor
    InputOutputData(String line){
        try{
            arq = new FileOutputStream(line);
            dos = new DataOutputStream(arq);
            arq2 = new FileInputStream(line);
            dis = new DataInputStream(arq2);
        }catch(Exception e){}
    }

    //gets
    public DataInputStream get_input_stream(){ return dis; }
    public DataOutputStream get_output_stream(){ return dos; }

    //open output
    public void open_Output(String line){
        try{
            arq = new FileOutputStream(line);
            dos = new DataOutputStream(arq);
        }catch(FileNotFoundException e){}
    }

    //open input
    public void open_Input(String line){
        try{
            arq2 = new FileInputStream(line);
            dis = new DataInputStream(arq2);

        }catch(FileNotFoundException e){}
    }

    //close output
    public void close_Output(){
        try{
            arq.close();
            dos.close();
        }catch(Exception e){}
    }

    //close input
    public void close_Input(){
        try{
            arq2.close();
            dis.close();
        }catch(Exception e){}
    }
}