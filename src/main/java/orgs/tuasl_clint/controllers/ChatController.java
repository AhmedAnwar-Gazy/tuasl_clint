package orgs.tuasl_clint.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
<<<<<<< HEAD
import javafx.scene.input.MouseEvent;
=======
import javafx.scene.layout.FlowPane;
>>>>>>> c6845a11b4cd526247a7f25cd146c527a535b34b
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import orgs.tuasl_clint.models2.*;
import orgs.tuasl_clint.utils.DatabaseConnection;
import orgs.tuasl_clint.utils.FilesHelper;
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
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.sound.sampled.*;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Button; // Import Button
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.ScrollPane;
public class ChatController {
    @FXML
    private BorderPane rootBorderPane;
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
    private FlowPane areaOfEmojis; //Added FXML annotation
    @FXML
    private Button audioCallButton;
    @FXML
    private Button menuButton;
    @FXML
    private Button videoCallButton;
    @FXML
    private Button cancel_message_media;
    @FXML
    private VBox main_message_input_container;
    @FXML
    private HBox message_media_selected_container;


    @FXML
    private HBox chatListItem;
<<<<<<< HEAD
=======
    @FXML
    private ScrollPane emojiScrollPane;
>>>>>>> c6845a11b4cd526247a7f25cd146c527a535b34b

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

                    loadMessages(message);
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


            loadMessages(new Message(messageText));


            // Clear the input field
            messageInputField.clear();


            // Scroll to bottom (might need slight delay depending on layout)
            messageScrollPane.setVvalue(1.0);
        }
    }

    @FXML
    private void loadMessages(Message messageText) {
        //private void loadMessages(String messageText) {
        try {
            // Create an FXMLLoader instance
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/orgs/tuasl_clint/fxml/sendMessageItem.fxml"));

            // Load the FXML file. This returns the root node of UserCard.fxml.
            Parent userCardNode = loader.load();

            // Get the controller for the loaded FXML (if you need to interact with it)
            SendMessageItemController sendMessageItemController = loader.getController();

            // Pass some data to the loaded controller
            userCardCount++;
            sendMessageItemController.setUserData(messageText);
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

    }

    @FXML
    public void handleSendVoiceButtonAction() {
        System.out.println("Send Voice Button Clicked!");
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




        private final String SHARE_FOLDER = "src/main/resources/orgs/tuasl_clint/file/";


        @FXML
        private void handleShareButtonAction(ActionEvent event) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select File to Share");

            // Optional: Set initial directory
            // You might want to save the last used directory or default to user's home
            // File initialDirectory = new File(System.getProperty("user.home"));
            // if (initialDirectory.exists()) {
            //     fileChooser.setInitialDirectory(initialDirectory);
            // }

            // Get the stage from the event source (the button)
            Stage stage = (Stage) shareButton.getScene().getWindow();

            // Show the open file dialog
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                try {
                    // Ensure the destination directory exists
                    Path destinationDirectory = Paths.get(FilesHelper.getFilePath(selectedFile));
                    if (!Files.exists(destinationDirectory)) {
                        Files.createDirectories(destinationDirectory);
                        System.out.println("Created directory: " + destinationDirectory.toAbsolutePath());
                    }

                    // Define the destination path for the selected file
                    Path destinationPath = destinationDirectory.resolve(selectedFile.getName());

                    // Copy the selected file to the destination directory
                    // StandardCopyOption.REPLACE_EXISTING will overwrite if a file with the same name already exists
                    Files.copy(selectedFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
                    String FileItem = FilesHelper.getMediaViewerPath(selectedFile);
                    //this.message_media_selected_container.setVisible(false);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/orgs/tuasl_clint/fxml/fileItem.fxml"));
                    Parent pp = loader.load();
                    FileItemController f = loader.getController();
                    f.setFileInfo(selectedFile);
                    this.message_media_selected_container.getChildren().add(0,pp);
//                    this.main_message_input_container.setPrefHeight(Region.USE_COMPUTED_SIZE);
//                    this.message_media_selected_container.setPrefHeight(Region.USE_COMPUTED_SIZE);
                    System.out.println("File copied successfully: " + selectedFile.getAbsolutePath() +
                            " to " + destinationPath.toAbsolutePath());

                    // Optional: You might want to send a message here indicating file shared,
                    // or update your UI to show the shared file.
                    // Example:
                    // Message sharedMessage = new Message("Shared file: " + selectedFile.getName(), "file", destinationPath.toString());
                    // loadMessages(sharedMessage); // Assuming you have a loadMessages method that handles file types

                } catch (IOException e) {
                    System.err.println("Error copying file: " + e.getMessage());
                    e.printStackTrace();
                    // Optionally show an error dialog to the user
                    // Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to share file: " + e.getMessage());
                    // alert.showAndWait();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } else {
                System.out.println("File selection cancelled.");
            }
        }

//    @FXML
//    void handle_unselect_message_media(ActionEvent event) {
//            if(fileItemController != null){
//                this.message_media_selected_container.getChildren().remove(fileItemController);
//                fileItemController = null;
//                this.message_media_selected_container.setVisible(false);
//            }
//
//    }


    @FXML
    private void handleEmojiButtonAction() {
        System.out.println("Emoji button clicked!");

        // تبديل حالة الظهور للـ ScrollPane
        boolean isVisible = emojiScrollPane.isVisible();
        emojiScrollPane.setVisible(!isVisible);
        emojiScrollPane.setManaged(!isVisible); // تبديل خاصية Managed

        // إذا أصبحت اللوحة مرئية، قم بتعبئة الإيموجي إذا كانت حاوية الإيموجي فارغة
        if (!isVisible) { // بمعنى إذا كانت ستصبح مرئية الآن
            if (areaOfEmojis.getChildren().isEmpty()) { // نتحقق من FlowPane الفعلي
                String[] emojis = {
                        "\uD83D\uDE00", "\uD83D\uDE01", "\uD83D\uDE02", "\uD83D\uDE03", "\uD83D\uDE04",
                        "\uD83D\uDE05", "\uD83D\uDE06", "\uD83D\uDE07", "\uD83D\uDE08", "\uD83D\uDE09",
                        "\uD83D\uDE0A", "\uD83D\uDE0B", "\uD83D\uDE0C", "\uD83D\uDE0D", "\uD83D\uDE0E",
                        "\uD83D\uDE0F", "\uD83D\uDE10", "\uD83D\uDE11", "\uD83D\uDE12", "\uD83D\uDE13",
                        "\uD83D\uDE14", "\uD83D\uDE15", "\uD83D\uDE16", "\uD83D\uDE17", "\uD83D\uDE18",
                        "\uD83D\uDE19", "\uD83D\uDE1A", "\uD83D\uDE1B", "\uD83D\uDE1C", "\uD83D\uDE1D",
                        "\uD83D\uDE1E", "\uD83D\uDE1F", "\uD83D\uDE20", "\uD83D\uDE21", "\uD83D\uDE22",
                        "\uD83D\uDE23", "\uD83D\uDE24", "\uD83D\uDE25", "\uD83D\uDE26", "\uD83D\uDE27",
                        "\uD83D\uDE28", "\uD83D\uDE29", "\uD83D\uDE2A", "\uD83D\uDE2B", "\uD83D\uDE2C",
                        "\uD83D\uDE2D", "\uD83D\uDE2E", "\uD83D\uDE2F", "\uD83D\uDE30", "\uD83D\uDE31",
                        "\uD83D\uDE32", "\uD83D\uDE33", "\uD83D\uDE34", "\uD83D\uDE35", "\uD83D\uDE36",
                        "\uD83D\uDE37", "\uD83E\uDD2A", "\uD83E\uDD2B", "\uD83E\uDD2C", "\uD83E\uDD2D",
                        "\uD83E\uDD2E", "\uD83E\uDD2F", "\uD83E\uDD70", "\uD83E\uDD71", "\uD83E\uDD72",
                        "\uD83E\uDD73", "\uD83E\uDD74", "\uD83E\uDD75", "\uD83E\uDD76", "\uD83E\uDD77",
                        "\uD83E\uDD78", "\uD83E\uDD7A", "\uD83E\uDD7B", "\uD83E\uDD7C", "\uD83E\uDD7D",
                        "\uD83E\uDD7E", "\uD83D\uDC4D", "\uD83D\uDC4F", "\uD83D\uDC4C", "\uD83D\uDC4A",
                        "\uD83D\uDC4B", "\uD83D\uDC4E", "\uD83D\uDE4B", "\u270C\uFE0F", "\uD83D\uDC50",
                        "\uD83D\uDE4C", "\u2764\uFE0F", "\uD83D\uDC99", "\uD83D\uDC9A", "\uD83D\uDC9B",
                        "\uD83D\uDC9C", "\uD83D\uDC9D", "\uD83D\uDC9E", "\uD83D\uDC9F", "\uD83D\uDCAF",
                        "\uD83D\uDCA3", "\uD83D\uDCA4", "\uD83D\uDCA6", "\uD83D\uDCA8", "\uD83D\uDCAB",
                        "\uD83D\uDCC8", "\uD83D\uDCC9", "\uD83D\uDCCC", "\uD83D\uDCCD", "\uD83D\uDCE0",
                        "\uD83D\uDCE1", "\uD83D\uDCE2", "\uD83D\uDCE3", "\uD83D\uDCE4", "\uD83D\uDCE5",
                        "\uD83D\uDCE6", "\uD83D\uDCE7", "\uD83D\uDCE8", "\uD83D\uDCE9", "\uD83D\uDCEA",
                        "\uD83D\uDCED", "\uD83D\uDCEF", "\uD83D\uDCF0", "\uD83D\uDCF1", "\uD83D\uDCF2",
                        "\uD83D\uDCF3", "\uD83D\uDCF4", "\uD83D\uDCF5", "\uD83D\uDCF6", "\uD83D\uDCF7",
                        "\uD83D\uDCF8", "\uD83D\uDCF9", "\uD83D\uDCFA", "\uD83D\uDCFB", "\uD83D\uDCFC",
                        "\uD83D\uDCFD", "\uD83D\uDCFE", "\uD83D\uDCFF", "\uD83D\uDD00", "\uD83D\uDD01",
                        "\uD83D\uDD02", "\uD83D\uDD03", "\uD83D\uDD04", "\uD83D\uDD05", "\uD83D\uDD06",
                        "\uD83D\uDD07", "\uD83D\uDD08", "\uD83D\uDD09", "\uD83D\uDD0A", "\uD83D\uDD0B",
                        "\uD83D\uDD0C", "\uD83D\uDD0D", "\uD83D\uDD0E", "\uD83D\uDD0F", "\uD83D\uDD10",
                        "\uD83D\uDD11", "\uD83D\uDD12", "\uD83D\uDD13", "\uD83D\uDD14", "\uD83D\uDD15",
                        "\uD83D\uDD16", "\uD83D\uDD17", "\uD83D\uDD18", "\uD83D\uDD19", "\uD83D\uDD1A",
                        "\uD83D\uDD1B", "\uD83D\uDD1C", "\uD83D\uDD1D", "\uD83D\uDD1E", "\uD83D\uDD1F",
                        "\uD83D\uDD20", "\uD83D\uDD21", "\uD83D\uDD22", "\uD83D\uDD23", "\uD83D\uDD24",
                        "\uD83D\uDD25", "\uD83D\uDD26", "\uD83D\uDD27", "\uD83D\uDD28", "\uD83D\uDD29",
                        "\uD83D\uDD2A", "\uD83D\uDD2B", "\uD83D\uDD2C", "\uD83D\uDD2D", "\uD83D\uDD2E",
                        "\uD83D\uDD2F", "\uD83D\uDD30", "\uD83D\uDD31", "\uD83D\uDD32", "\uD83D\uDD33",
                        "\uD83D\uDD34", "\uD83D\uDD35", "\uD83D\uDD36", "\uD83D\uDD37", "\uD83D\uDD38",
                        "\uD83D\uDD39", "\uD83D\uDD3A", "\uD83D\uDD3B", "\u2B1C", "\u2B1B",
                        "\u25FC", "\u25FB", "\u25FD", "\u25FE"
                };

<<<<<<< HEAD
            for (String emoji : emojis) {
                Label emojiLabel = new Label(emoji);
                emojiLabel.setStyle("-fx-font-size: 30px;");
                areaOfEmojis.getChildren().add(emojiLabel);
                emojiLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        messageInputField.setText(messageInputField.getText() + emoji);
                    }
                });
=======
                for (String emoji : emojis) {
                    Label emojiLabel = new Label(emoji);
                    emojiLabel.setStyle("-fx-font-size: 30px; -fx-padding: 5px;");
                    emojiLabel.setOnMouseClicked((MouseEvent e) -> {
                        messageInputField.appendText(emojiLabel.getText());
                        // عند اختيار إيموجي، قم بإخفاء الـ ScrollPane وإعادة منطقة المنتصف إلى التوسع
                        //emojiScrollPane.setVisible(false);
                        //emojiScrollPane.setManaged(false);
                        messageInputField.requestFocus();
                        e.consume();
                    });
                    areaOfEmojis.getChildren().add(emojiLabel);
                }
>>>>>>> c6845a11b4cd526247a7f25cd146c527a535b34b
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
    public void handleAudioCallButtonAction(ActionEvent event) {
        String selectedUser = chatListView.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            System.out.println("❌ لم يتم اختيار مستخدم لإجراء المكالمة");
            return;
        }

        try {

            Socket audioSocket = new Socket("localhost", 6001); // منفذ الصوت
            AudioCallWindow audioCallWindow = new AudioCallWindow("📞 مع " + selectedUser);
            AudioSender audioSender = new AudioSender();
            audioSender.start(audioSocket);

            AudioReceiver audioReceiver = new AudioReceiver();
            audioReceiver.start(audioSocket);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ فشل الاتصال بمكالمة الصوت");
        }

    }



    @FXML
    public void handleVideoCallButtonAction(ActionEvent event) {
            String selectedUser = chatListView.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            System.out.println("❌ لم يتم اختيار مستخدم لإجراء المكالمة");
            return;
        }

        try {
            Socket videoSocket = new Socket("localhost", 6000);
            Socket audioSocket = new Socket("localhost", 6001); // منفذ الصوت

            VideoCallWindow callWindow = new VideoCallWindow("📹 مكالمة فيديو مع " + selectedUser);
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




































