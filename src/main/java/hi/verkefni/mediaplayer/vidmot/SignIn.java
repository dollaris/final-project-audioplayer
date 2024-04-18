package hi.verkefni.mediaplayer.vidmot;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * Controller class for the sign-in view in the Media Player application.
 * This class handles user interaction within the sign-in scene, where users
 * input their name and can proceed to the main application functionality.
 */
public class SignIn {

    @FXML
    private TextField nameInput;

    private MediaApplication mainApp;

    /**
     * Sets the reference to the main application class. This method allows this
     * controller to interact with the main application, enabling it to change
     * scenes and pass user data.
     *
     * @param mainApp the main application class instance which controls the stage and scenes.
     */
    public void setMainApp(MediaApplication mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Handles the login action when the login button is pressed.
     * This method checks if the username field is not empty and proceeds to load
     * the drag-and-drop scene, passing the entered username. If the username field
     * is empty, it visually indicates an error by setting the border color of the
     * text field to red.
     *
     * @param event the event triggered by pressing the login button.
     */
    @FXML
    private void onLogin(ActionEvent event) {
        String name = nameInput.getText().trim();
        if (!name.isEmpty()) {
            mainApp.loadDragAndDropScene(name);
            System.out.println("Looged in as: " + name);
        } else {
            nameInput.setStyle("-fx-border-color: red;"); //just did for fun, don't have much affect
        }
    }

    /**
     * Handles the quit action when the quit button is pressed.
     * This method terminates the application.
     *
     * @param actionEvent the event triggered by pressing the quit button.
     */
    public void onQuit(ActionEvent actionEvent) {
        System.exit(0);
    }
}
