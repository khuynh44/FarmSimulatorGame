package main.java.ui.inventory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.java.entities.items.Fertilizer;
import main.java.entities.items.Irrigation;
import main.java.entities.items.Item;
import main.java.entities.items.Pesticide;
import main.java.entities.items.Plant;
import main.java.entities.items.Tractor;
import main.java.entities.items.workers.Worker;
import main.java.util.definition.cache.Caches;
import main.java.util.persistence.IPersistable;

public class Inventory implements IPersistable {
    public static final int INF_CAP = -1;

    private final HashMap<Item<?>, Integer> inventory = new HashMap<>();
    private int capacity;
    private int itemCount;

    public Inventory(int capacity) {
        this.capacity = capacity;
    }

    public Inventory() {
        this(8);
    }

    public Integer getCount(Item<?> item) {
        return inventory.getOrDefault(item, 0);
    }

    public boolean decrementCount(Item<?> item) {
        return operationOnCount(item, -1);
    }

    public boolean incrementCount(Item<?> item) {
        return operationOnCount(item, 1);
    }

    public boolean operationOnCount(Item<?> item, int amount) {
        Integer currentItemCount = getCount(item);
        Integer newItemCount = currentItemCount + amount;

        if (newItemCount < 0) {
            return false;
        }

        if (capacity != INF_CAP && itemCount + amount > capacity) {
            return false;
        }

        if (newItemCount == 0) {
            inventory.remove(item);
        } else {
            inventory.put(item, newItemCount);
        }

        itemCount += amount;
        return true;
    }

    public HashMap<Item<?>, Integer> expose() {
        return inventory;
    }

    public void clear() {
        inventory.clear();
        itemCount = 0;
    }

    public String generateCapacityString() {
        return String.format("%s / %s", itemCount, capacity);
    }

    public boolean hasAllDeadCrops() {
        for (Item<?> item : inventory.keySet()) {
            if (!(item instanceof Plant)) {
                continue;
            }
            if (!((Plant) item).isDead()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public Map<String, Object> generateSaveData() {
        final Map<String, Object> saveData = new HashMap<>();
        saveData.put("inventory", inventory);
        saveData.put("capacity", capacity);
        saveData.put("itemCount", itemCount);

        return saveData;
    }

    @SuppressWarnings("unchecked")
    public void processLoadData(Map<String, Object> loadData) {
        clear();

        final Map<String, Object> inventoryItems = (Map<String, Object>) loadData.get("inventory");
        for (Object value : inventoryItems.values()) {
            final List<Object> inventoryEntry = (List<Object>) value;
            final Map<String, Object> itemLoadData = (Map<String, Object>) inventoryEntry.get(0);
            final String id = (String) itemLoadData.get("id");

            Item<?> item = null;
            if (Caches.WORKER_DEFS.has(id)) {
                item = Worker.fromLoadData(itemLoadData);
            } else if (Caches.PLANT_DEFS.has(id)) {
                item = Plant.fromLoadData(itemLoadData);
            } else if (Caches.FERTILIZER_DEFS.has(id)) {
                item = Fertilizer.fromLoadData(itemLoadData);
            } else if (Caches.PESTICIDE_DEFS.has(id)) {
                item = Pesticide.fromLoadData(itemLoadData);
            } else if (Caches.IRRIGATION_DEFS.has(id)) {
                item = Irrigation.fromLoadData(itemLoadData);
            } else if (Caches.TRACTOR_DEFS.has(id)) {
                item = Tractor.fromLoadData(itemLoadData);
            } else {
                System.out.println("Unable to find definition corresponding to id " + id);
            }

            if (item != null) {
                inventory.put(item, (Integer) inventoryEntry.get(1));
            }
        }
        capacity = (Integer) loadData.get("capacity");
        itemCount = (Integer) loadData.get("itemCount");
    }
}
