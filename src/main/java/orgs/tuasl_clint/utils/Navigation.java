package orgs.tuasl_clint.utils;

import orgs.tuasl_clint.MainApp ;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class Navigation {

    private static Stage primaryStage;
    private static MainApp mainApp; // Reference to the main app if needed later

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static void setMainApp(MainApp app) {
        mainApp = app;
    }


    public static void loadPage(String fxmlFile) {
        if (primaryStage == null) {
            System.err.println("Primary stage is not set in Navigation!");
            return;
        }
        try {
            // Construct the full path relative to the resources folder
            String fxmlPath = "/orgs/tuasl_clint/fxml/" + fxmlFile;
            URL fxmlUrl = Navigation.class.getResource(fxmlPath);
            if (fxmlUrl == null) {
                System.err.println("Cannot find FXML file: " + fxmlPath);
                return;
            }
            System.out.println("Loading the page from : "+fxmlUrl.toString().toLowerCase());

            Parent root = FXMLLoader.load(Objects.requireNonNull(fxmlUrl));

            Scene scene = primaryStage.getScene();
            if (scene == null) {
                scene = new Scene(root);
                // Apply stylesheet globally when the first scene is created
                String cssPath = "/orgs/tuasl_clint/css/styles.css";
                URL cssUrl = Navigation.class.getResource(cssPath);
                if(cssUrl != null) {
                    scene.getStylesheets().add(cssUrl.toExternalForm());
                } else {
                    System.err.println("Cannot find CSS file: " + cssPath);
                }
                primaryStage.setScene(scene);
            } else {
                // Apply stylesheet if switching scenes (ensure it persists)
                String cssPath = "/orgs/tuasl_clint/css/styles.css";
                URL cssUrl = Navigation.class.getResource(cssPath);
                if(cssUrl != null && scene.getStylesheets().isEmpty()) {
                    scene.getStylesheets().add(cssUrl.toExternalForm());
                } else if (cssUrl == null && scene.getStylesheets().isEmpty()) {
                    System.err.println("Cannot find CSS file: " + cssPath);
                }
                scene.setRoot(root); // Change the content of the existing scene
            }
            primaryStage.sizeToScene(); // Adjust stage size if needed

        } catch (IOException e) {
            System.err.println("Error loading FXML file: " + fxmlFile);
            e.printStackTrace(); // Print stack trace for debugging
        } catch (NullPointerException e) {
            System.err.println("Null pointer exception likely due to missing FXML or CSS resource. Check paths.");
            e.printStackTrace();
        }
    }
}