package org.example.source.Sever_Client;

import java.io.IOException;
import java.net.Socket;

public class ClientProcess {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 8080;

    public boolean connect() throws IOException {
        try (Socket clientSocket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            System.out.println("Connected to server: " + clientSocket.getRemoteSocketAddress());
            return true;
        } catch (IOException e) {
            System.err.println("Error connecting to server: " + e.getMessage());
            return false;
        }
    }
}
