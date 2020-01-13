package com.lyennon;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class IOServer {

    public static void main(String[] args) throws IOException {
        final ServerSocket serverSocket = new ServerSocket(8080);

        new Thread(() -> {
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    new Thread(()->{
                        int i = 0;
                        while (true){
                            byte[] data = new byte[1024];
                            try {
                                InputStream inputStream = socket.getInputStream();
                                int len;
                                while ((len = inputStream.read(data)) != -1){
                                    i++;
                                    System.out.println(new String(data, 0, len));
                                }
                                if(i == 2){
                                    socket.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        ).start();
    }
}
