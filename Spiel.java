import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Spiel {

    List<Card> deck = Deck.makeDeck();
    private Card topCard;
    DiscardPile discardPile;

    public Spiel(DiscardPile discardPile) {
        this.discardPile = discardPile;
    }

    public static void initGame() {
        DiscardPile discardPile = new DiscardPile();
        Spiel uno = new Spiel(discardPile);

        uno.shuffle();

        Player[] playerListe = new Player[4];

        List<String> vorhandeneNamen = new ArrayList<>();

        for (int i = 0; i < 4; i++) {

            playerListe[i] = new Player(discardPile);

            String name = playerListe[i].gettingName(vorhandeneNamen);

            playerListe[i].setName(name);

            vorhandeneNamen.add(name);
        }

        // Karten einmal verteilen
        for (int i = 0; i < 4; i++) {
            playerListe[i].setHand(uno.dealInitialHand(7));
        }

        // Startspieler
        int start = playerListe[0].randomizePlayer();

        while (true) {

            Player aktuellerPlayer = playerListe[(start++) % 4];

            Card topCard = uno.getStartercard();

            Menu.setSpieler(aktuellerPlayer);
            Menu.setSpiel(uno);

            // Menu.showMenu();
            Menu.runMenu();
        }
    }

    public void displayCards() {
        for (Card i : deck) {
            System.out.println(i);
        }
    }

    public void shuffle() {
        Random rand = new Random();

        // Länge der ArrayListe -1
        for (int i = deck.size() - 1; i > 0; i--) {

            int randIndex = rand.nextInt(i + 1);

            // Karte an Position i merken
            Card currentCard = deck.get(i);

            // Zufällige Karte nach Position i setzen
            deck.set(i, deck.get(randIndex));

            // Gemerkte Karte an die zufällige Position setzen
            deck.set(randIndex, currentCard);
        }
    }

//    // Erstellt zum Testen der Ausgabe, ob Karten richtig gemischt werden
//    public void alleKartenAnzeigen() {
//        for (Card c : deck) {
//            c.print();
//        }
//    }


    public ArrayList<Card> dealInitialHand(int anzahl) {

        // Erstellen unserer Hand, die noch eine Karten hat
        ArrayList<Card> hand = new ArrayList<>();

        // Schleife die 7 mal durchgeht, damit wir unsere Karten in die Hand bekommen
        for (int i = 0; i < anzahl; i++) {
            // Hinzufügen einer Karte in unsere Hand (Entfernen einer Karte vom Deck [nimmt von ganz oben])
            hand.add(deck.remove(0));
        }

        // Zurückgeben der ArrayListe Hand, damit wir die Karten zeigen können
        return hand;
    }

    // NOCH NICHT FERTIG: Methode, wo Karten im Laufe des Spiels gezogen werden
    public void drawCards(int amount) {
        // Karten vom Ablegestapel mischen und hinzufügen, wenn Deck weniger als 4 Karten hat
        if (deck.size() < 4) {

            List<Card> recycled = discardPile.takeAllExceptTop();

            deck.addAll(recycled);

            shuffle();
        }
    }

    public void setTopCard(Card card) {
        this.topCard = card;
    }

    public Card getTopCard() {
        return topCard;
    }

    public Card getStartercard() {
        do {
           // shuffle(); // mischt DEIN aktuelles Deck
            topCard = dealInitialHand(1).get(0);
        } while (topCard.color == Color.BLACK);

        System.out.println("Startkarte ist: " + topCard);
        return topCard;
    }




}

// Zu Beginn des Spiels – alle Spieler-Objekte erstellen
// Player anna  = new Player(discardPile);
// Player ben   = new Player(discardPile);
// Player clara = new Player(discardPile);
//
// anna.setName("Anna");
// ben.setName("Ben");
// clara.setName("Clara");
//
// ScoreCalculator mit Spieler-Objekten starten
// List<Player> allPlayers = List.of(anna, ben, clara);
// ScoreCalculator calculator = new ScoreCalculator(allPlayers);
//
// Nach jeder Runde – Sieger und Verlierer als Spieler-Objekte übergeben
// List<Player> losers = List.of(ben, clara);
// calculator.settleRound(anna, losers);
// calculator.displayScoreboard();
//
// Prüfen ob das Spiel vorbei ist
// String winner = calculator.determineGameWinner();
// if (winner != null) {
//     System.out.println("Spielende! Gesamtsieger: " + winner);
// }
