package orgs.tuasl_clint.controllers;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
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

    public Button login_using_data_last_btn;
    public VBox mainContainer;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Button registerButton;
    @FXML private Label loginMessage;
    @FXML private CheckBox saveData;

    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(Timestamp.class, new TimestampAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .serializeNulls()
            .create();

    @FXML
    private void handleLoginButtonAction(ActionEvent event) throws SQLException {
        User u;
        Response serverLoginResponse = ChatClient2.getChatClient2().Login(usernameField.getText(), passwordField.getText());
        User userFromServer = gson.fromJson(serverLoginResponse.getData(), User.class);
        if((u = this.login(usernameField.getText(),passwordField.getText())) != null) {
            if (userFromServer != null) {
                System.out.println("Server User : "+ userFromServer.toString());
                System.out.println("Database User : " + u.toString());
                if(areEqalsUsers(u,userFromServer)){
                    userFromServer.setId(u.getId());
                    User.user = userFromServer;
                    sucessLogin();
                }
                else{
                    //DatabaseConnectionSQLite.DeleteData();
                    sucessLogin();
                }
            }else{
                this.loginMessage.setText(serverLoginResponse.getMessage());
            }
        }else if(serverLoginResponse.isSuccess()){
            //DatabaseConnectionSQLite.DeleteData();
            try {
                userFromServer.save();
            } catch (SQLException e) {
                System.out.println("-------Cannot Save This User In Database ---------");
            }
            User.user = userFromServer;
            sucessLogin();
        }
    }
    private User login(String phone , String password){
        System.out.println("Login attempt with username: " + usernameField.getText());
        try(Connection conn = DatabaseConnectionSQLite.getInstance().getConnection()){
            String sql = "SELECT * FROM USERS WHERE ((username = ? or phone_number = ?) and hashed_password = ?);";
            PreparedStatement pr = conn.prepareStatement(sql);
            pr.setString(1, phone);
            pr.setString(2, phone);
            pr.setString(3, password);
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
        try{
            if(UserInfo.userInfo == null)
                UserInfo.userInfo = new UserInfo();
            if(UserInfo.userInfo.getFirst()){
                login_using_data_last_btn.setText("Login Using "+UserInfo.userInfo.getPhone()+" account");

                login_using_data_last_btn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        checkIfExist();
                    }
                });
            }else {
                mainContainer.getChildren().remove(login_using_data_last_btn);
            }
        } catch (SQLException e) {
        System.out.println("Cannot Sign in Automatically Error : "+ e.getMessage());
        e.printStackTrace();
    }

//        Platform.runLater(this::checkIfExist);
    }
    void checkIfExist(){
//        System.out.println("_____________ current User is : "+current_user.toString()+"___________________");
        if(UserInfo.userInfo != null && UserInfo.userInfo.getIsEnabled() >0) {
            System.out.println("_____________ current User is : "+UserInfo.userInfo.toString()+"___________________");
            Response serverLoginResponse = ChatClient2.getChatClient2().Login(UserInfo.userInfo.getPhone(), UserInfo.userInfo.getPassword());
            System.out.println("Trying to Auto Login. Response is : "+ serverLoginResponse.toString());
            if (serverLoginResponse.isSuccess()) {
                User userFromServer = gson.fromJson(serverLoginResponse.getData(), User.class);
                User userFromDatabase = login(UserInfo.userInfo.getPhone(), UserInfo.userInfo.getPassword());
                if (userFromDatabase != null) {
                    if (areEqalsUsers(userFromDatabase,userFromServer)) {
                        User.user = userFromServer;
                    }
                }
                else {
                    User.user = userFromServer;
                }
                sucessLogin();
            }
        }

    }
    private void sucessLogin(){
        int counter = 0;
        User.user.setOnline(true);
        try {
            if(!User.user.update())
                User.user.save();
            if(saveData.isSelected()){
                if(UserInfo.userInfo.getFirst()){
                    UserInfo.userInfo.setPhone(User.user.getPhoneNumber());
                    UserInfo.userInfo.setPassword(User.user.getPassword());
                    UserInfo.userInfo.setIsEnabled(1);
                }else{
                    UserInfo.userInfo.setPassword(User.user.getPassword());
                    UserInfo.userInfo.setPhone(User.user.getPhoneNumber());
                    UserInfo.userInfo.setIsEnabled(1);
                    UserInfo.userInfo.setUser_id(1);
                    if(UserInfo.userInfo.save()){
                        System.out.println("Login Data Saved Successfully in Database");
                        counter = 5;
                    }else {
                        System.err.println("-------Login Data Wasn't Be Saved in Database-----");
                    }
                }
                UserInfo.userInfo.setUser_id(1);
                for (counter = 0; counter < 4; counter++){
                    if(!UserInfo.userInfo.update()){
                        if(UserInfo.userInfo.save()){
                            if(UserInfo.userInfo.getUser_id() != 1) {
                                UserInfo.userInfo.setUser_id(1);
                                if(counter == 4)
                                    System.err.println("-------Login Data Wasn't Be Saved in Database-----");
                                continue;
                            }else {
                                break;
                            }
                        }
                    }else {
                        System.out.println("Login Data Saved Successfully in Database");
                        break;
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error occored While updating User data in database ! Error Message : "+e.getMessage());
        }
        Navigation.loadPage("chat.fxml");
        System.out.println("Auto Sign in BY Username : " + UserInfo.userInfo.toString());
    }
    private boolean areEqalsUsers(User userFromDatabase, User userFromServer) {
        return  userFromDatabase.getPhoneNumber().equals(userFromServer.getPhoneNumber()) && userFromDatabase.getPassword().equals(userFromServer.getPassword());
    }
}