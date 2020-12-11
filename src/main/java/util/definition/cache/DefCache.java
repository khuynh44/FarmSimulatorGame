package main.java.util.definition.cache;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class DefCache<T extends ICacheable> {
    private final HashMap<String, T> cache = new HashMap<>();

    public T intern(T def) {
        return cache.computeIfAbsent(def.getId(), k -> def);
    }

    public T get(String id) {
        return cache.getOrDefault(id, null);
    }

    public boolean has(String id) {
        return cache.containsKey(id);
    }

    @SuppressWarnings("unchecked")
    public T getRandom() {
        if (cache.size() <= 0) {
            return null;
        }

        final Object[] values = cache.values().toArray();

        final int randomIndex = ThreadLocalRandom.current().nextInt(values.length);
        return (T) values[randomIndex];
    }

    public Set<String> getIds() {
        return cache.keySet();
    }
}
