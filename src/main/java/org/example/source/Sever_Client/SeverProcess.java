package org.example.source.Sever_Client;

import java.io.*;

import java.net.ServerSocket;
import java.net.Socket;

public class SeverProcess {
    private static final int PORT = 8080;

    // Create sever with PORT
    public static void main(String[] args) throws IOException {
        // Listen to client devices connect to sever
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Sever is listening from: " + clientSocket.getRemoteSocketAddress());

                // Handle data between sever and clients
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                String message;
                // Receive message from client
                while((message = in.readLine()) != null) {
                    System.out.println("Client received: " + "\n" + message);
                }
            }
        }catch (IOException e){
            System.err.println("Error sever: " + e.getMessage());
        }
    }
}
