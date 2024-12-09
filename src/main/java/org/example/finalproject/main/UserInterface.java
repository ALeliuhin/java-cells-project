package org.example.finalproject.main;

import io.github.cdimascio.dotenv.Dotenv;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.util.Objects;

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

    public void welcomeScene() {
        userWindow = new Stage();
        userWindow.setTitle("User Environment");

        // Load the font
        Font montserrat = Font.loadFont(
                Objects.requireNonNull(getClass().getResource("/org/example/finalproject/static/Montserrat-Regular.ttf")).toExternalForm(), 14
        );

        // Create the header
        Label header = new Label("Cells Laboratory");
        header.setFont(Font.font("Montserrat", FontWeight.BOLD, 24)); // Bold, larger font size
        header.setAlignment(Pos.CENTER);
        header.setStyle("-fx-text-fill: #333333; -fx-padding: 10;"); // Set color and padding

        // Create the menu bar
        Menu editMenu = new Menu("Edit");
        editMenu.getItems().add(new MenuItem("Copy"));
        editMenu.getItems().add(new MenuItem("Paste"));
        editMenu.getItems().add(new MenuItem("Cut"));

        Menu fileMenu = new Menu("File");
        fileMenu.getItems().add(new MenuItem("Save"));
        fileMenu.getItems().add(new MenuItem("Load"));
        fileMenu.getItems().add(new MenuItem("Exit"));

        Menu profileMenu = new Menu("Profile");
        profileMenu.getItems().add(new MenuItem("Logout"));

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu, editMenu, profileMenu);

        // Combine the header and menu bar in a VBox
        VBox topSection = new VBox();
        topSection.getChildren().addAll(menuBar, header);

        // Create the main GridPane for the buttons
        GridPane mainGridPane = new GridPane();
        mainGridPane.setAlignment(Pos.CENTER);
        mainGridPane.setHgap(10);
        mainGridPane.setVgap(10);
        mainGridPane.setPadding(new Insets(25, 25, 25, 25));

        // Create buttons
        Button inspectCellsButton = new Button("Inspect Cells");
        inspectCellsButton.setFont(montserrat);

        Button playGameButton = new Button("Play Game");
        playGameButton.setFont(montserrat);

        Button backButton = new Button("Exit");
        backButton.setFont(montserrat);
        backButton.setOnAction(e -> userWindow.close());

        // Add buttons to the grid
        mainGridPane.add(inspectCellsButton, 0, 0);
        mainGridPane.add(playGameButton, 0, 1);
        mainGridPane.add(backButton, 0, 2);

        // Use a BorderPane to organize the layout
        BorderPane root = new BorderPane();
        root.setTop(topSection); // Place the VBox with header and MenuBar at the top
        root.setCenter(mainGridPane); // Place the GridPane at the center

        // Create the scene and apply styles
        userMainScene = new Scene(root, 600, 400);
        userMainScene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/org/example/finalproject/Global.css")).toExternalForm()
        );

        // Set the scene and show the stage
        userWindow.setScene(userMainScene);
        userWindow.show();
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
