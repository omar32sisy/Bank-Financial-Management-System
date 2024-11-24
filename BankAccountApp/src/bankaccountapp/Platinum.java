package bankaccountapp;

/**
 * Represents a Platinum level customer in the bank application.
 * It extends the Level class and provides the specific online fee for Platinum customers.
 */
public class Platinum extends Level {

    private double onlineFee = 0.0; // Fee for online purchases for Platinum customers

    /**
     * Gets the fee for online purchases for Platinum customers.
     *
     * @return the fee for online purchases (0.0)
     */
    @Override
    public double getOnlineFee() {
        return onlineFee; // Return the online fee specific to Platinum level
    }

    @Override
    public String toString() {
        return "Platinum";
    }
}
