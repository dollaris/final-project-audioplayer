package hi.verkefni.mediaplayer.vinnsla;

import javafx.collections.ObservableList;

import java.util.List;

public class Genre {

    private String name;
    private ObservableList<Song> songs;
    private int index = 0;

    public Genre(String name, ObservableList<Song> songs) {
        this.name = name;
        this.songs = songs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(ObservableList<Song> songs) {
        this.songs = songs;
    }

}
