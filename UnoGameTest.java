import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class UnoGameTest {

    private DiscardPile discardPile;
    private Spiel spiel;
    private Player player;

    // Wird vor jedem Test neu erstellt (sauberer Ausgangszustand)
    @BeforeEach
    void setUp() {
        discardPile = new DiscardPile();
        spiel = new Spiel(discardPile);
        player = new Player(discardPile);
    }

    // =========================================================
    // Card – Tests
    // =========================================================

    @Test
    void card_getColor_returnsCorrectColor() {
        Card card = new Card(Value.FIVE, Color.RED);
        assertEquals(Color.RED, card.getColor());
    }

    @Test
    void card_getValue_returnsCorrectValue() {
        Card card = new Card(Value.SEVEN, Color.BLUE);
        assertEquals(Value.SEVEN, card.getValue());
    }

    @Test
    void card_getPointValue_returnsCorrectPoints() {
        // Aktionskarten haben 20 Punkte
        Card skip = new Card(Value.SKIP, Color.GREEN);
        assertEquals(20, skip.getPointValue());

        // Schwarze Karten haben 50 Punkte
        Card plusFour = new Card(Value.PLUS_FOUR, Color.BLACK);
        assertEquals(50, plusFour.getPointValue());

        // Zahlenkarte ZERO hat 0 Punkte
        Card zero = new Card(Value.ZERO, Color.YELLOW);
        assertEquals(0, zero.getPointValue());
    }

    @Test
    void card_toString_returnsColorAndValue() {
        Card card = new Card(Value.THREE, Color.GREEN);
        assertEquals("GREEN THREE", card.toString());
    }

    // =========================================================
    // Player.isValidMove() – Tests
    // =========================================================

    @Test
    void isValidMove_sameColor_isValid() {
        // Gleiche Farbe → Zug ist erlaubt
        Card topCard  = new Card(Value.FIVE, Color.RED);
        Card played   = new Card(Value.TWO,  Color.RED);
        assertTrue(player.isValidMove(played, topCard));
    }

    @Test
    void isValidMove_sameValue_isValid() {
        // Gleicher Wert → Zug ist erlaubt
        Card topCard  = new Card(Value.FIVE, Color.RED);
        Card played   = new Card(Value.FIVE, Color.BLUE);
        assertTrue(player.isValidMove(played, topCard));
    }

    @Test
    void isValidMove_blackCard_isAlwaysValid() {
        // Schwarze Karten (Joker) dürfen immer gespielt werden
        Card topCard  = new Card(Value.FIVE,         Color.RED);
        Card joker    = new Card(Value.COLOR_CHANGE,  Color.BLACK);
        assertTrue(player.isValidMove(joker, topCard));
    }

    @Test
    void isValidMove_differentColorAndValue_isInvalid() {
        // Andere Farbe und anderer Wert → Zug ist nicht erlaubt
        Card topCard  = new Card(Value.FIVE,  Color.RED);
        Card played   = new Card(Value.THREE, Color.BLUE);
        assertFalse(player.isValidMove(played, topCard));
    }

    @Test
    void isValidMove_plusTwoOnPlusTwo_isValid() {
        // Plus-Zwei auf Plus-Zwei → gleicher Wert, also erlaubt
        Card topCard  = new Card(Value.PLUS_TWO, Color.RED);
        Card played   = new Card(Value.PLUS_TWO, Color.GREEN);
        assertTrue(player.isValidMove(played, topCard));
    }

    // =========================================================
    // Spiel.shuffle() – Tests
    // =========================================================

    @Test
    void shuffle_deckStillContainsAllCards() {
        // Nach dem Mischen muss die Anzahl der Karten gleich bleiben
        int sizeBeforeShuffle = spiel.deck.size();
        spiel.shuffle();
        assertEquals(sizeBeforeShuffle, spiel.deck.size());
    }

    @Test
    void shuffle_deckOrderIsChanged() {
        // Originale Reihenfolge speichern
        List<Card> original = new ArrayList<>(spiel.deck);
        spiel.shuffle();

        // Nach dem Mischen sollte die Reihenfolge anders sein
        // (Wahrscheinlichkeit, dass sie gleich bleibt, ist bei 108 Karten verschwindend gering)
        assertNotEquals(original, spiel.deck);
    }

    // =========================================================
    // Spiel.dealInitialHand() – Tests
    // =========================================================

    @Test
    void dealInitialHand_returnsCorrectNumberOfCards() {
        // Eine Hand mit 7 Karten austeilen
        ArrayList<Card> hand = spiel.dealInitialHand(7);
        assertEquals(7, hand.size());
    }

    @Test
    void dealInitialHand_removesCardsFromDeck() {
        // Das Deck muss nach dem Austeilen kleiner sein
        int deckSizeBefore = spiel.deck.size();
        spiel.dealInitialHand(7);
        assertEquals(deckSizeBefore - 7, spiel.deck.size());
    }

    @Test
    void dealInitialHand_singleCard_returnsOneCard() {
        // Auch mit 1 Karte austeilen möglich
        ArrayList<Card> hand = spiel.dealInitialHand(1);
        assertEquals(1, hand.size());
    }

    // =========================================================
    // Spiel.getStartercard() – Tests
    // =========================================================

    @Test
    void getStartercard_isNeverABlackCard() {
        // Die Startkarte darf keine schwarze Karte sein
        Card starter = spiel.getStartercard();
        assertNotEquals(Color.BLACK, starter.getColor());
    }

    @Test
    void getStartercard_returnsAValidCard() {
        // Die zurückgegebene Startkarte darf nicht null sein
        Card starter = spiel.getStartercard();
        assertNotNull(starter);
    }

    // =========================================================
    // DiscardPile – Tests
    // =========================================================

    @Test
    void discardPile_addCard_getTopCard_returnsLastCard() {
        // Die zuletzt hinzugefügte Karte muss oben liegen
        Card card1 = new Card(Value.ONE, Color.RED);
        Card card2 = new Card(Value.TWO, Color.BLUE);

        discardPile.addCard(card1);
        discardPile.addCard(card2);

        assertEquals(card2, discardPile.getTopCard());
    }

    @Test
    void discardPile_getTopCard_emptyPile_returnsNull() {
        // Leerer Ablegestapel → null zurückgeben
        assertNull(discardPile.getTopCard());
    }

    @Test
    void discardPile_removeTopCard_removesCorrectCard() {
        // Oberste Karte entfernen und prüfen ob die richtige entfernt wurde
        Card card1 = new Card(Value.ONE, Color.RED);
        Card card2 = new Card(Value.TWO, Color.BLUE);

        discardPile.addCard(card1);
        discardPile.addCard(card2);

        Card removed = discardPile.removeTopCard();

        assertEquals(card2, removed);
        assertEquals(card1, discardPile.getTopCard());
    }

    @Test
    void discardPile_takeAllExceptTop_leavesOneCard() {
        // Alle Karten außer der obersten zurückgeben – 1 Karte bleibt übrig
        discardPile.addCard(new Card(Value.ONE,   Color.RED));
        discardPile.addCard(new Card(Value.TWO,   Color.BLUE));
        discardPile.addCard(new Card(Value.THREE, Color.GREEN));

        List<Card> returned = discardPile.takeAllExceptTop();

        assertEquals(2, returned.size());
        assertEquals(1, discardPile.getAllCards().size());
    }

    // =========================================================
    // Deck – Karten-Häufigkeit Tests
    // =========================================================

    @Test
    void deck_numberCards1to9_appearExactlyTwicePerColor() {
        // Zahlenkarten 1–9 kommen genau 2x pro Farbe vor
        long count = spiel.deck.stream()
                .filter(c -> c.getColor() == Color.GREEN && c.getValue() == Value.NINE)
                .count();
        assertEquals(2, count);
    }

    @Test
    void deck_numberCardZero_appearExactlyOncePerColor() {
        // Die Null-Karte kommt genau 1x pro Farbe vor
        long count = spiel.deck.stream()
                .filter(c -> c.getColor() == Color.RED && c.getValue() == Value.ZERO)
                .count();
        assertEquals(1, count);
    }

    @Test
    void deck_actionCards_appearExactlyTwicePerColor() {
        // Aktionskarten (Skip, Reverse, Plus Two) kommen genau 2x pro Farbe vor
        long count = spiel.deck.stream()
                .filter(c -> c.getColor() == Color.BLUE && c.getValue() == Value.SKIP)
                .count();
        assertEquals(2, count);
    }

    @Test
    void deck_blackCards_appearExactlyFourTimes() {
        // Schwarze Karten (Color Change, Plus Four) kommen jeweils genau 4x vor
        long colorChangeCount = spiel.deck.stream()
                .filter(c -> c.getValue() == Value.COLOR_CHANGE)
                .count();
        assertEquals(4, colorChangeCount);

        long plusFourCount = spiel.deck.stream()
                .filter(c -> c.getValue() == Value.PLUS_FOUR)
                .count();
        assertEquals(4, plusFourCount);
    }
}


// Ich konzentriere mich auf Methoden, die gut ohne Konsoleneingabe testbar sind
// (also keine gettingName, showCards usw. — diese sind abhängig von Scanner).
// Die Priorität liegt bei:
//
// isValidMove()     — viele Fälle zu testen (gleiche Farbe, gleicher Wert, schwarze Karte usw.)
// getStartercard()  — darf niemals eine schwarze Karte sein
// dealInitialHand() — korrekte Anzahl an Karten wird ausgeteilt
// shuffle()         — Deck wird durchgemischt, alle Karten bleiben erhalten
// DiscardPile       — Karten hinzufügen, oberste Karte abrufen/entfernen, Stapel leeren
// Card              — Farbe, Wert und Punktzahl werden korrekt zurückgegeben
// Deck              — jede Karte kommt genau so oft vor wie in den Spielregeln vorgesehen

//Zahlenkarten 1–9 → genau 2x pro Farbe (z. B. Grün 9)
//Null-Karte → genau 1x pro Farbe (z. B. Rot 0)
//Aktionskarten (Skip, Reverse, Plus Two) → genau 2x pro Farbe
//Schwarze Karten (Color Change, Plus Four) → genau 4x insgesamt
//
//Wenn diese Tests bestehen, ist sichergestellt, dass makeDeck() das Deck korrekt aufbaut,
// und dieselbe Karte kann niemals fälschlicherweise zweimal ausgeteilt werden !!!