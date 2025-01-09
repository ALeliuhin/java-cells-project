package org.example.finalproject.cells;

import org.example.finalproject.iohandler.InputDevice;
import org.example.finalproject.iohandler.OutputDevice;

import java.io.Serializable;

public class PlantCell extends Cell implements IsCell, IsPlantCell, Comparable<PlantCell>, Serializable {
    public String plantType;
    public Organelles.Chloroplast chloroplast;

    public PlantCell(String plantType, double sizeMm){
        this.plantType = plantType;
        this.sizeMm = sizeMm;
        this.nucleolus = new Organelles.Nucleolus();
        this.mitochondria = new Organelles.Mitochondria(InputDevice.yieldIntNumber(10, 100));
        this.golgiApparatus = new Organelles.GolgiApparatus((byte) InputDevice.yieldIntNumber(1, 10));

        this.chloroplast = new Organelles.Chloroplast(InputDevice.yieldIntNumber(10, 100));

        this.ribosomes = new Organelles.Ribosome(InputDevice.yieldIntNumber(100000, 1000000));
    }

    public PlantCell(String plantType, double sizeMm, int numberRibosomes, int numberMitochondria, int numberGolgiApparatus, int numberChloroplasts) {
        this.plantType = plantType;
        this.sizeMm = sizeMm;
        this.ribosomes = new Organelles.Ribosome(numberRibosomes);
        this.mitochondria = new Organelles.Mitochondria(numberMitochondria);
        this.golgiApparatus = new Organelles.GolgiApparatus(numberGolgiApparatus);
        this.chloroplast = new Organelles.Chloroplast(numberChloroplasts);
        this.nucleolus = new Organelles.Nucleolus();
    }


    @Override
    public void performPhotosynthesis() {
        OutputDevice.writeMessage("\t\nPhotosynthesis -> The plant cell of type " + this.plantType +
                " used chlorophyll from chloroplasts to convert light energy into glucose." +
                "\n\t\t6CO₂ + 6H₂O (uv) →C₆H₁₂O₆ + 6O₂");
        OutputDevice.writeMessage("\t\tThylakoid membranes of the chloroplasts converted sunlight into ATP and NADPH");
        OutputDevice.writeMessage("\t\tUsing ADP and NADPH to fix CO2 into glucose.");
    }

    @Override
    public void storeStarch() {
        OutputDevice.writeMessage("\tAnimal cells do not store starch. Instead, they store energy in the form of glycogen.");
    }


    @Override
    public void obtainEnergy() {
        performPhotosynthesis();
    }

    @Override
    public void produceEnergy() {
        OutputDevice.writeMessage("\tCellular Respiration -> Carrying cellular respiration in mitochondrias " +
                "to break down glucose into ATP.");
    }

    @Override
    public void synthesizeProtein() {
        OutputDevice.writeMessage("\tProtein Synthesis -> Initiating protein synthesis in the plant cell of type " + this.plantType + ".");

        OutputDevice.writeMessage("\t\t1. Transcription: DNA in the nucleus is transcribed into mRNA.");
        OutputDevice.writeMessage("\t\t   RNA polymerase assists in producing mRNA based on the genetic code.");

        OutputDevice.writeMessage("\t\t2. mRNA is transported from the nucleus to the cytoplasm.");

        OutputDevice.writeMessage("\t\t3. Translation: Ribosomes are using the mRNA to assemble amino acids into a polypeptide chain.");
        OutputDevice.writeMessage("\t\t   tRNA molecules bring specific amino acids to the ribosome according to the mRNA codons.");
        OutputDevice.writeMessage("\t\t   Ribosome assembles a polypeptide chain (protein) by joining amino acids.");

        OutputDevice.writeMessage("\t\t4. Protein Folding and Modification: The newly synthesized protein is transported to the Golgi apparatus.");
        OutputDevice.writeMessage("\t\t   The Golgi apparatus modifies, folds, and prepares the protein for its final function.");

        OutputDevice.writeMessage("\t\tProtein synthesis in the plant cell completed.");
    }

    @Override
    public int compareTo(PlantCell plantCell) {
        int sizeComparison = Double.compare(this.sizeMm, plantCell.sizeMm);
        if (sizeComparison != 0) {
            return sizeComparison;
        }
        return Double.compare(this.mitochondria.numberMitochondrias, plantCell.mitochondria.numberMitochondrias);
    }

}
