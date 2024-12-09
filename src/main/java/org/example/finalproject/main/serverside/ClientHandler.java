package org.example.finalproject.main.serverside;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>(); // shared across objects
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String userRole;

    public ClientHandler(Socket socket){
        try{
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.userRole = bufferedReader.readLine();
            clientHandlers.add(this);
            bufferedWriter.write("200");
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }
        catch (Exception e){
            System.err.println("Error creating client handler");
        }
    }

    public void removeClientHandler(){
        clientHandlers.remove(this);
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        removeClientHandler();
        try {
            if (bufferedReader != null){
                bufferedReader.close();
            }
            if (bufferedWriter != null){
                bufferedWriter.close();
            }
            if (socket != null){
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String messageFromClient;
        while(socket.isConnected()){
            try{
                messageFromClient = bufferedReader.readLine();
                if (messageFromClient != null && messageFromClient.equals("Test message")) {
                    System.out.println("User accepted connection.");
                    bufferedWriter.write("200"); // Respond with 200 on success
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                } else if (messageFromClient == null) {
                    // Handle client disconnection
                    System.out.println("Client disconnected.");
                    closeEverything(socket, bufferedReader, bufferedWriter);
                    break;
                }
                // parse OP and content
                // decide how to response
            }
            catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }
}
