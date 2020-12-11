package test.java.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.beans.property.DoubleProperty;
import main.java.entities.Wallet;

public class WalletTest {
    private Wallet wallet;
    private Wallet recipient;

    @BeforeEach
    public void beforeEach() {
        wallet = new Wallet(100.0);
        recipient = new Wallet(1.0);
    }

    private void checkWalletBalance(Wallet wallet, double expectedBalance)
            throws NoSuchFieldException, IllegalAccessException {
        final Field balance = wallet.getClass().getDeclaredField("balance");
        balance.setAccessible(true);

        final DoubleProperty extractedBalance = (DoubleProperty) balance.get(wallet);

        assertNotNull(extractedBalance);
        assertEquals(expectedBalance, extractedBalance.get());
    }

    @Test
    public void testSendMoneyTo() throws NoSuchFieldException, IllegalAccessException {
        final double ourExpectedBalance = 49.8;
        final double recipientExpectedBalance = 51.2;

        final boolean actualCondition = wallet.sendMoneyTo(50.2, recipient);

        assertTrue(actualCondition);
        checkWalletBalance(wallet, ourExpectedBalance);
        checkWalletBalance(recipient, recipientExpectedBalance);
    }

    @Test
    public void testSendMoneyToNegative()
            throws NoSuchFieldException, IllegalAccessException {
        final double ourExpectedBalance = 100.0;
        final double recipientExpectedBalance = 1.0;

        final boolean actual = wallet.sendMoneyTo(-1.0, recipient);

        assertFalse(actual);
        checkWalletBalance(wallet, ourExpectedBalance);
        checkWalletBalance(recipient, recipientExpectedBalance);
    }

    @Test
    public void testSendMoneyToNull() throws NoSuchFieldException, IllegalAccessException {
        final double ourExpectedBalance = 100.0;
        final boolean actual = wallet.sendMoneyTo(1, null);

        assertFalse(actual);
        checkWalletBalance(wallet, ourExpectedBalance);
    }

    @Test
    public void testSendMoneyToSelf() throws NoSuchFieldException, IllegalAccessException {
        final double expectedBalance = 100.0;

        final boolean actual = wallet.sendMoneyTo(1, wallet);

        assertFalse(actual);
        checkWalletBalance(wallet, expectedBalance);
    }

    @Test
    public void testSendMoneyToNotEnoughMoney()
            throws NoSuchFieldException, IllegalAccessException {
        final double ourExpectedBalance = 100.0;
        final double recipientExpectedBalance = 1.0;

        final boolean actual = wallet.sendMoneyTo(200, recipient);

        assertFalse(actual);
        checkWalletBalance(wallet, ourExpectedBalance);
        checkWalletBalance(recipient, recipientExpectedBalance);
    }

    @Test
    public void testDefaultConstructor() throws NoSuchFieldException, IllegalAccessException {
        final Wallet wallet = new Wallet();

        checkWalletBalance(wallet, 0.0);
    }

    @Test
    public void testToStringWithBalance() {
        final String actual = wallet.toString();

        assertEquals("$100.00", actual);
    }

    @Test
    public void testToStringEmptyBalance() {
        final Wallet emptyBalanceWallet = new Wallet(0);
        final String actual = emptyBalanceWallet.toString();

        assertEquals("$0.00", actual);
    }

    @Test
    public void testTestSendMoneyTo() throws NoSuchFieldException, IllegalAccessException {
        final double ourExpectedBalance = 100.0;
        final double recipientExpectedBalance = 1.0;

        final boolean actual = wallet.testSendMoneyTo(50, recipient);

        assertTrue(actual);
        checkWalletBalance(wallet, ourExpectedBalance);
        checkWalletBalance(recipient, recipientExpectedBalance);
    }

    @Test
    public void testTestSendMoneyToNotEnoughMoney()
            throws NoSuchFieldException, IllegalAccessException {
        final double ourExpectedBalance = 100.0;
        final double recipientExpectedBalance = 1.0;

        final boolean actual = wallet.testSendMoneyTo(500, recipient);

        assertFalse(actual);
        checkWalletBalance(wallet, ourExpectedBalance);
        checkWalletBalance(recipient, recipientExpectedBalance);
    }

    @Test
    public void testTestSendMoneyToSelf()
            throws NoSuchFieldException, IllegalAccessException {
        final double ourExpectedBalance = 100.0;
        final double recipientExpectedBalance = 1.0;

        final boolean actual = wallet.testSendMoneyTo(2, wallet);

        assertFalse(actual);
        checkWalletBalance(wallet, ourExpectedBalance);
        checkWalletBalance(recipient, recipientExpectedBalance);
    }

    @Test
    public void testTestSendMoneyToNull()
            throws NoSuchFieldException, IllegalAccessException {
        final double ourExpectedBalance = 100.0;

        final boolean actual = wallet.testSendMoneyTo(2, null);

        assertFalse(actual);
        checkWalletBalance(wallet, ourExpectedBalance);
    }

    @Test
    public void testTestSendMoneyToNegativeValue()
            throws NoSuchFieldException, IllegalAccessException {
        final double ourExpectedBalance = 100.0;
        final double recipientExpectedBalance = 1.0;

        final boolean actual = wallet.testSendMoneyTo(-10, recipient);

        assertFalse(actual);
        checkWalletBalance(wallet, ourExpectedBalance);
        checkWalletBalance(recipient, recipientExpectedBalance);
    }

    @Test
    public void testBurnMoney() throws NoSuchFieldException, IllegalAccessException {
        final double ourExpectedBalance = 90.0;

        final boolean actual = wallet.burnMoney(10);

        assertTrue(actual);
        checkWalletBalance(wallet, ourExpectedBalance);
    }

    @Test
    public void testBurnMoneyNegative() throws NoSuchFieldException, IllegalAccessException {
        final double ourExpectedBalance = 100.0;

        final boolean actual = wallet.burnMoney(-10);

        assertFalse(actual);
        checkWalletBalance(wallet, ourExpectedBalance);
    }

    @Test
    public void testOperationOnMoneyPositive() throws NoSuchFieldException, IllegalAccessException {
        final double ourExpectedBalance = 110.0;

        final boolean actual = wallet.operationOnMoney(10);

        assertTrue(actual);
        checkWalletBalance(wallet, ourExpectedBalance);
    }

    @Test
    public void testOperationOnMoneyNegative() throws NoSuchFieldException, IllegalAccessException {
        final double ourExpectedBalance = 10.0;

        final boolean actual = wallet.operationOnMoney(-90);

        assertTrue(actual);
        checkWalletBalance(wallet, ourExpectedBalance);
    }

    @Test
    public void testOperationOnMoneyNegativeNotEnoughMoney()
            throws NoSuchFieldException, IllegalAccessException {
        final double ourExpectedBalance = 100.0;

        final boolean actual = wallet.operationOnMoney(-110);

        assertFalse(actual);
        checkWalletBalance(wallet, ourExpectedBalance);
    }

    @Test
    public void testOperationOnMoneyDecimal() throws NoSuchFieldException, IllegalAccessException {
        final double ourExpectedBalance = 100.01;

        final boolean actual = wallet.operationOnMoney(0.01);

        assertTrue(actual);
        checkWalletBalance(wallet, ourExpectedBalance);
    }

    @Test
    public void testBalance() {
        final DoubleProperty actual = wallet.balance();

        assertNotNull(actual);
        assertEquals(100.0, actual.get());
    }
}
