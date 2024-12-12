package org.example.finalproject.cells;

import java.io.Serializable;


abstract public class Cell implements Serializable{
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

// ClientInterface for plant cells
interface IsPlantCell extends IsCell {
    void performPhotosynthesis();
    void storeStarch();
}

// ClientInterface for animal cells
interface IsAnimalCell extends IsCell {
    void maintainHomeostasis();
}
