package org.example.finalproject.main.serverside;

import org.example.finalproject.cells.AnimalCell;
import org.example.finalproject.cells.PlantCell;
import org.example.finalproject.cells.Cell;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ManagerDatabaseTest {

    private ManagerDatabase managerDatabase;
    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        managerDatabase = new ManagerDatabase();
        connection = managerDatabase.getConnection();

        // Ensure autoCommit is disabled for transactional control
        if (connection.getAutoCommit()) {
            connection.setAutoCommit(false);
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (connection != null) {
            try {
                connection.rollback(); // Rollback to maintain a clean state
            } finally {
                connection.close();
            }
        }
    }

    @Test
    void testGetConnection() {
        assertNotNull(connection, "Connection should not be null");
    }


    @Test
    void testFindSpecieID_ExistingSpecies() {
        int specieId = ManagerDatabase.findSpecieID(connection, new AnimalCell("Lion", 10.5, 100, 50, 30));
        assertTrue(specieId > 0, "Specie ID should be positive for an existing species");
    }

    @Test
    void testFindSpecieID_NewSpecies() {
        int specieId = ManagerDatabase.findSpecieID(connection, new AnimalCell("Tiger", 12.5, 150, 70, 35));
        assertTrue(specieId > 0, "Specie ID should be positive for a new species");
    }


    @Test
    void testGetCells() {
        List<Cell> cells = ManagerDatabase.getCells(connection);
        assertNotNull(cells, "The list of cells should not be null");
    }

}
