import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class GameDatabase {

    // Datenbankverbindung – wird direkt aus der Lehrer-Klasse verwendet
    private SqliteClient client;

    // Name der Tabelle
    private static final String TABLE = "FinalResults";

    // SQL zum Erstellen der Tabelle
    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE + " (" +
                    "player_name TEXT NOT NULL, " +
                    "final_score INTEGER NOT NULL);";

    // Vorlage zum Einfuegen eines Eintrags – %1s = Name, %2d = Punkte
    private static final String INSERT_TEMPLATE =
            "INSERT INTO " + TABLE + " (player_name, final_score) " +
                    "VALUES ('%1s', %2d);";

    // Alle Eintraege absteigend nach Punkten abfragen
    private static final String SELECT_ALL =
            "SELECT player_name, final_score FROM " + TABLE +
                    " ORDER BY final_score DESC;";


    // Konstruktor

    /**
     * Oeffnet die Datenbankverbindung und erstellt die Tabelle,
     * falls sie noch nicht existiert.
     */
    public GameDatabase() {
        try {
            // Datenbankdatei oeffnen oder neu erstellen
            client = new SqliteClient("uno.sqlite");

            // Tabelle nur erstellen wenn sie noch nicht existiert
            if (!client.tableExists(TABLE)) {
                client.executeStatement(CREATE_TABLE);
                System.out.println("Tabelle erstellt: " + TABLE);
            }
        } catch (SQLException e) {
            System.out.println("Datenbankfehler beim Starten: " + e.getMessage());
        }
    }


    // Endergebnis speichern

    /**
     * Speichert den Endpunktestand eines Spielers nach Spielende.
     * Wird einmal pro Spieler am Ende des Spiels aufgerufen.
     *
     * @param playerName  Name des Spielers
     * @param finalScore  Endpunktestand des Spielers
     */
    public void saveFinalScore(String playerName, int finalScore) {
        try {
            // String.format fuellt %1s und %2d mit den uebergebenen Werten
            String sql = String.format(INSERT_TEMPLATE, playerName, finalScore);
            client.executeStatement(sql);
            System.out.println("Gespeichert: " + playerName + " – " + finalScore + " Punkte");
        } catch (SQLException e) {
            System.out.println("Fehler beim Speichern: " + e.getMessage());
        }
    }


    // Endergebnisse anzeigen

    /**
     * Laedt alle gespeicherten Endergebnisse aus der Datenbank
     * und gibt sie in der Konsole aus.
     */
    public void displayFinalResults() {
        try {
            ArrayList<HashMap<String, String>> results = client.executeQuery(SELECT_ALL);

            System.out.println("=== Endergebnis ===");
            for (HashMap<String, String> row : results) {
                System.out.println(
                        row.get("player_name") + ": " + row.get("final_score") + " Punkte"
                );
            }
        } catch (SQLException e) {
            System.out.println("Fehler beim Laden: " + e.getMessage());
        }
    }

    // Alle Eintraege aus der Tabelle loeschen
    // Wird zu Beginn jeder neuen Runde aufgerufen
    public void resetScores() {
        try {
            client.executeStatement("DELETE FROM " + TABLE + ";");
            System.out.println("Punktestand zurueckgesetzt.");
        } catch (SQLException e) {
            System.out.println("Fehler beim Zuruecksetzen: " + e.getMessage());
        }
    }
}