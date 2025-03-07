import Factory.MainFactory;
import Database.MongoDBHandler;
import Rest.RESTHandler;
import Rest.javalinConfig;

import java.io.IOException;

public class Main_ue3 {


    /**
     * Main methode die alles ausführt, die macht eigentlich alles: db verbinden, alle docs und mongos laden und dann den resthandler starten, der macht dann den rest
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        // erstmal mit der db verbinden
        MongoDBHandler dbHandler = new MongoDBHandler();
        dbHandler.connectToDB();

        // hier einmal index erstellen für suche später (auskommentiert weil schon angelegt)
        // dbHandler.createIndex("Abgeordnete");

        // mainfactory für alles wichtige
        MainFactory factory = new MainFactory(dbHandler);


        // --- ------ jetzt alle docs laden aus der db (über factory, über dbhandlr) ------------------
        System.out.println("--------------- Lade die docs aus der db raus-------------");
        try {

            // hier dann alle docs nochmal aus db laden und dann zählen dass nix verloren gegangen ist


            factory.loadAbgeordneteDocsFromDB();
            System.out.println("Abgeordnete Docs aus DB: " + factory.getAbgeordneteDocs().size());

            factory.loadSitzungenDocsFromDB();
            System.out.println("situngen Docs aus DB: " + factory.getSitzungenDocs().size());

            factory.loadTopDocsFromDB();
            System.out.println("tops Docs aus DB: " + factory.getTopDocs().size());


            factory.loadRedeDocsFromDB();
            System.out.println("rede Docs aus DB: " + factory.getRedenDocs().size());

            factory.loadRednerDocsFromDB();
            System.out.println("redner Docs aus DB: " + factory.getRednerDocs().size());


            factory.loadKommDocsFromDB();
            System.out.println("kommi Docs aus DB: " + factory.getKommentarDocs().size());


            factory.loadFraktionenDocsFromDB();
            System.out.println("frak Docs aus DB: " + factory.getFraktionDocs().size());

        } catch (Exception e){
            System.err.println("Fehler beim laden der docs aus der db" + e.getMessage());

        }


        //---------------  mongo db impls listen füllen --------------
        System.out.println("------------ Erstelle Mongo DB Impls aus den Doc listen aus der DB: --------");
        try {

            factory.createAbgeordneterMongoDBList();
            System.out.println("Abgeordnete mongo db impls: "+ factory.getAbgeordneterMongoDBList().size());



            factory.createSitzungMongoDBList();
            System.out.println("sitzung mongo db impls: "+ factory.getSitzungMongoDBList().size());


            factory.createTagesordnungspunktMongoDBList();
            System.out.println("tops mongo db impls: "+ factory.getTagesordnungspunktMongoDBList().size());

            factory.createRedeMongoDBList();
            System.out.println("rede mongo db impls: "+ factory.getRedeMongoDBList().size());


            factory.createRednerMongoDBList();
            System.out.println("redner mongo db impls: "+ factory.getRednerMongoDBList().size());


            factory.createKommentarMongoDBList();
            System.out.println("kommi mongo db impls: "+ factory.getKommentarMongoDBList().size());


            factory.createFraktionMongoDBList();
            System.out.println("fraks mongo db impls: "+ factory.getFraktionMongoDBList().size());




        } catch (Exception e){
            System.err.println("Fehler beim füllen der mongo impl listen" + e.getMessage());

        }

        // javalin config machen
        javalinConfig config = new javalinConfig();

        // resthandler für alles dynamische web krams
        RESTHandler restHandler = new RESTHandler(config, factory);

        // alle routen aufbauen
        restHandler.routes(restHandler.getJavalin());

        System.out.println("Server läuft auf Port 7070");




    }
}
