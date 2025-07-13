package orgs.tuasl_clint.livecall;

import javax.sound.sampled.*;
import java.net.*;

public class AudioSendUDP {
    private TargetDataLine microphone;
    private volatile boolean running = true;

    public void start(String remoteIP, int remotePort) {
        new Thread(() -> {
            try {
                AudioFormat format = new AudioFormat(44100.0f, 16, 1, true, true);
                DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
                microphone = (TargetDataLine) AudioSystem.getLine(info);
                microphone.open(format);
                microphone.start();

                DatagramSocket socket = new DatagramSocket();
                InetAddress address = InetAddress.getByName(remoteIP);

                byte[] buffer = new byte[4096];
                System.out.println("Length  : "  + buffer.length);

                while (running) {
                    int count = microphone.read(buffer, 0, buffer.length);
                    DatagramPacket packet = new DatagramPacket(buffer, count, address, remotePort);
                    socket.send(packet);
                }

                microphone.stop();
                microphone.close();
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
