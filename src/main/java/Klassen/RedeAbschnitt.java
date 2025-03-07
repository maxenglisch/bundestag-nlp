package Klassen;

import Database.MongoDBHandler;
import File_Impls.Kommentar_File_Impl;
import MongoDB_Impls.Kommentar_MongoDB_Impl;
import org.bson.Document;

public class RedeAbschnitt {
    private boolean isKommentar;  // ob kommi oder nicht
    private String inhalt;        // inhalt des abschnitts
    private String kommentarID;  // ref zu kommi ID

    // konstuktor für wenn es rede ist
    public RedeAbschnitt(String inhalt) {
        this.isKommentar = false;
        this.inhalt = inhalt;
        this.kommentarID = "leer";
    }

    // Konstruktor für wenn es kommi ist
    public RedeAbschnitt(String inhalt, String kommentarID) {
        this.isKommentar = true;
        this.inhalt = inhalt;
        this.kommentarID = kommentarID;
    }


    // Getter für Kommentar aus MongoDB
    public Kommentar_MongoDB_Impl getKommentar(MongoDBHandler dbHandler) {

        Document kommentarDoc = dbHandler.findDocument("Kommentare", "_id", this.kommentarID);

        if (kommentarDoc != null){
            return new Kommentar_MongoDB_Impl(dbHandler, kommentarDoc);

        }
        else {
            return null;
        }

    }



    // Getter
    public boolean isKommentar() {
        return isKommentar;
    }

    public String getInhalt() {
        return inhalt;
    }

    public String getKommentarID() {
        return kommentarID;
    }
}

