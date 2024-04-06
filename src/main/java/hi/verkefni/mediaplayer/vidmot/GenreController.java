package hi.verkefni.mediaplayer.vidmot;

import hi.verkefni.mediaplayer.vinnsla.Genre;
import hi.verkefni.mediaplayer.vinnsla.Genres;
import hi.verkefni.mediaplayer.vinnsla.Song;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class GenreController {
    @FXML
    private ListView<Song> fxListView;
    @FXML
    private ImageView fxImage;
    @FXML
    private ImageView fxImagePlayPause;
    private Genre genre;
    private MediaPlayer player;
    private Song currentSong;


    public void initialize() {
        genre = Genres.getGenre();
        fxListView.setItems(genre.getSongs());
        fxListView.getSelectionModel().selectIndices(genre.getIndex());
        fxListView.requestFocus();
        choosenSong();
        setPLayer();
    }

    @FXML
    protected void onValidSong(MouseEvent mouseEvent) {
        choosenSong();
        playSong();
    }

    @FXML
    public void onPlayPause(ActionEvent actionEvent) {
        if(player.getStatus() == MediaPlayer.Status.PLAYING) {
            player.pause();
        } else if (player.getStatus() == MediaPlayer.Status.PAUSED) {
            player.play();
        }
    }
    private void choosenSong() {
        currentSong = fxListView.getSelectionModel().getSelectedItem();
        genre.setIndex(fxListView.getSelectionModel().getSelectedIndex());
        setImage(fxImage, currentSong.getPathImage());
    }

    public void setImage(ImageView image, String imgName) {
        fxImage.setImage(new Image(getClass().getResourceAsStream(imgName)));
    }

    private void playSong() {
        setPLayer();
        player.play();
    }

    private void setPLayer() {
        if (player != null) {
            player.stop();
        }
        player = new MediaPlayer(new Media(getClass().getResource(currentSong.getPathSong()).toExternalForm()));
        player.setOnEndOfMedia(this::onNextTrack);
    }

    private void onNextTrack() {
        genre.next();
        fxListView.getSelectionModel().selectIndices(genre.getIndex());
        choosenSong();
        playSong();
    }

}
