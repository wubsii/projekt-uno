import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Help {

        public static void showRulesInFile() {
            String rules = """
            Spielvorbereitung
            Das Spiel wird mit 4 Spielern gestartet. Jeder Spieler erhält zu Beginn 7 Karten aus dem gemischten Deck.
            
            Spielernamen:
            
            Namen müssen einzigartig sein (keine Dopplungen).
            Erlaubte Zeichen: Buchstaben (a-z, äöüÄÖÜß) und Leerzeichen.
            Leere Namen oder Namen mit Zahlen/Sonderzeichen werden abgelehnt.
            
            Startkarte:
            
            Die erste Karte wird zufällig aus dem Deck gezogen und auf den Ablagestapel gelegt.
            Sonderfälle bei der Startkarte:
            
            +2: Der Startspieler zieht 2 Karten.
            Aussetzen (Skip): Der Startspieler wird übersprungen, der nächste Spieler ist dran.
            Richtungswechsel (Reverse): Die Spielrichtung wird umgekehrt (im Uhrzeigersinn ↔ gegen den Uhrzeigersinn).
            
            Spielablauf
            Das Spiel verläuft im Uhrzeigersinn (Standardrichtung). Die Richtung kann durch die Reverse-Karte geändert werden.
            
            Karten anzeigen:
           
            Der aktuelle Spieler sieht seine Handkarten (nach Bestätigung mit j).
            Beispielausgabe:
            
            Du (Max) hast folgende Karten:
            1: Rot 5
            2: Blau Aussetzen
            3: Schwarz +4
            Karte spielen:
          
            Der Spieler wählt eine spielbare Karte aus seiner Hand (durch Eingabe der Nummer).
            
            Regeln für gültige Züge:
            Die Karte muss dieselbe Farbe wie die oberste Karte des Ablagestapels haben oder
            - derselbe Wert (z. B. zwei rote 7er) oder
            - eine schwarze Karte (Joker oder +4) sein.
            Bei einer Farbwechsel-Karte (Joker oder +4) wählt der Spieler eine neue Farbe (r = Rot, g = Grün, b = Blau, y = Gelb).
            
            Keine spielbare Karte?
            Der Spieler zieht eine Karte vom Nachziehstapel.
            Falls die gezogene Karte spielbar ist, kann sie sofort gespielt werden.
            Falls nicht, ist der nächste Spieler dran.
            UNO rufen:
            Hat ein Spieler nur noch 1 Karte, muss er "UNO" rufen (durch Eingabe von uno beim Spielen der vorletzten Karte,
            getrennt mit einem Semikolon).
            Vergisst er es, zieht er 1 Strafkarte.
            
            Spielende
            Der erste Spieler, der alle Karten loswird, gewinnt das Spiel.
            Die Punkte der übrig gebliebenen Karten der anderen Spieler werden berechnet und in der Datenbank gespeichert.
           
            Punkteberechnung:
            Jede Karte hat einen Punktewert (z. B. Zahlkarten = ihrem Wert, Sonderkarten = 20 Punkte).
            Die Punkte aller übrig gebliebenen Karten eines Spielers werden summiert und als Endergebnis angezeigt.
            """;

            try {
                Files.write(Paths.get("uno_rules.txt"), rules.getBytes());
                System.out.println("Rules saved to 'uno_rules.txt'. Opening file...");
                // Open the file (platform-dependent)
                if (System.getProperty("os.name").toLowerCase().contains("win")) {
                    Runtime.getRuntime().exec("notepad uno_rules.txt");
                } else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
                    Runtime.getRuntime().exec("open uno_rules.txt");
                } else {
                    Runtime.getRuntime().exec("xdg-open uno_rules.txt");
                }
            } catch (IOException e) {
                System.err.println("Failed to open rules file: " + e.getMessage());
            }
        }
    }

