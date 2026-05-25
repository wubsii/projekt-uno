import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Verwaltet die Punkte aller Spieler ueber mehrere Runden.
 * Berechnet Rundenpunkte, speichert Gesamtpunkte und ermittelt den Spielsieger.
 *
 * Zusammenarbeit mit anderen Klassen:
 *   - Card:     Liefert den Punktwert einer einzelnen Karte (getPointValue())
 *   - Value:    Enum mit den festgelegten Punktwerten pro Kartentyp
 *   - Player:   Liefert den Namen und die Handkarten eines Spielers
 *   - ScoreDB:  Speichert und laedt den Punktestand aus der Datenbank
 *   - Game:     Ruft den ScoreCalculator nach jeder Runde auf
 */
public class ScoreCalculator {

    // Speichert den aktuellen Gesamtpunktestand jedes Spielers (Name → Punkte)
    private Map<String, Integer> totalScores;

    // Datenbankverbindung – speichert den Punktestand dauerhaft
    private ScoreDB database;

    // Ab dieser Punktzahl hat ein Spieler das gesamte Spiel gewonnen
    private static final int WINNING_SCORE = 500;

    // -----------------------------------------------------------------------
    // Konstruktor
    // -----------------------------------------------------------------------

    /**
     * Erstellt einen neuen ScoreCalculator, setzt alle Spieler auf 0 Punkte
     * und traegt sie in die Datenbank ein.
     *
     * @param players  Liste aller Spieler-Objekte, die am Spiel teilnehmen
     */
    public ScoreCalculator(List<Player> players) {
        totalScores = new HashMap<>();

        // Datenbankverbindung oeffnen
        // Die Tabelle wird automatisch erstellt falls sie noch nicht existiert
        database = new ScoreDB();

        // Jeden Spieler mit 0 Punkten in die Map und in die Datenbank eintragen
        for (Player p : players) {
            totalScores.put(p.getName(), 0);
            database.registerPlayer(p.getName());
        }
    }

    // -----------------------------------------------------------------------
    // Rundenabrechnung
    // -----------------------------------------------------------------------

    /**
     * Rechnet eine Runde ab:
     *   1. Holt die Handkarten jedes Verlierers ueber getHand()
     *   2. Zaehlt alle Punktwerte der Handkarten zusammen
     *   3. Addiert die Gesamtsumme zum Punktestand des Rundesiegers
     *   4. Speichert den neuen Punktestand in der Datenbank
     *
     * Hinweis: In UNO bekommt der Sieger die Punkte der Verlierer gutgeschrieben.
     * Wer zuerst 500 Punkte erreicht, gewinnt das gesamte Spiel.
     *
     * @param winner   Das Spieler-Objekt, das die Runde gewonnen hat
     * @param losers   Liste aller Spieler-Objekte, die die Runde verloren haben
     */
    public void settleRound(Player winner, List<Player> losers) {
        int roundPoints = 0;

        // Alle Handkarten aller Verlierer durchgehen und Punktwerte addieren
        for (Player p : losers) {
            int pointsThisPlayer = 0;

            for (Card card : p.getHand()) {
                // getPointValue() kommt aus der Card-Klasse,
                // die ihrerseits den Wert aus dem Value-Enum holt
                pointsThisPlayer += card.getPointValue();
            }

            System.out.println("  " + p.getName()
                    + " hatte Karten im Wert von " + pointsThisPlayer + " Punkten.");

            roundPoints += pointsThisPlayer;
        }

        // Rundenpunkte zum bisherigen Gesamtstand des Siegers hinzufügen
        // getOrDefault: falls der Name noch nicht in der Map ist, wird 0 genommen
        int previousScore = totalScores.getOrDefault(winner.getName(), 0);
        int newScore = previousScore + roundPoints;
        totalScores.put(winner.getName(), newScore);

        // Neuen Punktestand dauerhaft in der Datenbank speichern
        database.updateScore(winner.getName(), newScore);

        // Rundenabschluss in der Konsole ausgeben
        System.out.println("=== Rundenende ===");
        System.out.println("Rundensieger: " + winner.getName()
                + " bekommt " + roundPoints + " Punkte gutgeschrieben.");
        System.out.println("Neuer Gesamtstand von " + winner.getName()
                + ": " + newScore + " Punkte.");
    }

    // -----------------------------------------------------------------------
    // Spielsieger ermitteln
    // -----------------------------------------------------------------------

    /**
     * Prueft nach jeder Runde, ob ein Spieler bereits 500 oder mehr Punkte hat.
     * Der erste Spieler, der diese Grenze erreicht, gewinnt das gesamte Spiel.
     *
     * @return Name des Spielsiegers, oder null wenn noch kein Sieger feststeht
     */
    public String determineGameWinner() {

        // Alle Eintraege in der Map durchsuchen
        for (Map.Entry<String, Integer> entry : totalScores.entrySet()) {
            if (entry.getValue() >= WINNING_SCORE) {
                // Dieser Spieler hat die Gewinnschwelle erreicht
                return entry.getKey();
            }
        }

        // Noch kein Sieger → Spiel geht weiter
        return null;
    }

    // -----------------------------------------------------------------------
    // Punktestand anzeigen
    // -----------------------------------------------------------------------

    /**
     * Gibt den aktuellen Punktestand aller Spieler aus der Datenbank
     * in der Konsole aus.
     * Wird typischerweise nach jeder Runde aufgerufen.
     */
    public void displayScoreboard() {
        // Punktestand direkt aus der Datenbank laden und anzeigen
        // So ist sichergestellt, dass immer der gespeicherte Stand angezeigt wird
        database.displayScoreboard();
    }

    // -----------------------------------------------------------------------
    // Hilfsmethode
    // -----------------------------------------------------------------------

    /**
     * Gibt die aktuellen Gesamtpunkte eines bestimmten Spielers zurueck.
     * Kann von der Game-Klasse genutzt werden, um einzelne Punktstaende abzufragen.
     *
     * @param playerName   Name des gesuchten Spielers
     * @return             Aktuelle Punktzahl, oder 0 wenn Spieler nicht gefunden
     */
    public int getScore(String playerName) {
        // getOrDefault gibt 0 zurueck, falls der Spieler nicht in der Map ist
        return totalScores.getOrDefault(playerName, 0);
    }
}