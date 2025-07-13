package orgs.tuasl_clint.livecall;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class AudioCallWindow {
    private Stage stage;

    public AudioCallWindow(String title) {
        Platform.runLater(() -> {
            Label label = new Label("📞 مكالمة صوتية جارية...\n" + title);
            StackPane root = new StackPane(label);
            Scene scene = new Scene(root, 300, 150);
            stage = new Stage();
            stage.setTitle(title);
            stage.setScene(scene);
            stage.setOnCloseRequest(e -> stage.close()); // عند إغلاق النافذة
            stage.show();
        });
    }

    public void close() {
        Platform.runLater(() -> {
            if (stage != null) stage.close();
        });
    }
}

