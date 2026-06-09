import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Player {

    Scanner input = new Scanner(System.in);
    String name;
    private DiscardPile discardPile;
    ArrayList<Card> hand = new ArrayList<>();
    //Player[] playerListe = new Player[4];
    private Game game;

    public Player(Game game, DiscardPile discardPile) {
        this.game = game;
        this.discardPile = discardPile;
    }

  /*
Beim Erstellen eines Spielers wird der eingegebene Name auf folgende Punkte geprüft:
• Leerer Name: Gibt der Nutzer keinen Namen ein, wird er erneut zur Eingabe aufgefordert.
• Ungültige Zeichen: Enthält der Name Zahlen oder Sonderzeichen, wird er als ungültig betrachtet und der Nutzer wird erneut zur Eingabe aufgefordert.
• Doppelter Name: Ist der eingegebene Name bereits einem anderen Spieler zugewiesen, wird der Nutzer darauf hingewiesen und muss einen anderen Namen eingeben.
Die Namen der Spieler können zu jedem Zeitpunkt im Spiel abgerufen und angezeigt werden.
*/

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


    // Zum zeigen wie die Spieler heißen
    public void showNames(String[] playerName) {
        // Namen ausgeben
        System.out.println("Spieler: ");
        for (String name : playerName) {
            System.out.println(name);
        }
    }

//    public void createPlayers() {
//
//        List<String> vorhandeneNamen = new ArrayList<>();
//
//        // Spieler erstellen
//        for (int i = 0; i < 4; i++) {
//            playerListe[i] = new Player(discardPile);
//            String name = playerListe[i].gettingName(vorhandeneNamen);
//            playerListe[i].setName(name);
//            vorhandeneNamen.add(name);
//        }
//    }

    public String[] showOrder(String[] playerName, int start) {
        String[] neueReihenfolge = new String[4];
        for (int i = 0; i < 4; i++) {
            neueReihenfolge[i] = playerName[(start + i) % 4];
        }
        // Ausgabe Reihenfolge
//        System.out.println("Reihenfolge: ");
//        for (String name : neueReihenfolge) {
//            System.out.println(name);
//        }
        return neueReihenfolge;
    }

    public int randomizePlayer() {
        // generieren eines random index
        Random rand = new Random();
        return rand.nextInt(4);
    }


    public ArrayList<Card> playerHand(Game uno) {
        hand = uno.dealInitialHand(7);

        System.out.println("Du (" + name + ") hast folgende Karten:");
        for (Card c : hand) {
            c.print();
        }

        return hand;
    }


    public void showCards() {
        System.out.println(name + " ist jetzt an der Reihe");
        System.out.print("Bitte bestätigen (j/n), damit nur du die Karten siehst: ");

        char showMe = input.next().charAt(0);

        if (showMe == 'j' || showMe == 'J') {
            System.out.println("Du (" + name + ") hast folgende Karten:");
            for (Card c : hand) {
                c.print();
            }
        } else {
            System.out.println("Bitte bestätige mit 'j', um fortzufahren.");
        }
    }

    public boolean hasPlayableCard(Card topCard){
       // Card topCard = discardPile.getTopCard();
        for (Card card : hand) {
            if (isValidMove(card, topCard)) {
                return true;
            }
        }
        return false;
    }


    public Card whichCardWouldYouLikeToPlay(Card topCard, Game game) {

        // Karten anzeigen
        // System.out.println(hand);
        // System.out.println("HAND SIZE: " + hand.size());
            for (int i = 0; i < hand.size(); i++) {
                System.out.println((i + 1) + ": " + hand.get(i));
            }

        // NOCH NICHT FERTIG: Spieler hat keine spielbare Karte
        if (!hasPlayableCard(topCard)) {
            game.drawOneCard(this); // Pass the current player
            System.out.println("Du hast keine spielbare Karte. Es wird eine Karte vom Stapel gezogen.");
            if (!hasPlayableCard(topCard)) {
                System.out.println("Deine neue Karte ist nicht spielbar. Der nächste Spieler ist dran.");
                return null;
            } else {
                System.out.println("Deine Karten nach dem Ziehen:");
                for (int i = 0; i < hand.size(); i++) {
                    System.out.println((i + 1) + ": " + hand.get(i));
                }
            }
        }

        System.out.print("Welche Karte möchtest du spielen? (Nummer) ");
        String choice = input.next().toLowerCase();
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(choice);
        int choiceNumber = 0;
        if (matcher.find()) {
            String numberStr = matcher.group();
            choiceNumber = Integer.parseInt(numberStr);
            System.out.println(choiceNumber);
        }

        input.nextLine(); // Buffer leeren

        // prüfen ob Auswahl gültig ist
        if (choiceNumber < 1 || choiceNumber > hand.size()) {
            System.out.println("Ungültige Auswahl!");
            return null;
        }

        Card selectedCard = hand.get(choiceNumber - 1);

        // prüfen ob Karte spielbar ist
        if (isValidMove(selectedCard, topCard)) {
            hand.remove(choiceNumber - 1);
            System.out.println("Du hast gespielt: " + selectedCard);
            declareUNO(choice);
            discardPile.addCard(selectedCard); // Karte zum Ablegestapel hinzufügen
            return selectedCard;
        } else {

                System.out.println("Diese Karte darfst du nicht spielen! Du ziehst eine Strafkarte:");
                game.drawOneCard(this); // Pass the current player
                return null;
            }

        }

    public void declareUNO(String input) {
        if (hand.size() == 1 && input.contains("uno")) {
            System.out.println("UNO!");
        } else if (hand.size() == 1 && (!input.contains("uno"))) {
            System.out.println("Du hast vergessen, UNO zu rufen und bekommst eine Strafkarte.");
            game.drawOneCard(this); // Pass the current player
        }
    }

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

        // Abfrage ob die Karte die man drauflegt den gleicher Wert hat
        if (playedCard.value == topCard.value) {
            return true;
        }

        // Abfrage ob die Karte die man drauflegt eine schwarze Karte (Joker) ist
        if (playedCard.color == Color.BLACK) {
            return true;
        }

        return false;
    }

    public void setHand(ArrayList<Card> hand) {
        this.hand = hand;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }}