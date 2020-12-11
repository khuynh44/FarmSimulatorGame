package main.java.entities;

import java.util.HashMap;
import java.util.Map;

import main.java.util.definition.cache.DefCache;
import main.java.util.persistence.IPersistable;

public class Affixes implements IPersistable {
    private Affix prefix;
    private Affix suffix;

    public Affixes(DefCache<Affix> prefixCache, DefCache<Affix> suffixCache) {
        if (prefixCache != null) {
            this.prefix = prefixCache.getRandom();
        }

        if (suffixCache != null) {
            this.suffix = suffixCache.getRandom();
        }
    }

    public Affixes(Affix prefix, Affix suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public Affix getPrefix() {
        return prefix;
    }

    public Affix getSuffix() {
        return suffix;
    }

    public int getModifier() {
        return (prefix != null ? prefix.getModifier() : 0)
            + (suffix != null ? suffix.getModifier() : 0);
    }

    public String generateName(String baseName, String modifier) {
        return String.format("%s %s%s %s",
            prefix != null ? prefix : "",
            baseName,
            modifier != null ? " " + modifier : "",
            suffix != null ? suffix : "");
    }

    public String generateName(String baseName) {
        return generateName(baseName, null);
    }

    @Override
    public Map<String, Object> generateSaveData() {
        final Map<String, Object> saveData = new HashMap<>();
        saveData.put("prefix", prefix);
        saveData.put("suffix", suffix);

        return saveData;
    }

    @SuppressWarnings("unchecked")
    public static Affixes fromLoadData(Map<String, Object> loadData) {
        final Map<String, Object> prefixLoadData = (Map<String, Object>) loadData.get("prefix");
        final Affix prefix = Affix.fromLoadData(prefixLoadData);

        final Map<String, Object> suffixLoadData = (Map<String, Object>) loadData.get("suffix");
        final Affix suffix = Affix.fromLoadData(suffixLoadData);

        return new Affixes(prefix, suffix);
    }
}
