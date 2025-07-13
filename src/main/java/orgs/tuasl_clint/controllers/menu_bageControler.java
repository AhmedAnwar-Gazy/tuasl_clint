package orgs.tuasl_clint.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import orgs.tuasl_clint.utils.Navigation;

public class menu_bageControler {

    @FXML
    private Button backButton;

    @FXML
    private ListView<?> chatListView;

    @FXML
    private VBox sideMenuRoot;

    @FXML
    private Label userNameLabel;

    // إضافة حقل لواجهة إنشاء المجموعة
    @FXML
    private VBox createGroupPane;

    @FXML
    void handleBackButtonAction(ActionEvent event) {
        Navigation.loadPage("chat.fxml");
    }

    @FXML
    void handleLogoutAction(ActionEvent event) {
        Navigation.loadPage("registration.fxml");
    }

    @FXML
    void handleMyAccountAction(ActionEvent event) {
        // اضافة وظيفة حسب الحاجة
    }

    @FXML
    void handleNewContactAction(ActionEvent event) {
        // اضافة وظيفة حسب الحاجة
    }

    @FXML
    void handleNewGroupAction(ActionEvent event) {
        // إظهار واجهة إنشاء المجموعة وإخفاء قائمة الدردشة
        createGroupPane.setVisible(true);
        createGroupPane.setManaged(true);

        chatListView.setVisible(false);
        chatListView.setManaged(false);
    }

    @FXML
    void handleSettingsAction(ActionEvent event) {
        Navigation.loadPage("settings.fxml");
    }

    public void handleCreateGroup(ActionEvent event) {
    }

    public void handleCancelCreateGroup(ActionEvent event) {
        // إذا كنت تريد وظيفة للعودة من إنشاء مجموعة إلى القائمة
        if (createGroupPane.isVisible()) {
            createGroupPane.setVisible(false);
            createGroupPane.setManaged(false);
            chatListView.setVisible(true);
            chatListView.setManaged(true);
        }
    }
}
