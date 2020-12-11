package main.java.ui.config;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import main.java.entities.Difficulty;
import main.java.entities.items.Plant;
import main.java.ui.Model;
import main.java.util.definition.cache.Caches;

public class ConfigModel extends Model {
    private int money;
    private StringProperty name = new SimpleStringProperty();

    private final ObjectProperty<ObservableList<Difficulty>> difficultyList =
        new SimpleObjectProperty<>();
    private final ObjectProperty<Difficulty> difficulty = new SimpleObjectProperty<>();

    private final ObjectProperty<ObservableList<Plant>> seedList = new SimpleObjectProperty<>();
    private final ObjectProperty<Plant> seed = new SimpleObjectProperty<>();

    private final ObjectProperty<ObservableList<String>> seasonList = new SimpleObjectProperty<>();

    public ConfigModel() {
        difficultyList.set(FXCollections.observableArrayList());
        difficultyList.get().addAll(
            Caches.DIFFICULTIES.get("easy"),
            Caches.DIFFICULTIES.get("normal"),
            Caches.DIFFICULTIES.get("hard")
        );

        seedList.set(FXCollections.observableArrayList());
        seedList.get().addAll(
            new Plant("wheat"),
            new Plant("corn"),
            new Plant("sugar_cane"),
            new Plant("potato")
        );

        seasonList.set(FXCollections.observableArrayList());
        seasonList.get().addAll("Spring", "Summer", "Fall", "Winter");

        name.set("");
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public StringProperty name() {
        return name;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public ObjectProperty<ObservableList<Difficulty>> difficultyList() {
        return difficultyList;
    }

    public ObjectProperty<Difficulty> difficulty() {
        return difficulty;
    }

    public Difficulty getDifficulty() {
        return difficulty.get();
    }

    public ObjectProperty<ObservableList<Plant>> seedList() {
        return seedList;
    }

    public ObjectProperty<Plant> seed() {
        return seed;
    }

    public Plant getSeed() {
        return seed.get();
    }

    public ObjectProperty<ObservableList<String>> seasonList() {
        return seasonList;
    }
}
