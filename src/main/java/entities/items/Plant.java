package main.java.entities.items;

import java.util.List;
import java.util.Map;

import javafx.scene.image.Image;
import main.java.entities.Affixes;
import main.java.entities.items.definitions.PlantDef;
import main.java.util.Equations;
import main.java.util.definition.cache.Caches;

public class Plant extends Item<PlantDef> {
    public static enum Growth {
        SEED,
        RIPENING,
        RIPE,
        OVERRIPE,
        DEAD;
    }

    private boolean hasPesticide;
    private int currentGrowthTime;
    private Growth currentGrowth = Growth.SEED;

    private Plant(String id, int currentGrowthTime,
            Growth currentGrowth, int activeImageIndex, Affixes affixes, boolean hasPesticide) {
        super(Caches.PLANT_DEFS.get(id), activeImageIndex, affixes);
        this.currentGrowth = currentGrowth;
        this.currentGrowthTime = currentGrowthTime;
        this.hasPesticide = hasPesticide;
    }

    public Plant(PlantDef def, int currentGrowthTime) {
        super(def);
        this.currentGrowthTime = currentGrowthTime;
        this.hasPesticide = false;
        affixes = new Affixes(Caches.Affixes.PLANT_PREFIXES.instance(),
                                Caches.Affixes.PLANT_SUFFIXES.instance());
        if (def != null) {
            growHelper(0);
        }
    }

    public Plant(PlantDef plantDef) {
        this(plantDef, 0);
    }

    public Plant(String id) {
        this(Caches.PLANT_DEFS.get(id));
    }

    public void grow(int waterAmount) {
        grow(waterAmount, 1.0);
    }

    public void grow(int waterAmount, double fertilizeMultiplier) {
        final double x = (getIdealWaterAmount() - waterAmount) / (double) getIdealWaterAmount();

        growHelper((int) (fertilizeMultiplier * (7.5 * Equations.gaussian(x, 0, 2 / 5.0))));
    }

    public void grow() {
        growHelper(1);
    }

    private void growHelper(int growthAmount) {
        if (isDead()) {
            return;
        }

        currentGrowthTime += growthAmount;

        float percentGrown = currentGrowthTime / ((float) def.getBaseGrowTime());

        if (percentGrown > 1.5f) {
            currentGrowth = Growth.OVERRIPE;
        } else if (percentGrown >= 1f) {
            currentGrowth = Growth.RIPE;
        } else if (percentGrown < 0.1f) {
            currentGrowth = Growth.SEED;
        } else {
            currentGrowth = Growth.RIPENING;
        }

        final int artOrdinal = Math.min((int) (percentGrown * (def.getArtsCount() - 1)),
            def.getArtsCount() - 1);
        activeImageIndex = artOrdinal;
    }

    public void kill() {
        currentGrowth = Growth.DEAD;
    }

    @Override
    public double getWorth() {
        double worth = super.getWorth();
        if (currentGrowth == Growth.DEAD) {
            worth *= 0.1;
        } else if (currentGrowth == Growth.RIPE) {
            worth *= 3;
        } else {
            worth *= (currentGrowth.ordinal() + 1) / ((double) Growth.values().length + 1);
        }
        if (hasPesticide) {
            worth *= 0.8;
        }

        return worth;
    }

    @Override
    public Image getImage() {
        if (isDead()) {
            return def.getDeadImage();
        }

        return def.getImage(activeImageIndex);
    }

    public boolean isRipe() {
        return currentGrowth.ordinal() >= Growth.RIPE.ordinal();
    }

    public boolean isDead() {
        return currentGrowth == Growth.DEAD;
    }

    public int getCurrentGrowthTime() {
        return currentGrowthTime;
    }

    public Growth getCurrentGrowth() {
        return currentGrowth;
    }

    public int getBaseGrowTime() {
        return def.getBaseGrowTime();
    }

    public int getIdealWaterAmount() {
        return def.getIdealWaterAmount();
    }

    public void applyPesticide() {
        hasPesticide = true;
    }

    public boolean isPesticided() {
        return hasPesticide;
    }

    @Override
    public List<String> generateDescriptionEntries() {
        List<String> descriptionEntries = super.generateDescriptionEntries();

        descriptionEntries.add(String.format("grow time: %d/%d",
            getCurrentGrowthTime(), getBaseGrowTime()));
        descriptionEntries.add(String.format("grow state: %s", getCurrentGrowth()));
        descriptionEntries.add(String.format("ideal water: %s", getIdealWaterAmount()));
        descriptionEntries.add(String.format("is pesticided: %s", String.valueOf(isPesticided())));

        return descriptionEntries;
    }

    @Override
    public String toString() {
        return affixes.generateName(def.getName(), "of");
    }

    @Override
    public Map<String, Object> generateSaveData() {
        final Map<String, Object> saveData = super.generateSaveData();
        saveData.put("hasPesticide", hasPesticide);
        saveData.put("currentGrowthTime", currentGrowthTime);
        saveData.put("currentGrowth", currentGrowth.ordinal());

        return saveData;
    }

    @SuppressWarnings("unchecked")
    public static Plant fromLoadData(Map<String, Object> loadData) {
        if (loadData == null) {
            return null;
        }

        final String id = (String) loadData.get("id");
        final boolean hasPesticide = (boolean) loadData.get("hasPesticide");
        final int currentGrowthTime = (Integer) loadData.get("currentGrowthTime");
        final Growth currentGrowth = Growth.values()[(Integer) loadData.get("currentGrowth")];
        final int activeImageIndex = (Integer) loadData.get("activeImageIndex");
        final Affixes affixes = Affixes.fromLoadData((Map<String, Object>) loadData.get("affixes"));

        return new Plant(id, currentGrowthTime, currentGrowth,
            activeImageIndex, affixes, hasPesticide);
    }
}
