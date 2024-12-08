package org.example.finalproject.main;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.finalproject.iohandler.ArgumentParser;
import org.example.finalproject.main.exceptions.FileNameAlreadyExists;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws FileNameAlreadyExists {
        // Parse the arguments
        ArgumentParser argumentParser = new ArgumentParser();
        argumentParser.parseArguments(getParameters().getRaw());
    }

    public static void main(String[] args) {
        launch(args); // Pass arguments to JavaFX
    }
}
