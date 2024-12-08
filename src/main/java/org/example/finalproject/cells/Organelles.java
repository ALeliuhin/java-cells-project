package org.example.finalproject.cells;

import org.example.finalproject.iohandler.OutputDevice;

public class Organelles {
    public static class Centrosome {
        public int numberCentrosomes;

        public Centrosome(){
            this.numberCentrosomes = 1;
        }
    }

    public static class Chloroplast {
        public int numberChloroplasts;

        public Chloroplast(int numberChloroplasts) {
            this.numberChloroplasts = numberChloroplasts;
            OutputDevice.writeMessage("\tChloroplasts: " + numberChloroplasts);
        }
    }

    public static class GolgiApparatus {
        public int numberGolgiApparatus;

        public GolgiApparatus(int numberGolgiApparatus) {
            this.numberGolgiApparatus = numberGolgiApparatus;
            OutputDevice.writeMessage("\tGolgi apparatus: " + numberGolgiApparatus);
        }
    }

    public static class Mitochondria {
        public int numberMitochondrias;

        public Mitochondria(int numberMitochondrias) {
            this.numberMitochondrias = numberMitochondrias;
            OutputDevice.writeMessage("\tMitochondrias: " + numberMitochondrias);
        }

    }

    public static class Ribosome {
        public int numberRibosomes;

        public Ribosome(int numberRibosomes) {
            this.numberRibosomes = numberRibosomes;
            OutputDevice.writeMessage("\tRibosomes: " + numberRibosomes);
        }
    }

    public static class Nucleolus {
        public int numberNucleolus;

        public Nucleolus() {
            this.numberNucleolus = 1;
        }
    }
}
