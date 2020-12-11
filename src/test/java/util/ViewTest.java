package test.java.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import main.java.util.View;

@ExtendWith(ApplicationExtension.class)
public class ViewTest {
    @Test
    public void testView() {
        assertNotNull(View.MAIN.getController());
    }
}
