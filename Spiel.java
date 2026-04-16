import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Spiel {
    private ArrayList<Card> deck = new ArrayList<>();

    // Constructor vom Spiel wo wir die Daten farben und wert inunsere ArrayList namens deck hinzufügen
    public Spiel() {
        String[] farben = {"Rot", "Gelb", "Grün", "Blau"};
        String[] werte = {"0","1","2","3","4","5","6","7","8","9","Skip","Reverse","+2"};

        for (String f : farben) {
            for (String w : werte) {
                deck.add(new Card(f, w));
            }
        }
    }

    public void shuffle() {
        Random rand = new Random();

        // Lenge der ArrayListe -1
        for (int i = deck.size() - 1; i > 0; i--) {

            int randIndex = rand.nextInt(i + 1);

            // Karte an Position i merken
            Card currentCard = deck.get(i);

            // Zufällige Karte nach Position i setzen
            deck.set(i, deck.get(randIndex));

            // Gemerkte Karte an die zufällige Position setzen
            deck.set(randIndex, currentCard);
        }
    }

    // Erstellt zum Testen der Ausgabe, ob Karten richtig gemischt werden
    public void alleKartenAnzeigen() {
        for (Card c : deck) {
            c.print();
        }
    }

    public ArrayList<Card> zieheKarten(int anzahl) {
        // Erstellen unserer Hand, di enoch eine Karten hat
        ArrayList<Card> hand = new ArrayList<>();

        // Schleife die 7 mal durchgeht, damit wir unsere Karten in die Hand bekommen
        for (int i = 0; i < anzahl; i++) {
            // Hinzufügen einer Karte in unsere Hand (Entfernen einer Karte vom Deck [nimmt von ganz oben])
            hand.add(deck.remove(0));
        }

        // Zurückgeben der ArrayListe Hand, damit wir die Karten zeigen können
        return hand;
    }
}


