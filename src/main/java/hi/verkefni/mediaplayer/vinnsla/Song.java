package hi.verkefni.mediaplayer.vinnsla;

public class Song {

    private String title;
    private String artist;
    private String album;
    private String Genre;
    private String imgName;
    private double songLength;
    private String path;

    public Song(String title, String artist, String album, String Genre, String imgName, double songLength, String path) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.Genre = Genre;
        this.songLength = songLength;
        this.imgName = imgName;
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getGenre() {
        return Genre;
    }

    public void setGenre(String genre) {
        Genre = genre;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public double getSongLength() {
        return songLength;
    }

    public void setSongLength(double songLength) {
        this.songLength = songLength;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
