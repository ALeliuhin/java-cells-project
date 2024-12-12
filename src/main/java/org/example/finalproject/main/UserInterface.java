package org.example.finalproject.main;

import org.example.finalproject.cells.AnimalCell;
import org.example.finalproject.cells.Cell;

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
import org.example.finalproject.cells.PlantCell;
import org.example.finalproject.main.exceptions.ServerUnreached;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
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
                throw new ServerUnreached("Server unreachable.");
            }

        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
            errorScene();
            throw new ServerUnreached("Server unreachable.");
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
        header.setFont(Font.font("Montserrat", FontWeight.BOLD, 24));
        header.setAlignment(Pos.CENTER);
        header.setStyle("-fx-text-fill: #333333; -fx-padding: 10;");

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
        inspectCellsButton.setOnAction(event -> {
            try {
                bufferedWriter.write("/inspect");
                bufferedWriter.newLine();
                bufferedWriter.flush();

               List<Cell> cells = (List<Cell>) objectInputStream.readObject();
               inspectCellsScene(cells);
            }
            catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

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
        root.setTop(topSection);
        root.setCenter(mainGridPane);

        // Create the scene and apply styles
        userMainScene = new Scene(root, 600, 400);
        userMainScene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/org/example/finalproject/Global.css")).toExternalForm()
        );

        // Set the scene and show the stage
        userWindow.setScene(userMainScene);
        userWindow.show();
    }

    public void inspectCellsScene(List<Cell> cells) {
        // Set up the new stage
        userWindow = new Stage();
        userWindow.setTitle("Retrieved Cells");

        // Create a VBox to hold cell information
        VBox cellListVBox = new VBox();
        cellListVBox.setSpacing(20);
        cellListVBox.setPadding(new Insets(20));
        cellListVBox.setAlignment(Pos.CENTER); // Center align the cells

        // Add each cell's properties to the VBox
        for (Cell cell : cells) {
            double sizeMm = cell.sizeMm;
            int nucleolus = cell.nucleolus.numberNucleolus;
            int golgiApparatus = cell.golgiApparatus.numberGolgiApparatus;
            int ribosomes = cell.ribosomes.numberRibosomes;
            int mitochondria = cell.mitochondria.numberMitochondrias;

            String speciesName;
            String cellSpecificInfo;

            if (cell instanceof AnimalCell animalCell) {
                int centrosomes = animalCell.centrosome.numberCentrosomes;
                speciesName = animalCell.animalType;
                cellSpecificInfo = "Centrosomes: " + centrosomes;
            } else if (cell instanceof PlantCell plantCell) {
                int chloroplasts = plantCell.chloroplast.numberChloroplasts;
                speciesName = plantCell.plantType;
                cellSpecificInfo = "Chloroplasts: " + chloroplasts;
            } else {
                speciesName = "Unknown";
                cellSpecificInfo = "N/A";
            }

            // Create a Label for each cell's properties
            Label cellLabel = new Label(
                    "Species: " + speciesName + "\n" +
                            "Size (mm): " + sizeMm + "\n" +
                            "Nucleolus: " + nucleolus + "\n" +
                            "Golgi Apparatus: " + golgiApparatus + "\n" +
                            "Ribosomes: " + ribosomes + "\n" +
                            "Mitochondria: " + mitochondria + "\n" +
                            cellSpecificInfo
            );

            cellLabel.setStyle("-fx-text-fill: #333333; -fx-font-size: 16px; -fx-padding: 10; -fx-background-color: linear-gradient(to bottom, #ffffff, #e0e0e0); -fx-border-color: #cccccc; -fx-border-radius: 10; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.25), 5, 0, 0, 2);");
            cellLabel.setMaxWidth(400);
            cellLabel.setWrapText(true);
            cellListVBox.getChildren().add(cellLabel);
        }

        // Wrap the VBox in a ScrollPane
        ScrollPane scrollPane = new ScrollPane(cellListVBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPadding(new Insets(20));

        // Create a Back button to return to the previous scene
        Button backButton = new Button("Back");
        backButton.setFont(Font.font("Montserrat", FontWeight.NORMAL, 16));
        backButton.setStyle("-fx-text-fill: #ffffff; -fx-background-color: #4e59c2; -fx-padding: 10; -fx-border-radius: 5; -fx-background-radius: 5; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 8, 0, 0, 2);");
        backButton.setOnAction(e -> userWindow.close());

        // Layout for the Back button and ScrollPane
        VBox layout = new VBox();
        layout.setSpacing(20);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(scrollPane, backButton);

        // Create a Scene for the stage
        Scene scene = new Scene(layout, 600, 400);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/org/example/finalproject/Global.css")).toExternalForm());

        // Set the scene and show the stage
        userWindow.setScene(scene);
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
