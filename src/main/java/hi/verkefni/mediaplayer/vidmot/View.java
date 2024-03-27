package hi.verkefni.mediaplayer.vidmot;

public enum View {
    MAIN("home-Page.fxml"),
    PLAYLIST("mediaPlayer.fxml");

    private String fileName;

    View(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
