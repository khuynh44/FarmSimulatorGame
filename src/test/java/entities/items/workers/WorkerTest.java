package test.java.entities.items.workers;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.java.entities.Difficulty;
import main.java.entities.Player;
import main.java.entities.items.definitions.WorkerDef;
import main.java.entities.items.workers.Waterer;
import main.java.entities.items.workers.Worker;
import main.java.util.definition.cache.Caches;

public class WorkerTest {
    private Worker waterer;

    @BeforeAll
    public static void beforeAll() {
        Player.getInstance().setDifficulty(new Difficulty("test", 100, 1.00, 1.00, 1.00));
        Caches.WORKER_DEFS.intern(new WorkerDef("waterer", 0));
    }

    @BeforeEach
    public void beforeEach() {
        waterer = new Waterer();
    }

    @Test
    public void testEmptyNameCache() {
        assertDoesNotThrow(() -> {
            @SuppressWarnings("unused")
            final Worker worker = new Waterer();
        });
    }

    @Test
    public void testNonZeroBaseWorkTime() {
        assertTrue(waterer.getWorkTime() > 0);
    }

    @Test
    public void testDescriptionsGenerating() {
        final List<String> workerEntries = waterer.generateDescriptionEntries();

        assertTrue(workerEntries.size() > 2);
    }
}
