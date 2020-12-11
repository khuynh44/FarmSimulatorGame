package test.java.ui.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import main.java.ui.config.ConfigController;
import main.java.ui.config.ConfigModel;
import main.java.entities.Difficulty;
import main.java.entities.items.Plant;

@ExtendWith(ApplicationExtension.class)
public class ConfigControllerTest {
    private Stage stage;
    private ConfigController controller;
    private ConfigModel model;

    @Start
    public void start(final Stage stage) throws IOException {
        final FXMLLoader loader =
            new FXMLLoader(ConfigController.class.getResource("config.fxml"));
        final Parent mainNode = loader.load();
        controller = loader.getController();

        model = new ConfigModel();
        controller.initialize(model);

        stage.setScene(new Scene(mainNode));
        stage.show();
        stage.toFront();
        this.stage = stage;
    }

    @Test
    public void testSwtichToFarmScreenNullDifficulty(final FxRobot robot)
            throws NoSuchFieldException, IllegalAccessException {
        final ObjectProperty<Difficulty> injectedDifficulty = new SimpleObjectProperty<>();

        injectedDifficulty.set(null);

        final Field difficultyField = model.getClass().getDeclaredField("difficulty");
        difficultyField.setAccessible(true);
        difficultyField.set(model, injectedDifficulty);

        robot.interact(() -> {
            controller.switchToFarmScreen(null);
        });

        final Parent actual = stage.getScene().getRoot();
        assertEquals("config", actual.getId());
    }

    @Test
    public void testSwtichToFarmScreenNullName(final FxRobot robot)
            throws NoSuchFieldException, IllegalAccessException {
        final StringProperty injectedName = new SimpleStringProperty();

        injectedName.set(null);

        final Field name = model.getClass().getDeclaredField("name");
        name.setAccessible(true);
        name.set(model, injectedName);

        robot.interact(() -> {
            controller.switchToFarmScreen(null);
        });

        final Parent actual = stage.getScene().getRoot();
        assertEquals("config", actual.getId());
    }

    @Test
    public void testSwtichToFarmScreenSpaceName(final FxRobot robot)
            throws NoSuchFieldException, IllegalAccessException {
        final StringProperty injectedName = new SimpleStringProperty();

        injectedName.set(" ");

        final Field name = model.getClass().getDeclaredField("name");
        name.setAccessible(true);
        name.set(model, injectedName);

        robot.interact(() -> {
            controller.switchToFarmScreen(null);
        });

        final Parent actual = stage.getScene().getRoot();
        assertEquals("config", actual.getId());
    }

    @Test
    public void testSwtichToFarmScreenNullSeed(final FxRobot robot)
            throws NoSuchFieldException, IllegalAccessException {
        final ObjectProperty<Plant> injectedSeed = new SimpleObjectProperty<>();

        injectedSeed.set(null);

        final Field seedField = model.getClass().getDeclaredField("seed");
        seedField.setAccessible(true);
        seedField.set(model, injectedSeed);

        robot.interact(() -> {
            controller.switchToFarmScreen(null);
        });

        final Parent actual = stage.getScene().getRoot();
        assertEquals("config", actual.getId());
    }
}
