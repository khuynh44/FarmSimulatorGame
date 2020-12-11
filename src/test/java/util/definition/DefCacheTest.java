package test.java.util.definition;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Set;

import org.junit.jupiter.api.Test;

import main.java.entities.items.definitions.PlantDef;
import main.java.util.definition.cache.DefCache;

public class DefCacheTest {
    private final DefCache<PlantDef> plantDefCache = new DefCache<>();

    @Test
    public void testIntern() {
        final PlantDef plantDef = new PlantDef("strawberry", "Strawberry Plant", 24, 50, 1);

        assertEquals(plantDef, plantDefCache.intern(plantDef));
    }

    @Test
    public void testInternLookup() {
        final PlantDef plantDef = new PlantDef("strawberry", "Strawberry Plant", 24, 50, 1);

        // should compute and then return on the second call
        assertEquals(plantDef, plantDefCache.intern(plantDef));
        assertEquals(plantDef, plantDefCache.intern(plantDef));
    }

    @Test
    public void testGet() throws NoSuchFieldException, IllegalAccessException {
        final PlantDef plantDef = new PlantDef("strawberry", "Strawberry Plant", 24, 50, 1);

        final HashMap<String, PlantDef> injectedCache = new HashMap<>();
        injectedCache.put("strawberry", plantDef);

        final Field cacheField = plantDefCache.getClass().getDeclaredField("cache");
        cacheField.setAccessible(true);
        cacheField.set(plantDefCache, injectedCache);

        final PlantDef actual = plantDefCache.get("strawberry");

        assertEquals(actual, plantDef);
    }

    @Test
    public void testGetIds() throws NoSuchFieldException, IllegalAccessException {
        final PlantDef plantDef = new PlantDef("strawberry", "Strawberry Plant", 24, 50, 1);

        final HashMap<String, PlantDef> injectedCache = new HashMap<>();
        injectedCache.put("strawberry", plantDef);

        final Field cacheField = plantDefCache.getClass().getDeclaredField("cache");
        cacheField.setAccessible(true);
        cacheField.set(plantDefCache, injectedCache);

        final Set<String> actual = plantDefCache.getIds();

        assertEquals(actual, injectedCache.keySet());
    }
}
