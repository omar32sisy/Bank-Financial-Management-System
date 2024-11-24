package bankaccountapp;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.Initializable;

public class LoginController implements Initializable {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ComboBox<String> userTypeComboBox;

    private static Customer currentCustomer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        userTypeComboBox.setItems(FXCollections.observableArrayList("Manager", "Customer"));
        currentCustomer=null;
    }

    @FXML
    public void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String userType = userTypeComboBox.getValue();

        if (userType == null) {
            showAlert("Error", "Please select a user type.");
            return;
        }

        if ("Manager".equals(userType)) {
            if (Manager.login(username, password, "Manager")) {
                try {
                    // Load Manager Page
                    Parent root = FXMLLoader.load(getClass().getResource("ManagerPage.fxml"));
                    Stage stage = (Stage) usernameField.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException e) {
                    showAlert("Error", "Failed to load Manager page.");
                }
            } else {
                showAlert("Login Error", "Invalid Manager credentials.");
            }
        } else if ("Customer".equals(userType)) {
            try {
                currentCustomer = new Customer(username);
                if (currentCustomer.login(username, password, "Customer")) {
                    try {
                        // Load Customer Page
                        Parent root = FXMLLoader.load(getClass().getResource("CustomerPage.fxml"));
                        Stage stage = (Stage) usernameField.getScene().getWindow();
                        stage.setScene(new Scene(root));
                        stage.show();
                    } catch (IOException e) {
                        showAlert("Error", "Failed to load Customer page.");
                    }
                } else {
                    showAlert("Login Error", "Wrong username or password.");
                }
            } catch (IllegalArgumentException e) {
                showAlert("Error", "Wrong username or password.");
            }
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static Customer getCurrentCustomer() {
        return currentCustomer;
    }
}
