package MongoDB_Impls;

import Factory.MainFactory;
import Interfaces.IRedner;
import Database.MongoDBHandler;
import org.bson.Document;

import java.util.List;

public class Redner_MongoDB_Impl implements IRedner<String, String> {

    private final MongoDBHandler dbHandler;
    private final String collectionName = "Redner"; // MongoDB-Collection für Redner
    private final Document doc; // Zuordnung zu MongoDB-Dokument

    private String rednerID;
    private String vorname;
    private String nachname;
    private String fraktion;
    private List<String> reden; // hier nur die ids, späer verknüpfen
    private String abgeordneter_ref; // ref ID

    public Redner_MongoDB_Impl(MongoDBHandler dbHandler, Document document) {
        this.dbHandler = dbHandler;
        this.doc = document;

        // attribute aus der db holen
        this.rednerID = doc.getString("_id");
        this.vorname = doc.getString("vorname");
        this.nachname = doc.getString("nachname");
        this.fraktion = doc.getString("fraktion");
        this.abgeordneter_ref = doc.getString("abgeordneterID");
        this.reden = doc.getList("reden", String.class);
    }

    @Override
    public String getRednerID() {
        return rednerID;
    }

    @Override
    public void setRednerID(String rednerID) {
        dbHandler.updateField(collectionName, "_id", this.rednerID, "_id", rednerID);
        doc.put("_id", rednerID);
        this.rednerID = rednerID;
    }

    @Override
    public String getAbgeordneter_ref() {
        return abgeordneter_ref;
    }

    @Override
    public void setAbgeordneter_ref(String abgeordneter_ref) {
        dbHandler.updateField(collectionName, "_id", rednerID, "abgeordneter_ref", abgeordneter_ref);
        doc.put("abgeordneter_ref", abgeordneter_ref);
        this.abgeordneter_ref = abgeordneter_ref;
    }

    @Override
    public String getVorname() {
        return vorname;
    }

    @Override
    public void setVorname(String vorname) {
        dbHandler.updateField(collectionName, "_id", rednerID, "vorname", vorname);
        doc.put("vorname", vorname);
        this.vorname = vorname;
    }

    @Override
    public String getNachname() {
        return nachname;
    }

    @Override
    public void setNachname(String nachname) {
        dbHandler.updateField(collectionName, "_id", rednerID, "nachname", nachname);
        doc.put("nachname", nachname);
        this.nachname = nachname;
    }

    @Override
    public String getFraktion() {
        return fraktion;
    }

    @Override
    public void setFraktion(String fraktion) {
        dbHandler.updateField(collectionName, "_id", rednerID, "fraktion", fraktion);
        doc.put("fraktion", fraktion);
        this.fraktion = fraktion;
    }

    @Override
    public List<String> getReden() {
        return reden;
    }

    @Override
    public void addRede(String redeID) {
        // Füge neue Rede-ID zur Liste hinzu
        reden.add(redeID);
        doc.put("reden", reden);
    }


    // normales tohtml wenn auch abgeordnete verknüpft sind
    public String toHTML() {
        StringBuilder html = new StringBuilder();



        // jetzt zu den reden (ids angeben für navigation) mit href als link zum springen zur jeweiligen id
        html.append("<p><strong>Reden:</strong></p>").append("<ul>");

        for (String redeID : reden) {
                html.append("<li><a href='#rede").append(redeID).append("'>Rede ").append(redeID).append("</a></li>");
        }

        html.append("</ul>");
        return html.toString();
    }


    // hier html2 für redner ohne abgeordneten ID, da brauche ich dann die infos aus dem redner
    public String toHTML2() {
        StringBuilder html = new StringBuilder();

        // erstmal die basics wie vorname, id, fraktion und so.
        html.append("<div class='redner'>")
                .append("<h3>Redner ID: ").append(rednerID).append("</h3>")
                .append("<p><strong>Vorname:</strong> ").append(vorname).append("</p>")
                .append("<p><strong>Nachname:</strong> ").append(nachname).append("</p>")
                .append("<p><strong>Fraktion:</strong> ").append(fraktion).append("</p>");

        return html.toString();
    }


}