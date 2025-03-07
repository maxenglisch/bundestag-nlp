package MongoDB_Impls;

import Interfaces.IKommentar;
import Database.MongoDBHandler;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class Kommentar_MongoDB_Impl implements IKommentar{

    private final MongoDBHandler dbHandler;
    private final String collectionName = "Kommentare";
    private final Document doc;

    private String kID;
    private String redeID;
    private String kInhalt;

    private List<String> abgeordnetenIDs;
    private List<String> fraktionen;

    public Kommentar_MongoDB_Impl(MongoDBHandler dbHandler, Document document) {
        this.dbHandler = dbHandler;
        this.doc = document;

        this.kID = doc.getString("_id");
        this.redeID = doc.getString("redeID");
        this.kInhalt = doc.getString("kInhalt");
        this.abgeordnetenIDs = doc.getList("abgeordnetenIDs", String.class);

        this.fraktionen = doc.getList("fraktionen", String.class);
    }

    @Override
    public List<String> getAbgeordnetenIDs() { return abgeordnetenIDs; }

    @Override
    public void addAbgeordneterID(String abgeordneterID) {

        // falls null dann mit leerer liste initialisieren
        if (this.abgeordnetenIDs == null){
            this.abgeordnetenIDs = new ArrayList<>();
        }

        // überprüfen ob die ID schon zugewisen wurde
        if (!this.abgeordnetenIDs.contains(abgeordneterID)) {
            this.abgeordnetenIDs.add(abgeordneterID);
            dbHandler.updateFieldWithList(collectionName, "_id", this.kID, "abgeordnetenIDs", this.abgeordnetenIDs);
            doc.put("abgeordnetenIDs", this.abgeordnetenIDs);
        }

    }

    @Override
    public List<String> getFraktionen() {
        return fraktionen;
    }

    @Override
    public void addFraktion(String fraktion) {

        // falls null dann mit leerer liste initialisieren
        if (this.fraktionen == null){
            this.fraktionen = new ArrayList<>();
        }

        // überprüfen ob die frak schon zugewisen wurde
        if (!this.fraktionen.contains(fraktion)) {
            this.fraktionen.add(fraktion);
            dbHandler.updateFieldWithList(collectionName, "_id", this.kID, "fraktionen", this.fraktionen);
            doc.put("fraktionen", this.fraktionen);
        }
    }

    @Override
    public String getkID() {
        return kID;
    }

    @Override
    public void setkID(String kID) {
        dbHandler.updateField(collectionName, "_id", this.kID, "_id", kID);
        doc.put("_id", kID);
        this.kID = kID;
    }
    @Override
    public String getRedeID() {
        return redeID;
    }
    @Override
    public void setRedeID(String redeID) {
        dbHandler.updateField(collectionName, "_id", this.kID, "redeID", redeID);
        doc.put("redeID", redeID);
        this.redeID = redeID;
    }
    @Override
    public String getkInhalt() {
        return kInhalt;
    }
    @Override
    public void setkInhalt(String kInhalt) {
        dbHandler.updateField(collectionName, "_id", this.kID, "kInhalt", kInhalt);
        doc.put("kInhalt", kInhalt);
        this.kInhalt = kInhalt;
    }

    public String toHTML() {
        // Kommentar als HTML-Abschnitt
        return "<li>" + kInhalt + "</li>";
    }

}