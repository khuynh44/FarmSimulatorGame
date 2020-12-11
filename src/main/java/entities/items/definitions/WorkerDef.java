package main.java.entities.items.definitions;

import main.java.entities.Player;

public class WorkerDef extends ItemDef {
    private final long baseWorkTime;

    private WorkerDef() {
        super("blank_worker", "blank_worker");
        baseCost = 10;
        baseWorkTime = 1;
    }

    public WorkerDef(String id, long baseWorkTime) {
        super(id, id);

        this.baseWorkTime = baseWorkTime;
    }

    public long getBaseWorkTime() {
        return baseWorkTime;
    }

    @Override
    public double getBaseCostModifier() {
        return Player.getInstance().getDifficulty().getWorkerCostMultiplier();
    }
}
