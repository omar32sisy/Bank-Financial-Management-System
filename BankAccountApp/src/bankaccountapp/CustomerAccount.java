package bankaccountapp;

/**
 * Represents a customer's bank account.
 */
public class CustomerAccount {

    private double balance;

    public CustomerAccount(double initialBalance) {
        this.balance = initialBalance;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
