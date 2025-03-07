package File_Impls;

import Interfaces.IAbgeordneter;
import Klassen.Funktion;
import Klassen.Person;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class Abgeordneter_File_Impl extends Person implements IAbgeordneter<Funktion> {

    // basisinformationen aus XML, über person stuff hinaus (die sind vererbt)
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
    private List<Funktion> funktionen;

    // bild (neu)
    private String bildURL;



    // public konstruktor, nur über factory aufrufbar mit create Methode
    public Abgeordneter_File_Impl(String abgeordnetenID, String vorname, String nachname, String geschlecht,
                                   String gebDatum, String gebOrt, String fraktion,
                                   String anrede, String akademTitel,
                                   String familienstand, String religion, String beruf,
                                   String wkrLand, int wkrNr, String mandatsArt, List<Funktion> funktionen) {
        super(vorname, nachname, geschlecht, gebDatum, gebOrt);
        this.abgeordnetenID = abgeordnetenID;
        this.fraktion = fraktion;
        this.anrede = anrede;
        this.akademTitel = akademTitel;
        this.familienstand = familienstand;
        this.religion = religion;
        this.beruf = beruf;
        this.wkrLand = wkrLand;
        this.wkrNummer = wkrNr;
        this.mandatsArt = mandatsArt;
        this.funktionen = funktionen;
    }


    /**
     *  to doc methode (hier etwas komplexer wegen funktionen list, dann einfach als strings weil eifnacher
     * @return ein abgeDoc, also die file impl als doc für die db dann
     */
    public Document toDoc() {

        Document abgeDoc = new Document("_id", this.abgeordnetenID) // sehr praktisch, hier einfach die interne _id einfach mit meiner eigenen überschreiben,
            // so ist direkt die eindeutigkeit gegeben, da _id wie ein primary key in SQL ist (also eindeutig)

            // hier dann basic infos von person
            .append("vorname", this.getVorname()).append("nachname", this.getNachname())
            .append("geburtsdatum", this.getGebDatum()).append("geburtsort", this.getGebOrt()).append("geschlecht", this.getGeschlecht())
            // hier dann eher abgeordnete infos
            .append("anrede", this.anrede).append("akademischer_titel", this.akademTitel)
            .append("familienstand", this.familienstand)
            .append("religion", this.religion).append("beruf", this.beruf)
            .append("fraktion", this.fraktion)
            .append("wahlkreis_land", this.wkrLand).append("wahlkreis_nummer", this.wkrNummer).append("mandatsart", this.mandatsArt);

            // funks als list von strings
            List<String> funkStrings = new ArrayList<>();
            if (funktionen != null) {
                for (Funktion funktion : funktionen) {
                    funkStrings.add(funktion.toString());
                }
            }
            // am ende noch die funk strings appenden
            abgeDoc.append("funktionen", funkStrings);

            // und noch das bild url
            abgeDoc.append("bildURL", this.bildURL);
        return abgeDoc;
    }



    // override von den interface methoden (ohne override weil ist eh klar)
    // ------------ getter setter ------------------------





    @Override
    public String getBildURL() {
        return bildURL;
    }

    @Override
    public void setBildURL(String bildURL) {
        this.bildURL = bildURL;
    }



    public String getFraktion() {
        return fraktion;
    }

    public void setFraktion(String fraktion) {
        this.fraktion = fraktion;
    }



    public String getAbgeordnetenID() {
        return abgeordnetenID;
    }

    public void setAbgeordnetenID(String abgeordnetenID) {
        this.abgeordnetenID = abgeordnetenID;
    }

    public String getAnrede() {
        return anrede;
    }

    public void setAnrede(String anrede) {
        this.anrede = anrede;
    }

    public String getAkademTitel() {
        return akademTitel;
    }

    public void setAkademTitel(String akademTitel) {
        this.akademTitel = akademTitel;
    }

    public String getFamilienstand() {
        return familienstand;
    }

    public void setFamilienstand(String familienstand) {
        this.familienstand = familienstand;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }
    public String getBeruf() {
        return beruf;
    }
    public void setBeruf(String beruf) {
        this.beruf = beruf;
    }

    public String getWkrLand() {
        return wkrLand;
    }

    public void setWkrLand(String wkrLand) {
        this.wkrLand = wkrLand;
    }

    public int getWkrNummer() {
        return wkrNummer;
    }

    public void setWkrNummer(int wkrNummer) {
        this.wkrNummer = wkrNummer;
    }

    public String getMandatsArt() {
        return mandatsArt;
    }

    public void setMandatsArt(String mandatsArt) {
        this.mandatsArt = mandatsArt;
    }

    public List<Funktion> getFunktionen() {
        return funktionen;
    }

    public void setFunktionen(List<Funktion> funktionen) {
        this.funktionen = funktionen;
    }
}
