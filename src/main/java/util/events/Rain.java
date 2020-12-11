package main.java.util.events;

import java.util.concurrent.ThreadLocalRandom;
import main.java.ui.farm.FarmModel;
import main.java.entities.Player;

public class Rain extends FarmEvent {
    public Rain(long frequency, FarmModel model) {
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
        model.rain(amount);
        message = String.format("It has rained %d units of water", amount);
        super.run();
    }
}
