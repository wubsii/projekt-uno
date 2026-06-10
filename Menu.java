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
    public void runMenu() {
        // Sicherheitscheck (verhindert Nullpointer)
        if (uno == null || player == null) {
            System.out.println("Fehler: Spiel nicht korrekt initialisiert!");
            return;
        }
        player.showCards();
        showMenu();
        System.out.print(YELLOW + "\nGib deine Wahl ein (1-4): " + RESET);

        int input;
        try {
            input = scanner.nextInt();
        } catch (Exception e) {
            System.out.println("Ungültige Eingabe!");
            scanner.nextLine(); // Buffer reset
            return;
        }
        scanner.nextLine(); // Buffer sauber halten
        switch (input) {
            case 1:
                playTurn();
                break;
            case 2:
                // Datenbankverbindung oeffnen und Endergebnisse anzeigen
                GameDatabase db = new GameDatabase();
                db.displayFinalResults();
                break;
            case 3:
                Help.showRulesInFile();
                break;
            case 4:
                if (isExit()) System.exit(0);
                break;
            default:
                System.out.println("Ungültige Auswahl (1-4)");
        }
    }

    // Führt einen Spielzug aus und gibt die gespielte Karte zurück
    public Card playTurn() {
        Card topCard = uno.getTopCard();
        // Spieler wählt eine Karte aus
        Card played = player.whichCardWouldYouLikeToPlay(topCard, uno);

        // falls keine Karte gespielt wurde
        if (played == null) {
            return null;
        }

        // Karte auf Ablagestapel setzen
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