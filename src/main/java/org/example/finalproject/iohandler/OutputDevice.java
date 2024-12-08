package org.example.finalproject.iohandler;

import java.io.IOException;
import java.io.OutputStream;

import java.util.*;

public class OutputDevice {
    private static OutputStream outputStream = System.out;
    public static Map<String, List<Map<String, Object>>> cellData = new HashMap<>();

    static {
        cellData.put("animalCells", new ArrayList<>());
        cellData.put("plantCells", new ArrayList<>());
    }

    public static void writeMessage(String msg) {
        try {
            outputStream.write((msg + "\n").getBytes());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setOutputStream(OutputStream newOutputStream) {
        outputStream = newOutputStream;
    }

}