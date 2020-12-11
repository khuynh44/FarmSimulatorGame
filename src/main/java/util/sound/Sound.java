package main.java.util.sound;

import javafx.scene.media.Media;
import main.java.util.definition.cache.ICacheable;

public class Sound implements ICacheable {
    private final String id;
    private final String path;
    private Media sound;

    private Sound() {
        this.id = "default_id";
        this.path = "";
    }

    public void loadSound() {
        sound = new Media(getClass().getResource(path).toString());
    }

    public Media getSound() {
        return sound;
    }

    @Override
    public String getId() {
        return id;
    }
}
