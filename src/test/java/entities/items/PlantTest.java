package test.java.entities.items;

import static junit.framework.TestCase.assertEquals;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.embed.swing.JFXPanel;
import javafx.scene.image.Image;
import main.java.entities.items.Plant;
import main.java.entities.items.definitions.PlantDef;
import main.java.util.definition.cache.Caches;

public class PlantTest {
    @SuppressWarnings("unused")
    private JFXPanel panel = new JFXPanel();

    private Plant plant;

    @BeforeAll
    public static void beforeAll() {
        Caches.PLANT_DEFS.intern(new PlantDef("root", "root", 5, 50, 1));
    }

    @BeforeEach
    public void beforeEach() {
        plant = new Plant(Caches.PLANT_DEFS.get("root"));
    }

    @Test
    public void testGrowFertilizer() {
        final Plant fertilizedPlant = new Plant(new PlantDef("toor", "admin", 40, 50, 1));
        final Plant nonFertilizedPlant = new Plant(new PlantDef("toor", "admin", 40, 50, 1));

        fertilizedPlant.grow(10, 2);
        nonFertilizedPlant.grow(10);
        assertTrue(
            fertilizedPlant.getCurrentGrowthTime() > nonFertilizedPlant.getCurrentGrowthTime()
        );
    }

    @Test
    public void testIsRipe() {
        final Plant plant = new Plant(new PlantDef("toor", "admin", 1, 50, 1));

        plant.grow();
        assertTrue(plant.isRipe());
    }

    @Test
    public void testIsRipeOverripe() {
        final Plant plant = new Plant(new PlantDef("toor", "admin", 1, 50, 1));

        plant.grow(10);
        assertTrue(plant.isRipe());
    }

    @Test
    public void testIsRipeUnripe() {
        final Plant plant = new Plant(Caches.PLANT_DEFS.get("root"));
        assertFalse(plant.isRipe());
    }

    @Test
    public void testKill() throws NoSuchFieldException, IllegalAccessException {
        plant.kill();

        final Field currentGrowth = plant.getClass().getDeclaredField("currentGrowth");
        currentGrowth.setAccessible(true);

        assertEquals(Plant.Growth.DEAD, currentGrowth.get(plant));
    }

    @Test
    public void testDeadCorrectImage() throws NoSuchFieldException, IllegalAccessException {
        final Image testImage = new Image("/test/java/entities/items/testDeadImage.png");
        final Plant deadPlant = new Plant(
            new PlantDef("img", "ooga", 9, new ArrayList<>(), testImage));

        final Field currentGrowth = deadPlant.getClass().getDeclaredField("currentGrowth");
        currentGrowth.setAccessible(true);
        currentGrowth.set(deadPlant, Plant.Growth.DEAD);

        assertEquals(testImage, deadPlant.getImage());
    }

    @Test
    public void testToString() {
        final String actual = plant.toString();

        assertNotNull(actual);
        assertTrue(actual.length() > 4);
    }
}
