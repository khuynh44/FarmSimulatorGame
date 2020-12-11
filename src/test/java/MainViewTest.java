package test.java;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import javafx.stage.Stage;

import main.java.Main;
import main.java.util.Constants;
import main.java.util.View;

@ExtendWith(ApplicationExtension.class)
public class MainViewTest {
    private Stage stage;

    @Start
    public void start(final Stage stage) {
        View.MAIN.unloadView();

        Main main = new Main();
        main.start(stage);

        this.stage = stage;
    }

    @Test
    public void testCore() {
        // this must remain as one one test due to the way testfx sets up its tests
        assertEquals(Constants.TITLE, stage.getTitle());
        assertEquals(true, stage.isShowing());
    }
}
