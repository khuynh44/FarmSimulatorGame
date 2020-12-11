package main.java.ui.worker_panel;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Callback;
import main.java.entities.items.workers.Harvester;
import main.java.entities.items.workers.Waterer;
import main.java.entities.items.workers.Worker;
import main.java.ui.Model;
import main.java.ui.farm.Plot;
import main.java.ui.farm.WorkHandler;
import main.java.util.persistence.IPersistable;
import main.java.util.persistence.ISaveable;
import main.java.util.persistence.Saver;

public class WorkerPanelModel extends Model implements IPersistable, ISaveable {
    private final ObjectProperty<ObservableList<Worker>> workerList =
        new SimpleObjectProperty<>(FXCollections.observableArrayList(extractor()));
    private final ObjectProperty<ObservableList<String>> descriptionList =
        new SimpleObjectProperty<>(FXCollections.observableArrayList());

    private WorkHandler<Waterer> watererWorkHandler =
        new WorkHandler<>(new WorkHandler.WaterComparator());
    private WorkHandler<Harvester> harvesterWorkHandler =
        new WorkHandler<>(new WorkHandler.GrowthComparator());

    public void unregisterWithWorkHandlers(Plot plot) {
        watererWorkHandler.removePlot(plot);
        harvesterWorkHandler.removePlot(plot);
    }

    public void registerWithWorkHandlers(Plot plot) {
        watererWorkHandler.addPlot(plot);
        harvesterWorkHandler.addPlot(plot);
    }

    public void refreshWorkHandlers(Plot plot) {
        watererWorkHandler.refreshPlot(plot);
        harvesterWorkHandler.refreshPlot(plot);
    }

    public void startWorkHandlers() {
        watererWorkHandler.startAll();
        harvesterWorkHandler.startAll();
    }

    public void stopWorkHandlers() {
        watererWorkHandler.stopAll();
        harvesterWorkHandler.stopAll();
    }

    public void payWorkers() {
        watererWorkHandler.payWorkers(this);
        harvesterWorkHandler.payWorkers(this);
    }

    public void removeWorker(Worker worker) {
        workerList.get().remove(worker);
    }

    public void removeAllWorkers() {
        workerList.get().clear();
        watererWorkHandler.removeAllWorkers();
        harvesterWorkHandler.removeAllWorkers();
    }

    public void addWorker(Worker worker) {
        if (worker instanceof Harvester) {
            harvesterWorkHandler.addWorker((Harvester) worker);
        } else if (worker instanceof Waterer) {
            watererWorkHandler.addWorker((Waterer) worker);
        }

        workerList.get().add(worker);
    }

    public void generateDescriptionList(Worker worker) {
        descriptionList.get().clear();

        if (worker == null) {
            return;
        }

        descriptionList.get().addAll(worker.generateDescriptionEntries());
    }

    public ObjectProperty<ObservableList<Worker>> workerList() {
        return workerList;
    }

    public ObjectProperty<ObservableList<String>> descriptionList() {
        return descriptionList;
    }

    private Callback<Worker, Observable[]> extractor() {
        return new Callback<Worker, Observable[]>() {
            @Override
            public Observable[] call(Worker worker) {
                return new Observable[]{
                    worker.timeUntilWork()
                };
            }
        };
    }

    @Override
    public Map<String, Object> generateSaveData() {
        final Map<String, Object> saveData = new HashMap<>();
        saveData.put("worker_list", workerList.get());

        return saveData;
    }

    @Override
    public void save(String prefix) throws IOException {
        Saver.save(this, prefix + "_worker_model.yaml");
    }

    @Override
    @SuppressWarnings("unchecked")
    public void load(String prefix) throws IOException {
        removeAllWorkers();
        final Map<String, Object> loadData = Saver.load(prefix + "_worker_model.yaml");

        final List<Map<String, Object>> workerLoadData =
            (List<Map<String, Object>>) loadData.get("worker_list");
        for (Map<String, Object> workerDatum : workerLoadData) {
            addWorker(Worker.fromLoadData(workerDatum));
        }
    }
}
