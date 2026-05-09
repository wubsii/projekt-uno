public class Main {
    public static void main(String[] args) {

        DiscardPile discardPile = new DiscardPile();
        Spieler spieler = new Spieler(discardPile);
        Spiel uno = new Spiel(discardPile);

        String[] playerName = new String[4];
        uno.shuffle();

        // Namen eingeben
        for (int i = 0; i < 4; i++) {
            playerName[i] = spieler.gettingName();
        }

        // Startspieler bestimmen
        int start = spieler.randomizePlayer();

        // Reihenfolge anzeigen
        String[] neueReihenfolge = spieler.showOrder(playerName, start);

        // Karten ziehen
        spieler.setHand(uno.dealInitialHand(7));

        // Karten anzeigen lassen / Spiel starten
        spieler.showCards(neueReihenfolge);

        // Menü starten
        Menu.setSpieler(spieler);
        Menu.runMenu();
    }
}