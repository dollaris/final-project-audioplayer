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
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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

    private final Queue<File> fileQueue = new LinkedList<>();

    private final List<File> fileList = new LinkedList<>();
    private int currentIndex = -1; // Start before the first element


    private MediaPlayer mediaPlayer;

    @FXML
    void initialize() {
        setProgressBarHandlers();
    }

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
                double progress = newValue.toSeconds() / mediaPlayer.getTotalDuration().toSeconds();
                progressBar.setProgress(progress);
            });
        }
    }

     public void paneDragDropped(DragEvent event) {
        Dragboard dragboard = event.getDragboard();
        boolean success = false;
        if (dragboard.hasFiles()) {
            List<File> files = dragboard.getFiles();
            for (File file : files) {
                playlistListView.getItems().add(file.getName());
                if (!fileList.contains(file)) {
                    fileList.add(file);
                }
            }
            success = true;
            event.setDropCompleted(success);
            event.consume();

            // Auto-play the first file if nothing is currently playing
            if ((mediaPlayer == null || mediaPlayer.getStatus() == MediaPlayer.Status.STOPPED) && !fileList.isEmpty() && currentIndex == -1) {
                playNextInQueue();
            }
        }
    }


    private void playNextInQueue() {
        if (currentIndex + 1 < fileList.size()) {
            currentIndex++;
            File nextFile = fileList.get(currentIndex);
            displayFile(nextFile);
        }
    }

    private void playPreviousInQueue() {
        if (currentIndex - 1 >= 0) {
            currentIndex--;
            mediaPlayer.stop();
            File previousFile = fileList.get(currentIndex);
            displayFile(previousFile);
        } else {
            System.out.println("No previous tracks are in the queue.");
        }
    }


    private void displayFile(File file) {
        String fileName = file.getName().toLowerCase();
        if (fileName.matches(".+\\.(mp3|wav|aiff|aac|flac|ogg)$")) {
            playAudio(file);
            if (!playlistListView.getItems().contains(file.getName())) {
                playlistListView.getItems().add(file.getName());
            }
        } else if (fileName.matches(".+\\.(jpg|jpeg|png|gif|bmp)$")) {
            displayImage(file);
        } else if (fileName.matches(".+\\.(avi|mp4|flv|mov|wmv|webm)$")) {
            playVideo(file);
            if (!playlistListView.getItems().contains(file.getName())) {
                playlistListView.getItems().add(file.getName());
            }
        }
    }

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

    private void handleMetadataChange(String key, Object value) {
        System.out.println("Metadata - Key: " + key + ", Value: " + value);
        if (key.equals("artist") || key.equals("album") || key.equals("title") || key.equals("genre") || key.equals("year")) {
            metadataListView.getItems().clear();
            metadataListView.getItems().add(key + ": " + value);
            System.out.println("Added audio properties to the ListView" + metadataListView.getItems());
        }

        if (key.equals("image")) {
            Image image = (Image) value;
            imageView.setImage(image);
        }
    }

    private void setMediaView(Media media) {
        mediaView.setMediaPlayer(mediaPlayer);
    }

    @FXML
    void paneDragOver(DragEvent event) {
        Dragboard dragboard = event.getDragboard();
        if (dragboard.hasFiles()) {
            event.acceptTransferModes(TransferMode.COPY);
        }
        event.consume();
    }

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

    @FXML
    public void onStop(ActionEvent actionEvent) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

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

    @FXML
    public void onPreviousTrack(ActionEvent actionEvent) {
        playPreviousInQueue();
    }

    @FXML
    public void onNextTrack(ActionEvent actionEvent) {
        if (currentIndex + 1 < fileList.size()) {
            mediaPlayer.stop();
            playNextInQueue();
        } else {
            System.out.println("No more tracks in the queue.");
        }
    }

    public void updateUserInfo(String userName) {
        userNameLabel.setText(userName);
        userImageView.setImage(new Image("https://robohash.org/" + userName + ".png"));
    }
}
