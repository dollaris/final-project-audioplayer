package hi.verkefni.mediaplayer.vidmot;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class NavbarController {
    @FXML
    private BorderPane fxMainPane;
    @FXML
    private ScrollPane fxMorePane;
    @FXML
    private Button fxMore;
    @FXML
    private Button fxSignIn;
    @FXML
    public Label userName;
    @FXML
    public ImageView userImage;


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
        loadContent("SignIn.fxml");
    }

    void loadContent(String fxmlFileName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
            loader.setControllerFactory(c -> {
                if (c == SignIn.class) {
                    SignIn controller = new SignIn();
                    controller.setNavbarController(this);
                    return controller;
                } else {
                    try {
                        return c.getDeclaredConstructor().newInstance();
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            });
            fxMainPane.setCenter(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void loadContent() {
        loadContent("signin.fxml");
    }

    public void updateUserInfo(String name) {
        userName.setText(name);

        // Generate user image URL and set it
        String idUserImage = "https://robohash.org/" + name;
        userImage.setImage(new Image(idUserImage));
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
        loadContent("listenandwatch.fxml");
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

}
