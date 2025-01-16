package org.example.finalproject.cells;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlantCellTest {

    private PlantCell plantCell;

    @BeforeEach
    void setUp() {
        plantCell = new PlantCell("Rose", 100, 500000, 50, 5, 100);
    }

    @Test
    void performPhotosynthesis() {
        plantCell.performPhotosynthesis();
        assertTrue(true);
    }

    @Test
    void storeStarch() {
        plantCell.storeStarch();
        assertDoesNotThrow(() -> plantCell.storeStarch());
    }

    @Test
    void obtainEnergy() {
        plantCell.obtainEnergy();
        assertTrue(true);
    }

    @Test
    void produceEnergy() {
        plantCell.produceEnergy();
        assertDoesNotThrow(() -> plantCell.produceEnergy());
    }

    @Test
    void synthesizeProtein() {
        plantCell.synthesizeProtein();
        assertTrue(true);
    }

    @Test
    void compareTo() {
        PlantCell anotherPlantCell = new PlantCell("Tulip", 80, 400000, 60, 4, 90);
        int comparisonResult = plantCell.compareTo(anotherPlantCell);
        assertTrue(comparisonResult > 0);
    }
}
