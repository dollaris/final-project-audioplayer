package hi.verkefni.mediaplayer.vidmot;

import hi.verkefni.mediaplayer.vinnsla.Song;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;

import java.io.IOException;

public class GenreController {
    @FXML
    private ListView<Song> fxListView;
    @FXML
    private Image fxImage;

    public void onExplore() throws IOException {
        ViewSwitcher.switchTo(View.MAIN, true);
    }

}
