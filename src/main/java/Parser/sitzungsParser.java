package Parser;

import File_Impls.*;
import Factory.MainFactory;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.List;


public class sitzungsParser {


    private final MainFactory factory;

    public sitzungsParser(MainFactory factory) {
        this.factory = factory;
    }

    public void parseSitzung(String filepath) throws DocumentException {
        // saxreader wieder nehmen, klappt super
        SAXReader reader = new SAXReader();
        Document document = reader.read(new File(filepath));

        // root holen, (doch lieber die attribute in dbtplenarprotokoll auslesen statt über kopfdaten)
        Element root = document.getRootElement();
        // System.out.println(root);
        // System.out.println(root.getName());

        // ----------- Sitzungen durchgehen -------------------


        // erstmal basic sitzungseckdaten
        String sID = root.attributeValue("sitzung-nr");
        int wpNr = Integer.parseInt(root.attributeValue("wahlperiode"));
        String sDatum = root.attributeValue("sitzung-datum");
        String sOrt = root.attributeValue("sitzung-ort");
        String sStartZeit = root.attributeValue("sitzung-start-uhrzeit");
        String sEndzeit = root.attributeValue("sitzung-ende-uhrzeit");
        // System.out.println(sID + wpNr + sDatum + sOrt +sStartZeit + sEndzeit);

        //  öffentlich (falls keine Angabe vorhanden ist)
        boolean oeffentlichkeit = true;

        // sitzung bauen mit factory, später dann die listen füllen
        Sitzung_File_Impl sitzungFileImpl = factory.createSitzung(sID, wpNr, sDatum, sOrt, sStartZeit, sEndzeit, oeffentlichkeit);
        // System.out.println(sitzung.getsDatum() + "\n" + sitzung.getsID() + "\n" + sitzung.getWpNr());

        // ----------- jetzt tagesordnungspunkte (tops) parsen: --------------

        Element sitzungsverlauf = root.element("sitzungsverlauf");
        // System.out.println(sitzungsverlauf.getName());

        List<Element> tops = sitzungsverlauf.elements("tagesordnungspunkt");
        // System.out.println("tops: " + tops.size());

        for (Element topElement : tops) {
            String topID = topElement.attributeValue("top-id");
            Tagesordnungspunkt_File_Impl top = factory.createTagesordnungspunkt(topID, sitzungFileImpl.getsID());
            // System.out.println(top.getTopID());
            sitzungFileImpl.addTagesordnungspunkt(top); // schonmal die tops in die sitzung adden
            // hier schnell noch den topkey holen nach createTOp und den auch der rede dann geben
            String topKey = top.getTopID();

            // ------------- als nächstes die Reden zu den Tops: -------------
            List<Element> reden = topElement.elements("rede");
            for (Element redeElement : reden) {
                // rede id einfach direkt nehmen
                String redID = redeElement.attributeValue("id");

//                // ---- für reden text etwas umständlich zusammenbauen:
//                StringBuilder redeBauer = new StringBuilder();
//
//                // alle redeElement Kinder auslesen und nach redeInhalt und Kommentaren trennen
//                // habe mich doch entschieden kommentare mit in rede zu nehmen weil bei html braucht man das
//                List<Element> redeKinder = redeElement.elements();
//                // System.out.println(redeKinder);
//                for (Element redeKind : redeKinder){
//                    // wenn paragraph dann einfach rede text in redebauer string
//                    if (redeKind.getName().equals("p")) {
//                        // redeBauer.append(redeKind.getText()).append("\n");
//                    }
//                    if (redeKind.getName().equals("kommentar")) {
//                        String kommentarText = redeKind.getText();
//                        // bissl unschön, aber alles andere wäre mir jetzt zu viel:
//                        // also ich füge schon beim parsen html elemente zum kommi hinzu, um in dann später in der rede optisch getrennt zu haben.
//                        // (nicht die beste lösung, schon klar, aber die einfachste erstmal)
//                        String htmlKommentar = "<span style='background-color: #b1eef2; color: #a0423a;'>" + kommentarText + "</span>";
//                        redeBauer.append(htmlKommentar).append("\n");
//
//                    }
//
//                }
//
//                String redInhalt = redeBauer.toString().trim();

//                // <p>-Elemente auslesen und zusammenfügen
//                List<Element> paragraphs = redeElement.elements("p");
//                for (Element paragraph : paragraphs) {
//                    redeBauer.append(paragraph.getText()).append("\n");
//                }
//                String redInhalt = redeBauer.toString().trim();
                // System.out.println("Extrahierter Redeinhalt für Reden-ID " + redID + ": " + redInhalt);

                // ------------- redner zu der reden parsen ------------

                // redner parsen oder von abgeordnetenID einfach nehmen

                // alle <p> Elemente in <rede> nach dem p mit klasse=redner durcsuchen
                List<Element> pElements = redeElement.elements("p");
                Element pRednerElement = null;

                for (Element pElement : pElements) {
                    // System.out.println(pElement);
                    if ("redner".equals(pElement.attributeValue("klasse"))) {
                        pRednerElement = pElement;
                        break; // erste <p klasse="redner"> gefunden
                    }
                }
                // System.out.println(pRednerElement);

                assert pRednerElement != null;
                Element rednerElement = pRednerElement.element("redner");

                String rednerID = rednerElement.attributeValue("id");
                Redner_File_Impl rednerFileImpl = factory.getRednerIDZuRedner().get(rednerID);

                if (rednerFileImpl == null) { // wenn es den redner noch nicht gibt: neuen machen
                    Abgeordneter_File_Impl abgeordneterFileImpl = factory.getAbgeordneterByID(rednerID); // prüfen, ob abgeordneterFileImpl existiert

                    if (abgeordneterFileImpl == null) {// wenn kein gemappeter abgeordneterFileImpl: redner neu erstellen ohne abgeordneten
                        Element nameElement = rednerElement.element("name");
                        String vorname = nameElement.elementText("vorname");
                        String nachname = nameElement.elementText("nachname");
                        String fraktion = nameElement.elementText("fraktion");
                        if (fraktion == null || fraktion.isEmpty()) {
                            // System.err.println("frak ist null für rednerID: " + rednerID + "und kein abge dazu gefunden");
                            fraktion = "fraktionslos"; // alle redner, die zu keiner abgeID zuweisbar sind = Gastredner
                        }

                        // redner ohne Bezug zu einem Abgeordneten erstellen (abgeordneterFileImpl = null)
                        rednerFileImpl = factory.createRedner(rednerID, null, vorname, nachname, fraktion);
                    }
                    if (abgeordneterFileImpl != null){
                        // falls abgeordneterFileImpl mit gleicher ID existiert: einfach daran nachbauen
                        // lieber fraktion aus sitzungen nehmen, bei mdb.xml war eher partei gemeint, ich nehme lieber fraktion einfach
                        Element nameElement = rednerElement.element("name");
                        String fraktion = nameElement.elementText("fraktion");
                        // System.err.println(fraktion);
                        if (fraktion == null) {
                            // System.out.println(fraktion);
                            fraktion = abgeordneterFileImpl.getFraktion();
                        }
                        rednerFileImpl = factory.createRedner(rednerID, abgeordneterFileImpl, abgeordneterFileImpl.getVorname(), abgeordneterFileImpl.getNachname(), fraktion);

                    }

                }

                // rede erstellen und direkt schon dem top und redner hinzufügen
                Rede_File_Impl redeFileImpl = factory.createRede(redID, topKey, rednerFileImpl);
//                System.out.println("Reden ID: " + rede.getRedID()); // reden ID
//                System.out.println("Redner ID: " + redeFileImpl.getRedner().getRednerID());
//                System.out.println("Abgeordneten ID: " + redeFileImpl.getRedner().getAbgeordneter_ref().getAbgeordnetenID()); // damn, komischer code, aber funktioniert, macht auch sinn
//                System.out.println("Rede: " + redeFileImpl.getRedInhalt());
                top.addRede(redeFileImpl);
                rednerFileImpl.addRede(redeFileImpl);



                // ----------- rede inhalt parsen ------------ unterscheiden zwischen rede und kommi
                List<Element> redeKinder = redeElement.elements();
                for (Element kind : redeKinder) {
                    if ("p".equals(kind.getName())) {
                        // abschnitt als redeinahlt adden
                        redeFileImpl.addAbschnitt(kind.getText(), false, "leer");
                    } else if ("kommentar".equals(kind.getName())) {
                        // abschnitt als kommi adden
                        String kommentarText = kind.getText();
                        Kommentar_File_Impl kommentar = factory.createKommentar(redID, kommentarText);
                        redeFileImpl.addAbschnitt(kommentarText, true, kommentar.getkID());
                    }

//                // --------------- kommentare zur rede parsen---------------------
//                List<Element> kommentare = redeElement.elements("kommentar");
//                for (Element kommentarElement : kommentare) {
//                    String kommentarInhalt = kommentarElement.getText();
//
//
//
//                    Kommentar_File_Impl kommentarFileImpl = factory.createKommentar(redID, kommentarInhalt);
                }


            }
        }

    }
}