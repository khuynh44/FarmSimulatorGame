package main.java.entities.items.definitions;

public class IrrigationDef extends ItemDef {
    @SuppressWarnings("unused")
    private IrrigationDef() {
        this("default_irrigation", "Default Irrigation");
    }

    public IrrigationDef(String id, String name) {
        super(id, name);
    }
}
