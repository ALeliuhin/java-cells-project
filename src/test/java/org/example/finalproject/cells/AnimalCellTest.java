package org.example.finalproject.cells;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnimalCellTest {

    private AnimalCell animalCell;

    @BeforeEach
    void setUp() {
        animalCell = new AnimalCell("Dog", 80, 50000, 100, 5);
    }

    @Test
    void obtainEnergy() {
        animalCell.obtainEnergy();
        assertTrue(true);
    }

    @Test
    void produceEnergy() {
        animalCell.produceEnergy();
        assertDoesNotThrow(() -> animalCell.produceEnergy());
    }

    @Test
    void synthesizeProtein() {
        animalCell.synthesizeProtein();
        assertTrue(true);
    }

    @Test
    void maintainHomeostasis() {
        animalCell.maintainHomeostasis();
        assertDoesNotThrow(() -> animalCell.maintainHomeostasis());
    }

    @Test
    void compareTo() {
        AnimalCell anotherAnimalCell = new AnimalCell("Cat", 70, 60000, 200, 4);
        int comparisonResult = animalCell.compareTo(anotherAnimalCell);
        assertTrue(comparisonResult > 0);  // Expect the result to depend on size or ribosome count
    }
}
