package Model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Calendar;
import java.io.IOException;

public class Spotify{
    public short id; //chave primaria
    private String track_uri;  // string fixa = 36
    private String music_name; // string flex   maior = 150
    private String singer;     // string var = maior = 80
    private Calendar date;     // campo data
    private int track_duration;// inteiro
    private String artist_genres;//string flex  maior = 300


    //CONSTRUTORES
    public Spotify(){}

    public Spotify( String track_uri, String music_name, String singer, Calendar date, int  track_duration, String artist_genres, Short id){
        this.track_uri = track_uri;
        this.music_name = music_name;
        this.singer = singer;
        this.date = date;
        this.track_duration = track_duration;
        this.artist_genres = artist_genres;
        this.id = id;
    }

    //gets
    public String getTrackUri(){return track_uri;}
    public String getMusicName(){return music_name;}
    public String getSinger(){return singer;}
    public Calendar getDate(){return date;}
    public int getTrackDuration(){return track_duration;}
    public String getArtistGenres(){return artist_genres;}
    public int getId(){ return id;}

    //escrever dados do cantor
    public void write_data(){
        System.out.println(track_uri+"\t"+music_name+"\t"+singer+"\t"+"\t"+date.get(Calendar.YEAR)+"\t"+date.get(Calendar.DAY_OF_MONTH)+"\t"+date.get(Calendar.MONTH)+"\t"+track_duration+"\t"+artist_genres);
    }

    //printar data
    public void printDate(){
        System.out.println(date.get(Calendar.YEAR));
        System.out.println(date.get(Calendar.DAY_OF_MONTH));
        System.out.println(date.get(Calendar.MONTH));
    }

    
    // ORDEM: TAMANHO ARQUIVO || ID || NOME DA MUSICA || CANTOR || DATA || TRACK DURATION || GENERO MUSICAL || URI
    
    //transforma classe em array de bytes
    public byte[] toByteArray() throws IOException{

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeShort(getId());
        dos.writeUTF(getMusicName()); 
        dos.writeUTF(getSinger());
        getDate();
        dos.writeInt(Calendar.YEAR);
        dos.writeInt(Calendar.MONTH);
        dos.writeInt(Calendar.DAY_OF_MONTH);
        dos.writeInt(getTrackDuration());
        dos.writeUTF(getArtistGenres());
        dos.writeUTF(getTrackUri());

        return baos.toByteArray();
    }

    //lê um array de bytes e salva no objeto da classe
    public void fromByteArray(byte[] ba) throws IOException{

        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);

        id = dis.readShort();
        music_name = dis.readUTF();
        singer = dis.readUTF();
        date.set(Calendar.YEAR,dis.readInt());
        date.set(Calendar.MONTH,dis.readInt());
        date.set(Calendar.DAY_OF_MONTH,dis.readInt());
        track_duration = dis.readInt();
        artist_genres = dis.readUTF();
        track_uri = dis.readUTF();

    }

}