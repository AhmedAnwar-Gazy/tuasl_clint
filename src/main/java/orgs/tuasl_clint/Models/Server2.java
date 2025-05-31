package orgs.tuasl_clint.Models;

import java.io.*;
import java.net.Socket;

public class Server2 {
    public static void forwardVideo(Socket inSocket, Socket outSocket) {
        try {
            DataInputStream input = new DataInputStream(inSocket.getInputStream());
            DataOutputStream output = new DataOutputStream(outSocket.getOutputStream());

            while (true) {
                int length = input.readInt();
                byte[] data = new byte[length];
                input.readFully(data);
                output.writeInt(length);
                output.write(data);
                output.flush();
            }
        } catch (IOException e) {
            System.out.println("⛔ اتصال الفيديو انتهى: " + e.getMessage());
        }
    }

    public static void forwardAudio(Socket inSocket, Socket outSocket) {
        try {
            InputStream input = inSocket.getInputStream();
            OutputStream output = outSocket.getOutputStream();

            byte[] buffer = new byte[4096];
            int count;

            while ((count = input.read(buffer)) != -1) {
                output.write(buffer, 0, count);
                output.flush();
            }

        } catch (IOException e) {
            System.out.println("⛔ انقطع اتصال الصوت");
        }
    }
}
