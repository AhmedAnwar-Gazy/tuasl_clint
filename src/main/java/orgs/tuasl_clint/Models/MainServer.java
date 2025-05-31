package orgs.tuasl_clint.Models;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static orgs.tuasl_clint.Models.Server2.forwardVideo;

public class MainServer {
    public static void main(String [] argc)
    {
        new Thread(() -> {
            try {
                ServerSocket videoSocket = new ServerSocket(6000);
                System.out.println("🎥 Video Call Server Started on port 6000");

                while (true) {
                    Socket senderSocket = videoSocket.accept();
                    Socket receiverSocket = videoSocket.accept();

                    // ربط المرسل بالمستقبل
                    new Thread(() -> forwardVideo(senderSocket, receiverSocket)).start();
                    new Thread(() -> forwardVideo(receiverSocket, senderSocket)).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                ServerSocket audioSocket = new ServerSocket(6001);
                System.out.println("🔊 Audio Call Server Started on port 6001");

                while (true) {
                    Socket senderSocket = audioSocket.accept();
                    Socket receiverSocket = audioSocket.accept();

                    new Thread(() -> Server2.forwardAudio(senderSocket, receiverSocket)).start();
                    new Thread(() -> Server2.forwardAudio(receiverSocket, senderSocket)).start();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
