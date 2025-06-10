package orgs.tuasl_clint.Models;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Locale;
import java.nio.file.Files;

public class Server {
    public static void main(String[] args) throws IOException {
        try(ServerSocket serverSocket = new ServerSocket(1234)){
            System.out.println("Server started on port 1234");
            for(;;) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> handleClient(clientSocket)).start();
            }
        }
    }
/*
    private static void handleClient(Socket socket) {
        try (
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            String sender = input.readUTF();
            String recipient = input.readUTF();
            String message = input.readUTF();
            long timestamp = input.readLong();
            int mediaLength = input.readInt();
            String extension = input.readUTF().toLowerCase(Locale.ROOT);
            String name = input.readUTF();

            byte[] media = new byte[mediaLength];
            input.readFully(media);

            System.out.println(" Message From: " + sender + " to " + recipient);
            System.out.println(" Text: " + message);
            System.out.println(" Timestamp: " + timestamp);
            System.out.println(" Media received: " + media.length + " bytes");

            // Try decoding image
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(media));

            if (image == null) {
                System.out.println("❌ Failed to decode image. Saving raw file for debugging...");
                Files.write(new File(name + "_raw." + extension).toPath(), media);
            } else {
                // Save image
                File dir = new File("received_images");
                dir.mkdirs(); // create folder if not exists
                File file = new File(dir, name + "." + extension);
                ImageIO.write(image, extension, file);
                System.out.println(" Image saved at: " + file.getAbsolutePath());
            }

            output.writeUTF("Server received and processed the message.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    */
private static void handleClient(Socket socket) {
    try (
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream())
    ) {
        String sender = input.readUTF();
        String recipient = input.readUTF();
        String message = input.readUTF();
        long timestamp = input.readLong();
        int mediaLength = input.readInt();
        String extension = input.readUTF().toLowerCase(Locale.ROOT);
        String name = input.readUTF();

        byte[] media = new byte[mediaLength];
        input.readFully(media);

        System.out.println("Message From: " + sender + " to " + recipient);
        System.out.println("Text: " + message);
        System.out.println("Timestamp: " + timestamp);
        System.out.println("Media received: " + media.length + " bytes");

        // التأكد من نوع الملف
        if (extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png") || extension.equals("bmp")) {
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(media));
            if (image == null) {
                System.out.println("❌ Failed to decode image. Saving raw file...");
                Files.write(new File(name + "_raw." + extension).toPath(), media);
            } else {
                File dir = new File("received_images");
                Files.createDirectories(dir.toPath());
                File file = new File(dir, name + "." + extension);
                ImageIO.write(image, extension, file);
                System.out.println("✅ Image saved at: " + file.getAbsolutePath());
            }
        } else {
            // حفظ الفيديو أو أي ملف آخر
            File dir = new File("received_videos");
            Files.createDirectories(dir.toPath());
            File file = new File(dir, name + "." + extension);
            Files.write(file.toPath(), media);
            System.out.println("🎞️ Video or file saved at: " + file.getAbsolutePath());
        }

        output.writeUTF("Server received and saved the file.");

    } catch (Exception e) {
        e.printStackTrace();
    }
}

}
