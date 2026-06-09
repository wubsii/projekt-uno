import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Help {

        public static void showRulesInFile() {
            String rules = """
            **Ziel des Spiels**
            Das Ziel beim klassischen UNO ist es als Erster Spieler 500 Punkte zu erzielen. Pro Runde erhält
            der Spieler Punkte, welcher als Erster alle seine Karten auf der Hand ablegt. Punkte gibt es für alle
            Karten, die die übrigen Mitspieler noch auf der Hand halten (siehe Punkte).
           
            **Spielvorbereitung**
           
            Die Karten werden gemischt und jeder Spieler erhält 7 Karten, die er auf die Hand nimmt. Die
            verbleibenden Karten werden verdeckt in die Mitte gelegt und bilden den Kartenstapel. Vom
            Kartenstapel wird die oberste Karte aufgedeckt und daneben gelegt. Dieser Stapel bildet den
            Ablegestapel. Ein Spieler wird ausgelost der die Runde beginnt.
           
            **Spielverlauf**
           
            Der erste Spiele legt eine Karte von seiner Hand auf den Ablegestapel. Dabei gilt: Eine Karte kann
            nur auf eine Karte der gleichen Farbe oder der gleichen Zahl gelegt werden. Die schwarzen Karten
            sind spezielle Aktionskarten mit besonderen Regeln (siehe Aktionskarten). Kann ein Spieler keine
            passende Karte legen, so muss er eine Strafkarte vom verdeckten Stapel ziehen. Diese Karte kann er
            sofort wieder ausspielen, sofern diese passt. Hat er keine passende Karte ist der nächste Spieler an
            der Reihe. Wer die vorletzte Karte ablegt, muss „UNO!“ (das bedeutet “Eins”) rufen und
            signalisiert damit, dass er nur noch eine Karte auf der Hand hat. Vergisst ein Spieler das und ein
            anderer bekommt es rechtzeitig mit (bevor der nächste Spieler eine Karte gezogen oder abgeworfen
            hat) so muss er 2 Strafkarten ziehen. Die Runde gewinnt derjenige, welcher die letzte Karte
            abgelegt hat. Die Punkte werden addiert und eine neue Runde wird gespielt.
           
            **Aktionskarten**
           
            ZIEH ZWEI KARTE
           
            Wenn diese Karte gelegt wird, muss der nächste Spieler 2 Karten ziehen und darf
            in dieser Runde keine Karten ablegen. Diese Karte kann nur auf eine Karte mit
            entsprechender Farbe oder andere Zieh Zwei Karten gelegt werden. Wenn sie zu Beginn
            des Spiels aufgedeckt wird, gelten diesselben Regeln.
           
            RETOUR KARTE
           
            Bei dieser Karte ändert sich die Spielrichtung. Wenn bisher nach links gespielt wurde,
            wird nun nach rechts gespielt und umgekehrt. Die Karte kann nur auf eine
            entsprechende Farbe oder eine andere Retour Karte gelegt werden. Wenn diese Karte zu
            Beginn des Spiels gezogen wird, fängt der Geber an und dann setzt der Spieler zu seiner
            Rechten anstatt zu seiner Liunken das Spiel fort.
           
            AUSSETZEN KARTE
           
            Nachdem diese Karte gelegt wurde, wird der nächste Spieler „übersprungen“. Die Karte
            kann nur auf eine andere mit entsprechender Farbe oder eine andere Aussetzen Karte
            gelegt werden. Wenn diese Karte zu Beginn des Spiels gezogen wird, wird der Spieler
            zur Linken des Gebers „übersprungen“ und der Spieler zur Linken dieses Spielers setzt
            das Spiel fort.
           
            FARBENWAHLKARTE
           
            Der Spieler, der diese Karte legt, entscheidet welche Farbe als nächstes gelegt werden
            soll. Auch die schon liegende Farbe darf gewählt werden. Ein Farbenwahl Karte darf
            auch dann gelegt werden, wenn der Spieler eine andere Karte legen könnte. Wenn eine
            Farbenwahl Karte zu Beginn des Spiels gezogen wird, entscheidet der Spieler zur Linken
            des Gebers, welche Farbe als nächstes gelegt werden soll.
           
            ZIEH VIER FARBENWAHLKARTE
            
            Diese Karte ist die beste. Der Spieler, der sie legt, entscheidet, welche Farbe als nächstes
            gelegt werden soll. Zudem muss der nächste Spieler 4 Karten von dem Kartenstapel
            nehmen. Er darf in dieser Runde keine Karte ablegen. Leider darf die Karte nur dann
            gelegt werden, wenn der Spieler der sie hat, keine Karte in der Hand hält, die der Farbe
            auf dem Ablegestapel entspricht. Hat der Spieler eine Karte mit der entsprechenden
            Nummer oder Aktionskarten, kann die Zieh Vier Farbenwahlkarte dennoch gelegt
            werden.
            
            **Strafen**
            Wenn ein Spieler sich nicht an die Regeln hält, so muss er ein oder mehrere Strafkarten ziehen. Die
            Regelungen sind wie folgt:
            • UNO: Vergisst ein Spieler nach dem legen seiner vorletzten Karte UNO! zu rufen und hat
            der nächste Spieler seine Karte noch nicht ausgespielt, so muss er eine Strafkarte ziehen.
            • Falsch gelegt: Wer eine falsche Karte gelegt
            hat, muss diese wieder aufnehmen und erhält zusätzlich eine Strafkarte.
           • Die +4 darf nur dann gelegt werden, wenn der Spieler die aktuelle Farbe mit Ausnahme von
            anderen Aktionskarten nicht bedienen kann. Ist der von der +4 betroffene Spieler der
            Auffassung, dass die Karte zu unrecht ausgespielt wurde, so kann er den vorherigen Spieler
            herausfordern. Dieser muss ihm dann durch Vorzeigen seiner Karten nachweisen, dass er die
            Farbe tatsächlich nicht korrekt bedienen konnte. Kann er dies bestätigen, so muss der
            herausfordernde Spieler statt 4 nun 6 Karten aufnehmen. Wurde er hingegen überführt, die
            +4 unrechtmäßig ausgespielt zu haben, so muss er selbst die 4 Karten ziehen.
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

