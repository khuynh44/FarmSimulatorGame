package main.java.entities.items.definitions;

import main.java.entities.Player;

public class PesticideDef extends ItemDef {
    @SuppressWarnings("unused")
    private PesticideDef() {
        this("default_pest", "Default Pest");
    }

    public PesticideDef(String id, String name) {
        super(id, name);
    }

    @Override
    public double getBaseCostModifier() {
        return Player.getInstance().getDifficulty().getPesticideCostMultiplier();
    }
}
