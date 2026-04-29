import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Card {
//    String farbe;
//    String wert;
//
//    // Constructor der Karten, wo die Werte von der Klasse SPIEL.java genommen werden und in die Variablen in der Klasse CARD.java gespeichert werden ....
//    public Card(String farbe, String wert) {
//        this.farbe = farbe;
//        this.wert = wert;
//    }
//
    // ... damit wir sie mit der methode PRINT() ausgeben können
    public void print() {
        System.out.println(color + " " + value);
    }
    public Value value;
    public Color color;

    public Card(Value v, Color c) {
        value = v;
        color = c;
    }

    public static List<Card> makeDeck() {
        List<Card> deck = new ArrayList<Card>();
        for (Color c : Color.values()) {
            for (Value v : Value.values()) {
                deck.add(new Card(v, c));
            }
        }
        return deck;
    }

    // Override für die ausgabe der Karten
    @Override
    public String toString() {
        return color + " " + value;
    }

//    public static List<Card> makeDeck() {
//        List<Card> deck = new ArrayList<Card>();
//        for (Color c : Color.values()) { // alle farben loopen
//            // schwarze erstellen
//            if (c == Color.BLACK) {
//                for (int i = 0; i < 4; i++) {
//                    deck.add(new Card(Value.COLOR_CHANGE, c));
//                    deck.add(new Card(Value.PLUS_FOUR, c));
//                }
//            }
//            // alle die 1 mal vorkommen erstellen
//            deck.add(new Card(Value.ZERO, c));
//
//            for (int i = 0; i < 2 ; i++) {
//                deck.add(new Card(Value.SKIP, c));
//                deck.add(new Card(Value.REVERSE, c));
//                deck.add(new Card(Value.PLUS_TWO, c));
//
//                deck.add(new Card(Value.ONE, c));
//                deck.add(new Card(Value.TWO, c));
//                deck.add(new Card(Value.THREE, c));
//                deck.add(new Card(Value.FOUR, c));
//                deck.add(new Card(Value.FIVE, c));
//                deck.add(new Card(Value.SIX, c));
//                deck.add(new Card(Value.SEVEN, c));
//                deck.add(new Card(Value.EIGHT, c));
//                deck.add(new Card(Value.NINE, c));
//            }
//        }
//
//        // schwarze karten erstellen
//
//        return deck;
//    }

}