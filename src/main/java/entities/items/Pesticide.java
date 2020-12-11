package main.java.entities.items;

import java.util.Map;

import main.java.entities.Affixes;
import main.java.entities.items.definitions.PesticideDef;
import main.java.ui.farm.FieldGrid;
import main.java.util.definition.cache.Caches;

public class Pesticide extends Item<PesticideDef> implements IActivatable {
    public Pesticide(PesticideDef def) {
        super(def);
        affixes = new Affixes(Caches.Affixes.PESTICIDE_PREFIXES.instance(),
                Caches.Affixes.PESTICIDE_SUFFIXES.instance());
    }

    public Pesticide(String id, int activeImageIndex, Affixes affixes) {
        super(Caches.PESTICIDE_DEFS.get(id), activeImageIndex, affixes);
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

        if (!fieldGrid.getSelectedPlot().applyPesticide()) {
            return false;
        }

        return true;
    }

    @SuppressWarnings("unchecked")
    public static Pesticide fromLoadData(Map<String, Object> loadData) {
        final String id = (String) loadData.get("id");
        final int activeImageIndex = (Integer) loadData.get("activeImageIndex");
        final Affixes affixes = Affixes.fromLoadData((Map<String, Object>) loadData.get("affixes"));

        return new Pesticide(id, activeImageIndex, affixes);
    }
}
