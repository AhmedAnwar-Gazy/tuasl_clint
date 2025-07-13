package orgs.tuasl_clint.livecall;

import org.bytedeco.javacv.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.*;

public class VideoSenderUDP {
    private volatile boolean running = true;

    public void start(String remoteIP, int remotePort) {
        new Thread(() -> {
            try {
                DatagramSocket socket = new DatagramSocket();
                InetAddress address = InetAddress.getByName(remoteIP);

                OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat();
                Java2DFrameConverter java2DFrameConverter = new Java2DFrameConverter();
                VideoCaptureUtil captureUtil = new VideoCaptureUtil();

                while (running) {
                    Frame frame = captureUtil.grabFrame();
                    if (frame != null) {
                        BufferedImage image = java2DFrameConverter.getBufferedImage(frame);
                       // ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        if (image != null) {
                            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                                ImageIO.write(image, "jpg", baos);
                                byte[] data = baos.toByteArray();
                                DatagramPacket packet = new DatagramPacket(data, data.length, address, remotePort);
                                socket.send(packet);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }else
                        {
                            System.out.println("image image image image   is null ");
                        }

                    }else
                    {
                        System.out.println("Frammm      is  null");
                    }

                    Thread.sleep(100); // 20 FPS تقريبًا
                }

                captureUtil.release();
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

