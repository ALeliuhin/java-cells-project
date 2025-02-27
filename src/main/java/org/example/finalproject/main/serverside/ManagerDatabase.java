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

    private static final String INSERT_INTO_CELLS_QUERY = "INSERT INTO cells (type_id, specie_id, data_id) VALUES (?, ?, ?)";
    private static final String INSERT_INTO_DATA_QUERY = "INSERT INTO data (nucleolus_id, ribosomes_id, mitochondria_id, golgi_apparatus_id, centrosome_id, chloroplast_id, size_mm) VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING data_id";
    private static final String INSERT_INTO_NUCLEOLUS_QUERY = "INSERT INTO nucleolus (number_nucleolus) VALUES (?) RETURNING nucleolus_id";
    private static final String INSERT_INTO_RIBOSOME_QUERY = "INSERT INTO ribosomes (number_ribosomes) VALUES (?) RETURNING ribosomes_id";
    private static final String INSERT_INTO_MITOCHONDRIA_QUERY = "INSERT INTO mitochondria (number_mitochondrias) VALUES (?) RETURNING mitochondria_id";
    private static final String INSERT_INTO_GOLGI_APPARATUS_QUERY = "INSERT INTO golgi_apparatus (number_golgi_apparatus) VALUES (?) RETURNING golgi_apparatus_id";
    private static final String INSERT_INTO_CENTROSOME_QUERY = "INSERT INTO centrosome (number_centrosomes) VALUES (?) RETURNING centrosome_id";
    private static final String INSERT_INTO_CHLOROPLAST_QUERY = "INSERT INTO chloroplast (number_chloroplasts) VALUES (?) RETURNING chloroplast_id";
    private static final String INSERT_INTO_TYPES_QUERY = "INSERT INTO types (type_name) VALUES (?) ON CONFLICT (type_name) DO UPDATE SET type_name = types.type_name RETURNING type_id";
    private static final String INSERT_INTO_SPECIES_QUERY = "INSERT INTO species (specie_name) VALUES (?) ON CONFLICT (specie_name) DO UPDATE SET specie_name = species.specie_name RETURNING specie_id";

    public Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/" + DATABASE_NAME,
                    "biologist",
                    ADMIN_PASSWORD);
            if (connection != null) {
                System.out.println("Connected to database");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
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

        return -1;
    }


    public static void insertCells(Connection connection, List<Cell> cells) throws SQLException {
        try (
                // Prepare all the required statements
                PreparedStatement insertNucleolusStmt = connection.prepareStatement(INSERT_INTO_NUCLEOLUS_QUERY);
                PreparedStatement insertRibosomeStmt = connection.prepareStatement(INSERT_INTO_RIBOSOME_QUERY);
                PreparedStatement insertMitochondriaStmt = connection.prepareStatement(INSERT_INTO_MITOCHONDRIA_QUERY);
                PreparedStatement insertGolgiApparatusStmt = connection.prepareStatement(INSERT_INTO_GOLGI_APPARATUS_QUERY);
                PreparedStatement insertCentrosomeStmt = connection.prepareStatement(INSERT_INTO_CENTROSOME_QUERY);
                PreparedStatement insertChloroplastStmt = connection.prepareStatement(INSERT_INTO_CHLOROPLAST_QUERY);
                PreparedStatement insertDataStmt = connection.prepareStatement(INSERT_INTO_DATA_QUERY);
                PreparedStatement insertTypeStmt = connection.prepareStatement(INSERT_INTO_TYPES_QUERY);
                PreparedStatement insertSpeciesStmt = connection.prepareStatement(INSERT_INTO_SPECIES_QUERY);
                PreparedStatement insertCellsStmt = connection.prepareStatement(INSERT_INTO_CELLS_QUERY)
        ) {
            connection.setAutoCommit(false); // Start transaction

            for (Cell cell : cells) {
                int nucleolusId = insertDataAndReturnId(insertNucleolusStmt, cell.nucleolus.numberNucleolus);
                int ribosomeId = insertDataAndReturnId(insertRibosomeStmt, cell.ribosomes.numberRibosomes);
                int mitochondriaId = insertDataAndReturnId(insertMitochondriaStmt, cell.mitochondria.numberMitochondrias);
                int golgiApparatusId = insertDataAndReturnId(insertGolgiApparatusStmt, cell.golgiApparatus.numberGolgiApparatus);
                int centrosomeId = 0;
                int chloroplastId = 0;
                String specieName = "";
                String typeName = ""; // Store the type name here

                // Handle centrosome for AnimalCell or chloroplast for PlantCell
                if (cell instanceof AnimalCell) {
                    AnimalCell animalCell = (AnimalCell) cell;
                    centrosomeId = insertDataAndReturnId(insertCentrosomeStmt, animalCell.centrosome.numberCentrosomes);
                    specieName = animalCell.animalType; // Access specieName for AnimalCell
                    typeName = "Animal";  // Access animalType for AnimalCell
                } else if (cell instanceof PlantCell) {
                    PlantCell plantCell = (PlantCell) cell;
                    chloroplastId = insertDataAndReturnId(insertChloroplastStmt, plantCell.chloroplast.numberChloroplasts);
                    specieName = plantCell.plantType; // Access specieName for PlantCell
                    typeName = "Plant";  // Access plantType for PlantCell
                }

                // Step 2: Insert into 'data' table with the returned IDs
                insertDataStmt.setInt(1, nucleolusId);
                insertDataStmt.setInt(2, ribosomeId);
                insertDataStmt.setInt(3, mitochondriaId);
                insertDataStmt.setInt(4, golgiApparatusId);
                if (centrosomeId == 0) {
                    insertDataStmt.setNull(5, Types.INTEGER); // Centrosome ID
                } else {
                    insertDataStmt.setInt(5, centrosomeId);
                }

                if (chloroplastId == 0) {
                    insertDataStmt.setNull(6, Types.INTEGER); // Chloroplast ID
                } else {
                    insertDataStmt.setInt(6, chloroplastId);
                }

                insertDataStmt.setDouble(7, cell.sizeMm);

                ResultSet rsData = insertDataStmt.executeQuery();
                rsData.next();
                int dataId = rsData.getInt("data_id");

                int typeId = insertTypeAndReturnId(insertTypeStmt, typeName);

                int specieId = insertSpeciesAndReturnId(insertSpeciesStmt, specieName);

                insertCellsStmt.setInt(1, typeId);
                insertCellsStmt.setInt(2, specieId);
                insertCellsStmt.setInt(3, dataId);
                insertCellsStmt.executeUpdate();
            }
            connection.commit(); // Commit transaction

        } catch (SQLException e) {
            connection.rollback(); // Rollback transaction on error
            throw new RuntimeException("Error inserting cells", e);
        } finally {
            connection.setAutoCommit(true);
        }
    }

    private static int insertDataAndReturnId(PreparedStatement stmt, int number) throws SQLException {
        stmt.setInt(1, number);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            return rs.getInt(1);
        }
        return -1;
    }

    private static int insertTypeAndReturnId(PreparedStatement stmt, String typeName) throws SQLException {
        stmt.setString(1, typeName);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt(1);
        }
        return -1;
    }

    private static int insertSpeciesAndReturnId(PreparedStatement stmt, String specieName) throws SQLException {
        stmt.setString(1, specieName);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt(1);
        }
        return -1;
    }


    public static void deleteCells(Connection connection, List<Cell> cells) throws SQLException {
                // Prepare all the required delete statements
        PreparedStatement getSpecieID = connection.prepareStatement("SELECT specie_id FROM species WHERE species_name = ?");
        PreparedStatement getDataIDbySize = connection.prepareStatement("SELECT data_id FROM data WHERE size_mm = ?");
        PreparedStatement getCellID = connection.prepareStatement("SELECT cell_id FROM cells JOIN species ON cells.specie_id = specie.id JOIN data ON cells.data_id = data.data_id");
        PreparedStatement deleteCellsStmt = connection.prepareStatement("DELETE FROM cells WHERE cell_id = ?");
        PreparedStatement selectDataIDs = connection.prepareStatement("SELECT * FROM data WHERE data_id = ?");
        PreparedStatement deleteNucleolus = connection.prepareStatement("DELETE FROM nucleolus WHERE nucleolus_id = ?");
        PreparedStatement deleteRibosomes = connection.prepareStatement("DELETE FROM ribosomes WHERE ribosomes_id = ?");
        PreparedStatement deleteMitochondria = connection.prepareStatement("DELETE FROM mitochondria WHERE mitochondria_id = ?");
        PreparedStatement deleteGolgi = connection.prepareStatement("DELETE FROM golgi_apparatus WHERE golgi_apparatus_id = ?");
        PreparedStatement deleteChloroplast = connection.prepareStatement("DELETE FROM chloroplast WHERE chloroplast_id = ?");
        PreparedStatement deleteCentrosome = connection.prepareStatement("DELETE FROM centrosome WHERE centrosome_id = ?");
    }



    public static List<Cell> getCells(Connection connection) {
        Statement statement = null;
        List<Cell> cells = new ArrayList<>();
        try {
            String getCellsQuery = "SELECT * FROM cell_view";

            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(getCellsQuery);

            while (resultSet.next()) {
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

                if (typeName.equals("Animal")) {
                    AnimalCell animalCell = new AnimalCell(speciesName, sizeMm, numberRibosomes, numberMitochondria, numberGolgiApparatus);
                    cells.add(animalCell);
                } else if (typeName.equals("Plant")) {
                    PlantCell plantCell = new PlantCell(speciesName, sizeMm, numberRibosomes, numberMitochondria, numberGolgiApparatus, numberChloroplasts);
                    cells.add(plantCell);
                }
            }
            return cells;
        } catch (SQLException e) {
            throw new RuntimeException("Error accessing cell data", e);
        } finally {
            try {
                if (statement != null) statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}