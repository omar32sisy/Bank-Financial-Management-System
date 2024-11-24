package bankaccountapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Represents a customer in the bank application.
 * 
 * Overview:
 * This class represents a bank customer, including their account information, login credentials, and role.
 * It allows for operations such as deposit, withdrawal, online purchases, and login/logout.
 * This class is mutable as it allows modifications to the customer's account balance, password, and role.
 * 
 * Abstraction Function:
 * AF(c) = A customer with username c.username, password c.password, role c.role, account balance c.account.balance,
 *         and level c.level determined by the balance.
 * 
 * Representation Invariant:
 * RI(c) = c.username != null && c.password != null && c.role != null && c.account != null &&
 *         (c.role.equals("Manager") || c.role.equals("Customer")) &&
 *         c.level != null
 */
public class Customer {

    private String username;
    private String password;
    private String role; // Role for login (Manager or Customer)
    private Level level; // Level based on balance (Silver, Gold, or Platinum)
    private CustomerAccount account;

    private static final String DATABASE_DIR = System.getProperty("user.dir") + "/database/";
    private static final double SILVER_THRESHOLD = 10000.0;
    private static final double GOLD_THRESHOLD = 20000.0;

    public Customer(String username) {
        this.username = username;
        loadCustomerFromFile();
        updateLevel(); // Initialize level based on balance
    }

    private void loadCustomerFromFile() {
        File file = new File(DATABASE_DIR, username);
        if (!file.exists()) {
            throw new IllegalArgumentException("Customer file does not exist.");
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            this.username = reader.readLine();
            this.password = reader.readLine();
            this.role = reader.readLine();
            double balance = Double.parseDouble(reader.readLine());
            this.account = new CustomerAccount(balance);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
    }

    /**
     * Sets the password for the customer.
     * 
     * @param password the new password
     * @modifies this.password
     * @effects updates the password and writes the change to the customer file
     */
    public void setPassword(String password) {
        this.password = password;
        updateCustomerFile();
    }

    /**
     * Sets the role for the customer.
     * 
     * @param role the new role (either "Manager" or "Customer")
     * @requires role.equals("Manager") || role.equals("Customer")
     * @modifies this.role
     * @effects updates the role and writes the change to the customer file
     */
    public void setRole(String role) {
        this.role = role;
        updateCustomerFile();
    }

    /**
     * Deposits a specified amount into the customer's account.
     * 
     * @param amount the amount to deposit
     * @requires amount > 0
     * @modifies this.account.balance
     * @effects increases the account balance by the specified amount and updates the level
     */
    public void deposit(double amount) {
        if (amount > 0) {
            account.setBalance(account.getBalance() + amount);
            updateLevel(); // Update level based on new balance
            updateCustomerFile();
        } else {
            System.out.println("Deposit amount must be positive.");
        }
    }

    /**
     * Withdraws a specified amount from the customer's account.
     * 
     * @param amount the amount to withdraw
     * @requires amount > 0 && amount <= account.getBalance()
     * @modifies this.account.balance
     * @effects decreases the account balance by the specified amount and updates the level
     */
    public void withdraw(double amount) {
        if (amount > 0 && amount <= account.getBalance()) {
            account.setBalance(account.getBalance() - amount);
            updateLevel(); // Update level based on new balance
            updateCustomerFile();
        } else {
            System.out.println("Insufficient funds or invalid amount.");
        }
    }

    /**
     * Performs an online purchase if the balance is sufficient.
     * 
     * @param amount the amount to purchase
     * @requires amount >= 50.0
     * @modifies this.account.balance
     * @effects deducts the purchase amount and the online fee from the account balance if sufficient funds exist
     * @return true if the purchase was successful, false otherwise
     */
    public boolean doOnlinePurchase(double amount) {
        if (amount >= 50.0) {
            double fee = level.getOnlineFee();
            double totalAmount = amount + fee;

            if (account.getBalance() >= totalAmount) {
                account.setBalance(account.getBalance() - totalAmount);
                updateLevel();
                updateCustomerFile();
                
                return true;
            } else {
                System.out.println("Insufficient funds for the purchase and fee.");
                return false;
            }
        } else {
            System.out.println("Minimum purchase amount is $50.");
            return false;
        }
    }

    /**
     * Logs in the customer with the provided credentials.
     * 
     * @param username the username
     * @param password the password
     * @param role the role (Manager or Customer)
     * @return true if login is successful, false otherwise
     */
    public boolean login(String username, String password, String role) {
        return this.username.equals(username) && this.password.equals(password) && this.role.equals(role);
    }

    /**
     * Logs out the customer.
     */
    public void logout() {
        // Clear session details or handle logout operations as needed
        System.out.println("Logged out.");
    }

    private void updateLevel() {
        double balance = account.getBalance();
        if (balance >= GOLD_THRESHOLD) {
            this.level = new Platinum();
        } else if (balance >= SILVER_THRESHOLD) {
            this.level = new Gold();
        } else {
            this.level = new Silver();
        }
    }

    private void updateCustomerFile() {
        File file = new File(DATABASE_DIR, username);
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.println(username);
            writer.println(password);
            writer.println(role);
            writer.println(account.getBalance());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Getter methods
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public Level getLevel() {
        return level;
    }

    public double getBalance() {
        return account.getBalance();
    }
    
    public CustomerAccount getAccount() {
        return account;
    }

    /**
     * Checks if the representation invariant holds for this object.
     * 
     * @return true if the rep invariant holds, false otherwise
     */
    public boolean repOk() {
        return username != null && password != null && role != null && account != null &&
               (role.equals("Manager") || role.equals("Customer")) &&
               level != null;
    }

    /**
     * Returns a string representation of the customer.
     * 
     * @return a string representation of the customer
     */
    @Override
    public String toString() {
        return "Customer[username=" + username + ", balance=" + account.getBalance() + ", level=" + level + "]";
    }
}
