package orgs.clint_pages.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ImageMessageController implements Initializable {

    @FXML
    private ImageView imageView;


    // Default image path for demonstration
    // IMPORTANT: Replace this with a valid path to an image file (e.g., in src/main/resources)
    // Example: "file:src/main/resources/default_image.jpg" or "https://example.com/image.png"
    private String defaultImagePath = "file:src/main/resources/orgs/clint_pages/images/R.png";

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
                imageView.setImage(image);
                // Adjust clip for rounded corners if needed (e.g., dynamically adjust Rectangle size)
                // For this FXML, we assume fixed fitWidth, so clip would need to match.
                // If image has arbitrary size, you might need to bind clip dimensions to imageView's actual dimensions.
            } else {
                imageView.setImage(null); // Clear image if path is empty
            }
        } catch (Exception e) {
            System.err.println("Error loading image from: " + imagePath + " - " + e.getMessage());
            // Optionally set a fallback image or display an error icon
            imageView.setImage(null); // Clear image if error occurs
        }
    }

}