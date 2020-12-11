package main.java.entities.items;

import java.util.List;
import java.util.Map;

import main.java.entities.Affixes;
import main.java.entities.items.definitions.FertilizerDef;
import main.java.ui.farm.FieldGrid;
import main.java.util.Equations;
import main.java.util.definition.cache.Caches;

public class Fertilizer extends Item<FertilizerDef> implements IActivatable {
    public Fertilizer(FertilizerDef def) {
        super(def);
        affixes = new Affixes(Caches.Affixes.FERTILIZE_PREFIXES.instance(),
                Caches.Affixes.FERTILIZE_SUFFIXES.instance());
    }

    public Fertilizer(String id, int activeImageIndex, Affixes affixes) {
        super(Caches.FERTILIZER_DEFS.get(id), activeImageIndex, affixes);
    }

    public Fertilizer(String id) {
        this(Caches.FERTILIZER_DEFS.get(id));
    }

    public int getFertilizerAmount() {
        return (int) Equations.weighWithModifiers(def.getBaseFertilizerAmount(), getModifier());
    }

    @Override
    public String toString() {
        return affixes.generateName(def.getName(), "of");
    }

    @Override
    public boolean activate(FieldGrid fieldGrid) {
        if (fieldGrid.getSelectedPlot() == null) {
            return false;
        }

        if (!fieldGrid.getSelectedPlot().fertilize(this)) {
            return false;
        }

        return true;
    }

    @Override
    public List<String> generateDescriptionEntries() {
        final List<String> descriptionEntries = super.generateDescriptionEntries();

        descriptionEntries.add(String.format("fertilizes for: %d", getFertilizerAmount()));
        descriptionEntries.add(String.format("multiplies for: %.1f", def.getBaseMultiplier()));

        return descriptionEntries;
    }

    @SuppressWarnings("unchecked")
    public static Fertilizer fromLoadData(Map<String, Object> loadData) {
        final String id = (String) loadData.get("id");
        final int activeImageIndex = (Integer) loadData.get("activeImageIndex");
        final Affixes affixes = Affixes.fromLoadData((Map<String, Object>) loadData.get("affixes"));

        return new Fertilizer(id, activeImageIndex, affixes);
    }
}
