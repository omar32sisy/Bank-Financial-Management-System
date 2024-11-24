package bankaccountapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Manager class for handling customer operations in the bank application.
 */
public class Manager {

    private static final String DATABASE_DIR = System.getProperty("user.dir") + "/database/";
    private static Map<String, Customer> customers = new HashMap<>();

    private static final String MANAGER_USERNAME = "admin";
    private static final String MANAGER_PASSWORD = "admin";
    private static final String MANAGER_ROLE = "Manager";

    static {
        File dbDir = new File(DATABASE_DIR);
        if (!dbDir.exists()) {
            dbDir.mkdirs(); // Create the database directory if it doesn't exist
        }
        populateCustomers();
    }

    private static void populateCustomers() {
        File dbDir = new File(DATABASE_DIR);
        File[] files = dbDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    String username = file.getName();
                    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                        username=reader.readLine();
                        String password = reader.readLine();
                        String role = reader.readLine();
                        double balance = Double.parseDouble(reader.readLine());
                        // Create a Customer object and add it to the map
                        Customer customer = new Customer(username);
                        customer.setPassword(password);
                        customer.setRole(role);
                        customer.getAccount().setBalance(balance);
                        customers.put(username, customer);
                    } catch (IOException | NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static ArrayList<Customer> getCustomers() {
        return new ArrayList<>(customers.values());
    }

    public static void addCustomer(String username, String password) {
        if (!customers.containsKey(username)) {
            saveCustomerToFile(username, password, "Customer", 100.0);
            Customer customer = new Customer(username);
            customers.put(username, customer);
        }
    }

    public static void deleteCustomer(String username) {
        customers.remove(username);
        File file = new File(DATABASE_DIR, username);
        if (file.exists()) {
            file.delete();
        }
    }

    private static void saveCustomerToFile(String username, String password, String role, double balance) {
        File file = new File(DATABASE_DIR, username);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(username);
            writer.newLine();
            writer.write(password);
            writer.newLine();
            writer.write(role);
            writer.newLine();
            writer.write(String.valueOf(balance));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean login(String username, String password, String role) {
        return MANAGER_USERNAME.equals(username) && MANAGER_PASSWORD.equals(password) && MANAGER_ROLE.equals(role);
    }
}
