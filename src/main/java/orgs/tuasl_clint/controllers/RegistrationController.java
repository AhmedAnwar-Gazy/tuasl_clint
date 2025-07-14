package orgs.tuasl_clint.controllers;


import javafx.scene.control.*;
import orgs.tuasl_clint.models2.User;
import orgs.tuasl_clint.models2.UserInfo;
import orgs.tuasl_clint.protocol.Response;
import orgs.tuasl_clint.utils.ChatClient2;
import orgs.tuasl_clint.utils.Navigation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

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
    @FXML private CheckBox saveData;
    @FXML private TextField firstNameTF;
    @FXML private TextField lastNameTF;

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
        String firstName = firstNameTF.getText();
        String lastName = lastNameTF.getText();

        if(! password.equals(cpassword)){
            registerMessage.setText("Passwords are Difference");
            return;
        }else if(!isAlpha(username.charAt(0))){
            registerMessage.setText("UserName must Start With a Litter");
            return;
        } else if (username.contains(" ") || username.contains("\n") || username.isBlank() || username.isEmpty()) {
            registerMessage.setText("UserName mustn't be empty or has whitespace");
            return;
        } else if( isNum(phone.charAt(0)) || phone.charAt(0) == '+'){
            for(char p : phone.substring(1).toCharArray()){
                if(! isNum(p)){
                    registerMessage.setText("Invalid Phone Number");
                    return;
                }
            }
            try {
                Response res =  ChatClient2.getChatClient2().Register(phone,password,firstName,lastName);
                if(res != null && res.isSuccess()){
                    registerMessage.setText("Success Create an Account You Can now Sign in");
                    User u = new User(username,phone,password);
                    u.setFirstName(firstName);
                    u.setLastName(lastName);
                    if(u.save()){
                        System.out.println("success Save User account Locally");
                        User.user = u;
//                        System.out.println("Registration successful (placeholder)!");
                        Logger.getLogger(this.getClass().getName()).log(Level.WARNING,"Success, Now Please Login with your account");
                        Navigation.loadPage("login.fxml");
                        //Navigation.loadPage("chat.fxml");
                    }else {
                        registerMessage.setText("Unknown Error Occurred During Create the account..!!");
                        System.out.println("cannot create the account");
                    }
                    if(saveData.isSelected()){
                        try{
                            UserInfo.DeleteAll();
                        } catch (SQLException e) {
                            System.out.println("Cannot Delete All the data last from the table");
                        }
                        if(UserInfo.userInfo == null){
                            UserInfo.userInfo = new UserInfo(phone,password);
                            UserInfo.userInfo.setIsEnabled(1);
                            UserInfo.userInfo.setUser_id(1);
                        }else{
                            UserInfo.userInfo.setUser_id(1);
                            UserInfo.userInfo.setPhone(phone);
                            UserInfo.userInfo.setPassword(password);
                            UserInfo.userInfo.setIsEnabled(1);
                        }
                        if(UserInfo.userInfo.save()){
                            System.out.println("Login Data Saved Sucessfully");
                        }else {
                            System.out.println("--------------Cannot Save Login Data-");
                        }

                    }
                }else {
                    registerMessage.setText(res.getMessage());
                    Logger.getLogger(this.getClass().getName()).log(Level.WARNING,"Login Failed ...!! Error Message is : "+res.getMessage());
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