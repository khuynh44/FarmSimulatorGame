package main.java.util.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import main.java.util.Constants;

public class Saver {
    private Saver() {
    }

    @SuppressWarnings("unchecked")
    private static Object typeHandler(Object obj) {
        if (obj instanceof Map) {
            final Map<String, Object> localSaveData = new HashMap<>();

            for (Map.Entry<?, ?> entry : ((Map<?, ?>) obj).entrySet()) {
                final Object localKey = entry.getKey();
                final Object localValue = entry.getValue();
                final Object[] holder = new Object[2];
                holder[0] = typeHandler(localKey);
                holder[1] = typeHandler(localValue);
                localSaveData.put(String.valueOf(localKey.hashCode()), holder);
            }

            return localSaveData;
        }

        if (obj instanceof List) {
            final List<Object> localSaveData = new ArrayList<>();

            final List<Object> localList = (List<Object>) obj;
            for (Object localObj : localList) {
                localSaveData.add(typeHandler(localObj));
            }

            return localSaveData;
        }

        if (obj instanceof Object[]) {
            final List<Object> localSaveData = new ArrayList<>();

            final Object[] localArray = (Object[]) obj;
            for (Object localObj : localArray) {
                localSaveData.add(typeHandler(localObj));
            }

            return localSaveData;
        }

        if (obj instanceof IPersistable) {
            final Map<String, Object> recurSaveData = ((IPersistable) obj).generateSaveData();
            return generateSaveDataHelper(recurSaveData);
        }

        return obj;
    }

    private static Map<String, Object> generateSaveDataHelper(Map<String, Object> saveData) {
        for (Map.Entry<String, Object> entry : saveData.entrySet()) {
            final String key = entry.getKey();
            final Object value = entry.getValue();
            saveData.put(key, typeHandler(value));
        }

        return saveData;
    }

    public static void save(IPersistable obj, String filePath) throws IOException {
        Files.createDirectories(Paths.get(Constants.PATH_TO_SAVE));

        final DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);

        final Yaml yaml = new Yaml(options);
        final FileWriter writer = new FileWriter(Constants.PATH_TO_SAVE + filePath);

        final Map<String, Object> saveData = generateSaveDataHelper(obj.generateSaveData());

        yaml.dump(saveData, writer);
    }

    public static Map<String, Object> load(String filePath) throws IOException {
        final Yaml yaml = new Yaml();
        final File file = new File(Constants.PATH_TO_SAVE + filePath);
        final InputStream input = new FileInputStream(file);
        final Map<String, Object> loadData = yaml.load(input);

        return loadData;
    }
}
