public class Card {
    public Value value;
    public Color color;
    private char chosenColor;

    // Konstruktor
    public Card(Value v, Color c) {
        value = v;
        color = c;
    }

    // Damit wir die Karten mit der methode PRINT() ausgeben können
    public void print() {
        System.out.println(color + " " + value);
    }

    // Gibt einfach den Wert der Enumeration zurück
    public int getPointValue() {
        return value.getPointValue();
    }

    // Überschreibt die toString()-Methode und gibt die Karte farbig im Format "| wert |" zurück.
    @Override
    public String toString() {
        String ansiColor = getAnsiColor(color);
        String reset = "\u001B[0m";
        return ansiColor + "| " + value.toString().toLowerCase() + " |" + reset;
    }

    // Hilfsmethode zur Ermittlung des passenden ANSI-Farbcodes für eine Kartenfarbe.
    private String getAnsiColor(Color color) {
        switch (color) {
            case RED:    return "\u001B[31m"; // Red
            case BLUE:   return "\u001B[34m"; // Blue
            case GREEN:  return "\u001B[32m"; // Green
            case YELLOW: return "\u001B[33m"; // Yellow
            case BLACK:  return "\u001B[37m"; // White (default)
            default:     return "\u001B[37m"; // White (default)
        }
    }

    // Setzt die aktuell ausgewählte Farbe
    public void setChosenColor(char color) {
        this.chosenColor = color;
    }

    // Gibt die aktuell ausgewählte Farbe zurück
    public char getChosenColor() {
        return chosenColor;
    }

    // Gibt den Wert dieser Karte zurück
    public Value getValue() {
        return value;
    }

    // Gibt die Farbe dieser Karte zurück
    public Color getColor() {
        return color;
    }
}