package main.java.ui.farm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import main.java.entities.Player;
import main.java.ui.Model;
import main.java.ui.inventory.InventoryController;
import main.java.ui.inventory.InventoryModel;
import main.java.ui.market.MarketModel;
import main.java.ui.worker_panel.WorkerPanelModel;
import main.java.util.Constants;
import main.java.util.events.Event;
import main.java.util.Scheduler;
import main.java.util.View;
import main.java.util.events.Rain;
import main.java.util.persistence.IPersistable;
import main.java.util.persistence.ISaveable;
import main.java.util.persistence.Saver;
import main.java.util.events.Drought;
import main.java.util.events.Locusts;
import main.java.util.sound.SoundController;

public class FarmModel extends Model implements IPersistable, ISaveable {
    private final SimpleStringProperty muteText = new SimpleStringProperty("Mute");
    private final SimpleIntegerProperty day = new SimpleIntegerProperty(1);
    private final ObjectProperty<ObservableList<String>> eventList =
        new SimpleObjectProperty<>(FXCollections.observableArrayList());
    private InventoryController inventoryController;
    private FieldGrid fieldGrid;
    private WorkerPanelModel workerModel;

    private final BooleanProperty isGameOver = new SimpleBooleanProperty(false);
    private final List<Event> events = new ArrayList<>();

    private int timesWatered = 0;
    private int timesHarvested = 0;

    public FarmModel() {
        final Event growEvent = new Event(() -> grow(), Constants.DAY_LENGTH);
        events.add(growEvent);
        events.add(new Rain(Constants.DAY_LENGTH * 2, this));
        events.add(new Drought(Constants.DAY_LENGTH * 2, this));
        events.add(new Locusts(Constants.DAY_LENGTH * 2, this));

        Scheduler.register(growEvent);
        Scheduler.disableEvents(events);
    }

    public void rain(int amount) {
        fieldGrid.waterAll(amount);
    }

    public void unWater(int amount) {
        fieldGrid.unwaterAll(amount);
    }

    public int locustPlague() {
        return fieldGrid.killRandomPlants();
    }

    private void zeroWateringAndHarvesting() {
        timesWatered = 0;
        timesHarvested = 0;
    }

    public void grow() {
        fieldGrid.grow();
        zeroWateringAndHarvesting();
        day.set(day.get() + 1);

        if (day.get() % 14 == 0) {
            workerModel.payWorkers();
        }

        checkGameLost();
        checkGameWon();
    }

    @Override
    public void onLoad() {
        inventoryController.getModel().onLoad();
        fieldGrid.onLoad();

        Scheduler.enableEvents(events);
    }

    public SimpleStringProperty muteText() {
        return muteText;
    }

    public void toggleMute() {
        muteText.set(SoundController.toggleMuteBackgroundMusic() ? "Unmute" : "Mute");
    }

    public boolean checkGameLost() {
        if (Player.getInstance().getWallet().getBalance() > Constants.MONEY_TO_LOSE) {
            return false;
        }

        if (!fieldGrid.isGridEntirelyDeadOrEmpty()) {
            return false;
        }

        if (!Player.getInstance().getInventoryModel().hasAllDeadCrops()) {
            return false;
        }

        isGameOver.set(true);
        return true;
    }

    public boolean checkGameWon() {
        if (Player.getInstance().getWallet().getBalance() <= Constants.MONEY_TO_WIN) {
            return false;
        }

        isGameOver.set(true);
        return true;
    }

    public BooleanProperty isGameOver() {
        return isGameOver;
    }

    public void postMessage(String message) {
        eventList.get().add(0, message);
    }

    public void setInventoryController(InventoryController inventoryController) {
        this.inventoryController = inventoryController;
    }

    public InventoryController getInventoryController() {
        return inventoryController;
    }

    public InventoryModel getPlayerInventoryModel() {
        return inventoryController.getModel();
    }

    public SimpleIntegerProperty day() {
        return day;
    }

    public void setFieldGrid(FieldGrid fieldGrid) {
        this.fieldGrid = fieldGrid;
    }

    public void setWorkerModel(WorkerPanelModel workerModel) {
        this.workerModel = workerModel;
    }

    public ObjectProperty<ObservableList<String>> eventList() {
        return eventList;
    }

    public void water() {
        if (timesWatered > Constants.WATERING_CAP && !fieldGrid.hasIrrigation()) {
            postMessage("You have watered too much for today!");
            return;
        }

        if (fieldGrid.waterSelected()) {
            timesWatered++;
            SoundController.play("water");
        }
    }

    public void harvest() {
        if (timesHarvested > Constants.HARVESTING_CAP && !fieldGrid.hasTractor()) {
            postMessage("You have harvested too much for today!");
            return;
        }

        if (fieldGrid.harvestSelected()) {
            timesHarvested++;
            SoundController.play("harvest");
        }
    }

    public void plant() {
        if (fieldGrid.plant()) {
            SoundController.play("shovel");
        }
    }

    public void switchToEndScreen(ActionEvent event) {
        Player.getInstance().getInventoryModel().clear();
        fieldGrid.regenerate();
        ((MarketModel) View.MARKET.getModel()).resetMarket();
        Player.getInstance().getWallet().zeroBalance();
        isGameOver.set(false);

        View.END.setViewOn(event);
    }

    @Override
    public Map<String, Object> generateSaveData() {
        final Map<String, Object> saveData = new HashMap<>();
        saveData.put("day", day.get());
        saveData.put("is_game_over", isGameOver.get());
        saveData.put("times_watered", timesWatered);
        saveData.put("times_harvested", timesHarvested);
        saveData.put("mute", muteText.get());

        return saveData;
    }

    @Override
    public void save(String prefix) throws IOException {
        Saver.save(this, prefix + "_farm.yaml");
        fieldGrid.save(prefix);
    }

    @Override
    public void load(String prefix) throws IOException {
        final Map<String, Object> loadData = Saver.load(prefix + "_farm.yaml");
        final int day = (Integer) loadData.get("day");
        this.day.set(day);
        final boolean isGameOver = (boolean) loadData.get("is_game_over");
        this.isGameOver.set(isGameOver);
        final int timesWatered = (Integer) loadData.get("times_watered");
        this.timesWatered = timesWatered;
        final int timesHarvested = (Integer) loadData.get("times_harvested");
        this.timesHarvested = timesHarvested;
        final String muteText = (String) loadData.get("mute");
        this.muteText.set(muteText);
        if (muteText.equals("Mute")) {
            SoundController.setMuteBackgroundMusic(false);
        } else if (muteText.equals("Unmute")) {
            SoundController.setMuteBackgroundMusic(true);
        }

        fieldGrid.load(prefix);
    }
}
