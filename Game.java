import java.util.*;

public class Game {

    List<Card> deck = Deck.makeDeck();
    private Card topCard;
    DiscardPile discardPile;
    private static int direction = 1;
    private static Menu menu;
    private static Help help;
    // Erstellen unserer Hand, die noch eine Karten hat
    ArrayList<Card> hand = new ArrayList<>();

    public Game(DiscardPile discardPile) {
        this.discardPile = discardPile;
    }

    public static void initGame() {

        DiscardPile discardPile = new DiscardPile();
        Game uno = new Game(discardPile);

        uno.shuffle();

        Player[] playerListe = new Player[4];
        List<String> existingNames = new ArrayList<>();

        // Spielernamen einmalig zu Beginn eingeben
        for (int i = 0; i < 4; i++) {
            playerListe[i] = new Player(uno, discardPile);
            String name = playerListe[i].gettingName(existingNames);
            playerListe[i].setName(name);
            existingNames.add(name);
        }

        // Datenbankverbindung oeffnen und Tabelle erstellen falls noetig
        GameDatabase db = new GameDatabase();

        // Punktestand zuruecksetzen – wird bei jedem neuen Spielstart aufgerufen
        db.resetScores();

        // Gesamtpunktestand aller Spieler (Name → Punkte)
        Map<String, Integer> totalScores = new HashMap<>();
        for (Player player : playerListe) {
            totalScores.put(player.getName(), 0);
        }

        int roundNumber = 1;

        // Aeussere Schleife – laeuft bis jemand 500 Punkte hat
        while (true) {

            System.out.println("\n=== Runde " + roundNumber + " beginnt! ===");

            // Neues Deck erstellen und mischen
            uno.deck = Deck.makeDeck();
            uno.shuffle();

            // Karten verteilen
            for (int i = 0; i < 4; i++) {
                playerListe[i].setHand(uno.dealInitialHand(7));
            }

            // Startspieler zufaellig bestimmen
            int start = playerListe[0].randomizePlayer();

            // Startkarte ziehen
            uno.getStartercard();
            Card startCard = uno.getTopCard();

            // Startkarte auswerten und Spezialeffekte anwenden
            switch (startCard.value) {

                case PLUS_TWO:
                    // Startspieler muss 2 Karten ziehen
                    System.out.println(playerListe[start].getName() + " zieht 2 Karten!");

                    uno.drawOneCard(playerListe[start]);
                    uno.drawOneCard(playerListe[start]);
                    break;

                case SKIP:
                    // Startspieler wird übersprungen → nächster Spieler ist dran
                    System.out.println(playerListe[start].getName() + " wird übersprungen!");

                    start = (start + 1) % 4;
                    break;

                case REVERSE:
                    // Spielrichtung wird umgedreht
                    direction *= -1;
                    break;
            }

            // Innere Schleife – eine Runde spielen
            while (true) {

                Player aktuellerPlayer = playerListe[start];

                Menu menu = new Menu(uno, aktuellerPlayer);
                menu.runMenu();

                // Pruefen ob der aktuelle Spieler die Runde gewonnen hat
                if (aktuellerPlayer.getHand().isEmpty()) {

                    // Rundensieger ausgeben
                    System.out.println("\n" + aktuellerPlayer.getName()
                            + " hat die Runde gewonnen!");

                    // Punkte der Verlierer berechnen und dem Sieger gutschreiben
                    int roundPoints = 0;
                    for (Player player : playerListe) {
                        if (!player.equals(aktuellerPlayer)) {
                            for (Card card : player.getHand()) {
                                roundPoints += card.getPointValue();
                            }
                        }
                    }

                    // Gesamtpunktestand des Siegers aktualisieren
                    int newScore = totalScores.get(aktuellerPlayer.getName()) + roundPoints;
                    totalScores.put(aktuellerPlayer.getName(), newScore);

                    // Tabelle leeren und aktuelle Punktestaende speichern
                    db.resetScores();
                    for (Player player : playerListe) {
                        db.saveFinalScore(player.getName(), totalScores.get(player.getName()));
                    }

                    // Punktestand nach der Runde anzeigen
                    System.out.println("\n=== Punktestand nach Runde " + roundNumber + " ===");
                    for (Player player : playerListe) {
                        System.out.println(player.getName() + ": "
                                + totalScores.get(player.getName()) + " Punkte");
                    }

                    // Pruefen ob jemand 500 Punkte erreicht hat
                    if (newScore >= 500) {
                        System.out.println("\n=== Spielende! " + aktuellerPlayer.getName()
                                + " hat das Spiel mit " + newScore
                                + " Punkten gewonnen! ===");
                        db.displayFinalResults();
                        return;
                    }

                    // Naechste Runde ankuendigen
                    roundNumber++;
                    System.out.println("\n=== Runde " + roundNumber + " beginnt! ===");
                    break;
                }

                Card topCard = uno.getTopCard();

                // Falls eine Farbwechsel-Karte oder +4 Karte gespielt wurde, muss der Spieler eine neue Farbe auswählen
                if (topCard.value == Value.COLOR_CHANGE || topCard.value == Value.PLUS_FOUR) {
                    char chosenColor = cardValue(topCard);

                    // gewählte Farbe wird auf der Karte gespeichert
                    topCard.setChosenColor(chosenColor);

                    System.out.println("Neue Farbe ist: " + colorName(chosenColor));
                }

                switch (topCard.value) {

                    case REVERSE:
                        // Richtungswechsel: Spielrichtung wird umgedreht
                        System.out.println("Richtung geändert!");
                        direction *= -1;

                        // nächster Spieler nach Richtungswechsel berechnen
                        start = (start + direction + 4) % 4;
                        break;

                    case SKIP:
                        // aktueller Spieler wird übersprungen (2x weitergehen)
                        System.out.println("Spieler wird übersprungen!");

                        start = (start + direction + 4) % 4;
                        start = (start + direction + 4) % 4;
                        break;

                    case PLUS_TWO:
                        // nächster Spieler muss 2 Karten ziehen
                        start = (start + direction + 4) % 4;

                        System.out.println(playerListe[start].getName() + " zieht 2 Karten!");

                        uno.drawOneCard(playerListe[start]);
                        uno.drawOneCard(playerListe[start]);

                        // nach Strafkarten weiter zum nächsten Spieler
                        start = (start + direction + 4) % 4;
                        break;

                    case PLUS_FOUR:
                        // nächster Spieler muss 4 Karten ziehen
                        start = (start + direction + 4) % 4;

                        System.out.println(playerListe[start].getName() + " zieht 4 Karten!");

                        for (int i = 0; i < 4; i++) {
                            uno.drawOneCard(playerListe[start]);
                        }

                        // nach Strafkarten weiter zum nächsten Spieler
                        start = (start + direction + 4) % 4;
                        break;

                    default:
                        // normale Karte: einfach zum nächsten Spieler wechseln
                        start = (start + direction + 4) % 4;
                }
            }
        }
    }

    // Als Test, ob alle Karten angezeigt werden
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

    // Gibt einem Spieler zu Beginn eine feste Anzahl Karten
    public ArrayList<Card> dealInitialHand(int anzahl) {

        // Neue Hand für den Spieler erstellen
        ArrayList<Card> newHand = new ArrayList<>();

        // so viele Karten ziehen wie angegeben
        for (int i = 0; i < anzahl; i++) {

            // falls der Nachziehstapel leer ist → Ablagestapel recyceln
            if (deck.isEmpty()) {

                // alle Karten außer der obersten zurückholen
                List<Card> recycled = discardPile.takeAllExceptTop();

                // zurück ins Deck legen
                deck.addAll(recycled);

                // Deck neu mischen
                shuffle();
            }

            // oberste Karte vom Deck nehmen und in die Hand geben
            newHand.add(deck.remove(0));
        }

        // fertige Hand zurückgeben
        return newHand;
    }


    // Spieler zieht genau eine Karte vom Deck
    public void drawOneCard(Player player) {

        // wenn nur noch wenige Karten im Deck sind → auffüllen
        if (deck.size() < 4) {

            // Karten vom Ablagestapel zurückholen
            List<Card> recycled = discardPile.takeAllExceptTop();

            // ins Deck mischen
            deck.addAll(recycled);

            // neu mischen
            shuffle();
        }

        // oberste Karte ziehen und dem Spieler geben
        player.getHand().add(deck.remove(0));
    }


    // setzt die oberste Karte im Spiel (Ablagestapel-Top)
    public void setTopCard(Card card) {
        this.topCard = card;
    }


    // gibt die aktuelle oberste Karte zurück
    public Card getTopCard() {
        return topCard;
    }


    // zieht die Startkarte für das Spiel
    public Card getStartercard() {

        do {
            // wenn Deck leer ist → Karten recyceln
            if (deck.isEmpty()) {

                List<Card> recycled = discardPile.takeAllExceptTop();
                deck.addAll(recycled);
                shuffle();
            }

            // oberste Karte ziehen
            topCard = deck.remove(0);

            // solange ziehen bis keine BLACK Karte kommt
        } while (topCard.color == Color.BLACK);

        // Startkarte auf Ablagestapel legen
        discardPile.addCard(topCard);

        System.out.println("Startkarte ist: " + topCard);

        return topCard;
    }


    // bestimmt Farbe oder Effekt einer Spezialkarte
    public static char cardValue(Card topCard) {

        Scanner input = new Scanner(System.in);
        switch (topCard.value) {

            // Farbwechsel-Karte: Spieler wählt neue Farbe
            case COLOR_CHANGE:

                System.out.println("""
                        Welche Farbe wünschst du dir?
                        r = Rot
                        g = Grün
                        b = Blau
                        y = Gelb
                        """);

                return input.next().toLowerCase().charAt(0);

            // +4 Karte: Farbe wählen + Strafeffekt
            case PLUS_FOUR:

                System.out.println("+4 Karten!");

                System.out.println("""
                        Welche Farbe wünschst du dir?
                        r = Rot
                        g = Grün
                        b = Blau
                        y = Gelb
                        """);

                return input.next().toLowerCase().charAt(0);

            // +2 Karte: einfache Strafkarte
            case PLUS_TWO:

                System.out.println("+2 Karten!");
                return 'T';

            // Richtungswechsel
            case REVERSE:

                System.out.println("Richtungswechsel!");
                return 'R';

            // Spieler überspringen
            case SKIP:

                System.out.println("Spieler wird übersprungen!");
                return 'S';

            // Standardfall (keine Spezialkarte)
            default:
                return '0';
        }
    }


    // wandelt Farbcode in lesbaren Namen um
    private static String colorName(char color) {
        return switch (color) {

            case 'r' -> "Rot";
            case 'g' -> "Grün";
            case 'b' -> "Blau";
            case 'y' -> "Gelb";

            // falls unbekannter Wert kommt
            default -> "Unbekannt";
        };
    }
}