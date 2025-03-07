import Database.MongoDBHandler;
import Factory.MainFactory;
import Rest.RESTHandler;
import Rest.javalinConfig;

import java.io.IOException;


public class Main {

    /**
     * Main hier einfach ausführen um die webseite aufzurufen
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {


        // db handler und mainfac machen
        MongoDBHandler dbHandler = new MongoDBHandler();
        dbHandler.connectToDB();

        MainFactory factory = new MainFactory(dbHandler);

        // javalin config machen
        javalinConfig config = new javalinConfig();

        // resthandler und routes aufsetzten
        RESTHandler restHandler = new RESTHandler(config, factory);
        restHandler.routes(restHandler.getJavalin());


        System.out.println("Server läuft auf Port 7070");
    }
}