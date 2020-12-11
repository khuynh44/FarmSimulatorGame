package main.java.entities.items;

import java.util.Map;

import main.java.entities.items.definitions.TractorDef;
import main.java.ui.farm.FieldGrid;
import main.java.util.definition.cache.Caches;

public class Tractor extends Item<TractorDef> implements IActivatable {
    public Tractor(TractorDef def) {
        super(def);
    }

    public Tractor(String id, int activeImageIndex) {
        super(Caches.TRACTOR_DEFS.get(id), activeImageIndex);
    }

    @Override
    public String toString() {
        return def.getName();
    }

    @Override
    public boolean activate(FieldGrid fieldGrid) {
        fieldGrid.setTractor(true);
        return true;
    }

    public static Tractor fromLoadData(Map<String, Object> loadData) {
        final String id = (String) loadData.get("id");
        final int activeImageIndex = (Integer) loadData.get("activeImageIndex");

        return new Tractor(id, activeImageIndex);
    }
}
