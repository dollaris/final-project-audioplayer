package hi.verkefni.mediaplayer.vidmot;

import hi.verkefni.mediaplayer.vinnsla.DragAndDrop;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The main class for the Media Player application.
 * This class extends Application and sets up the primary stage with different scenes
 * depending on user actions. It handles the initialization and transitions between
 * the sign-in scene and the drag-and-drop media handling scene.
 */
public class MediaApplication extends Application {

    private Stage primaryStage;

    /**
     * Starts the primary stage and sets the initial scene for the application.
     * This method is called as soon as the JavaFX application should start,
     * and is where initial scene setup is performed.
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set.
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        loadSignInScene();
        primaryStage.setTitle("Media Player");
        primaryStage.show();
    }

    /**
     * Loads and displays the sign-in scene. This method attempts to load the FXML for
     * the sign-in view and sets it on the primary stage. It configures the SignIn controller
     * and initializes it with a reference to this main application class.
     */
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

    /**
     * Loads and displays the drag-and-drop scene. This scene allows users to interact
     * with media files through a drag-and-drop interface. This method sets up the
     * scene for handling media files after a user has signed in.
     *
     * @param userName the username to pass to the drag-and-drop controller, which
     *                 uses it to update user-specific settings or greetings.
     */
    public void loadDragAndDropScene(String userName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("draganddrop.fxml"));
            Parent root = loader.load();
            DragAndDrop controller = loader.getController();
            controller.updateUserInfo(userName);

            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The main entry point for all JavaFX applications. This method is called last,
     * after all JavaFX infrastructure has been initialized. This is where the JavaFX
     * application is launched.
     *
     * @param args command line arguments passed to the application. Not used directly.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
