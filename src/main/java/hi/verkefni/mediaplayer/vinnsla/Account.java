package hi.verkefni.mediaplayer.vinnsla;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Account {

    private final StringProperty nameProperty = new SimpleStringProperty();

    public Account(String name) {
        setName(name);
    }

    public String getName() {
        return nameProperty.get();
    }

    public void setName(String name) {
        nameProperty.set(name);
    }

    public StringProperty nameProperty() {
        return this.nameProperty;
    }
}
