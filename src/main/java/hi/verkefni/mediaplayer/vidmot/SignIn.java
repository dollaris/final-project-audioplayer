package hi.verkefni.mediaplayer.vidmot;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;

public class SignIn {

    @FXML
    private TextField nameInput;

    private NavbarController navbarController;

    @FXML
    public void initialize() {
        nameInput.setText("");
    }

    public void setNavbarController(NavbarController navbarController) {
        this.navbarController = navbarController;
    }

    public void onLogin(ActionEvent actionEvent) throws IOException {
        String name = nameInput.getText();
        if (name.isEmpty()) {
            nameInput.setStyle("-fx-border-color: red");
            System.out.println("Please enter a name.");
            return;
        }

        nameInput.setStyle("-fx-border-color: green");

        if (navbarController != null) {
            navbarController.updateUserInfo(name);
        } else {
            System.out.println("Error: NavbarController is null.");
        }

        System.out.println("Logged in as: " + name);
        navbarController.loadContent("explore.fxml");
    }

    public void onQuit(ActionEvent actionEvent) {
        System.exit(0);
    }
}
