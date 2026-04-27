public class Main {
    public static void main(String[] args) {

        Spieler spieler = new Spieler();
        Spiel uno = new Spiel();

        String[] playerName = new String[4];

        uno.shuffle();

        // Namen eingeben
        for (int i = 0; i < 4; i++) {
            playerName[i] = spieler.gettingName();
        }

        // Namen ausgeben
        System.out.println("Spieler: ");
        for (String name : playerName) {
            System.out.println(name);
        }

        // Startspieler bestimmen
        int start = spieler.randomizePlayer();
        //System.out.println("Startspieler Index: " + start);

        // Neue Reihenfolge
        String[] neueReihenfolge = new String[4];
        for (int i = 0; i < 4; i++) {
            neueReihenfolge[i] = playerName[(start + i) % 4];
        }

        // Ausgabe Reihenfolge
        System.out.println("Reihenfolge: ");
        for (String name : neueReihenfolge) {
            System.out.println(name);
        }

        // Karten anzeigen (nur Demo)
        spieler.playerHand();

        //Menü-Ausgabe (nur Demo)
        Menu.runMenu();
    }
}