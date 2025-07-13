package orgs.tuasl_clint.controllers;

import orgs.tuasl_clint.models2.User;
import orgs.tuasl_clint.utils.Navigation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Button; // Import Button

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SettingsController {

    @FXML private CheckBox enableNotificationsCheckbox;
    @FXML private CheckBox playSoundCheckbox;
    @FXML private Button backButton; // Added FXML annotation

    @FXML
    public void initialize() {
        // Load saved settings values here if applicable
        System.out.println("Settings page initialized.");
        // Example: load a saved setting
        // boolean notificationsEnabled = loadNotificationSetting(); // Placeholder
        // enableNotificationsCheckbox.setSelected(notificationsEnabled);
    }


    @FXML
    private void handleSaveSettingsAction(ActionEvent event) { // Assuming you add a Save button
        System.out.println("Saving settings...");
        boolean notifications = enableNotificationsCheckbox.isSelected();
        boolean sound = playSoundCheckbox.isSelected();
        // Add logic to save these settings (e.g., to a file or preferences)
        System.out.println("Notifications: " + notifications + ", Sound: " + sound);
        // Optionally show confirmation
    }


    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        // Optionally check if settings were changed and prompt to save
        Navigation.loadPage("menu_bage.fxml");
    }

    @FXML
    private void handleChangPasswordButton(ActionEvent event){
        //TODO: load the page of chang the password
    }
    @FXML
    private void handleLogoutButton(ActionEvent event){
        try {
            User.user.setOnline(false);
            if(User.user.update()){
                Navigation.loadPage("login.fxml");
            }else{
                Logger.getLogger(null).log(Level.WARNING,"Error occurred while trying to logout");
            }
        } catch (SQLException e) {
            Logger.getLogger(null).log(Level.WARNING,"Error occurred while trying to logout");
        }
    }

    // Add handlers for other buttons (Change Password, Log Out) as needed
}