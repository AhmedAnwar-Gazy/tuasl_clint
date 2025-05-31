package orgs.tuasl_clint.controllers;


import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;

public class VideoCallWindow {

    private final Stage stage;
    private final ImageView imageView;
    private volatile boolean running = true;

    public VideoCallWindow(String title) {
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

    public void startReceiving(Socket videoSocket) {
        new Thread(() -> {
            try {
                DataInputStream input = new DataInputStream(videoSocket.getInputStream());
                while (running) {
                    int length = input.readInt();
                    byte[] data = new byte[length];
                    input.readFully(data);

                    ByteArrayInputStream bais = new ByteArrayInputStream(data);
                    BufferedImage bufferedImage = ImageIO.read(bais);
                    WritableImage fxImage = SwingFXUtils.toFXImage(bufferedImage, null);

                    Platform.runLater(() -> imageView.setImage(fxImage));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void startSending(Socket videoSocket) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat();
                Java2DFrameConverter java2DFrameConverter = new Java2DFrameConverter();
                VideoCaptureUtil captureUtil = new VideoCaptureUtil();

                try {
                    DataOutputStream output = new DataOutputStream(videoSocket.getOutputStream());
                    while (running) {
                        Frame frame = captureUtil.grabFrame();
                        if (frame != null) {
                            BufferedImage image = java2DFrameConverter.getBufferedImage(frame);
                            WritableImage fxImage = SwingFXUtils.toFXImage(image, null);
                            Platform.runLater(() -> imageView.setImage(fxImage));
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            ImageIO.write(image, "jpg", baos);
                            byte[] data = baos.toByteArray();

                            output.writeInt(data.length);
                            output.write(data);
                            output.flush();
                        }
                        Thread.sleep(50); // تقريباً 20 فريم في الثانية
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    captureUtil.release();
                }
            }
        }).start();
    }

    public void stop() {
        running = false;
        stage.close();
    }
}


