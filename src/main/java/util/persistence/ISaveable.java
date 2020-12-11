package main.java.util.persistence;

import java.io.IOException;

public interface ISaveable {
    public void save(String prefix) throws IOException;
    public void load(String prefix) throws IOException;
}
