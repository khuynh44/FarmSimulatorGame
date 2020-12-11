package main.java.entities.items.definitions;

import java.util.ArrayList;

import javafx.scene.image.Image;
import main.java.util.definition.cache.ICacheable;

public abstract class ItemDef implements ICacheable {
    protected String id;
    protected String name;
    protected double baseCost;

    protected final ArrayList<String> artPaths = new ArrayList<>();
    protected final ArrayList<Image> arts = new ArrayList<>();

    protected ItemDef(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Image getImage(int index) {
        if (index >= arts.size() || index < 0) {
            return null;
        }

        return arts.get(index);
    }

    public int getArtsCount() {
        return arts.size();
    }


    public void loadImages() {
        for (String path : artPaths) {
            System.out.println("     - " + path);
            arts.add(new Image(path));
        }
    }

    @Override
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getBaseCost() {
        return baseCost * getBaseCostModifier();
    }

    public double getBaseCostModifier() {
        return 1.0;
    }
}
