package orgs.tuasl_clint.livecall;

import javax.sound.sampled.*;
import java.net.*;

public class AudioReceiverUDP {
    private volatile boolean running = true;

    public void start(int listenPort) {
        new Thread(() -> {
            try {
                AudioFormat format = new AudioFormat(44100.0f, 16, 1, true, true);
                SourceDataLine speakers = AudioSystem.getSourceDataLine(format);
                speakers.open(format);
                speakers.start();

                DatagramSocket socket = new DatagramSocket(listenPort);
                byte[] buffer = new byte[4096];
                System.out.println("📥 استقبلنا صورة بحجم: " +buffer.length);


                while (running) {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);
                    speakers.write(packet.getData(), 0, packet.getLength());
                }

                speakers.drain();
                speakers.stop();
                speakers.close();
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

