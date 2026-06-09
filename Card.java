import java.util.ArrayList;
import java.util.List;

public class Card {
    public Value value;
    public Color color;

    public Card(Value v, Color c) {
        value = v;
        color = c;
    }

    public int getPointValue() {
        return value.getPointValue();
    }

    public static List<Card> makeDeck() {
        List<Card> deck = new ArrayList<>();
        for (Color c : Color.values()) {
            for (Value v : Value.values()) {
                deck.add(new Card(v, c));
            }
        }
        return deck;
    }

    // Override toString to return colored output in the format | value |
    @Override
    public String toString() {
        String ansiColor = getAnsiColor(color);
        String reset = "\u001B[0m";
        return ansiColor + "| " + value.toString().toLowerCase() + " |" + reset;
    }

    // Helper method to get ANSI color code for a given Color
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

    // Getters and setters
    private char chosenColor;

    public void setChosenColor(char color) {
        this.chosenColor = color;
    }

    public char getChosenColor() {
        return chosenColor;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}