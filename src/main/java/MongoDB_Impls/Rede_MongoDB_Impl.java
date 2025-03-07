package MongoDB_Impls;

import Interfaces.IRede;
import Database.MongoDBHandler;
import Klassen.RedeAbschnitt;
import org.apache.uima.cas.CASException;
import org.apache.uima.resource.ResourceInitializationException;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

// neu für toCAS methode jcas kram importieren
import org.apache.uima.jcas.JCas;
import org.apache.uima.fit.factory.JCasFactory;

public class Rede_MongoDB_Impl implements IRede<String, String> {

    private final MongoDBHandler dbHandler;
    private final String collectionName = "Reden";
    private final Document doc;

    private String redID;
    private String topID;
    // private String redInhalt;
    private String rednerID; // link zu Redner über ID
    // private List<String> redKommentare; // list von kommi IDs
    private List<RedeAbschnitt> abschnitte;

    public Rede_MongoDB_Impl(MongoDBHandler dbHandler, Document document) {
        this.dbHandler = dbHandler;
        this.doc = document;

        // Initialisierung aus dem MongoDB-Dokument
        this.redID = doc.getString("_id");
        this.topID = doc.getString("topID");
        //this.redInhalt = doc.getString("inhalt");
        this.rednerID = doc.getString("rednerID");
        //this.redKommentare = doc.getList("kommentare", String.class);

        // abschnitte laden und dann wieder in die redeabschnitt klassen umwandeln und in liste packen
        this.abschnitte = new ArrayList<>();
        List<Document> abschnittDocs = doc.getList("abschnitte", Document.class);
        for (Document abschnittDoc : abschnittDocs) {
            boolean isKommentar = abschnittDoc.getBoolean("isKommentar");
            String inhalt = abschnittDoc.getString("inhalt");
            String kommentarID = abschnittDoc.getString("kommentarID");
            if (isKommentar) {
                abschnitte.add(new RedeAbschnitt(inhalt, kommentarID));
            } else {
                abschnitte.add(new RedeAbschnitt(inhalt));
            }


        }
    }


    public List<RedeAbschnitt> getAbschnitte() {
        return abschnitte;
    }

    @Override
    public String getRedID() {
        return redID;
    }

    @Override
    public void setRedID(String redID) {
        dbHandler.updateField(collectionName, "_id", this.redID, "_id", redID);
        doc.put("_id", redID);
        this.redID = redID;
    }

    @Override
    public String getTopID() {
        return topID;
    }

    @Override
    public void setTopID(String topID) {
        dbHandler.updateField(collectionName, "_id", this.redID, "topID", topID);
        doc.put("topID", topID);
        this.topID = topID;
    }

//    @Override
//    public String getRedInhalt() {
//        return redInhalt;
//    }
//
//    @Override
//    public void setRedInhalt(String redInhalt) {
//        dbHandler.updateField(collectionName, "_id", this.redID, "inhalt", redInhalt);
//        doc.put("inhalt", redInhalt);
//        this.redInhalt = redInhalt;
//    }

    public String getRedner() {
        return rednerID;
    }

    public void setRedner(String rednerID) {
        dbHandler.updateField(collectionName, "_id", this.redID, "rednerID", rednerID);
        doc.put("rednerID", rednerID);
        this.rednerID = rednerID;
    }

//    @Override
//    public List<String> getRedKommentare() {
//        return redKommentare;
//    }




    // to HTML Methode mit string builder um die reden htmls zu erstellen


    // erstmal normal nur für die Infos, danach dann sitzungs infos mit toHTML in Sitzung
    // und unten dann toHTML2 für inhalt
    public String toHTML() {
        StringBuilder html = new StringBuilder();

        html.append("<div id='rede").append(redID).append("'>") // hier ist dann der ankerpunkt für den sprung
                // .append("<p><strong>Redner ID:</strong> ").append(rednerID).append("</p>")
                .append("<h3>Rede ID: ").append(redID).append("</h3>")
                .append("<p><strong>Top ID:</strong> ").append(topID).append("</p>");

        return html.toString();
    }

    // hier toHTML2 für den redeInhalt
    public String toHTML2() {
        StringBuilder html = new StringBuilder();

        html.append("<p><strong>Inhalt:</strong></p>");
        for (RedeAbschnitt abschnitt : abschnitte) {
            if (abschnitt.isKommentar()) {
                html.append("<span style='background-color: #7ab6e5; color: #07263e;'>")
                        .append(abschnitt.getInhalt())
                        .append(")</span><br>");
            } else {
                html.append("<p>").append(abschnitt.getInhalt()).append("</p>");
            }
        }

        return html.toString();
    }


    /**
     * Neue Methode toCAS um die mongo_impl in ein jCAS (java Cas Objekt) umzuwandeln mit text, metadaten und allem
     * @return eben die jCAS Objekt Datei
     * @throws ResourceInitializationException wurde mir von IntelliJ empfohlen
     * @throws CASException scheint Sinn zu machen
     */

    public JCas toCAS() throws ResourceInitializationException, CASException {
        // erstmal leeres jCas mit der factory createn
        JCas cas = JCasFactory.createJCas();

        // wieder inhalt und kommentar als einen string vereinen für documenttext
        StringBuilder kombiText = new StringBuilder();
        for (RedeAbschnitt abschnitt : abschnitte) {
                kombiText.append(abschnitt.getInhalt()).append("\n");
        }

        // text und lang setzen
        cas.setDocumentText(kombiText.toString());
        cas.setDocumentLanguage("de");

        // text view erstellen und mit initial text füllen (hat iwie nicht automatisch geklappt mit spacy, also mach ichs so)
        JCas textView = cas.createView("Text");
        JCas initialView = cas.getView("_InitialView");

        textView.setDocumentText(initialView.getDocumentText());


        return cas;
    }



}