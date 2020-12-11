package main.java;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.java.ui.config.ConfigModel;
import main.java.ui.end.EndModel;
import main.java.ui.farm.FarmModel;
import main.java.ui.market.MarketModel;
import main.java.ui.persistence.PersistenceModel;
import main.java.util.View;
import main.java.util.definition.Definitions;
import main.java.util.KeyTracker;
import main.java.util.Scheduler;
import main.java.util.Constants;
import main.java.util.sound.SoundController;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        Scheduler.initialize();

        Definitions.loadPlants();
        Definitions.loadAffixes();
        Definitions.loadWorkerNames();
        Definitions.loadPesticide();
        Definitions.loadIrrigation();
        Definitions.loadTractor();
        Definitions.loadWorkers();
        Definitions.loadfertilizers();
        Definitions.loadMarkets();
        Definitions.loadDifficulties();
        Definitions.loadSounds();

        System.out.println("loading fxml: ");
        Parent root = View.MAIN.loadView();

        View.CONFIG.loadAndApplyModel(new ConfigModel());
        View.FARM.loadAndApplyModel(new FarmModel());
        View.MARKET.loadAndApplyModel(new MarketModel());
        View.PERSISTENCE.loadAndApplyModel(new PersistenceModel());
        View.END.loadAndApplyModel(new EndModel());

        primaryStage.setResizable(false);

        final Scene primaryScene = new Scene(root, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        primaryStage.setTitle(Constants.TITLE);
        primaryStage.setScene(primaryScene);

        primaryScene.setOnKeyPressed(e -> KeyTracker.setkey(e.getCode()));
        primaryScene.setOnKeyReleased(e -> KeyTracker.unsetKey(e.getCode()));

        SoundController.setBackgroundMusic("main_background_nusic");

        primaryStage.show();
    }

    public void switchToConfigScreen(ActionEvent event) {
        View.CONFIG.setViewOn(event);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
