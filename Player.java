import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Player {

    // Stellt einen Spieler im UNO-Spiel dar.
    // Verwaltet Name, Handkarten und Spielaktionen wie Karten ausspielen oder UNO rufen.
    Scanner input = new Scanner(System.in);
    String name;
    private final DiscardPile discardPile;
    ArrayList<Card> hand = new ArrayList<>();
    private final Game game;

    // Konstruktor
    public Player(Game game, DiscardPile discardPile) {
        this.game = game;
        this.discardPile = discardPile;
    }

    // Fordert den Spielernamen an und validiert ihn.
    public String gettingName(List<String> vorhandeneNamen) {
        while (true) {
            System.out.print("Wie heißt der Spieler? ");
            String eingabe = input.nextLine().trim();

            // Prüfen auf leeren Namen
            if (eingabe.isEmpty()) {
                System.out.println("Der Name darf nicht leer sein.");
                continue;
            }
            // Prüfen auf ungültige Zeichen
            // Erlaubt nur Buchstaben und Leerzeichen
            if (!eingabe.matches("[a-zA-ZäöüÄÖÜß ]+")) {
                System.out.println("Der Name enthält ungültige Zeichen.");
                continue;
            }
            // Prüfen auf doppelte Namen
            if (vorhandeneNamen.contains(eingabe)) {
                System.out.println("Dieser Name ist bereits vergeben.");
                continue;
            }
            // Name gültig
            this.name = eingabe;
            return this.name;
        }
    }

    // Testmethode: Gibt alle Spielernamen aus.
    public void showNames(String[] playerName) {
        // Namen ausgeben
        System.out.println("Spieler: ");
        for (String name : playerName) {
            System.out.println(name);
        }
    }

    // Testmethode: Gibt die Spielerreihenfolge ab einem Startindex zurück.
    public String[] showOrder(String[] playerName, int start) {
        String[] neueReihenfolge = new String[4];
        for (int i = 0; i < 4; i++) {
            neueReihenfolge[i] = playerName[(start + i) % 4];
        }
        return neueReihenfolge;
    }

    // Gibt einen zufälligen Spielerindex zurück.
    public int randomizePlayer() {
        // generieren eines random index
        Random rand = new Random();
        return rand.nextInt(4);
    }

    // Zeigt die Handkarten des Spielers nach Bestätigung an.
    public void showCards() {
        System.out.println(name + " ist jetzt an der Reihe");
        System.out.print("Bitte bestätigen (j/n), damit nur du die Karten siehst: ");

        char showMe = input.next().charAt(0);

        if (showMe == 'j' || showMe == 'J') {
            System.out.println("-----------------------------------------------");
            System.out.println("Du (" + name + ") hast folgende Karten:");
            for (Card c : hand) {
                System.out.print(c + " ");
            }
            System.out.println("\n-----------------------------------------------");
        } else {
            System.out.println("Bitte bestätige mit 'j', um fortzufahren.");
        }
    }

    // Prüft, ob der Spieler eine spielbare Karte auf die oberste Karte hat.
    public boolean hasPlayableCard(Card topCard) {
        // Card topCard = discardPile.getTopCard();
        for (Card card : hand) {
            if (isValidMove(card, topCard)) {
                return true;
            }
        }
        return false;
    }

    // Fordert den Spieler zur Auswahl einer Karte auf und spielt diese.
    public Card whichCardWouldYouLikeToPlay(Card topCard, Game game) {
        displayHand();

        if (!hasPlayableCard(topCard)) {
            handleNoPlayableCard(topCard, game);
            if (!hasPlayableCard(topCard)) {
                return null;
            }
            displayHand();
        }

        int choiceNumber = getCardChoiceFromUser();
        if (choiceNumber == -1) {
            return null;
        }

        Card selectedCard = hand.get(choiceNumber - 1);
        if (!isValidMove(selectedCard, topCard)) {
            handleInvalidMove(game);
            return null;
        }

        playCard(selectedCard, choiceNumber);
        return selectedCard;
    }

    // Zeigt die Handkarten mit Nummern zur Auswahl an.
    private void displayHand() {
        System.out.println("Deine Karten:");
        for (int i = 0; i < hand.size(); i++) {
            System.out.print((i + 1) + ": " + hand.get(i) + " ");
        }
        System.out.println();
    }

    // Behandelt den Fall, dass der Spieler keine spielbare Karte hat.
    private void handleNoPlayableCard(Card topCard, Game game) {
        System.out.println("Du hast keine spielbare Karte. Es wird eine Karte vom Stapel gezogen.");
        game.drawOneCard(this);
        if (!hasPlayableCard(topCard)) {
            System.out.println("Deine neue Karte ist nicht spielbar. Der nächste Spieler ist dran.");
        } else {
            System.out.println("Deine Karten nach dem Ziehen:");
            displayHand();
        }
    }

    // Fordert die Kartenauswahl vom Benutzer an und validiert sie.
    private int getCardChoiceFromUser() {
        System.out.print("Welche Karte möchtest du spielen? (Nummer) ");
        String choice = input.next().toLowerCase();
        input.nextLine(); // Buffer leeren

        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(choice);
        if (matcher.find()) {
            int choiceNumber = Integer.parseInt(matcher.group());
            if (choiceNumber < 1 || choiceNumber > hand.size()) {
                System.out.println("Ungültige Auswahl!");
                return -1;
            }
            return choiceNumber;
        }
        System.out.println("Ungültige Eingabe!");
        return -1;
    }

    // Behandelt einen ungültigen Zug durch Ziehen einer Strafkarte.
    private void handleInvalidMove(Game game) {
        System.out.println("Diese Karte darfst du nicht spielen! Du ziehst eine Strafkarte. Deine Karten:");
        game.drawOneCard(this);
        displayHand();
    }

    // Entfernt die ausgewählte Karte aus der Hand und fügt sie dem Ablegestapel hinzu.
    private void playCard(Card selectedCard, int choiceNumber) {
        hand.remove(choiceNumber - 1);
        System.out.println("Du hast gespielt: " + selectedCard);
        declareUNO(String.valueOf(choiceNumber));
        discardPile.addCard(selectedCard);
    }

    // Prüft und behandelt "UNO", falls der Spieler nur eine Karte hat.
    public void declareUNO(String input) {
        if (hand.size() == 1 && input.contains("uno")) {
            System.out.println("UNO!");
        } else if (hand.size() == 1 && (!input.contains("uno"))) {
            System.out.println("Du hast vergessen, UNO zu rufen und bekommst eine Strafkarte.");
            game.drawOneCard(this); // Pass the current player
        }
    }

    // Prüft, ob eine Karte auf die oberste Karte gespielt werden darf.
    public boolean isValidMove(Card playedCard, Card topCard) {

        //Abfrage ob die Karte die man drauflegt die gleiche Farbe hat
        if (playedCard.color == topCard.color) {
            return true;
        }
        if (topCard.color == Color.BLACK &&
                topCard.getChosenColor() != '\0') {

            switch (topCard.getChosenColor()) {
                case 'r':
                    return playedCard.color == Color.RED;
                case 'g':
                    return playedCard.color == Color.GREEN;
                case 'b':
                    return playedCard.color == Color.BLUE;
                case 'y':
                    return playedCard.color == Color.YELLOW;
            }
        }

        // Abfrage, ob die Karte die man drauflegt den gleichen Wert hat
        if (playedCard.value == topCard.value) {
            return true;
        }

        // Abfrage, ob die Karte die man drauflegt eine schwarze Karte (Joker) ist
        return playedCard.color == Color.BLACK;
    }

    // Setter-Methoden
    public void setHand(ArrayList<Card> hand) {
        this.hand = hand;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter-Methoden
    public String getName() {
        return name;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }
}