package main.java.util.events;

public class Event {
    private final EventAction action;
    private long frequency;

    public Event(EventAction action, long frequency) {
        this.action = action;
        this.frequency = frequency;
    }

    public long getFrequency() {
        return frequency;
    }

    public void run() {
        action.run();
    }

    @FunctionalInterface
    public interface EventAction {
        public void run();
    }
}
