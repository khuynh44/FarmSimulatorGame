package main.java.ui.farm;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.util.converter.NumberStringConverter;
import main.java.entities.Player;
import main.java.ui.Controller;
import main.java.ui.inventory.InventoryController;
import main.java.ui.worker_panel.WorkerPanelController;
import main.java.ui.worker_panel.WorkerPanelModel;
import main.java.util.View;

public class FarmController extends Controller<FarmModel> {
    @FXML
    private Text moneyLabel;
    @FXML
    private Text nameText;
    @FXML
    private FieldGrid fieldGrid;
    @FXML
    private InventoryController inventoryController;
    @FXML
    private WorkerPanelController workerController;
    @FXML
    private ButtonBar cropMenu;
    @FXML
    private ButtonBar specialMenu;
    @FXML
    private Button muteButton;
    @FXML
    private Button endGameButton;
    @FXML
    private Label waterLabel;
    @FXML
    private Text dayText;
    @FXML
    private Text upgradeCost;
    @FXML
    private ListView<String> eventList;

    @Override
    public void initialize(FarmModel model) {
        super.initialize(model);

        nameText.textProperty().bindBidirectional(Player.getInstance().name());
        moneyLabel.textProperty().bindBidirectional(Player.getInstance().getWallet().balance(),
            new NumberStringConverter("$0.00"));
        waterLabel.textProperty().bindBidirectional(fieldGrid.waterAmount(),
            new NumberStringConverter("Water: #"));
        dayText.textProperty().bindBidirectional(model.day(), new NumberStringConverter("Day: #"));
        upgradeCost.textProperty().bindBidirectional(fieldGrid.upgradeCost(),
                new NumberStringConverter("Cost: #"));

        eventList.itemsProperty().bindBidirectional(model.eventList());

        inventoryController.initialize(Player.getInstance().getInventoryModel());
        model.setInventoryController(inventoryController);

        workerController.initialize(new WorkerPanelModel());
        model.setWorkerModel(workerController.getModel());

        fieldGrid.initialize(inventoryController.getModel(), workerController.getModel());

        cropMenu.visibleProperty().bind(fieldGrid.selectedPlot().isNotNull());
        specialMenu.visibleProperty().bind(
            inventoryController.getModel().isSpecialItemSelected()
        );

        endGameButton.disableProperty().bind(model.isGameOver().not());

        muteButton.textProperty().bindBidirectional(model.muteText());

        model.setFieldGrid(fieldGrid);
    }

    public void goToMarket(ActionEvent event) {
        View.MARKET.setViewOn(event);
    }

    public void goToPersistence(ActionEvent event) {
        View.PERSISTENCE.setViewOn(event);
    }

    public void switchToEndScreen(ActionEvent event) {
        model.switchToEndScreen(event);
    }

    public void harvest(ActionEvent event) {
        model.harvest();
    }

    public void water(ActionEvent event) {
        model.water();
    }

    public void grow(ActionEvent event) {
        model.grow();
    }

    public void plant(ActionEvent event) {
        model.plant();
    }

    public void activate(ActionEvent event) {
        fieldGrid.activateSelectedItem();
    }

    public void upgrade(ActionEvent event) {
        fieldGrid.addRow();
    }

    public void toggleMute() {
        model.toggleMute();
    }
}
