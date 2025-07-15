package orgs.tuasl_clint.controllers;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.SwipeEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import orgs.tuasl_clint.models2.FactoriesSQLite.MediaFactory;
import orgs.tuasl_clint.models2.FactoriesSQLite.UserFactory;
import orgs.tuasl_clint.models2.Media;
import orgs.tuasl_clint.models2.Message;
import orgs.tuasl_clint.models2.User;
import orgs.tuasl_clint.utils.FilesHelper;
import orgs.tuasl_clint.utils.Navigation;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.attribute.FileTime;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Objects;

public class SendMessageItemController {

    @FXML
    private VBox VboxMessage;

    @FXML
    private Text contentText;

    @FXML
    private Label emojiLabel;

    @FXML
    private Label heartEmoji;

    @FXML
    private VBox mediaContainers;

    @FXML
    private Label noActionEmoji;

    @FXML
    private Label okEmoji;

    @FXML
    private Label optionsButton;

    @FXML
    private VBox reactionsContainer;

    @FXML
    private Label sadEmoji;

    @FXML
    private Label senderLabel;

    @FXML
    private Label smileEmoji;

    @FXML
    private Label statusLabel;

    @FXML
    private Label timeLabel;

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
                System.err.println("Error : Error getting the user for this message");
                e.printStackTrace();
            }
        }
        senderLabel.setText(String.valueOf(message.getSenderName()));

        timeLabel.setText( new SimpleDateFormat("HH:mm a").format(message.getSentAt()));
        emojiLabel.setText(message.getMessageType());


        //ENUM('text', 'image', 'video', 'voiceNote', 'file', 'system')


        switch (FilesHelper.toMediaType(message.getMessageType())){
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
                loadFileMessages(message);
                break;
            default:
                System.out.println("Unknown message type");
        }

    }

    private void loadFileMessages(Message message) {
        this.message = message;
        try {
            // Create an FXMLLoader instance
            Media m = MediaFactory.findById(message.getMediaId());
            // Get the controller for the loaded FXML (if you need to interact with it)
            if(m != null){
                File f = new File(m.getFilePathOrUrl());
                if(f.exists() && f.isFile()){
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/orgs/tuasl_clint/fxml/fileItem.fxml"));
                    FileItemController fileItemController = loader.getController();
                    Parent mesiaCard = loader.load();
                    fileItemController.setFile(f, new FileItemController.Action() {
                        @Override
                        public void OnActionDelete() {

                        }

                        @Override
                        public void OnActionCleared() {

                        }

                        @Override
                        public void OnClickItem() {
                            try {
                                if(Desktop.isDesktopSupported())
                                    Desktop.getDesktop().open(f);
                            } catch (IOException e) {
                                System.out.println("Cannot Open The File Error : "+ e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    });
                    fileItemController.desableCloseButton();
                    mediaContainers.getChildren().add(mesiaCard);
                }else {
                    System.out.println("\n\n------------Media File Is not Fount , Loading File Item Loader........");
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/orgs/tuasl_clint/fxml/loadItem.fxml"));
                    Parent loadCard = loader.load();
                    LoadItemController loadItemController = loader.getController();
                    loadItemController.setMedia(m);
                    mediaContainers.getChildren().add(loadCard);
                    loadItemController.setOnReadyItemListener(new LoadItemController.OnReadyItemListener() {
                        @Override
                        public void onReadyItem(HBox fileItemContainer) {
                            loadFileMessages(message);
                        }
                    });
                }
            }
            //messageScrollPane.setVvalue(1.0);


        } catch (IOException e) {
            e.printStackTrace();
            // Handle the error, e.g., show an alert
            System.err.println("Error : Failed to load UserCard.fxml: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error in sendMessageController in method of files messages while getting the media from database");
            e.printStackTrace();
        }
    }


    private void loadAudioMessages(Message message) {
        this.message = message;
        try {
            Media media = MediaFactory.findById(message.getMediaId());
            if(media != null){
                File f = new File(media.getFilePathOrUrl());
                if(f.exists() && f.isFile()){
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/orgs/tuasl_clint/fxml/audioItem.fxml"));
                    Parent mesiaCard = loader.load();
                    AudioController audioController = loader.getController();
                    audioController.loadAudioMedia(media.getFilePathOrUrl());
                    mediaContainers.getChildren().add(mesiaCard);

                }else {
                    System.out.println("\n\n------------Media File Is not Fount , Loading File Item Loader........");
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/orgs/tuasl_clint/fxml/loadItem.fxml"));
                    Parent loadCard = loader.load();
                    LoadItemController loadItemController = loader.getController();
                    loadItemController.setMedia(media);
                    mediaContainers.getChildren().add(loadCard);
                    loadItemController.setOnReadyItemListener(new LoadItemController.OnReadyItemListener() {
                        @Override
                        public void onReadyItem(HBox fileItemContainer) {
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/orgs/tuasl_clint/fxml/audioItem.fxml"));
                                Parent mesiaCard = loader.load();
                                AudioController audioController = loader.getController();
                                audioController.loadAudioMedia(media.getFilePathOrUrl());
                                fileItemContainer.getChildren().add(mesiaCard);
                            } catch (IOException e) {
                                System.out.println("Fail to Load the media downloaded");
                            }
                        }
                    });
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the error, e.g., show an alert
            System.err.println("Error : Failed to load UserCard.fxml: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Cannot Get The Audio Media Of Id : "+message.getMediaId()+" With Message ID: "+message.getMessageId());
        }
    }

    private void loadVideoMessages(Message message) {
        System.out.println("Loading Message With Video Item");
        this.message = message;
        try {
            // Load FXML
            Media m = MediaFactory.findById(message.getMediaId());
            if (m != null) {
                File mediaFile = new File(m.getFilePathOrUrl());
                if(mediaFile.exists() && mediaFile.isFile()){
                    URL fxmlUrl = getClass().getResource("/orgs/tuasl_clint/fxml/videoItem.fxml");
                    FXMLLoader loader = new FXMLLoader(fxmlUrl);
                    Parent videoItem = loader.load();
                    VideoPlayerController controller = loader.getController();
                    String mediaUri = "src/main/resources/orgs/tuasl_clint/videos/" + m.getFileName();
                    controller.setVideoFile(mediaUri);
                    mediaContainers.getChildren().add(videoItem);
                    System.out.println("complete loading the Message ....");
                }
                else {
                    System.out.println("\n\n------------Media File Is not Fount , Loading File Item Loader........");
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/orgs/tuasl_clint/fxml/loadItem.fxml"));
                    LoadItemController loadItemController = loader.getController();
                    Parent loadCard = loader.load();
                    loadItemController.setMedia(m);
                    mediaContainers.getChildren().add(loadCard);
                    loadItemController.setOnReadyItemListener(new LoadItemController.OnReadyItemListener() {
                        @Override
                        public void onReadyItem(HBox fileItemContainer) {
                            try {
                                URL fxmlUrl = getClass().getResource("/orgs/tuasl_clint/fxml/videoItem.fxml");
                                FXMLLoader loader = new FXMLLoader(fxmlUrl);
                                Parent videoItem = loader.load();
                                VideoPlayerController controller = loader.getController();
                                String mediaUri = "src/main/resources/orgs/tuasl_clint/videos/" + m.getFileName();
                                controller.setVideoFile(mediaUri);
                                fileItemContainer.getChildren().add(videoItem);
                            } catch (IOException e) {
                                System.out.println("Fail to Load the media downloaded");
                            }
                        }
                    });
                }
            }else {
                System.err.println("Error : Cannot get the media from database");
            }

        } catch (Exception e) {
            showErrorAlert("Video Error", "Failed to load video: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadImageMessages(Message message) {
        System.out.println("loadImageMessage method in messages Controller : Loading the Image Message.....");
        try {
            Media m = MediaFactory.findById(message.getMediaId());
            if(m != null) {
                File imageFile = new File(String.valueOf(getClass().getResource("orgs/tuasl_clint/images/" + m.getFileName())));
                if(imageFile.exists() && imageFile.isFile()){
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/orgs/tuasl_clint/fxml/imageItem.fxml"));
                    Parent mesiaCard = loader.load();
                    ImageMessageController imageMessageController = loader.getController();
                    imageMessageController.loadImage(imageFile.getPath());
                    System.out.println("Message id : "+message.getMessageId()+" with image media : " + imageFile.getPath());
                    mediaContainers.getChildren().add(mesiaCard);
                }else {
                    System.out.println("\n\n------------Media File Is not Fount , Loading File Item Loader........");
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/orgs/tuasl_clint/fxml/loadItem.fxml"));
                    Parent loadCard = loader.load();
                    LoadItemController loadItemController = loader.getController();
                    loadItemController.setMedia(m);
                    mediaContainers.getChildren().add(loadCard);
                    loadItemController.setOnReadyItemListener(new LoadItemController.OnReadyItemListener() {
                        @Override
                        public void onReadyItem(HBox fileItemContainer) {
                            try {
                                URL fxmlUrl = getClass().getResource("/orgs/tuasl_clint/fxml/imageItem.fxml");
                                FXMLLoader loader = new FXMLLoader(fxmlUrl);
                                Parent videoItem = loader.load();
                                ImageMessageController controller = loader.getController();
                                URL mediaUri = getClass().getResource("/orgs/tuasl_clint/videos/" + m.getFileName());
                                if(mediaUri != null){
                                    controller.loadImage(mediaUri.toString());
                                    fileItemContainer.getChildren().add(videoItem);
                                    mediaContainers.getChildren().add(videoItem);
                                }
                            } catch (IOException e) {
                                System.out.println("Fail to Load the media downloaded");
                            }
                        }
                    });
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            // Handle the error, e.g., show an alert
            System.err.println("Error : Failed to load UserCard.fxml: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error : Image not found for this message");
            e.printStackTrace();
        }
    }
    private void showErrorAlert(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
    @FXML
    void handleReaction(MouseEvent event) {
        String reaction = ((Label) event.getSource()).getText();
        System.out.println("user clicked on emoji on a message : "+ reaction+ " with x : "+event.getX()+ " Y : "+ event.getY() + " ON MESSAGE ID : "+ this.getMessage().getMessageId());
        // عرض الإيموجي في الـ emojiLabel
        emojiLabel.setText(reaction);
        emojiLabel.setVisible(true);
        emojiLabel.setManaged(true);

        // يمكنك حفظ هذا التفاعل في كائن Message أو قاعدة البيانات لاحقًا
        message.setMessageType(reaction);
    }

    public Message getMessage() {
        return message;
    }

    @FXML
    void handleMessageHoverEnter(MouseEvent event) {
//        System.out.println("mouse enter message with x : "+ event.getX()+ " and y : "+ event.getY() + "messh : "+ VboxMessage.getHeight() + " sum... : " + + sumofChildsHeights(reactionsContainer.getChildren()) );
//        if(VboxMessage.getHeight() <= sumofChildsHeights(reactionsContainer.getChildren()))
//            reactionsContainer.setLayoutY(-1 * VboxMessage.getHeight() + 3);
//        else if(VboxMessage.getHeight() > event.getY() + sumofChildsHeights(reactionsContainer.getChildren()))
//            reactionsContainer.setLayoutY(( event.getY()-VboxMessage.getHeight()));
//        else
//            reactionsContainer.setLayoutY(1 - sumofChildsHeights(reactionsContainer.getChildren()));
//        reactionsContainer.setLayoutX(VboxMessage.getWidth());
        this.reactionsContainer.setVisible(true);

    }


    @FXML
    void handleMessageHoverExit(MouseEvent event) {
//        System.out.println("mouse exit message");
//        if(message.getViewCount() == 0)
        this.reactionsContainer.setVisible(false);
    }


}
