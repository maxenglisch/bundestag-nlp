package Parser;

// klassen und factory importieren
import File_Impls.Abgeordneter_File_Impl;
import Factory.MainFactory;

// dom4j stuff importieren
import Klassen.Funktion;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

// klassiker java sachen importieren
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class abgeordnetenParser {


    // erstmal die wichtigen Konstanten festlegen: wp und factory
    private static final int AKTUELLE_WP = 20;
    private final MainFactory factory;

    // parser mit factory erzeugen
    public abgeordnetenParser(MainFactory factory) {
        this.factory = factory;
    }

    // methode um abg. zu parsen über filepath (hier in resources)
    public List<Abgeordneter_File_Impl> parseAbgeordnete(String filepath) throws DocumentException {


        // List<Abgeordneter_File_Impl> abgeordneteList = new ArrayList<>();

        // saxreader benutzen (discord empfehlung)
        SAXReader reader = new SAXReader();
        Document document = reader.read(new File(filepath));

        // erstmal root nehmen, sollte <DOCUMENT> sein
        Element root = document.getRootElement();

        // geht alle elemente ab root durch für jeden abgeordn.
        List<Element> abgeordneterElements = root.elements("MDB"); // neue "root" ist dann ab <MDB>
        for (Element element : abgeordneterElements) {
            // für jeden abgeord. die elemente in ordentlichen attributen speichern
            // eigentlich zuweisung von xml tags zu attributen aus meinen klassen hier

            // ID steht ganz oben (erstmal überprüfen ob != "" (also kein leerer string, das macht probleme bei parseInt
            // ID deklarieren damit sie auch außerhalb vom if teil da ist, -1 für ungültige ID
            String ID = "-1";
            String IDText = element.elementText("ID");

            if (IDText != null && !IDText.isEmpty()){
                ID = element.elementText("ID");
            }


            // für namen tiefer in die xml gehen über die elements
            Element nameElement = element.element("NAMEN").element("NAME");
            String vorname = nameElement.elementText("VORNAME");
            String nachname = nameElement.elementText("NACHNAME");
            String anrede = nameElement.elementText("ANREDE_TITEL");
            String akademTitel = nameElement.elementText("AKAD_TITEL");

            // jetzt die bio daten auslesen in neuem element
            Element bioElement = element.element("BIOGRAFISCHE_ANGABEN");
            String gebDatum = bioElement.elementText("GEBURTSDATUM");
            String gebOrt = bioElement.elementText("GEBURTSORT");
            String geschlecht = bioElement.elementText("GESCHLECHT");
            String familienstand = bioElement.elementText("FAMILIENSTAND");
            String religion = bioElement.elementText("RELIGION");
            String beruf = bioElement.elementText("BERUF");
            String fraktionName = bioElement.elementText("PARTEI_KURZ");


            // jetzt alle wahlinformationen parsen aber nur aktuelle wp (20)

            // erstmal filtern nach aktuellerwp
            Element aktuelleWP = null;
            Element wpElement = element.element("WAHLPERIODEN");
            List<Element> wps = wpElement.elements("WAHLPERIODE");
            for (Element wp : wps){
                int wpNummer = Integer.parseInt(wp.elementText("WP"));
                if (wpNummer == AKTUELLE_WP) {
                    aktuelleWP = wp;
                    break;
                }
            }
            // falls aktuelleWP nicht geändert wurde dann ist der abgeordnete outdated
            // und wird ignoriert
            if (aktuelleWP == null) {
                // System.out.println("Keine Daten zur aktuellen WP für Abgeordneten: " + nachname + vorname);
                continue;
            }

            // jetzt infos parsen

            // wkrLand mit default um null exceptions zu umgehen
            String wkrLand = "kein Wahlkreis";
            wkrLand = aktuelleWP.elementText("WKR_LAND");

            // selbes Spiel wie bei ID
            int wkrNummer = -1;
            String wkrString = aktuelleWP.elementText("WKR_NUMMER");
            if (wkrString != null && !wkrString.isEmpty()){
                wkrNummer = Integer.parseInt(aktuelleWP.elementText("WKR_NUMMER"));
            }

            String mandatsArt = aktuelleWP.elementText("MANDATSART");


            // funktionen auch noch
            List<Funktion> funktionen = new ArrayList<>();
            Element instiElem = aktuelleWP.element("INSTITUTIONEN");
            List<Element> instis = instiElem.elements("INSTITUTION");
            for (Element insti : instis){
                String institutionName = insti.elementText("INS_LANG");
                String funktionName = insti.elementText("FKT_LANG");
                // neue funktion erstellen als klasse dann funktion in liste funktionen adden
                Funktion funktion = factory.createFunktion(institutionName, funktionName);
                funktionen.add(funktion);
            }


            // am ende daraus neuen abgeordneten bauen
            Abgeordneter_File_Impl abgeordneterFileImpl = factory.createAbgeordneter(ID, vorname, nachname, geschlecht, gebDatum, gebOrt, fraktionName, anrede, akademTitel, familienstand,
                    religion, beruf, wkrLand, wkrNummer, mandatsArt, funktionen);
            // zur liste hinzufügen
        }

        // rückgabe am ende dann liste der abgeordneten aus der xml (erstmal test mit wenigen Daten)
        return factory.getAbgeordneteListe();
    }
}
