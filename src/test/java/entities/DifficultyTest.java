package test.java.entities;

import static junit.framework.TestCase.assertEquals;

import org.junit.jupiter.api.Test;

import main.java.entities.Difficulty;

public class DifficultyTest {
    @Test
    public void testDifficultyEasy() {
        final Difficulty difficulty = new Difficulty("Easy", 75, 1, 1, 1);

        assertEquals("Easy", difficulty.getName());
        assertEquals(75, difficulty.getStartingMoney());
        assertEquals("Easy", difficulty.toString());
    }

    @Test
    public void testDifficultyMedium() {
        final Difficulty difficulty = new Difficulty("Normal", 50, 1, 1, 1);

        assertEquals("Normal", difficulty.getName());
        assertEquals(50, difficulty.getStartingMoney());
        assertEquals("Normal", difficulty.toString());
    }

    @Test
    public void testDifficultyToHard() {
        final Difficulty difficulty = new Difficulty("Hard", 25, 1, 1, 1);

        assertEquals("Hard", difficulty.getName());
        assertEquals(25, difficulty.getStartingMoney());
        assertEquals("Hard", difficulty.toString());
    }
}
