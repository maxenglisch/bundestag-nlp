package File_Impls;

import Interfaces.IRede;
import Klassen.RedeAbschnitt;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class Rede_File_Impl implements IRede<RedeAbschnitt, Redner_File_Impl> {
    private String redID;
    private String topID; // für referenz zu tagesordnungspunkt
    // private String redInhalt;
    private Redner_File_Impl redner;
    // private List<Kommentar_File_Impl> redKommentare;

    private List<RedeAbschnitt> abschnitte;


    // public konstruktor, der nur von factory bentutz wird
    public Rede_File_Impl(String redID, String topID, Redner_File_Impl redner) {
        this.redID = redID;
        this.topID = topID;
        // this.redInhalt = redInhalt;
        this.redner = redner;
        // this.redKommentare = new ArrayList<>();
        this.abschnitte = new ArrayList<>();
    }

    // toDoc für rede in redeDoc
    public Document toDoc() {

        Document redeDoc = new Document("_id", this.redID) // _id wider überschreiben mit id der Rede
                .append("topID", this.topID)
                //.append("inhalt", this.redInhalt) // inhalt als string mit allem
                .append("rednerID", this.redner.getRednerID());


        // abschnitte aus liste

        List<Document> abschnitteDocs = new ArrayList<>();

        for (RedeAbschnitt abschnitt : this.abschnitte) {
            Document abschnittDoc = new Document()
                    .append("isKommentar", abschnitt.isKommentar())
                    .append("inhalt", abschnitt.getInhalt());

            if (abschnitt.isKommentar() && abschnitt.getKommentarID() != null) {
                abschnittDoc.append("kommentarID", abschnitt.getKommentarID());
            }
            abschnitteDocs.add(abschnittDoc);
        }

        redeDoc.append("abschnitte", abschnitteDocs);

        return redeDoc;
    }


    public List<RedeAbschnitt> getAbschnitte() {
        return abschnitte;
    }

    public void addAbschnitt(String inhalt, boolean isKommentar, String kommentarID) {
        if (isKommentar) {
            // falls kommetnar true dann neuer abschntt mit inahtl und kommi id
            abschnitte.add(new RedeAbschnitt(inhalt, kommentarID));
        } else {
            // sonst einfach nur redeabschnitt mti inhalt
            abschnitte.add(new RedeAbschnitt(inhalt));
        }
    }

    public String getTopID() {
        return topID;
    }

    public void setTopID(String topID) {
        this.topID = topID;
    }



    public Redner_File_Impl getRedner() {
        return redner;
    }

    public String getRedID() {
        return redID;
    }

    public void setRedID(String redID) {
        this.redID = redID;
    }

//    public String getRedInhalt() {
//        return redInhalt;
//    }
//
//    public void setRedInhalt(String redInhalt) {
//        this.redInhalt = redInhalt;
//    }

    public void setRedner(Redner_File_Impl redner) {
        this.redner = redner;
    }

//    public List<Kommentar_File_Impl> getRedKommentare() {
//        return redKommentare;
//    }
//    public void addredKommentar(Kommentar_File_Impl kommentar) {
//        redKommentare.add(kommentar);
//    }


}
