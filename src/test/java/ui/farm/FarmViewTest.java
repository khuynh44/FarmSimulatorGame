package test.java.ui.farm;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.java.entities.items.definitions.PlantDef;
import main.java.ui.farm.FarmController;
import main.java.ui.farm.FarmModel;
import main.java.ui.farm.FieldGrid;
import main.java.util.definition.cache.Caches;

@ExtendWith(ApplicationExtension.class)
public class FarmViewTest {
    @BeforeAll
    public static void beforeAll() {
        Caches.PLANT_DEFS.intern(new PlantDef("banana", "banana", 10, 50, 1));
    }

    @Start
    public void start(final Stage stage) throws IOException {
        final FXMLLoader loader =
                new FXMLLoader(FarmController.class.getResource("farm.fxml"));
        final Parent mainNode = loader.load();

        final FarmController controller = loader.getController();
        controller.initialize(new FarmModel());

        stage.setScene(new Scene(mainNode));
        stage.show();
        stage.toFront();
    }

    @Test
    public void testMoneyText(final FxRobot robot) {
        final Text moneyText = robot.lookup("#moneyLabel").query();

        assertNotNull(moneyText);
        assertNotNull(moneyText.getText());
    }

    @Test
    public void testNameText(final FxRobot robot) {
        final Text nameText = robot.lookup("#nameText").query();

        assertNotNull(nameText);
        assertNotNull(nameText.getText());
    }

    @Test
    public void testFieldGrid(final FxRobot robot) {
        final FieldGrid fieldGrid = robot.lookup("#fieldGrid").query();

        assertNotNull(fieldGrid);
    }
}
