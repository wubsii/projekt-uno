import java.util.ArrayList;
import java.util.List;

public class Deck {
    public static List<Card> makeDeck() {
        List<Card> deck = new ArrayList<Card>();
        for (Color c : Color.values()) { // alle farben loopen
            // schwarze erstellen
            if (c == Color.BLACK) {
                for (int i = 0; i < 4; i++) {
                    deck.add(new Card(Value.COLOR_CHANGE, c));
                    deck.add(new Card(Value.PLUS_FOUR, c));
                }
            } else {
                // alle die 1 mal vorkommen erstellen
                deck.add(new Card(Value.ZERO, c));
            }

            for (int i = 0; i < 2 ; i++) {
                if (c != Color.BLACK) {
                    deck.add(new Card(Value.SKIP, c));
                    deck.add(new Card(Value.REVERSE, c));
                    deck.add(new Card(Value.PLUS_TWO, c));

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

        return deck;
    }
}
