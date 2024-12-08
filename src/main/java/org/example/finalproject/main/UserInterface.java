package org.example.finalproject.main;

import io.github.cdimascio.dotenv.Dotenv;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;

public class UserInterface extends ClientInterface{
    private Stage userWindow;
    private Scene userMainScene, userInspectCellsScene, userPlayGameScene;

    public UserInterface() throws IOException {
        super(new Socket("localhost", Integer.parseInt(Dotenv.load().get("PORT"))), "user");
        try {
            bufferedWriter.write(userRole);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            if(bufferedReader.readLine().equals("200")){
                welcomeScene();
            }
            else{
                closeEverything(socket, bufferedReader, bufferedWriter);
                errorScene();
            }

        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
            errorScene();
        }
    }

    public void start() {
        userWindow.setScene(userMainScene);
        userWindow.show();
    }

    public void welcomeScene(){
        userWindow = new Stage();
        userWindow.setTitle("User Environment");

        // Create a VBox for reviews
        VBox reviewBox = new VBox();
        reviewBox.setSpacing(10);
        reviewBox.setPadding(new Insets(10, 10, 10, 10));
        reviewBox.setAlignment(Pos.TOP_CENTER);

        // Generate 15 reviews
        for (int i = 1; i <= 15; i++) {
            Label review = new Label("Review " + i + ": This is a sample review.");
            review.setStyle("-fx-background-color: #83d2fb; -fx-padding: 10; -fx-border-color: #dcdcdc; -fx-border-width: 1;");
            review.setMaxWidth(550);
            review.setWrapText(true);
            reviewBox.getChildren().add(review);
        }

        // Create a ScrollPane and add the VBox to it
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(reviewBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPadding(new Insets(10));

        // Back button to close the window
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> userWindow.close());
        reviewBox.getChildren().add(backButton);

        // Create the main scene
        userMainScene = new Scene(scrollPane, 600, 400);
    }

    public void errorScene(){
        userWindow = new Stage();
        userWindow.setTitle("Error");

        Label errorLabel = new Label("An error occurred. Please try again.");
        errorLabel.setStyle("-fx-background-color: #83d2fb; -fx-padding: 10; -fx-border-color: #dcdcdc; -fx-border-width: 1;");
        errorLabel.setMaxWidth(550);
        errorLabel.setWrapText(true);

        Button backButton = new Button("OK");
        backButton.setOnAction(e -> userWindow.close());

        userMainScene = new Scene(errorLabel, 600, 400);
    }
}
