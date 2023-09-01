package Model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import Presentation.ParseSpotify;

public class Spotify{
    public short id; //chave primaria
    private String track_uri;  // string fixa = 36
    private String music_name; // string flex   maior = 150
    private String singer;     // string var = maior = 80
    private Data date;     // campo data
    private int track_duration;// inteiro
    private String artist_genres;//string flex  maior = 300


    //CONSTRUTORES
    public Spotify(){}

    public Spotify( String track_uri, String music_name, String singer, Data date, int  track_duration, String artist_genres, Short id){
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
    public Data getDate(){return date;}
    public int getTrackDuration(){return track_duration;}
    public String getArtistGenres(){return artist_genres;}
    public Short getId(){ return id;}

    //sets
    public void setTrackUri(String track_uri){this.track_uri = track_uri;}
    public void setMusicName(String music_name){this.music_name = music_name;}
    public void setSinger(String singer){this.singer = singer;}
    public void setDate(Data date){this.date = date;}
    public void setTrackDuration(int track_duration){this.track_duration = track_duration;}
    public void setArtistGenres(String artist_genres){this.artist_genres = artist_genres;}


    //escrever dados do cantor
    public void write_data(){
        System.out.println(track_uri+"\t"+music_name+"\t"+singer+"\t"+"\t"+date.getAno()+"\t"+date.getMes()+"\t"+date.getDia()+"\t"+track_duration+"\t"+artist_genres);
    }
    
    // ORDEM: TAMANHO ARQUIVO || ID || NOME DA MUSICA || CANTOR || DATA || TRACK DURATION || GENERO MUSICAL || URI

    //obs data = ano,mes,dia    
    
    //transforma classe em array de bytes
    public byte[] toByteArray() throws IOException{

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeShort(id);
        dos.writeUTF(music_name); 
        dos.writeUTF(singer);
        dos.writeInt(date.getAno());
        dos.writeInt(date.getMes());
        dos.writeInt(date.getDia());
        dos.writeInt(track_duration);
        dos.writeUTF(artist_genres);
        dos.writeUTF(track_uri);

        return baos.toByteArray();
    }

    //lê um array de bytes e salva no objeto da classe
    public void fromByteArray(byte[] ba) throws IOException{

        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);

        id = dis.readShort();
        music_name = dis.readUTF();
        singer = dis.readUTF();
        int aux = dis.readInt();
        int aux1 = dis.readInt();
        int aux2 = dis.readInt();
        track_duration = dis.readInt();
        artist_genres = dis.readUTF();
        track_uri = dis.readUTF();

        date = ParseSpotify.setDate(aux,aux1,aux2);

    }
    

}