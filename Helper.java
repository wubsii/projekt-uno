import java.util.List;

public class Helper {

    public static List<Card> takeFromDiscard(DiscardPile pile) {
        return pile.takeAllExceptTop();
    }

    public static void refillDeck(List<Card> deck, List<Card> cards) {
        deck.addAll(cards);
    }
}
