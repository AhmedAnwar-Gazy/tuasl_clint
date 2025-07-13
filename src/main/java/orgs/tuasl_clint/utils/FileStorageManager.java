// src/orgs/utils/FileStorageManager.java
package orgs.tuasl_clint.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class FileStorageManager {

    // Base directory for all uploads. Adjust this path as needed for your server.
    public static final String BASE_UPLOAD_DIR = "uploads"; // Relative to where your server JAR runs or an absolute path

    static {
        // Ensure the base upload directory exists
        try {
            Files.createDirectories(Paths.get(BASE_UPLOAD_DIR));
            Files.createDirectories(Paths.get(BASE_UPLOAD_DIR, "images"));
            Files.createDirectories(Paths.get(BASE_UPLOAD_DIR, "videos"));
            Files.createDirectories(Paths.get(BASE_UPLOAD_DIR, "voice_notes"));
            Files.createDirectories(Paths.get(BASE_UPLOAD_DIR, "files")); // For general files
        } catch (IOException e) {
            System.err.println("Error creating upload directories: " + e.getMessage());
            // This is a critical error, you might want to stop the application or log it severely.
        }
    }

    /**
     * Saves an InputStream content to a file in the appropriate subdirectory.
     * Generates a unique filename.
     *
     * @param inputStream The input stream of the file content.
     * @param originalFileName The original name of the file (used for extension).
     * @param messageType The type of message ('image', 'video', 'voiceNote', 'file').
     * @return The relative path to the saved file (e.g., "uploads/images/unique_name.jpg").
     * @throws IOException If an I/O error occurs during saving.
     * @throws IllegalArgumentException If an unsupported messageType is provided.
     */
    public static String saveFile(InputStream inputStream, String originalFileName, String messageType) throws IOException {
        String subDir;
        switch (messageType) {
            case "image":
                subDir = "images";
                break;
            case "video":
                subDir = "videos";
                break;
            case "voiceNote":
                subDir = "voice_notes";
                break;
            case "file": // For general files, document, etc.
                subDir = "files";
                break;
            default:
                throw new IllegalArgumentException("Unsupported message type for file storage: " + messageType);
        }

        String fileExtension = "";
        int dotIndex = originalFileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < originalFileName.length() - 1) {
            fileExtension = originalFileName.substring(dotIndex); // Includes the dot, e.g., ".jpg"
        }

        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
        Path targetPath = Paths.get(BASE_UPLOAD_DIR, subDir, uniqueFileName);

        Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);

        return Paths.get(BASE_UPLOAD_DIR, subDir, uniqueFileName).toString(); // Return relative path
    }

    /**
     * Gets the full absolute path for a given relative file path.
     * Used for serving files or checking existence.
     * @param relativePath The path as stored in the database (e.g., "uploads/images/abc.jpg")
     * @return Absolute Path object.
     */
    public static Path getAbsolutePath(String relativePath) {
        return Paths.get(relativePath).toAbsolutePath();
    }


    private static final String UPLOAD_DIRECTORY = "server_uploads"; // Directory relative to server's run location

    public static Path createUploadDirectory() {
        Path uploadPath = Paths.get(UPLOAD_DIRECTORY);
        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
                System.out.println("Created upload directory: " + uploadPath.toAbsolutePath());
            } catch (IOException e) {
                System.err.println("Failed to create upload directory: " + e.getMessage());
                e.printStackTrace();
                // Handle this error appropriately, perhaps by exiting or disabling file features
            }
        }
        return uploadPath;
    }

    public static String getUploadDirectory() {
        return UPLOAD_DIRECTORY;
    }

}