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



    public void setUserData (String data1,String data2) {
        contentText.setText(data1);
        contentText.setFont(new Font("Segoe UI Emoji", 12));
        loadAudioMessages("hi");
        //statusLabel.setText(data1);
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

}
