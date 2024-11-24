package bankaccountapp;

/**
 * Represents a Gold level customer in the bank application.
 */
public class Gold extends Level {

    private double onlineFee = 10.0;

    @Override
    public double getOnlineFee() {
        return onlineFee;
    }

    @Override
    public String toString() {
        return "Gold";
    }
}
