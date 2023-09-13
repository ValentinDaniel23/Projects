package Lab3.task3;

import Lab3.task1.CandyBox;
import Lab3.task2.Lindt;
import Lab3.task2.Baravelli;
import Lab3.task2.ChocAmor;
import Lab3.task4.Area;

import java.util.ArrayList;

public class CandyBag {
   private ArrayList<CandyBox> candyBoxes = new ArrayList<>();

    public CandyBag() {
        candyBoxes.add(new Lindt("cherry", "Austria", 20.0f, 5.4f, 19.2f));
        candyBoxes.add(new Lindt("apricot", "Austria", 20.0f, 5.4f, 19.2f));
        candyBoxes.add(new Lindt("strawberry", "Austria", 20.0f, 5.4f, 19.2f));
        candyBoxes.add(new Baravelli("grape", "Italy", 6.7f, 8.7f));
        candyBoxes.add(new ChocAmor("coffee", "France", 5.5f));
        candyBoxes.add(new ChocAmor("vanilla", "France", 5.5f));
    }

    public ArrayList<CandyBox> getCandyBoxes() {
        return candyBoxes;
    }

    public static void main(String[] args) {
        CandyBag gift = new CandyBag();

        /*
        for (CandyBox candy : gift.candyBoxes) {
            System.out.println(candy);

            if( candy instanceof Baravelli ) {
                ((Baravelli) candy).printBaravelliDim();
            }
            if( candy instanceof Lindt ) {
                ((Lindt) candy).printLindtDim();
            }
            if( candy instanceof  ChocAmor ) {
                ((ChocAmor) candy).printChocAmorDim();
            }
        }
        */

        Area letter = new Area(24, "Mihai Eminescu", gift, "Love you");
        // System.out.println(letter.getBirthdayCard());
        System.out.println(letter.getBirthdayCardV2());
    }
}
