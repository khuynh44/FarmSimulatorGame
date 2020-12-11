package main.java.entities;

import main.java.util.definition.cache.ICacheable;

public class Name implements ICacheable {
    // this class is unnecesarrily large due to mandated checkstyle
    private final String name;

    public Name(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public String getId() {
        return name;
    }
}
