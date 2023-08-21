package Presentation;

import java.util.Calendar;

public class ParseSpotify {

    // parse string -> vetor atributo
    public static String[] parse(String line){
        line = changeComma(line);
        String aux[] = line.split(",");
        aux = removeAspas(aux);
        System.out.println(aux[0]);

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
    public static Calendar set_date(String line){
        Calendar date = Calendar.getInstance();
        if(line.length() == 0){

        }else if(line.length() == 4){
            date.set(Calendar.YEAR, Integer.parseInt(line.substring(0, 4)));
        }else if(line.length()==7){
            date.set(Calendar.YEAR, Integer.parseInt(line.substring(0, 4)));
            date.set(Calendar.MONTH, Integer.parseInt(line.substring(5,7)));
        }else{
            date.set(Calendar.YEAR, Integer.parseInt(line.substring(0, 4)));
            date.set(Calendar.MONTH, Integer.parseInt(line.substring(5,7)));
            date.set(Calendar.DAY_OF_MONTH,Integer.parseInt(line.substring(8)));
        }
        return date;
    }
}
