import java.util.Scanner;

public class Menu {
static Scanner scanner = new Scanner(System.in);
private static String RESET = "\u001B[0m";
private static String YELLOW = "\u001B[33m";

public static int runMenu() {
while(true) {
        showMenu();
        System.out.println(YELLOW + "Gib deine Wahl ein (1-4): " + RESET);
        int input = scanner.nextInt();

        switch(input){
            case 1:
                System.out.println("WIP");
                break;
            case 2:
                System.out.println("WIP");
                break;
            case 3:
                showHelp();
                break;
            case 4:
                if (isExit()) System.exit(0);
                break;
            default:
                System.out.println(YELLOW + "Ungültige Eingabe, bitte 1 - 4 eingeben: " + RESET);
        }
    }
}

public static void showMenu() {
        System.out.printf(YELLOW + """
                Menü:
                1 - Spielzug
                2 - Punktestand anzeigen
                3 - Hilfe
                4 - Spiel beenden\n""" + RESET);
}

public static void showHelp() {
            System.out.printf(YELLOW + """
                """ + RESET);

    }

public static boolean isExit() {
while (true) {
        System.out.print(YELLOW + "Willst du das Spiel wirklich beenden? (j/n) " + RESET);
        String inputExit = scanner.next();
            if (inputExit.equals("n")) {
                return false;
            } else if  (inputExit.equals("j")) {
                return true;
            } else {
                System.out.println(YELLOW + "Ungültige Eingabe, bitte j/n eingeben: " + RESET);
                continue;
            }
        }
    }
}
