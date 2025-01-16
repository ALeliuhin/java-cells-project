package org.example.finalproject.main;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.finalproject.iohandler.ArgumentParser;
import org.example.finalproject.main.exceptions.FileNameAlreadyExists;
import org.example.finalproject.main.exceptions.ServerUnreached;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws FileNameAlreadyExists {
        try {
            ArgumentParser argumentParser = new ArgumentParser();
            argumentParser.parseArguments(getParameters().getRaw());
        }
        // Parse the arguments
        catch (ServerUnreached e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        launch(args); // Pass arguments to JavaFX
    }
}
