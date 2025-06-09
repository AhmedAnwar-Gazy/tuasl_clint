package orgs.tuasl_clint.controllers;


import javafx.scene.control.Label;
import orgs.tuasl_clint.models2.User;
import orgs.tuasl_clint.utils.Navigation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button; // Import Button

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class RegistrationController {

    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Button registerButton; // Added FXML annotation
    @FXML private Button backButton;     // Added FXML annotation
    @FXML private Label registerMessage;
    //handleRegisterButtonAction

    @FXML
    private void handleRegisterButtonAction(ActionEvent event) {
        System.out.println("Registration attempt for: " + usernameField.getText());
        registerMessage.setText("registering");
        // Add validation and registration logic here
        String username = usernameField.getText().trim();
        String phone = emailField.getText().trim();
        String password = passwordField.getText();
        String cpassword = confirmPasswordField.getText();

        if(! password.equals(cpassword)){
            registerMessage.setText("Passwords are Difference");
            return;
        }else if(!isAlpha(username.charAt(0))){
            registerMessage.setText("UserName must Start With a Litter");
            return;
        } else if (username.contains(" ") || username.contains("\n")) {
            registerMessage.setText("UserName mustn't has whitespace");
            return;
        } else if( isNum(phone.charAt(0)) || phone.charAt(0) == '+'){
            for(char p : phone.substring(1).toCharArray()){
                if(! isNum(p)){
                    registerMessage.setText("Invalid Phone Number");
                    return;
                }
            }
            User u = new User(username,phone,password);
            try {
                if(u.save()){
                    System.out.println("success create account");
                    User.user = u;
                    System.out.println("Registration successful (placeholder)!");
                    Logger.getLogger(this.getClass().getName()).log(Level.WARNING,"Success, Now Please Login with your account");
                    Navigation.loadPage("login.fxml");
                    //Navigation.loadPage("chat.fxml");
                }else {
                    registerMessage.setText("Unknown Error Occurred During Create the account..!!");
                    System.out.println("cannot create the account");
                }
            } catch (SQLException e) {
                System.out.println("Error register for "+ username+ "Error Message : "+ e.getMessage());
                Logger.getLogger(this.getClass().getName()).log(Level.WARNING,"Error : "+e.getMessage());
            }
        }else {
            registerMessage.setText("Invalid Phone Number");
            return;
        }
        // Check if passwords match, if username/email is valid, etc.
        // If successful registration:
        // Optionally show a success message
        // Or Navigation.loadPage("chat.fxml");
    }

    private boolean isNum(char c) {
        return (c >= '0' && c <= '9');
    }

    private boolean isAlpha(char c) {
        return ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'));
    }

    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        Navigation.loadPage("login.fxml");
    }
}