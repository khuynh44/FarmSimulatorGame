package test.java.ui.farm;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.embed.swing.JFXPanel;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.util.ArrayList;

import main.java.entities.items.Plant;
import main.java.entities.items.definitions.PlantDef;
import main.java.ui.farm.Plot;
import main.java.util.definition.cache.Caches;

public class PlotTest {
    @SuppressWarnings("unused")
    private JFXPanel panel = new JFXPanel();

    @BeforeAll
    public static void beforeAll() {
        Caches.PLANT_DEFS.intern(new PlantDef("potato", "potato", 9, 50, 1));
        Caches.PLANT_DEFS.intern(new PlantDef("corn", "corn", 9, 50, 1));
        Caches.PLANT_DEFS.intern(new PlantDef("wheat", "wheat", 9, 50, 1));
    }

    @Test
    public void testUpdateImage() throws NoSuchFieldException, IllegalAccessException {
        final ArrayList<Image> arts = new ArrayList<>();
        final Image testImage = new Image("/test/java/ui/farm/testImage.png");
        arts.add(testImage);
        Caches.PLANT_DEFS.intern(new PlantDef("img", "ooga", 9, arts, null));

        final Plot plot = new Plot(new Plant(Caches.PLANT_DEFS.get("img")), 50);

        plot.updateImage();

        final Field plantDisplay = plot.getClass().getDeclaredField("plantDisplay");
        plantDisplay.setAccessible(true);

        assertEquals(testImage,
            ((ImagePattern) ((Rectangle) plantDisplay.get(plot)).getFill()).getImage());
    }

    @Test
    public void testUpdateImageNoPlants() throws NoSuchFieldException, IllegalAccessException {
        final Plot plot = new Plot(50);

        plot.updateImage();

        final Field plantDisplay = plot.getClass().getDeclaredField("plantDisplay");
        plantDisplay.setAccessible(true);

        assertNotNull(((Rectangle) plantDisplay.get(plot)).getFill());
    }

    @Test
    public void testUpdateImageNullArt() throws NoSuchFieldException, IllegalAccessException {
        final Plot plot = new Plot(new Plant(new PlantDef("fruit", "f", 2, 50, 1)), 50);

        plot.updateImage();

        final Field plantDisplay = plot.getClass().getDeclaredField("plantDisplay");
        plantDisplay.setAccessible(true);

        assertNotNull(((Rectangle) plantDisplay.get(plot)).getFill());
    }

    @Test
    public void testGrowOnce() throws NoSuchFieldException, IllegalAccessException {
        final Plant plant = new Plant(Caches.PLANT_DEFS.get("potato"));
        final Plot plot = new Plot(plant, 50);
        plot.grow();

        final Field growTime = plant.getClass().getDeclaredField("currentGrowthTime");
        growTime.setAccessible(true);

        assertTrue((Integer) growTime.get(plant) > 1);
    }

    @Test
    public void testGrowTwice() throws NoSuchFieldException, IllegalAccessException {
        final Plant plant = new Plant(Caches.PLANT_DEFS.get("potato"));
        final Plot plot = new Plot(plant, 50);
        plot.grow();
        plot.grow();

        final Field growTime = plant.getClass().getDeclaredField("currentGrowthTime");
        growTime.setAccessible(true);

        assertTrue((Integer) growTime.get(plant) > 2);

    }

    @Test
    public void testWater() {
        final Plant plant = new Plant(Caches.PLANT_DEFS.get("potato"));
        final Plot plot = new Plot(plant, 50);
        plot.water();

        assertTrue(plot.getWaterAmount() > 50);
    }

    @Test
    public void testWaterBelowZero() throws NoSuchFieldException, IllegalAccessException {
        final Plant plant = new Plant(Caches.PLANT_DEFS.get("potato"));
        final Plot plot = new Plot(plant, 0);
        plot.grow();
        plot.grow();

        final Field waterAmount = plot.getClass().getDeclaredField("waterAmount");
        waterAmount.setAccessible(true);

        assertEquals(0, ((SimpleIntegerProperty) waterAmount.get(plot)).get());
    }
}
