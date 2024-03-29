package hi.verkefni.mediaplayer.vidmot;

import hi.verkefni.mediaplayer.vinnsla.Account;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.Optional;

public class navbarController {
    @FXML
    private BorderPane fxMainPane;
    @FXML
    private ScrollPane fxMorePane;
    @FXML
    private Button fxMore;
    @FXML
    private Button fxSignIn;


    @FXML
    private void initialize() {
        loadContent("explore.fxml");
        fxMorePane.setTranslateX(135);
        fxMorePane.setPrefWidth(135);
        fxMore.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(javafx.util.Duration.seconds(0.6));
            slide.setNode(fxMorePane);

            if (fxMorePane.getTranslateX() == 135) {
                slide.setToX(0);
            } else {
                slide.setToX(135);

            }
            slide.play();
        });

    }
    @FXML
    public void onLogin(ActionEvent e) {
        Dialog<Account> dialog = new AccountDialog(new Account(""));
        Optional<Account> result = dialog.showAndWait();

        result.ifPresent(account -> {
            fxSignIn.setText(account.getName());
        });
    }

    @FXML
    public void onExplore(ActionEvent actionEvent) {
        loadContent("explore.fxml");
    }

    @FXML
    public void onArtist(ActionEvent actionEvent) {
        loadContent("artist.fxml");
    }

    @FXML
    public void onAlbums(ActionEvent actionEvent) {
        loadContent("albums.fxml");
    }

    @FXML
    public void onSongs(ActionEvent actionEvent) {
        loadContent("songs.fxml");
    }

    @FXML
    public void onListenAndWatch(ActionEvent actionEvent) {
        loadContent("mediaplayer.fxml");
    }

    @FXML
    public void onSettings(ActionEvent actionEvent) {
        loadContent("settings.fxml");
    }

    @FXML
    public void onPlaylists(ActionEvent actionEvent) {
        loadContent("playlists.fxml");
    }

    @FXML
    public void onChat(ActionEvent actionEvent) {
        loadContent("chat.fxml");
    }

    private void loadContent(String fxmlFileName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
            fxMainPane.setCenter(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
