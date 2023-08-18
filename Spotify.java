import java.util.Calendar;

class Spotify{
    private static int id = 0; // colocar p ele incrementar a cada nova classe criada
    private String music_name;
    private String singer;
    private String track_uri; 
    private Calendar date;
    private int track_duration;
    private String artist_genres;

    Spotify(){}
    Spotify( String music_name, String singer, String album_artist_uri, Calendar date, int  track_duration, String artist_genres){
        this.track_uri = album_artist_uri;
        this.music_name = music_name;
        this.singer = singer;
        this.date = date;
        this.track_duration = track_duration;
        this.artist_genres = artist_genres;
    }

    public String getTrackUri(){return track_uri;}
    public String getMusicName(){return music_name;}
    public String getSinger(){return singer;}
    public Calendar getDate(){return date;}
    public int getTrackDuration(){return track_duration;}
    public String getArtistGenres(){return artist_genres;}
    public int getId(){return id;}

    public void write_data(){
        System.out.println(track_uri+"\t"+music_name+"\t"+singer+"\t"+"\t"+date.get(Calendar.YEAR)+"\t"+date.get(Calendar.DAY_OF_MONTH)+"\t"+date.get(Calendar.MONTH)+"\t"+track_duration+"\t"+artist_genres);
    }

    private String changeComma(String line){
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
    private String[] removeAspas(String aux[]){
        for(int i = 0; i <aux.length;i++)
            aux[i] = aux[i].substring(1, aux[i].length()-1);
        
        return aux;
    }

    public void read_csv(String line){
        System.out.println("PORRA");
        line = changeComma(line);
        String aux[] = line.split(",");
        aux = removeAspas(aux);
        track_uri = aux[0];
        music_name = aux[1];
        singer = aux[7];
        //System.out.println(aux[8]);
        date = set_date(aux[8]);
        track_duration = Integer.parseInt(aux[12]);
        artist_genres = aux[19];
    }

    private Calendar set_date(String line){
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

    public void printDate(){
        System.out.println(date.get(Calendar.YEAR));
        System.out.println(date.get(Calendar.DAY_OF_MONTH));
        System.out.println(date.get(Calendar.MONTH));
    }
}