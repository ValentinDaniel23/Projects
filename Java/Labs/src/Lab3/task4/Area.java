package Lab3.task4;

import Lab3.task3.CandyBag;
import Lab3.task1.CandyBox;
import Lab3.task2.ChocAmor;
import Lab3.task2.Lindt;
import Lab3.task2.Baravelli;
public class Area {
    private CandyBag bag;
    private int number;
    private String street;
    private String message;

    void Area() {
        bag = new CandyBag();
        number = 0;
        street = "nowhere";
        message = "nowhere";
    }

    public Area(int number, String street, CandyBag candyBag, String message) {
        this.number = number;
        this.street = street;
        this.bag = candyBag;
        this.message = message;
    }

    public String getBirthdayCard() {
        String address = "Address: " + number + " " + street + "\n";
        String birthdayMessage = "Happy Birthday!";
        return address + birthdayMessage;
    }

    public String getBirthdayCardV2() {
        String address = "\nAddress: " + number + " " + street + "\n";
        String birthdayMessage = "Happy Birthday!\n\n";

        StringBuilder card = new StringBuilder(address + birthdayMessage);

        for (CandyBox candy : bag.getCandyBoxes()) {
            card.append(candy.toString()).append("\n");


            if (candy instanceof Baravelli) {
                ((Baravelli) candy).printBaravelliDim();
            } else if (candy instanceof ChocAmor) {
                ((ChocAmor) candy).printChocAmorDim();
            } else if (candy instanceof Lindt) {
                ((Lindt) candy).printLindtDim();
            }

        }

        return card.toString();
    }
}
