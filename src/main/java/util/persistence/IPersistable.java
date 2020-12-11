package main.java.util.persistence;

import java.util.Map;

public interface IPersistable {
    public Map<String, Object> generateSaveData();
}
