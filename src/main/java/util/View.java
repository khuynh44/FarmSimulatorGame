package main.java.util;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;

import java.io.IOException;

import main.java.ui.Controller;
import main.java.ui.Model;

public enum View {
    MAIN("/main/java/start.fxml"),
    CONFIG("/main/java/ui/config/config.fxml"),
    FARM("/main/java/ui/farm/farm.fxml"),
    MARKET("/main/java/ui/market/market.fxml"),
    PERSISTENCE("/main/java/ui/persistence/persistence.fxml"),
    END("/main/java/ui/end/end.fxml");

    private final String fxmlFileName;
    private FXMLLoader loader;
    private Parent root;

    private View(String fxmlFileName) {
        this.fxmlFileName = fxmlFileName;
    }

    public void unloadView() {
        loader = null;
        root = null;
    }

    public Parent loadView() {
        if (root != null) {
            throw new IllegalStateException("Cannot load a view more than once.");
        }

        System.out.println("  - " + fxmlFileName);

        loader = new FXMLLoader();

        try {
            loader.setLocation(getClass().getResource(fxmlFileName));

            root = loader.load();
            return root;
        } catch (IOException e) {
            System.out.println("Unable to load the fxml file for " + fxmlFileName);
            System.out.println(e.getMessage());
            return null;
        }
    }

    public <T extends Model> void applyModel(T model) {
        final Controller<T> controller = getController();

        try {
            controller.initialize(model);
        } catch (IllegalStateException e) {
            System.out.println(e);
        }
    }

    public <T extends Model> void loadAndApplyModel(T model) {
        loadView();
        applyModel(model);
    }

    public Parent getRoot() {
        return root;
    }

    public <T> T getController() {
        return loader.getController();
    }

    public <T extends Model> T getModel() {
        Controller<T> controller = getController();
        return controller.getModel();
    }

    public void setViewOn(ActionEvent event) {
        if (getController() instanceof Controller) {
            getModel().onLoad();
        }
        ((Node) event.getSource()).getScene().setRoot(root);
    }
}
