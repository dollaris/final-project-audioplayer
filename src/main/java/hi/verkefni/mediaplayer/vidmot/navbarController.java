package hi.verkefni.mediaplayer.vidmot;

public class navbarController {
    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private void initialize() {
        loadContent("explore.fxml"); //default content
    }

    @FXML
    public void onSignIn(MouseEvent actionEvent) {
        loadContent("signin.fxml");
    }

    @FXML
    public void onExplore(MouseEvent actionEvent) {
        loadContent("explore.fxml");
    }

    @FXML
    public void onArtist(MouseEvent actionEvent) {
        loadContent("artist.fxml");
    }

    @FXML
    public void onAlbums(MouseEvent actionEvent) {
        loadContent("albums.fxml");
    }

    @FXML
    public void onSongs(MouseEvent actionEvent) {
        loadContent("songs.fxml");
    }

    @FXML
    public void onListenAndWatch(MouseEvent actionEvent) {
        loadContent("listenandwatch.fxml");
    }

    @FXML
    public void onSettings(MouseEvent actionEvent) {
        loadContent("settings.fxml");
    }

    @FXML
    public void onPlaylists(MouseEvent actionEvent) {
        loadContent("playlists.fxml");
    }

    @FXML
    public void onChat(MouseEvent actionEvent) {
        loadContent("chat.fxml");
    }

    private void loadContent(String fxmlFileName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
            mainBorderPane.setCenter(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
