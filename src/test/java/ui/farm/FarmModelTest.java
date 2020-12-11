package test.java.ui.farm;

import main.java.ui.farm.FarmModel;
import main.java.ui.farm.FieldGrid;
import main.java.entities.Player;
import main.java.entities.items.Plant;
import main.java.entities.items.definitions.PlantDef;
import main.java.ui.worker_panel.WorkerPanelModel;
import main.java.util.definition.cache.Caches;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;

public class FarmModelTest {
    private FieldGrid fieldGrid;
    private FarmModel farmModel;

    @BeforeAll
    public static void beforeAll() {
        Caches.PLANT_DEFS.intern(new PlantDef("potato", "potato", 100, 50, 1));
        Caches.PLANT_DEFS.intern(new PlantDef("corn", "corn", 100, 50, 1));
        Caches.PLANT_DEFS.intern(new PlantDef("wheat", "wheat", 100, 50, 1));
    }

    @BeforeEach
    public void beforeEach() {
        farmModel = new FarmModel();
        fieldGrid = new FieldGrid();

        fieldGrid.initialize(Player.getInstance().getInventoryModel(), new WorkerPanelModel());
        farmModel.setFieldGrid(fieldGrid);
    }

    @Test
    public void testCheckGameLostFalseNonEmptyWallet() {
        Player.getInstance().getWallet().operationOnMoney(100);
        Player.getInstance().getInventoryModel().clear();

        assertFalse(farmModel.checkGameLost());
    }

    @Test
    public void testCheckGameLostFalseNonEmptyInventory() {
        Player.getInstance().getInventoryModel().incrementCount(
            new Plant(Caches.PLANT_DEFS.get("potato")));
        Player.getInstance().getWallet().zeroBalance();

        assertFalse(farmModel.checkGameLost());
    }

    @Test
    public void testCheckGameLostTrue() {
        Player.getInstance().getInventoryModel().clear();
        Player.getInstance().getWallet().zeroBalance();

        assertTrue(farmModel.checkGameLost());
    }
}
