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

    // Gibt einfach den Wert der Enumeration zurück
    public int getPunktwert() {
        return value.getPunktwert();
    }

        public static List<Card> makeDeck () {
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
        public String toString () {
            return color + " " + value;
        }
    }


    //toString-Methode für Ausgabe von buntem Text für die Karten:

//    @Override
//    public String toString() {
//        String ansiColor = "";
//
//        switch (color) {
//            case RED:
//                ansiColor = "\u001B[31m";
//                break;
//            case BLUE:
//                ansiColor = "\u001B[34m";
//                break;
//            case GREEN:
//                ansiColor = "\u001B[32m";
//                break;
//            case YELLOW:
//                ansiColor = "\u001B[33m";
//                break;
//            case BLACK:
//                ansiColor = "\u001B[37m"; // or keep default
//                break;
//        }
//
//        String reset = "\u001B[0m";
//
//        return ansiColor + "[" + color + " " + value + "]" + reset;
//    }


