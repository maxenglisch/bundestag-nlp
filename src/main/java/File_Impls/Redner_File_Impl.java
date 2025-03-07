package File_Impls;

import Interfaces.IRedner;
import org.bson.Document;

import java.util.List;
import java.util.ArrayList;

public class Redner_File_Impl implements IRedner<Abgeordneter_File_Impl, Rede_File_Impl> {

    private String rednerID;
    private String vorname;
    private String nachname;
    private String fraktion;
    private List<Rede_File_Impl> reden;

    // referenz auf abgeordneten (meist sind redner = abgeordnete aber nicht immer)
    private Abgeordneter_File_Impl abgeordneter_FileImpl_ref;



    // Konstrutkor für fac
    public Redner_File_Impl(String rednerID, Abgeordneter_File_Impl abgeordneter_FileImpl_ref, String vorname, String nachname, String fraktion) {
        this.rednerID = rednerID;
        this.abgeordneter_FileImpl_ref = abgeordneter_FileImpl_ref;
        this.vorname = vorname;
        this.nachname = nachname;
        this.fraktion = fraktion;
        this.reden = new ArrayList<>();
    }

    // toDoc Methode: returned ein schönes doc für die db
    public Document toDoc() {

        Document rednerDoc = new Document("_id", this.rednerID) // ID des Redners
                .append("vorname", this.vorname)
                .append("nachname", this.nachname)
                .append("fraktion", this.fraktion);
        // dachte hier erst kurz das passt nicht aber geht wohl doch einfach mit abgeordneter als klasse und dann.getid
        if (this.abgeordneter_FileImpl_ref != null) {
            rednerDoc.append("abgeordneterID", this.abgeordneter_FileImpl_ref.getAbgeordnetenID());
        } else {
            rednerDoc.append("abgeordneterID", "Gastredner"); // wenn kein bezug auf abge dann gastredner (mit null hat fehler gemacht)
        }

        List<String> redIDs = new ArrayList<>();
        for (Rede_File_Impl rede : this.reden) {
            redIDs.add(rede.getRedID());
        }
        rednerDoc.append("reden", redIDs); // lsit der redIDs

        return rednerDoc;
    }



    public String getRednerID() {
        return rednerID;
    }


    public Abgeordneter_File_Impl getAbgeordneter_ref() {
        return abgeordneter_FileImpl_ref;
    }

    public void setAbgeordneter_ref(Abgeordneter_File_Impl abgeordneter_FileImpl_ref) {
        this.abgeordneter_FileImpl_ref = abgeordneter_FileImpl_ref;
        // auch noch direkt die fraktion setzen
        if (abgeordneter_FileImpl_ref != null) {
            this.fraktion = abgeordneter_FileImpl_ref.getFraktion();
        }
    }

    public void setRednerID(String rednerID) {
        this.rednerID = rednerID;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getFraktion() {
        return fraktion;
    }

    public void setFraktion(String fraktion) {
        this.fraktion = fraktion;
    }

    public List<Rede_File_Impl> getReden() {
        return reden;
    }
    // reden adder
    public void addRede(Rede_File_Impl redeFileImpl) {
        reden.add(redeFileImpl);}


}
