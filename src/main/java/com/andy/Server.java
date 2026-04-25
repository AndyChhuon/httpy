package com.andy;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {

        try (ServerSocket socket = new ServerSocket(8080))  {
            System.out.println("Server running");
            while (true) {
                try (Socket client = socket.accept(); OutputStream out = client.getOutputStream()) {
                    out.write("HTTP/1.1 200 OK\r\n\r\n".getBytes());

                    out.flush();
                    System.out.println("Wrote to client");
                }
            }

        } catch (IOException e){
            System.out.println("IOException:" + e.getMessage());
        }
    }
}
