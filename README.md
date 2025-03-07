# Projektbeschreibung

### Ziel des Projekts
---

Diesmal ist das Ziel, eine NLP Analyse mit gegebenen Tools (Spacy, GerVader, WhisperX) durchzuführen auf zwei Redevideos, die uns gegeben wurden. 
Einmal analysieren wir den Text der Rede an sich, dann lassen wir das Video mit Whisper transkribieren und analysieren dann diesen Text nochmal genauso mit spacy und gervader. 
Dann wird das alles schön dargestellt auf einer Website mit Rede Video, sentiment analyse auf satzebene und Diagramme für POS Typen und Named Entities.

---

## Programm Aufbau (Packages, Klassen etc. )

### **RESTHandler**
Der RestHandler übernimmt auch wieder die neue ROUTE: nlp_analysen, wo diesmal alles drauf zu sehen ist. 

- **1. Starseite**:
  - Route: /
  - Methode: GET
  - Funktion: Zeigt die Startseite mit Links zu den wichtigsten Seiten (Übersicht, Historie).
  - Template: home.ftl

- **2. NLP Analysen**:
  - Route: /nlp_analysen
  - Methode: GET
  - Funktion: NLP Analysen anzeigen
  - Template: nlp_analysen.ftl


### **NLP**
Das neue Package NLP kümmert sich um die ganzen Remote Driver von SpAcy, GerVader und Whisper. 
nlpPipeline ist für das Setup verantwortlich 
nlpAnalyse führt die Sachen wirklich aus, auf den Videos und den Reden an sich. 

### **MongoImpls**

Diesmal ist eine neue NLP_Analyse Mongo Impl für die Serialisierung der Cas Objekte dazugekommen. Hier sind vor allem die Getter wieder sehr wichtig. 
Man kann einfach die id oder Sentiments, POS, und alles weitere einfach so mit den Gettern direkt aus der DB ziehen, ohne über die Cas gehen zu müssen. 



---

## Anleitung zur Benutzung

Also eigentlich einfach die Main Klasse starten und dann sollte alles laufen.
Sollte eigentlich instant gehen. 

### **DB Anbindung**
- Die properties müssen in db.properties in `src/main/resources/` liegen und dort sind alle infos für die URI connection
- VPN Verbindung muss an sein (goethe vpn)


### **Main.java**
- Einfach ausführen, dann sollte alles losgehen und mit prints in der Konsole kriegt man einen Überblick was passiert 
- (alle DB Sachen dauern lange, der Rest geht schnell)

### Auf der Website:
- Hier dann einfach auf die Seite gehen, die du willst, also nlp Analysen direkt 
- Hier siehst du dann Videos, Sentiment pro Satz und die Diagramme, das Hervorheben habe ich nicht mehr geschafft. 
---

## Hinweise

- Programmausführung über `Main.java`, einfach die main methode ausführen dann sollte alles laden. 
- Dann einfach unten im Terminal auf den Link gehen: http://localhost:7070/
