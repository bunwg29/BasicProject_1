package org.example.source.Sever_Client;

import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;

public class SeverProcess {
    private static final int PORT = 8080;
    public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Sever is listening from: " + clientSocket.getRemoteSocketAddress());
            }
        }
    }
}
