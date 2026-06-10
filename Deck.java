import java.util.ArrayList;
import java.util.List;

public class Deck {

    public static List<Card> makeDeck() {
        // Neue Liste für das Kartendeck erstellen
        List<Card> deck = new ArrayList<>();

        // Alle verfügbaren Farben durchlaufen
        for (Color c : Color.values()) {

            // Schwarze Jokerkarten erzeugen
            if (c == Color.BLACK) {

                // Je 4 Farbwahl- und +4-Karten hinzufügen
                for (int i = 0; i < 4; i++) {
                    deck.add(new Card(Value.COLOR_CHANGE, c));
                    deck.add(new Card(Value.PLUS_FOUR, c));
                }

            } else {

                // Die 0-Karte gibt es pro Farbe nur einmal
                deck.add(new Card(Value.ZERO, c));
            }

            // Alle anderen Karten kommen pro Farbe zweimal vor
            for (int i = 0; i < 2; i++) {

                // Für schwarze Karten keine weiteren Karten erzeugen
                if (c != Color.BLACK) {

                    // Aktionskarten hinzufügen
                    deck.add(new Card(Value.SKIP, c));
                    deck.add(new Card(Value.REVERSE, c));
                    deck.add(new Card(Value.PLUS_TWO, c));

                    // Zahlenkarten 1 bis 9 hinzufügen
                    deck.add(new Card(Value.ONE, c));
                    deck.add(new Card(Value.TWO, c));
                    deck.add(new Card(Value.THREE, c));
                    deck.add(new Card(Value.FOUR, c));
                    deck.add(new Card(Value.FIVE, c));
                    deck.add(new Card(Value.SIX, c));
                    deck.add(new Card(Value.SEVEN, c));
                    deck.add(new Card(Value.EIGHT, c));
                    deck.add(new Card(Value.NINE, c));
                }
            }
        }

        // Vollständiges Kartendeck zurückgeben
        return deck;
    }
}
