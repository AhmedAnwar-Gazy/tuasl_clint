package orgs.tuasl_clint.livecall;


import javax.sound.sampled.*;
import java.io.OutputStream;
import java.net.Socket;

public class AudioSender {

    private TargetDataLine microphone;
    private volatile boolean running = true;

    public void start(Socket socket) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    AudioFormat format = new AudioFormat(44100.0f, 16, 1, true, true);
                    DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
                    microphone = (TargetDataLine) AudioSystem.getLine(info);
                    microphone.open(format);
                    microphone.start();

                    OutputStream out = socket.getOutputStream();
                    byte[] buffer = new byte[4096];

                    while (running) {
                        int count = microphone.read(buffer, 0, buffer.length);
                        if (count > 0) {
                            out.write(buffer, 0, count);
                        }
                    }

                    microphone.stop();
                    microphone.close();
                    out.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void stop() {
        running = false;
    }
}
