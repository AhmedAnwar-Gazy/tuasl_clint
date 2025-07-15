package orgs.tuasl_clint.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import orgs.tuasl_clint.models2.Chat;
import orgs.tuasl_clint.models2.ChatParticipant;
import orgs.tuasl_clint.models2.User;
import orgs.tuasl_clint.protocol.Command;
import orgs.tuasl_clint.protocol.Request;
import orgs.tuasl_clint.protocol.Response;
import orgs.tuasl_clint.utils.ChatClient2;
import orgs.tuasl_clint.utils.Navigation;

import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class menu_bageControler implements Initializable {
    public TextField groupDicription;
    public TextField publicLinkTF;
    @FXML
    private TextField personNumberField;

    @FXML
    private Button backButton;

    @FXML
    private ListView<?> chatListView;


    @FXML
    private ImageView profileImage;
    @FXML
    private VBox sideMenuRoot;

    @FXML
    private Label userNameLabel;

    // إضافة حقل لواجهة إنشاء المجموعة
    @FXML
    private VBox createGroupPane;
    private String groupTypeSelected;

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
    private ComboBox<String> groupType;

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
        if(this.onGoBackButtonClickListener != null){
            this.onGoBackButtonClickListener.onGoBackButtonClickListener();
        }
    }
    @FXML
    private TextField groupNameField ;
    private Chat.ChatType chatType1;
    public interface CreateGroupListiner{
        public void onCreateGroup(Chat chat , boolean isSaved);
    }
    private CreateGroupListiner createGroupListiner;
    public void setOnCreateGroupLisiner(CreateGroupListiner createGroupListiner){
        this.createGroupListiner = createGroupListiner;
    }
    public interface OnGoBackButtonClickListener{
        public void onGoBackButtonClickListener();
    }
    private OnGoBackButtonClickListener onGoBackButtonClickListener;

    public void setOnGoBackButtonClickListener(OnGoBackButtonClickListener onGoBackButtonClickListener) {
        this.onGoBackButtonClickListener = onGoBackButtonClickListener;
    }


    public void handleCreateGroup(ActionEvent event) {
        Map<String, Object> data = new HashMap<>();
        System.out.print("Enter chat type (private, group, channel): ");
        String chatType = groupTypeSelected;
        System.out.print("Enter chat name (optional for private, required for group/channel): ");
        String chatName = groupNameField.getText();
        System.out.print("Enter chat description (optional): ");
        String chatDescription = groupDicription.getText();
        System.out.print("Enter public link (optional, for public channels only): ");
        String publicLink = publicLinkTF.getText();

        data.put("chat_type", chatType);
        data.put("chat_name", chatName.isEmpty() ? null : chatName);
        data.put("chat_description", chatDescription.isEmpty() ? null : chatDescription);
        data.put("public_link", publicLink.isEmpty() ? null : publicLink);
        Request request = new Request(Command.CREATE_CHAT, data);
        Response response = ChatClient2.getChatClient2().sendRequestAndAwaitResponse(request);
        System.out.println( "Server Responsed By : "+ response.toString());



        Chat chat = new Chat(chatType1,new Timestamp(new Date().getTime()));
        chat.setPublicLink(publicLink);
        chat.setChatDescription(chatDescription);
        chat.setChatName(chatName);
        chat.setCreatorUserId(User.user.getId());
        boolean saved = false;
        try {
            saved = chat.save();
            ChatParticipant p = new ChatParticipant(chat.getChatId(),User.user.getId(),chat.getCreatedAt());
            System.out.println("Chat Participant is add to this chat and State of process is : "+ p.save());
        } catch (SQLException e) {
            System.out.println("-------from the menu bage : Cannot Save th Chat !! Error: "+e.getMessage());
        }

        if(this.createGroupListiner != null ){
            this.createGroupListiner.onCreateGroup(chat,saved);
        }


    }
    public void setImage(Image img) {
        if (img != null) {
            // Set the image
            profileImage.setImage(img);

        }
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setImage(new Image("C:\\Users\\alraw\\OneDrive\\الصور\\67f958d43a91c9.23223583.jpg"));
        groupTypeSelected = "private";
        chatType1 = Chat.ChatType.PRIVATE;
        groupType.getItems().addAll("private","group","channel");
        groupType.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                System.out.println("New selection: " + newValue);
                switch(newValue) {
                    case "group":
                        chatType1 = Chat.ChatType.GROUP;
                        break;
                    case "channel":
                        chatType1 = Chat.ChatType.CHANNEL;
                        break;
                    default:
                        chatType1 = Chat.ChatType.PRIVATE;
                }
                groupTypeSelected = newValue;
            }
        });


    }
}
