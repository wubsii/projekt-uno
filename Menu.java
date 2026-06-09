import java.util.Scanner;

public class Menu {

    private final Scanner scanner = new Scanner(System.in);

    private static final String RESET = "\u001B[0m";
    private static final String YELLOW = "\u001B[33m";

    private Player player;
    private Game uno;
    private Help help;

    // Konstruktor → sauberer OOP Ansatz
    public Menu(Game uno, Player player, Help help) {
        this.uno = uno;
        this.player = player;
        this.help = help;
    }

    public void runMenu() {

        // Sicherheitscheck (verhindert Nullpointer)
        if (uno == null || player == null) {
            System.out.println("Fehler: Spiel nicht korrekt initialisiert!");
            return;
        }

        player.showCards();

        showMenu();

        System.out.print(YELLOW + "Gib deine Wahl ein (1-4): " + RESET);

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

    private void playTurn() {

        Card topCard = uno.getTopCard();

        Card played = player.whichCardWouldYouLikeToPlay(topCard, uno);

        if (played != null) {
            uno.setTopCard(played);
            System.out.println("Neue Top-Karte: " + played);
        }
    }

        public void showMenu() {
            System.out.printf(YELLOW + """
                Menü:
                1 - Spielzug
                2 - Endergebnisse anzeigen
                3 - Hilfe
                4 - Spiel beenden\n""" + RESET);
        }

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
                    continue;
                }
            }
        }

}