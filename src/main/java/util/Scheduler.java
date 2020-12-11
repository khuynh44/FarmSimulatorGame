package main.java.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javafx.animation.PauseTransition;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.util.Duration;
import main.java.util.events.Event;

public class Scheduler {
    private static final long EVENT_DISABLED_TIME = -1;
    private static final PauseTransition CORE = new PauseTransition(Duration.millis(500));
    private static final Map<Event, EventMetadata> EVENTS = new HashMap<>();

    private static long counter = 0;
    private static boolean running = false;

    private Scheduler() {
    }

    public static void initialize() {
        if (CORE.getOnFinished() != null) {
            throw new IllegalStateException("Cannot initialize the Scheduler more than once!");
        }

        System.out.println("loading scheduler");
        CORE.setOnFinished(e -> {
            counter++;
            runEvents();
            CORE.playFromStart();
        });

        CORE.play();
        running = true;
    }

    private static void runEvents() {
        if (!running) {
            return;
        }

        final Iterator<Entry<Event, EventMetadata>> eventIterator = EVENTS.entrySet().iterator();
        while (eventIterator.hasNext()) {
            final Entry<Event, EventMetadata> entry = eventIterator.next();

            final EventMetadata md = entry.getValue();
            if (md.remove) {
                eventIterator.remove();
            }

            final Event event = entry.getKey();
            if (md.enabled && counter - md.lastCounter >= event.getFrequency()) {
                md.lastCounter = counter;
                event.run();
            }

            computeTimeToOccur(md);
        }
    }

    private static void computeTimeToOccur(EventMetadata md) {
        if (!md.enabled || !running) {
            md.timeToOccur.set(EVENT_DISABLED_TIME);
            return;
        }

        md.timeToOccur.set(md.event.getFrequency() - (counter - md.lastCounter));
    }

    public static void register(Event event) {
        EVENTS.put(event, new EventMetadata(event));
    }

    public static void start() {
        running = true;
    }

    public static void stop() {
        running = false;
    }

    public static LongProperty timeToOccur(Event event) {
        final EventMetadata md = EVENTS.get(event);
        return md.timeToOccur;
    }

    public static void disableEvent(Event event) {
        final EventMetadata md = EVENTS.get(event);
        if (md == null) {
            return;
        }

        md.enabled = false;
    }

    public static void disableEvents(List<Event> events) {
        for (Event event : events) {
            disableEvent(event);
        }
    }

    public static void enableEvent(Event event) {
        final EventMetadata md = EVENTS.get(event);
        if (md == null) {
            return;
        }

        md.enabled = true;
        md.lastCounter = counter;
    }

    public static void enableEvents(List<Event> events) {
        for (Event event : events) {
            enableEvent(event);
        }
    }

    public static void removeEvent(Event event) {
        EVENTS.get(event).remove = true;
    }

    private static class EventMetadata {
        private long lastCounter;
        private boolean enabled = true;
        private boolean remove = false;
        private Event event;
        private LongProperty timeToOccur = new SimpleLongProperty();

        private EventMetadata(Event event) {
            this.event = event;
            timeToOccur.set(event.getFrequency());
        }
    }
}
