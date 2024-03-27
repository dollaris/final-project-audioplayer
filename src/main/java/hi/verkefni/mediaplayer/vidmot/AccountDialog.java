package hi.verkefni.mediaplayer.vidmot;

import hi.verkefni.mediaplayer.vinnsla.Account;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;

import java.io.IOException;

public class AccountDialog extends Dialog<Account> {

    private final Account account;
    @FXML
    private TextField userName;


    public AccountDialog(Account account) {
        this.account = account;
        this.setTitle("Sign in");
        setDialogPane(readDialog());
        setResultConverter();
        setPropertyBinding();

    }



    private void setResultConverter() {
        setResultConverter(b -> {
            if (b.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                account.setName(userName.getText());
                return account;
            } else {
                return null;
            }
        });
    }

    private DialogPane readDialog() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("account-view.fxml"));
            fxmlLoader.setController(this);
            return fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setPropertyBinding() {
        userName.textProperty().bindBidirectional(account.nameProperty());
    }
}
