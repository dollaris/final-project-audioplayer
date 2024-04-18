package hi.verkefni.mediaplayer.vinnsla;

import javafx.collections.MapChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.io.File;
import java.util.*;

public class DragAndDrop {

    @FXML
    private MediaView mediaView;

    @FXML
    private ListView<String> playlistListView;

    @FXML
    private ListView<String> metadataListView;

    @FXML
    private ImageView imageView;

    @FXML
    private ImageView userImageView;

    @FXML
    private Label userNameLabel;

    @FXML
    private Slider volumeSlider;

    @FXML
    private ProgressBar progressBar;

    private final List<File> fileList = new LinkedList<>();
    private final Map<String, String> fileNamePathMap = new HashMap<>();
    private int currentIndex = -1;
    private MediaPlayer mediaPlayer;

    /**
     * Initializes the controller class. This method is automatically called
     * after the FXML file has been loaded. It sets up the initial state of
     * the progress bar handlers and initializes the volume slider to a default
     * value of 50%.
     */
    @FXML
    void initialize() {
        setProgressBarHandlers();
        volumeSlider.setValue(50.0);
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (mediaPlayer != null) {
                mediaPlayer.setVolume(newValue.doubleValue() / 100.0);
            }
        });
    }

    /**
     * Sets up event handlers for the media progress bar. This method configures
     * the progress bar to seek the media player to the corresponding time position
     * when clicked by the user based on the click position relative to the width
     * of the progress bar.
     */
    private void setProgressBarHandlers() {
        progressBar.setOnMouseClicked(event -> {
            if (mediaPlayer != null && mediaPlayer.getStatus() != MediaPlayer.Status.UNKNOWN) {
                double mouseX = event.getX();
                double progressBarWidth = progressBar.getWidth();
                double seekPosition = (mouseX / progressBarWidth) * mediaPlayer.getTotalDuration().toSeconds();
                mediaPlayer.seek(Duration.seconds(seekPosition));
            }
        });
    }

    /**
     * Handles the drag over event on the application's drag area. This method
     * checks if the drag data contains files. If so, it accepts the transfer
     * modes to allow file dropping.
     *
     * @param event the drag event that contains information about the drag operation.
     */
    @FXML
    void paneDragOver(DragEvent event) {
        if (event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.ANY);
        }
        event.consume();
    }

    /**
     * Handles the drop event for dragging files over the application. This method
     * retrieves the files from the dragboard, adds them to the playlist, and updates
     * the playlist view. If no media is currently playing, the first file dropped
     * will be played automatically, while the rest are queued.
     *
     * @param event the drag event that contains the dragboard with the files.
     */
    @FXML
    void paneDragDropped(DragEvent event) {
        Dragboard dragboard = event.getDragboard();
        boolean success = false;
        if (dragboard.hasFiles()) {
            List<File> files = dragboard.getFiles();
            boolean isFirstFile = true;
            for (File file : files) {
                String fileName = file.getName();
                playlistListView.getItems().add(fileName);
                fileNamePathMap.put(fileName, file.getAbsolutePath());
                fileList.add(file);
                if (mediaPlayer == null || mediaPlayer.getStatus() != MediaPlayer.Status.PLAYING) {
                    if (isFirstFile) {
                        displayFile(file);
                        isFirstFile = false;
                    }
                }
            }
            success = true;
            event.setDropCompleted(success);
            event.consume();
        }
    }

    /**
     * Handles double-click events on the playlist ListView. This method checks
     * if a media file is double-clicked in the playlist, and if so, it stops
     * any currently playing media, and plays the selected file.
     *
     * @param mouseEvent the mouse event that triggered this method, including details
     *                   like click count and selected item.
     */
    public void onTrackList(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            String selectedItem = playlistListView.getSelectionModel().getSelectedItem();
            String filePath = fileNamePathMap.get(selectedItem);
            if (filePath != null && new File(filePath).exists()) {
                if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                    mediaPlayer.stop();
                }
                displayFile(new File(filePath));
            } else {
                System.err.println("Error: File not found - " + filePath);
            }
        }
    }

    /**
     * Initializes and plays a media file. This method stops any currently playing
     * media, initializes a new MediaPlayer for the provided file, and sets it to
     * automatically start playing. It also sets volume and attaches necessary
     * listeners to handle playback events like end of media.
     *
     * @param file the File object of the media to be played.
     */
    private void playMedia(File file) {
        try {
            Media media = new Media(file.toURI().toString());
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setAutoPlay(true);
            mediaPlayer.setVolume(volumeSlider.getValue() / 100.0);
            mediaPlayer.setOnEndOfMedia(this::playNextInQueue);
            mediaPlayer.setOnReady(() -> progressBar.setProgress(0));
            attachMediaPlayerListeners();
            System.out.println("Playing media: " + file.getName());
        } catch (Exception e) {
            System.err.println("Error loading media: " + e.getMessage());
        }
    }

    /**
     * Attaches listeners to the current MediaPlayer instance to handle changes
     * in playback time and errors. This method sets up the MediaPlayer to update
     * the progress bar as the media plays and handles volume adjustments.
     */
    private void attachMediaPlayerListeners() {
        mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
            if (mediaPlayer.getTotalDuration() != null) {
                progressBar.setProgress(newValue.toMillis() / mediaPlayer.getTotalDuration().toMillis());
            }
        });
        mediaPlayer.setVolume(volumeSlider.getValue() / 100.0);
        mediaPlayer.setOnError(() -> System.err.println("Error occurred: " + mediaPlayer.getError().getMessage()));
    }

    /**
     * Plays the next media file in the queue. This method checks if there are
     * more files in the queue; if so, it stops the current playback and starts
     * the next file. If no more files are available, it resets the queue index.
     */
    private void playNextInQueue() {
        if (++currentIndex < fileList.size()) {
            mediaPlayer.stop();
            playMedia(fileList.get(currentIndex));
        } else {
            System.out.println("No more tracks in the queue.");
            currentIndex = -1;
        }
    }

    /**
     * Determines the type of the file based on its extension and calls the appropriate
     * method to handle its display or playback. Clears previous metadata entries in the
     * ListView before displaying new file information.
     *
     * @param file the File object to be processed.
     */
    private void displayFile(File file) {
        String fileName = file.getName().toLowerCase();
        if (fileName.matches(".+\\.(mp3|wav|aiff|aac|flac|ogg)$")) {
            metadataListView.getItems().clear();
            playAudio(file);
        } else if (fileName.matches(".+\\.(jpg|jpeg|png|gif|bmp)$")) {
            metadataListView.getItems().clear();
            displayImage(file);
        } else if (fileName.matches(".+\\.(avi|mp4|flv|mov|wmv|webm)$")) {
            metadataListView.getItems().clear();
            playVideo(file);
        } else {
            System.err.println("Unsupported file format: " + fileName);
        }
    }

    /**
     * Plays an audio file using the MediaPlayer. This method sets up the MediaPlayer for
     * audio playback, including setting volume levels, updating progress bars, and handling
     * end-of-media actions.
     *
     * @param file the audio file to be played.
     */
    private void playAudio(File file) {
        Media media = new Media(file.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.stop();
            playNextInQueue();
        });

        mediaPlayer.setOnReady(() -> {
            progressBar.setProgress(0);
            progressBar.setProgress(1);
        });

        mediaPlayer.currentTimeProperty().addListener((observableValue, oldValue, newValue) -> {
            progressBar.setProgress(newValue.toSeconds() / mediaPlayer.getMedia().getDuration().toSeconds());
        });

        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (mediaPlayer != null) {
                mediaPlayer.setVolume(newValue.doubleValue() / 100.0);
            }
        });

        mediaPlayer.setOnError(() -> System.out.println("Error occurred: " + mediaPlayer.getError().getMessage()));

        media.getMetadata().addListener((MapChangeListener.Change<? extends String, ? extends Object> change) -> {
            if (change.wasAdded()) {
                handleMetadataChange(change.getKey(), change.getValueAdded());
            }
        });

        setMediaView(media);
        System.out.println("Playing audio: " + file);
    }

    /**
     * Plays a video file using the MediaPlayer. This method sets up the MediaPlayer for
     * video playback, including setting volume levels, updating progress bars, and handling
     * end-of-media actions. It also ensures that the video is visible and the image view is hidden.
     *
     * @param file the video file to be played.
     */
    private void playVideo(File file) {
        Media media = new Media(file.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.stop();
            playNextInQueue();
        });

        mediaPlayer.setOnReady(() -> {
            progressBar.setProgress(0);
            progressBar.setProgress(1);
        });

        mediaPlayer.currentTimeProperty().addListener((observableValue, oldValue, newValue) -> {
            progressBar.setProgress(newValue.toSeconds() / mediaPlayer.getMedia().getDuration().toSeconds());
        });

        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (mediaPlayer != null) {
                mediaPlayer.setVolume(newValue.doubleValue() / 100.0);
            }
        });

        mediaPlayer.setOnError(() -> System.out.println("Error occurred: " + mediaPlayer.getError().getMessage()));

        media.getMetadata().addListener((MapChangeListener.Change<? extends String, ? extends Object> change) -> {
            if (change.wasAdded()) {
                handleMetadataChange(change.getKey(), change.getValueAdded());
            }
        });

        setMediaView(media);
        imageView.setVisible(false);
        mediaView.setVisible(true);
        System.out.println("Playing video: " + file);
    }

    /**
     * Displays an image in the ImageView. Clears previous metadata and updates
     * the ListView with image properties such as height, width, and pixel availability.
     *
     * @param file the image file to be displayed.
     */
    private void displayImage(File file) {
        Image image = new Image(file.toURI().toString());
        imageView.setImage(image);
        imageView.setVisible(true);
        mediaView.setVisible(false);
        System.out.println("Displaying image: " + file);

        metadataListView.getItems().clear();
        metadataListView.getItems().addAll(
                "Height: " + image.getHeight(),
                "Width: " + image.getWidth(),
                "Pixel Reader Available: " + (image.getPixelReader() != null)
        );
        System.out.println("Added image properties to the ListView" + metadataListView.getItems());
    }

    /**
     * Sets the media player in the media view component.
     * This method is crucial for ensuring that the media player instance is correctly
     * associated with the media view UI component, allowing video output to be displayed.
     *
     * @param media the Media object being played, not directly used in this method but
     *              implies the media being handled by the media player.
     */
    private void setMediaView(Media media) {
        mediaView.setMediaPlayer(mediaPlayer);
    }

    /**
     * Handles the action of moving to the previous track in the playlist.
     * This method decrements the current index, stops any currently playing media,
     * and plays the previous track if available.
     *
     * @param actionEvent the event triggered by clicking the previous button.
     */
    @FXML
    public void onPreviousTrack(ActionEvent actionEvent) {
        if (currentIndex > 0) {
            currentIndex--;
            File file = fileList.get(currentIndex);
            if (file.exists()) {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                }
                displayFile(file);
                playlistListView.getSelectionModel().select(currentIndex);
            } else {
                System.err.println("Error: File not found - " + file.getAbsolutePath());
            }
        } else {
            System.out.println("No previous tracks are in the queue.");
            currentIndex = 0;
        }
    }

    /**
     * Handles the action of moving to the next track in the playlist.
     * This method increments the current index, stops any currently playing media,
     * and plays the next track if available.
     *
     * @param actionEvent the event triggered by clicking the next button.
     */
    @FXML
    public void onNextTrack(ActionEvent actionEvent) {
        if (currentIndex < fileList.size() - 1) {
            currentIndex++;
            File file = fileList.get(currentIndex);
            if (file.exists()) {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                }
                displayFile(file);
                playlistListView.getSelectionModel().select(currentIndex);
            } else {
                System.err.println("Error: File not found - " + file.getAbsolutePath());
            }
        } else {
            System.out.println("No more tracks in the queue.");
            currentIndex = -1;
        }
    }

    /**
     * Toggles the play and pause state of the currently loaded media in the media player.
     * This method pauses the media if it is currently playing, or plays the media if it is paused.
     *
     * @param actionEvent the event triggered by clicking the play/pause button.
     */
    @FXML
    public void onPlayPause(ActionEvent actionEvent) {
        if (mediaPlayer != null) {
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                mediaPlayer.pause();
                System.out.println("Media paused");
            } else {
                mediaPlayer.play();
                System.out.println("Media playing");
            }
        }
    }

    /**
     * Stops the playback of the currently loaded media in the media player.
     * This method also resets the progress bar to zero and optionally reloads
     * the currently selected media file for future playback.
     *
     * @param actionEvent the event triggered by clicking the stop button.
     */
    @FXML
    public void onStop(ActionEvent actionEvent) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            progressBar.setProgress(0);
            System.out.println("Media stopped");

            if (currentIndex >= 0 && currentIndex < fileList.size()) {
                File file = fileList.get(currentIndex);
                displayFile(file);
            }
        }
    }

    /**
     * Handles changes to the metadata of the currently playing media.
     * This method updates the ListView with metadata entries, except for
     * raw metadata and image data, which are either handled differently or
     * not displayed.
     *
     * @param key the metadata key indicating the type of metadata.
     * @param value the metadata value associated with the key.
     */
    private void handleMetadataChange(String key, Object value) {
        if (key.equals("image")) {
            if (value instanceof Image) {
                Image image = (Image) value;
                imageView.setImage(image);
                imageView.setVisible(true);
                mediaView.setVisible(false);
            }
        } else if (!key.equals("raw metadata")) {
            System.out.println("Metadata - Key: " + key + ", Value: " + value);
            metadataListView.getItems().add(key + ": " + value);
        }
    }

    /**
     * Updates the user information display in the UI.
     * This method sets the user's name label and updates the user's image
     * based on a generated URL that likely links to an avatar service.
     *
     * @param userName the user's name to display.
     */
    public void updateUserInfo(String userName) {
        userNameLabel.setText(userName);
        userImageView.setImage(new Image("https://robohash.org/" + userName + ".png"));
    }
}
