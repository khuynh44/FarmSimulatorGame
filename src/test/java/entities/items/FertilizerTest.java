package test.java.entities.items;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import main.java.entities.items.Fertilizer;
import main.java.entities.items.definitions.FertilizerDef;
import main.java.util.definition.cache.Caches;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public class FertilizerTest {
    private Fertilizer fertilizer;

    @BeforeAll
    public static void beforeAll() {
        Caches.FERTILIZER_DEFS.intern(new FertilizerDef("fertilizer", 3));
    }

    @BeforeEach
    public void beforeEach() {
        fertilizer = new Fertilizer(Caches.FERTILIZER_DEFS.get("fertilizer"));
    }

    public void testToString() {
        final String actual = fertilizer.toString();
        assertNotNull(actual);
    }
}
