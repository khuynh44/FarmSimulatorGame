package main.java.entities;

import java.util.HashMap;
import java.util.Map;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import main.java.util.persistence.IPersistable;

public class Wallet implements IPersistable {
    private DoubleProperty balance = new SimpleDoubleProperty();

    public Wallet(double balance) {
        this.balance.set(balance);
    }

    public Wallet() {
        this(0);
    }

    public boolean testOperationOnMoney(double amount) {
        if (balance.get() + amount < 0) {
            return false;
        }

        return true;
    }

    public boolean operationOnMoney(double amount) {
        if (!testOperationOnMoney(amount)) {
            return false;
        }

        balance.set(balance.get() + amount);
        return true;
    }

    public boolean burnMoney(double amount) {
        if (amount < 0) {
            return false;
        }

        if (!testOperationOnMoney(-1 * amount)) {
            return false;
        }

        balance.set(balance.get() - amount);
        return true;
    }

    public boolean testSendMoneyTo(double amount, Wallet wallet) {
        if (amount < 0) {
            return false;
        }
        if (wallet == null) {
            return false;
        }
        if (this == wallet) {
            return false;
        }

        return testOperationOnMoney(-1 * amount);
    }

    public boolean sendMoneyTo(double amount, Wallet wallet) {
        if (!testSendMoneyTo(amount, wallet)) {
            return false;
        }

        wallet.balance.set(wallet.balance.get() + amount);
        burnMoney(amount);
        return true;
    }

    public double getBalance() {
        return balance.get();
    }

    public DoubleProperty balance() {
        return balance;
    }

    public boolean hasMoney() {
        return balance.get() > 0;
    }

    public void zeroBalance() {
        balance.set(0);
    }

    @Override
    public String toString() {
        return String.format("$%.2f", balance.get());
    }

    @Override
    public Map<String, Object> generateSaveData() {
        final Map<String, Object> saveData = new HashMap<>();
        saveData.put("balance", balance.get());

        return saveData;
    }

    public void processLoadData(Map<String, Object> loadData) {
        final double balance = (Double) loadData.get("balance");
        this.balance.set(balance);
    }
}
