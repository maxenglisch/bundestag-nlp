package Database;


import com.mongodb.client.*;
import org.bson.Document;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import java.util.Date;
import java.text.SimpleDateFormat;



import static com.mongodb.client.model.Filters.eq;

import static com.mongodb.client.model.Updates.set;



public class MongoDBHandler {

    private MongoClient client;
    private MongoDatabase db;


    public void connectToDB() throws IOException {

        // erstmal properties parametrisiert auslesen für URI
        Properties properties = new Properties();
        properties.load(new FileInputStream("src/main/resources/db.properties"));

        String host = properties.getProperty("remote_host");
        String dbName = properties.getProperty("remote_database");
        String user = properties.getProperty("remote_user");
        String pw = properties.getProperty("remote_password");
        int port = Integer.parseInt(properties.getProperty("remote_port"));

        String uri = String.format("mongodb://%s:%s@%s:%d/%s", user, pw, host, port, dbName);


        // Verbinden (aus tut)
        try {
            this.client = MongoClients.create(uri);
            this.db = client.getDatabase(dbName);
            System.out.println("Verbindung hergestellt zu MongoDB: " + dbName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Datenbankverbindung schließen
    public void closeConnection() {
        if (client != null) {
            client.close();
            System.out.println("Datenbankverbindung geschlossen.");
        }
    }

    public MongoDatabase getDb() {
        return db;
    }

    public MongoCollection<Document> getCollection(String collectionName) {
        return db.getCollection(collectionName);
    }

    // hatte es erst so wie im tut, aber mache glaub lieber methoden jetzt für die einzelnen sachen (erstellen, löschen etc)

//        // Dokumente hinzufügen (aus tut)
//        org.bson.Document docZumAdden = new org.bson.Document().
//                append("_id", new ObjectId()).
//                append("title", "test").
//                append("testArray", Arrays.asList("C, D"));
//
//        collection.insertOne(docZumAdden);
//
//        // Documente lesen (aus tut) mit filter
//
//        Document doc = collection.find(eq("title", "test")).first();
//        System.out.println(doc.get("title"));
//        System.out.println(doc.toJson());


    // doc hinzufügen
    public void addDocument(String collectionName, Document doc) {
        MongoCollection<Document> collection = db.getCollection(collectionName);
        collection.insertOne(doc);
        // System.out.println("Dokument hinzugefügt zu Collection "+ collectionName + ":   " + doc.toJson());

    }

    // mehrere docs auf einmal hinzufügen
    public void addDocuments(String collectionName, List<Document> docs) {
        for (Object doc : docs) {
            if (!(doc instanceof Document)) {
                throw new IllegalArgumentException("Fehler: Nicht-Document-Objekt in der Liste gefunden: " + doc.getClass());
            }
        }

        MongoCollection<Document> collection = db.getCollection(collectionName);
        collection.insertMany(docs);
        // System.out.println("Dokumente hinzugefügt: " + docs.size());
    }


    // doc finden und zurückgeben (falls vorhanden) mit find.first() und equals
    public Document findDocument(String collectionName, String field, String value) {

        MongoCollection<Document> collection = db.getCollection(collectionName);
        Document doc = collection.find(eq(field, value)).first();
        if (doc != null) {
            // System.out.println("dok gefunden: " + doc.toJson() + "in Collection: " + collection);
        } else {
            // System.out.println("Kein dok mit " + field + " = " + value + " gefunden");
        }
        return doc;
    }

    // mehrere docs finden in der collection mit einem filter
    public List<Document> findDocuments(String collectionName, Document filter) {
        MongoCollection<Document> collection = db.getCollection(collectionName);
        // hier dann find mit filter und in neue array list einfügen und returnen
        List<Document> docList = new ArrayList<>();
        collection.find(filter).into(docList);
        return docList;
    }

    // alle docs ausgeben einer collection
    public List<Document> getAllDocs(String collectionName) {
        MongoCollection<Document> collection = db.getCollection(collectionName);
        List<Document> documents = new ArrayList<>();
        // alle finden und in documents liste packen
        collection.find().into(documents);
        return documents;
    }

    // docs zählen mit long statt int (die brauchen long)
    public long countDocuments(String collectionName) {

        MongoCollection<Document> collection = db.getCollection(collectionName);
        long counter = collection.countDocuments();
        // System.out.println("Anzahl docs in Collection "+ collection +":   " + counter);
        return counter;
    }


    // ein feld im doc updaten (für setter aus mongo impl)
    public void updateField(String collectionName, String field, String value, String updateField, String updateValue) {
        MongoCollection<Document> collection = db.getCollection(collectionName);

        // in der collection das _id field (mongo intern) suchen mit der übergebenen value und dann neues feld überschreiben mit neue value
        collection.updateOne(eq(field, value), set(updateField, updateValue));

        // System.out.println("field aktualisiert: " + updateField + " = " + updateValue + " in collection: "+collectionName);
    }

    /**
     * Hier nochmal update Field nur mit Liste (brauche ich für fraks und abges zu kommi zuweisen
     * @param collectionName name der colection, wichtig
     * @param field feld zum auswählen (_id)
     * @param value
     * @param updateField
     * @param updateValue hier dann eben liste mit strings statt string wie oben
     */
    public void updateFieldWithList(String collectionName, String field, String value, String updateField, List<String> updateValue) {
        MongoCollection<Document> collection = db.getCollection(collectionName);

        // aktualisieren mit liste
        collection.updateOne(eq(field, value), set(updateField, updateValue));

        // System.out.println("liste aktualisiert: " + updateField + " = " + updateValue + " in collection: " +collectionName);
    }

    // doc updaten
    public void updateDocument(String collectionName, String field, String value, Document updatedDocument) {
        MongoCollection<Document> collection = db.getCollection(collectionName);
        collection.replaceOne(eq(field, value), updatedDocument);
        // System.out.println("doc aktualisiert: " + updatedDocument.toJson());
    }


    // doc löschen
    public void deleteDocument(String collectionName, String field, String value) {
        MongoCollection<Document> collection = db.getCollection(collectionName);

        collection.deleteOne(eq(field, value));
        // System.out.println("doc mit " + field + " = " + value + " gelöscht");
    }

    // collection clearen (sehr praktisch, dann muss ich nicht immer im compass alles löschen
    public void clearCollection(String collectionName) {
        MongoCollection<Document> collection = db.getCollection(collectionName);
        collection.deleteMany(new Document());
        //System.out.println("collection '" + collectionName + "' gecleared.");
    }

    // noch praktischer weil s.o.
    public void clearAllCollections() {
        for (String collectionName : db.listCollectionNames()) {
            clearCollection(collectionName);
        }
        //System.out.println("alle collections gecleared.");
    }


    /**
     * // neu für Aufgabe 3: Hier kann man in MongoDB so coole Indizes erstellen (quasi wie ein Wörterbuch dann sortiert nach Anfangsbuchstabe)
     *     // und dan eben danach suchen mit volltextsuche, ähnlich wie in mongoDB compass, nur hier halt in code statt GUI
     *     // also eben mit query und dann ist index top für die suche
     * @param collectionName gibt die collection an für die ich den index erstelle, erstmal nur abgeordnete
     *                       vielleicht dann auch noch reden wenn ich lust hab
     */

    public void createIndex(String collectionName){
        // hier mongocollection nehmen
        MongoCollection<Document> collection = db.getCollection(collectionName);


        // eigenes doc für die indexierung/indizierung? erstmal nur vorname, nachname, frak
        // jetzt auch noch mehr wie beruf und sowas
        Document indexKeys = new Document("vorname", "text").append("nachname", "text")
                .append("fraktion", "text").append("beruf", "text").append("geburtsort", "text").append("religion", "text");

        collection.createIndex(indexKeys);
    }

    /**
     * Hier jetzt die volltextsuche implementieren mit index
     * @param collectionName übergeben für welche collection gesucht wird (falls ich auch noch reden machen will)
     * @param searchText hier dann eben der query als searchtext
     * @return am ende eine liste von results die eben dem text matchen
     */

    public List<Document> textSearch(String collectionName, String searchText) {

        // erstmal collection holen
        MongoCollection<Document> collection = db.getCollection(collectionName);

        // dann neue docuemt list machen für results
        List<Document> results = new ArrayList<>();

        // hier mit .find von collection arbeiten und neue docs erstellen, die eben den $text parameter
        // von oben haben und dann mit $search halt suchen (mongoDB syntax)
        collection.find(new Document("$text", new Document("$search", searchText))).into(results);

        return results;
    }


    /**
     * Neue methode für die historie, einfach alle änderung loggen und in db speichern
     * @param typ hier entweder "bild" oder "kommi" oder sowas halt
     * @param beschreibung hier dann was geändert wurde, also bild url zu neuer oder zuweisung
     * @param betroffenerID hier dann abge id von bild oder kommi id von kommi
     */
    public void addToHistory(String typ, String beschreibung, String betroffenerID) {
        MongoCollection<Document> collection = db.getCollection("Historie");

        // hier erstmal zeitstempel ordentlich formatieren, sonst klappt gar nix
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.y, HH:mm:ss");
        String formattedDate = dateFormat.format(new Date());

        System.out.println(formattedDate);

        Document log = new Document()
                .append("zeitstempel", formattedDate)
                .append("typ", typ)
                .append("beschreibung", beschreibung)
                .append("betroffenerID", betroffenerID);

        collection.insertOne(log);
    }





}