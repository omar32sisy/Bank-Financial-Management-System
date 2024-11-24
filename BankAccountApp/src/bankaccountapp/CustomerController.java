package bankaccountapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.io.IOException;

public class CustomerController {

    @FXML
    private Label balanceLabel;

    @FXML
    private Label levelLabel;

    @FXML
    private TextField depositAmount;

    @FXML
    private TextField withdrawAmount;

    @FXML
    private TextField purchaseAmount;

    private Customer customer;

    @FXML
    public void initialize() {
        customer = LoginController.getCurrentCustomer();
        updateBalance();
        updateLevel();
    }

    private void updateBalance() {
        if (customer != null) {
            balanceLabel.setText("Balance: $" + customer.getBalance());
        }
    }

    private void updateLevel() {
        if (customer != null) {
            levelLabel.setText("Level: " + customer.getLevel());
        }
    }

    @FXML
    public void handleDeposit(ActionEvent event) {
        try {
            double amount = Double.parseDouble(depositAmount.getText());
            if (amount > 0) {
                customer.deposit(amount);
                updateBalance();
                updateLevel();
            } else {
                showAlert("Invalid Deposit", "Deposit amount must be greater than zero.");
            }
        } catch (NumberFormatException e) {
            showAlert("Invalid input", "Please enter a valid number for deposit.");
        }
    }

    @FXML
    public void handleWithdraw(ActionEvent event) {
        try {
            double amount = Double.parseDouble(withdrawAmount.getText());
            if (amount > 0 && customer.getBalance() >= amount) {
                customer.withdraw(amount);
                updateBalance();
                updateLevel();
            } else if (amount <= 0) {
                showAlert("Invalid Withdraw", "Withdraw amount must be greater than zero.");
            } else {
                showAlert("Insufficient funds", "You don't have enough balance to withdraw this amount.");
            }
        } catch (NumberFormatException e) {
            showAlert("Invalid input", "Please enter a valid number for withdraw.");
        }
    }

    @FXML
    public void handlePurchase(ActionEvent event) {
        try {
            double amount = Double.parseDouble(purchaseAmount.getText());
            if (amount >= 50) {
                double fee = customer.getLevel().getOnlineFee();
                if (customer.getBalance() >= amount + fee) {
                    customer.withdraw(amount + fee);
                    updateBalance();
                    updateLevel();
                } else {
                    showAlert("Insufficient funds", "You don't have enough balance to make this purchase.");
                }
            } else {
                showAlert("Invalid Purchase", "Purchase amount must be at least $50.");
            }
        } catch (NumberFormatException e) {
            showAlert("Invalid input", "Please enter a valid number for purchase.");
        }
    }

    @FXML
    public void handleLogout(ActionEvent event) {
        try {
            // Load the login scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent loginRoot = loader.load();
            Scene loginScene = new Scene(loginRoot);
            
            // Get the current stage and set the new scene
            Stage stage = (Stage) balanceLabel.getScene().getWindow();
            stage.setScene(loginScene);
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Failed to load login page.");
        }
    }

    @FXML
    public void handleShowBalance(ActionEvent event) {
        showAlert("Current Balance", "Your current balance is: $" + customer.getBalance());
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
