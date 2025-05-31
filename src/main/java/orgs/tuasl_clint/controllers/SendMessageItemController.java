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
import javafx.scene.input.MouseEvent; // استورد MouseEvent

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

    @FXML
    private HBox reactionsContainer; // أضف هذا المتغير لحاوية الرموز التعبيرية

    public void setUserData (Message message) {
        contentText.setText(message.getContent());
        contentText.setFont(new Font("Segoe UI Emoji", 12));
        senderLabel.setText(message.getSenderUserId().toString());
        timeLabel.setText(message.getSentAt().toString());
        // لا تقم بتعيين emojiLabel مباشرة هنا إذا كان دوره للتفاعل
        // emojiLabel.setText(message.getMessageType());

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

    // جديد: معالج حدث عند مرور الماوس فوق الرسالة
    @FXML
    private void handleMessageHoverEnter(MouseEvent event) {
        reactionsContainer.setVisible(true);
        reactionsContainer.setManaged(true);
    }

    // جديد: معالج حدث عند خروج الماوس من الرسالة
    @FXML
    private void handleMessageHoverExit(MouseEvent event) {
        reactionsContainer.setVisible(false);
        reactionsContainer.setManaged(false);
    }

    // جديد: معالج حدث عند النقر على أحد رموز التفاعل
    @FXML
    private void handleReaction(MouseEvent event) {
        // الحصول على الـ Label الذي تم النقر عليه
        Label clickedLabel = (Label) event.getSource();
        // الحصول على النص (الرمز التعبيري) من الـ Label
        String reactionEmoji = clickedLabel.getText();
        // تعيين الرمز التعبيري في الـ emojiLabel
        emojiLabel.setText(reactionEmoji);

        // اختياري: يمكنك إخفاء حاوية الرموز التعبيرية بعد اختيار رمز
        reactionsContainer.setVisible(false);
        reactionsContainer.setManaged(false);

        System.out.println("Reaction selected: " + reactionEmoji);
        // هنا يمكنك إضافة منطق لحفظ التفاعل في قاعدة البيانات أو إرساله عبر الشبكة
    }

    @FXML
    private void loadAudioMessages(String messageText) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/orgs/tuasl_clint/fxml/audioItem.fxml"));
            Parent mesiaCard = loader.load();
            AudioController audioController = loader.getController();
            mediaContainers.getChildren().add(mesiaCard);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load audioItem.fxml: " + e.getMessage());
        }
    }

    @FXML
    private void loadVideoMessages(String messageText) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/orgs/tuasl_clint/fxml/videoItem.fxml"));
            Parent mesiaCard = loader.load();
            VideoPlayerController videoPlayerController = loader.getController();
            mediaContainers.getChildren().add(mesiaCard);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load videoItem.fxml: " + e.getMessage());
        }
    }

    @FXML
    private void loadImageMessages(String messageText) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/orgs/tuasl_clint/fxml/imageItem.fxml"));
            Parent mesiaCard = loader.load();
            ImageMessageController imageMessageController = loader.getController();
            mediaContainers.getChildren().add(mesiaCard);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load imageItem.fxml: " + e.getMessage());
        }
    }
}