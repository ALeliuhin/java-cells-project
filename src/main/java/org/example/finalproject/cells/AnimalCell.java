package org.example.finalproject.cells;

import org.example.finalproject.iohandler.InputDevice;
import org.example.finalproject.iohandler.OutputDevice;


public class AnimalCell extends Cell implements IsCell, IsAnimalCell, Comparable<AnimalCell>{
    public String animalType;
    public Organelles.Centrosome centrosome;

    // Constructor for Animal Cell
    public AnimalCell(String animalType, double sizeMm) {
        this.animalType = animalType;
        this.sizeMm = sizeMm;

        OutputDevice.writeMessage(String.format("The animal cell of type %s was produced. " +
                        "Size: %.2f mm.",
                this.animalType, this.sizeMm));

        this.nucleolus = new Organelles.Nucleolus();
        this.mitochondria = new Organelles.Mitochondria(InputDevice.yieldIntNumber(100, 2000));
        this.golgiApparatus = new Organelles.GolgiApparatus((byte) InputDevice.yieldIntNumber(1, 10));

        this.centrosome = new Organelles.Centrosome();

        this.ribosomes = new Organelles.Ribosome(InputDevice.yieldIntNumber(10000, 1000000));
    }

    public AnimalCell(String animalType,
                      double sizeMm,
                      int numberRibosomes,
                      int numberMitochondria,
                      int numberGolgiApparatus) {
        this.animalType = animalType;
        this.sizeMm = sizeMm;
        this.ribosomes = new Organelles.Ribosome(numberRibosomes);
        this.mitochondria = new Organelles.Mitochondria(numberMitochondria);
        this.golgiApparatus = new Organelles.GolgiApparatus(numberGolgiApparatus);
        this.centrosome = new Organelles.Centrosome();
    }

    @Override
    public void obtainEnergy() {
        OutputDevice.writeMessage("\t\nFood consumption -> Obtaining glucose from the food consumed by " + this.animalType);
    }

    @Override
    public void produceEnergy() {
        OutputDevice.writeMessage("\tCell respiration -> Breaking down glucose in mitochondrias into Adenosine Triphosphate.");
    }

    @Override
    public void synthesizeProtein() {
        OutputDevice.writeMessage("\tProtein Synthesis -> Initiating protein synthesis in the animal cell of type " + this.animalType + ".");

        OutputDevice.writeMessage("\t\t1. Transcription: DNA in the nucleus is transcribed into mRNA.");
        OutputDevice.writeMessage("\t\t   RNA polymerase helps produce mRNA based on the genetic information.");

        OutputDevice.writeMessage("\t\t2. mRNA is transported from the nucleus to the cytoplasm.");

        OutputDevice.writeMessage("\t\t3. Translation: Ribosomes in the cytoplasm translate the mRNA code into a sequence of amino acids.");
        OutputDevice.writeMessage("\t\t   tRNA molecules deliver specific amino acids to the ribosome based on the mRNA codons.");
        OutputDevice.writeMessage("\t\t   The ribosome assembles the amino acids into a polypeptide chain (protein).");

        OutputDevice.writeMessage("\t\t4. Protein Folding and Modification: The newly synthesized protein is transported to the Golgi apparatus for further processing.");
        OutputDevice.writeMessage("\t\t   The Golgi apparatus folds, modifies, and packages the protein for its specific function.");

        OutputDevice.writeMessage("\t\tProtein synthesis in the animal cell completed using " + this.ribosomes.numberRibosomes + " ribosomes.");
    }

    @Override
    public void maintainHomeostasis(){
        OutputDevice.writeMessage("\tMaintaining homeostasis...");
    }

    @Override
    public int compareTo(AnimalCell animalCell) {
        int sizeComparison = Double.compare(this.sizeMm, animalCell.sizeMm);
        if (sizeComparison != 0) {
            return sizeComparison;
        }
        return Double.compare(this.ribosomes.numberRibosomes, animalCell.ribosomes.numberRibosomes);
    }
}
