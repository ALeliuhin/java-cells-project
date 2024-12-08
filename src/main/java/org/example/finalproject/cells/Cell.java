package org.example.finalproject.cells;

abstract public class Cell {
    public Organelles.Nucleolus nucleolus;
    public Organelles.Ribosome ribosomes;
    public Organelles.Mitochondria mitochondria;
    public Organelles.GolgiApparatus golgiApparatus;

    public double sizeMm;
}

interface IsCell {
    void obtainEnergy();
    void produceEnergy();
    void synthesizeProtein();
}

// Interface for plant cells
interface IsPlantCell extends IsCell {
    void performPhotosynthesis();
    void storeStarch();
}

// Interface for animal cells
interface IsAnimalCell extends IsCell {
    void maintainHomeostasis();
}