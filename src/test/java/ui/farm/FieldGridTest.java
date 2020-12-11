package test.java.ui.farm;

import main.java.entities.Player;
import main.java.entities.items.definitions.PlantDef;
import main.java.ui.farm.FieldGrid;
import main.java.ui.inventory.InventoryModel;
import main.java.ui.worker_panel.WorkerPanelModel;
import main.java.util.definition.cache.Caches;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FieldGridTest {
    private FieldGrid field;

    @BeforeAll
    public static void beforeAll() {
        Caches.PLANT_DEFS.intern(new PlantDef("potato", "potato", 9, 50, 1));
        Caches.PLANT_DEFS.intern(new PlantDef("corn", "corn", 9, 50, 1));
        Caches.PLANT_DEFS.intern(new PlantDef("wheat", "wheat", 9, 50, 1));
    }

    @BeforeEach
    public void beforeEach() {
        field = new FieldGrid();
        Player.getInstance().getWallet().operationOnMoney(100);
        field.initialize(new InventoryModel(), new WorkerPanelModel());
    }

    @Test
    public void testAddRow() {
        final int expected = field.ourGetRowCount() + 1;

        field.addRow();
        assertEquals(expected, field.ourGetRowCount());
    }

    @Test
    public void testAddRowLessMoney() {
        final double initialPlayerBalance = Player.getInstance().getWallet().getBalance();
        final double initialFieldCost = field.upgradeCost().get();

        field.addRow();
        assertTrue(field.upgradeCost().get() > initialFieldCost);
        assertTrue(Player.getInstance().getWallet().getBalance() < initialPlayerBalance);
    }
}
