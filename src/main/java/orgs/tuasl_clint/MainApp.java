package orgs.tuasl_clint;


import com.google.gson.Gson;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Pair;
import orgs.tuasl_clint.models2.User;
import orgs.tuasl_clint.protocol.Response;
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
        Navigation.loadPage("chatListItem.fxml");

        primaryStage.setMinWidth(800); // Minimum responsive width
        primaryStage.setMaxHeight(1200);
        primaryStage.setMinHeight(600); // Minimum responsive height
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}