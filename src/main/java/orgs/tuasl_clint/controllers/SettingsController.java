package orgs.clint_pages.controllers;

import orgs.clint_pages.utils.Navigation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Button; // Import Button

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
        Navigation.loadPage("chat.fxml");
    }

    // Add handlers for other buttons (Change Password, Log Out) as needed
}