package hi.verkefni.mediaplayer.vidmot;

import hi.verkefni.mediaplayer.vinnsla.Account;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;

import java.util.Optional;

public class MediaController {
    @FXML
    private Button fxSignIn;


    @FXML
    public void onLogin(ActionEvent e) {
        Dialog<Account> dialog = new AccountDialog(new Account(""));
        Optional<Account> result = dialog.showAndWait();
        result.ifPresent(account -> {
            fxSignIn.setText(account.getName());
        });
    }
}