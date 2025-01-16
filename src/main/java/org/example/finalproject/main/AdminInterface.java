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
import org.example.finalproject.cells.AnimalCell;
import org.example.finalproject.cells.Cell;
import org.example.finalproject.cells.PlantCell;
import org.example.finalproject.main.exceptions.ServerUnreached;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AdminInterface extends ClientInterface{
    private Stage adminWindow;
    private Scene adminMainScene;
    private Cell selectedCell;

    public AdminInterface() throws IOException {
        super(new Socket("localhost", Integer.parseInt(Dotenv.load().get("PORT"))), "admin");
        try{
            bufferedWriter.write(userRole);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            if(bufferedReader.readLine().equals("200")){
                welcomeScene();
            }
        }
        catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
            errorScene();
            throw new ServerUnreached("Server unreachable.");
        }
    }

    public void start() {
        adminWindow.setScene(adminMainScene);
        adminWindow.show();
    }

    public void welcomeScene() {
        adminWindow = new Stage();
        adminWindow.setTitle("Admin Environment");

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

                List<org.example.finalproject.cells.Cell> cells = (List<Cell>) objectInputStream.readObject();
                inspectCellsScene(cells);
            }
            catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        Button createCellsButton = new Button("Create Cells");
        createCellsButton.setFont(montserrat);
        createCellsButton.setOnAction(event -> createCells());

        Button experimentsButton = new Button("Experiments");
        experimentsButton.setFont(montserrat);
        experimentsButton.setOnAction(event -> {
            try {
                bufferedWriter.write("/inspect");
                bufferedWriter.newLine();
                bufferedWriter.flush();

                List<org.example.finalproject.cells.Cell> cells = (List<Cell>) objectInputStream.readObject();
                experimentScene(cells);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });


        Button playGameButton = new Button("Play Game");
        playGameButton.setFont(montserrat);

        Button backButton = new Button("Exit");
        backButton.setFont(montserrat);
        backButton.setOnAction(e -> adminWindow.close());

        // Add buttons to the grid
        mainGridPane.add(inspectCellsButton, 0, 0);
        mainGridPane.add(createCellsButton, 0, 1);
        mainGridPane.add(experimentsButton, 0, 2);
        mainGridPane.add(playGameButton, 0, 3);
        mainGridPane.add(backButton, 0, 4);

        // Use a BorderPane to organize the layout
        BorderPane root = new BorderPane();
        root.setTop(topSection);
        root.setCenter(mainGridPane);

        // Create the scene and apply styles
        adminMainScene = new Scene(root, 600, 400);
        adminMainScene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/org/example/finalproject/Global.css")).toExternalForm()
        );

        // Set the scene and show the stage
        adminWindow.setScene(adminMainScene);
        adminWindow.show();
    }

    public void inspectCellsScene(List<Cell> cells) {
        // Set up the new stage
        Stage inspectWindow = new Stage();
        inspectWindow.setTitle("Retrieved Cells");

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
        backButton.setOnAction(e -> inspectWindow.close());

        // Layout for the Back button and ScrollPane
        VBox layout = new VBox();
        layout.setSpacing(20);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(scrollPane, backButton);

        // Create a Scene for the stage
        Scene scene = new Scene(layout, 800, 600);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/org/example/finalproject/Global.css")).toExternalForm());

        // Set the scene and show the stage
        inspectWindow.setScene(scene);
        inspectWindow.show();
    }

    public void experimentScene(List<Cell> cells) {
        // Set up the new stage
        Stage experimentWindow = new Stage();
        experimentWindow.setTitle("Experiment on Cells");

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

            // Create a Button for each cell's properties
            Button cellButton = new Button(
                    "Species: " + speciesName + "\n" +
                            "Size (mm): " + sizeMm + "\n" +
                            "Nucleolus: " + nucleolus + "\n" +
                            "Golgi Apparatus: " + golgiApparatus + "\n" +
                            "Ribosomes: " + ribosomes + "\n" +
                            "Mitochondria: " + mitochondria + "\n" +
                            cellSpecificInfo
            );

            cellButton.setStyle("-fx-text-fill: #333333; -fx-font-size: 16px; -fx-padding: 10; -fx-background-color: linear-gradient(to bottom, #ffffff, #e0e0e0); -fx-border-color: #cccccc; -fx-border-radius: 10; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.25), 5, 0, 0, 2);");
            cellButton.setMaxWidth(400);
            cellButton.setWrapText(true);

            cellButton.setOnAction(event -> {
                selectedCell = cell;
                experimentWindow.close();

                Stage optionsWindow = new Stage();
                optionsWindow.setTitle("Cell Options");

                // VBox for cell information
                VBox cellInfoBox = new VBox();
                cellInfoBox.setSpacing(10);
                cellInfoBox.setPadding(new Insets(20));
                cellInfoBox.setAlignment(Pos.TOP_LEFT);

                Label cellInfoLabel = new Label(
                        "Selected Cell:\n" +
                                "Size (mm): " + cell.sizeMm + "\n" +
                                "Nucleolus: " + cell.nucleolus.numberNucleolus + "\n" +
                                "Golgi Apparatus: " + cell.golgiApparatus.numberGolgiApparatus + "\n" +
                                "Ribosomes: " + cell.ribosomes.numberRibosomes + "\n" +
                                "Mitochondria: " + cell.mitochondria.numberMitochondrias
                );

                // Add type-specific information
                if (cell instanceof AnimalCell animalCell) {
                    cellInfoLabel.setText(cellInfoLabel.getText() + "\nCentrosomes: " + animalCell.centrosome.numberCentrosomes);
                } else if (cell instanceof PlantCell plantCell) {
                    cellInfoLabel.setText(cellInfoLabel.getText() + "\nChloroplasts: " + plantCell.chloroplast.numberChloroplasts);
                }

                cellInfoLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333333;");

                cellInfoBox.getChildren().add(cellInfoLabel);

                // VBox for action buttons
                VBox buttonBox = new VBox();
                buttonBox.setSpacing(20);
                buttonBox.setPadding(new Insets(20));
                buttonBox.setAlignment(Pos.CENTER);

                Button performEnergyButton = new Button("Obtain Energy");
                Button synthesizeProteinButton = new Button("Synthesize Protein");
                Button maintainHomeostasisButton = new Button("Maintain Homeostasis");
                Button storeStarchButton = new Button("Store Starch");

                if (selectedCell instanceof AnimalCell) {
                    AnimalCell animalCell = (AnimalCell) selectedCell;
                    performEnergyButton.setOnAction(e -> animalCell.obtainEnergy());
                    Map<Cell, Integer> synthesisCountMap = new HashMap<>();

                    synthesizeProteinButton.setOnAction(e -> {
                        int currentCount = synthesisCountMap.getOrDefault(selectedCell, 0);

                        int newCount = currentCount + 1;
                        synthesisCountMap.put(selectedCell, newCount);

                        // Perform the protein synthesis action
                        ((AnimalCell) selectedCell).synthesizeProtein();

                        // Check if count reaches the threshold
                        if (newCount >= 3) {
                            // Send the `/delete` command to the server
                            try {
                                bufferedWriter.write("/delete");
                                bufferedWriter.newLine();
                                bufferedWriter.flush();

                                // Send the selected cell object to the server
                                objectOutputStream.writeObject(selectedCell);
                                objectOutputStream.flush();

                                // Reset the count for this cell (optional if deletion is permanent)
                                synthesisCountMap.remove(selectedCell);

                                System.out.println("Cell deleted: " + selectedCell);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    });

                    maintainHomeostasisButton.setOnAction(e -> animalCell.maintainHomeostasis());
                } else if (selectedCell instanceof PlantCell) {
                    PlantCell plantCell = (PlantCell) selectedCell;
                    performEnergyButton.setOnAction(e -> plantCell.obtainEnergy());
                    // Map to track synthesis counts for each cell
                    Map<Cell, Integer> synthesisCountMap = new HashMap<>();

                    synthesizeProteinButton.setOnAction(e -> {
                        int currentCount = synthesisCountMap.getOrDefault(selectedCell, 0);

                        int newCount = currentCount + 1;
                        synthesisCountMap.put(selectedCell, newCount);

                        ((PlantCell) selectedCell).synthesizeProtein();

                        if (newCount >= 3) {
                            // Send the `/delete` command to the server
                            try {
                                bufferedWriter.write("/delete");
                                bufferedWriter.newLine();
                                bufferedWriter.flush();

                                // Send the selected cell object to the server
                                objectOutputStream.writeObject(selectedCell);
                                objectOutputStream.flush();

                                // Reset the count for this cell (optional if deletion is permanent)
                                synthesisCountMap.remove(selectedCell);

                                System.out.println("Cell deleted: " + selectedCell);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    });

                    maintainHomeostasisButton.setOnAction(e -> plantCell.performPhotosynthesis());
                    storeStarchButton.setOnAction(e -> plantCell.storeStarch());
                }

                buttonBox.getChildren().addAll(
                        performEnergyButton,
                        synthesizeProteinButton,
                        maintainHomeostasisButton
                );

                if (selectedCell instanceof PlantCell) {
                    buttonBox.getChildren().add(storeStarchButton);
                }

                BorderPane borderPane = new BorderPane();
                borderPane.setTop(cellInfoBox);
                borderPane.setCenter(buttonBox);

                Scene optionsScene = new Scene(borderPane, 800, 600);
                optionsScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/org/example/finalproject/Global.css")).toExternalForm());

                optionsWindow.setScene(optionsScene);
                optionsWindow.show();
            });


            cellListVBox.getChildren().add(cellButton);
        }

        ScrollPane scrollPane = new ScrollPane(cellListVBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPadding(new Insets(20));

        VBox layout = new VBox();
        layout.setSpacing(20);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().add(scrollPane);

        Scene scene = new Scene(layout, 800, 600);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/org/example/finalproject/Global.css")).toExternalForm());

        experimentWindow.setScene(scene);
        experimentWindow.show();
    }


    public void createCells() {
        // Set up the stage for creating cells
        Stage createCellsWindow = new Stage();
        createCellsWindow.setTitle("Synthesize Cells");

        // Create the layout for the scene
        VBox layout = new VBox();
        layout.setSpacing(20);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        // Create a Label for instructions
        Label instructionLabel = new Label("Enter the number of cells to synthesize:");
        instructionLabel.setFont(Font.font("Montserrat", FontWeight.NORMAL, 16));

        // Create an input field for the number of cells
        TextField inputField = new TextField();
        inputField.setPromptText("Number of cells");
        inputField.setMaxWidth(200);

        // Create the Synthesize button
        Button synthesizeButton = new Button("Synthesize");
        synthesizeButton.setFont(Font.font("Montserrat", FontWeight.NORMAL, 16));
        synthesizeButton.setStyle("-fx-text-fill: #ffffff; -fx-background-color: #4e59c2; -fx-padding: 10; -fx-border-radius: 5; -fx-background-radius: 5; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 8, 0, 0, 2);");

        synthesizeButton.setOnAction(event -> {
            try {
                String input = inputField.getText();

                // Validate that the input is a valid integer
                int numCells;
                try {
                    numCells = Integer.parseInt(input);
                    if (numCells <= 0) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException e) {
                    // Show an error alert for invalid input
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Invalid Input");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Please enter a valid positive integer.");
                    errorAlert.showAndWait();
                    return;
                }

                // Send the number of cells to the server
                bufferedWriter.write("/synthesize");
                bufferedWriter.newLine();
                bufferedWriter.flush();

                if(bufferedReader.readLine().equals("200")) {
                    bufferedWriter.write(input);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                } else {
                    errorScene();
                }

                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Successfully synthesized " + input + " cells!");
                successAlert.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("An error occurred while communicating with the server.");
                errorAlert.showAndWait();
            }
        });


        // Create the Back button
        Button backButton = new Button("Back");
        backButton.setFont(Font.font("Montserrat", FontWeight.NORMAL, 16));
        backButton.setStyle("-fx-text-fill: #ffffff; -fx-background-color: #4e59c2; -fx-padding: 10; -fx-border-radius: 5; -fx-background-radius: 5; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 8, 0, 0, 2);");
        backButton.setOnAction(event -> createCellsWindow.close());

        // Add components to the layout
        layout.getChildren().addAll(instructionLabel, inputField, synthesizeButton, backButton);

        // Create the scene
        Scene scene = new Scene(layout, 400, 300);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/org/example/finalproject/Global.css")).toExternalForm());

        // Set the scene and show the stage
        createCellsWindow.setScene(scene);
        createCellsWindow.show();
    }


    public void errorScene(){
        adminWindow = new Stage();
        adminWindow.setTitle("Error");

        Label errorLabel = new Label("An error occurred. Please try again.");
        errorLabel.setStyle("-fx-background-color: #83d2fb; -fx-padding: 10; -fx-border-color: #dcdcdc; -fx-border-width: 1;");
        errorLabel.setMaxWidth(550);
        errorLabel.setWrapText(true);

        Button backButton = new Button("OK");
        backButton.setOnAction(e -> adminWindow.close());

        adminMainScene = new Scene(errorLabel, 600, 400);
    }
}
