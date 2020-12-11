package main.java.util.events;

import main.java.ui.farm.FarmModel;
import main.java.util.Scheduler;

public abstract class FarmEvent extends Event {
    protected final FarmModel model;
    protected String message;

    protected FarmEvent(long frequency, FarmModel model) {
        super(null, frequency);
        this.model = model;
        Scheduler.register(this);
        Scheduler.disableEvent(this);
    }

    @Override
    public void run() {
        model.postMessage(message);
    }
}
