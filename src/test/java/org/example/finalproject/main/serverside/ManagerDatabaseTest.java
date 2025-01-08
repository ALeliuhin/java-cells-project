package org.example.finalproject.main.serverside;

import org.example.finalproject.cells.AnimalCell;
import org.example.finalproject.cells.Cell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ManagerDatabaseTest {

    private ManagerDatabase managerDatabase;
    private Connection connection;

    @BeforeEach
    void setUp() {
        managerDatabase = new ManagerDatabase();
        connection = managerDatabase.getConnection();
    }

    @Test
    void testGetConnection() {
        assertNotNull(connection, "Connection should not be null");
    }

    @Test
    void testCloseConnection() {
        assertDoesNotThrow(() -> ManagerDatabase.closeConnection(connection), "Should not throw exception while closing connection");
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
    void testInsertCells() {
        List<Cell> cells = new ArrayList<>();
        cells.add(new AnimalCell("Tiger", 12.5, 150, 70, 35));
        assertDoesNotThrow(() -> ManagerDatabase.insertCells(connection, cells), "Inserting cells should not throw an exception");
    }

    @Test
    void testGetCells() {
        List<Cell> cells = ManagerDatabase.getCells(connection);
        assertNotNull(cells, "The list of cells should not be null");
        assertFalse(cells.isEmpty(), "The list of cells should not be empty");
    }

    @Test
    void testInsertMultipleCells() {
        List<Cell> cells = new ArrayList<>();
        cells.add(new AnimalCell("Tiger", 12.5, 150, 70, 35));
        cells.add(new AnimalCell("Lion", 10.5, 100, 50, 30));
        assertDoesNotThrow(() -> ManagerDatabase.insertCells(connection, cells), "Inserting multiple cells should not throw an exception");
    }

    @Test
    void testInsertEmptyCellList() {
        List<Cell> cells = new ArrayList<>();
        assertDoesNotThrow(() -> ManagerDatabase.insertCells(connection, cells), "Inserting an empty list of cells should not throw an exception");
    }

    @Test
    void testFindSpecieID_InvalidSpecies() {
        int specieId = ManagerDatabase.findSpecieID(connection, new AnimalCell("Unicorn", 0, 0, 0, 0));
        assertEquals(-1, specieId, "Specie ID should be -1 for a non-existent species");
    }

}
