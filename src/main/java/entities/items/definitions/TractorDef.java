package main.java.entities.items.definitions;

public class TractorDef extends ItemDef {
    @SuppressWarnings("unused")
    private TractorDef() {
        this("default_tractor", "Default Tractor");
    }

    public TractorDef(String id, String name) {
        super(id, name);
    }
}
