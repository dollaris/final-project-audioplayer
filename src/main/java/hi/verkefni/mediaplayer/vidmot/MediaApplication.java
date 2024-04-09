package hi.verkefni.mediaplayer.vidmot;

import hi.verkefni.mediaplayer.vinnsla.DragAndDrop;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MediaApplication extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        loadSignInScene();
        primaryStage.setTitle("Media Player");
        primaryStage.show();
    }

    public void loadSignInScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("signin.fxml"));
            Parent root = loader.load();
            SignIn signInController = loader.getController();
            signInController.setMainApp(this);
            primaryStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadDragAndDropScene(String userName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("draganddrop.fxml"));
            Parent root = loader.load();
            // Get the controller and call updateUserInfo
            DragAndDrop controller = loader.getController();
            controller.updateUserInfo(userName); // Pass the username to the controller

            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args) {
        launch(args);
    }
}
