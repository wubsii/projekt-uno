public class Main {

    public static void main(String[] args) {

        DiscardPile discardPile = new DiscardPile();
        Spiel uno = new Spiel(discardPile);

        uno.shuffle();


        Spieler[] spielerListe = new Spieler[4];

        // Spieler erstellen
        for (int i = 0; i < 4; i++) {

            spielerListe[i] = new Spieler(discardPile);

            String name = spielerListe[i].gettingName();
            spielerListe[i].setName(name);
        }

        // Karten einmal verteilen
        for (int i = 0; i < 4; i++) {
            spielerListe[i].setHand(
                    uno.dealInitialHand(7)
            );
        }

        // Startspieler
        int start = spielerListe[0].randomizePlayer();

        while (true) {

            Spieler aktuellerSpieler = spielerListe[(start++) % 4];
            Card topCard = uno.getStartercard();
            Menu.setSpieler(aktuellerSpieler);
            Menu.setSpiel(uno);

            //Menu.showMenu();
            Menu.runMenu();
        }
    }
}