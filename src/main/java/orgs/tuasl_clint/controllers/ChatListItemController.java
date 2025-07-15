package orgs.tuasl_clint.controllers;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import orgs.tuasl_clint.models2.Chat;
import orgs.tuasl_clint.models2.Message;
import orgs.tuasl_clint.utils.TimeStampHelperClass;

import java.net.URL;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.ResourceBundle;

public class ChatListItemController implements Initializable {

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
    private StackPane unreadCountPane;

    private ObjectProperty<Chat> chatObjectProperty;

    public Chat getChat() {
        return chatObjectProperty.get();
    }

    public void setChat(Chat chat) {
        this.chatObjectProperty.set(chat);
        if (chat != null) {
            updateChatItemName(chat.getChatName());
            if(chat.getChatPictureUrl() != null && !(chat.getChatPictureUrl().isEmpty() || chat.getChatPictureUrl().isEmpty()))
                try {
                    updateProfilePicture(new Image(chat.getChatPictureUrl()));
                } catch (Exception e) {
                    System.out.println("an Error occured while trying to updata the chat Image");
                }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.chatObjectProperty = new SimpleObjectProperty<>();
//        this.statusLabel.setText("");
//        this.nameLabel.setText("Chat Name Label");
    }

    private interface OnImageItemClickedListener{
        public void onImageItemClicked(Chat chat,ChatListItemController controller);
    }
    private interface OnItemClickedListener{
        public void onItemClicked(Chat Chat,ChatListItemController controller);
    }

    private OnImageItemClickedListener onImageItemClickedListener;
    private OnItemClickedListener onItemClickedListener;

    public void setOnImageItemClickedListener(OnImageItemClickedListener onImageItemClickedListener) {
        this.onImageItemClickedListener = onImageItemClickedListener;
    }

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }

    public void updateChatItemName(String name){
        this.nameLabel.setText(name);
    }
    public void updateLastMessageContent(String content ){
        this.lastMessageLabel.setText(content);
    }
    // Make timestamp updates thread-safe
    public void updateLastMessageDate(Timestamp date) {
        javafx.application.Platform.runLater(() ->
                timestampLabel.setText(TimeStampHelperClass.formatTimeLeft(date))
        );
    }
    public void updateProfilePicture(Image image) {
        this.profilePictureImageView.setImage(image != null ? image : null);//getDefaultImage()); //TODO replace this code after fixing resources shared paths..
        makeCircularImage(profilePictureImageView);
    }

//    private Image getDefaultImage() {
//        return new Image(this.getClass().getResourceAsStream("src/main/resources/orgs/tuasl_clint/images/default-group-profile.jpg"));
//    }

    public void updateLastMessage(Message message) {
        if (message != null) {
            this.updateLastMessageContent(message.getContent());
            this.updateLastMessageDate(message.getSentAt());
        } else {
            this.updateLastMessageContent("");
            this.updateLastMessageDate(null); // Handle null in TimeStampHelperClass
        }
    }

    public void updateUnreadMessagesCount(long count) {
        boolean hasUnread = count > 0;
        this.unreadCountLabel.setText(hasUnread ? String.valueOf(count) : "");
        this.unreadCountPane.setVisible(hasUnread);
    }

    @FXML
    void handelGroupImageClicked(MouseEvent event) {
        System.out.println("Image Of Item Clicked Of Chat ID: "+((chatObjectProperty.get() != null)?this.chatObjectProperty.get().getChatId(): "null"));
        if (chatObjectProperty.get() != null && onImageItemClickedListener != null) {
            onImageItemClickedListener.onImageItemClicked(chatObjectProperty.get(), this);
        }
    }
    private void makeCircularImage(ImageView imageView) {
        Circle clip = new Circle(
                imageView.getFitWidth()/2,
                imageView.getFitHeight()/2,
                imageView.getFitWidth()/2
        );
        imageView.setClip(clip);
    }
    public ChatListItemController get_Controller(){
        return this;
    }
    public void cleanup() {
        this.onImageItemClickedListener = null;
        this.onItemClickedListener = null;
        if (profilePictureImageView != null) {
            profilePictureImageView.setImage(null);
        }
    }
    @FXML
    void handleGoToChat(MouseEvent event) {
        System.out.println("Item Clicked With Chat_ID: "+((chatObjectProperty.get() != null)?this.chatObjectProperty.get().getChatId(): "null"));
        if (chatObjectProperty.get() != null) {
            if (onItemClickedListener != null) {
                onItemClickedListener.onItemClicked(chatObjectProperty.get(), this);
            }
            this.unreadCountPane.setVisible(false);
        }
    }

}
