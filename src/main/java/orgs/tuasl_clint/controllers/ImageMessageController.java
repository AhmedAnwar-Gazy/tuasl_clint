package orgs.tuasl_clint.controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import org.bytedeco.libfreenect._freenect_device;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ImageMessageController implements Initializable {

    @FXML
    private ImageView imageView;



    // Default image path for demonstration
    // IMPORTANT: Replace this with a valid path to an image file (e.g., in src/main/resources)
    // Example: "file:src/main/resources/default_image.jpg" or "https://example.com/image.png"
    private String defaultImagePath = "file:src/main/resources/orgs/tuasl_clint/images/ImageNotFound.jpg";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Load a default image for initial display (or you can remove this if always set externally)
        loadImage(defaultImagePath);

        // Set a default caption (or hide if not needed)


        // Set the current time as the timestamp
    }

    /**
     * Loads an image into the ImageView from a given path or URL.
     * @param imagePath The path to the image file (e.g., "file:/C:/images/myimage.jpg" or "http://example.com/image.png")
     */
    public void loadImage(String imagePath) {
        try {
            if (imagePath != null && !imagePath.isEmpty()) {
                Image image = new Image(imagePath);
                if(image.getHeight() > 0 && image.getWidth() > 0){
                    System.out.println("Loading The Image: "+ imagePath);
                    imageView.setImage(image);
                    File imageFile = new File(imagePath);
                    if(imageFile.exists())
                        imageView.setOnMouseClicked(mouseEvent -> {
                            try {
                                if(Desktop.isDesktopSupported())
                                    Desktop.getDesktop().open(imageFile);
                            } catch (IOException e) {
                                System.out.println("Cannot Open The Image");
                            }
                        });

                }
                // Adjust clip for rounded corners if needed (e.g., dynamically adjust Rectangle size)
                // For this FXML, we assume fixed fitWidth, so clip would need to match.
                // If image has arbitrary size, you might need to bind clip dimensions to imageView's actual dimensions.
            } else {
                if(!imagePath.equals(defaultImagePath)){
                    System.out.println("loading default image");
                    loadImage(defaultImagePath);
                }
                else{
                    System.out.println("default Image Cannot Be Found");
                    imageView.setImage(null);
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading image from: " + imagePath + " - " + e.getMessage());
            // Optionally set a fallback image or display an error icon
            if(!imagePath.equals(defaultImagePath)){
                System.out.println("loading default image");
                loadImage(defaultImagePath);
            }
            else{
                System.out.println("default Image Cannot Be Found");
                imageView.setImage(null);
            }
        }
    }

}