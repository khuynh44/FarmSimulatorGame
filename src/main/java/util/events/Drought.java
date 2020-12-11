package main.java.util.events;

import java.util.concurrent.ThreadLocalRandom;
import main.java.ui.farm.FarmModel;
import main.java.entities.Player;

public class Drought extends FarmEvent {
    public Drought(long frequency, FarmModel model) {
        super(frequency, model);
    }

    @Override
    public void run() {
        final int upperBound =
            (int) (5 * Player.getInstance().getDifficulty().getEnvironmentalModifier());
        if (ThreadLocalRandom.current().nextInt(upperBound) != 0) {
            return;
        }

        final int amount = ThreadLocalRandom.current().nextInt(21) + 20;
        model.unWater(amount);
        message = String.format("All plants have lost %d units of water due to drought", amount);
        super.run();
    }
}
