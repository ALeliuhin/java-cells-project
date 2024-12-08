package org.example.finalproject.main;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Interface {

    public static void playGame(){

    }

    public static class UserInterface {
        private final Stage userWindow;
        private Scene userMainScene, userInspectCellsScene, userPlayGameScene;

        public UserInterface() {
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


        public void start(String path) {
            userWindow.setScene(userMainScene);
            userWindow.show();
        }
    };
    public static class AdminInterface {
        private Stage adminWindow;
        public void start(String path) {

        }
    };
}
