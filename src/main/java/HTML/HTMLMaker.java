package HTML;

import Factory.MainFactory;
import File_Impls.Redner_File_Impl;
import MongoDB_Impls.*;

import java.io.FileWriter;
import java.io.IOException;


import java.util.List;
import java.util.Objects;


import java.util.Collections;
import java.util.Comparator;

public class HTMLMaker {

    private final MainFactory factory;

    public HTMLMaker(MainFactory factory) {
        this.factory = factory;
    }

    public void generatePortfolios(String outputDir, String sortParameter) {


        List<Abgeordneter_MongoDB_Impl> abgeordnete = factory.getAbgeordneterMongoDBList();
        List<Redner_MongoDB_Impl> rednerListe = factory.getRednerMongoDBList();
        List<Rede_MongoDB_Impl> reden = factory.getRedeMongoDBList();
        List<Sitzung_MongoDB_Impl> sitzungen = factory.getSitzungMongoDBList();
        List<Fraktion_MongoDB_Impl> fraktionen = factory.getFraktionMongoDBList();

//
//        System.out.println("Anzahl Abgeordnete: " + abgeordnete.size());
//        System.out.println("Anzahl Redner: " + rednerListe.size());
//        System.out.println("Anzahl Sitzungen: " + sitzungen.size());
//        System.out.println("Anzahl Fraktionen: " + fraktionen.size());


        // abgeordnete liste sortieren: mit parameter aus übergabe (nummern)
        // hier cool switch case für alles cases nehmen (besser als 100 ifs)
        switch(sortParameter) {

            case "nachname":
                // nach alphabet mit nachnamen
                rednerListe.sort(Comparator.comparing(Redner_MongoDB_Impl::getNachname));
                break;
            case "vorname":
                // nach alphabet mit vorname
                rednerListe.sort(Comparator.comparing(Redner_MongoDB_Impl::getVorname));
                break;
            case"id":
                // nach id
                rednerListe.sort(Comparator.comparing(Redner_MongoDB_Impl::getRednerID));
                break;
            case "reden":
                // nach anzahl reden aufsteigend
                rednerListe.sort(Comparator.comparingInt(redner -> redner.getReden().size()));
                break;
            default:
                // default einfach nach Nachname:
                System.err.println("Ungültiger sortParameter: " + sortParameter + "Ausgabe sortiert nach Nachname");
                abgeordnete.sort(Comparator.comparing(Abgeordneter_MongoDB_Impl::getNachname));
                break;

        }







        for (Redner_MongoDB_Impl redner : rednerListe) {
            // System.out.println("Generiere Portfolio für " + redner.getVorname() + " " + redner.getNachname());
            // Abgeordneter toHTML generieren
            StringBuilder htmlContent = new StringBuilder();

            // hier dann erstmal die groben basics und die reden IDs erstmal
            // htmlContent.append(redner.toHTML());
            // System.out.println(htmlContent);
            // System.out.println(outputDir + abgeordneter.getAbgeordnetenID() + ".html");
            // saveHTMLToFile(htmlContent.toString(), outputDir + redner.getAbgeordneter_ref() + ".html");

            // hier noch alle abgeordneten infos verknüpfen mit redner:
            Abgeordneter_MongoDB_Impl abgeordneter = factory.getAbgeordneterMongoByID(redner.getAbgeordneter_ref());

            // wenn keine abgeordneten ID zum redner da ist: Gastredner, kümmer ich vllt später noch drum,
            // aber aufgabenstellung sagt: nur abgeordnete (hätte mir einiges an arbeit erspart, daran zu denken)
            // also: jetzt toHTML2 wenn keine abgeordneten ID da ist um dann so die infos vom redner zu kriegen
            if (abgeordneter == null) {
                htmlContent.append(redner.toHTML2());
            }

            // hier den toHTML vom abgeordneten für alle weiter infos (beruf, wahlkreis, funktionen etc. )
            // hier auch die Fotos dann einfügen (bzw bei abgeordneter toHTML dann)
            else {
                htmlContent.append(abgeordneter.toHTML());
            }


            // hier jetzt alle reden IDs von redner toHTML mit links zu den reden
            htmlContent.append(redner.toHTML());

            // hier jetzt die reden durchgehen und über redner ID verknüfpen mit der rede
            for (Rede_MongoDB_Impl rede : reden) {
                // hier rede rednerID mit redner Abge ref ID verknüpfen (Redner ID in Rede sollte = redner Abgeordneten ID sein dann alle reden von abgeordneten mit
                // to HTML hier reinbauen
                // System.out.println(rede.getRedner() + "       " + redner.getAbgeordneter_ref());
                if (Objects.equals(rede.getRedner(), redner.getRednerID())) {
                    // System.out.println(rede.toHTML());
                    // boolean checker = true;
                    // htmlContent.append(rede.toHTML());
                    // break;
                    // hier erstmal die rede ID und topID dann sitzungsdetails dann inhalt
                    htmlContent.append(rede.toHTML());

                    // jetzt noch die sitzungsinfos der rede einbauen
                    for (Sitzung_MongoDB_Impl sitzung : sitzungen) {
                        // hier checken ob die sitzungsID == der rede top ID gesplitten beim _ ist (habe ja als topID
                        // immer den topkey aus sitzungsNR_TopNR

                        // System.out.println(sitzung.getsID() + "  " + rede.getTopID().split("_")[0]);

                        if (Objects.equals(sitzung.getsID(), rede.getTopID().split("_")[0])) {
                            htmlContent.append("<div class='sitzungs-info'>")
                                    .append("<h3>Sitzungsdetails:</h3>")
                                    .append(sitzung.toHTML()) // hier einfach das toHTML von sitzung nehmen
                                    .append("</div>");
                        }

                        // rede erst nach sitzungsdetails adden
                    }
                    // hier dann der inhalt
                    htmlContent.append(rede.toHTML2());
                }
                // break;
            }
            saveHTMLToFile(htmlContent.toString(), outputDir + redner.getRednerID() + ".html");
            // break;
        }

        // hier einsteigsseite so schön mit style und allem (später)
        generateEntryPage(outputDir, fraktionen, rednerListe);

        System.out.println("Portfolios erfolgreich generiert.");
    }

    // hier dann mit filewriter eien html file aus nem string erstellen
    private void saveHTMLToFile(String html, String filePath) {
        try (FileWriter writer = new FileWriter(filePath, false)) { // hier false für überschreiben statt anhängen
            writer.write(html);
        } catch (IOException e) {
            System.err.println("Fehler beim Speichern der Datei: " + e.getMessage());
        }
    }


    // schicke einstigsseite
    private void generateEntryPage(String outputDir, List<Fraktion_MongoDB_Impl> fraktionen, List<Redner_MongoDB_Impl> rednerListe) {
        StringBuilder entryPage = new StringBuilder();
        entryPage.append("<!DOCTYPE html>")
                .append("<html>")
                .append("<head>")
                .append("<title>Reden-Portfolios nach Fraktionen</title>")
                .append("</head>")
                .append("<body>")
                .append("<h1>Reden-Portfolios nach Fraktionen</h1>");

        // alle fraks durchloopen
        for (Fraktion_MongoDB_Impl fraktion : fraktionen) {

//            // fraktionslose werden übersprungen (nur abgoerdnete)
//            if (fraktion.getfrakName().equalsIgnoreCase("Fraktionslos")) {
//                continue;
//            }

            // fraktion als header mit toTHML (ohne fraktionslos)
            entryPage.append(fraktion.toHTML());

            // liste für alle redner bzw abgeordnete der frak
            entryPage.append("<ul>");

            // redner loopen und fraktion checken
            for (Redner_MongoDB_Impl redner : rednerListe) {
                if (redner.getFraktion().equalsIgnoreCase(fraktion.getfrakName())) {
                    // alle mit gleicher frak hinzufügen
                    entryPage.append("<li>")
                            .append("<a href='")
                            .append(redner.getRednerID())
                            .append(".html'>")
                            .append(redner.getVorname()).append(" ").append(redner.getNachname())
                            .append("</a>")
                            .append(" ( ").append(redner.getReden().size()).append(" Reden").append(" )")
                            .append("</li>");
                }
            }

            // liste für abges schließen
            entryPage.append("</ul>");
        }

        entryPage.append("</body>")
                .append("</html>");

        // startseite speicherb
        saveHTMLToFile(entryPage.toString(), outputDir + "index.html");
    }

}