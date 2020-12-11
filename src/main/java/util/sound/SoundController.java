package main.java.util.sound;

import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import main.java.util.definition.cache.Caches;

public class SoundController {
    private static MediaPlayer backgroundPlayer;

    public static void play(String id) {
        final Sound sound = Caches.SOUNDS.get(id);
        if (sound == null) {
            return;
        }

        final MediaPlayer mediaPlayer = new MediaPlayer(sound.getSound());
        mediaPlayer.play();
    }

    public static void setBackgroundMusic(String id) {
        final Sound sound = Caches.SOUNDS.get(id);
        if (sound == null) {
            return;
        }

        backgroundPlayer = new MediaPlayer(sound.getSound());
        backgroundPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                backgroundPlayer.seek(Duration.ZERO);
                backgroundPlayer.play();
            }
        });

        backgroundPlayer.play();
    }

    public static void stopBackgroundMusic() {
        backgroundPlayer.pause();
    }

    public static void startBackgroundMusic() {
        backgroundPlayer.play();
    }

    public static boolean toggleMuteBackgroundMusic() {
        final boolean newState = !backgroundPlayer.isMute();
        backgroundPlayer.setMute(newState);

        return newState;
    }

    public static void setMuteBackgroundMusic(boolean state) {
        backgroundPlayer.setMute(state);
    }
}
