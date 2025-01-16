package org.example.finalproject.main.serverside;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class ServerTest {

    private Server server;
    private ServerSocket serverSocket;
    private static final String LOGS_FILE = "logs.txt";

    @BeforeEach
    void setUp() throws IOException {
        serverSocket = new ServerSocket(5174);
        server = new Server(serverSocket);

        // Clean up the logs file before each test
        Files.deleteIfExists(Paths.get(LOGS_FILE));
    }

    @AfterEach
    void tearDown() throws IOException {
        server.closeServerSocket();

        if (!serverSocket.isClosed()) {
            serverSocket.close();
        }
    }

    @Test
    void closeServerSocket() throws IOException {
        assertFalse(serverSocket.isClosed(), "Server socket should initially be open.");

        server.closeServerSocket();

        assertTrue(serverSocket.isClosed(), "Server socket should be closed after calling closeServerSocket.");
    }

    @Test
    void startServer() throws IOException {
        Thread serverThread = new Thread(() -> {
            try {
                server.startServer();
            } catch (RuntimeException e) {
                // Ignore runtime exception from stopping the server
            }
        });

        serverThread.start();

        // Simulate a client connecting to the server
        Socket clientSocket = new Socket("localhost", 5174);

        assertNotNull(clientSocket, "Client socket should not be null after connecting to the server.");
        assertTrue(clientSocket.isConnected(), "Client socket should be connected to the server.");

        // Wait briefly to ensure the log file is written
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Verify logs
        File logFile = new File(LOGS_FILE);
        assertTrue(logFile.exists(), "Log file should exist after a client connects.");

        try (FileReader fileReader = new FileReader(LOGS_FILE)) {
            char[] buffer = new char[1000];
            int charsRead = fileReader.read(buffer);
            String logContent = new String(buffer, 0, charsRead);

            assertTrue(logContent.contains("connected"), "Log file should contain the connection message.");
        }

        // Clean up
        clientSocket.close();
        server.closeServerSocket();
        serverThread.interrupt();
    }

}
