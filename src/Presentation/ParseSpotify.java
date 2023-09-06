package Presentation;

import java.io.IOException;

import Model.Spotify;
import Model.Data;

//classe para auxiliar a classe Spotify


public class ParseSpotify {
    // statico, id sempre irá incrementar após ler uma nova linha(linha 20) lastID++
    private static short lastId = 1;

    // parse string -> vetor atributo
    public static String[] parse(String line){
        line = changeComma(line);
        String aux[] = line.split(",");
        aux = removeAspas(aux);
        aux[20] = String.valueOf(lastId++);

        return aux;
    }

    //auxiliar para separar string do csv
    public static String changeComma(String line){
        int num = 1;
        String line2 = "";
        for(int i = 0; i < line.length();i++){
            if(line.charAt(i) == '\"')
                num *= -1;
            if(line.charAt(i) == ',' && num == -1){
                line2+=';';
            }else{
                line2+=line.charAt(i);
            }

        }
        return line2;
    }

    //remover aspas das string
    public static String[] removeAspas(String aux[]){
        for(int i = 0; i <aux.length;i++)
            aux[i] = aux[i].substring(1, aux[i].length()-1);
        
        return aux;
    }

    //aux, setar data
    public static Data set_date(String line){
        Data date;
        if(line.length() == 0){
            // sem informação de data
            date = new Data();
        }else if(line.length() == 4){
            // só ano
            date = new Data(Integer.parseInt(line.substring(0, 4)));
        }else if(line.length()==7){
            // ano e mes
            date = new Data(Integer.parseInt(line.substring(5,7)), Integer.parseInt(line.substring(0, 4)));
        }else{
            // dia, mes e ano
            date = new Data(Integer.parseInt(line.substring(8)), Integer.parseInt(line.substring(5,7)), Integer.parseInt(line.substring(0, 4)));
        }
        return date;
    }

    public static Data setDate(int ano, int mes, int dia){
        return new Data(dia,mes,ano);
    }

    //BytetoSpotify
    public static Spotify ByteToSpotify(byte vetor[]){
        Spotify retorno = new Spotify();
        try{
            retorno.fromByteArray(vetor);
        }catch(IOException e){
            System.out.println("ERROR: byte to spotify");
        }
        return retorno;
    }

    //retorna o ultimo id criado
    public static short getLastId(){
        int a = (int)lastId;
        a--;
        return (short)a;
        //Converter.ToInt16();
    }

}


