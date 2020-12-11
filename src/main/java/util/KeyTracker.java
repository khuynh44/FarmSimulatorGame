package main.java.util;

import java.util.HashSet;
import java.util.Set;

import javafx.scene.input.KeyCode;

public class KeyTracker {
    private static final Set<KeyCode> KEYS_PRESSED = new HashSet<>();

    public static void setkey(KeyCode code) {
        KEYS_PRESSED.add(code);
    }

    public static void unsetKey(KeyCode code) {
        KEYS_PRESSED.remove(code);
    }

    public static boolean isKeyPressed(KeyCode code) {
        return KEYS_PRESSED.contains(code);
    }

    public static boolean isInterestingKeyPressed() {
        return isKeyPressed(KeyCode.CONTROL)
               || isKeyPressed(KeyCode.SHIFT)
               || isKeyPressed(KeyCode.Z);
    }

    private KeyTracker() {
    }
}
