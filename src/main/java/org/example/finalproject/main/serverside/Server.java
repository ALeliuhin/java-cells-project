package org.example.finalproject.main.serverside;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Server {

    private static final Dotenv dotenv = Dotenv.load();
    private static final int PORT = Integer.parseInt(dotenv.get("PORT"));
    private static final String LOGS_FILE = "logs.txt";
    private static final String ADMIN_PASSWORD = dotenv.get("ADMIN_PASSWORD");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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
                try {
                    String timestamp = LocalDateTime.now().format(DATE_TIME_FORMATTER);
                    FileWriter fileWriter = new FileWriter(LOGS_FILE, true);
                    fileWriter.write(socket.getInetAddress() + " connected at " + timestamp + "\n" );
                    fileWriter.close();
                }catch (IOException e){
                    System.out.println("Error writing logs to logs.txt");
                }

                ClientHandler clientHandler = new ClientHandler(socket);
                Thread clientThread = new Thread(clientHandler);
                clientThread.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException, SQLException {
    // Create server socket
        ServerSocket serverSocket1 = new ServerSocket(PORT);
        Server server = new Server(serverSocket1);
        server.startServer();
    }


}
