package MongoDB_Impls;

import Interfaces.ISitzung;
import Database.MongoDBHandler;
import File_Impls.Tagesordnungspunkt_File_Impl;
import org.bson.Document;

import java.util.List;

public class Sitzung_MongoDB_Impl implements ISitzung<String> {

    // Sitzungsattribute
    private String sID;
    private int wpNr;
    private String sDatum;
    private String sOrt;
    private String sStartzeit;
    private String sEndzeit;
    private boolean sOeffentlichkeit;

    // Listen für tagesordnungspunkte
    private List <String> top_ids;


    private final MongoDBHandler dbHandler;
    private final String collectionName = "Sitzungen";
    private final Document doc;

    public Sitzung_MongoDB_Impl(MongoDBHandler dbHandler, Document document) {
        this.dbHandler = dbHandler;
        this.doc = document;
        this.sID = doc.getString("_id");
        this.wpNr = doc.getInteger("wahlperiode");
        this.sDatum = doc.getString("datum");
        this.sOrt = doc.getString("ort");
        this.sStartzeit = doc.getString("startzeit");
        this.sEndzeit = doc.getString("endzeit");
        this.sOeffentlichkeit = doc.getBoolean("oeffentlichkeit");
        this.top_ids = doc.getList("tagesordnungspunkte", String.class);
    }

    // Getter und Setter
    @Override
    public String getsID() {
        return sID;
    }

    @Override
    public void setsID(String sitzungsID) {
        dbHandler.updateField(collectionName, "_id", doc.getString(":_id"), "_id", sitzungsID);
        doc.put("sitzungsID", sitzungsID);
    }

    @Override
    public int getwpNr() {
        return wpNr;
    }

    @Override
    public void setwpNr(int wahlperiode) {
        dbHandler.updateField(collectionName, "_id", doc.getString("_id"), "wahlperiode", String.valueOf(wahlperiode));
        doc.put("wahlperiode", wahlperiode);
    }

    @Override
    public String getsDatum() {
        return sDatum;
    }

    @Override
    public void setsDatum(String datum) {
        dbHandler.updateField(collectionName, "_id", doc.getString("_id"), "datum", datum);
        doc.put("datum", datum);
    }

    @Override
    public String getsOrt() {
        return sOrt;
    }

    @Override
    public void setsOrt(String ort) {
        dbHandler.updateField(collectionName, "_id", doc.getString("_id"), "ort", ort);
        doc.put("ort", ort);
    }

    @Override
    public String getsStartzeit() {
        return sStartzeit;
    }

    @Override
    public void setsStartzeit(String startzeit) {
        dbHandler.updateField(collectionName, "_id", doc.getString("_id"), "startzeit", startzeit);
        doc.put("startzeit", startzeit);
    }

    @Override
    public String getsEndzeit() {
        return sEndzeit;
    }

    @Override
    public void setsEndzeit(String endzeit) {
        dbHandler.updateField(collectionName, "_id", doc.getString("_id"), "endzeit", endzeit);
        doc.put("endzeit", endzeit);
    }

    @Override
    public boolean getsOeffentlichkeit() {
        return sOeffentlichkeit;
    }

    @Override
    public void setsOeffentlichkeit(boolean oeffentlichkeit) {
        dbHandler.updateField(collectionName, "_id", doc.getString("_id"), "oeffentlichkeit", String.valueOf(oeffentlichkeit));
        doc.put("oeffentlich", oeffentlichkeit);
    }

    @Override
    public List<String> getTagesordnungspunkte() {
        return top_ids;
    }

    @Override
    public void addTagesordnungspunkt(String top) {
    }

    public String toHTML() {
        StringBuilder html = new StringBuilder();

        html.append("<div class='sitzung'>")
                .append("<p><strong>Sitzung ID: </strong>").append(sID).append("<p>")
                .append("<p><strong>Datum: </strong>").append(sDatum).append("</p>")
                .append("<p><strong>Ort: </strong>").append(sOrt).append("</p>")
                .append("<p><strong>Startzeit: </strong>").append(sStartzeit).append("</p>")
                .append("<p><strong>Endzeit: </strong>").append(sEndzeit).append("</p>")
                .append("<p><strong>Wahlperiode: </strong>").append(wpNr).append("</p>")
//                .append("<p><strong>Tagesordnungspunkte: </strong></p>")
//                .append("<ul>");
//
//        for (String topID : top_ids) {
//            html.append("<li>").append(topID.split("_")[1]).append("</li>");
//        }
//
//        html.append("</ul>")
                .append("</div>");

        return html.toString();
    }




}