package main.java.ui.market;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.util.converter.NumberStringConverter;

import main.java.entities.Player;
import main.java.ui.Controller;
import main.java.ui.inventory.InventoryController;
import main.java.util.View;

public class MarketController extends Controller<MarketModel> {
    @FXML
    private InventoryController marketInventoryController;
    @FXML
    private InventoryController playerInventoryController;
    @FXML
    private Label moneyLabel;
    @FXML
    private Label itemCostLabel;
    @FXML
    private Text marketName;

    @Override
    public void initialize(MarketModel model) {
        // to not call super because we need to re-initialize on load
        this.model = model;
        reinitialize();
    }

    public void reinitialize() {
        marketInventoryController.initialize(model.getMarketInventoryModel());
        playerInventoryController.initialize(Player.getInstance().getInventoryModel());

        moneyLabel.textProperty().unbind();
        moneyLabel.textProperty().bindBidirectional(Player.getInstance().getWallet().balance(),
            new NumberStringConverter("$0.00"));
        itemCostLabel.textProperty().unbind();
        itemCostLabel.textProperty().bindBidirectional(model.itemCost(),
            new NumberStringConverter("Cost: $0.00"));

        marketName.setText(model.getMarketName());
    }

    public void buyItem(ActionEvent event) {
        model.buyItem();
    }

    public void sellItem(ActionEvent event) {
        model.sellItem();
    }

    public void switchToFarmScreen(ActionEvent event) {
        View.FARM.setViewOn(event);
    }
}
