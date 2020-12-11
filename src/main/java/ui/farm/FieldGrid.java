package main.java.ui.farm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import main.java.entities.items.IActivatable;
import main.java.entities.items.Item;
import main.java.entities.items.Plant;
import main.java.entities.items.workers.Worker;
import main.java.ui.inventory.InventoryModel;
import main.java.ui.worker_panel.WorkerPanelModel;
import main.java.util.KeyTracker;
import main.java.util.View;
import main.java.util.definition.cache.Caches;
import main.java.util.persistence.IPersistable;
import main.java.util.persistence.ISaveable;
import main.java.util.persistence.Saver;
import main.java.entities.Player;

public class FieldGrid extends GridPane implements IPersistable, ISaveable {
    private InventoryModel inventoryModel;
    private Plot[][] plots;
    private int rowCount;
    private int columnCount;
    private SimpleObjectProperty<Plot> selectedPlot = new SimpleObjectProperty<>();
    private IntegerProperty waterAmount = new SimpleIntegerProperty();
    private IntegerProperty upgradeCost = new SimpleIntegerProperty(10);
    private WorkerPanelModel workerModel;

    private boolean hasTractor = false;
    private boolean hasIrrigation = false;

    public FieldGrid() {
        super();

        selectedPlot.addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) {
                waterAmount.unbindBidirectional(oldValue.waterAmount());
                oldValue.setBorder(false);
            }

            if (newValue != null) {
                waterAmount.bindBidirectional(newValue.waterAmount());
                newValue.setBorder(true);
            }
        });
    }

    public void onLoad() {
        workerModel.startWorkHandlers();
    }

    public void setInventoryModel(InventoryModel inventoryModel) {
        this.inventoryModel = inventoryModel;
    }

    private void unregisterAllPlots() {
        for (int i = 0; i < this.plots.length; i++) {
            for (int j = 0; j < this.plots[i].length; j++) {
                final Plot oldPlot = this.plots[i][j];
                workerModel.unregisterWithWorkHandlers(oldPlot);
                getChildren().remove(oldPlot);
            }
        }
    }

    public void registerNewPlots(List<Plot> plots) {
        final Plot[][] newPlots = new Plot[rowCount][columnCount];

        unregisterAllPlots();

        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                final Plot newPlot = plots.get(i * columnCount + j);
                linkPlot(newPlot);
                newPlots[i][j] = newPlot;
                workerModel.registerWithWorkHandlers(newPlot);

                add(newPlot, j, i);
                setHalignment(newPlot, HPos.CENTER);
                setValignment(newPlot, VPos.CENTER);
            }
        }

        this.plots = newPlots;
    }

    public boolean isGridEntirelyDeadOrEmpty() {
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                if (plots[i][j].hasLivePlant()) {
                    return false;
                }
            }
        }

        return true;
    }

    public int ourGetRowCount() {
        return this.rowCount;
    }

    public void initialize(InventoryModel inventoryModel, WorkerPanelModel workerModel) {
        this.inventoryModel = inventoryModel;
        this.workerModel = workerModel;
        rowCount = getRowCount();
        columnCount = getColumnCount();

        plots = new Plot[rowCount][columnCount];
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                final Plot plot = initializePlot();
                plots[i][j] = plot;

                add(plot, j, i);
                setHalignment(plot, HPos.CENTER);
                setValignment(plot, VPos.CENTER);
            }
        }
        workerModel.stopWorkHandlers();
    }

    public void regenerate() {
        hasTractor = false;
        hasIrrigation = false;
        unregisterAllPlots();
        workerModel.removeAllWorkers();
        initialize(inventoryModel, workerModel);
    }

    public void addRow() {
        if (!Player.getInstance().getWallet().burnMoney(upgradeCost.get())) {
            return;
        }

        upgradeCost.set(upgradeCost.get() * 2);
        final Plot[][] newPlots = new Plot[rowCount + 1][columnCount];
        for (int i = 0; i < rowCount; i++) {
            System.arraycopy(plots[i], 0, newPlots[i], 0, columnCount);
        }

        for (int j = 0; j < columnCount; j++) {
            final Plot plot = initializeEmptyPlot();
            newPlots[rowCount][j] = plot;

            add(plot, j, rowCount);
            setHalignment(plot, HPos.CENTER);
            setValignment(plot, VPos.CENTER);
        }

        rowCount++;
        plots = newPlots;
    }

    private void applyToEachPlot(PlotAction action) {
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                action.run(plots[i][j]);
            }
        }
    }

    private void linkPlot(Plot plot) {
        plot.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (selectedPlot.get() == plot) {
                    if (!KeyTracker.isInterestingKeyPressed()) {
                        selectedPlot.set(null);
                    }
                } else {
                    selectedPlot.set(plot);
                }

                if (KeyTracker.isKeyPressed(KeyCode.CONTROL)) {
                    ((FarmController) View.FARM.getController()).harvest(null);
                }

                if (KeyTracker.isKeyPressed(KeyCode.SHIFT)) {
                    ((FarmController) View.FARM.getController()).water(null);
                }

                if (KeyTracker.isKeyPressed(KeyCode.Z)) {
                    ((FarmController) View.FARM.getController()).plant(null);
                }
            }
        });
    }

    private Plot initializeEmptyPlot() {
        final Plot plot = new Plot(ThreadLocalRandom.current().nextInt(30) + 35);
        linkPlot(plot);
        workerModel.registerWithWorkHandlers(plot);
        return plot;
    }

    private Plot initializePlot() {
        final Plot plot = initializeEmptyPlot();

        if (ThreadLocalRandom.current().nextInt(6) != 0) {
            final Plant plant = new Plant(Caches.PLANT_DEFS.getRandom());
            plant.grow(ThreadLocalRandom.current().nextInt(plant.getBaseGrowTime() + 1));
            plot.setPlant(plant);
        }

        return plot;
    }

    public void grow() {
        applyToEachPlot((Plot plot) -> {
            plot.grow();
            plot.updateImage();
            workerModel.refreshWorkHandlers(plot);
        });
    }

    public boolean waterSelected() {
        if (getSelectedPlot() == null) {
            return false;
        }

        getSelectedPlot().water();
        getSelectedPlot().updateImage();
        workerModel.refreshWorkHandlers(getSelectedPlot());
        return true;
    }

    public void waterAll(int amount) {
        applyToEachPlot((Plot plot) -> {
            plot.water(amount);
            plot.updateImage();
        });
    }

    public void unwaterAll(int amount) {
        applyToEachPlot((Plot plot) -> {
            plot.unwater(amount);
            plot.updateImage();
        });
    }

    public int killRandomPlants() {
        final AtomicInteger plantsKilled = new AtomicInteger(0);
        applyToEachPlot((Plot plot) -> {
            int upperBound =
                (int) (10 * Player.getInstance().getDifficulty().getEnvironmentalModifier());
            if (ThreadLocalRandom.current().nextInt(upperBound) == 0) {
                if (!plot.plantIsPesticided() && plot.hasLivePlant()) {
                    plot.killPlant();
                    plot.updateImage();
                    plantsKilled.getAndIncrement();
                }
            }
        });

        return plantsKilled.get();
    }

    public boolean harvest(Plot plot) {
        if (plot == null) {
            return false;
        }

        final boolean success = plot.harvest(inventoryModel);
        if (success) {
            workerModel.refreshWorkHandlers(plot);
        }

        return success;
    }

    public void setTractor(boolean boo) {
        hasTractor = boo;
    }

    public boolean hasTractor() {
        return hasTractor;
    }

    public void setIrrigation(boolean boo) {
        hasIrrigation = boo;
    }

    public boolean hasIrrigation() {
        return hasIrrigation;
    }

    public boolean harvestSelected() {
        return harvest(getSelectedPlot());
    }

    public void activateSelectedItem() {
        final Item<?> selectedItem = inventoryModel.getSelectedItem();
        if (!(selectedItem instanceof IActivatable)) {
            return;
        }

        if (((IActivatable) selectedItem).activate(this)) {
            inventoryModel.decrementCount(selectedItem);
        }
    }

    public void addWorker(Worker worker) {
        workerModel.addWorker(worker);
    }

    public IntegerProperty waterAmount() {
        return waterAmount;
    }

    public IntegerProperty upgradeCost() {
        return upgradeCost;
    }

    public boolean plant() {
        final Item<?> selectedItem = inventoryModel.getSelectedItem();
        if (!(selectedItem instanceof Plant)) {
            return false;
        }

        if (!getSelectedPlot().isEmpty()) {
            return false;
        }

        getSelectedPlot().setPlant((Plant) selectedItem);
        inventoryModel.decrementCount(selectedItem);
        return true;
    }

    public SimpleObjectProperty<Plot> selectedPlot() {
        return selectedPlot;
    }

    public Plot getSelectedPlot() {
        return selectedPlot.get();
    }

    @Override
    public Map<String, Object> generateSaveData() {
        final Map<String, Object> saveData = new HashMap<>();
        saveData.put("plots", plots);
        saveData.put("row_count", rowCount);
        saveData.put("column_count", columnCount);
        saveData.put("upgrade_cost", upgradeCost.get());
        saveData.put("has_tractor", hasTractor);
        saveData.put("has_irrigation", hasIrrigation);

        return saveData;
    }

    @Override
    public void save(String prefix) throws IOException {
        Saver.save(this, prefix + "_field_grid.yaml");
        workerModel.save(prefix);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void load(String prefix) throws IOException {
        final Map<String, Object> loadData = Saver.load(prefix + "_field_grid.yaml");

        final Integer upgradeCost = (Integer) loadData.get("upgrade_cost");
        this.upgradeCost.set(upgradeCost);

        final boolean hasTractor = (boolean) loadData.get("has_tractor");
        this.hasTractor = hasTractor;
        final boolean hasIrrigation = (boolean) loadData.get("has_irrigation");
        this.hasIrrigation = hasIrrigation;

        final Integer columnCount = (Integer) loadData.get("column_count");
        this.columnCount = columnCount;
        final Integer rowCount = (Integer) loadData.get("row_count");
        this.rowCount = rowCount;

        final List<List<Object>> plotObjects = (List<List<Object>>) loadData.get("plots");
        final List<Plot> plots = new ArrayList<>();
        for (List<Object> rowObj : plotObjects) {
            for (Object obj : rowObj) {
                final Plot plot = Plot.fromLoadData((Map<String, Object>) obj);
                plots.add(plot);
            }
        }

        registerNewPlots(plots);

        workerModel.load(prefix);
    }

    @FunctionalInterface
    public interface PlotAction {
        public void run(Plot plot);
    }
}
