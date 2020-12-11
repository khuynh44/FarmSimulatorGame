package main.java.ui.config;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import main.java.entities.Difficulty;
import main.java.entities.Player;
import main.java.entities.items.Plant;
import main.java.ui.Controller;
import main.java.util.View;

public class ConfigController extends Controller<ConfigModel> {
    @FXML
    private ComboBox<Difficulty> difficultyBox;
    @FXML
    private ComboBox<Plant> seedBox;
    @FXML
    private ComboBox<String> seasonBox;
    @FXML
    private TextField nameField;
    @FXML
    private Text nameException;
    @FXML
    private Text difficultyException;
    @FXML
    private Text seedException;

    @Override
    public void initialize(ConfigModel model) {
        super.initialize(model);

        difficultyBox.itemsProperty().bindBidirectional(model.difficultyList());
        difficultyBox.valueProperty().bindBidirectional(model.difficulty());
        seedBox.itemsProperty().bindBidirectional(model.seedList());
        seedBox.valueProperty().bindBidirectional(model.seed());
        seasonBox.itemsProperty().bindBidirectional(model.seasonList());

        nameField.textProperty().bindBidirectional(model.name());
    }

    public void switchToFarmScreen(ActionEvent event) {
        boolean exception = false;

        nameException.setVisible(false);
        difficultyException.setVisible(false);
        seedException.setVisible(false);

        String name = model.getName();
        if (name == null || name.trim().isEmpty()) {
            nameException.setVisible(true);
            exception = true;
        } else {
            name = name.trim();
        }

        if (model.getDifficulty() == null) {
            difficultyException.setVisible(true);
            exception = true;
        }

        if (model.getSeed() == null) {
            seedException.setVisible(true);
            exception = true;
        }

        if (exception) {
            return;
        }

        model.setMoney(model.getDifficulty().getStartingMoney());

        Player.getInstance().setDifficulty(model.getDifficulty());
        Player.getInstance().setName(model.getName());
        Player.getInstance().getWallet().operationOnMoney(model.getMoney());
        Player.getInstance().getInventoryModel().incrementCount(model.getSeed());

        View.FARM.setViewOn(event);
    }
}
