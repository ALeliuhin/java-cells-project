package org.example.finalproject.main;
import org.example.finalproject.main.exceptions.ServerUnreached;

import java.io.*;
import java.net.Socket;

public class ClientInterface {

    public Socket socket;
    public BufferedReader bufferedReader;
    public BufferedWriter bufferedWriter;
    public ObjectOutputStream objectOutputStream;
    public ObjectInputStream objectInputStream;
    public String userRole;

    public ClientInterface(Socket socket, String userRole) {
        try {
            this.socket = socket;
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            this.userRole = userRole;
        } catch (IOException e) {
            throw new ServerUnreached("Could not connect to server.");
        }
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
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
}
