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
            playerListe[i] = new Player(uno, discardPile);
            String name = playerListe[i].gettingName(existingNames);
            playerListe[i].setName(name);
            existingNames.add(name);
            playerListe[i].setHand(uno.dealInitialHand(7));
        }

        // Startspieler
        int start = playerListe[0].randomizePlayer();

        // Startkarte
        Card startCard = uno.getStartercard(); // Fixed: Use getStartercard()

        switch (startCard.value) {
            case PLUS_TWO:
                System.out.println(playerListe[start].getName() + " zieht 2 Karten!");
                uno.drawOneCard(playerListe[start]);
                uno.drawOneCard(playerListe[start]);
                break;

            case SKIP:
                System.out.println(playerListe[start].getName() + " wird übersprungen!");
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
                System.out.println(aktuellerPlayer.getName() + " hat das Spiel gewonnen!");
                GameDatabase db = new GameDatabase();
                for (Player player : playerListe) {
                    int score = 0;
                    for (Card card : player.getHand()) {
                        score += card.getPointValue();
                    }
                    db.saveFinalScore(player.getName(), score);
                }
                db.displayFinalResults();
                break;
            }

            Card topCard = uno.getTopCard();

            if (topCard.value == Value.COLOR_CHANGE || topCard.value == Value.PLUS_FOUR) {
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
                    uno.drawOneCard(playerListe[start]);
                    uno.drawOneCard(playerListe[start]);
                    start = (start + direction + 4) % 4;
                    break;

                case PLUS_FOUR:
                    start = (start + direction + 4) % 4;
                    System.out.println(playerListe[start].getName() + " zieht 4 Karten!");
                    for (int i = 0; i < 4; i++) {
                        uno.drawOneCard(playerListe[start]);
                    }
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
            ArrayList<Card> newHand = new ArrayList<>();
            for (int i = 0; i < anzahl; i++) {
                if (deck.isEmpty()) {
                    List<Card> recycled = discardPile.takeAllExceptTop();
                    deck.addAll(recycled);
                    shuffle();
                }
                newHand.add(deck.remove(0));
            }
            return newHand;
        }


public void drawOneCard(Player player) {
    if (deck.size() < 4) {
        List<Card> recycled = discardPile.takeAllExceptTop();
        deck.addAll(recycled);
        shuffle();
    }
    // Add the card to the player's hand
    player.getHand().add(deck.remove(0));
}

    public void setTopCard(Card card) {
        this.topCard = card;
    }

    public Card getTopCard() {
        return topCard;
    }

    public Card getStartercard() {
        do {
            if (deck.isEmpty()) {
                List<Card> recycled = discardPile.takeAllExceptTop();
                deck.addAll(recycled);
                shuffle();
            }
            topCard = deck.remove(0);
        } while (topCard.color == Color.BLACK);

        discardPile.addCard(topCard); // Add the starter card to the discard pile
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