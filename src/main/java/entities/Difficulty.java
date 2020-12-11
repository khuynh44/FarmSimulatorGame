package main.java.entities;

import java.util.HashMap;
import java.util.Map;

import main.java.util.definition.cache.Caches;
import main.java.util.definition.cache.ICacheable;
import main.java.util.persistence.IPersistable;

public class Difficulty implements ICacheable, IPersistable {
    private final String id;
    private final String name;
    private final int startingMoney;
    private final double workerCostMultiplier;
    private final double pesticideCostMultiplier;
    private final double environmentalModifier;

    @SuppressWarnings("unused")
    private Difficulty() {
        this.name = "EZ";
        this.id = "ez";
        this.startingMoney = 999;
        this.workerCostMultiplier = 1.00;
        this.pesticideCostMultiplier = 1.00;
        this.environmentalModifier = 1.00;
    }

    public Difficulty(String name, int startingMoney, double workerCostMultiplier,
            double pesticideCostMultiplier, double environmentalModifier) {
        this.name = name;
        this.id = name;
        this.startingMoney = startingMoney;
        this.workerCostMultiplier = workerCostMultiplier;
        this.pesticideCostMultiplier = pesticideCostMultiplier;
        this.environmentalModifier = environmentalModifier;
    }

    public int getStartingMoney() {
        return startingMoney;
    }

    public String getName() {
        return name;
    }

    public double getWorkerCostMultiplier() {
        return workerCostMultiplier;
    }

    public double getPesticideCostMultiplier() {
        return pesticideCostMultiplier;
    }

    public double getEnvironmentalModifier() {
        return environmentalModifier;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Map<String, Object> generateSaveData() {
        final Map<String, Object> saveData = new HashMap<>();
        saveData.put("id", id);

        return saveData;
    }

    public static Difficulty fromLoadData(Map<String, Object> loadData) {
        final String id = (String) loadData.get("id");
        return Caches.DIFFICULTIES.get(id);
    }
}
