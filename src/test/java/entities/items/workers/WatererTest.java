package test.java.entities.items.workers;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.beans.property.SimpleIntegerProperty;
import main.java.entities.items.Plant;
import main.java.entities.items.definitions.PlantDef;
import main.java.entities.items.definitions.WorkerDef;
import main.java.entities.items.workers.Waterer;
import main.java.entities.items.workers.Worker;
import main.java.ui.farm.Plot;
import main.java.util.definition.cache.Caches;

public class WatererTest {
    private Worker waterer;

    @BeforeAll
    public static void beforeAll() {
        Caches.PLANT_DEFS.intern(new PlantDef("planty", "mc plant", 23, 50, 1));
        Caches.WORKER_DEFS.intern(new WorkerDef("waterer", 1));
    }

    @BeforeEach
    public void beforeEach() {
        waterer = new Waterer();
    }

    @Test
    public void testWaterPlot() throws IllegalAccessException, NoSuchFieldException {
        final Plant plant = new Plant(Caches.PLANT_DEFS.get("planty"));
        final Plot plot = new Plot(plant, 50);

        waterer.work(plot);

        final Field waterAmount = plot.getClass().getDeclaredField("waterAmount");
        waterAmount.setAccessible(true);

        assertTrue(((SimpleIntegerProperty) waterAmount.get(plot)).get() > 50);
    }
}
