package hi.verkefni.mediaplayer.vinnsla;

import java.io.IOException;

public class Genres {
    private static int index;
    private static final int NUMBER_OF_GENRES = 4;

    private static Genre[] genres = new Genre[NUMBER_OF_GENRES];
    private static final String[] fileNames = new String[]{"classical.txt", "hiphop.txt", "pop.txt", "rock.txt"};

    public Genres() throws IOException {
        index = 0;
        for (int i = 0; i < NUMBER_OF_GENRES; i++) {
            try {
                genres[i] = new Genre(fileNames[i]);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    public static Genre getGenre() {
        return genres[index];
    }

    public void setGenreIndex(int index) {
        Genres.index = index;
    }
}
