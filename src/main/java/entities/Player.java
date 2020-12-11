package main.java.entities;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import main.java.ui.inventory.InventoryModel;
import main.java.util.persistence.IPersistable;
import main.java.util.persistence.ISaveable;
import main.java.util.persistence.Saver;

public final class Player implements ISaveable, IPersistable {
    private static Player player = new Player();

    private StringProperty name = new SimpleStringProperty();
    private Difficulty difficulty;
    private InventoryModel inventoryModel;
    private Wallet wallet = new Wallet();

    private Player(String name,
                    double money, Difficulty difficulty, InventoryModel inventoryModel) {
        this.name.set(name);
        this.difficulty = difficulty;
        this.inventoryModel = inventoryModel;
    }

    private Player() {
        this("George Burdell", 77777.77, null, new InventoryModel());
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty name() {
        return name;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setInventoryModel(InventoryModel inventoryModel) {
        this.inventoryModel = inventoryModel;
    }

    public InventoryModel getInventoryModel() {
        return inventoryModel;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public static Player getInstance() {
        return player;
    }

    @Override
    public String toString() {
        return name.get();
    }

    @Override
    public Map<String, Object> generateSaveData() {
        final Map<String, Object> saveData = new HashMap<>();
        saveData.put("name", name.get());
        saveData.put("wallet", wallet);
        saveData.put("difficulty", difficulty);

        return saveData;
    }

    @Override
    public void save(String prefix) throws IOException {
        Saver.save(this, prefix + "_player.yaml");
        inventoryModel.save(prefix);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void load(String prefix) throws IOException {
        final Map<String, Object> playerLoadData = Saver.load(prefix + "_player.yaml");
        final String name = (String) playerLoadData.get("name");
        player.setName(name);

        final Map<String, Object> difficultyLoadData =
            (Map<String, Object>) playerLoadData.get("difficulty");
        difficulty = Difficulty.fromLoadData(difficultyLoadData);

        wallet.processLoadData((Map<String, Object>) playerLoadData.get("wallet"));
        inventoryModel.load(prefix);
    }
}
