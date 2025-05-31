package orgs.clint_pages.Models;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.net.Socket;

public class Client {

    public Client() throws IOException {
    }

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 1234);

        try (DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
             DataInputStream inputStream = new DataInputStream(socket.getInputStream())) {

            String sender = "mohammed";
            String recipient = "ali";
            String text = "jjjjj";

            // Ask the user to input the file path via the console
//            Scanner scanner = new Scanner(System.in);
            System.out.println("Please enter the full path of the image file to send:");
            //String imagePath = "D:\\ThiredYear\\java\\Serversocket\\src\\main\\resources\\profile.jpeg";
            //String imagePath = "D:\\tyep the file\\fgl\\nm.jpeg";
            //String imagePath = "D:\\\u202Bاجمل زامل عزك يا يمن جديد 2016 youtube mp3 shelah\u202C - YouTube.mp3";
            String imagePath = "C:\\Users\\ahmed\\OneDrive\\Pictures\\download (1).jpeg";

            byte[] media = null;
            String extension = "";
            String name = "";

            try {
                // Check if the file exists before attempting to read
                Path filePath = Path.of(imagePath);
                if (Files.exists(filePath) && Files.isRegularFile(filePath)) {
                    media = Files.readAllBytes(filePath);

                    // Extract extension and name dynamically from the file path
                    String fileName = filePath.getFileName().toString();
                    int dotIndex = fileName.lastIndexOf('.');
                    if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
                        extension = fileName.substring(dotIndex + 1);  // Get extension (e.g., jpeg)
                        name = fileName.substring(0, dotIndex);  // Get file name without extension (e.g., profile)
                    }
                } else {
                    System.out.println("File not found or invalid path.");
                }
            } catch (InvalidPathException e) {
                System.out.println("Invalid file path: " + e.getMessage());
            }

            // Send data to the server
            outputStream.writeUTF(sender);
            outputStream.writeUTF(recipient);
            outputStream.writeUTF(text);  // Can be empty if no text is to be sent
            outputStream.writeLong(System.currentTimeMillis());

            if (media != null && media.length > 0) {
                // If media exists, send the file information and content
                outputStream.writeInt(media.length);  // Send media length
                outputStream.writeUTF(extension);  // Send file extension (e.g., jpeg)
                outputStream.writeUTF(name);  // Send file name without extension (e.g., profile)
                outputStream.write(media);  // Send the file content
            } else {
                // If no media, send 0 for the length
                outputStream.writeInt(0);
            }

            // Get server response
            String response = inputStream.readUTF();
            System.out.println("Server says: " + response);

            socket.close();
        }
    }
}
