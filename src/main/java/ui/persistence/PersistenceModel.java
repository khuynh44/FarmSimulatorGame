package main.java.ui.persistence;

import java.io.IOException;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import main.java.entities.Player;
import main.java.ui.Model;
import main.java.ui.market.MarketController;
import main.java.util.View;
import main.java.util.persistence.ISaveable;

public class PersistenceModel extends Model {
    private final StringProperty statusText = new SimpleStringProperty();

    public void save() throws IOException {
        statusText.set("Saving");
        Player.getInstance().save("player");
        ((ISaveable) View.MARKET.getModel()).save("market");
        ((ISaveable) View.FARM.getModel()).save("farm");
        statusText.set("Saved");
    }

    public void load() {
        statusText.set("Loading");
        try {
            Player.getInstance().load("player");
            ((ISaveable) View.MARKET.getModel()).load("market");
            ((MarketController) View.MARKET.getController()).reinitialize();
            ((ISaveable) View.FARM.getModel()).load("farm");
        } catch (Exception e) {
            statusText.set("Invalid Load Files");
            return;
        }
        statusText.set("Loaded");
    }

    public StringProperty statusText() {
        return statusText;
    }
}
