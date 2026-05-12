import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Spieler {

    Scanner input = new Scanner(System.in);
    String name;
    private DiscardPile discardPile;
    Spiel uno = new Spiel(discardPile);
    ArrayList<Card> hand = new ArrayList<>();

    public Spieler(DiscardPile discardPile) {
        this.discardPile = discardPile;
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

    public String gettingName() {
        // Eingabe wie die Leute heißen, die das mit spielen
        System.out.print("Wie heißt der Spieler? ");
        return this.name = input.nextLine();
    }

    public void showNames(String[] playerName) {
        // Namen ausgeben
        System.out.println("Spieler: ");
        for (String name : playerName) {
            System.out.println(name);
        }
    }

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


    public ArrayList<Card> playerHand() {
        uno.shuffle();
        hand = uno.dealInitialHand(7);

        System.out.println("Du (" + name + ") hast folgende Karten:");
        for (Card c : hand) {
            c.print();
        }

        return hand;
    }


    public void showCards() {
        System.out.println(name + " ist jetzt an der Reihe");
        System.out.println("Bitte bestätigen (j/n), damit nur du die Karten siehst: ");

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


    public Card whichCardWouldYouLikeToPlay(Card topCard) {

        // Karten anzeigen
        // System.out.println(hand);
        // System.out.println("HAND SIZE: " + hand.size());
        uno.shuffle();
        for (int i = 0; i < hand.size(); i++) {
            System.out.println((i + 1) + ": " + hand.get(i));
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
            discardPile.addCard(selectedCard); // Karte zum Ablegestapel hinzufügen
            return selectedCard;
        } else {
            System.out.println("Diese Karte darfst du nicht spielen!");
            return null;
        }
    }

    public void declareUNO(String input){
        if (hand.size() == 1 && input.contains("uno")) {
            System.out.println("UNO!");
        }
    }

    public boolean isValidMove(Card playedCard, Card topCard) {

        //Abfrage ob die Karte die man drauflegt die gleiche Farbe hat
        if (playedCard.color == topCard.color) {
            return true;
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



    // DARF KEINE BLACK +4 CARD SEIN
    public Card getStartercard() {
        Card topCard;
        // Startkarte vom Deck
        uno.shuffle();
        do {
            topCard = uno.dealInitialHand(1).get(0);
        } while ((topCard.color == Color.BLACK) && (topCard.value == Value.PLUS_FOUR));
        System.out.println("Startkarte ist: " + topCard);
        return topCard;
    }
}