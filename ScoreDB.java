import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Verwaltet alle Datenbankoperationen fuer den Punktestand des UNO-Spiels.
 * Verwendet die SqliteClient-Klasse fuer den Zugriff auf die SQLite-Datenbank.
 *
 * Zusammenarbeit mit anderen Klassen:
 *   - SqliteClient:   Stellt die Verbindung zur Datenbank her (Klasse vom Lehrer)
 *   - ScoreCalculator: Ruft ScoreDB auf, um Punkte zu speichern und zu laden
 */
public class ScoreDB {

    // Die Verbindung zur Datenbank (Klasse vom Lehrer)
    private SqliteClient db;

    // Name der Tabelle in der Datenbank
    private static final String TABLE = "PUNKTESTAND";

    // -----------------------------------------------------------------------
    // Konstruktor
    // -----------------------------------------------------------------------

    /**
     * Oeffnet die Datenbankverbindung und erstellt die Tabelle,
     * falls sie noch nicht existiert.
     *
     * Die Datenbankdatei "uno.db" wird automatisch erstellt,
     * wenn sie noch nicht vorhanden ist.
     */
    public ScoreDB() {
        try {
            // Datenbankdatei oeffnen oder neu erstellen
            db = new SqliteClient("uno.db");

            // Tabelle nur erstellen wenn sie noch nicht existiert
            if (!db.tableExists(TABLE)) {
                db.executeStatement(
                        "CREATE TABLE " + TABLE + " (" +
                                "spielername TEXT PRIMARY KEY, " + // Name ist der eindeutige Schluessel
                                "punkte INTEGER NOT NULL" +         // Punktestand als Ganzzahl
                                ");"
                );
                System.out.println("Datenbanktabelle wurde erstellt: " + TABLE);
            }

        } catch (SQLException e) {
            // Fehlermeldung ausgeben wenn die Verbindung nicht aufgebaut werden kann
            System.out.println("Datenbankfehler beim Starten: " + e.getMessage());
        }
    }

    // -----------------------------------------------------------------------
    // Spieler eintragen
    // -----------------------------------------------------------------------

    /**
     * Traegt einen neuen Spieler mit 0 Punkten in die Datenbank ein.
     * Wird einmalig zu Beginn des Spiels fuer jeden Spieler aufgerufen.
     *
     * @param playerName  Name des Spielers, der eingetragen werden soll
     */
    public void registerPlayer(String playerName) {
        try {
            db.executeStatement(
                    "INSERT INTO " + TABLE + " (spielername, punkte) " +
                            "VALUES ('" + playerName + "', 0);"
            );
            System.out.println("Spieler eingetragen: " + playerName);

        } catch (SQLException e) {
            // Fehlermeldung ausgeben wenn das Eintragen fehlschlaegt
            System.out.println("Fehler beim Eintragen von " + playerName
                    + ": " + e.getMessage());
        }
    }

    // -----------------------------------------------------------------------
    // Punkte aktualisieren
    // -----------------------------------------------------------------------

    /**
     * Aktualisiert den Punktestand eines Spielers in der Datenbank.
     * Wird nach jeder Runde fuer den Rundensieger aufgerufen.
     *
     * @param playerName  Name des Spielers, dessen Punkte aktualisiert werden
     * @param newScore    Neuer Gesamtpunktestand des Spielers
     */
    public void updateScore(String playerName, int newScore) {
        try {
            db.executeStatement(
                    "UPDATE " + TABLE + " " +
                            "SET punkte = " + newScore + " " +
                            "WHERE spielername = '" + playerName + "';"
            );
            System.out.println("Punkte aktualisiert: " + playerName
                    + " → " + newScore + " Punkte.");

        } catch (SQLException e) {
            // Fehlermeldung ausgeben wenn die Aktualisierung fehlschlaegt
            System.out.println("Fehler beim Aktualisieren von " + playerName
                    + ": " + e.getMessage());
        }
    }

    // -----------------------------------------------------------------------
    // Alle Punktestaende laden
    // -----------------------------------------------------------------------

    /**
     * Laedt alle Punktestaende aus der Datenbank und gibt sie zurueck.
     * Die Ergebnisse sind absteigend nach Punkten sortiert (hoechster zuerst).
     *
     * Rueckgabe: Eine ArrayList, in der jede Zeile eine HashMap ist.
     *   Key   = Spaltenname  (z.B. "spielername", "punkte")
     *   Value = Datenwert    (z.B. "Anna",         "240"  )
     *
     * @return ArrayList mit allen Spielern und ihren Punkten
     */
    public ArrayList<HashMap<String, String>> loadAllScores() {
        try {
            // SELECT mit absteigender Sortierung nach Punkten
            return db.executeQuery(
                    "SELECT spielername, punkte FROM " + TABLE +
                            " ORDER BY punkte DESC;"
            );

        } catch (SQLException e) {
            // Fehlermeldung ausgeben und leere Liste zurueckgeben
            System.out.println("Fehler beim Laden der Punkte: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // -----------------------------------------------------------------------
    // Punktestand anzeigen
    // -----------------------------------------------------------------------

    /**
     * Laedt alle Punktestaende aus der Datenbank und gibt sie in der Konsole aus.
     * Kann jederzeit aufgerufen werden, um den aktuellen Stand anzuzeigen.
     */
    public void displayScoreboard() {
        // Alle Eintraege aus der Datenbank laden
        ArrayList<HashMap<String, String>> results = loadAllScores();

        System.out.println("=== Punktestand aus der Datenbank ===");

        // Jede Zeile ist eine HashMap mit Spaltenname als Key und Wert als Value
        for (HashMap<String, String> row : results) {
            String name  = row.get("spielername");
            String score = row.get("punkte");
            System.out.println("  " + name + ": " + score + " Punkte");
        }
    }
}