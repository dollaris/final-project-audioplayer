module hi.verkefni.mediaplayer {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.desktop;


    opens hi.verkefni.mediaplayer.vidmot to javafx.fxml, javafx.media;
    exports hi.verkefni.mediaplayer.vidmot;
    exports hi.verkefni.mediaplayer.vinnsla;
}