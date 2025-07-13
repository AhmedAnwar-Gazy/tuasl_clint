package orgs.tuasl_clint.livecall;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class VideoCallWindowUDP {
    private final Stage stage;
    private final ImageView imageView;
    private final VideoSenderUDP sender = new VideoSenderUDP();
    private final VideoReceiverUDP receiver = new VideoReceiverUDP();

    public VideoCallWindowUDP(String title) {
        imageView = new ImageView();
        imageView.setFitWidth(320);
        imageView.setPreserveRatio(true);

        StackPane root = new StackPane(imageView);
        Scene scene = new Scene(root, 340, 260);

        stage = new Stage();
        stage.setTitle(title);
        stage.setScene(scene);
        stage.setOnCloseRequest(e -> stop());
        stage.show();
    }

    public void startSending(String remoteIP, int remotePort) {
        sender.start(remoteIP, remotePort);
    }

    public void startReceiving() {
        receiver.start(imageView);
    }

    public void stop() {
        sender.stop();
        receiver.stop();
        Platform.runLater(stage::close);
    }
}

