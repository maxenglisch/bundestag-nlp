package Rest;

import Factory.MainFactory;
import Klassen.RedeAbschnitt;
import io.javalin.Javalin;
import MongoDB_Impls.*;
import io.javalin.rendering.template.JavalinFreemarker;
import org.bson.Document;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;


public class RESTHandler {


    private Javalin javalin;
    private final MainFactory factory;


    /**
     *     konstruktor der dann ein javalin erstellt mit dem port aus config
      */
    public RESTHandler (javalinConfig jlconfig, MainFactory factory) {

        javalin = Javalin.create(config -> {config.fileRenderer(new JavalinFreemarker());}).start(jlconfig.getPort());
        this.factory = factory;

    }


    /**
     * hier sind quasi alle meine javalin routen einzeln definiert, ich habe GET, POST und PUT benutzt (für DELETE habe ich keinen Fall der sinnvoll ist gesehen)
     * @param javalin man muss halt javalin übergebn natürlich
     */
    public void routes(Javalin javalin) {


        // ---------------------eine home seite "/" auf die man zuerst kommt (einfach link auf index) ---------------------------------
        // hab doch einfach direkt auf home, war useless mit homepage
        javalin.get("/", ctx -> { ctx.render("templates/home.ftl");});





        // ----------------------------- eine übersichtsseite für alle abges und gastredner, nach fraktionen (GET) --------------------------------
        javalin.get("/abgeordnete", ctx -> {
            List<Abgeordneter_MongoDB_Impl> abgeordnete = factory.getAbgeordneterMongoDBList();

            // abges nach nachname alphjabetisch sortieren



            List<Fraktion_MongoDB_Impl> fraktionen = factory.getFraktionMongoDBList();
            List<Redner_MongoDB_Impl> redner = factory.getRednerMongoDBList();

            // redner sorten (abges sind egal)
            redner.sort((r1, r2) -> r1.getNachname().compareToIgnoreCase(r2.getNachname()));


//            System.out.println(fraktionen.size());
//            System.out.println(abgeordnete.size());
//            System.out.println(redner.size());



            // neue map machen wie im tut gezeigt und da dann alles reinmachen zum rendern später
            Map<String, Object> map = new HashMap<>();
            map.put("abgeordnete", abgeordnete);
            map.put("redner", redner);
            map.put("fraktionen", fraktionen);

            // übersichtsseite template rendern
            ctx.render("templates/index.ftl", map);
        });




        // Voltextsuche mit GET
        javalin.get("/abgeordnete/suche", ctx -> {

            // query kommt aus url (ctx param)
            String query = ctx.queryParam("q");


            // hier textsearch aus dbhandler ausführen auf abgeordnete mit der query aus url

            List<Document> results = factory.getDbHandler().textSearch("Abgeordnete", query);

            // hier jetzt liste von abge mongos erstellen (einfach aus den docs die halt die begriffe enthalten)
            // bissl doppelt gemoppelt aber passte eigentlich
            List<Abgeordneter_MongoDB_Impl> abgeordnete = new ArrayList<>();
            for (Document doc : results) {
                abgeordnete.add(new Abgeordneter_MongoDB_Impl(factory.getDbHandler(), doc));
            }

            // am ende halt wieder results und query übergeben an das template das dann alles schön anzeigt

            Map<String, Object> map = new HashMap<>();
            map.put("query", query);
            map.put("abgeordnete", abgeordnete);

            ctx.render("templates/suchergebnisse.ftl", map);
        });




        // ----------------------------- dynamische seiten erstellen für abges (mit get) ------------------------------
        javalin.get("/abgeordnete/{id}", ctx -> {
            String id = ctx.pathParam("id");

            // redner und abges aus der factory ziehen (auch fraks und alle abges)
            Redner_MongoDB_Impl redner = factory.getRednerMongoByID(id);
            Abgeordneter_MongoDB_Impl abgeordneter = factory.getAbgeordneterMongoByID(id);

            List<Abgeordneter_MongoDB_Impl> abgeordnete = factory.getAbgeordneterMongoDBList();
            List<Fraktion_MongoDB_Impl> fraktionen = factory.getFraktionMongoDBList();

            List<Kommentar_MongoDB_Impl> kommentare = factory.getKommentarMongoDBList();

            MainFactory factory = this.factory;

//            // hier erstmal error für die gastrender (kann ihc später noch machen)
//            if (abgeordneter == null) {
//                ctx.status(404).result("Kein Abgeordneter mit id = " + id + " gefunden");
//                return;
//            }

//            // redner aus factory ziehen und nach abgeordneten filtern
//            List <Redner_MongoDB_Impl> rednerList = factory.getRednerMongoDBList();
//            Redner_MongoDB_Impl redner = null;
//            for (Redner_MongoDB_Impl r : rednerList) {
//                if (r.getAbgeordneter_ref().equals(abgeordnetenID)) {
//                    redner = r;
//                    break;
//                }
//            }

            // reden aus db ziehen und nach redner aufbauen (also die reden vom redner)
            List<Rede_MongoDB_Impl> reden = new ArrayList<>();
            if (redner != null) {
                for (Rede_MongoDB_Impl rede : factory.getRedeMongoDBList()) {
                    if (rede.getRedner().equals(redner.getRednerID())) {
                        reden.add(rede);
                    }
                }
            }

            // maps erstellen und hinzufügen wie im tut
            Map<String, Object> map = new HashMap<>();
            map.put("abgeordneter", abgeordneter);
            map.put("redner", redner);
            map.put("reden", reden);
            map.put("id", id);
            map.put("abgeordnete", abgeordnete);
            map.put("fraktionen", fraktionen);
            map.put("factory", factory);

            // Template rendern
            ctx.render("templates/abgeordneter.ftl", map);



        });


        // -------------------- update bild route mit PUT --------------------------------
        javalin.put("/abgeordneter/{id}/update-bild", ctx -> {

            // string id aus url ctxpath ziehen
            String id = ctx.pathParam("id");

            // neue bild url aus context body ziehen als string
            String updatedBildURL = ctx.bodyAsClass(Map.class).get("bildURL").toString();

//            System.out.println("empfang ID: " + id);
//            System.out.println("empanfg bild-URL: " + updatedBildURL);

            // redner aus db holen und bild mit setter aktualisieren
            Abgeordneter_MongoDB_Impl abgeordneter = factory.getAbgeordneterMongoByID(id);
            // abgeordneter kann auch null sein (bei gastrednern) da kann man halt das bild nicht ändern dann
            if (abgeordneter != null) {
                String alteBildURL = abgeordneter.getBildURL();
                abgeordneter.setBildURL(updatedBildURL); // setter macht db update in mongo impl automatisch
//                System.out.println("Neues bild: " + abgeordneter.getBildURL());
                // Log in die DB schreiben
                factory.getDbHandler().addToHistory(
                        "Bild",
                        String.format("Bild geändert von " + alteBildURL + " zu: " + updatedBildURL),
                        id
                );

                ctx.status(200).result("Bild neu gesetzt");
            } else {
                ctx.status(404).result("Abgeordneter nicht gefunden");
            }


        });


        // route für kommi zuweisen mit POST
        javalin.post("/kommentar/{kommentarID}/zuweisen", ctx -> {


            String kommentarID = ctx.pathParam("kommentarID");

            String zuweisungsart = ctx.formParam("zuweisungsart");

            Kommentar_MongoDB_Impl kommentar = factory.getKommentarMongoByID(kommentarID);
            System.out.println(kommentar);


            // zuweisung zu abge wenn zuweisung auf abge gestellt
            if (zuweisungsart.equals("abgeordneter")) {
                String abgeordnetenID = ctx.formParam("abgeordnetenID");
                //System.out.println(abgeordnetenID);
                kommentar.addAbgeordneterID(abgeordnetenID);
                //System.out.println(kommentar.getAbgeordneterID());
                // hier dann acuh wieder zur history dazupacken bei änderung
                factory.getDbHandler().addToHistory(
                        "Kommentar-Zuweisung",
                        String.format("Kommentar: " + kommentarID+ " zu Abgeordnetem: " + abgeordnetenID + " zugewiesen"),
                        kommentarID);



            } else if (zuweisungsart.equals("fraktion")) {
                String fraktion = ctx.formParam("fraktion");
                //System.out.println(fraktion);
                kommentar.addFraktion(fraktion);
                //System.out.println(kommentar.getFraktion());
                factory.getDbHandler().addToHistory(
                        "Kommentar-Zuweisung",
                        String.format("Kommentar: " + kommentarID+ " zu Fraktion: " + fraktion + " zugewiesen"),
                        kommentarID
                );
            }

            // redirect zur Abgeordnetenseite
            String abgeordnetenID = ctx.formParam("abgeordnetenID");
            ctx.redirect("/abgeordnete/" + abgeordnetenID);



        });

        // GET Route für historie
        javalin.get("/historie", ctx -> {
            List<Document> logs = factory.getDbHandler().getAllDocs("Historie");

            Map<String, Object> map = new HashMap<>();
            map.put("logs", logs);

            ctx.render("templates/historie.ftl", map);
        });

        // error route für 404 seite nicht gefunden
        javalin.error(404, ctx -> {
            ctx.render("templates/404.ftl");
        });


        // delete route die nix macht (wenn ich was löschen will dann lieber hier im code)
        javalin.delete("/delete", ctx -> {
            ctx.render("templates/home.ftl");
        });

        // NEU: Get Route für NLP Analysen visualisierungen
        javalin.get("/nlp_analysen", ctx -> {


            // analysen mit ids zum loopem
            List<String> ids = List.of("ID2019200100", "ID2019200200");
            List<Map<String, Object>> analysenData = new ArrayList<>();

            // daten aus db holen und POS getten
            for (String id : ids) {
                NLPAnalyse_MongoDB_Impl analyse = new NLPAnalyse_MongoDB_Impl(factory.getDbHandler(), id);

                // pos tags holen
                List<String> posTags = analyse.getPosTags();
                // map für counts vorbereiten (für barchart)
                Map<String, Integer> posCounts = new HashMap<>();

                for (String posTag : posTags) {
                    String[] parts = posTag.split(" "); // in der mitte das leerzeichen trennt typ und wirklichen pos
                    if (parts.length > 1) {
                        String type = parts[1]; // Nur den POS-Typ holen (z.B. NN, NE)
                        posCounts.put(type, posCounts.getOrDefault(type, 0) + 1);
                    }
                }
                // Named Entities holen
                List<String> namedEntities = analyse.getNamedEntities();

                // maps für entities
                Map<String, Integer> entityCounts = new HashMap<>();

                for (String entity : namedEntities) {
                    String[] parts = entity.split(" \\[\\ "); // Trennung bei " [ " um nicht bei [CDU] aus versehen zu trennen sondern wirklich beim typ und value
                    if (parts.length > 0) {
                        String value = parts[0].trim(); // Nur den Entity-Wert holen (z.B. "Deutschland", "FDP")
                        entityCounts.put(value, entityCounts.getOrDefault(value, 0) + 1);
                    }
                }

                // ma gucken obs klappt
                System.out.println("ID: " + id);
                System.out.println("poss: " + posCounts);
                System.out.println("entities: " + entityCounts);


                // listen vorbereiten für sentences und farben
                List<String> sentences = analyse.getSentenceSentiments();
                List<Map<String, String>> sentencesWithColors = new ArrayList<>();


                // hier einfach die tolle helper funktion aufrufen
                for (String sentence : sentences) {
                    sentencesWithColors.add(processSentence(sentence));
                }

                // vid hinzufügen um später streamen zu lassen
                String videoPath = "/video/" + id; // pfad zum vid (REST-Route weiter unten)




                // alles in die map packen
                Map<String, Object> data = new HashMap<>();
                data.put("id", analyse.getId());
                data.put("posCounts", posCounts); // häufigkeiten der POS-Typen
                data.put("entityCounts", entityCounts); // anzahl und gruppen der named entities
                data.put("sentences", sentencesWithColors); // sätze mit farben
                data.put("videoPath", videoPath); // video pfad
                analysenData.add(data);
            }

            // map in neue map und ans template geben
            Map<String, Object> map = new HashMap<>();
            map.put("analysen", analysenData);
            ctx.render("templates/nlp_analysen.ftl", map);
        });


        // REST ROUTE zum Video streamen
        javalin.get("/video/{id}", ctx -> {
            String id = ctx.pathParam("id");
            String videoPath = "src/main/resources/videos/" + id + ".mp4";
            java.nio.file.Path path = java.nio.file.Paths.get(videoPath);

            if (java.nio.file.Files.exists(path)) {
                ctx.result(java.nio.file.Files.newInputStream(path));
                ctx.contentType("video/mp4");
            } else {
                ctx.status(404).result("Video not found");
            }
        });








    }


    /**
     * Get javalin aus sich selbst, das brauche ich irgendwo, hab vergessen wo
     * @return
     */
    public Javalin getJavalin() {
        return javalin;
    }

    /**
     * neuer helper zur farbe für sentiment jeden einzelnen Satzes
     */
    private Map<String, String> processSentence(String sentence) {
        // sentiment aus den eckigen klllammern holen
        String[] parts = sentence.split("\\[|\\]");
        String sentimentString = parts[parts.length - 1].trim(); // hintere teil hat den wert
        double sentiment = Double.parseDouble(sentimentString);

        // rgb werte berchnen
        int red, green, blue;
        if (sentiment > 0) {
            // wenn über null = pos dann grün mehr hoch machen
            red = (int) (255 - sentiment * 200);
            green = (int) (255);
            blue = (int) (255 - sentiment * 200);
        } else if (sentiment < 0) {
            // wenn negativ dann rot erhöhen, grün weniger
            red = (int) (255);
            green = (int) (255 + sentiment * 200);
            blue = (int) (255 + sentiment * 200);
        } else {
            // 0 = neutral= weiß
            red = 255;
            green = 255;
            blue = 255;
        }

        // rbg string zur übergabe ans template
        String backgroundColor = "rgb(" + red + "," + green + "," + blue + ")";

        // Map erstellen, um die Daten des Satzes zu speichern
        Map<String, String> sentenceData = new HashMap<>();
        sentenceData.put("text", parts[0].trim()); // satz ohne senti
        sentenceData.put("sentiment", String.format("%.4f", sentiment)); // setni schön fromatiert
        sentenceData.put("color", backgroundColor); // farbe für senti

        return sentenceData;
    }




}
