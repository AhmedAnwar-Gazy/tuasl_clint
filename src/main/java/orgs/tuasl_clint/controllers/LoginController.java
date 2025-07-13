package orgs.tuasl_clint.controllers;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import orgs.tuasl_clint.models2.FactoriesSQLite.UserFactory;
import orgs.tuasl_clint.models2.User;
import orgs.tuasl_clint.models2.UserInfo;
import orgs.tuasl_clint.protocol.Response;
import orgs.tuasl_clint.utils.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javax.swing.*;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;       // Added FXML annotation
    @FXML private Button registerButton;    // Added FXML annotation
    @FXML private Label loginMessage;
    @FXML private CheckBox saveData;

    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(Timestamp.class, new TimestampAdapter())  // <-- Add this line
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .serializeNulls()
            .create();

    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        User u;
        if((u = this.login(usernameField.getText(),passwordField.getText())) != null) {
            Response serverLoginResponse = ChatClient2.getChatClient2().Login(usernameField.getText(), passwordField.getText());
            if (serverLoginResponse.isSuccess()) {
                User userFromServer = gson.fromJson(serverLoginResponse.getData(), User.class);
                if(areEqalsUsers(u,userFromServer)){
                    userFromServer.setUserId(u.getUserId());
                    User.user = userFromServer;
                    sucessLogin();
                }
                else{
                    DatabaseConnectionSQLite.DeleteData();
                }
            }else{
                this.loginMessage.setText(serverLoginResponse.getMessage());
            }
        }
    }
    private User login(String phone , String password){
        System.out.println("Login attempt with username: " + usernameField.getText());
        try(Connection conn = DatabaseConnectionSQLite.getInstance().getConnection()){
            String sql = "SELECT * FROM USERS WHERE ((username = ? or phone_number = ?) and hashed_password = ?);";
            PreparedStatement pr = conn.prepareStatement(sql);
            pr.setString(1, phone);//);
            pr.setString(2, phone);//usernameField.getText());
            pr.setString(3, password);//);
            ResultSet rs = pr.executeQuery();
            if(rs.next()){
                return UserFactory.createFromResultSet(rs);
            }
            else{
                System.err.println("\n\n--------------------cannot sign in using this account-----------------\n\n");
                loginMessage.setText("Error in username/phone number or password");
                return null;
            }
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }
    @FXML
    private void handleRegisterButtonAction(ActionEvent event) {
        Navigation.loadPage("registration.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UserInfo current_user = new UserInfo();
        try {
            if(current_user.getFirst()) {
                Response serverLoginResponse = ChatClient2.getChatClient2().Login(current_user.getPhone(), current_user.getPassword());
                if (serverLoginResponse.isSuccess()) {
                    User userFromServer = gson.fromJson(serverLoginResponse.getData(), User.class);
                    User userFromDatabase = login(current_user.getPhone(), current_user.getPassword());
                    if (userFromDatabase != null) {
                        if (areEqalsUsers(userFromDatabase,userFromServer)) {
                            User.user = userFromDatabase = userFromServer;
                        }
                    }
                    else {
                        User.user = userFromServer;
                    }
                    sucessLogin();
                }
            }
        } catch (SQLException e) {
            System.out.println("Cannot Sign in Automatically Error : "+ e.getMessage());
            e.printStackTrace();
        }
    }
    private void sucessLogin(){
        User.user.setOnline(true);
        try {
            User.user.update();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error occored While updating data in database ! Error Message : "+e.getMessage());
        }
        Navigation.loadPage("chat.fxml");
        System.out.println("Auto Sign in BY Username : " + User.user.getUsername() + ", Phone : " + User.user.getPhoneNumber() + ", password : " + User.user.getPassword());
    }
    private boolean areEqalsUsers(User userFromDatabase, User userFromServer) {
        return userFromDatabase.getUsername().equals(userFromServer.getUsername()) && userFromDatabase.getPhoneNumber().equals(userFromServer.getPhoneNumber()) && userFromDatabase.getPassword().equals(userFromServer.getPassword());
    }
}