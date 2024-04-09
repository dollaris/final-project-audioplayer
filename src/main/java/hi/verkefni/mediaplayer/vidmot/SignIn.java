package hi.verkefni.mediaplayer.vidmot;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class SignIn {

    @FXML
    private TextField nameInput;

    private MediaApplication mainApp;

    public void setMainApp(MediaApplication mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void onLogin(ActionEvent event) {
        String name = nameInput.getText().trim();
        if (!name.isEmpty()) {
            mainApp.loadDragAndDropScene(name);
        } else {
            // Handle empty name case
            nameInput.setStyle("-fx-border-color: red;");
        }
    }

    public void onQuit(ActionEvent actionEvent) {
        System.exit(0);
    }
}
