package hi.verkefni.mediaplayer.vinnsla;

import java.util.List;

public class Artist {
    private String name;
    private String imgName;
    private List<Song> songs;

    public Artist(String name, String imgName, List<Song> songs) {
        this.name = name;
        this.imgName = imgName;
        this.songs = songs;
    }


}
