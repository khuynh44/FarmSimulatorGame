package main.java.ui.worker_panel;

import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import main.java.entities.items.workers.Worker;
import main.java.ui.Controller;
import main.java.util.Constants;

public class WorkerPanelController extends Controller<WorkerPanelModel> {
    @FXML
    private ListView<Worker> workerList;
    @FXML
    private ListView<String> descriptionList;

    @Override
    public void initialize(WorkerPanelModel model) {
        super.initialize(model);

        workerList.itemsProperty().bindBidirectional(model.workerList());
        descriptionList.itemsProperty().bindBidirectional(model.descriptionList());

        workerList.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                model.generateDescriptionList(newValue);
            }
        );

        workerList.setCellFactory(worker -> new ListCell<Worker>() {
            @Override
            protected void updateItem(Worker worker, boolean empty) {
                super.updateItem(worker, empty);
                if (empty || worker == null) {
                    setText(null);
                    return;
                }

                setText(String.format("%s - %.1f", worker, worker.timeUntilWork().get()
                    / (double) Constants.DAY_LENGTH));
            }
        });
    }
}
