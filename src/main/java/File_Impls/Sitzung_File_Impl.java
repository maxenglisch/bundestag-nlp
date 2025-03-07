package File_Impls;

import Interfaces.ISitzung;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class Sitzung_File_Impl implements ISitzung<Tagesordnungspunkt_File_Impl> {
    private String sID;
    private int wpNr;
    private String sDatum;
    private String sOrt;
    private String sStartzeit;
    private String sEndzeit;
    private boolean sOeffentlichkeit;

    // Listen für tagesordnungspunkte
    private List <Tagesordnungspunkt_File_Impl> tagesordnungspunkte;


    // public konstruktor den dann die factory nimmt
    public Sitzung_File_Impl(String sID, int wpNr, String sDatum, String sOrt, String sStartzeit, String sEndzeit, boolean sOeffentlichkeit) {
        this.sID = sID;
        this.wpNr = wpNr;
        this.sDatum = sDatum;
        this.sOrt = sOrt;
        this.sStartzeit = sStartzeit;
        this.sEndzeit = sEndzeit;
        this.sOeffentlichkeit = sOeffentlichkeit;
        this.tagesordnungspunkte = new ArrayList<>();
    }

    // to doc methode
    public Document toDoc() {
        Document sitzDoc = new Document("_id", this.sID) // wieder _id überschreiben in db = keine redundanzen möglich
                .append("wahlperiode", this.wpNr)
                .append("datum", this.sDatum).append("ort", this.sOrt)
                .append("startzeit", this.sStartzeit).append("endzeit", this.sEndzeit)
                .append("oeffentlichkeit", this.sOeffentlichkeit);

        // IDs der tops sammeln (referenz in der db dann, wichtig um rauszufinden welche reden von welchen tops bei welcher sitzungen waren,
        // deswegen verknüpfung über id
        List<String> topIDs = new ArrayList<>();
        for (Tagesordnungspunkt_File_Impl top : this.tagesordnungspunkte) {
            topIDs.add(top.getTopID()); // TopID zur Liste hinzufügen und gleich dann noch zum doc appenden
        }

        sitzDoc.append("tagesordnungspunkte", topIDs);

        return sitzDoc;
    }

    // tops startet als leere list, dann dynamisch auffüllen
    public void addTagesordnungspunkt(Tagesordnungspunkt_File_Impl top) {
        this.tagesordnungspunkte.add(top);
    }

    public int getwpNr() {
        return wpNr;
    }

    public void setwpNr(int wpNr) {
        this.wpNr = wpNr;
    }

    public String getsID() {
        return sID;
    }

    public void setsID(String sID) {
        this.sID = sID;
    }

    public String getsDatum() {
        return sDatum;
    }

    public void setsDatum(String sDatum) {
        this.sDatum = sDatum;
    }

    public String getsOrt() {
        return sOrt;
    }

    public void setsOrt(String sOrt) {
        this.sOrt = sOrt;
    }

    public boolean getsOeffentlichkeit() {
        return sOeffentlichkeit;
    }

    public void setsOeffentlichkeit(boolean sOeffentlichkeit) {
        this.sOeffentlichkeit = sOeffentlichkeit;
    }

    public String getsStartzeit() {
        return sStartzeit;
    }

    public void setsStartzeit(String sStartzeit) {
        this.sStartzeit = sStartzeit;
    }

    public String getsEndzeit() {
        return sEndzeit;
    }

    public void setsEndzeit(String sEndzeit) {
        this.sEndzeit = sEndzeit;
    }


    public List<Tagesordnungspunkt_File_Impl> getTagesordnungspunkte() {
        return tagesordnungspunkte;
    }

    public void setTagesordnungspunkte(List<Tagesordnungspunkt_File_Impl> tagesordnungspunkte) {
        this.tagesordnungspunkte = tagesordnungspunkte;
    }
}
