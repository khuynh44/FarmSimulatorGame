package main.java.ui.persistence;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import main.java.ui.Controller;
import main.java.util.View;

public class PersistenceController extends Controller<PersistenceModel> {
    @FXML
    private Label statusLabel;

    public void initialize(PersistenceModel model) {
        super.initialize(model);

        statusLabel.setTextFill(Color.RED);
        statusLabel.textProperty().bindBidirectional(model.statusText());
    }

    public void save(ActionEvent event) throws IOException {
        model.save();
    }

    public void load(ActionEvent event) {
        model.load();
    }

    public void goToFarm(ActionEvent event) {
        View.FARM.setViewOn(event);
    }
}
