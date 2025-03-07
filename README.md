# Multimodal_Parliament_Explorer_8_3

# Inhaltsverzeichnis
1. [Vorstellung des Squads](#der-squad)
2. [Beschreibung des Projektes](#beschreibung-des-projektes)
3. [Kurzbeschreibung](#kurzbeschreibung)
4. [Installation](#installation)
5. [Benutzung](#benutzung)
6. [Wichtige Methoden](#wichtige-methoden)
7. [Routen](#routen)
8. [Laufzeit](#laufzeit)
9. [Fehlerquellen](#fehlerquellen)


## Der Squad

<table style="border-collapse: collapse; border: none;">
  <tr>
    <th style="border: none;">Tony (Musiker)</th>
    <th style="border: none;">Max (Vater)</th>
    <th style="border: none;">Grischa (Lowperformer)</th>
    <th style="border: none;">Ben (Baller)</th>
  </tr>
  <tr>
    <td style="border: none;"><img src="anton1.png" width="180"></td>
    <td style="border: none;"><img src="max1.png" width="200"></td>
    <td style="border: none;"><img src="grischa.png" width="215"></td>
    <td style="border: none;"><img src="Ben1.png" width="225"></td>
  </tr>
</table>




# Beschreibung des Projektes

Das Projekt Übung 4 baut auf meiner vorherigen Projekten Übung 2 und 3 auf. Ich gehe in dieser ReadMe also nur auf die Erweiterung ein,
welche für diese Übung notwending sind. Für die anderen Informationen, bitte meine ReadMe zur Übung 2 und 3 sonst lesen.

Ich habe ein Programm gebaut, welches mithilfe von NLP-Methoden Reden analysieren kann und wichtige Informationen, wie Sentiments, Named Entities etc.
extrahiert und speichert, diese Information werden in der Datenbank gespeichert.

Zusätzlich könnnen für Videos von Reden mithilfe von Whisper Transkripte erstellt werden, welche auch mit NLP-Analysen analysiert werden.

Außerdem wird dann auf einer Website die Ergebnisse der Analyse mit Diagrammen dargestellt, sowie den Text der Rede mit der Färbung vom Sentiment
und das Video wird bereitgestellt.


## Kurzbeschreibung
- Analyse von Redetexten mithilfe von NLP-Methoden
- Hochladen der Daten in eine MongoDB-Datenbank
- HTML Webseiten erstellen mit Javalin und Freemarker:
    - Diagramme
    - Video
    - RedeText

## Installation

- Um das Programm zu nutzen, muss mindesten Java 17 installiert sein
- Eine Entwicklerumgebung wie IntelliJ um das Programm auszuführen
- Maven für die Dependencys
- Eine MongoDB-Datenbank
- Goethe-Uni VPN um Spacy, Gervader und Whisper zu nutzen
- Einen Browser um die Websiten zu nutzen

## Benutzung


### 1. Main ausführen

Um das Programm zu nutzen, muss die Main ausgeführt werden. Standardmäßig werden alle wichtigen Funktionen in der Main über die Factory aufgerufen.
In diesem Projekt wird nur die startRest methode aufgerufen, welche das ganze Restful und Javalin macht.
Wenn man die NLP-Analysen nochmal durchführen möchte, muss man factory.redenAnalysieren(); ausklammer und nutzen.

### 2. HTMLs anschauen
Danach muss http://localhost:7070/ im Browser geöffnet werden. Und man kann die Webapplikation nutzen.
Man startet auf der Startseite und dort kann man dann die zwei Links auswählen, zu den beiden Analysen.

### 3. Video anschauen
Auf der Website kann dann das Video angeschaut werden, sowie die Diagramme und den Redetext.

## Wichtige Methoden

### 1. Factory erstllen
- `Factory factory = new Factory()`

Zuerst wird eine Factory in der Main erstellt um das Programm zu nutzen


### 2. Webapplikation starten

- `factory.startRest()`

Methode initialisert die Webapplikation, d.h. Javalin mit Restful wird gestartet und alle Routen werden initialisiert.
Der Nutzer kann das Progamm nutzen.

### 3. NLP-Analyse starten

- `factory.redenAnalysieren()`

Methode analysiert die beiden Redentexte aus der Datenbank, dann wird Whisper genutzt um Transkripte für die Videos zu erstellen
und dann werden diese auch noch analysiert und in der Datenbank gespeichert.

## Routen

### 1. Startseite
- `web.get("/", ctx -> {...});`

Eine get-Route um Informationen für die Startseite zu holen und zu rendern, wenn "/" aufgerufen wird

### 2. Rede
- `web.get("/rede", ctx -> {...});`

Eine get-Route um Informationen für die Redenseite mit NLP-Analyse zu holen und zu rendern, wenn "/rede" mit einer id aufgerufen wird

### 3. Video
- `web.get("/video", ctx -> {...});`

Eine get-Route um das jeweilige Video für die Website bereitzustellen.

### 4. Image
- `web.get("/image", ctx -> {...});`

Eine get-Route um das jeweilige Bild für das sehr schöne Diagramm auf der Startseite zu returnen.

## Laufzeit

Alle Websiten sollten direkt geladen werden und auch das Video sollte direkt verfügbar sein.
Das Diagramm auf der Startseite lässt die Seite kurz ein bisschen laggen wenn es geladen wird.


## Fehlerquellen

- Dateien fehlen oder Ordner fehlen z.B. database.properties oder javelin.properties
- Nicht mit der Datenbank verbunden
- VPN nicht verbunden


