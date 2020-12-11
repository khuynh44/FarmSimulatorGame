package main.java.entities.items.workers;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import javafx.beans.property.LongProperty;
import main.java.entities.Affix;
import main.java.entities.Affixes;
import main.java.entities.Name;
import main.java.entities.items.IActivatable;
import main.java.entities.items.Item;
import main.java.entities.items.definitions.WorkerDef;
import main.java.ui.farm.FieldGrid;
import main.java.ui.farm.Plot;
import main.java.util.Constants;
import main.java.util.Equations;
import main.java.util.events.Event;
import main.java.util.Scheduler;
import main.java.util.definition.cache.Caches;

public abstract class Worker extends Item<WorkerDef> implements IActivatable {
    private Event event;

    public Worker(WorkerDef def) {
        super(def);
        final Name firstName = Caches.SingleNames.FIRST.instance().getRandom();
        final Name lastName = Caches.SingleNames.LAST.instance().getRandom();

        final Affix prefix =
            new Affix(firstName != null ? firstName.toString() : "",
                ThreadLocalRandom.current().nextInt(20) - 10);
        final Affix suffix =
            new Affix(lastName != null ? lastName.toString() : "",
                ThreadLocalRandom.current().nextInt(20) - 10);
        affixes = new Affixes(prefix, suffix);
    }

    public Worker(WorkerDef def, int activeImageIndex, Affixes affixes) {
        super(def, activeImageIndex, affixes);
    }

    public abstract void work(Plot plot);

    public void setEvent(Event event) {
        this.event = event;
        Scheduler.register(event);
    }

    public void unsetEvent() {
        Scheduler.removeEvent(event);
    }

    public Event getEvent() {
        return event;
    }

    public long getWorkTime() {
        return (long) Equations.weighWithModifiers(def.getBaseWorkTime(), -1 * getModifier()) + 2;
    }

    public LongProperty timeUntilWork() {
        return Scheduler.timeToOccur(event);
    }

    @Override
    public boolean activate(FieldGrid fieldGrid) {
        fieldGrid.addWorker(this);
        return true;
    }

    @Override
    public List<String> generateDescriptionEntries() {
        List<String> descriptionEntries = super.generateDescriptionEntries();

        descriptionEntries.add(String.format("worker type: %s", this.getClass().getSimpleName()));
        descriptionEntries.add(String.format("work time: %.2f",
            getWorkTime() / (double) Constants.DAY_LENGTH));
        descriptionEntries.add("pay period: 14");

        return descriptionEntries;
    }

    @Override
    public String toString() {
        return String.format("%s %s", affixes.getPrefix(), affixes.getSuffix());
    }

    @SuppressWarnings("unchecked")
    public static Worker fromLoadData(Map<String, Object> loadData) {
        final String id = (String) loadData.get("id");
        final int activeImageIndex = (Integer) loadData.get("activeImageIndex");
        final Affixes affixes = Affixes.fromLoadData((Map<String, Object>) loadData.get("affixes"));

        if (id.equals("harvester")) {
            return new Harvester(activeImageIndex, affixes);
        } else if (id.equals("waterer")) {
            return new Waterer(activeImageIndex, affixes);
        }

        return null;
    }
}
