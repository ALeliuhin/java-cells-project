package org.example.finalproject.main.serverside;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        Dotenv dotenv = Dotenv.load();
        int port = Integer.parseInt(dotenv.get("PORT"));
        System.out.println("Listening on port " + port);
        try {
            Socket clientSocket = new Socket("localhost", port);
        }
        catch (IOException e) {
            System.out.println("Error opening socket");
            System.exit(-1);
        }
    }
}
