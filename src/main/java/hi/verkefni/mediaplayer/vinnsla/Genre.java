package hi.verkefni.mediaplayer.vinnsla;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Genre {
    private ObservableList<Song> genre;
    private int index = 0;
    private String genreFile;

    public Genre(String genreFile) throws IOException {
        this.genreFile = genreFile;
        readSong();
    }

    public void readSong() throws IOException  {
        genre = FXCollections.observableArrayList();
        Path paths = Paths.get(genreFile);
        BufferedReader br = Files.newBufferedReader(paths, StandardCharsets.UTF_8);
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split("\\s+");
            String artist = parts[0].trim();
            String songName = parts[1].trim();
            double length = Double.parseDouble(parts[2].trim());
            String songPath = parts[3].trim();
            String imgPath = parts[4].trim();
            Song song = new Song(artist, songName, length, songPath, imgPath);
            genre.add(song);
        }
        br.close();
    }

    public void next() {
        index = ++index % genre.size();
    }
    public ObservableList<Song> getSongs() {
        return genre;
    }
    public void setIndex(int selectedIndex) {
        index = selectedIndex;
    }

    public int getIndex() {
        return index;
    }
}
