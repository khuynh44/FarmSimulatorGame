package main.java.entities.items.definitions;

import javafx.scene.paint.Color;

public class FertilizerDef extends ItemDef {
    private final int baseFertilizerAmount;
    private final double baseFertilizerMultiplier;
    private final String colorHex;
    private Color color;

    private FertilizerDef() {
        super("blank_fertilizer", "blank_fertilizer");
        baseCost = 3;
        baseFertilizerAmount = 5;
        baseFertilizerMultiplier = 2;
        this.colorHex = "#000000";
    }

    public FertilizerDef(String id, int baseFertileAmount) {
        super(id, id);
        this.baseFertilizerAmount = baseFertileAmount;
        this.baseFertilizerMultiplier = 2;
        this.colorHex = "#000000";
    }

    public double getBaseMultiplier() {
        return baseFertilizerMultiplier;
    }

    public int getBaseFertilizerAmount() {
        return baseFertilizerAmount;
    }

    public Color getColor() {
        return color;
    }

    public void loadColor() {
        color = Color.web(colorHex);
    }
}
