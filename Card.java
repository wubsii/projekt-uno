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

}