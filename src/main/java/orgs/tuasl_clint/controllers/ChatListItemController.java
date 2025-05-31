package orgs.clint_pages.controllers;


import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import orgs.clint_pages.utils.Navigation;

import java.io.IOException;

public class ChatListItemController {


    @FXML
    private HBox HboxAllProfile;

    @FXML
    private Label lastMessageLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private ImageView profilePictureImageView;

    @FXML
    private Label statusLabel;

    @FXML
    private Label timestampLabel;

    @FXML
    private Label unreadCountLabel;
    @FXML
    TextField txtin;
    private Stage stage;
    private Scene scene;
    private Parent root;


    @FXML
    void  GoToChat(MouseEvent event) throws IOException {
        Navigation.loadPage("chat.fxml");
    }

}
