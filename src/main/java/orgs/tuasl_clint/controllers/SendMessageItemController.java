package orgs.tuasl_clint.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.SwipeEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import orgs.tuasl_clint.models2.FactoriesSQLite.MediaFactory;
import orgs.tuasl_clint.models2.FactoriesSQLite.UserFactory;
import orgs.tuasl_clint.models2.Media;
import orgs.tuasl_clint.models2.Message;
import orgs.tuasl_clint.models2.User;
import orgs.tuasl_clint.utils.FilesHelper;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

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

    private Message message;

    public void setUserData (Message message) {
        this.message = message;
        contentText.setText(message.getContent());
        contentText.setFont(new Font("Segoe UI Emoji", 12));
        if(message.getSenderName() == null || message.getSenderName().isEmpty()){
            User u= null;
            try {
                u = UserFactory.findById(message.getSenderUserId());
                if (u != null)
                    message.setSenderName(u.getFirstName());
            } catch (SQLException e) {
                message.setSenderName("UnKnown");
                System.err.println("Error getting the user for this message");
                e.printStackTrace();
            }
        }
        senderLabel.setText(String.valueOf(message.getSenderName()));

        timeLabel.setText( new SimpleDateFormat("HH:mm a").format(message.getSentAt()));
        emojiLabel.setText(message.getMessageType());


        //ENUM('text', 'image', 'video', 'voiceNote', 'file', 'system')


        switch (FilesHelper.getFileType(message.getMessageType())){
            case TEXT:
                break;
            case IMAGE:
                loadImageMessages(message);
                break;
            case VIDEO:
                loadVideoMessages(message);
                break;
            case AUDIO:
                loadAudioMessages(message);
                break;
            case FILE, STICKER:
                System.out.println("Handle file message");
                break;
            default:
                System.out.println("Unknown message type");
        }

    }







    @FXML
    private void loadAudioMessages(Message message) {
        this.message = message;
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
    private void loadVideoMessages(Message message) {
        this.message = message;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/orgs/tuasl_clint/fxml/videoItem.fxml"));
            //TODO: Error In Method getClass().getResource()  check the code and fix it.

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
    private void loadImageMessages(Message message) {
        try {
            // Create an FXMLLoader instance
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/orgs/tuasl_clint/fxml/imageItem.fxml"));

            // Load the FXML file. This returns the root node of UserCard.fxml.
            Parent mesiaCard = loader.load();

            // Get the controller for the loaded FXML (if you need to interact with it)
            ImageMessageController imageMessageController = loader.getController();
            Media m = MediaFactory.findById(message.getMediaId());
            if(m != null) {
                String imagePath = getClass().getResource("/orgs/tuasl_clint/images/") + m.getFileName();
                if(imagePath.contains("target/classes")) {
                    imagePath = imagePath.replaceAll("target/classes","src/main/resources");
                }
                imageMessageController.loadImage(imagePath);
                //imageMessageController.loadImage(m.getFilePathOrUrl() + m.getFileName() + '.' + m.getMimeType());
                System.out.println("Message id : "+message.getMessageId()+" with image media : "+imagePath);
            }
            else
                imageMessageController.loadImage(getClass().getResource("/orgs/tuasl_clint/images/R.png").toString());

            //messageScrollPane.setVvalue(1.0);

            mediaContainers.getChildren().add(mesiaCard);

        } catch (IOException e) {
            e.printStackTrace();
            // Handle the error, e.g., show an alert
            System.err.println("Failed to load UserCard.fxml: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Image not found for this message");
            e.printStackTrace();
        }
    }
    @FXML
    void handleReaction(MouseEvent event) {
        String reaction = ((Label) event.getSource()).getText();
        System.out.println("user clicked on emoji on a message : "+ reaction+ " with x : "+event.getX()+ " Y : "+ event.getY() + " ON MESSAGE ID : "+ this.getMessage().getMessageId());
    }

    @FXML
    private Label heartEmoji;
    @FXML
    private Label noActionEmoji;
    @FXML
    private Label okEmoji;
    @FXML
    private Label sadEmoji;
    @FXML
    private Label smileEmoji;


    @FXML
    private HBox reactionsContainer;

    public Message getMessage() {
        return message;
    }

    @FXML
    void handleMessageHoverEnter(MouseEvent event) {
        System.out.println("mouse enter message with x : "+ event.getX()+ " and y : "+ event.getY() + "messh : "+ VboxMessage.getHeight() + " sum... : " + + sumofChildsHeights(reactionsContainer.getChildren()) );
        if(VboxMessage.getHeight() <= sumofChildsHeights(reactionsContainer.getChildren()))
            reactionsContainer.setLayoutY(-1 * VboxMessage.getHeight() + 3);
        else if(VboxMessage.getHeight() > event.getY() + sumofChildsHeights(reactionsContainer.getChildren()))
            reactionsContainer.setLayoutY(( event.getY()-VboxMessage.getHeight()));
        else
            reactionsContainer.setLayoutY(1 - sumofChildsHeights(reactionsContainer.getChildren()));
        reactionsContainer.setLayoutX(VboxMessage.getWidth());
        this.reactionsContainer.setVisible(true);

    }
    double sumofChildsHeights(ObservableList<Node> n){
        double sum = 0;
        for(Node nn : n){
            sum += ((Label) nn).getHeight();
        }
        return  sum;
    }

    @FXML
    void handleMessageHoverExit(MouseEvent event) {
        System.out.println("mouse exit message");
//        if(message.getViewCount() == 0)
            this.reactionsContainer.setVisible(false);
    }

    @FXML
    void handleReactionMouseSwipLeft(SwipeEvent event) {
        System.out.println("mouse swipe icons right message");
        if(this.reactionsContainer.visibleProperty().get())
            this.reactionsContainer.setVisible(false);
    }


}
