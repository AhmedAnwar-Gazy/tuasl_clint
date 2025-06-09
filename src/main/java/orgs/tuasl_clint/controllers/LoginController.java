package orgs.tuasl_clint.controllers;


import javafx.scene.control.Label;
import orgs.tuasl_clint.models2.FactoriesSQLite.UserFactory;
import orgs.tuasl_clint.models2.User;
import orgs.tuasl_clint.utils.DatabaseConnectionSQLite;
import orgs.tuasl_clint.utils.Navigation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button; // Import Button

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;       // Added FXML annotation
    @FXML private Button registerButton;    // Added FXML annotation
    @FXML private Label loginMessage;

    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        System.out.println("Login attempt with username: " + usernameField.getText());
        try(Connection conn = DatabaseConnectionSQLite.getInstance().getConnection()){
            String sql = "SELECT * FROM USERS WHERE ((username = ? or phone_number = ?) and hashed_password = ?);";
            PreparedStatement pr = conn.prepareStatement(sql);
            pr.setString(1,usernameField.getText());
            pr.setString(2,usernameField.getText());
            pr.setString(3,passwordField.getText());
            ResultSet rs = pr.executeQuery();
            if(rs.next()){
                User.user = UserFactory.createFromResultSet(rs);
                User.user.setOnline(true);
                User.user.update();
                Navigation.loadPage("chat.fxml");
            }
            else{
                loginMessage.setText("Error in username/phone number or password");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRegisterButtonAction(ActionEvent event) {
        Navigation.loadPage("registration.fxml");
    }

}