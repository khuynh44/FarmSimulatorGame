package main.java.ui.farm;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import main.java.entities.items.Fertilizer;
import main.java.util.Constants;
import main.java.util.definition.cache.Caches;
import main.java.util.persistence.IPersistable;

public class FertilizerHandler implements IPersistable {
    private SimpleIntegerProperty fertilizerAmount = new SimpleIntegerProperty();
    private final Map<String, Integer> fertilizers;
    private final VBox meter = new VBox();

    public FertilizerHandler(Map<String, Integer> fertilizers, int fertilizerAmount) {
        this.fertilizers = fertilizers;
        this.fertilizerAmount.set(fertilizerAmount);
        meter.setMaxWidth(8);
        meter.setMinWidth(8);
        meter.setMaxHeight(Constants.PLOT_HEIGHT);
        meter.setMinHeight(Constants.PLOT_HEIGHT);
        meter.setAlignment(Pos.BOTTOM_CENTER);
    }

    public FertilizerHandler() {
        this(new HashMap<>(), 0);
    }

    public boolean addFertilizer(Fertilizer fertilizer) {
        if (fertilizerAmount.get() + fertilizer.getFertilizerAmount() > Constants.FERTILIZER_CAP) {
            return false;
        }

        fertilizers.compute(fertilizer.getId(),
            (id, amount) -> fertilizer.getFertilizerAmount() + (amount == null ? 0 : amount));
        addToFertilizerAmount(fertilizer.getFertilizerAmount());
        return true;
    }

    public void processFertilizer(int lossAmount) {
        final Iterator<Map.Entry<String, Integer>> iterator =
            fertilizers.entrySet().iterator();

        while (iterator.hasNext()) {
            final Map.Entry<String, Integer> entry = iterator.next();
            final int amount = entry.getValue();

            final int newAmount = amount - lossAmount;
            if (newAmount <= 0) {
                iterator.remove();
                subtractFromFertilizerAmount(amount);
                continue;
            }

            entry.setValue(newAmount);
            subtractFromFertilizerAmount(lossAmount);
        }
    }

    public int getFertilizerMultiplier() {
        return (int) fertilizers.keySet().stream().mapToDouble(i ->
            Caches.FERTILIZER_DEFS.get(i).getBaseMultiplier()).average().orElse(0);
    }

    public IntegerProperty fertilizerAmount() {
        return fertilizerAmount;
    }

    public int getFertilizerAmount() {
        return fertilizerAmount.get();
    }

    public boolean isFertilized() {
        return getFertilizerAmount() > 0;
    }

    private void updateMeter() {
        meter.getChildren().clear();
        fertilizers.forEach((id, value) -> {
            final Rectangle rect = new Rectangle();
            rect.setFill(Caches.FERTILIZER_DEFS.get(id).getColor());
            rect.setWidth(8);
            rect.setHeight(
                (value / (double) Constants.FERTILIZER_CAP) * Constants.PLOT_HEIGHT
            );
            meter.getChildren().add(0, rect);
        });
    }

    private void subtractFromFertilizerAmount(int amount) {
        fertilizerAmount.set(fertilizerAmount.get() - amount);
        updateMeter();
    }

    private void addToFertilizerAmount(int amount) {
        fertilizerAmount.set(fertilizerAmount.get() + amount);
        updateMeter();
    }

    public VBox meter() {
        return meter;
    }

    @Override
    public Map<String, Object> generateSaveData() {
        final Map<String, Object> saveData = new HashMap<>();
        saveData.put("fertilizers", fertilizers);
        saveData.put("fertilizer_amount", fertilizerAmount.get());

        return saveData;
    }

    @SuppressWarnings("unchecked")
    public static FertilizerHandler fromLoadData(Map<String, Object> loadData) {
        final Map<String, Object> loadedFertilizers =
            (Map<String, Object>) loadData.get("fertilizers");

        final Map<String, Integer> fertilizers = new HashMap<>();
        for (Object value : loadedFertilizers.values()) {
            final List<Object> fertilizerEntry = (List<Object>) value;
            final String id = (String) fertilizerEntry.get(0);
            final int amount = (Integer) fertilizerEntry.get(1);
            fertilizers.put(id, amount);
        }

        final int fertilizerAmount = (Integer) loadData.get("fertilizer_amount");

        return new FertilizerHandler(fertilizers, fertilizerAmount);
    }
}
