package Factory;

import Database.MongoDBHandler;
import File_Impls.*;
import Klassen.Funktion;
import MongoDB_Impls.*;
import Parser.abgeordnetenParser;
import Parser.sitzungsParser;
import org.bson.Document;

import java.util.*;


/**
 * MainFactory zur zentralen Steuerung und Verwaltung der Objekterstellung,
 * Speicherung und Datenbankoperationen.
 *
 * Alle Klassenobjekte werden ausschließlich über die Factory erstellt.
 * Die Factory ruft Parser auf und kann Daten in die Datenbank schreiben oder daraus laden.
 */
public class MainFactory {


    // erstmal alle Listen zum speichern der geparsten xml daten
    private final List<Abgeordneter_File_Impl> abgeordneteListe = new ArrayList<>();
    private final List<Sitzung_File_Impl> sitzungenListe = new ArrayList<>();
    private final List<Tagesordnungspunkt_File_Impl> tagesordnungspunktListe = new ArrayList<>();
    private final List<Rede_File_Impl> redenListe = new ArrayList<>();
    private final List<Redner_File_Impl> rednerListe = new ArrayList<>();
    private final List<Kommentar_File_Impl> kommentarListe = new ArrayList<>();
    private final List<Fraktion_File_Impl> fraktionenListe = new ArrayList<>();


    // hier richtig cool: Listen mit mongo db documents (entstehen über toDoc methoden)
    private final List<Document> abgeordneteDocs = new ArrayList<>();
    private final List<Document> sitzungenDocs = new ArrayList<>();
    private final List<Document> topDocs = new ArrayList<>();
    private final List<Document> redenDocs = new ArrayList<>();
    private final List<Document> rednerDocs = new ArrayList<>();
    private final List<Document> kommentarDocs = new ArrayList<>();
    private final List<Document> fraktionDocs = new ArrayList<>();


    // hier dann nochmal explizit die MongoDB Impls als Listen (entstehen aus den doc listen)
    private final List<Abgeordneter_MongoDB_Impl> abgeordneterMongoDBList = new ArrayList<>();
    private final List<Sitzung_MongoDB_Impl> sitzungMongoDBList = new ArrayList<>();
    private final List<Tagesordnungspunkt_MongoDB_Impl> tagesordnungspunktMongoDBList = new ArrayList<>();
    private final List<Rede_MongoDB_Impl> redeMongoDBList = new ArrayList<>();
    private final List<Redner_MongoDB_Impl> rednerMongoDBList = new ArrayList<>();
    private final List<Kommentar_MongoDB_Impl> kommentarMongoDBList = new ArrayList<>();
    private final List<Fraktion_MongoDB_Impl> fraktionMongoDBList = new ArrayList<>();






    // maps für Zuordnungen von id zu abg, id zu redner, id zu sitzung, redner zu reden, id zu reden etc.
    private final Map<String, Abgeordneter_File_Impl> abgeordnetenIDZuAbgeordneter = new HashMap<>();
    private final Map<String, Sitzung_File_Impl> sIDZuSitzung = new HashMap<>();
    private final Map<String, Tagesordnungspunkt_File_Impl> topIDZuTop = new HashMap<>();
    private final Map<String, Redner_File_Impl> rednerIDZuRedner = new HashMap<>();
    private final Map<String, Rede_File_Impl> redIDZuRede = new HashMap<>();
    private final Map<String, Kommentar_File_Impl> kIDZuKommentar = new HashMap<>();

    // Map zur Verfolgung der laufenden Nummer pro Rede
    private final Map<String, Integer> redeIDZuNummer = new HashMap<>();


    // fraktionen string zu Fraktion Klasse
    private final Map<String, Fraktion_File_Impl> stringZuFraktion = new HashMap<>();

    // hashmaps für reden organisation
    private final Map<String, List<Rede_File_Impl>> rednerIDZuReden = new HashMap<>();


    // noch eine hashmap mit abgeordneten ID aber diesmal return abge mongo impl (sehr hilfreich)
    // für abge infos bei den rednern per id (verlnpüft)
    private final Map<String, Abgeordneter_MongoDB_Impl> abgeordneterMongoDBByID = new HashMap<>();
    // gleiches auch für sitzung um später sitzungsinfos in html bei den reden zu haben
    private final Map<String, Sitzung_MongoDB_Impl> sitzungMongoDBByID = new HashMap<>();

    // hier auch nchmal für redner und kommi bei meiner neuen dynamischen html hilfreich
    private final Map<String, Redner_MongoDB_Impl> rednerMongoByID = new HashMap<>();
    private final Map<String, Kommentar_MongoDB_Impl> kommentarMongoByID = new HashMap<>();



    // Mongo handler für DB Operationen (ganz unten)
    private final MongoDBHandler dbHandler;


    /**
     * Konstruktor der MainFactory
     *
     * @param dbHandler Datenbankhandler für MongoDB-Operationen
     */
    public MainFactory(MongoDBHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

    // -------------- parser aufrufen um alle listen etc. zu füllen ------------------

    // getter für db handler
    public MongoDBHandler getDbHandler() {
        return dbHandler;
    }

    /**
     * lädt die abgeordneten, frkation, funktionen und füllt die listen hier
     *
     * @param filepath Pfad zur XML-Datei mdb_stammdaten.xml
     */
    public void loadAbgeordneteFromXML(String filepath) {
        try {
            abgeordnetenParser parser = new abgeordnetenParser(this);
            parser.parseAbgeordnete(filepath);
        } catch (Exception e) {
            System.err.println("Fehler beim Laden der Abgeordneten: " + e.getMessage());
        }
    }

    /**
     * lädt die sitzungs xmls und füllt die listen hier
     *
     * @param filepath zu den sitzungsprotkoll xmls
     */
    public void loadSitzungenFromXML(String filepath) {
        try {
            sitzungsParser parser = new sitzungsParser(this);
            parser.parseSitzung(filepath);
        } catch (Exception e) {
            System.err.println("Fehler beim Laden der Sitzungen: "+ filepath   + e.getMessage());
        }
    }



    // ----------------------- alle factory methoden zum erstellen von file impls    -----------------------------------



    // abgeordneten erstellen über factory
    public Abgeordneter_File_Impl createAbgeordneter(String ID, String vorname, String nachname, String geschlecht,
                                                     String gebDatum, String gebOrt, String fraktionName, String anrede, String akademTitel,
                                                     String familienstand, String religion, String beruf, String wkrLand, int wkrNummer,
                                                     String mandatsArt, List<Funktion> funktionen) {

        // gucken ob schon in hashmap -> verhindert duplikate, eindeutige ID bleibt erhalten
        if (abgeordnetenIDZuAbgeordneter.containsKey(ID)){
            return abgeordnetenIDZuAbgeordneter.get(ID);
        }

        Abgeordneter_File_Impl abgeordneterFileImpl = new Abgeordneter_File_Impl(ID, vorname, nachname, geschlecht, gebDatum, gebOrt,
                fraktionName, anrede, akademTitel, familienstand, religion, beruf, wkrLand, wkrNummer, mandatsArt, funktionen );
//
//        // frak zuordnen
//        if (fraktionName != null && !fraktionName.isEmpty()) {
//            Fraktion_File_Impl fraktion = createFraktion(fraktionName);
//            fraktion.addfrakMitglied(abgeordneterFileImpl);
//        }

        // abgeordneterFileImpl zu hashmap und liste hinzufügen
        abgeordnetenIDZuAbgeordneter.put(ID, abgeordneterFileImpl);
        abgeordneteListe.add(abgeordneterFileImpl);

        return abgeordneterFileImpl;
    }

    public Fraktion_File_Impl createFraktion(String fraktionName) {
        // wenn frak schon existiert, zurückgeben


        String fraktionClean = fraktionName.replaceAll("\\s+", " ") // normales leerzeichen ersetzen
                .replace('\u00A0', ' ')  // es gibt auch noch diese geschützten leerzeichen anscheinend bei den grünen
                .trim().toUpperCase();
        // System.out.println(fraktionClean);

        if (fraktionClean.equals("SPDCDU/CSU")){
            fraktionClean = "CDU/CSU";
        }

        if (stringZuFraktion.containsKey(fraktionClean)) {
            return stringZuFraktion.get(fraktionClean);
        }

        // wenn nicht -> neue frak erstellen und hinzufügen
        Fraktion_File_Impl fraktionFileImpl = new Fraktion_File_Impl(fraktionClean);
        stringZuFraktion.put(fraktionClean, fraktionFileImpl);
        fraktionenListe.add(fraktionFileImpl);
        return fraktionFileImpl;
    }

    // Sitzung erstellen mit factory
    public Sitzung_File_Impl createSitzung(String sID, int wpNr, String sDatum, String sOrt, String sStartzeit, String sEndzeit, boolean sOeffentlichkeit)
    {
        // falls in der hashmap schon eine sitzung mit der id ist: nur returnen (gegen duplikate)
        if (sIDZuSitzung.containsKey(sID)){
            return sIDZuSitzung.get(sID);
        }
        // else: sitzung machen und zu liste und map dazu, dann return
        Sitzung_File_Impl sitzungFileImpl = new Sitzung_File_Impl(sID, wpNr, sDatum, sOrt, sStartzeit, sEndzeit, sOeffentlichkeit);
        sIDZuSitzung.put(sID, sitzungFileImpl);
        // System.out.println("Sitzung mit ID: " + sitzung.getsID());
        sitzungenListe.add(sitzungFileImpl);

        return sitzungFileImpl;
    }

    // etc. für die anderen Klassen (nur über factory erstellen, speichern und mappen)
    public Tagesordnungspunkt_File_Impl createTagesordnungspunkt(String topID, String sitzungsID) {


        // eindeutigen key erstellen, weil topID nur pro sitzung eindeutig ist-> mit sID kombinieren
        String topKey = sitzungsID + "_" + topID;

        if (topIDZuTop.containsKey(topKey)){
            return topIDZuTop.get(topKey);
        }

        Tagesordnungspunkt_File_Impl tagesordnungspunktFileImpl = new Tagesordnungspunkt_File_Impl(topKey,sitzungsID );

        tagesordnungspunktListe.add(tagesordnungspunktFileImpl);
        topIDZuTop.put(topKey, tagesordnungspunktFileImpl);

        return tagesordnungspunktFileImpl;
    }


    // hier mal javadoc doku ausprobiert, finde aber normale kommis auch gut, hoffentlich reicht das
    /**
     * Erstellt einen Redner anhand von id, ref, name etc.
     * @param rednerID
     * @param abgeordneter_FileImpl_ref
     * @param vorname
     * @param nachname
     * @param fraktion
     * @return am ende den redner dann
     */
    public Redner_File_Impl createRedner(String rednerID, Abgeordneter_File_Impl abgeordneter_FileImpl_ref, String vorname, String nachname, String fraktion) {

        if (rednerIDZuRedner.containsKey(rednerID)){
            return rednerIDZuRedner.get(rednerID);
        }

        Abgeordneter_File_Impl abgeRef = abgeordnetenIDZuAbgeordneter.get(rednerID);

        String fraktionClean = fraktion.replaceAll("\\s+", " ") // normales leerzeichen ersetzen
                .replace('\u00A0', ' ')  // es gibt auch noch diese geschützten leerzeichen anscheinend
                .trim().toUpperCase();

        Redner_File_Impl rednerFileImpl = new Redner_File_Impl(rednerID, abgeRef, vorname, nachname, fraktionClean);


//        if (fraktion == null) {
//            fraktion = "fraktionslos";
//        }


        // die redner per id ihrer fraktion zuweisen
        if (fraktion != null && !fraktion.isEmpty()) {
            Fraktion_File_Impl fraktionFile = createFraktion(fraktion);
            // wenn abgref == null ist dann nicht zur fraktion hinzufügen, weil gastredner keine fraktion haben
            if (abgeRef != null) {
                fraktionFile.addfrakMitglied(abgeRef);
            }
            else {
                fraktionFile.addfrakMitglied(null);
            }

        }

        rednerListe.add(rednerFileImpl);
        rednerIDZuRedner.put(rednerID, rednerFileImpl);


        return rednerFileImpl;
    }

    public Rede_File_Impl createRede(String redID, String topID, Redner_File_Impl redner) {
        if (redIDZuRede.containsKey(redID)){
            return redIDZuRede.get(redID);
        }





        Rede_File_Impl redeFileImpl = new Rede_File_Impl(redID, topID, redner);

        redenListe.add(redeFileImpl);
        redIDZuRede.put(redID, redeFileImpl);


        return redeFileImpl;

    }



    /**
     * Erstellt einen neuen Kommentar mit einer eindeutigen ID basierend auf redeID und laufender Nummer.
     *
     * @param redeID   id der rede vom kommentar
     * @param kInhalt  text vom kommentar (meist beifall, manchmal auch inhalt)
     * @return Der erstellte Kommentar mit neuer ID aus rede ID und laufender nummer
     */
    public Kommentar_File_Impl createKommentar(String redeID, String kInhalt) {
        // nummer basiert auf anzahl der kommentare in hashmap, initialisert mit 0
        int kommentarNummer = redeIDZuNummer.getOrDefault(redeID, 0) + 1;
        // eindeutige id mit rede ID und kommi nummer machen
        String kommentarID = redeID + "_" + kommentarNummer;
        // System.out.println("Generierte kID: " + kommentarID);

        // prüfe ob Kommentar mit dieser ID schon existiert
        if (kIDZuKommentar.containsKey(kommentarID)) {
            return kIDZuKommentar.get(kommentarID);
        }

        Kommentar_File_Impl kommentarFileImpl = new Kommentar_File_Impl(kommentarID, redeID, kInhalt);

        // kommi direkt Liste und Map hinzufügen
        kommentarListe.add(kommentarFileImpl);
        kIDZuKommentar.put(kommentarID, kommentarFileImpl);

        // nummer für die redeID aktualisieren
        redeIDZuNummer.put(redeID, kommentarNummer);

        return kommentarFileImpl;
    }



    // nice funktion für abgeordneter nach ID finden

    public Abgeordneter_File_Impl getAbgeordneterByID(String id) {
        return abgeordnetenIDZuAbgeordneter.getOrDefault(id, null);
    }

    // gleiches nochmal für mongo impl return
    public Abgeordneter_MongoDB_Impl getAbgeordneterMongoByID(String id) {
        return abgeordneterMongoDBByID.getOrDefault(id, null);
    }

    // hier nochmal für redner mongo impl
    public Redner_MongoDB_Impl getRednerMongoByID(String id) {
        return rednerMongoByID.getOrDefault(id, null);


    } // hier nochmal für kommi mongo impl
    public Kommentar_MongoDB_Impl getKommentarMongoByID(String id) {
        return kommentarMongoByID.getOrDefault(id, null);
    }

    // hier auch noch mal mit sitzung by ID und return mongo impl (für html)
    public Sitzung_MongoDB_Impl getSitzungMongoByID(String id) {
        return sitzungMongoDBByID.getOrDefault(id, null);
    }



    public Funktion createFunktion(String institution, String funktion) {
        return new Funktion(institution, funktion);
    }




    // ----------------- create methoden für die mongo db impls direkt mal als liste alles in einem ist besser ------------------

    // alle abge mongo impls erstellen und zur liste adden
    public void createAbgeordneterMongoDBList() {
        for (Document doc : abgeordneteDocs) {
            Abgeordneter_MongoDB_Impl impl = new Abgeordneter_MongoDB_Impl(dbHandler, doc);
            abgeordneterMongoDBList.add(impl);
            // map für id->mongo impl auffüllen
            abgeordneterMongoDBByID.put(impl.getAbgeordnetenID(), impl);
        }
    }

    // sittzung mongo impl füllen
    public void createSitzungMongoDBList() {
        for (Document doc : sitzungenDocs) {
            Sitzung_MongoDB_Impl impl = new Sitzung_MongoDB_Impl(dbHandler, doc);
            sitzungMongoDBList.add(impl);
            // hier noch die hahsmap stetig auffülklen mit id und impl
            sitzungMongoDBByID.put(impl.getsID(), impl);

        }
    }

    // alle tops db implgs machen und zur liste
    public void createTagesordnungspunktMongoDBList() {
        for (Document doc : topDocs) {
            Tagesordnungspunkt_MongoDB_Impl impl = new Tagesordnungspunkt_MongoDB_Impl(dbHandler, doc);
            tagesordnungspunktMongoDBList.add(impl);
        }
    }

    // alle rede mongo impls machen
    public void createRedeMongoDBList() {
        for (Document doc : redenDocs) {
            Rede_MongoDB_Impl impl = new Rede_MongoDB_Impl(dbHandler, doc);
            redeMongoDBList.add(impl);

        }
    }

    // alle redner mongos machen
    public void createRednerMongoDBList() {
        for (Document doc : rednerDocs) {
            Redner_MongoDB_Impl impl = new Redner_MongoDB_Impl(dbHandler, doc);
            rednerMongoDBList.add(impl);
            // hier noch die hahsmap stetig auffülklen mit id und impl
            rednerMongoByID.put(impl.getRednerID(), impl);
        }
    }

    // alle kommi mongos machen
    public void createKommentarMongoDBList() {
        for (Document doc : kommentarDocs) {
            Kommentar_MongoDB_Impl impl = new Kommentar_MongoDB_Impl(dbHandler, doc);
            kommentarMongoDBList.add(impl);
            // hier noch die hahsmap stetig auffülklen mit id und impl
            kommentarMongoByID.put(impl.getkID(), impl);
        }
    }

    // alle fraks mongos machen
    public void createFraktionMongoDBList() {
        for (Document doc : fraktionDocs) {
            Fraktion_MongoDB_Impl impl = new Fraktion_MongoDB_Impl(dbHandler, doc);
            fraktionMongoDBList.add(impl);
        }
    }

     //---------------------- getter für listen------------------------------

    // erst listen


    public List<Abgeordneter_File_Impl> getAbgeordneteListe() {
        return abgeordneteListe;
    }

    public List<Sitzung_File_Impl> getSitzungenListe() {
        return sitzungenListe;
    }

    public List<Tagesordnungspunkt_File_Impl> getTagesordnungspunktListe() {
        return tagesordnungspunktListe;
    }

    public List<Rede_File_Impl> getRedenListe() {
        return redenListe;
    }

    public List<Redner_File_Impl> getRednerListe() {
        return rednerListe;
    }

    public List<Kommentar_File_Impl> getKommentarListe() {
        return kommentarListe;
    }

    public List<Fraktion_File_Impl> getFraktionenListe() { return fraktionenListe;}



    // getter für die hashmaps (sehr praktisch)
    public Map<String, Abgeordneter_File_Impl> getAbgeordnetenIDZuAbgeordneter() {
        return abgeordnetenIDZuAbgeordneter;
    }

    public Map<String, Sitzung_File_Impl> getsIDZuSitzung() {
        return sIDZuSitzung;
    }

    public Map<String, Tagesordnungspunkt_File_Impl> getTopIDZuTop() {
        return topIDZuTop;
    }

    public Map<String, Redner_File_Impl> getRednerIDZuRedner() {
        return rednerIDZuRedner;
    }

    public Map<String, Rede_File_Impl> getRedIDZuRede() {
        return redIDZuRede;
    }

    public Map<String, List<Rede_File_Impl>> getRednerIDZuReden() {
        return rednerIDZuReden;
    }


    //  ---------------- getter für doc listen --------------

    public List<Document> getFraktionDocs() {
        return fraktionDocs;
    }

    public List<Document> getAbgeordneteDocs() {
        return abgeordneteDocs;
    }

    public List<Document> getSitzungenDocs() {
        return sitzungenDocs;
    }

    public List<Document> getTopDocs() {
        return topDocs;
    }

    public List<Document> getRedenDocs() {
        return redenDocs;
    }

    public List<Document> getRednerDocs() {
        return rednerDocs;
    }

    public List<Document> getKommentarDocs() {
        return kommentarDocs;
    }




    // ------------------ alle getter für die mongo impls listen -........................ -------
    public List<Abgeordneter_MongoDB_Impl> getAbgeordneterMongoDBList() {
        return abgeordneterMongoDBList;
    }

    public List<Sitzung_MongoDB_Impl> getSitzungMongoDBList() {
        return sitzungMongoDBList;
    }

    public List<Tagesordnungspunkt_MongoDB_Impl> getTagesordnungspunktMongoDBList() {
        return tagesordnungspunktMongoDBList;
    }

    public List<Rede_MongoDB_Impl> getRedeMongoDBList() {
        return redeMongoDBList;
    }

    public List<Redner_MongoDB_Impl> getRednerMongoDBList() {
        return rednerMongoDBList;
    }

    public List<Kommentar_MongoDB_Impl> getKommentarMongoDBList() {
        return kommentarMongoDBList;
    }

    public List<Fraktion_MongoDB_Impl> getFraktionMongoDBList() {
        return fraktionMongoDBList;
    }


    // ------------------------ DATENBANK OPERATIONEN _________________________________



    // ---------- erstmal alle file impl listen -> doc listen (mongo impls) funktionen: ---------

    // abge file impl liste in abge doc liste
    public void abgeListToDoc() {
        for (Abgeordneter_File_Impl abgeordneter : abgeordneteListe) {
            abgeordneteDocs.add(abgeordneter.toDoc());
        }
    }

    // sitzungen liste aus file impls mit todoc in docs (mongo docs = mongo impls)
    public void sitzListToDocs() {
        for (Sitzung_File_Impl sitzung : sitzungenListe) {
            sitzungenDocs.add(sitzung.toDoc());
        }
    }

    // tops in docs
    public void topListeToDocs() {
        for (Tagesordnungspunkt_File_Impl top : tagesordnungspunktListe) {
            topDocs.add(top.toDoc());
        }
    }

    // reden file impls in docs
    public void redeListetoDocs(){
        for (Rede_File_Impl rede : redenListe) {
            redenDocs.add(rede.toDoc());
        }
    }

    // redner file impls in docs
    public void rednerListeToDocs() {
        rednerDocs.clear();
        for (Redner_File_Impl redner : rednerListe) {
            rednerDocs.add(redner.toDoc());
        }
    }

    // komms aus liste zu docliste
    public void kommListeToDocs(){
        for (Kommentar_File_Impl kommentar : kommentarListe) {
            kommentarDocs.add(kommentar.toDoc());
        }
    }

    // fraks aus file impls liste zu docs liste (mongo impls dann schon?)
    public void frakListeToDocs() {
        for (Fraktion_File_Impl fraktion : fraktionenListe) {
            fraktionDocs.add(fraktion.toDoc());
        }
    }




    // ---------------------- jetzt alle methoden um die doc listen in die db zu adden mit dbhandler und addDocs (insermany()) -------------------------
    // abge docs in db laden
    public void addabgeDocsToDB() {
        dbHandler.clearCollection("Abgeordnete"); // Sammlung vor dem Einfügen leeren
//        System.out.println("Speichere " + abgeordneteDocs.size() + " Dokumente in die Collection 'Abgeordnete'");
//        System.out.println();

        dbHandler.addDocuments("Abgeordnete", abgeordneteDocs);
    }

    // siotzdocs in db aden
    public void addsitzDocsToDB() {
        dbHandler.addDocuments("Sitzungen", sitzungenDocs);
    }

    // top docs in die db adden
    public void addtopDocsToDB() {
        dbHandler.addDocuments("Tagesordnungspunkte", topDocs);
    }

    // reden docs in db
    public void addredenDocsToDB() {
        dbHandler.addDocuments("Reden", redenDocs);
    }

    // redner in db
    public void addrednerDocsToDB() {
        dbHandler.addDocuments("Redner", rednerDocs);
    }

    // kommis auch in db
    public void addkommDocsToDB() {
        dbHandler.addDocuments("Kommentare", kommentarDocs);
    }

    // fraks in db
    public void addfrakDocsToDB() {
        dbHandler.addDocuments("Fraktionen", fraktionDocs);
    }

    // ------------------------ adnersrum auch: also von der db zurück zu docs listen (und dann zu mongo impls) ----------


    // hier einfach erstmal die docs wieder clearen (sonst alles doppelt)
    // und dann einfach mit dbhandler alle docs aus der collection ziehen und mit addAll zu den docs hinzufügen wieder
    // sollte eigentlich nix geändert haben, nur jetzt ist die quelle der docs eben die db
    // +++ gleiches gilt für alle diese methoden ++++++
    public void loadAbgeordneteDocsFromDB() {
        abgeordneteDocs.clear();
        abgeordneteDocs.addAll(dbHandler.getAllDocs("Abgeordnete")); // docs aus MongoDB holen
    }


    public void loadRednerDocsFromDB() {
        rednerDocs.clear();
        rednerDocs.addAll(dbHandler.getAllDocs("Redner"));
    }

    public void loadRedeDocsFromDB() {
        redenDocs.clear();
        redenDocs.addAll(dbHandler.getAllDocs("Reden"));
    }


    public void loadSitzungenDocsFromDB() {
        sitzungenDocs.clear();
        sitzungenDocs.addAll(dbHandler.getAllDocs("Sitzungen"));
    }

    public void loadFraktionenDocsFromDB() {
        fraktionDocs.clear();
        fraktionDocs.addAll(dbHandler.getAllDocs("Fraktionen"));
    }

    public void loadTopDocsFromDB() {
        topDocs.clear();
        topDocs.addAll(dbHandler.getAllDocs("Tagesordnungspunkte"));
    }

    public void loadKommDocsFromDB() {
        kommentarDocs.clear();
        kommentarDocs.addAll(dbHandler.getAllDocs("Kommentare"));
    }

}

