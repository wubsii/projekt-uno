import java.util.ArrayList;
import java.util.List;

public class DiscardPile {

    private List<Card> pile = new ArrayList<>();

    // Karten zum Ablegestapel hinzufügen
    public void addCard(Card card) {
        pile.add(card);
    }

    // erste Karte ausgeben
    public Card getTopCard() {
        if (pile.isEmpty()) {
            return null;
        }
        return pile.get(pile.size() - 1);
    }

    // alle Karten vom Ablegestapel
    public List<Card> getAllCards() {
        return pile;
    }

    // erste Karte vom Ablegestapel entfernen
    public Card removeTopCard() {
        if (pile.isEmpty()) {
            return null;
        }
        return pile.remove(pile.size() - 1);
    }

    // alle Karten außer erster ausgeben
    public List<Card> takeAllExceptTop() {

        List<Card> newPile = new ArrayList<>();

        for (int i = 0; i < pile.size() - 1; i++) {
            newPile.add(pile.get(i));
        }

        Card top = pile.get(pile.size() - 1);
        pile.clear();
        pile.add(top);

        return newPile;
    }
}
