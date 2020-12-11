package main.java.ui.inventory;

import java.util.Map;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import main.java.entities.items.Item;
import main.java.ui.Controller;

public class InventoryController extends Controller<InventoryModel> {
    @FXML
    private TableView<Map.Entry<SimpleObjectProperty<Item<?>>, SimpleIntegerProperty>>
        inventoryTable;
    @FXML
    private ListView<String> itemDescriptionList;
    @FXML
    private ImageView itemImage;
    @FXML
    private Label capacityLabel;

    @Override
    @SuppressWarnings("unchecked")
    public void initialize(InventoryModel model) {
        // to not call super because we need to re-initialize on load
        this.model = model;

        inventoryTable.getColumns().setAll(model.itemColumnn(), model.quantityColumn());
        inventoryTable.itemsProperty().unbind();
        inventoryTable.itemsProperty().bindBidirectional(model.inventoryTable());

        inventoryTable.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                model.setItem(newValue == null ? null : newValue.getKey().get());
                model.generateDescriptionList();
            }
        );

        itemDescriptionList.itemsProperty().unbind();
        itemDescriptionList.itemsProperty().bindBidirectional(model.descriptionList());

        itemImage.imageProperty().unbind();
        itemImage.imageProperty().bindBidirectional(model.itemImage());

        capacityLabel.textProperty().unbind();
        capacityLabel.textProperty().bindBidirectional(model.capacityText());
    }
}
