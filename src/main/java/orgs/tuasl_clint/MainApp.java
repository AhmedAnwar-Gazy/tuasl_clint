package orgs.tuasl_clint;


import javafx.application.Application;
import javafx.stage.Stage;
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

        this.primaryStage = stage;
        this.primaryStage.setTitle("Chat Application");

        // Initialize Navigation helper
        Navigation.setMainApp(this); // Pass the instance to the helper
        Navigation.setPrimaryStage(primaryStage); // Pass the stage

        // Load the initial login screen
        Navigation.loadPage("sendMessageItem.fxml");

        primaryStage.setMinWidth(800); // Minimum responsive width
        primaryStage.setMaxHeight(1200);
        primaryStage.setMinHeight(600); // Minimum responsive height
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}