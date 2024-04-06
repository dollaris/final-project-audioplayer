
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
import javafx.util.Duration;

import java.io.IOException;

public class NavbarController {
    @FXML
    private BorderPane fxMainPane;
    @FXML
    private ScrollPane fxMorePane;
    @FXML
    private Button fxMore;
    @FXML
    public Label userName;
    @FXML
    public ImageView userImage;

    private static final String SIGN_IN_FXML = "SignIn.fxml";


    @FXML
    private void initialize() {
        loadContent("explore.fxml");
        configureMorePaneAnimation();
    }

    private void configureMorePaneAnimation() {
        fxMorePane.setTranslateX(135);
        fxMorePane.setPrefWidth(135);
        fxMore.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.6));
            slide.setNode(fxMorePane);
            slide.setToX(fxMorePane.getTranslateX() == 135 ? 0 : 135);
            slide.play();
        });
    }
    @FXML
    public void onLogin(ActionEvent e) {
        loadContent(SIGN_IN_FXML);
    }

    public void loadContent(String fxmlFileName) {
        if (userName.getText().isEmpty() && !fxmlFileName.equals(SIGN_IN_FXML)) {
            fxmlFileName = SIGN_IN_FXML;
        }
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

    public void loadContent() {
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
}
