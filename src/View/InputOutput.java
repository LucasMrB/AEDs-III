package View;

import java.io.DataOutputStream;    
import java.io.IOException;
class InputOutput {

    //escrever int em bytes
    public void write(int value, DataOutputStream dos){
        try{
            
            dos.writeInt(value);
        }catch(IOException e){}
    }

    //escrever short em bytes
    public void write(short value, DataOutputStream dos){
        try{

            dos.writeShort(value);
        }catch(IOException e){}
    }

    //escrever float em bytes
    public void write(float value, DataOutputStream dos){
        try{

            dos.writeFloat(value);
        }catch(IOException e){}
    }

    //escrever string em bytes
    public void write(String word, DataOutputStream dos){
        try{
            dos.writeUTF(word);
        }catch(IOException e){}
    }

}

