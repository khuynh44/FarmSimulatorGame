package main.java.ui.inventory;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.util.Callback;
import main.java.entities.items.IActivatable;
import main.java.entities.items.Item;
import main.java.ui.Model;
import main.java.util.persistence.ISaveable;
import main.java.util.persistence.Saver;

public class InventoryModel extends Model implements ISaveable {
    private final Inventory inventory;

    private final ObjectProperty<ObservableList<Map.Entry<SimpleObjectProperty<Item<?>>,
        SimpleIntegerProperty>>> inventoryTable = new SimpleObjectProperty<>();
    private ObjectProperty<Item<?>> item = new SimpleObjectProperty<>();

    private final TableColumn<Map.Entry<SimpleObjectProperty<Item<?>>, SimpleIntegerProperty>,
        SimpleObjectProperty<Item<?>>> itemColumn = new TableColumn<>("Item");
    private final TableColumn<Map.Entry<SimpleObjectProperty<Item<?>>, SimpleIntegerProperty>,
        SimpleIntegerProperty> quantityColumn = new TableColumn<>("#");

    private final ObjectProperty<ObservableList<String>> descriptionList =
        new SimpleObjectProperty<>();

    private final ObjectProperty<Image> itemImage = new SimpleObjectProperty<>();

    private final SimpleStringProperty capacityText = new SimpleStringProperty();

    private final BooleanBinding isSpecialItemSelected =
        Bindings.createBooleanBinding(() -> {
            if (item.get() instanceof IActivatable) {
                return true;
            }

            return false;
        }, item);

    public InventoryModel(int capacity) {
        itemColumn.setCellValueFactory(p ->
            new ReadOnlyObjectWrapper<>(p.getValue().getKey()));
        itemColumn.setCellFactory(item -> new TableCell<Entry<SimpleObjectProperty<Item<?>>,
                SimpleIntegerProperty>, SimpleObjectProperty<Item<?>>>() {
            @Override
            protected void updateItem(SimpleObjectProperty<Item<?>> item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    return;
                }

                setText(item.get().toString());
            }
        });

        lockWidth(itemColumn, 221);

        quantityColumn.setCellValueFactory(p ->
            new ReadOnlyObjectWrapper<>(p.getValue().getValue()));
        quantityColumn.setCellFactory(item -> new TableCell<Entry<SimpleObjectProperty<Item<?>>,
                SimpleIntegerProperty>, SimpleIntegerProperty>() {
            @Override
            protected void updateItem(SimpleIntegerProperty item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("");
                    return;
                }

                setText(String.valueOf(item.get()));
            }
        });

        lockWidth(quantityColumn, 30);

        inventoryTable.set(FXCollections.observableArrayList(extractor()));

        descriptionList.set(FXCollections.observableArrayList());

        inventory = new Inventory(capacity);
    }

    public InventoryModel() {
        this(8);
    }

    public static Callback<Map.Entry<SimpleObjectProperty<Item<?>>, SimpleIntegerProperty>,
            Observable[]> extractor() {
        return new Callback<Map.Entry<SimpleObjectProperty<Item<?>>, SimpleIntegerProperty>,
                Observable[]>() {
            @Override
            public Observable[] call(Map.Entry<SimpleObjectProperty<Item<?>>,
                    SimpleIntegerProperty> p) {
                return new Observable[]{
                    p.getKey(),
                    p.getValue()
                };
            }
        };
    }

    private void lockWidth(TableColumn<?, ?> column, double width) {
        column.setMaxWidth(width);
        column.setMinWidth(width);
        column.setPrefWidth(width);
        column.setResizable(false);
    }

    private void updateItems() {
        final Set<Entry<Item<?>, Integer>> inventorySet = inventory.expose().entrySet();
        final Set<Entry<SimpleObjectProperty<Item<?>>, SimpleIntegerProperty>>
            observableInventorySet = new HashSet<>();
        inventorySet.forEach(entry -> {
            observableInventorySet.add(
                new AbstractMap.SimpleEntry<>(new SimpleObjectProperty<>(entry.getKey()),
                new SimpleIntegerProperty(entry.getValue()))
            );
        });

        final ObservableList<Map.Entry<SimpleObjectProperty<Item<?>>, SimpleIntegerProperty>>
            items = FXCollections.observableArrayList(observableInventorySet);
        inventoryTable.set(items);

        capacityText.set(inventory.generateCapacityString());
    }

    public void clear() {
        inventory.clear();
        item.set(null);
    }

    @Override
    public void onLoad() {
        updateItems();
    }

    public ObjectProperty<ObservableList<Map.Entry<SimpleObjectProperty<Item<?>>,
            SimpleIntegerProperty>>> inventoryTable() {
        return inventoryTable;
    }

    public void setItem(Item<?> item) {
        itemImage.set(item == null ? null : item.getImage());
        this.item.set(item);
    }

    public Item<?> getSelectedItem() {
        return item.get();
    }

    public ObjectProperty<Item<?>> selectedItem() {
        return item;
    }

    public TableColumn<Map.Entry<SimpleObjectProperty<Item<?>>, SimpleIntegerProperty>,
            SimpleObjectProperty<Item<?>>> itemColumnn() {
        return itemColumn;
    }

    public TableColumn<Map.Entry<SimpleObjectProperty<Item<?>>, SimpleIntegerProperty>,
            SimpleIntegerProperty> quantityColumn() {
        return quantityColumn;
    }

    public ObjectProperty<ObservableList<String>> descriptionList() {
        return descriptionList;
    }

    public void generateDescriptionList() {
        descriptionList.get().clear();

        if (item.get() == null) {
            return;
        }

        descriptionList.get().addAll(item.get().generateDescriptionEntries());
    }

    public ObjectProperty<Image> itemImage() {
        return itemImage;
    }

    public SimpleStringProperty capacityText() {
        return capacityText;
    }

    public boolean incrementCount(Item<?> item) {
        final boolean succesful = inventory.incrementCount(item);

        if (succesful) {
            updateItems();
        }

        return succesful;
    }

    public boolean decrementCount(Item<?> item) {
        final boolean succesful = inventory.decrementCount(item);

        if (succesful) {
            updateItems();
        }

        return succesful;
    }

    public boolean operationOnCount(Item<?> item, int amount) {
        final boolean succesful = inventory.operationOnCount(item, amount);

        if (succesful) {
            updateItems();
        }

        return succesful;
    }

    public BooleanBinding isSpecialItemSelected() {
        return isSpecialItemSelected;
    }

    public int getCount(Item<?> item) {
        return inventory.getCount(item);
    }

    @Override
    public void save(String prefix) throws IOException {
        Saver.save(inventory, prefix + "_inventory.yaml");
    }

    @Override
    public void load(String prefix) throws IOException {
        final Map<String, Object> inventoryLoadData = Saver.load(prefix + "_inventory.yaml");
        inventory.processLoadData(inventoryLoadData);
    }

    public boolean hasAllDeadCrops() {
        return inventory.hasAllDeadCrops();
    }
}
