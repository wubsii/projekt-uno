import java.util.ArrayList;
import java.util.List;

// Verwaltet den Ablegestapel im UNO-Spiel.
// Speichert Karten und stellt Operationen zum Hinzufügen, Entfernen und Abrufen bereit.
public class DiscardPile {

    private final List<Card> pile = new ArrayList<>();

    // Fügt eine Karte zum Ablegestapel hinzu.
    public void addCard(Card card) {
        pile.add(card);
    }

    // Gibt die oberste Karte des Stapels zurück.
    public Card getTopCard() {
        if (pile.isEmpty()) {
            return null;
        }
        return pile.getLast();
    }

    // Gibt alle Karten im Stapel zurück.
    public List<Card> getAllCards() {
        return pile;
    }

    // Entfernt und gibt die oberste Karte des Stapels zurück.
    public Card removeTopCard() {
        if (pile.isEmpty()) {
            return null;
        }
        return pile.removeLast();
    }

    // Gibt alle Karten außer der obersten zurück und behält diese im Stapel.
    public List<Card> takeAllExceptTop() {
        List<Card> newPile = new ArrayList<>();

        for (int i = 0; i < pile.size() - 1; i++) {
            newPile.add(pile.get(i));
        }

        Card top = pile.getLast();
        pile.clear();
        pile.add(top);

        return newPile;
    }
}