package MongoDB_Impls;

import Database.MongoDBHandler;
import Interfaces.IAbgeordneter;
import Klassen.Funktion;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class Abgeordneter_MongoDB_Impl implements IAbgeordneter<String> {

    private final MongoDBHandler dbHandler;
    private final String collectionName = "Abgeordnete"; //  Collection für Abgeordnete
    private final Document doc; //  zugehöriges MongoDB doc


    private String vorname;
    private String nachname;
    private String gebDatum;
    private String gebOrt;
    private String geschlecht;


    private String abgeordnetenID;
    private String fraktion;
    private String anrede;
    private String akademTitel;
    private String familienstand;
    private String religion;
    private String beruf;

    // politische infos: wahlkreis etc.
    private String wkrLand;
    private int wkrNummer;
    private String mandatsArt;
    private List<String> funktionen;

    // bild auch in db
    private String bildURL;






    public Abgeordneter_MongoDB_Impl(MongoDBHandler dbHandler, Document document) {
        this.dbHandler = dbHandler;
        this.doc = document;

        this.vorname = doc.getString("vorname");
        this.nachname =  doc.getString("nachname");
        this.gebDatum = doc.getString("geburtsdatum");
        this.gebOrt = doc.getString("geburtsort");
        this.geschlecht = doc.getString("geschlecht");

        this.abgeordnetenID = doc.getString("_id");
        this.anrede = doc.getString("anrede");
        this.akademTitel = doc.getString("akademischer_titel");
        this.familienstand = doc.getString("familienstand");
        this.religion = doc.getString("religion");
        this.beruf = doc.getString("beruf");
        this.fraktion = doc.getString("fraktion");
        this.wkrLand = doc.getString("wahlkreis_land");
        this.wkrNummer = (doc.getInteger("wahlkreis_nummer"));
        this.mandatsArt = doc.getString("mandatsart");
        this.funktionen = doc.getList("funktionen", String.class);

        this.bildURL = doc.getString("bildURL");
    }




    // --------------- erst noch getter und setter von Person (file impl erbt von person -> hat die) ----------------

    public String getVorname() {
        return vorname;
    }


    // erst in die db, dann lokal ändern (db ist wichtiger)
    public void setVorname(String vorname) {
        // update field aufrufen mit id vom aktuellen doc (abgeordneter impl) und dann vorname setten
        dbHandler.updateField(collectionName, "_id", doc.getString("_id"), "vorname", vorname);
        doc.put("vorname", vorname);
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        dbHandler.updateField(collectionName, "_id", doc.getString("_id"), "nachname", nachname);
        doc.put("nachname", nachname);
    }

    public String getGeschlecht() {
        return geschlecht;
    }

    public void setGeschlecht(String geschlecht) {
        dbHandler.updateField(collectionName, "_id", doc.getString("_id"), "geschlecht", geschlecht);
        doc.put("geschlecht", geschlecht);
    }

    public String getGebDatum() {
        return gebDatum;
    }

    public void setGebDatum(String gebDatum) {
        dbHandler.updateField(collectionName, "_id", doc.getString("_id"), "gebDatum", gebDatum);
        doc.put("gebDatum", gebDatum);
    }

    public String getGebOrt() {
        return gebOrt;
    }

    public void setGebOrt(String gebOrt) {
        dbHandler.updateField(collectionName, "_id", doc.getString("_id"), "gebOrt", gebOrt);
        doc.put("gebOrt", gebOrt);
    }



    // ------------ hier noch die bild url methoden --------------------
    @Override
    public String getBildURL() {
        return bildURL;
    }

    @Override
    public void setBildURL(String bildURL) {
        dbHandler.updateField(collectionName, "_id", doc.getString("_id"), "bildURL", bildURL );
        doc.put("bildURL", bildURL);
        this.bildURL = bildURL;
    }


    @Override
    public String getFraktion() {
        return fraktion;
    }

    @Override
    // hier dann set als mongo impl mit doc.put erstmal lokal und dann mit updateField (vom handler) in der db
    // bzw. doch andersrum, dann ist db safe und erst dann wird lokal geändert (falls was bei db update schief geht)
    public void setFraktion(String fraktion) {
        dbHandler.updateField(collectionName, "_id", doc.getString("_id"), "fraktion", fraktion);
        doc.put("fraktion", fraktion);
    }

    @Override
    public String getAbgeordnetenID() {
        return abgeordnetenID;
    }

    @Override
    public void setAbgeordnetenID(String abgeordnetenID) {
        dbHandler.updateField(collectionName, "_id", getAbgeordnetenID(), "_id", abgeordnetenID);
        doc.put("id", abgeordnetenID);
    }

    @Override
    public String getAnrede() {
        return anrede;
    }

    @Override
    public void setAnrede(String anrede) {
        dbHandler.updateField(collectionName, "_id", getAbgeordnetenID(), "anrede", anrede);
        doc.put("id", abgeordnetenID);
    }

    @Override
    public String getAkademTitel() {
        return akademTitel;
    }

    @Override
    public void setAkademTitel(String akademTitel) {

    }

    @Override
    public String getFamilienstand() {
        return familienstand;
    }

    @Override
    public void setFamilienstand(String familienstand) {

    }

    @Override
    public String getReligion() {
        return religion;
    }

    @Override
    public void setReligion(String religion) {

    }

    @Override
    public String getBeruf() {
        return beruf;
    }

    @Override
    public void setBeruf(String beruf) {

    }

    @Override
    public String getWkrLand() {
        return wkrLand;
    }

    @Override
    public void setWkrLand(String wkrLand) {

    }

    @Override
    public int getWkrNummer() {
        return wkrNummer;
    }

    @Override
    public void setWkrNummer(int wkrNummer) {

    }

    @Override
    public String getMandatsArt() {
        return mandatsArt;
    }

    @Override
    public void setMandatsArt(String mandatsArt) {

    }

    @Override
    public List<String> getFunktionen() {
        return funktionen;
    }

    @Override
    public void setFunktionen(List<Funktion> funktionen) {

    }



    // neue funktion für getREden als verknüpfung zu redner (hätte mal direkt abgeordneter = redner machen sollen aber naja)
    public List<String> getReden() {
        // Abgeordneter mit Rednern verbinden
        List<String> redenIDs = new ArrayList<>();

        // Alle Redner finden, die zu diesem Abgeordneten gehören
        List<Document> rednerDocs = dbHandler.findDocuments("Redner", new Document("abgeordnetenID", this.getAbgeordnetenID()));

        for (Document rednerDoc : rednerDocs) {
            // Reden des Redners sammeln
            List<String> rednerReden = rednerDoc.getList("reden", String.class);
            if (rednerReden != null) {
                redenIDs.addAll(rednerReden);
            }
        }
        return redenIDs;
    }



    public String toHTML() {
        StringBuilder html = new StringBuilder();

        html.append("<h1>").append(vorname).append(" ").append(nachname).append("</h1>");

        // Bild url einfügen
        html.append("<img src='").append(bildURL).append("' alt='Bild von ").append(vorname).append(" ").append(nachname).append("' style='max-width: 200px; height: auto;' />");


        // hier alle möglichen Infos über politik und person
        html.append("<p><strong>ID: </strong>").append(abgeordnetenID).append("</p>")
        .append("<p><strong>Partei: </strong>").append(fraktion).append("</p>")
        .append("<p><strong>Wahlkreis: </strong>").append(wkrLand).append("</p>")
        .append("<p><strong>Mandats-Art: </strong>").append(mandatsArt).append("</p>")
        .append("<p><strong>Geschlecht: </strong>").append(geschlecht).append("</p>")
        .append("<p><strong>Anrede: </strong>").append(anrede).append("</p>")
        .append("<p><strong>Akademischer Titel: </strong>").append(akademTitel).append("</p>");

        // hier funktionen schön darstellen:
        html.append("<h2>Funktionen:</h2>").append("<ul>");

        // gucken ob leere liste oder null (macht immer probleme)
        if (funktionen != null && !funktionen.isEmpty()){
            for (String funktion : funktionen) {
                html.append("<li>").append(funktion).append("</li>");
            }
            // hier falls nix da ist
        } else {
            html.append("<li>Keine Funktionen verfügbar.</li>");
        }

        // hier noch der rest an infos (eigentlich unnötig, aber hab das alles also why not

        html.append("</ul>")
                .append("<h2>Details:</h2>")
                .append("<p><strong>Geburtsdatum: </strong>").append(gebDatum).append("</p>")
                .append("<p><strong>Geburtsort: </strong>").append(gebOrt).append("</p>")
                .append("<p><strong>Familienstand: </strong>").append(familienstand).append("</p>")
                .append("<p><strong>Religion: </strong>").append(religion).append("</p>")
                .append("<p><strong>Beruf: </strong>").append(beruf).append("</p>");

        return html.toString();
    }




}
