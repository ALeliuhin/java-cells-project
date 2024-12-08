package org.example.finalproject.iohandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.Scanner;

public class InputDevice {
    private InputStream inputStream;
    private Scanner scanner;

    public InputDevice(InputStream inputStream) {
        this.inputStream = inputStream;
        this.scanner = new Scanner(inputStream);
    }

    public int readInt() {
        if (scanner.hasNextInt()) {
            return scanner.nextInt();
        } else {
            throw new IllegalArgumentException("No integer found in the input stream.\n");
        }
    }

    public String readString() {
        if (scanner.hasNext()) {
            return scanner.next();
        } else {
            throw new IllegalArgumentException("No string found in the input stream.\n");
        }
    }

    public double readDouble() {
        if (scanner.hasNextDouble()) {
            return scanner.nextDouble();
        } else {
            throw new IllegalArgumentException("No double found in the input stream.\n");
        }
    }


    public String nextLine() throws IOException {
        StringBuilder sb = new StringBuilder();
        int n = inputStream.read();
        while (n != -1 && n != '\n') {
            sb.append((char) n);
            n = inputStream.read();
        }
        return sb.toString();
    }

    public static int yieldIntNumber(int bound1, int bound2) {
        return new Random().nextInt(bound1, bound2 + 1);
    }

    public static double yieldDoubleNumber(double bound1, double bound2) {
        return new Random().nextDouble(bound1, bound2 + 1);
    }

    public void close() {
        if (scanner != null) {
            scanner.close();
        }
    }
}

