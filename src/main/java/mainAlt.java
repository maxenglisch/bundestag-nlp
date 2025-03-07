import Database.MongoDBHandler;
import Factory.MainFactory;

import java.io.IOException;

public class mainAlt {


    /**
     * Meine Main Methode, einfach ausführen und dann wird die Datenbank aus den XMLs
     * beladen und dann daraus die HTML seiten generiert, je nach Aufruf mit verschiedener Sortierung
     * z.B. "vorname", "nachname", "id", "geschlecht"
     * @param args
     * @throws IOException falls was beim IO nicht klappt etc.
     */
    public static void main(String[] args) throws IOException {


        // erstmal den db handler initialisiren und mit der db verbinden
        MongoDBHandler dbHandler = new MongoDBHandler();
        dbHandler.connectToDB();
        // erstmal datebank bereinigen am anfang, dann wieder neu beladen
        // dbHandler.clearAllCollections();
        dbHandler.clearCollection("Kommentare");

        System.out.println("DB verbunden und bereinigt");

        // dann das wichtigste: die main factory mit dem dbhandler als übergabe
        MainFactory factory = new MainFactory(dbHandler);


        // ------------------ erstmal alle XMLs parsen und in den file impls speichern
        System.out.println("-----  XMLs parsen ------ ");
        try {
//            factory.loadAbgeordneteFromXML("src/main/resources/MDB_STAMMDATEN.xml");
//            System.out.println("Abgeordnete geparsed: " + factory.getAbgeordneteListe().size());

            // loop durch die sitzungs xmls
            String dirPath = "src/main/resources/sitzungen/";
            String xmlFile = ".xml";

            for (int i = 1; i <= 197; i++) {
                String filepath = dirPath + i + xmlFile;
                factory.loadSitzungenFromXML(filepath);
            }

            // hier als infos wie groß die file impl listen sind also wie viele impls erstellt (geparsed wurden)
//            System.out.println("Sitzungen geparsed: " + factory.getSitzungenListe().size());
//            System.out.println("Tops geparsed: " + factory.getTagesordnungspunktListe().size());
//            System.out.println("Reden geparsed: " + factory.getRedenListe().size());
//            System.out.println("Redner geparsed: " + factory.getRednerListe().size());
            System.out.println("Kommntare geparsed: " + factory.getKommentarListe().size());
            //System.out.println("Fraktionen geparsed: " + factory.getFraktionenListe().size());

        } catch (Exception e) {
            throw new RuntimeException("Fehler beim parsen: " + e.getMessage(), e);
        }


//        // ------ auch noch die bilder JSOn parsen
//        System.out.println("------- Bilder JSON parsen ---------");
//        try{
//
//            bildParser bildParser = new bildParser("src/main/resources/mpPictures.json");
//
//            for (Abgeordneter_File_Impl abgeordneter : factory.getAbgeordneteListe()) {
//                String bildURL = bildParser.getBildURL(abgeordneter.getAbgeordnetenID());
////                if (bildURL != null) {
//                // hab kein null mehr, dann einfach standard url genommen für fehler anzeige
//                    abgeordneter.setBildURL(bildURL);
//                    // System.out.println(abgeordneter.getBildURL());
//                // }
//            }
//

//        } catch (Exception e){
//            throw new RuntimeException("Fehler beim parsen: + " + e.getMessage(), e);
//        }




        // ------------- dann die file impls in docs umwandeln und in die DB laden
        System.out.println("-----  Listen in Docs umwandeln ---- ");
        try {
//            factory.abgeListToDoc();
//            System.out.println("Abgeordnete docs: " + factory.getAbgeordneteDocs().size());
//
//
//
//
//            factory.sitzListToDocs();
//            System.out.println("Sitzungs docs: " + factory.getSitzungenDocs().size());
//
//            factory.topListeToDocs();
//            System.out.println("Tagesordnungspunkte docs: " + factory.getTopDocs().size());
//
//
//
//            factory.redeListetoDocs();
//            System.out.println("Reden docs: " + factory.getRedenDocs().size());
//
//
//            factory.rednerListeToDocs();
//            System.out.println("Redner docs: " + factory.getRednerDocs().size());
//

            factory.kommListeToDocs();
            System.out.println("Kommentare docs: " + factory.getKommentarDocs().size());



//            factory.frakListeToDocs();
//            System.out.println("Fraktionen docs: " + factory.getFraktionDocs().size());
//

        } catch (Exception e) {
            throw new RuntimeException("Fehler beim umwandeln in docs: " + e.getMessage(), e);
        }

        // --------- jetzt die docs in die db laden ------------
        System.out.println("------ Lade docs in die db -----");
        try {

            // hier dann immer in db laden und dann doks zählen, dass auch alle geladen werden und printen

//            factory.addabgeDocsToDB();
//            long abgeCount = dbHandler.getCollection("Abgeordnete").countDocuments();
//            System.out.println("Anzahl Abgeordnete docs: " + abgeCount);
//
//
//
//
//            factory.addsitzDocsToDB();
//            long sitzCount = dbHandler.getCollection("Sitzungen").countDocuments();
//            System.out.println("Anzahl sitz docs: " + sitzCount);
//
//            factory.addtopDocsToDB();
//            long topCount = dbHandler.getCollection("Tagesordnungspunkte").countDocuments();
//            System.out.println("Anzahl top docs: " + topCount);
//
//
//            factory.addredenDocsToDB();
//            long redenCount = dbHandler.getCollection("Reden").countDocuments();
//            System.out.println("Anzahl reden docs: " + redenCount);
//
//
//
//            factory.addrednerDocsToDB();
//            long rednerCount = dbHandler.getCollection("Redner").countDocuments();
//            System.out.println("Anzahl redner docs: " + rednerCount);


            factory.addkommDocsToDB();
            long kommCount = dbHandler.getCollection("Kommentare").countDocuments();
            System.out.println("Anzahl kommi docs: " + kommCount);



//            factory.addfrakDocsToDB();
//            long frakCount = dbHandler.getCollection("Fraktionen").countDocuments();
//            System.out.println("Anzahl frak docs: " + frakCount);
//

        } catch (Exception e) {
            System.err.println("Fehler beim laden der docs in db: " + e.getMessage());
        }





//        // --- ------ jetzt alle docs laden aus der db (über factory, über dbhandlr) ------------------
//        System.out.println("--------------- Lade die docs wieder aus der db raus-------------");
//        try {
//
//            // hier dann alle docs nochmal aus db laden und dann zählen dass nix verloren gegangen ist
//
//
//            factory.loadAbgeordneteDocsFromDB();
//            System.out.println("Abgeordnete Docs aus DB: " + factory.getAbgeordneteDocs().size());
//
//            factory.loadSitzungenDocsFromDB();
//            System.out.println("situngen Docs aus DB: " + factory.getSitzungenDocs().size());
//
//            factory.loadTopDocsFromDB();
//            System.out.println("tops Docs aus DB: " + factory.getTopDocs().size());
//
//
//            factory.loadRedeDocsFromDB();
//            System.out.println("rede Docs aus DB: " + factory.getRedenDocs().size());
//
//            factory.loadRednerDocsFromDB();
//            System.out.println("redner Docs aus DB: " + factory.getRednerDocs().size());
//
//
//            // kommentare benutze ich eh eigentlich nicht weiter
//            // factory.loadKommDocsFromDB();
//
//
//            factory.loadFraktionenDocsFromDB();
//            System.out.println("frak Docs aus DB: " + factory.getFraktionDocs().size());
//
//        } catch (Exception e){
//            System.err.println("Fehler beim laden der docs aus der db" + e.getMessage());
//
//        }
//
//
//        //---------------  mongo db impls listen füllen --------------
//        System.out.println("------------ Erstelle Mongo DB Impls aus den Doc listen aus der DB: --------");
//        try {
//
//            factory.createAbgeordneterMongoDBList();
//            System.out.println("Abgeordnete mongo db impls: "+ factory.getAbgeordneterMongoDBList().size());
//
//
//
//            factory.createSitzungMongoDBList();
//            System.out.println("sitzung mongo db impls: "+ factory.getSitzungMongoDBList().size());
//
//
//            factory.createTagesordnungspunktMongoDBList();
//            System.out.println("tops mongo db impls: "+ factory.getTagesordnungspunktMongoDBList().size());
//
//            factory.createRedeMongoDBList();
//            System.out.println("rede mongo db impls: "+ factory.getRedeMongoDBList().size());
//
//
//            factory.createRednerMongoDBList();
//            System.out.println("redner mongo db impls: "+ factory.getRednerMongoDBList().size());
//
//
//
//            factory.createFraktionMongoDBList();
//            System.out.println("fraks mongo db impls: "+ factory.getFraktionMongoDBList().size());
//
//
//
//
//        } catch (Exception e){
//            System.err.println("Fehler beim füllen der mongo impl listen" + e.getMessage());
//
//        }

//
//        // htmlmaker erstellen
//        HTMLMaker maker = new HTMLMaker(factory);
//
//        // output directory machen (eifnach html)
//        String outputDir = "out/html/";
//
//
//        // alle portfolioes generaten
//
//        // -------------- bonusaufgabe -------------------
//
//        // --------------- hier kann man auch unterschiedlich sortieren: "vorname", "nachname", "geschlecht", "id" z.B. --------
//        maker.generatePortfolios(outputDir, "vorname");

        // am ende db connection closen
        dbHandler.closeConnection();
    }
}
