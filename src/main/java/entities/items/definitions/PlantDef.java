package main.java.entities.items.definitions;

import java.util.ArrayList;

import javafx.scene.image.Image;

public class PlantDef extends ItemDef {
    private final int baseGrowTime;
    private final int idealWaterAmount;
    private final String deadImagePath = null;
    private Image deadImage;

    @SuppressWarnings("unused")
    private PlantDef() {
        this("blank_plant", "Blank Plant", 0, 0, 1);
    }

    public PlantDef(
            String id, String name, int baseGrowTime, int idealWaterAmount, double baseCost) {
        super(id, name);
        this.baseGrowTime = baseGrowTime;
        this.idealWaterAmount = idealWaterAmount;
        this.baseCost = baseCost;
    }

    public PlantDef(String id, String name, int baseGrowTime, ArrayList<Image> arts,
            Image deadImage) {
        this(id, name, baseGrowTime, 0, 1);
        this.arts.addAll(arts);
        this.deadImage = deadImage;
    }

    @Override
    public void loadImages() {
        super.loadImages();
        deadImage = new Image(deadImagePath);
    }

    public Image getDeadImage() {
        return deadImage;
    }

    public int getBaseGrowTime() {
        return baseGrowTime;
    }

    public int getIdealWaterAmount() {
        return idealWaterAmount;
    }
}
