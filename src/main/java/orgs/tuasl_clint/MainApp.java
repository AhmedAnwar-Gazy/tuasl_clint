package orgs.tuasl_clint;


import javafx.application.Application;
import javafx.stage.Stage;
import orgs.tuasl_clint.models2.User;
import orgs.tuasl_clint.utils.ChatClient2;
import orgs.tuasl_clint.utils.DatabaseConnectionSQLite;
import orgs.tuasl_clint.utils.Navigation;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


public class MainApp extends Application {

    private Stage primaryStage;
    @Override
    public void start(Stage stage) throws IOException {
        User u = ChatClient2.getChatClient2().Login("730673145","730673145");
        if(u != null){
            User.user = u;
            System.out.println("User signed in ID is : "+u.getUserId()+"    U_name  : "+u.getUsername());
        }else {
            System.err.println("\n\n--------------------cannot sign in using this account-----------------\n\n");
        }
        try(Connection conn = DatabaseConnectionSQLite.getInstance().getConnection()){
            System.out.println("Success Connect sqlite database");
        }catch (SQLException e){
            System.out.println("Connection Faild");
        }
        this.primaryStage = stage;
        this.primaryStage.setTitle("Chat Application");

        // Initialize Navigation helper
        Navigation.setMainApp(this); // Pass the instance to the helper
        Navigation.setPrimaryStage(primaryStage); // Pass the stage

        // Load the initial login screen
        Navigation.loadPage("chat.fxml");

        primaryStage.setMinWidth(800); // Minimum responsive width
        primaryStage.setMaxHeight(1200);
        primaryStage.setMinHeight(600); // Minimum responsive height
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}