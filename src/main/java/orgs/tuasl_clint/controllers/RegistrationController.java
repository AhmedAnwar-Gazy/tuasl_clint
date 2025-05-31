package orgs.tuasl_clint.controllers;


import orgs.tuasl_clint.utils.Navigation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button; // Import Button


public class RegistrationController {

    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Button registerButton; // Added FXML annotation
    @FXML private Button backButton;     // Added FXML annotation


    @FXML
    private void handleRegisterButtonAction(ActionEvent event) {
        System.out.println("Registration attempt for: " + usernameField.getText());
        // Add validation and registration logic here
        // Check if passwords match, if username/email is valid, etc.
        // If successful registration:
        // Optionally show a success message
        System.out.println("Registration successful (placeholder)!");
        // Navigate back to login or directly to chat
        Navigation.loadPage("login.fxml");
        // Or Navigation.loadPage("chat.fxml");
    }

    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        Navigation.loadPage("login.fxml");
    }
}