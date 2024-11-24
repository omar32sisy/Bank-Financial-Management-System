package bankaccountapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ManagerController {

    @FXML
    private ListView<String> customerList;

    @FXML
    private TextField newCustomerUsername;

    @FXML
    private TextField newCustomerPassword;

    @FXML
    public void initialize() {
        // Populate the customer list when the manager page is loaded
        refreshCustomerList();
    }

    @FXML
    public void handleAddCustomer(ActionEvent event) {
        String username = newCustomerUsername.getText();
        String password = newCustomerPassword.getText();
        
        if (!username.isEmpty() && !password.isEmpty()) {
            try {
                // Add customer using the Manager class
                Manager.addCustomer(username, password);
                
                // Add customer to the list view
                customerList.getItems().add(username);
                
                // Clear input fields
                newCustomerUsername.clear();
                newCustomerPassword.clear();
            } catch (Exception e) {
                showAlert("Error", "Failed to add customer. " + e.getMessage());
            }
        } else {
            showAlert("Error", "Username and password cannot be empty.");
        }
    }

    @FXML
    public void handleDeleteCustomer(ActionEvent event) {
        String selectedCustomer = customerList.getSelectionModel().getSelectedItem();
        
        if (selectedCustomer != null) {
            try {
                // Delete customer using the Manager class
                Manager.deleteCustomer(selectedCustomer);
                
                // Remove customer from the list view
                customerList.getItems().remove(selectedCustomer);
            } catch (Exception e) {
                showAlert("Error", "Failed to delete customer. " + e.getMessage());
            }
        } else {
            showAlert("Error", "No customer selected for deletion.");
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
            Stage currentStage = (Stage) customerList.getScene().getWindow();
            currentStage.setScene(loginScene);
            currentStage.show();
        } catch (Exception e) {
            showAlert("Error", "Failed to log out. " + e.getMessage());
        }
    }

    private void refreshCustomerList() {
        customerList.getItems().clear();
        for (Customer customer : Manager.getCustomers()) {
            customerList.getItems().add(customer.getUsername());
        }
    }

    private void showAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
