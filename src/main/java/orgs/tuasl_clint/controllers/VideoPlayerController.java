package orgs.tuasl_clint.controllers;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.jar.JarOutputStream;

public class VideoPlayerController implements Initializable {

    @FXML
    private StackPane videoPlayerContainer; // Root container for mouse events and fullscreen
    @FXML
    private MediaView mediaView;
    @FXML
    private VBox controlsOverlay; // The bar at the bottom
    @FXML
    private Slider progressBar;
    @FXML
    private StackPane playPauseButtonContainer; // Small button container
    @FXML
    private Button playPauseButton; // Small play/pause button
    @FXML
    private Label currentTimeLabel;
    @FXML
    private Label totalTimeLabel;
    @FXML
    private Slider volumeSlider;
    @FXML
    private Button fullscreenButton;
    @FXML
    private StackPane centerPlayButtonContainer; // Large central button container
    @FXML
    private Button centerPlayButton; // Large central play button

    // Icons
    private SVGPath playIconSmall;
    private SVGPath pauseIconSmall;
    private SVGPath playIconLarge;

    private MediaPlayer mediaPlayer;
    private BooleanProperty isPlaying = new SimpleBooleanProperty(false);
    private BooleanProperty isFullscreen = new SimpleBooleanProperty(false);
    private boolean isSeeking = false; // Flag to prevent slider from updating during user drag

    // --- IMPORTANT: Set this to the actual path of your video file ---
    // For testing, place a video file (e.g., sample.mp4) in your project's resources folder.
    private String defaultVideoFilePath = "src/main/resources/orgs/tuasl_clint/videos/goBack.mp4"; // Adjust this path!

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("initialize");
        // 1. Initialize Icons
        initializeIcons();

        // Add initial play icon to small button container
        if (playPauseButtonContainer != null) {
            playPauseButtonContainer.getChildren().add(playIconSmall);
        }

        // Add initial play icon to large center button container
        if (centerPlayButtonContainer != null) {
            centerPlayButtonContainer.getChildren().add(playIconLarge);
        }

        // 2. Load Video Media
        loadVideoMedia(defaultVideoFilePath);

        // 3. Set up Button Actions
        playPauseButton.setOnAction(event -> togglePlayPause());
        centerPlayButton.setOnAction(event -> togglePlayPause());
        fullscreenButton.setOnAction(event -> toggleFullscreen());

        // 4. Volume Slider Binding
        volumeSlider.setValue(100); // Default to max volume
        if (mediaPlayer != null) {
            mediaPlayer.volumeProperty().bind(volumeSlider.valueProperty().divide(100));
        }

        // 5. Hide/Show Controls on Mouse Hover
        setupHoverEffects();

        // 6. Fullscreen Listener (for stage)
        Platform.runLater(() -> {
            Stage stage = (Stage) mediaView.getScene().getWindow();
            if (stage != null) {
                stage.fullScreenProperty().addListener((obs, oldVal, newVal) -> {
                    isFullscreen.set(newVal);
                    // Adjust MediaView size when fullscreen changes
                    if (newVal) {
                        mediaView.fitWidthProperty().bind(stage.widthProperty());
                        mediaView.fitHeightProperty().bind(stage.heightProperty());
                    } else {
                        // Unbind and revert to FXML defined size or default
                        mediaView.fitWidthProperty().unbind();
                        mediaView.fitHeightProperty().unbind();
                        mediaView.setFitWidth(600); // Or whatever your FXML default is
                        mediaView.setFitHeight(400); // Or whatever your FXML default is
                    }
                });

                // Escape key listener for fullscreen exit
                stage.getScene().setOnKeyPressed(event -> {
                    if (event.getCode() == KeyCode.ESCAPE && stage.isFullScreen()) {
                        stage.setFullScreen(false);
                    }
                });
            }
        });

        // Initial state of controls (hidden)
        controlsOverlay.setOpacity(0);
        centerPlayButtonContainer.setVisible(true);
        centerPlayButtonContainer.setManaged(true);
    }

    /**
     * Initializes the SVGPath icons for play and pause.
     */
    private void initializeIcons() {
        System.out.println("initializeIcons");
        // Small play icon for control bar
        playIconSmall = new SVGPath();
        playIconSmall.setContent("M -5 -6 L 10 0 L -5 6 Z"); // Small triangle
        playIconSmall.setFill(javafx.scene.paint.Color.WHITE);
        playIconSmall.setMouseTransparent(true);

        // Small pause icon for control bar
        pauseIconSmall = new SVGPath();
        pauseIconSmall.setContent("M -4 -6 H -1 V 6 H -4 Z M 1 6 H 4 V -6 H 1 Z"); // Two small rectangles
        pauseIconSmall.setFill(javafx.scene.paint.Color.WHITE);
        pauseIconSmall.setMouseTransparent(true);

        // Large play icon for center button
        playIconLarge = new SVGPath();
        playIconLarge.setContent("M -10 -15 L 20 0 L -10 15 Z"); // Larger triangle
        playIconLarge.setFill(javafx.scene.paint.Color.WHITE);
        playIconLarge.setMouseTransparent(true);
    }

    /**
     * Loads the video media and sets up the MediaPlayer.
     * @param filePath The absolute or relative path to the video file.
     */
    public void loadVideoMedia(String filePath) {
        System.out.println("loadVideoMedia(String : "+filePath+")");
        // Clean up previous media player if exists
        if (mediaPlayer != null) {
            System.out.println("if (mediaPlayer != null) {");
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
        }

        File videoFile = new File(filePath);
        if (!videoFile.exists() || !videoFile.isFile()) {
            System.out.println("if (!videoFile.exists() || !videoFile.isFile()) {");
            System.err.println("Video file not found or is not a file: " + filePath);
            playPauseButton.setDisable(true);
            centerPlayButton.setDisable(true);
            totalTimeLabel.setText("Error");
            currentTimeLabel.setText("Error");
            return;
        }

        try {
            Media videoMedia = new Media(videoFile.toURI().toString());
            mediaPlayer = new MediaPlayer(videoMedia);
            mediaView.setMediaPlayer(mediaPlayer);

            // Bind MediaView size to parent or other properties (e.g., stage size for fullscreen)
            // Initial binding to fill the container, can be adjusted for aspect ratio
            mediaView.fitWidthProperty().bind(videoPlayerContainer.widthProperty());
            mediaView.fitHeightProperty().bind(videoPlayerContainer.heightProperty());
            mediaView.setPreserveRatio(true); // Maintain aspect ratio

            // Bind volume slider
            mediaPlayer.volumeProperty().bind(volumeSlider.valueProperty().divide(100.0));

            // Set up MediaPlayer listeners
            mediaPlayer.setOnReady(() -> {
                System.out.println("mediaPlayer.setOnReady(() -> {");
                progressBar.setMin(0);
                progressBar.setMax(mediaPlayer.getMedia().getDuration().toSeconds());
                totalTimeLabel.setText(formatDuration(mediaPlayer.getMedia().getDuration()));
                currentTimeLabel.setText(formatDuration(Duration.ZERO));
                playPauseButton.setDisable(false);
                centerPlayButton.setDisable(false);

                // Initialize control bar state (hide initially, show on hover)
                controlsOverlay.setOpacity(0);
                centerPlayButtonContainer.setVisible(true); // Show large play button
                centerPlayButtonContainer.setManaged(true);
            });

            mediaPlayer.currentTimeProperty().addListener((observable, oldTime, newTime) -> {
                System.out.println("mediaPlayer.currentTimeProperty().addListener((observable, oldTime, newTime) -> {");
                if (!isSeeking) { // Only update slider if user isn't dragging it
                    progressBar.setValue(newTime.toSeconds());
                }
                currentTimeLabel.setText(formatDuration(newTime));
            });

            progressBar.valueProperty().addListener((observable, oldValue, newValue) -> {
                System.out.println("progressBar.valueProperty().addListener((observable, oldValue, newValue) -> {");
                if (progressBar.isValueChanging()) {
                    isSeeking = true;
                    mediaPlayer.seek(Duration.seconds(newValue.doubleValue()));
                }
            });

            progressBar.setOnMouseReleased(event -> {
                System.out.println("progressBar.setOnMouseReleased(event -> {");
                isSeeking = false;
                mediaPlayer.seek(Duration.seconds(progressBar.getValue()));
                if (!isPlaying.get()) { // If paused, ensure frame updates
                    mediaPlayer.play();
                    mediaPlayer.pause();
                }
            });

            mediaPlayer.setOnEndOfMedia(() -> {
                System.out.println("mediaPlayer.setOnEndOfMedia(() -> {");
                mediaPlayer.stop();
//                isPlaying.set(false);
                mediaPlayer.seek(Duration.ZERO); // Reset to beginning
                updatePlayPauseIcons();
                centerPlayButtonContainer.setVisible(true); // Show large play button
                centerPlayButtonContainer.setManaged(true);
                controlsOverlay.setOpacity(1.0); // Show controls at end of media
            });

            mediaPlayer.setOnError(() -> {
                System.out.println("mediaPlayer.setOnError(() -> {");
                System.err.println("MediaPlayer error: " + mediaPlayer.getError());
                playPauseButton.setDisable(true);
                centerPlayButton.setDisable(true);
//                isPlaying.set(false);
                updatePlayPauseIcons();
                currentTimeLabel.setText("Error");
            });

            // Bind isPlaying property to MediaPlayer status for convenience
            isPlaying.bind(mediaPlayer.statusProperty().isEqualTo(MediaPlayer.Status.PLAYING));
            isPlaying.addListener((obs, oldVal, newVal) -> updatePlayPauseIcons());

        } catch (Exception e) {
            System.err.println("Failed to load video media: " + e.getMessage());
            e.printStackTrace();
            playPauseButton.setDisable(true);
            centerPlayButton.setDisable(true);
        }
    }

    /**
     * Toggles play/pause state of the video.
     */
    private void togglePlayPause() {
        System.out.println("togglePlayPause");
        if (mediaPlayer == null || mediaPlayer.getStatus() == MediaPlayer.Status.UNKNOWN || mediaPlayer.getStatus() == MediaPlayer.Status.HALTED) {
            System.out.println("Media player not ready or in error state.");
            return;
        }

        if (isPlaying.get()) {
            mediaPlayer.pause();
        } else {
            mediaPlayer.play();
        }
        // isPlaying property is now bound, so it will update automatically
    }

    /**
     * Updates the play/pause icons based on the isPlaying state.
     */
    private void updatePlayPauseIcons() {
        System.out.println("updatePlayPauseIcons");
        // Small button icon
        if (playPauseButtonContainer != null) {
            playPauseButtonContainer.getChildren().remove(playIconSmall);
            playPauseButtonContainer.getChildren().remove(pauseIconSmall);
            if (isPlaying.get()) {
                playPauseButtonContainer.getChildren().add(pauseIconSmall);
            } else {
                playPauseButtonContainer.getChildren().add(playIconSmall);
            }
        }

        // Large center button visibility
        if (centerPlayButtonContainer != null) {
            centerPlayButtonContainer.setVisible(!isPlaying.get());
            centerPlayButtonContainer.setManaged(!isPlaying.get()); // Also manage space
        }
    }

    /**
     * Formats a Duration object into an "MM:SS" string.
     */
    private String formatDuration(Duration duration) {
        System.out.println("formatDuration");
        if (duration == null || duration.isUnknown() || duration.isIndefinite()) {
            return "--:--";
        }
        long minutes = (long) duration.toMinutes();
        long seconds = (long) (duration.toSeconds() % 60);
        return String.format("%02d:%02d", minutes, seconds);
    }

    /**
     * Sets up mouse hover effects for the controls overlay.
     */
    private void setupHoverEffects() {
        System.out.println("setupHoverEffects");
        // Show controls on mouse enter, hide on mouse exit
        videoPlayerContainer.setOnMouseEntered(event -> {
            System.out.println("videoPlayerContainer.setOnMouseEntered(event -> {");
            FadeTransition ft = new FadeTransition(Duration.millis(300), controlsOverlay);
            ft.setToValue(1.0);
            ft.play();
        });

        videoPlayerContainer.setOnMouseExited(event -> {
            System.out.println("videoPlayerContainer.setOnMouseExited(event -> {");
            if (isPlaying.get()) { // Only hide if playing
                FadeTransition ft = new FadeTransition(Duration.millis(300), controlsOverlay);
                ft.setToValue(0.0);
                ft.play();
            }
        });
    }

    /**
     * Toggles fullscreen mode for the stage.
     */
    private void toggleFullscreen() {
        System.out.println("toggleFullscreen");
        Stage stage = (Stage) mediaView.getScene().getWindow();
        if (stage != null) {
            stage.setFullScreen(!stage.isFullScreen());
        }
    }

    /**
     * Call this method to load a different video file.
     * @param filePath The path to the new video file.
     */
    public void setVideoFile(String filePath) {
        System.out.println("setVideoFile");
        defaultVideoFilePath = filePath; // Update the path
        loadVideoMedia(filePath); // Reload the media
    }

    /**
     * Disposes of the MediaPlayer to release system resources.
     * Call this when the stage/scene containing this controller is closed.
     */
    public void dispose() {
        System.out.println("dispose");
        if (mediaPlayer != null) {
            mediaPlayer.dispose();
            mediaPlayer = null;
        }
    }
}


//package orgs.tuasl_clint.controllers;
//
//import javafx.animation.FadeTransition;
//import javafx.application.Platform;
//import javafx.beans.property.BooleanProperty;
//import javafx.beans.property.SimpleBooleanProperty;
//import javafx.fxml.FXML;
//import javafx.fxml.Initializable;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.control.Slider;
//import javafx.scene.input.KeyCode;
//import javafx.scene.layout.StackPane;
//import javafx.scene.layout.VBox;
//import javafx.scene.media.Media;
//import javafx.scene.media.MediaPlayer;
//import javafx.scene.media.MediaView;
//import javafx.scene.shape.SVGPath;
//import javafx.stage.Stage;
//import javafx.util.Duration;
//
//import java.io.File;
//import java.net.URL;
//import java.util.ResourceBundle;
//
//public class VideoPlayerController implements Initializable {
//
//    @FXML private StackPane videoPlayerContainer;
//    @FXML private MediaView mediaView;
//    @FXML private VBox controlsOverlay;
//    @FXML private Slider progressBar;
//    @FXML private StackPane playPauseButtonContainer;
//    @FXML private Button playPauseButton;
//    @FXML private Label currentTimeLabel;
//    @FXML private Label totalTimeLabel;
//    @FXML private Slider volumeSlider;
//    @FXML private Button fullscreenButton;
//    @FXML private StackPane centerPlayButtonContainer;
//    @FXML private Button centerPlayButton;
//
//    // Icons
//    private final SVGPath playIconSmall = createPlayIconSmall();
//    private final SVGPath pauseIconSmall = createPauseIconSmall();
//    private final SVGPath playIconLarge = createPlayIconLarge();
//
//    private MediaPlayer mediaPlayer;
//    private final BooleanProperty isPlaying = new SimpleBooleanProperty(false);
//    private final BooleanProperty isFullscreen = new SimpleBooleanProperty(false);
//    private boolean isSeeking = false;
//    private String videoFilePath = "videos/goBack.mp4";
//
//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//        System.out.println("initialize vidio item");
//        initializeIcons();
//        setupUIComponents();
//        loadVideoMedia(videoFilePath);
//    }
//
//    private SVGPath createPlayIconSmall() {
//        System.out.println("createPlayIconSmall");
//        SVGPath icon = new SVGPath();
//        icon.setContent("M -5 -6 L 10 0 L -5 6 Z");
//        icon.setFill(javafx.scene.paint.Color.WHITE);
//        icon.setMouseTransparent(true);
//        return icon;
//    }
//
//    private SVGPath createPauseIconSmall() {
//        System.out.println("createPauseIconSmall");
//        SVGPath icon = new SVGPath();
//        icon.setContent("M -4 -6 H -1 V 6 H -4 Z M 1 6 H 4 V -6 H 1 Z");
//        icon.setFill(javafx.scene.paint.Color.WHITE);
//        icon.setMouseTransparent(true);
//        return icon;
//    }
//
//    private SVGPath createPlayIconLarge() {
//        System.out.println("createPlayIconLarge");
//        SVGPath icon = new SVGPath();
//        icon.setContent("M -10 -15 L 20 0 L -10 15 Z");
//        icon.setFill(javafx.scene.paint.Color.WHITE);
//        icon.setMouseTransparent(true);
//        return icon;
//    }
//
//    private void initializeIcons() {
//        System.out.println("initializeIcons");
//        // Initialization is now handled by the create methods
//    }
//
//    private void setupUIComponents() {
//        System.out.println("setupUIComponents");
//        // Set initial icons
//        updatePlayPauseIcons();
//
//        // Set up button actions
//        playPauseButton.setOnAction(event -> togglePlayPause());
//        centerPlayButton.setOnAction(event -> togglePlayPause());
//        fullscreenButton.setOnAction(event -> toggleFullscreen());
//
//        // Volume slider
//        volumeSlider.setValue(100);
//
//        // Set up hover effects
//        setupHoverEffects();
//
//        // Fullscreen listener
//        Platform.runLater(() -> {
//            System.out.println("Platform.runLater(()");
//            Stage stage = (Stage) mediaView.getScene().getWindow();
//            if (stage != null) {
//                stage.fullScreenProperty().addListener((obs, oldVal, newVal) -> {
//                    System.out.println("stage.fullScreenProperty().addListener((obs, oldVal, newVal)");
//                    isFullscreen.set(newVal);
//                    adjustMediaViewSize(newVal, stage);
//                });
//
//                // Escape key listener
//                stage.getScene().setOnKeyPressed(event -> {
//                    System.out.println("stage.getScene().setOnKeyPressed(event -> {");
//                    if (event.getCode() == KeyCode.ESCAPE && stage.isFullScreen()) {
//                        stage.setFullScreen(false);
//                    }
//                });
//            }
//        });
//
//        // Initial state
//        controlsOverlay.setOpacity(0);
//        centerPlayButtonContainer.setVisible(true);
//    }
//
//    private void adjustMediaViewSize(boolean fullscreen, Stage stage) {
//        System.out.println("adjustMediaViewSize");
//        if (fullscreen) {
//            System.out.println("if (fullscreen) {");
//            mediaView.fitWidthProperty().bind(stage.widthProperty());
//            mediaView.fitHeightProperty().bind(stage.heightProperty());
//        } else {
//            System.out.println("if (fullscreen) {} else {");
//            mediaView.fitWidthProperty().unbind();
//            mediaView.fitHeightProperty().unbind();
//            mediaView.setFitWidth(videoPlayerContainer.getWidth());
//            mediaView.setFitHeight(videoPlayerContainer.getHeight());
//        }
//    }
//
//    public void loadVideoMedia(String filePath) {
//        System.out.println("loadVideoMedia");
//        disposeMediaPlayer();
//
//        try {
//            if(filePath.equals("videos/goBack.mp4"))
//                filePath = getClass().getResource("/orgs/tuasl_clint/videos/goBack.mp4").toURI().toString();
//            Media videoMedia = new Media(filePath);
//            mediaPlayer = new MediaPlayer(videoMedia);
//            mediaView.setMediaPlayer(mediaPlayer);
//
//            // Bind MediaView size
//            mediaView.fitWidthProperty().bind(videoPlayerContainer.widthProperty());
//            mediaView.fitHeightProperty().bind(videoPlayerContainer.heightProperty());
//            mediaView.setPreserveRatio(true);
//
//            // Bind volume
//            mediaPlayer.volumeProperty().bind(volumeSlider.valueProperty().divide(100.0));
//
//            // Set up MediaPlayer listeners
//            setupMediaPlayerListeners();
//
//        } catch (Exception e) {
//            System.err.println("Failed to load video media: " + e.getMessage());
//            e.printStackTrace();
//            showErrorState("Media load error");
//        }
//    }
//
//    private void setupMediaPlayerListeners() {
//        System.out.println("setupMediaPlayerListeners");
//        mediaPlayer.setOnReady(() -> {
//            progressBar.setMin(0);
//            progressBar.setMax(mediaPlayer.getMedia().getDuration().toSeconds());
//            totalTimeLabel.setText(formatDuration(mediaPlayer.getMedia().getDuration()));
//            currentTimeLabel.setText(formatDuration(Duration.ZERO));
//            enableControls(true);
//        });
//
//        mediaPlayer.currentTimeProperty().addListener((observable, oldTime, newTime) -> {
//            System.out.println("mediaPlayer.currentTimeProperty().addListener((observable, oldTime, newTime) -> {");
//            if (!isSeeking) {
//                Platform.runLater(() -> progressBar.setValue(newTime.toSeconds()));
//            }
//            Platform.runLater(() -> currentTimeLabel.setText(formatDuration(newTime)));
//        });
//
//        progressBar.valueProperty().addListener((observable, oldValue, newValue) -> {
//            System.out.println("progressBar.valueProperty().addListener((observable, oldValue, newValue) -> {");
//            if (progressBar.isValueChanging() && mediaPlayer != null) {
//                isSeeking = true;
//                mediaPlayer.seek(Duration.seconds(newValue.doubleValue()));
//            }
//        });
//
//        progressBar.setOnMouseReleased(event -> {
//            System.out.println("progressBar.setOnMouseReleased(event -> {");
//            isSeeking = false;
//            if (mediaPlayer != null) {
//                mediaPlayer.seek(Duration.seconds(progressBar.getValue()));
//                if (!isPlaying.get()) {
//                    mediaPlayer.pause();
//                }
//            }
//        });
//
//        mediaPlayer.setOnEndOfMedia(() -> {
//            System.out.println("mediaPlayer.setOnEndOfMedia(() -> {");
//            Platform.runLater(() -> {
//                if (mediaPlayer != null) {
//                    mediaPlayer.stop();
//                    mediaPlayer.seek(Duration.ZERO);
//                }
//                updatePlayPauseIcons();
//                centerPlayButtonContainer.setVisible(true);
//                controlsOverlay.setOpacity(1.0);
//            });
//        });
//
//        mediaPlayer.setOnError(() -> {
//            System.out.println("mediaPlayer.setOnError(() -> {");
//            Platform.runLater(() -> {
//                System.err.println("MediaPlayer error: " + (mediaPlayer != null ? mediaPlayer.getError() : "Unknown"));
//                showErrorState("Playback error");
//            });
//        });
//
//        isPlaying.bind(mediaPlayer.statusProperty().isEqualTo(MediaPlayer.Status.PLAYING));
//        isPlaying.addListener((obs, oldVal, newVal) -> updatePlayPauseIcons());
//    }
//
//    private void togglePlayPause() {
//        System.out.println("togglePlayPause");
//        if (mediaPlayer == null) return;
//
//        Platform.runLater(() -> {
//            System.out.println("vid state is :");
//            switch (mediaPlayer.getStatus()) {
//                case PLAYING:
//                    System.out.println("playing");
//                    mediaPlayer.pause();
//                    break;
//                case PAUSED:
//                case STOPPED:
//                case READY:
//                    System.out.println("paused or stopped or ready");
//                    mediaPlayer.play();
//                    break;
//                default:
//                    System.out.println("default => none or null value");
//                    break;
//            }
//        });
//    }
//
//    private void updatePlayPauseIcons() {
//        System.out.println("updatePlayPauseIcons");
//        Platform.runLater(() -> {
//            // Clear existing icons
//            playPauseButtonContainer.getChildren().clear();
//            centerPlayButtonContainer.getChildren().clear();
//
//            // Add appropriate icons
//            if (isPlaying.get()) {
//                playPauseButtonContainer.getChildren().add(pauseIconSmall);
//            } else {
//                playPauseButtonContainer.getChildren().add(playIconSmall);
//                centerPlayButtonContainer.getChildren().add(playIconLarge);
//            }
//
//            centerPlayButtonContainer.setVisible(!isPlaying.get());
//        });
//    }
//
//    private void setupHoverEffects() {
//        System.out.println("setupHoverEffects");
//        videoPlayerContainer.setOnMouseEntered(event -> {
//            FadeTransition ft = new FadeTransition(Duration.millis(300), controlsOverlay);
//            ft.setToValue(1.0);
//            ft.play();
//        });
//
//        videoPlayerContainer.setOnMouseExited(event -> {
//            System.out.println("videoPlayerContainer.setOnMouseExited(event -> {");
//            if (isPlaying.get()) {
//                FadeTransition ft = new FadeTransition(Duration.millis(300), controlsOverlay);
//                ft.setToValue(0.0);
//                ft.play();
//            }
//        });
//    }
//
//    private void toggleFullscreen() {
//        System.out.println("toggleFullscreen");
//        Platform.runLater(() -> {
//            Stage stage = (Stage) mediaView.getScene().getWindow();
//            if (stage != null) {
//                stage.setFullScreen(!stage.isFullScreen());
//            }
//        });
//    }
//
//    private String formatDuration(Duration duration) {
//        System.out.println("formatDuration");
//        if (duration == null || duration.isUnknown() || duration.isIndefinite()) {
//            return "--:--";
//        }
//        long minutes = (long) duration.toMinutes();
//        long seconds = (long) (duration.toSeconds() % 60);
//        return String.format("%02d:%02d", minutes, seconds);
//    }
//
//    private void showErrorState(String message) {
//        System.out.println("showErrorState");
//        Platform.runLater(() -> {
//            enableControls(false);
//            currentTimeLabel.setText("Error");
//            totalTimeLabel.setText("Error");
//            System.err.println(message);
//        });
//    }
//
//    private void enableControls(boolean enable) {
//        System.out.println("enableControls");
//        playPauseButton.setDisable(!enable);
//        centerPlayButton.setDisable(!enable);
//        progressBar.setDisable(!enable);
//        volumeSlider.setDisable(!enable);
//    }
//
//    private void disposeMediaPlayer() {
//        System.out.println("disposeMediaPlayer");
//        if (mediaPlayer != null) {
//            mediaPlayer.volumeProperty().unbind();
//            mediaPlayer.stop();
//            mediaPlayer.dispose();
//            mediaPlayer = null;
//        }
//        mediaView.setMediaPlayer(null);
//    }
//
//    public void setVideoFile(String filePath) {
//        System.out.println("setVideoFile");
//        this.videoFilePath = filePath;
//        loadVideoMedia(filePath);
//    }
//
//    public void dispose() {
//        System.out.println("dispose");
//        disposeMediaPlayer();
//        mediaView.fitWidthProperty().unbind();
//        mediaView.fitHeightProperty().unbind();
//        videoPlayerContainer.setOnMouseEntered(null);
//        videoPlayerContainer.setOnMouseExited(null);
//    }
//}