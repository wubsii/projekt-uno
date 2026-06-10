# <span style="color:green">U</span><span style="color:red">N</span><span style="color:yellow">O</span> <span style="color:blue">R</span><span style="color:green">E</span><span style="color:red">A</span><span style="color:yellow">D</span><span style="color:blue">M</span><span style="color:green">E</span>

## <span style="color:green">Beschreibung des Spiels</span>

Dieses Projekt ist ein konsolenbasiertes UNO-Game, das in Java objekt-orientiert programmiert wurde. Es unterstützt bis zu vier Player
und beinhaltet simple UNO-Spielmechanik.

## <span style="color:red">Features</span>

- Deck erstellen 
- Karten mischen
- Karten austeilen
- Züge validieren
- Ablegestapel
- Aktionskarten 
- Punkte zählen
- Gewinner bestimmen

## <span style="color:yellow">Projektbeschreibung</span>

Das Projekt beinhaltet mehrere Klassen:

- Main.java --> Start des Spiels
- Game.java --> kontrolliert den Spielfluss und die Spielregeln
- Player.java --> Ausgabe der Kartenhand und Auswahl der Spielzüge
- Card.java --> erstellt die Spielkarten
- Deck.java --> erstellt das Spieldeck
- DiscardPile.java --> Zwischenlagerung des Ablagestapels
- Menu.java --> erstellt das Eingabemenü und kontrolliert dessen Logik
- Help.java --> erstellt ein .txt-File, wenn ein Spieler Hilfe auswählt
- GameDatabase.java --> Verwaltung der Punkte und Datenbank

## <span style="color:blue">Game starten</span>

1. Projekt in IntelliJ IDEA öffnen
2. Main.java starten
3. Anleitung in der Konsole befolgen

## <span style="color:green">Gruppenaufteilung/Unser Team</span>

- Arnela: Anforderungen, Datenbank und Punkteverwaltung, Testung
- Magdalena: Spielerinteraktionen, Spielablauf, Präsentation
- Stefanie: Karten- und Deckerstellung, Menü inkl. Hilfe, ReadMe

## <span style="color:red">Verbesserungsvorschläge</span>

- bessere Organisationsstruktur von Anfang an
- schönere/deutlichere Darstellung der Spielkarten
- Unit-Tests von Anfang an
- Konzepte der OOP (Vererbung, Interfaces etc.) mehr nutzen
- bessere Übersichtlichkeit durch mehr Klassen herstellen
- Methoden klarer aufteilen und kürzen

## <span style="color:yellow">Gameflow</span>

1. 4 Player werden erstellt
2. Deck wird erstellt und gemischt, Karten werden ausgeteilt
3. Startspieler wird ausgelost
4. Player sind der Reihe nach dran und können im Eingabemenü auswählen, was sie als nächstes tun wollen (Spielzug, Hilfe
anzeigen, Punkte anzeigen, Game beenden)
5. Gültig gespielte Karten werden dem Ablagestapel zugefügt
6. Player ziehen neue Karten vom Deck, wenn sie keine gültige Karte spielen können
7. Das Deck wird vom Ablegestapel wieder befüllt, wenn weniger als 4 Karten übrig sind
8. Punkte werden in einer Datenbank gespeichert
9. Eine Runde endet, wenn ein Player keine Karten mehr übrig hat
10. Ein Game endet, wenn ein Player mehr als 500 Punkte hat oder das Game selbst beendet