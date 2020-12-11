package main.java.entities.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.image.Image;
import main.java.entities.Affixes;
import main.java.entities.items.definitions.ItemDef;
import main.java.util.Equations;
import main.java.util.persistence.IPersistable;

public abstract class Item<T extends ItemDef> implements IPersistable {
    protected Affixes affixes;
    protected int activeImageIndex;
    protected T def;

    protected Item(T def, int activeImageIndex, Affixes affixes) {
        this.def = def;
        this.activeImageIndex = activeImageIndex;
        this.affixes = affixes;
    }

    protected Item(T def, int activeImageIndex) {
        this(def, activeImageIndex, null);
    }

    protected Item(T def) {
        this(def, 0, null);
    }

    public abstract String toString();

    public Image getImage() {
        return def.getImage(activeImageIndex);
    }

    public int getModifier() {
        if (affixes == null) {
            return 0;
        }

        return affixes.getModifier();
    }

    public double getBaseCost() {
        return def.getBaseCost();
    }

    public List<String> generateDescriptionEntries() {
        final List<String> descriptionEntries = new ArrayList<>();

        descriptionEntries.add(String.format("base cost: $%.2f", def.getBaseCost()));
        descriptionEntries.add(String.format("value: $%.2f", getWorth()));

        return descriptionEntries;
    }

    public double getWorth() {
        return Equations.weighWithModifiers(getBaseCost(), getModifier()) + 2;
    }

    public String getId() {
        return def.getId();
    }

    @Override
    public Map<String, Object> generateSaveData() {
        final Map<String, Object> saveData = new HashMap<>();
        saveData.put("id", getId());
        saveData.put("activeImageIndex", activeImageIndex);
        saveData.put("affixes", affixes);

        return saveData;
    }
}
