package orgs.tuasl_clint.livecall;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.net.*;

public class VideoReceiverUDP {
    private volatile boolean running = true;

    public void start(ImageView imageView) {
        new Thread(() -> {
            try {
                DatagramSocket socket = new DatagramSocket(0); // ⬅️ نختار بورت عشوائي
                int localPort = socket.getLocalPort(); // ⬅️ نحصل على البورت الفعلي اللي اختاره النظام
                System.out.println("📺 استقبال الفيديو على البورت العشوائي: " + localPort);

                byte[] buffer = new byte[65535];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                while (running) {
                    socket.receive(packet);
                    ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
                    BufferedImage bufferedImage = ImageIO.read(bais);
                    WritableImage fxImage = SwingFXUtils.toFXImage(bufferedImage, null);

                    Platform.runLater(() -> imageView.setImage(fxImage));
                }

                socket.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }


    public void stop() {
        running = false;
    }
}

