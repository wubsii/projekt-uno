import java.util.Random;

public class Card {
    String farbe;
    String wert;

    // Constructor der Karten, wo die Werte von der Klasse SPIEL.java genommen werden und in die Variablen in der Klasse CARD.java gespeichert werden ....
    public Card(String farbe, String wert) {
        this.farbe = farbe;
        this.wert = wert;
    }

    // ... damit wir sie mit der methode PRINT() ausgeben können
    public void print() {
        System.out.println(farbe + " " + wert);
    }
}