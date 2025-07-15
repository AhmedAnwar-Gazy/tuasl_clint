package orgs.tuasl_clint.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.SVGPath;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class AudioController implements Initializable {

    @FXML
    private Button audioButon; // The transparent button for play/pause
    @FXML
    private Slider audioBar; // The progress slider
    @FXML
    private Label audioTime; // Label for current time and total duration
    @FXML
    private Label audioSize; // Label for audio file size
    @FXML
    private StackPane playButtonContainer; // The StackPane holding the circle and button, where we'll add icons

    private MediaPlayer mediaPlayer;
    private SVGPath playIcon;
    private SVGPath pauseIcon;
    private boolean isPlaying = false; // Tracks if audio is currently playing

    // --- IMPORTANT: Set this to the actual path of your audio file ---
    // For testing, place an audio.mp3 file in your project's root or a known path.
    // Example: "C:/Users/YourUser/Music/audio.mp3" or "src/main/resources/audio.mp3"
    private String defaultAudioFilePath = "src/main/resources/orgs/tuasl_clint/voiceNote/Clean_Code.mp3"; // Adjust this path!

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeIcons();
        if (playButtonContainer != null) {
            playButtonContainer.getChildren().add(playIcon);
        }
        audioButon.setOnAction(event -> togglePlayPause());
        loadAudioMedia(defaultAudioFilePath);
    }


    /**
     * Initializes the SVGPath icons for play and pause.
     * SVGPath is used for flexible vector shapes.
     */
    private void initializeIcons() {
        playIcon = new SVGPath();
        // SVG path for a standard play triangle. Adjust points if needed.
        // These points are chosen to be visually centered for a small button.
        playIcon.setContent("M -6 -8 L 12 0 L -6 8 Z"); // Triangle pointing right
        playIcon.setFill(javafx.scene.paint.Color.WHITE);
        playIcon.setMouseTransparent(true); // Allows clicks to pass through to the underlying Button

        pauseIcon = new SVGPath();
        // SVG path for a pause icon (two vertical bars). Adjust points if needed.
        pauseIcon.setContent("M -5 -8 H -1 V 8 H -5 Z M 1 8 H 5 V -8 H 1 Z"); // Two rectangles
        pauseIcon.setFill(javafx.scene.paint.Color.WHITE);
        pauseIcon.setMouseTransparent(true);
    }

    /**
     * Loads the audio media and sets up the MediaPlayer.
     * @param filePath The absolute or relative path to the audio file.
     */
    public void loadAudioMedia(String filePath) {
        // Clean up previous media player if exists
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
        }

        File audioFile = new File(filePath);
        if (!audioFile.exists() || !audioFile.isFile()) {
            System.err.println("Audio file not found or is not a file: " + filePath);
            audioButon.setDisable(true); // Disable button if file not found
            audioTime.setText("Error loading audio");
            audioSize.setText("");
            updatePlayPauseIcon(); // Ensure play icon is shown
            return;
        }

        try {
            Media audioMedia = new Media(audioFile.toURI().toString());
            mediaPlayer = new MediaPlayer(audioMedia);

            // Set file size label
            DecimalFormat df = new DecimalFormat("#.##"); // For 2 decimal places
            double fileSizeKB = (double) audioFile.length() / 1024.0;
            audioSize.setText(df.format(fileSizeKB) + " KB");

            // Listener for when media is ready (duration, etc. available)
            mediaPlayer.setOnReady(() -> {
                audioBar.setMin(0);
                audioBar.setMax(mediaPlayer.getMedia().getDuration().toSeconds());
                updateTimeLabel(Duration.ZERO, mediaPlayer.getMedia().getDuration());
                audioButon.setDisable(false); // Enable button once media is ready
            });

            // Listener for current time updates during playback
            mediaPlayer.currentTimeProperty().addListener((observable, oldTime, newTime) -> {
                if (!audioBar.isValueChanging()) { // Update slider only if user isn't dragging it
                    audioBar.setValue(newTime.toSeconds());
                }
                updateTimeLabel(newTime, mediaPlayer.getMedia().getDuration());
            });

            // Listener for slider value changes (user dragging the slider)
            audioBar.valueProperty().addListener((observable, oldValue, newValue) -> {
                if (audioBar.isValueChanging()) { // If user is actively dragging
                    mediaPlayer.seek(Duration.seconds(newValue.doubleValue()));
                }
            });

            // Listener for end of media
            mediaPlayer.setOnEndOfMedia(() -> {
                mediaPlayer.stop();
                isPlaying = false;
                mediaPlayer.seek(Duration.ZERO); // Reset to beginning
                updatePlayPauseIcon();
                updateTimeLabel(Duration.ZERO, mediaPlayer.getMedia().getDuration());
            });

            // Handle potential errors during media playback
            mediaPlayer.setOnError(() -> {
                System.err.println("MediaPlayer error: " + mediaPlayer.getError());
                audioButon.setDisable(true);
                isPlaying = false;
                updatePlayPauseIcon();
                audioTime.setText("Error");
            });

            // Initialize display
            audioTime.setText("00:00 / --:--"); // Show initial time
            audioButon.setDisable(true); // Disable until media is ready
            updatePlayPauseIcon(); // Ensure play icon is visible

        } catch (Exception e) {
            System.err.println("Failed to load audio media: " + e.getMessage());
            e.printStackTrace();
            audioButon.setDisable(true);
            audioTime.setText("Load Error");
        }
    }

    /**
     * Toggles play/pause state of the audio.
     */
    private void togglePlayPause() {
        if (mediaPlayer == null || mediaPlayer.getStatus() == MediaPlayer.Status.UNKNOWN || mediaPlayer.getStatus() == MediaPlayer.Status.HALTED) {
            System.out.println("Media player not ready or in error state.");
            return;
        }

        if (isPlaying) {
            mediaPlayer.pause();
        } else {
            mediaPlayer.play();
        }
        isPlaying = !isPlaying;
        updatePlayPauseIcon();
    }

    /**
     * Updates the play/pause icon displayed on the button.
     */
    private void updatePlayPauseIcon() {
        if (playButtonContainer == null) return;

        // Remove existing icons
        playButtonContainer.getChildren().remove(playIcon);
        playButtonContainer.getChildren().remove(pauseIcon);

        // Add the appropriate icon
        if (isPlaying) {
            playButtonContainer.getChildren().add(pauseIcon);
        } else {
            playButtonContainer.getChildren().add(playIcon);
        }
    }

    /**
     * Updates the audio time label with current and total duration.
     */
    private void updateTimeLabel(Duration currentTime, Duration totalDuration) {
        String current = formatDuration(currentTime);
        String total = formatDuration(totalDuration);
        audioTime.setText(current + " / " + total);
    }

    /**
     * Formats a Duration object into a "MM:SS" string.
     */
    private String formatDuration(Duration duration) {
        if (duration == null || duration.isUnknown() || duration.isIndefinite()) {
            return "--:--"; // Or "00:00" depending on preference
        }
        long minutes = (long) duration.toMinutes();
        long seconds = (long) (duration.toSeconds() % 60);
        return String.format("%02d:%02d", minutes, seconds);
    }

    /**
     * Call this method to dispose of the MediaPlayer when the scene or application is closing.
     * This releases system resources.
     */
    public void dispose() {
        if (mediaPlayer != null) {
            mediaPlayer.dispose();
            mediaPlayer = null;
        }
    }
}