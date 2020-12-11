package test.java.ui.inventory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.HashMap;

import main.java.entities.items.Plant;
import main.java.entities.items.definitions.PlantDef;
import main.java.entities.items.Item;
import main.java.ui.inventory.Inventory;

public class InventoryTest {
    private Inventory inventory;
    private Item<?> injectedItem;
    private HashMap<Item<?>, Integer> injectedInventory;

    @BeforeEach
    public void beforeEach() throws IllegalAccessException, NoSuchFieldException {
        inventory = new Inventory(5);
        injectedItem = new Plant(new PlantDef("52", "largerbananas", 5, 50, 1));
        injectedInventory = new HashMap<>();

        final Field inventoryField = inventory.getClass().getDeclaredField("inventory");

        injectedInventory.put(injectedItem, 1);
        inventoryField.setAccessible(true);
        inventoryField.set(inventory, injectedInventory);

        final Field itemCount = inventory.getClass().getDeclaredField("itemCount");
        itemCount.setAccessible(true);
        itemCount.set(inventory, 1);
    }

    @Test
    public void testGetCount() {
        final int actual = inventory.getCount(injectedItem);

        assertEquals(1, actual);
    }

    @Test
    public void testGetCountDNE() {
        final Item<?> itemNotInInventory = new Plant(new PlantDef("53", "pumpkin", 100, 50, 1));
        final int actual = inventory.getCount(itemNotInInventory);

        assertEquals(0, actual);
    }

    @Test
    public void testDecrementCount() throws NoSuchFieldException, IllegalAccessException {
        inventory.decrementCount(injectedItem);

        final Integer actual = injectedInventory.get(injectedItem);

        assertNull(actual);
    }

    @Test
    public void testIncrementCount() {
        inventory.incrementCount(injectedItem);

        final Integer actual = injectedInventory.get(injectedItem);

        assertEquals(2, actual);
    }

    @Test
    public void testValidOperationOnCount() {
        assertEquals(true, inventory.operationOnCount(injectedItem, 1));
    }

    @Test
    public void testInvalidOperationOnCount() {
        final Item<?> itemNotInInventory = new Plant(new PlantDef("5s", "largerbananas", 5, 50, 1));
        assertEquals(false, inventory.operationOnCount(itemNotInInventory, -1));
    }

    @Test
    public void testExpose() {
        assertEquals(injectedInventory, inventory.expose());
    }

    @Test
    public void testGenerateCapacityString() {
        final String expected = "1 / 5";
        final String actual = inventory.generateCapacityString();

        assertEquals(expected, actual);
    }

    @Test
    public void testFullInventory() {
        assertEquals(false, inventory.operationOnCount(injectedItem, 5));
    }
}
