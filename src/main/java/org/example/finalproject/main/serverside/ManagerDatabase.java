package org.example.finalproject.main.serverside;

import io.github.cdimascio.dotenv.Dotenv;
import org.example.finalproject.cells.AnimalCell;
import org.example.finalproject.cells.Cell;
import org.example.finalproject.cells.PlantCell;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ManagerDatabase {

    private static final Dotenv dotenv = Dotenv.load();
    private static final String ADMIN_PASSWORD = dotenv.get("ADMIN_PASSWORD");
    private static final String DATABASE_NAME = dotenv.get("DATABASE_NAME");

    public void getConnection() {
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/" + DATABASE_NAME,
                    "biologist",
                    ADMIN_PASSWORD);
            if(connection != null){
                System.out.println("Connected to database");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static int findSpecieID(Connection connection, Cell cell) {
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            String findSpeciesIdQuery = null;
            if (cell instanceof AnimalCell animalCell) {
                findSpeciesIdQuery = String.format("SELECT specie_id FROM species WHERE specie_name = '%s'", animalCell.animalType);
            } else if (cell instanceof PlantCell plantCell) {
                findSpeciesIdQuery = String.format("SELECT specie_id FROM species WHERE specie_name = '%s'", plantCell.plantType);
            }

            statement = connection.createStatement();
            resultSet = statement.executeQuery(findSpeciesIdQuery);

            if (resultSet.next()) {
                return resultSet.getInt("specie_id");
            } else {
                String addSpeciesQuery = null;
                if (cell instanceof AnimalCell animalCell) {
                    addSpeciesQuery = String.format("INSERT INTO species (specie_name) VALUES ('%s') RETURNING specie_id", animalCell.animalType);
                } else if (cell instanceof PlantCell plantCell) {
                    addSpeciesQuery = String.format("INSERT INTO species (specie_name) VALUES ('%s') RETURNING specie_id", plantCell.plantType);
                }

                resultSet = statement.executeQuery(addSpeciesQuery);
                if (resultSet.next()) {
                    return resultSet.getInt("specie_id");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error accessing species data", e);
        } finally {
            // Close resources to avoid memory leaks
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return -1; // Return -1 if no specie_id is found or inserted
    }

    //    public static void insertCells(Connection connection, List<Cell> cells){}
    //    public static void updateCells(List<Cell> cells){}
    //    public static void deleteCells(List<Cell> cells){}

    public static List<Cell> getCells(Connection connection) {
        Statement statement = null;
        List<Cell> cells = new ArrayList<>();
        try {
            String getCellsQuery = "SELECT c.cell_id, s.specie_name, t.type_name, " +
                    "n.number_nucleolus, r.number_ribosomes, m.number_mitochondrias, " +
                    "g.number_golgi_apparatus, ct.number_centrosomes, ch.number_chloroplasts, " +
                    "d.size_mm " +
                    "FROM cells c " +
                    "JOIN species s ON c.specie_id = s.specie_id " +
                    "JOIN types t ON c.type_id = t.type_id " +
                    "JOIN data d ON c.data_id = d.data_id " +
                    "JOIN nucleolus n ON d.nucleolus_id = n.nucleolus_id " +
                    "JOIN ribosomes r ON d.ribosomes_id = r.ribosomes_id " +
                    "JOIN mitochondria m ON d.mitochondria_id = m.mitochondria_id " +
                    "JOIN golgi_apparatus g ON d.golgi_apparatus_id = g.golgi_apparatus_id " +
                    "JOIN centrosome ct ON d.centrosome_id = ct.centrosome_id " +
                    "JOIN chloroplast ch ON d.chloroplast_id = ch.chloroplast_id";

            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(getCellsQuery);

            while (resultSet.next()) {
                // Retrieve values from the result set
                int cellId = resultSet.getInt("cell_id");
                String speciesName = resultSet.getString("specie_name");
                String typeName = resultSet.getString("type_name");
                int numberNucleolus = resultSet.getInt("number_nucleolus");
                int numberRibosomes = resultSet.getInt("number_ribosomes");
                int numberMitochondria = resultSet.getInt("number_mitochondrias");
                int numberGolgiApparatus = resultSet.getInt("number_golgi_apparatus");
                int numberCentrosomes = resultSet.getInt("number_centrosomes");
                int numberChloroplasts = resultSet.getInt("number_chloroplasts");
                double sizeMm = resultSet.getDouble("size_mm");

                // Create a Cell based on typeName
                if (typeName.equals("Animal")) {
                    AnimalCell animalCell = new AnimalCell(speciesName, sizeMm, numberRibosomes, numberMitochondria, numberGolgiApparatus);
                    cells.add(animalCell);
                } else if (typeName.equals("Plant")) {
                    PlantCell plantCell = new PlantCell(speciesName, sizeMm, numberRibosomes, numberMitochondria, numberGolgiApparatus, numberChloroplasts);
                    cells.add(plantCell);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error accessing cell data", e);
        } finally {
            try {
                if (statement != null) statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return cells;
    }

}

