package main.java.ui.market;

import java.io.IOException;
import java.util.Map;

import javafx.beans.property.SimpleDoubleProperty;
import main.java.entities.Player;
import main.java.entities.items.Item;
import main.java.ui.Model;
import main.java.ui.inventory.InventoryModel;
import main.java.util.definition.cache.Caches;
import main.java.util.persistence.ISaveable;
import main.java.util.persistence.Saver;
import main.java.util.sound.SoundController;

public class MarketModel extends Model implements ISaveable {
    private Market market;

    private final SimpleDoubleProperty itemCost = new SimpleDoubleProperty(0.0);

    public MarketModel() {
        market = Caches.MARKETS.getRandom();
    }

    @Override
    public void onLoad() {
        Player.getInstance().getInventoryModel().onLoad();
        getMarketInventoryModel().onLoad();

        market.selectedItem().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                itemCost.set(0.0);
                return;
            }

            itemCost.set(market.calculateCost(newValue));
        });
    }

    public InventoryModel getMarketInventoryModel() {
        return market.getInventoryModel();
    }

    public void buyItem() {
        if (market.buySelectedItem(Player.getInstance().getWallet(),
                Player.getInstance().getInventoryModel())) {
            SoundController.play("cash");
        }
    }

    public void sellItem() {
        final Item<?> selectedItem = Player.getInstance().getInventoryModel().getSelectedItem();
        market.sellItem(Player.getInstance().getWallet(),
            selectedItem, Player.getInstance().getInventoryModel());
    }

    public SimpleDoubleProperty itemCost() {
        return itemCost;
    }

    public String getMarketName() {
        return market.getName();
    }

    public void resetMarket() {
        market.resetInventory();
    }

    @Override
    public void save(String prefix) throws IOException {
        Saver.save(market, prefix + "_market.yaml");
        market.getInventoryModel().save(prefix);
    }

    @Override
    public void load(String prefix) throws IOException {
        final Map<String, Object> marketLoadData = Saver.load(prefix + "_market.yaml");
        final String id = (String) marketLoadData.get("id");

        market = Caches.MARKETS.get(id);
        market.getInventoryModel().load(prefix);
    }
}
