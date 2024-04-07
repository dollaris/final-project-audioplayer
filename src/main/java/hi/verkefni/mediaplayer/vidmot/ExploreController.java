package hi.verkefni.mediaplayer.vidmot;

import hi.verkefni.mediaplayer.vinnsla.Genres;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Optional;


public class ExploreController implements Initializable {
    @FXML
    private AnchorPane fxMainPane;
    @FXML
    private GridPane fxGridPaneGenre;

    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        for (int i = 0; i < fxGridPaneGenre.getChildren().size(); i++) {
            if (fxGridPaneGenre.getChildren().get(i) instanceof VBox) {
                VBox vbox = (VBox) fxGridPaneGenre.getChildren().get(i);
                for (int j = 0; j < vbox.getChildren().size(); j++) {
                    if (vbox.getChildren().get(j) instanceof Button button) {
                        button.setOnAction(event -> {
                                try {
                                    handleButtonClick(event);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });

                    }
                }
            }
        }
    }

    @FXML
    public void showAddPlaylistDialog(ActionEvent e) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(fxMainPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("addplaylistdialog.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException exception) {
            System.out.println("Couldn't load the dialog");
            exception.printStackTrace();
            return;
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.out.println("OK pressed");
        } else {
            System.out.println("Cancel pressed");
        }
    }

    @FXML
    public void handleButtonClick(ActionEvent e) throws IOException {
        Genres genre = new Genres();
        try {
            Button button = (Button) e.getSource();
            String buttonId = button.getId();

            switch (buttonId) {
                case "classical":
                    genre.setGenreIndex(0);
                    loadView("genre-view.fxml");
                    break;
                case "hiphop":
                    genre.setGenreIndex(1);
                    loadView("genre-view.fxml");
                    break;
                case "pop":
                    genre.setGenreIndex(2);
                    loadView("genre-view.fxml");
                    break;
                case "rock":
                    genre.setGenreIndex(3);
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
            AnchorPane genreView = fxmlLoader.load();
            Parent parent = fxMainPane.getParent();

            if (parent instanceof BorderPane) {
                BorderPane borderPane = (BorderPane) parent;
                borderPane.setCenter(genreView);
            } else {
                System.out.println("Parent is not a BorderPane");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}