package org.example.finalproject.iohandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.Scanner;

public class InputDevice {
    private InputStream inputStream;
    private Scanner scanner;

    static String[] animals = {
            "Lion",
            "Elephant",
            "Tiger",
            "Bear",
            "Wolf",
            "Giraffe",
            "Zebra",
            "Kangaroo",
            "Panda",
            "Leopard",
            "Cheetah",
            "Rhinoceros",
            "Hippopotamus",
            "Fox",
            "Rabbit",
            "Deer",
            "Moose",
            "Eagle",
            "Owl",
            "Shark"
    };

    static String[] plants = {
            "Rose",
            "Sunflower",
            "Tulip",
            "Oak",
            "Pine",
            "Maple",
            "Cactus",
            "Bamboo",
            "Fern",
            "Moss",
            "Lavender",
            "Daffodil",
            "Hibiscus",
            "Lotus",
            "Aloe Vera",
            "Daisy",
            "Ivy",
            "Cherry Blossom",
            "Palm Tree",
            "Orchid"
    };

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

    public static String getRandomAnimal() {
        return animals[new Random().nextInt(animals.length)]; // Return the animal at the random index
    }

    // Function to randomly pick a plant
    public static String getRandomPlant() {
        return plants[new Random().nextInt(plants.length)]; // Return the plant at the random index
    }

    public void close() {
        if (scanner != null) {
            scanner.close();
        }
    }
}

