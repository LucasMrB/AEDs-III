package Presentation;

import Model.Spotify;
import java.io.IOException;

public class ParseByteSpotify {
    public static Spotify toSpotify(byte vetor[]){
        Spotify retorno = new Spotify();
        try{
            retorno.fromByteArray(vetor);
        }catch(IOException e){
            System.out.println("ERROR: byte to spotify");
        }
        return retorno;
    }
}
