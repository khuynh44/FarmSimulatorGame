package test.java.ui.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.testfx.assertions.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import main.java.ui.config.ConfigController;
import main.java.ui.config.ConfigModel;

@ExtendWith(ApplicationExtension.class)
public class ConfigViewTest {
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
    }

    @Test
    public void testSeedBox(final FxRobot robot) {
        final ComboBox<String> actualSeedBox = robot.lookup("#seedBox").queryComboBox();

        assertNotNull(actualSeedBox);
        assertThat(actualSeedBox).isVisible();
        assertNotNull(actualSeedBox.getItems());
        assertNotEquals(0, actualSeedBox.getItems().size());
    }

    @Test
    public void testSeasonBox(final FxRobot robot) {
        final ComboBox<String> actualSeasonBox = robot.lookup("#seasonBox").queryComboBox();

        assertNotNull(actualSeasonBox);
        assertThat(actualSeasonBox).isVisible();
        assertThat(actualSeasonBox.getItems());
        assertNotEquals(0, actualSeasonBox.getItems().size());
    }

    @Test
    public void testDifficultyBox(final FxRobot robot) {
        final ComboBox<String> actualDifficultyBox = robot.lookup("#difficultyBox").queryComboBox();

        assertNotNull(actualDifficultyBox);
        assertThat(actualDifficultyBox).isVisible();
        assertThat(actualDifficultyBox.getItems());
        assertNotEquals(0, actualDifficultyBox.getItems().size());
    }

    @Test
    public void testNameField(final FxRobot robot) {
        final TextField actualTextField = robot.lookup("#nameField").query();

        assertNotNull(actualTextField);
        assertThat(actualTextField).isVisible();
        assertThat(actualTextField.getText()).isBlank();
    }

    @Test
    public void testDifficultyExceptionInitialState(final FxRobot robot) {
        final Text actualDifficultyExceptionText = robot.lookup("#difficultyException").query();

        assertNotNull(actualDifficultyExceptionText);
        assertThat(actualDifficultyExceptionText).isInvisible();
        assertThat(actualDifficultyExceptionText.getText()).isNotBlank();
    }

    @Test
    public void testEmptyDifficultyException(final FxRobot robot) {
        final Text actualDifficultyExceptionText = robot.lookup("#difficultyException").query();

        robot.clickOn("#nextButton");

        assertNotNull(actualDifficultyExceptionText);
        assertThat(actualDifficultyExceptionText).isVisible();
        assertThat(actualDifficultyExceptionText.getText()).isNotBlank();
    }

    @Test
    public void testNameExceptionInitialState(final FxRobot robot) {
        final Text actualNameExceptionText = robot.lookup("#nameException").query();

        assertNotNull(actualNameExceptionText);
        assertThat(actualNameExceptionText).isInvisible();
        assertThat(actualNameExceptionText.getText()).isNotBlank();
    }

    @Test
    public void testEmptyNameException(final FxRobot robot) {
        final Text actualNameExceptionText = robot.lookup("#nameException").query();

        robot.clickOn("#nextButton");

        assertNotNull(actualNameExceptionText);
        assertThat(actualNameExceptionText).isVisible();
        assertThat(actualNameExceptionText.getText()).isNotBlank();
    }

    @Test
    public void testSpaceNameException(final FxRobot robot) {
        final Text actualNameExceptionText = robot.lookup("#nameException").query();

        robot.clickOn("#nameField");
        robot.type(KeyCode.SPACE);

        robot.clickOn("#nextButton");

        assertNotNull(actualNameExceptionText);
        assertThat(actualNameExceptionText).isVisible();
        assertThat(actualNameExceptionText.getText()).isNotBlank();
    }
}
