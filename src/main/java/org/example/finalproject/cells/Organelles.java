package org.example.finalproject.cells;

import org.example.finalproject.iohandler.OutputDevice;

import java.io.Serializable;

public class Organelles {
    public static class Centrosome implements Serializable{
        public int numberCentrosomes;

        public Centrosome(){
            this.numberCentrosomes = 1;
        }
    }

    public static class Chloroplast implements Serializable{
        public int numberChloroplasts;

        public Chloroplast(int numberChloroplasts) {
            this.numberChloroplasts = numberChloroplasts;
        }
    }

    public static class GolgiApparatus implements Serializable{
        public int numberGolgiApparatus;

        public GolgiApparatus(int numberGolgiApparatus) {
            this.numberGolgiApparatus = numberGolgiApparatus;
        }
    }

    public static class Mitochondria implements Serializable{
        public int numberMitochondrias;

        public Mitochondria(int numberMitochondrias) {
            this.numberMitochondrias = numberMitochondrias;
        }

    }

    public static class Ribosome implements Serializable{
        public int numberRibosomes;

        public Ribosome(int numberRibosomes) {
            this.numberRibosomes = numberRibosomes;
        }
    }

    public static class Nucleolus implements Serializable{
        public int numberNucleolus;

        public Nucleolus() {
            this.numberNucleolus = 1;
        }
    }
}
