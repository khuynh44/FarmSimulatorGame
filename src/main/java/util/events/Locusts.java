package main.java.util.events;

import java.util.concurrent.ThreadLocalRandom;
import main.java.ui.farm.FarmModel;
import main.java.entities.Player;

public class Locusts extends FarmEvent {
    public Locusts(long frequency, FarmModel model) {
        super(frequency, model);
    }

    @Override
    public void run() {
        final int upperBound =
            (int) (6 * Player.getInstance().getDifficulty().getEnvironmentalModifier());
        if (ThreadLocalRandom.current().nextInt(upperBound) != 0) {
            return;
        }

        final int killCount = model.locustPlague();
        message = String.format("Locusts killed %d plant%s", killCount, killCount != 1 ? "s" : "");
        super.run();
    }
}
