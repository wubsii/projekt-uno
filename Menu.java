import java.util.Scanner;

// Stellt das Hauptmenü des UNO-Spiels bereit und verwaltet die Benutzerinteraktion
public class Menu {

    private final Scanner scanner = new Scanner(System.in);
    private static final String RESET = "\u001B[0m";
    private static final String YELLOW = "\u001B[33m";
    private final Player player;
    private final Game uno;

    // Konstruktor
    public Menu(Game uno, Player player) {
        this.uno = uno;
        this.player = player;
    }

    // Führt die Hauptmenüschleife aus: Zeigt Optionen an und verarbeitet die Benutzereingabe
    // Behandelt ungültige Eingaben und delegiert an die entsprechenden Methoden
    public Card runMenu() {

        // Sicherheitscheck: verhindert NullPointerException, falls Spiel nicht korrekt initialisiert wurde
        if (uno == null || player == null) {
            System.out.println("Fehler: Spiel nicht korrekt initialisiert!");
            return null;
        }

        // zeigt die Handkarten des aktuellen Spielers (nur für diesen Spieler sichtbar gedacht)
        player.showCards();

        // zeigt das Menü mit den verfügbaren Aktionen
        showMenu();

        // Eingabeaufforderung für den Spieler
        System.out.print(YELLOW + "\nGib deine Wahl ein (1-4): " + RESET);

        int input;

        try {
            // liest die Menüauswahl ein
            input = scanner.nextInt();
        } catch (Exception e) {
            // falls keine gültige Zahl eingegeben wurde
            System.out.println("Ungültige Eingabe!");
            scanner.nextLine(); // Buffer leeren
            return runMenu();   // Menü erneut anzeigen
        }

        scanner.nextLine(); // restliche Eingabe aus dem Buffer entfernen

        // verarbeitet die Menüauswahl
        switch (input) {

            case 1:
                // Spieler führt einen normalen Spielzug aus
                return playTurn();

            case 2:
                // zeigt den aktuellen Punktestand aus der Datenbank an
                GameDatabase db = new GameDatabase();
                db.displayFinalResults();

                // danach zurück ins Menü, damit der Spieler weiter spielen kann
                return runMenu();

            case 3:
                // zeigt die Spielregeln an
                Help.showRulesInFile();

                // danach wieder ins Menü zurückkehren
                return runMenu();

            case 4:
                // fragt nach Bestätigung und beendet ggf. das Spiel
                if (isExit()) System.exit(0);
                return null;

            default:
                // falls Eingabe nicht zwischen 1–4 liegt
                System.out.println("Ungültige Auswahl (1-4)");

                // Menü erneut anzeigen
                return runMenu();
        }
    }

    // Führt einen Spielzug aus und gibt die gespielte Karte zurück
    public Card playTurn() {

        Card topCard = uno.getTopCard();

        // Sicherheitscheck
        if (topCard == null) {
            System.out.println("Fehler: Keine Startkarte gesetzt!");
            return null;
        }
        Card played = player.whichCardWouldYouLikeToPlay(topCard, uno);
        if (played == null) {
            return null;
        }

        uno.setTopCard(played);
        System.out.println("Neue Top-Karte: " + played);

        return played;
    }

    // Zeigt das Hauptmenü mit den verfügbaren Optionen an.
    public void showMenu() {
        System.out.println();
        System.out.printf(YELLOW + """
                Menü:
                1 - Spielzug
                2 - Punktestand anzeigen
                3 - Hilfe
                4 - Spiel beenden""" + RESET);
    }

    // Fordert den Benutzer zur Bestätigung des Spielabbruchs auf
    public boolean isExit() {
        while (true) {
            System.out.print(YELLOW + "Willst du das Spiel wirklich beenden? (j/n) " + RESET);
            String inputExit = scanner.next();
            if (inputExit.equals("n")) {
                return false;
            } else if (inputExit.equals("j")) {
                return true;
            } else {
                System.out.println(YELLOW + "Ungültige Eingabe, bitte j/n eingeben: " + RESET);
            }
        }
    }
}