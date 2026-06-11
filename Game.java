import java.util.*;

public class Game {

    // Deck mit allen Karten
    List<Card> deck = Deck.makeDeck();

    // aktuelle oberste Karte am Ablagestapel
    private Card topCard;

    // Ablagestapel
    DiscardPile discardPile;

    // Spielrichtung (1 = normal, -1 = rückwärts)
    private static int direction = 1;

    // Konstruktor: verbindet Deck mit Ablagestapel
    public Game(DiscardPile discardPile) {
        this.discardPile = discardPile;
    }

    // Spieler wird begrüßt und wählt aus, ob die Hilfe angezeigt werden soll
    // initGame wird gestartet
    public static void startScreen(){
        System.out.println("*******Willkommen bei UNO!*******");
        System.out.println("Willst du die Hilfe sehen? (j/n)");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        if (input.equals("n")){
            initGame();
        } else if  (input.equals("j")){
            Help.showRulesInFile();
            initGame();
        }
    }

    // Startet das komplette Spiel
    public static void initGame() {

        // neuen Ablagestapel erstellen
        DiscardPile discardPile = new DiscardPile();

        // Spielinstanz erstellen
        Game uno = new Game(discardPile);

        // Spieler erstellen
        Player[] playerListe = createPlayers(uno, discardPile);

        // Datenbank vorbereiten und Punkte zurücksetzen
        GameDatabase db = new GameDatabase();
        db.resetScores();

        // Punktestand initialisieren
        Map<String, Integer> totalScores = initializeScores(playerListe);

        int roundNumber = 1;

        // Hauptspielschleife (läuft bis jemand 500 Punkte erreicht)
        while (true) {

            System.out.println("\n=== Runde " + roundNumber + " beginnt! ===");

            // neue Runde vorbereiten (Deck + Karten)
            prepareRound(uno, playerListe);

            // Startspieler bestimmen
            int start = determineStartingPlayer(playerListe);

            // Runde spielen und Gewinner erhalten
            Player winner = playRound(uno, playerListe);

            // Punkte der Runde berechnen
            int roundPoints = calculateRoundPoints(playerListe, winner);

            // Punkte zum Gesamtscore hinzufügen
            updateScores(totalScores, winner, roundPoints);

            // Scores in Datenbank speichern
            saveScores(db, playerListe, totalScores);

            // Punktestand ausgeben
            printScoreboard(playerListe, totalScores, roundNumber);

            // Prüfen ob jemand gewonnen hat
            if (totalScores.get(winner.getName()) >= 500) {

                System.out.println("\n=== Spielende! "
                        + winner.getName()
                        + " hat das Spiel mit "
                        + totalScores.get(winner.getName())
                        + " Punkten gewonnen! ===");

                db.displayFinalResults();
                return;
            }

            roundNumber++;
        }
    }

    // erstellt alle Spieler
    private static Player[] createPlayers(Game uno, DiscardPile discardPile) {

        Player[] playerListe = new Player[4];

        // Liste für eindeutige Namen
        List<String> existingNames = new ArrayList<>();

        for (int i = 0; i < 4; i++) {

            // Spieler erstellen
            playerListe[i] = new Player(uno, discardPile);

            // Namen abfragen (einzigartig)
            String name = playerListe[i].gettingName(existingNames);

            playerListe[i].setName(name);

            existingNames.add(name);
        }

        return playerListe;
    }

    // erstellt Score-Map (alle Spieler starten mit 0 Punkten)
    private static Map<String, Integer> initializeScores(Player[] playerListe) {

        Map<String, Integer> scores = new HashMap<>();

        for (Player player : playerListe) {
            scores.put(player.getName(), 0);
        }

        return scores;
    }

    // neue Runde vorbereiten (Deck mischen + Karten verteilen)
    private static void prepareRound(Game uno, Player[] playerListe) {

        // neues Deck erstellen
        uno.deck = Deck.makeDeck();

        // mischen
        uno.shuffle();

        // jedem Spieler 7 Karten geben
        for (Player player : playerListe) {
            player.setHand(uno.dealInitialHand(7));
        }

        uno.getStartercard();
    }

    // Startspieler bestimmen
    private static int determineStartingPlayer(Player[] playerListe) {
        return playerListe[0].randomizePlayer();
    }

    // verarbeitet Spezialeffekte einer Karte
    private static int handleCardEffect(
            Card card,
            Player[] playerListe,
            Game uno,
            int currentPlayer) {

        // Sicherheitscheck (falls keine Karte gespielt wurde)
        if (card == null) {
            return currentPlayer;
        }

        // berechnet nächsten Spieler
        int nextPlayer = (currentPlayer + direction + 4) % 4;

        // falls Farbwahl-Karte gespielt wurde
        if (card.value == Value.COLOR_CHANGE || card.value == Value.PLUS_FOUR) {
            char chosenColor = cardValue(card);

            card.setChosenColor(chosenColor);

            System.out.println("Neue Farbe: " + colorName(chosenColor));
        }

        switch (card.value) {

            case PLUS_TWO:
                // nächster Spieler zieht 2 Karten
                System.out.println(playerListe[nextPlayer].getName() + " zieht 2 Karten!");
                uno.drawOneCard(playerListe[nextPlayer]);
                uno.drawOneCard(playerListe[nextPlayer]);
                return (nextPlayer + direction + 4) % 4;

            case PLUS_FOUR:
                // nächster Spieler zieht 4 Karten
                System.out.println(playerListe[nextPlayer].getName() + " zieht 4 Karten!");
                for (int i = 0; i < 4; i++) {
                    uno.drawOneCard(playerListe[nextPlayer]);
                }
                return (nextPlayer + direction + 4) % 4;

            case SKIP:
                // Spieler wird übersprungen
                System.out.println("Spieler wird übersprungen!");
                return (nextPlayer + direction + 4) % 4;

            case REVERSE:
                // Spielrichtung wird geändert
                System.out.println("Richtung geändert!");
                direction *= -1;
                return (currentPlayer + direction + 4) % 4;

            default:
                // normale Karte → einfach weiter
                return nextPlayer;
        }
    }

    // komplette Runde spielen
    private static Player playRound(Game uno, Player[] playerListe) {
        // zufälliger Startspieler
        int start = playerListe[0].randomizePlayer();

        while (true) {
            Player current = playerListe[start];

            // Menü anzeigen + Spielzug durchführen
            Menu menu = new Menu(uno, current);
            Card played = menu.runMenu();

            // falls keine Karte gespielt wurde
            if (played == null) {
                start = (start + direction + 4) % 4;
                continue;
            }

            // gespielte Karte auf Ablagestapel setzen
            uno.setTopCard(played);

            // Spezialeffekte der Karte ausführen
            start = handleCardEffect(played, playerListe, uno, start);

            // Gewinnerprüfung
            if (current.getHand().isEmpty()) {
                return current;
            }
        }
    }

    // Punkte einer Runde berechnen
    private static int calculateRoundPoints(
            Player[] playerListe,
            Player winner) {

        int points = 0;

        for (Player player : playerListe) {

            // nur Verlierer zählen
            if (!player.equals(winner)) {

                for (Card card : player.getHand()) {
                    points += card.getPointValue();
                }
            }
        }

        return points;
    }

    // Punkte zum Gesamtscore hinzufügen
    private static void updateScores(
            Map<String, Integer> scores,
            Player winner,
            int points) {

        scores.put(
                winner.getName(),
                scores.get(winner.getName()) + points);
    }

    // Scores in Datenbank speichern
    private static void saveScores(
            GameDatabase db,
            Player[] playerListe,
            Map<String, Integer> scores) {

        db.resetScores();

        for (Player player : playerListe) {
            db.saveFinalScore(
                    player.getName(),
                    scores.get(player.getName()));
        }
    }

    // Punktestand anzeigen
    private static void printScoreboard(
            Player[] playerListe,
            Map<String, Integer> scores,
            int roundNumber) {

        System.out.println("\n=== Punktestand nach Runde " + roundNumber + " ===");

        for (Player player : playerListe) {

            System.out.println(
                    player.getName()
                            + ": "
                            + scores.get(player.getName())
                            + " Punkte");
        }
    }

    // Deck ausgeben (Debug)
    public void displayCards() {
        for (Card i : deck) {
            System.out.println(i);
        }
    }

    // Deck mischen (Fisher-Yates Shuffle)
    public void shuffle() {

        Random rand = new Random();

        for (int i = deck.size() - 1; i > 0; i--) {

            int randIndex = rand.nextInt(i + 1);

            Card currentCard = deck.get(i);

            deck.set(i, deck.get(randIndex));

            deck.set(randIndex, currentCard);
        }
    }

    // Karten austeilen (Starthand)
    public ArrayList<Card> dealInitialHand(int anzahl) {

        ArrayList<Card> newHand = new ArrayList<>();

        for (int i = 0; i < anzahl; i++) {

            // falls Deck leer → Karten zurückholen
            if (deck.isEmpty()) {

                List<Card> recycled = discardPile.takeAllExceptTop();
                deck.addAll(recycled);
                shuffle();
            }

            newHand.add(deck.removeFirst());
        }

        return newHand;
    }

    // eine Karte ziehen
    public void drawOneCard(Player player) {

        if (deck.size() < 4) {

            List<Card> recycled = discardPile.takeAllExceptTop();
            deck.addAll(recycled);
            shuffle();
        }

        player.getHand().add(deck.removeFirst());
    }

    // oberste Karte setzen
    public void setTopCard(Card card) {
        this.topCard = card;
    }

    // oberste Karte holen
    public Card getTopCard() {
        return topCard;
    }

    // Startkarte ziehen (keine BLACK Karte erlaubt)
    public Card getStartercard() {

        do {

            if (deck.isEmpty()) {
                List<Card> recycled = discardPile.takeAllExceptTop();
                deck.addAll(recycled);
                shuffle();
            }

            topCard = deck.removeFirst();

        } while (topCard.color == Color.BLACK);

        discardPile.addCard(topCard);

        System.out.println("Startkarte ist: " + topCard);

        return topCard;
    }

    // bestimmt Effekt/Farbe einer Karte
    public static char cardValue(Card topCard) {

        Scanner input = new Scanner(System.in);

        switch (topCard.value) {

            case COLOR_CHANGE:

                System.out.println("Welche Farbe wünschst du dir?");
                return input.next().toLowerCase().charAt(0);

            case PLUS_FOUR:

                System.out.println("+4 Karten!");
                System.out.println("Welche Farbe wünschst du dir?");
                return input.next().toLowerCase().charAt(0);

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

    // wandelt Farbcode in Text um
    private static String colorName(char color) {
        return switch (color) {
            case 'r' -> "Rot";
            case 'g' -> "Grün";
            case 'b' -> "Blau";
            case 'y' -> "Gelb";
            default -> "Unbekannt";
        };
    }
}