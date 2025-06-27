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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/orgs/tuasl_clint/fxml/fileItem.fxml"));
            Parent mesiaCard = loader.load();
            // Get the controller for the loaded FXML (if you need to interact with it)
            FileItemController fileItemController = loader.getController();
            Media m = MediaFactory.findById(message.getMediaId());
            if(m != null){
                File f = new File(m.getFilePathOrUrl());
                if(f.exists()){
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

                }
            }
            //messageScrollPane.setVvalue(1.0);
            fileItemController.desableCloseButton();
            mediaContainers.getChildren().add(mesiaCard);

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
            System.err.println("Error : Failed to load UserCard.fxml: " + e.getMessage());
        }
    }


//
//    private void loadVideoMessages(Message message) {
//        this.message = message;
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/orgs/tuasl_clint/fxml/videoItem.fxml"));
//            //TODO: Error In Method getClass().getResource()  check the code and fix it.
//
//            // Load the FXML file. This returns the root node of UserCard.fxml.
//            Parent mesiaCard = loader.load();
//
//            // Get the controller for the loaded FXML (if you need to interact with it)
//            VideoPlayerController videoPlayerController = loader.getController();
//
//
//            //messageScrollPane.setVvalue(1.0);
//
//            mediaContainers.getChildren().add(mesiaCard);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            // Handle the error, e.g., show an alert
//            System.err.println("Error : Failed to load UserCard.fxml: " + e.getMessage());
//        }
//    }

    private void loadVideoMessages(Message message) {
        System.out.println("Loading Message With Video Item");
        this.message = message;
        try {
            // Load FXML
            URL fxmlUrl = getClass().getResource("/orgs/tuasl_clint/fxml/videoItem.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent videoItem = loader.load();
            // Get controller
//            FileItemController fileItemController = loader.getController();
//            fileItemController.desableCloseButton();
            // Load media
            VideoPlayerController controller = loader.getController();
            Media m = MediaFactory.findById(message.getMediaId());
            if (m != null) {
                    String mediaUri = "src/main/resources/orgs/tuasl_clint/videos/" + m.getFileName();
                    controller.setVideoFile(mediaUri);


//                    fileItemController.setFile(new File(m.getFilePathOrUrl()), new FileItemController.Action() {
//                        @Override
//                        public void OnActionDelete() {
//                            System.out.println("delete button of video item clicked");
//                        }
//                        @Override
//                        public void OnActionCleared() {
//                            System.out.println("file item of video is closed");
//                        }
//                        @Override
//                        public void OnClickItem() {
//                            System.out.println("Loading the video to new stage");
//                            try {
//                                URL fxmlUrl = getClass().getResource("/orgs/tuasl_clint/fxml/videoItem.fxml");
//                                String mediaUri = "src/main/resources/orgs/tuasl_clint/videos/" + m.getFileName();
//                                FXMLLoader loader1 = new FXMLLoader(Objects.requireNonNull(fxmlUrl));
//                                Parent root = loader1.load();
//                                Stage stage = new Stage();
//                                Scene scene = new Scene(root);
//                                String cssPath = "/orgs/tuasl_clint/css/styles.css";
//                                URL cssUrl = Navigation.class.getResource(cssPath);
//                                if(cssUrl != null) {
//                                    scene.getStylesheets().add(cssUrl.toExternalForm());
//                                } else {
//                                    System.err.println("Error : Cannot find CSS file: " + cssPath);
//                                }
//                                VideoPlayerController controller = loader1.getController();
//                                controller.setVideoFile(mediaUri);
//                                stage.setScene(scene);
//                                stage.show();
//                            } catch (IOException e) {
//                                System.out.println("Error while trying to open new stage for video");
//                                throw new RuntimeException(e);
//                            }
//                        }
//                    });
//                }else {
//                    System.err.println("Error : Cannot get the resource File of video");
//                }
            }else {
                System.err.println("Error : Cannot get the media from database");;
            }
            mediaContainers.getChildren().add(videoItem);
            System.out.println("complete loading the Message ....");

        } catch (Exception e) {
            showErrorAlert("Video Error", "Failed to load video: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadImageMessages(Message message) {
        ImageMessageController imageMessageController = null;
        System.out.println("loadImageMessage method in messages Controller : Loading the Image Message.....");
        try {
            // Create an FXMLLoader instance
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/orgs/tuasl_clint/fxml/imageItem.fxml"));

            // Load the FXML file. This returns the root node of UserCard.fxml.
            Parent mesiaCard = loader.load();

            // Get the controller for the loaded FXML (if you need to interact with it)
            imageMessageController = loader.getController();
            Media m = MediaFactory.findById(message.getMediaId());
            if(m != null) {
                String imagePath = "file:src/main/resources/orgs/tuasl_clint/images/" + m.getFileName();
                imageMessageController.loadImage(imagePath);
                System.out.println("Message id : "+message.getMessageId()+" with image media : "+imagePath);
            }
            else
                imageMessageController.loadImage(null);
            mediaContainers.getChildren().add(mesiaCard);

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
