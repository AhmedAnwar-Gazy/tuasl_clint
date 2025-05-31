package orgs.tuasl_clint.controllers;

import javax.sound.sampled.*;
import java.io.InputStream;
import java.net.Socket;

public class AudioReceiver {

    private volatile boolean running = true;

    public void start(Socket socket) {
        new Thread(() -> {
            try {
                AudioFormat format = new AudioFormat(44100.0f, 16, 1, true, true);
                SourceDataLine speakers = AudioSystem.getSourceDataLine(format);
                speakers.open(format);
                speakers.start();

                InputStream in = socket.getInputStream();
                byte[] buffer = new byte[4096];

                while (running) {
                    int count = in.read(buffer, 0, buffer.length);
                    if (count > 0) {
                        speakers.write(buffer, 0, count);
                    }
                }

                speakers.drain();
                speakers.stop();
                speakers.close();
                in.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void stop() {
        running = false;
    }
}