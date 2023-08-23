import View.*;
import Presentation.*;
import Model.*;

import java.io.IOException;
import java.util.LinkedList;

public class Main {
    public static void main(String[] args){
        InputDB inputdb = new InputDB();
        LinkedList<String> list = inputdb.getLines();

        LinkedList<Spotify> spotify_list = new LinkedList<Spotify>();

        list.forEach(data -> {
            String[] aux = ParseSpotify.parse(data);
            spotify_list.add(new Spotify(aux[0],aux[1],aux[3],ParseSpotify.set_date(aux[8]),Integer.parseInt(aux[12]),aux[19], Short.parseShort(aux[20])));

        });

        
        try{
            WriteReadArchive.WriteArchive(spotify_list);
            Spotify tmp = new Spotify();
            WriteReadArchive.ReadArchive(tmp);
        }catch(Exception e){}
    }  
}