package hi.verkefni.mediaplayer.vidmot;

import hi.verkefni.mediaplayer.vinnsla.Account;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.Optional;

public class MediaController implements Initializable {
    @FXML
    private Button fxSignIn;
    @FXML
    private Button fxMore;
    @FXML
    private ScrollPane fxMorePane;
    @FXML
    private VBox fxLeftTabs;
    @FXML
    private BorderPane fxMainPane;
    @FXML
    private GridPane fxGridPaneGenre;
    @FXML
    private Button fxExplore;

    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        fxMorePane.setTranslateX(150);
        fxMorePane.setPrefWidth(150);
        fxMore.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(javafx.util.Duration.seconds(0.6));
            slide.setNode(fxMorePane);

            if (fxMorePane.getTranslateX() == 150) {
                slide.setToX(0);
            } else {
                slide.setToX(150);

            }
            slide.play();
        });

        for (int i = 0; i < fxGridPaneGenre.getChildren().size(); i++) {
            if (fxGridPaneGenre.getChildren().get(i) instanceof VBox) {
                VBox vbox = (VBox) fxGridPaneGenre.getChildren().get(i);
                for (int j = 0; j < vbox.getChildren().size(); j++) {
                    if (vbox.getChildren().get(j) instanceof Button) {
                        Button button = (Button) vbox.getChildren().get(j);
                        button.setOnAction(this::handleButtonClick);
                    }
                }
            }
        }
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
    public void onChooseTab(ActionEvent e) throws IOException {
        ViewSwitcher.switchTo(View.PLAYLIST, false);
    }

    @FXML
    public void handleButtonClick(ActionEvent e) {
        try {
            Button button = (Button) e.getSource();
            String buttonId = button.getId();

            switch (buttonId) {
                case "religious":
                    loadView("genre-view.fxml");
                    break;
                default:
                    break;
            }
        } catch (IOException ex) {
            // Handle or log the IOException
            ex.printStackTrace();
        }
    }

    private void loadView(String view) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(view));
            fxMainPane.setCenter(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}