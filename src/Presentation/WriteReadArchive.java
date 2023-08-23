package Presentation;

import Model.Spotify;
import java.io.RandomAccessFile;
import java.util.LinkedList;


public class WriteReadArchive {
    public static void WriteArchive(LinkedList<Spotify> spotify_list) throws Exception {

        RandomAccessFile raf = new RandomAccessFile("../archive//musicas.db", "rw");
        spotify_list.forEach(data->{
            try{
                raf.writeInt(data.toByteArray().length);
                raf.write(data.toByteArray());

            }catch(Exception e){}

        });

        raf.close();
        raf.close();
    }

    public static void ReadArchive(Spotify tmp)throws Exception{

        RandomAccessFile raf = new RandomAccessFile("../archive//musicas.db", "r");

        System.out.println(raf.getFilePointer());

        int len = raf.readInt();
        byte[] bytesV = new byte[len];

        raf.read(bytesV);

        for(int i= 0;i<151;i++){
            System.out.println(bytesV[i]);
        }

        tmp.fromByteArray(bytesV);

        raf.close();

    }

    public static Spotify ReadArchive()throws Exception{

        RandomAccessFile raf = new RandomAccessFile("../archive//musicas.db", "r");
        Spotify tmp = new Spotify();
        raf.seek(0);

        int len = raf.readInt();
        byte[] bytesV = new byte[len];
        raf.read(bytesV);

        tmp.fromByteArray(bytesV);
        
        raf.close();
        return tmp;
    }

    
}
