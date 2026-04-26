import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Spieler {

    Scanner input = new Scanner(System.in);
    String name;
    Spiel uno = new Spiel();

    public String gettingName() {
        System.out.print("Wie heißt der Spieler? ");
        return input.nextLine();
    }

    public int randomizePlayer() {
        Random rand = new Random();
        return rand.nextInt(4);
    }

    ArrayList<Card> hand = new ArrayList<>();

    public void playerHand() {
        hand = uno.zieheKarten(7);
        System.out.println("Deine Karten sind:");
        for (Card c : hand) {
            c.print();
        }
    }
}