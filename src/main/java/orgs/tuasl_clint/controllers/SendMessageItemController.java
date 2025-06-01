package orgs.tuasl_clint.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import orgs.tuasl_clint.models2.Message;

import java.io.IOException;

public class SendMessageItemController {


    @FXML
    private VBox VboxMessage;

    @FXML
    private Text contentText;

    @FXML
    private VBox mediaContainers;

    @FXML
    private Label senderLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private Label timeLabel;

    @FXML
    private Label emojiLabel;

    public void setUserData (Message message) {
        contentText.setText(message.getContent());
        contentText.setFont(new Font("Segoe UI Emoji", 12));
        senderLabel.setText(message.getSenderUserId().toString());
        timeLabel.setText(message.getSentAt().toString());
        emojiLabel.setText(message.getMessageType());


        //ENUM('text', 'image', 'video', 'voiceNote', 'file', 'system')


        switch (message.getMessageType()) {
            case "text":
                break;
            case "image":
                loadImageMessages("hi");
                break;
            case "video":
                loadVideoMessages("hi");
                break;
            case "voiceNote":
                loadAudioMessages("hi");
                break;
            case "file":
                System.out.println("Handle file message");
                break;
            default:
                System.out.println("Unknown message type");
        }

    }







    @FXML
    private void loadAudioMessages(String messageText) {
        try {
            // Create an FXMLLoader instance
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/orgs/tuasl_clint/fxml/audioItem.fxml"));

            // Load the FXML file. This returns the root node of UserCard.fxml.
            Parent mesiaCard = loader.load();

            // Get the controller for the loaded FXML (if you need to interact with it)
            AudioController audioController = loader.getController();


            //messageScrollPane.setVvalue(1.0);

            mediaContainers.getChildren().add(mesiaCard);

        } catch (IOException e) {
            e.printStackTrace();
            // Handle the error, e.g., show an alert
            System.err.println("Failed to load UserCard.fxml: " + e.getMessage());
        }
    }


    @FXML
    private void loadVideoMessages(String messageText) {
        try {
            // Create an FXMLLoader instance
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/orgs/tuasl_clint/fxml/videoItem.fxml"));

            // Load the FXML file. This returns the root node of UserCard.fxml.
            Parent mesiaCard = loader.load();

            // Get the controller for the loaded FXML (if you need to interact with it)
            VideoPlayerController videoPlayerController = loader.getController();


            //messageScrollPane.setVvalue(1.0);

            mediaContainers.getChildren().add(mesiaCard);

        } catch (IOException e) {
            e.printStackTrace();
            // Handle the error, e.g., show an alert
            System.err.println("Failed to load UserCard.fxml: " + e.getMessage());
        }
    }

    @FXML
    private void loadImageMessages(String messageText) {
        try {
            // Create an FXMLLoader instance
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/orgs/tuasl_clint/fxml/imageItem.fxml"));

            // Load the FXML file. This returns the root node of UserCard.fxml.
            Parent mesiaCard = loader.load();

            // Get the controller for the loaded FXML (if you need to interact with it)
            ImageMessageController imageMessageController = loader.getController();


            //messageScrollPane.setVvalue(1.0);

            mediaContainers.getChildren().add(mesiaCard);

        } catch (IOException e) {
            e.printStackTrace();
            // Handle the error, e.g., show an alert
            System.err.println("Failed to load UserCard.fxml: " + e.getMessage());
        }
    }


}
