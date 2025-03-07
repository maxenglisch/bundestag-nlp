package File_Impls;

import Interfaces.IKommentar;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class Kommentar_File_Impl implements IKommentar{

    // infos zum kommi
    private String kID;
    private String kInhalt;

    // referenz zur Rede (wichtig für zuordnung)
    private String redeID;


    // neu: zuweisung zu abges oder fraks ermöglichen
    private List<String> abgeordnetenIDs;
    private List<String> fraktionen;


    // public Konstruktor
    public Kommentar_File_Impl(String kID, String redeID, String kInhalt) {
        this.kID = kID;
        this.redeID = redeID;
        this.kInhalt = kInhalt;
        // beide erstmal standard null, werden später in html gesetzt erst
        this.abgeordnetenIDs = new ArrayList<>();
        this.fraktionen = new ArrayList<>();
    }


    // toDoc Methode für die umwandlung für db docs
    public Document toDoc() {
        Document kommentarDoc = new Document();
        kommentarDoc.put("_id", this.kID);
        kommentarDoc.put("redeID", this.redeID);
        kommentarDoc.put("kInhalt", this.kInhalt);
        kommentarDoc.put("abgeordnetenIDs", this.abgeordnetenIDs);
        kommentarDoc.put("fraktionen", this.fraktionen);
        return kommentarDoc;
    }


    // ------------- Getter Setter -------------------

    @Override
    public List<String> getAbgeordnetenIDs() { return abgeordnetenIDs; }

    @Override
    public void addAbgeordneterID(String abgeordneterID) { this.abgeordnetenIDs.add(abgeordneterID); }

    @Override
    public List<String> getFraktionen() { return fraktionen; }

    @Override
    public void addFraktion(String fraktion) { this.fraktionen.add(fraktion); }

    @Override
    public String getkID() {
        return kID;
    }
    @Override
    public void setkID(String kID) {
        this.kID = kID;
    }
    @Override
    public String getRedeID() {
        return redeID;
    }
    @Override
    public void setRedeID(String redeID) {
        this.redeID = redeID;
    }

    @Override
    public String getkInhalt() {
        return kInhalt;
    }
    @Override
    public void setkInhalt(String kInhalt) {
        this.kInhalt = kInhalt;
    }
}
