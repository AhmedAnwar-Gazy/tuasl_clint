package orgs.clint_pages.controllers;


import orgs.clint_pages.utils.Navigation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button; // Import Button

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;       // Added FXML annotation
    @FXML private Button registerButton;    // Added FXML annotation

    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        System.out.println("Login attempt with username: " + usernameField.getText());
        // Add actual authentication logic here
        // If successful:
        Navigation.loadPage("chat.fxml");
    }

    @FXML
    private void handleRegisterButtonAction(ActionEvent event) {
        Navigation.loadPage("registration.fxml");
    }
}