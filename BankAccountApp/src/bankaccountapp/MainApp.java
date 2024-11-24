package bankaccountapp;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp {

    public void showCustomerPage(Customer customer) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomerPage.fxml"));
        Parent root = loader.load();
        
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
