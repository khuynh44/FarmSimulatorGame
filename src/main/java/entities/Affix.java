package main.java.entities;

import java.util.HashMap;
import java.util.Map;

import main.java.util.definition.cache.Caches;
import main.java.util.definition.cache.ICacheable;
import main.java.util.persistence.IPersistable;

public class Affix implements ICacheable, IPersistable {
    private final String affix;
    private final int modifier;

    @SuppressWarnings("unused")
    private Affix() {
        this.affix = "affix";
        this.modifier = 1;
    }

    public Affix(String affix, int modifier) {
        this.affix = affix;
        this.modifier = modifier;
    }

    public int getModifier() {
        return modifier;
    }

    @Override
    public String getId() {
        return affix;
    }

    @Override
    public String toString() {
        return affix;
    }

    @Override
    public Map<String, Object> generateSaveData() {
        final Map<String, Object> saveData = new HashMap<>();
        saveData.put("id", getId());
        saveData.put("affix", affix);
        saveData.put("modifier", modifier);

        return saveData;
    }

    private static Affix matchId(String id) {
        if (Caches.Affixes.PLANT_PREFIXES.instance().has(id)) {
            return Caches.Affixes.PLANT_PREFIXES.instance().get(id);
        } else if (Caches.Affixes.PLANT_SUFFIXES.instance().has(id)) {
            return Caches.Affixes.PLANT_SUFFIXES.instance().get(id);
        } else if (Caches.Affixes.WORKER_PREFIXS.instance().has(id)) {
            return Caches.Affixes.WORKER_PREFIXS.instance().get(id);
        } else if (Caches.Affixes.WORKER_NAMES.instance().has(id)) {
            return Caches.Affixes.WORKER_NAMES.instance().get(id);
        }

        return null;
    }

    public static Affix fromLoadData(Map<String, Object> loadData) {
        final String id = (String) loadData.get("id");

        Affix affix = matchId(id);
        if (affix != null) {
            return affix;
        }

        final String text = (String) loadData.get("affix");
        final int modifier = (Integer) loadData.get("modifier");
        return new Affix(text, modifier);
    }
}
