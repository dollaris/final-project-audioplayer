package hi.verkefni.mediaplayer.vinnsla;

public class Song {

    private String artistName;
    private String songName;
    private double duration;
    private String pathSong;
    private String pathImage;

    public Song(String artistName, String songName, double duration, String pathSong, String pathImage) {
        this.artistName = artistName;
        this.songName = songName;
        this.duration = duration;
        this.pathSong = pathSong;
        this.pathImage = pathImage;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public String getPathSong() {
        return pathSong;
    }

    public void setPathSong(String pathSong) {
        this.pathSong = pathSong;
    }

    public String getPathImage() {
        return pathImage;
    }

    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
    }

    @Override
    public String toString() {
        return artistName + " - " + songName;
    }
}
