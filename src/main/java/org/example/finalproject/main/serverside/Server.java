package org.example.finalproject.main.serverside;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static final Dotenv dotenv = Dotenv.load();
    private static final int PORT = Integer.parseInt(dotenv.get("PORT"));
    private static final String ADMIN_PASSWORD = dotenv.get("ADMIN_PASSWORD");

    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void closeServerSocket(){
        try {
            if(serverSocket != null){
                serverSocket.close();
            }
        }
        catch (IOException e){
            System.err.println("Error closing server socket");
        }
    }

    public void startServer(){
        try {
            while(!serverSocket.isClosed()){
                Socket socket = serverSocket.accept();
                System.out.println("New connection from " + socket.getInetAddress());

                ClientHandler clientHandler = new ClientHandler(socket);
                Thread clientThread = new Thread(clientHandler);
                clientThread.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {

    // Initialize connection to DB

        ManagerDatabase managerDatabase = new ManagerDatabase();
        managerDatabase.getConnection();

    // Create server socket

        ServerSocket serverSocket1 = new ServerSocket(PORT);
        Server server = new Server(serverSocket1);
        server.startServer();
    }


}
