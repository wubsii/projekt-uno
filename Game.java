import java.util.*;

public class Game {

    List<Card> deck = Deck.makeDeck();
    private Card topCard;
    DiscardPile discardPile;
    private static int direction = 1;
    private static Menu menu;


    public Game(DiscardPile discardPile) {
        this.discardPile = discardPile;
    }

    public static void initGame() {

        DiscardPile discardPile = new DiscardPile();
        Game uno = new Game(discardPile);

        uno.shuffle();

        Player[] playerListe = new Player[4];

        List<String> existingNames = new ArrayList<>();

        for (int i = 0; i < 4; i++) {

            playerListe[i] = new Player(discardPile);
            String name = playerListe[i].gettingName(existingNames);
            playerListe[i].setName(name);
            existingNames.add(name);
        }

        // Karten verteilen
        for (int i = 0; i < 4; i++) {
            playerListe[i].setHand(uno.dealInitialHand(7));
        }

        // Startspieler
        int start = playerListe[0].randomizePlayer();

        // Startkarte
        uno.getStartercard();
        Card startCard = uno.getTopCard();

        switch (startCard.value) {

            case PLUS_TWO:

                System.out.println(
                        playerListe[start].getName()
                                + " zieht 2 Karten!");

                playerListe[start]
                        .getHand()
                        .addAll(uno.dealInitialHand(2));

                break;

            case SKIP:

                System.out.println(
                        playerListe[start].getName()
                                + " wird übersprungen!");

                start = (start + 1) % 4;

                break;

            case REVERSE:

                direction *= -1;

                break;
        }

        while (true) {

            Player aktuellerPlayer = playerListe[start];

            Menu menu = new Menu(uno, aktuellerPlayer);
            menu.runMenu();

            if (aktuellerPlayer.getHand().isEmpty()) {

                System.out.println(
                        aktuellerPlayer.getName() + " hat das Spiel gewonnen!");

                // Datenbankverbindung oeffnen und Tabelle erstellen falls noetig
                GameDatabase db = new GameDatabase();

                // Endpunktestand jedes Spielers in der Datenbank speichern
                for (Player player : playerListe) {
                    int score = 0;
                    for (Card card : player.getHand()) {
                        score += card.getPointValue();
                    }
                    db.saveFinalScore(player.getName(), score);
                }

                // Alle gespeicherten Endergebnisse in der Konsole anzeigen
                db.displayFinalResults();

                break;
            }

            Card topCard = uno.getTopCard();

            if (topCard.value == Value.COLOR_CHANGE ||
                    topCard.value == Value.PLUS_FOUR) {

                char chosenColor = cardValue(topCard);
                topCard.setChosenColor(chosenColor);
            }

            switch (topCard.value) {

                case REVERSE:
                    System.out.println("Richtung geändert!");
                    direction *= -1;
                    start = (start + direction + 4) % 4;
                    break;

                case SKIP:
                    System.out.println("Spieler wird übersprungen!");
                    start = (start + direction + 4) % 4;
                    start = (start + direction + 4) % 4;
                    break;

                case PLUS_TWO:
                    start = (start + direction + 4) % 4;
                    System.out.println(playerListe[start].getName() + " zieht 2 Karten!");
                    playerListe[start].getHand().addAll(uno.dealInitialHand(2));
                    start = (start + direction + 4) % 4;
                    break;

                case PLUS_FOUR:
                    start = (start + direction + 4) % 4;
                    System.out.println(playerListe[start].getName() + " zieht 4 Karten!");
                    playerListe[start].getHand().addAll(uno.dealInitialHand(4));
                    start = (start + direction + 4) % 4;
                    break;

                default:
                    start = (start + direction + 4) % 4;
            }
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
            if (deck.isEmpty()) {

                List<Card> recycled =
                        discardPile.takeAllExceptTop();

                deck.addAll(recycled);

                shuffle();
            }
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


    public static char cardValue(Card topCard) {

        Scanner input = new Scanner(System.in);

        switch (topCard.value) {

            case COLOR_CHANGE:

                System.out.println("""
                    Welche Farbe wünschst du dir?
                    r = Rot
                    g = Grün
                    b = Blau
                    y = Gelb
                    """);

                return input.next()
                        .toLowerCase()
                        .charAt(0);

            case PLUS_FOUR:

                System.out.println("+4 Karten!");

                System.out.println("""
                    Welche Farbe wünschst du dir?
                    r = Rot
                    g = Grün
                    b = Blau
                    y = Gelb
                    """);

                return input.next()
                        .toLowerCase()
                        .charAt(0);

            case PLUS_TWO:

                System.out.println("+2 Karten!");

                return 'T';

            case REVERSE:

                System.out.println("Richtungswechsel!");

                return 'R';

            case SKIP:

                System.out.println("Spieler wird übersprungen!");

                return 'S';

            default:

                return '0';
        }
    }
}