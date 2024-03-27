package hi.verkefni.mediaplayer.vinnsla;

import javafx.collections.MapChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class DragAndDrop {

    @FXML
    private MediaView mediaView;

    @FXML
    private ImageView imageView;

    @FXML
    private ListView<String> playlistListView;

    private final Queue<File> fileQueue = new LinkedList<>();

    @FXML
    private ListView<String> metadataListView;

    @FXML
    private Slider volumeSlider;

    @FXML
    private ProgressBar progressBar;

    private MediaPlayer mediaPlayer;

    /**
     * Initializes the controller after FXML file has been loaded.
     * This method is automatically called by the FXMLLoader.
     */
    @FXML
    void initialize() {
        setProgressBarHandlers();
    }

    /**
     * Sets the event handlers for the progress bar. It handles mouse click events on the progress bar by seeking the media to the corresponding position. It also updates the progress bar based on the current media playback time.
     *
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

        if (mediaPlayer != null) {
            mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
                Duration currentTime = mediaPlayer.getCurrentTime();
                Duration totalDuration = mediaPlayer.getTotalDuration();
                double progress = newValue.toSeconds() / totalDuration.toSeconds();
                progressBar.setProgress(progress);
            });
        }
    }

    /**
     * Handles the drag and drop event on the pane.
     *
     * @param  event  the DragEvent object representing the drag and drop event
     */
    @FXML
    void paneDragDropped(DragEvent event) {
        Dragboard dragboard = event.getDragboard();
        boolean success = false;
        if (dragboard.hasFiles()) {
            List<File> files = dragboard.getFiles();
            for (File file : files) {
                System.out.println("Dropped file: " + file.getName());
                playlistListView.getItems().add(file.getName());
                System.out.println("Added file to playlist: " + file.getName());
                if (!fileQueue.contains(file)) {
                    fileQueue.add(file);
                    System.out.println("Added file to queue: " + file.getName());
                }
            }
            success = true;
            event.setDropCompleted(success);
            event.consume();

            if ((mediaPlayer == null || mediaPlayer.getStatus() == MediaPlayer.Status.STOPPED)
                    && !fileQueue.isEmpty()) {
                playNextInQueue();
            }
        }
    }

    /**
     * Plays the next file in the queue if there are files in the queue.
     * If the next file is not already in the playlist, it is added to the playlist.
     * The file is then displayed and the metadata list is cleared.
     */
    private void playNextInQueue() {
        if (!fileQueue.isEmpty()) {
            File nextFile = fileQueue.poll();
            System.out.println("Next file in queue: " + nextFile.getName());
            if (!playlistListView.getItems().contains(nextFile.getName())) {
                System.out.println("Adding file to playlist: " + nextFile.getName());
                playlistListView.getItems().add(nextFile.getName());
            }
            displayFile(nextFile);
            metadataListView.getItems().clear();
        }
    }

    /**
     * Display the file based on its type and add it to the playlist if not already present.
     *
     * @param  file	The file to be displayed
     */
    private void displayFile(File file) {
        String fileName = file.getName().toLowerCase();
        if (fileName.endsWith(".mp3") || fileName.endsWith(".wav") || fileName.endsWith(".aiff") ||
                fileName.endsWith(".aac") || fileName.endsWith(".flac") || fileName.endsWith(".ogg")) {
            playAudio(file);
            if (!playlistListView.getItems().contains(file.getName())) {
                System.out.println("Adding file to playlist: " + file.getName());
                playlistListView.getItems().add(file.getName());
            }
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png") ||
                fileName.endsWith(".gif") || fileName.endsWith(".bmp")) {
            displayImage(file);
        } else if (fileName.endsWith(".avi") || fileName.endsWith(".mp4") || fileName.endsWith(".flv") ||
                fileName.endsWith(".mov") || fileName.endsWith(".wmv") || fileName.endsWith(".webm")) {
            playVideo(file);
            if (!playlistListView.getItems().contains(file.getName())) {
                System.out.println("Adding file to playlist: " + file.getName());
                playlistListView.getItems().add(file.getName());
            }
        }
    }

    /**
     * A method to play audio from a given file and handle various media player events.
     *
     * @param  file   the file to be played
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
            Duration totalDuration = mediaPlayer.getTotalDuration();
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

        mediaPlayer.setOnError(() -> {
            System.out.println("Error occurred: " + mediaPlayer.getError().getMessage());
        });

        media.getMetadata().addListener((MapChangeListener.Change<? extends String, ? extends Object> change) -> {
            if (change.wasAdded()) {
                String key = change.getKey();
                Object value = change.getValueAdded();
                System.out.println("Metadata - Key: " + key + ", Value: " + value);

                if (key.equals("artist") || key.equals("album") || key.equals("title") || key.equals("genre") || key.equals("year")) {
                    metadataListView.getItems().add(key + ": " + value);
                    System.out.println("Added audio properties to the ListView" + metadataListView.getItems());
                }

                if (key.equals("image")) {
                    Image image = (Image) value;
                    imageView.setImage(image);
                }
            }
        });

        mediaView.setMediaPlayer(mediaPlayer);
        System.out.println("Playing audio: " + file);
    }

    /**
     * Plays a video file.
     *
     * @param  file  the video file to be played
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
            Duration totalDuration = mediaPlayer.getTotalDuration();
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

        mediaPlayer.setOnError(() -> {
            System.out.println("Error occurred: " + mediaPlayer.getError().getMessage());
        });

        media.getMetadata().addListener((MapChangeListener.Change<? extends String, ? extends Object> change) -> {
            if (change.wasAdded()) {
                String key = change.getKey();
                Object value = change.getValueAdded();
                System.out.println("Video Metadata - Key: " + key + ", Value: " + value);

                if (key.equals("artist") || key.equals("album") || key.equals("title") || key.equals("genre") || key.equals("year")) {
                    metadataListView.getItems().add(key + ": " + value);
                }

                if (key.equals("image")) {
                    Image image = (Image) value;
                    imageView.setImage(image);
                }
            }
        });

        mediaView.setMediaPlayer(mediaPlayer);
        imageView.setVisible(false);
        mediaView.setVisible(true);
        System.out.println("Playing video: " + file);
    }

    /**
     * Displays an image in the imageView and updates the metadataListView with the image's properties.
     *
     * @param  file  the File object representing the image file to be displayed
     */
    private void displayImage(File file) {
        Image image = new Image(file.toURI().toString());
        imageView.setImage(image);
        imageView.setVisible(true);
        mediaView.setVisible(false);
        System.out.println("Displaying image: " + file);

        metadataListView.getItems().clear();

        metadataListView.getItems().add("Height: " + image.getHeight());
        metadataListView.getItems().add("Width: " + image.getWidth());
        metadataListView.getItems().add("Pixel Reader Available: " + (image.getPixelReader() != null));
        System.out.println("Added image properties to the ListView" + metadataListView.getItems());

        System.out.println("Height: " + image.getHeight());
        System.out.println("Width: " + image.getWidth());
        System.out.println("Pixel Reader Available: " + (image.getPixelReader() != null));
    }

    /**
     * Handles the drag over event for the pane.
     *
     * @param  event  the DragEvent object representing the drag over event
     */
    @FXML
    void paneDragOver(DragEvent event) {
        Dragboard dragboard = event.getDragboard();
        if (dragboard.hasFiles()) {
            event.acceptTransferModes(TransferMode.COPY);
        }
        event.consume();
        System.out.println("DragOver");
    }

    /**
     * Perform play/pause action on media player.
     *
     * @param  actionEvent    the action event triggering the function
     * @return         	    void
     */
    @FXML
    public void onPlayPause(ActionEvent actionEvent) {
        if (mediaPlayer != null) {
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                mediaPlayer.pause();
            } else {
                mediaPlayer.play();
            }
        }
    }

    /**
     * Stops the media player if it is currently playing.
     *
     * @param  actionEvent  the event that triggered the method
     */
    @FXML
    public void onStop(ActionEvent actionEvent) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    /**
     * Handles the mouse click event on the track list.
     *
     * @param  mouseEvent  the mouse event that triggered the function
     */
    @FXML
    public void onTrackList(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            String selectedItem = playlistListView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                File file = new File(selectedItem);
                if (file.exists()) {
                    displayFile(file);
                } else {
                    System.err.println("Error: File not found - " + selectedItem);
                }
            }
        }
    }

    /**
     * Handles the action event when the previous track button is clicked.
     *
     * @param  actionEvent  the action event triggered by the button click
     */
    public void onPreviousTrack(ActionEvent actionEvent) {
        File file = fileQueue.peek();
        if (file != null) {
            mediaPlayer.stop();
            displayFile(file);
        } else {
            System.out.println("No previous tracks is in the queue.");
        }
    }

    /**
     * A description of the entire Java function.
     *
     * @param  actionEvent    description of parameter
     * @return          description of return value
     */
    public void onNextTrack(ActionEvent actionEvent) {
        if(!fileQueue.isEmpty()) {
            mediaPlayer.stop();
            playNextInQueue();
        } else {
            System.out.println("No more tracks in the queue.");
        }
    }
}
