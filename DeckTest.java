import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class DeckTest {

    private List<Card> deck;

    // Wird vor jedem Test ausgeführt – erstellt ein neues Deck
    @BeforeEach
    void setUp() {
        deck = Deck.makeDeck();
    }

    // -------------------------------------------------------
    // Allgemeine Tests
    // -------------------------------------------------------

    // Überprüft, ob das Deck nicht null ist
    @Test
    void testDeckIsNotNull() {
        assertNotNull(deck);
    }

    // Überprüft die Gesamtanzahl der Karten im Deck
    // 4 Farben × (1×ZERO + 2×SKIP + 2×REVERSE + 2×PLUS_TWO + 2×ONE bis NINE)
    // = 4 × 25 = 100 farbige Karten
    // + BLACK: 4×COLOR_CHANGE + 4×PLUS_FOUR = 8 schwarze Karten
    // Gesamt = 108 Karten
    @Test
    void testDeckTotalSize() {
        assertEquals(108, deck.size());
    }

    // -------------------------------------------------------
    // Tests für schwarze Karten
    // -------------------------------------------------------

    // Überprüft, ob genau 4 COLOR_CHANGE-Karten im Deck vorhanden sind
    @Test
    void testBlackColorChangeCount() {
        long count = deck.stream()
                .filter(c -> c.getColor() == Color.BLACK && c.getValue() == Value.COLOR_CHANGE)
                .count();
        assertEquals(4, count);
    }

    // Überprüft, ob genau 4 PLUS_FOUR-Karten im Deck vorhanden sind
    @Test
    void testBlackPlusFourCount() {
        long count = deck.stream()
                .filter(c -> c.getColor() == Color.BLACK && c.getValue() == Value.PLUS_FOUR)
                .count();
        assertEquals(4, count);
    }

    // Überprüft, ob keine anderen schwarzen Karten vorhanden sind
    // Erwartet: nur 4×COLOR_CHANGE + 4×PLUS_FOUR = 8 schwarze Karten insgesamt
    @Test
    void testNoOtherBlackCards() {
        long count = deck.stream()
                .filter(c -> c.getColor() == Color.BLACK)
                .count();
        assertEquals(8, count);
    }

    // -------------------------------------------------------
    // Tests für farbige Karten: ZERO kommt 1× pro Farbe vor
    // -------------------------------------------------------

    // Überprüft, ob jede Farbe genau eine ZERO-Karte enthält
    @Test
    void testZeroCardCountPerColor() {
        for (Color c : Color.values()) {
            if (c == Color.BLACK) continue; // schwarze Farbe überspringen
            long count = deck.stream()
                    .filter(card -> card.getColor() == c && card.getValue() == Value.ZERO)
                    .count();
            assertEquals(1, count, "ZERO für Farbe " + c + " muss genau 1× vorkommen");
        }
    }

    // -------------------------------------------------------
    // Tests für farbige Karten: Aktionskarten kommen 2× pro Farbe vor
    // -------------------------------------------------------

    // Überprüft, ob jede Farbe genau 2 SKIP-Karten enthält
    @Test
    void testSkipCardCountPerColor() {
        for (Color c : Color.values()) {
            if (c == Color.BLACK) continue;
            long count = deck.stream()
                    .filter(card -> card.getColor() == c && card.getValue() == Value.SKIP)
                    .count();
            assertEquals(2, count, "SKIP für Farbe " + c + " muss 2× vorkommen");
        }
    }

    // Überprüft, ob jede Farbe genau 2 REVERSE-Karten enthält
    @Test
    void testReverseCardCountPerColor() {
        for (Color c : Color.values()) {
            if (c == Color.BLACK) continue;
            long count = deck.stream()
                    .filter(card -> card.getColor() == c && card.getValue() == Value.REVERSE)
                    .count();
            assertEquals(2, count, "REVERSE für Farbe " + c + " muss 2× vorkommen");
        }
    }

    // Überprüft, ob jede Farbe genau 2 PLUS_TWO-Karten enthält
    @Test
    void testPlusTwoCardCountPerColor() {
        for (Color c : Color.values()) {
            if (c == Color.BLACK) continue;
            long count = deck.stream()
                    .filter(card -> card.getColor() == c && card.getValue() == Value.PLUS_TWO)
                    .count();
            assertEquals(2, count, "PLUS_TWO für Farbe " + c + " muss 2× vorkommen");
        }
    }

    // -------------------------------------------------------
    // Tests für farbige Karten: Zahlenkarten 1–9 kommen 2× pro Farbe vor
    // -------------------------------------------------------

    // Überprüft, ob jede Zahlenkarte (ONE bis NINE) 2× pro Farbe vorkommt
    @Test
    void testNumberCardCountPerColor() {
        Value[] numbers = {
                Value.ONE, Value.TWO, Value.THREE, Value.FOUR, Value.FIVE,
                Value.SIX, Value.SEVEN, Value.EIGHT, Value.NINE
        };

        for (Color c : Color.values()) {
            if (c == Color.BLACK) continue; // BLACK hat keine Zahlenkarten
            for (Value v : numbers) {
                long count = deck.stream()
                        .filter(card -> card.getColor() == c && card.getValue() == v)
                        .count();
                assertEquals(2, count, v + " für Farbe " + c + " muss 2× vorkommen");
            }
        }
    }
}