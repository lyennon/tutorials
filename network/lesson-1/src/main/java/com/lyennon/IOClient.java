package com.lyennon;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Date;

public class IOClient {

    public static void main(String[] args) {
        new Thread(() -> {
            while (true) {
                Socket socket = new Socket();
                try {
                    socket.connect(new InetSocketAddress("127.0.0.1", 8080));
                    OutputStream outputStream = socket.getOutputStream();
                    outputStream.write((new Date()+ ":Hello World").getBytes());
                    outputStream.flush();
                    Thread.sleep(1000);
                    outputStream.write((new Date()+ ":Hello World").getBytes());
                    outputStream.flush();
                    socket.close();
                    Thread.sleep(1000);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
