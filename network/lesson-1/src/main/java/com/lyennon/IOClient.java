package com.lyennon;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;

public class IOClient {

    public static void main(String[] args) {
        new Thread(() -> {
            Socket socket = null;
            try {
                socket = new Socket("127.0.0.1", 8080);
                while (true) {
                    OutputStream outputStream = socket.getOutputStream();
                    outputStream.write((new Date() + ":Hello World").getBytes());
                    outputStream.flush();
                    Thread.sleep(1000);
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

        }).start();

    }
}
