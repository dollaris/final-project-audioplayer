package hi.verkefni.mediaplayer.vidmot;

import hi.verkefni.mediaplayer.vinnsla.Genre;
import hi.verkefni.mediaplayer.vinnsla.Song;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DialogController {
    @FXML
    private ChoiceBox<String> fxChoiceBox;
    @FXML
    private javafx.scene.control.ListView<Song> fxSongsAdd;

    @FXML
    public void initialize() {
        try {
            List<String> genreNames = new ArrayList<>();
            for (Genre genre : getGenres()) {
                genreNames.add(genre.getGenreName());
            }
            fxChoiceBox.getItems().addAll(genreNames);
        } catch (IOException e) {
            e.printStackTrace();
        }

        fxChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                try {
                    Genre selectedGenre = getGenres().get(fxChoiceBox.getItems().indexOf(newValue));
                    ObservableList<Song> songs = selectedGenre.getSongs();
                    fxSongsAdd.setItems(songs);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private List<Genre> getGenres() throws IOException {
        return List.of(new Genre("classical.txt"), new Genre("hiphop.txt"),
                new Genre("pop.txt"), new Genre("rock.txt"));
    }
}
