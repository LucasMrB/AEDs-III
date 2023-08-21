package Model;

import java.util.Calendar;

public class Spotify{
    private static short id = 0; //chave primaria
    private String track_uri;  // string fixa = 36
    private String music_name; // string flex   maior = 150
    private String singer;     // string var = maior = 80
    private Calendar date;     // campo data
    private int track_duration;// inteiro
    private String artist_genres;//string flex  maior = 300


    //CONSTRUTORES
    public Spotify(){}

    public Spotify( String music_name, String singer, String album_artist_uri, Calendar date, int  track_duration, String artist_genres){
        this.track_uri = album_artist_uri;
        this.music_name = music_name;
        this.singer = singer;
        this.date = date;
        this.track_duration = track_duration;
        this.artist_genres = artist_genres;
    }

    //gets
    public String getTrackUri(){return track_uri;}
    public String getMusicName(){return music_name;}
    public String getSinger(){return singer;}
    public Calendar getDate(){return date;}
    public int getTrackDuration(){return track_duration;}
    public String getArtistGenres(){return artist_genres;}
    public int getId(){return id;}

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
}