package View;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;

public class InputDB {
    private BufferedReader arq_csv;
    public InputDB(){
        try{
            arq_csv = new BufferedReader(new FileReader(new File("../archive//top_10000_1960-now.csv")));
            arq_csv.readLine();
        }catch(Exception e){}
    }

    public String readLine()throws Exception{
        return arq_csv.readLine();
    }

    public LinkedList<String> getLines(){
        LinkedList<String> list = new LinkedList<String>();
        for(int i = 0;i < 9998;i++){
            try{
                list.add(readLine());

            }catch(Exception e){}
        }
        return list;
    }

    public void closeCsv(){
        try{
            arq_csv.close();
        }catch(Exception e){}
    }
}
