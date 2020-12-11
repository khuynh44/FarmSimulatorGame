package main.java.ui.market;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.property.ObjectProperty;
import main.java.entities.Wallet;
import main.java.entities.items.Fertilizer;
import main.java.entities.items.Irrigation;
import main.java.entities.items.Item;
import main.java.entities.items.Pesticide;
import main.java.entities.items.Plant;
import main.java.entities.items.Tractor;
import main.java.entities.items.workers.Harvester;
import main.java.entities.items.workers.Waterer;
import main.java.ui.inventory.InventoryModel;
import main.java.util.definition.cache.Caches;
import main.java.util.definition.cache.ICacheable;
import main.java.util.persistence.IPersistable;

public class Market implements ICacheable, IPersistable {
    private final String id;

    private final Map<String, List<Integer>> initialItems;
    private final int initialInventoryCap;
    private InventoryModel inventoryModel;
    private final double tax;
    private final String name;
    private final Wallet wallet = new Wallet();

    @SuppressWarnings("unused")
    private Market() {
        this("defaults_market", "Default's Market", 0.06, 50);
    }

    public Market(String id, String name, double tax, int initialInventoryCap) {
        this.name = name;
        this.tax = tax;
        this.id = id;
        this.initialItems = null;
        this.initialInventoryCap = initialInventoryCap;
        this.inventoryModel = new InventoryModel(this.initialInventoryCap);
    }

    public void onLoad() {
        inventoryModel = new InventoryModel(initialInventoryCap);
        resetInventory();
    }

    private void handleLoadWorkers(String id) {
        if (id.equals("harvester")) {
            inventoryModel.incrementCount(new Harvester());
        } else if (id.equals("waterer")) {
            inventoryModel.incrementCount(new Waterer());
        }
    }

    private void handleLoadPlant(String id, int amount) {
        inventoryModel.operationOnCount(new Plant(Caches.PLANT_DEFS.get(id)), amount);
    }

    private void handleLoadPesticide(String id, int amount) {
        inventoryModel.operationOnCount(new Pesticide(Caches.PESTICIDE_DEFS.get(id)), amount);
    }

    private void handleLoadIrrigation(String id, int amount) {
        inventoryModel.operationOnCount(new Irrigation(Caches.IRRIGATION_DEFS.get(id)), amount);
    }

    private void handleLoadFertilizer(String id, int amount) {
        inventoryModel.operationOnCount(new Fertilizer(Caches.FERTILIZER_DEFS.get(id)), amount);
    }

    private void handleLoadTractor(String id, int amount) {
        inventoryModel.operationOnCount(new Tractor(Caches.TRACTOR_DEFS.get(id)), amount);
    }

    public void resetInventory() {
        inventoryModel.clear();
        if (initialItems != null) {
            initialItems.forEach((id, amounts) -> {
                amounts.forEach((amount) -> {
                    if (Caches.WORKER_DEFS.has(id)) {
                        handleLoadWorkers(id);
                    } else if (Caches.PLANT_DEFS.has(id)) {
                        handleLoadPlant(id, amount);
                    } else if (Caches.PESTICIDE_DEFS.has(id)) {
                        handleLoadPesticide(id, amount);
                    } else if (Caches.FERTILIZER_DEFS.has(id)) {
                        handleLoadFertilizer(id, amount);
                    } else if (Caches.IRRIGATION_DEFS.has(id)) {
                        handleLoadIrrigation(id, amount);
                    } else if (Caches.TRACTOR_DEFS.has(id)) {
                        handleLoadTractor(id, amount);
                    } else {
                        System.out.println("Unable to find definition corresponding to id " + id);
                    }
                });
            });
        }
    }

    @Override
    public String getId() {
        return id;
    }

    public InventoryModel getInventoryModel() {
        return inventoryModel;
    }

    public boolean sellItem(Wallet payee, Item<?> item, InventoryModel source) {
        if (item == null) {
            return false;
        }

        if (!inventoryModel.incrementCount(item)) {
            return false;
        }

        if (!source.decrementCount(item)) {
            inventoryModel.incrementCount(item);
            return false;
        }

        payee.operationOnMoney(calculateCost(item));
        return true;
    }

    public boolean buySelectedItem(Wallet payor, InventoryModel recipientInventoryModel) {
        final Item<?> selectedItem = getSelectedItem();
        if (selectedItem == null) {
            return false;
        }

        if (!payor.testOperationOnMoney(-1 * calculateCost(selectedItem))) {
            return false;
        }

        if (!recipientInventoryModel.incrementCount(selectedItem)) {
            return false;
        }

        if (!inventoryModel.decrementCount(selectedItem)) {
            return false;
        }

        payor.sendMoneyTo(calculateCost(selectedItem), wallet);
        return true;
    }

    public boolean operationOnCount(Item<?> item, int amount) {
        return inventoryModel.operationOnCount(item, amount);
    }

    public double calculateCost(Item<?> item) {
        if (item == null) {
            return 0;
        }

        return (1 + tax) * item.getWorth();
    }

    public Item<?> getSelectedItem() {
        return inventoryModel.getSelectedItem();
    }

    public ObjectProperty<Item<?>> selectedItem() {
        return inventoryModel.selectedItem();
    }

    public String getName() {
        return name;
    }

    @Override
    public Map<String, Object> generateSaveData() {
        final Map<String, Object> saveData = new HashMap<>();
        saveData.put("id", id);

        return saveData;
    }
}
