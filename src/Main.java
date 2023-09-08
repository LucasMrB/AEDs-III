import View.*;
import Presentation.*;
import Model.*;
import java.io.IOException;
import java.util.LinkedList;

public class Main {
    public static void main(String[] args){
        InputDB inputdb = new InputDB();
        //salvar todas as linhas do arquivo em uma linked list
        LinkedList<String> list = inputdb.getLines();

        LinkedList<Spotify> spotify_list = new LinkedList<Spotify>();

        //transformar todas as linhas do arquivo em uma lista de obj spotify
        list.forEach(data -> {
            String[] aux = ParseSpotify.parse(data);
            spotify_list.add(new Spotify(aux[0],aux[1],aux[3],ParseSpotify.set_date(aux[8]),Integer.parseInt(aux[12]),aux[19], Short.parseShort(aux[20])));

        });
        
        Crud crud = new Crud();
        crud.create(spotify_list);
        short a = ParseSpotify.getLastId();
        
        IntercalacaoBalanceadaC lucas = new IntercalacaoBalanceadaC();
        lucas.writeArchive_Distribuicao();
        //lucas.ordenacao_memsecundaria();
        lucas.intercalao();
        //lucas.teste();
        lucas.printArq3();
        //lucas.printarq();
        
        try{
            ParseSpotify.ByteToSpotify(crud.read(a));//.write_data();

            
            /*
            Spotify aux = ParseSpotify.ByteToSpotify(crud.read(a));
             aux.write_data();
             crud.update(a, "LUCAS");
             aux = ParseSpotify.ByteToSpotify(crud.read(a));
             aux.write_data();
             */

        }catch(IOException e){
            System.out.println("EROR IOEXC");
        }catch(Exception e){
            System.out.println("ERROR EXCEP");
        }
        
    }  
}