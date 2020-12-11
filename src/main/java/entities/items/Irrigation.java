package main.java.entities.items;

import java.util.Map;

import main.java.entities.items.definitions.IrrigationDef;
import main.java.ui.farm.FieldGrid;
import main.java.util.definition.cache.Caches;

public class Irrigation extends Item<IrrigationDef> implements IActivatable {
    public Irrigation(IrrigationDef def) {
        super(def);
    }

    public Irrigation(String id, int activeImageIndex) {
        super(Caches.IRRIGATION_DEFS.get(id), activeImageIndex);
    }

    @Override
    public String toString() {
        return def.getName();
    }

    @Override
    public boolean activate(FieldGrid fieldGrid) {
        fieldGrid.setIrrigation(true);
        return true;
    }

    public static Irrigation fromLoadData(Map<String, Object> loadData) {
        final String id = (String) loadData.get("id");
        final int activeImageIndex = (Integer) loadData.get("activeImageIndex");

        return new Irrigation(id, activeImageIndex);
    }
}
