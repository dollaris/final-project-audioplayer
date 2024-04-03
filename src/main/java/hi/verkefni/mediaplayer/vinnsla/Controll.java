package hi.verkefni.mediaplayer.vinnsla;

import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;

public interface Controll {
    /**
     * Perform play/pause action on media player.
     *
     * @param  actionEvent    the action event triggering the function
     * @return         	    void
     */
    void onPlayPause(ActionEvent actionEvent);

    /**
     * Stops the media player if it is currently playing.
     *
     * @param  actionEvent  the event that triggered the method
     */
    void onStop(ActionEvent actionEvent);

    /**
     * Handles the mouse click event on the track list.
     *
     * @param  mouseEvent  the mouse event that triggered the function
     */
    void onTrackList(MouseEvent mouseEvent);

    /**
     * Handles the action event when the previous track button is clicked.
     *
     * @param  actionEvent  the action event triggered by the button click
     */
    void onPreviousTrack(ActionEvent actionEvent);

    /**
     * A description of the entire Java function.
     *
     * @param  actionEvent    description of parameter
     * @return          description of return value
     */
    void onNextTrack(ActionEvent actionEvent);
}
