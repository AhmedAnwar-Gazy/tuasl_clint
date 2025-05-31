package orgs.tuasl_clint.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import orgs.tuasl_clint.models2.*;
import orgs.tuasl_clint.utils.DatabaseConnection;
import orgs.tuasl_clint.utils.Navigation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.animation.ScaleTransition;
//import javafx.scene.media.AudioRecorder;
import javafx.util.Duration;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.sound.sampled.*;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Button; // Import Button
import javafx.scene.layout.VBox;

public class ChatController {

    @FXML
    private ListView<String> chatListView;
    @FXML
    private Label chatTitleLabel;
    @FXML
    private ScrollPane messageScrollPane;
    @FXML
    private VBox messageDisplayArea;
    @FXML
    private TextField messageInputField;
    @FXML
    private Button settingsButton; // Added FXML annotation
    @FXML
    private Button sendButton; // Added FXML annotation
    @FXML
    private Button sendVoiceButton; //Added FXML annotation
    @FXML
    private Button emojiButton; //Added FXML annotation
    @FXML
    private Button shareButton; //Added FXML annotation
    @FXML
    private VBox areaOfEmojis; //Added FXML annotation
    @FXML
    private Button audioCallButton;
    @FXML
    private Button menuButton;
    @FXML
    private Button videoCallButton;



    @FXML
    void handleAudioCallButtonAction(ActionEvent event) {

    }

    @FXML
    void handleEmojiButtonAction(ActionEvent event) {

    }



    @FXML
    private HBox chatListItem;


    private int userCardCount = 0;

    private boolean isRecording = false;
    private TargetDataLine line;

    // Define the folder where recordings should be saved
    private final String RECORDING_FOLDER = "src/main/resources/orgs/tuasl_clint/voiceNote/";


    // Dummy data for chat list
    private ObservableList<String> chatItems = FXCollections.observableArrayList();
    private ObservableList<Chat> chatItemsChats = FXCollections.observableArrayList();
    // Dummy data for messages list
    private ObservableList<String> messageItems = FXCollections.observableArrayList();
    private ObservableList<Message> messageItemsMessage = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Populate the chat list


        loadMyChats();


//        chatListView.setItems(chatItems);
        chatListView.setItems(chatItems);

        // Add listener to chat list selection
        chatListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            System.out.println();
            if (newSelection != null) {

                chatTitleLabel.setText(newSelection);
                // Clear previous messages and load messages for the selected chat
                messageItems.clear();
                messageItemsMessage.clear();

                messageDisplayArea.getChildren().clear();

                // Add placeholder message loading logic
                messageDisplayArea.getChildren().add(new Label("Messages for " + newSelection + " would load here."));

                loadChatsMessages(newSelection);

                // --- NEW CODE: Populate messageDisplayArea directly after loading messages ---
                for (Message message : messageItemsMessage) {

                    loadMessages(message.getContent());
                    //messageDisplayArea.getChildren().add(new Label(message.getContent()));
                }
                // --- END NEW CODE ---


                //System.out.println(messageItems);

                System.out.println("Selected chat: " + newSelection);


            }
        });

        // Select the first chat by default
        if (!chatItems.isEmpty()) {
            chatListView.getSelectionModel().selectFirst();
        }

        messageInputField.setFont(Font.font("Segoe UI Emoji"));
        // Ensure the scroll pane scrolls to the bottom when new messages are added (basic setup)
        messageDisplayArea.heightProperty().addListener(observable -> messageScrollPane.setVvalue(1D));
    }

    @FXML
    private void handleSettingsButtonAction(ActionEvent event) {
        Navigation.loadPage("settings.fxml");
    }

    @FXML
    private void handleSendButtonAction(ActionEvent event) {
        String messageText = messageInputField.getText().trim();
        if (!messageText.isEmpty()) {
            System.out.println("Sending message: " + messageText);


            loadMessages(messageText);


            // Clear the input field
            messageInputField.clear();


            // Scroll to bottom (might need slight delay depending on layout)
            messageScrollPane.setVvalue(1.0);
        }
    }

    @FXML
    private void loadMessages(String messageText) {
        try {
            // Create an FXMLLoader instance
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/orgs/tuasl_clint/fxml/sendMessageItem.fxml"));

            // Load the FXML file. This returns the root node of UserCard.fxml.
            Parent userCardNode = loader.load();

            // Get the controller for the loaded FXML (if you need to interact with it)
            SendMessageItemController sendMessageItemController = loader.getController();

            // Pass some data to the loaded controller
            userCardCount++;
            sendMessageItemController.setUserData(messageText, "user" + userCardCount + "@example.com");
            messageScrollPane.setVvalue(1.0);
            // Add the loaded FXML's root node to the VBox
            messageDisplayArea.getChildren().add(userCardNode);

        } catch (IOException e) {
            e.printStackTrace();
            // Handle the error, e.g., show an alert
            System.err.println("Failed to load UserCard.fxml: " + e.getMessage());
        }
    }


    @FXML
    private void handleSendVoiceButtonAction1(ActionEvent event) {
        System.out.println("Send Voice Button Clicked!");
        // You can add additional logic if needed
    }

    @FXML
    public void handleSendVoiceButtonAction() {
        sendVoiceButton.setOnAction(this::handleSendVoiceButtonAction1);
        sendVoiceButton.setOnMousePressed(event -> {
            animateButton(true); // Start animation
            startRecording(); // Begin recording
        });

        sendVoiceButton.setOnMouseReleased(event -> {
            stopRecording(); // Stop recording when released
            animateButton(false); // Reset animation
        });
    }

    private void animateButton(boolean isClicked) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), sendVoiceButton);
        if (isClicked) {
            scaleTransition.setToX(1.2); // Expand the button
            scaleTransition.setToY(1.2);
        } else {
            scaleTransition.setToX(1.0); // Return to normal size
            scaleTransition.setToY(1.0);
        }
        scaleTransition.play();
    }

    private void startRecording() {
        try {
            // Ensure the directory exists
            File folder = new File(RECORDING_FOLDER);
            if (!folder.exists()) {
                folder.mkdirs(); // Create directory if it doesn’t exist
            }

            // Generate a unique filename based on the current timestamp
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File audioFile = new File(RECORDING_FOLDER, "recording_" + timeStamp + ".wav");

            AudioFormat format = new AudioFormat(16000, 8, 2, true, true);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

            if (!AudioSystem.isLineSupported(info)) {
                System.err.println("Line not supported");
                return;
            }

            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();
            isRecording = true;

            System.out.println("Recording started: " + audioFile.getAbsolutePath());

            Thread recordingThread = new Thread(() -> {
                try (AudioInputStream audioStream = new AudioInputStream(line)) {
                    AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, audioFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            recordingThread.start();

        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void stopRecording() {
        if (isRecording && line != null) {
            line.stop();
            line.close();
            isRecording = false;
            System.out.println("Recording stopped and saved.");
        }
    }


    @FXML
    private void handleMenuButtonAction(ActionEvent event) {

    }

    @FXML
    private void handleShareButtonAction(ActionEvent event) {

    }

    @FXML
    private void handleEmojiButtonAction() {
        // Toggle visibility: if visible, hide it; if hidden, show it
        boolean isVisible = areaOfEmojis.isVisible();
        areaOfEmojis.setVisible(!isVisible);

        // Only populate emojis when making it visible
        if (!isVisible) {
            areaOfEmojis.getChildren().clear();

            String[] emojis = {
                    "\uD83D\uDE00", "\uD83D\uDE01", "\uD83D\uDE02", "\uD83D\uDE03", "\uD83D\uDE04"
            };

            for (String emoji : emojis) {
                Label emojiLabel = new Label(emoji);
                emojiLabel.setStyle("-fx-font-size: 30px;");
                areaOfEmojis.getChildren().add(emojiLabel);
            }
        }
    }

    public void loadMyChats() {
        try {
            Connection con = DatabaseConnection.getConnection();

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT chats.* FROM users LEFT JOIN chat_participants on users.id = chat_participants.user_id LEFT JOIN chats on chat_participants.chat_id = chats.id WHERE users.id = 2");


            while (rs.next()) {
                chatItems.add(rs.getString("chat_name"));
                Chat chat = ChatFactory.createChatFromResultSet(rs);
                chatItemsChats.add(chat);
                System.out.println(chat.getChatName());
            }

        } catch (SQLException e) {
            System.err.println("An error occurred during database operations: " + e.getMessage());
        }
    }

    public void loadChatsMessages(String chatname) {
        try {
            Connection con = DatabaseConnection.getConnection();
            System.out.println("colam :" + chatname);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT messages.* ,media.* FROM chats left join messages on chats.id = messages.chat_id LEFT JOIN media on messages.media_id =   media.id  WHERE chats.chat_name =  \"" + chatname + "\" ORDER BY messages.id  ASC; ");


            while (rs.next()) {
                messageItems.add(rs.getString(4));
                Message message = MessageFactory.createMessageFromResultSet(rs);
                messageItemsMessage.add(message);
            }

        } catch (SQLException e) {
            System.err.println("An error occurred during database operations: " + e.getMessage());
        }
    }


    @FXML
    public void handleSendVoiceButtonAction(ActionEvent event) {

    }



    @FXML
    public void handleVideoCallButtonAction(ActionEvent event) {
        String selected = chatListView.getSelectionModel().getSelectedItem();
        System.out.println("Currently selected: " + selected);

        //chatListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {


        if (selected == null) {
            System.out.println("❌ لم يتم اختيار مستخدم لإجراء المكالمة");
            return;
        }

        try {
            Socket videoSocket = new Socket("localhost", 6000);
            Socket audioSocket = new Socket("localhost", 6001); // منفذ الصوت

            VideoCallWindow callWindow = new VideoCallWindow("📹 مكالمة فيديو مع " + selected);
            callWindow.startSending(videoSocket);
            callWindow.startReceiving(videoSocket);

            AudioSender audioSender = new AudioSender();
            audioSender.start(audioSocket);

            AudioReceiver audioReceiver = new AudioReceiver();
            audioReceiver.start(audioSocket);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ فشل الاتصال بمكالمة الفيديو والصوت");
        }

    }


}




































