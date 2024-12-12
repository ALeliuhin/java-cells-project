package org.example.finalproject.main.serverside;

import org.example.finalproject.cells.AnimalCell;
import org.example.finalproject.cells.Cell;
import org.example.finalproject.cells.PlantCell;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler implements Runnable{

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>(); // shared across objects
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private String userRole;
    Connection connectionToDb;

    public ClientHandler(Socket socket){
        try{
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
            connectionToDb = new ManagerDatabase().getConnection();
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
//            if(connection != null){
//                ManagerDatabase.closeConnection(connection);
//            }
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
                if (messageFromClient != null && messageFromClient.equals("/inspect")) {
                    System.out.println("User /inspect request.");
                    List<Cell> cells = ManagerDatabase.getCells(connectionToDb);
                    objectOutputStream.writeObject(cells);
                    objectOutputStream.flush();
                } else if (messageFromClient == null) {
                    // Handle client disconnection
                    System.out.println("Client disconnected.");
                    closeEverything(socket, bufferedReader, bufferedWriter);
                    ManagerDatabase.closeConnection(connectionToDb);
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
