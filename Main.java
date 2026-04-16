import java.util.ArrayList;

public class Main {
    static void main() {
        Spiel uno = new Spiel();

        uno.shuffle();

        // Wir erstellen eine ArrayListe mit dem Wert CARD - und ziehen dann 7 Karten vom Stapel
        ArrayList<Card> hand = uno.zieheKarten(7);

        // Ausgabe der Karten die in meiner Hand sind
        System.out.println("Deine Karten sind:");
        for (Card c : hand) {
            c.print();
        }
    }
}