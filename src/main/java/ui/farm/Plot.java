package main.java.ui.farm;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import main.java.entities.items.Fertilizer;
import main.java.entities.items.Plant;
import main.java.ui.inventory.InventoryModel;
import main.java.util.Constants;
import main.java.util.definition.cache.Caches;
import main.java.util.persistence.IPersistable;

public class Plot extends StackPane implements IPersistable {
    private Plant plant;

    private final Rectangle waterMeter = new Rectangle();
    private final Rectangle plantDisplay = new Rectangle();

    private SimpleIntegerProperty waterAmount = new SimpleIntegerProperty();
    private final FertilizerHandler fertilizerHandler;

    public Plot(Plant plant, int waterAmount, FertilizerHandler fertilizerHandler) {
        super();
        if (fertilizerHandler != null) {
            this.fertilizerHandler = fertilizerHandler;
        } else {
            this.fertilizerHandler = new FertilizerHandler();
        }

        setAlignment(this.fertilizerHandler.meter(), Pos.BOTTOM_RIGHT);
        linkMeter(
            this.waterAmount, waterMeter, Color.AQUAMARINE,
            Pos.BOTTOM_LEFT, Constants.WATER_CAP
        );

        this.waterAmount.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if ((int) newValue <= 0 || (int) newValue > Constants.WATER_CAP) {
                    if ((int) newValue < 0) {
                        this.waterAmount.set(0);
                    }

                    if ((int) newValue > Constants.WATER_CAP) {
                        this.waterAmount.set(Constants.WATER_CAP);
                    }

                    if (this.plant != null) {
                        this.plant.kill();
                    }
                }
            }
        });

        this.waterAmount.set(waterAmount);
        this.plant = plant;

        plantDisplay.setWidth(99.0);
        plantDisplay.setHeight(Constants.PLOT_HEIGHT);
        plantDisplay.setStrokeWidth(10);
        plantDisplay.setStrokeType(StrokeType.INSIDE);

        getChildren().add(plantDisplay);
        getChildren().add(waterMeter);
        getChildren().add(this.fertilizerHandler.meter());

        updateImage();

        setStyle("-fx-border-color: black;-fx-border-width: 0 0 1 1;");
    }

    public Plot(int waterAmount) {
        this(null, waterAmount, null);
    }

    public Plot(Plant plant, int waterAmount) {
        this(plant, waterAmount, null);
    }

    private void linkMeter(
            IntegerProperty amount, Rectangle meter, Color colour, Pos position, int cap)  {
        meter.setFill(colour);
        meter.setWidth(8);
        meter.yProperty().bind(plantDisplay.heightProperty());
        meter.heightProperty().bind(Bindings.createDoubleBinding(
            () -> (amount.get() / (double) cap) * Constants.PLOT_HEIGHT,
            amount)
        );
        setAlignment(meter, position);
    }

    public void updateImage() {
        if (isEmpty()) {
            plantDisplay.setFill(Color.WHITE);
            return;
        }

        final Image plantImage = plant.getImage();
        if (plantImage == null) {
            plantDisplay.setFill(Color.RED);
            return;
        }

        plantDisplay.setFill(new ImagePattern(plantImage));
    }

    public void setBorder(boolean active) {
        if (active) {
            plantDisplay.setStroke(Color.BLUEVIOLET);
        } else {
            plantDisplay.setStroke(null);
        }
    }

    public void water() {
        water(10);
    }

    public void water(int amount) {
        setWaterAmount(getWaterAmount() + amount);
    }

    public void unwater(int amount) {
        setWaterAmount(getWaterAmount() - amount);
    }

    public boolean harvest(InventoryModel inventoryModel) {
        final Plant plant = harvest();
        if (plant == null) {
            return false;
        }

        final boolean addToInventory = inventoryModel.incrementCount(plant);
        if (!addToInventory) {
            setPlant(plant);
            return false;
        }

        if (isFertilized() && !plant.isDead()) {
            if (ThreadLocalRandom.current().nextInt(4) == 0) {
                final Plant extraPlant = new Plant(Caches.PLANT_DEFS.get(plant.getId()));
                inventoryModel.incrementCount(extraPlant);
            }
        }

        setPlant(null);
        updateImage();
        return true;
    }

    public Plant harvest() {
        final Plant plant = getPlant();
        if (isEmpty()) {
            return null;
        }

        if (!plant.isRipe()) {
            return null;
        }

        setPlant(null);
        updateImage();
        return plant;
    }

    public void grow() {
        setWaterAmount(getWaterAmount() - 5);
        fertilizerHandler.processFertilizer(1);

        if (isEmpty()) {
            return;
        }

        if (fertilizerHandler.getFertilizerAmount() > 0) {
            plant.grow(getWaterAmount(), fertilizerHandler.getFertilizerMultiplier());
        } else {
            plant.grow(getWaterAmount());
        }
    }

    public boolean fertilize(Fertilizer fertilizer) {
        if (isEmpty()) {
            return false;
        }

        return fertilizerHandler.addFertilizer(fertilizer);
    }

    public boolean isFertilized() {
        return fertilizerHandler.isFertilized();
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
        updateImage();
    }

    public boolean applyPesticide() {
        if (isEmpty()) {
            return false;
        }

        plant.applyPesticide();
        return true;
    }

    public void killPlant() {
        if (isEmpty()) {
            return;
        }

        plant.kill();
    }

    public boolean plantIsPesticided() {
        if (isEmpty()) {
            return false;
        }

        return plant.isPesticided();
    }

    public int getWaterAmount() {
        return waterAmount.get();
    }

    public boolean hasLivePlant() {
        if (isEmpty()) {
            return false;
        }

        return !plant.isDead();
    }

    public void setWaterAmount(int waterAmount) {
        this.waterAmount.set(waterAmount);
    }

    public SimpleIntegerProperty waterAmount() {
        return waterAmount;
    }

    public boolean isEmpty() {
        return plant == null;
    }

    @Override
    public Map<String, Object> generateSaveData() {
        final Map<String, Object> saveData = new HashMap<>();
        saveData.put("plant", plant);
        saveData.put("water_amount", waterAmount.get());
        saveData.put("fertilizer_handler", fertilizerHandler);

        return saveData;
    }

    @SuppressWarnings("unchecked")
    public static Plot fromLoadData(Map<String, Object> loadData) {
        final Map<String, Object> plantLoadData = (Map<String, Object>) loadData.get("plant");
        final Plant plant = Plant.fromLoadData(plantLoadData);
        final Integer waterAmount = (Integer) loadData.get("water_amount");
        final Map<String, Object> fertilizerHandlerLoadData =
            (Map<String, Object>) loadData.get("fertilizer_handler");
        final FertilizerHandler fertilizerHandler =
            FertilizerHandler.fromLoadData(fertilizerHandlerLoadData);

        return new Plot(plant, waterAmount, fertilizerHandler);
    }
}
