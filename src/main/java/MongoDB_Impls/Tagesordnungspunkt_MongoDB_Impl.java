package MongoDB_Impls;

import Database.MongoDBHandler;
import Interfaces.ITagesordnungspunkt;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class Tagesordnungspunkt_MongoDB_Impl implements ITagesordnungspunkt<String> {

    private final MongoDBHandler dbHandler;
    private final String collectionName = "Tagesordnungspunkte";
    private final Document doc;

    // Attribute des Tagesordnungspunktes
    private String topID;
    private String sitzungsID;
    private List<String> redenIDs;

    public Tagesordnungspunkt_MongoDB_Impl(MongoDBHandler dbHandler, Document document) {
        this.dbHandler = dbHandler;
        this.doc = document;

        // Initialisierung der Attribute aus dem Dokument
        this.topID = doc.getString("_id");
        this.sitzungsID = doc.getString("sitzungsID");
        this.redenIDs = doc.getList("reden", String.class);
    }

    @Override
    public String getTopID() {
        return topID;
    }

    @Override
    public void setTopID(String topID) {
        dbHandler.updateField(collectionName, "_id", this.topID, "_id", topID);
        doc.put("_id", topID);
        this.topID = topID;
    }

    @Override
    public String getSitzungsID() {
        return sitzungsID;
    }

    @Override
    public void setSitzungsID(String sitzungsID) {
        dbHandler.updateField(collectionName, "_id", this.topID, "sitzungsID", sitzungsID);
        doc.put("sitzungsID", sitzungsID);
        this.sitzungsID = sitzungsID;
    }

    @Override
    public List<String> getTopReden() {
        return new ArrayList<>(redenIDs);
    }

    @Override
    public void addRede(String redeID) {
        if (!redenIDs.contains(redeID)) {
            redenIDs.add(redeID);
            doc.put("reden", redenIDs);
        }
    }


    public String toHTML() {
        StringBuilder html = new StringBuilder();

        html.append("<div class='top' id='top").append(topID).append("'>")
                .append("<h3>Tagesordnungspunkt</h3>")
                .append("<p><strong>ID:</strong> ").append(topID).append("</p>")
                .append("<p><strong>SitzungsID:</strong> ").append(sitzungsID).append("</p>")
                .append("<h4>Reden:</h4>")
                .append("<ul>");

        for (String redeID : redenIDs) {
            html.append("<li><a href='#rede").append(redeID).append("'>Rede ").append(redeID).append("</a></li>");
        }

        html.append("</ul>")
                .append("</div>");

        return html.toString();
    }




}