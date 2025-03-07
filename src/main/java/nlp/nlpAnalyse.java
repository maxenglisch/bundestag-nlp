package nlp;
// imports für db handler sachen, nlp pipelines und jcas kram
import Database.MongoDBHandler;
import MongoDB_Impls.Rede_MongoDB_Impl;
import org.apache.uima.jcas.JCas;
import org.apache.uima.fit.util.JCasUtil;
import org.texttechnologylab.DockerUnifiedUIMAInterface.DUUIComposer;



// alle tokens, senteces und sowas von tudarmstadt und anderen importen (von abrami + discord vorher keine ahnung wie man das holen sollte...)
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import org.hucompute.textimager.uima.type.Sentiment;
import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.dependency.Dependency;


// Standards improts
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;



public class nlpAnalyse {
    public static void main(String[] args) {
        try {
            // erstmal db verbinden wie immer
            MongoDBHandler dbHandler = new MongoDBHandler();
            dbHandler.connectToDB();

            // neue pipeline aufsetzen
            nlpPipeline pipeline = new nlpPipeline(new DUUIComposer());


            // reden aus db filtern (die aus olat ordnername "Alpha" bzw. "1")
            List<String> ids = List.of("ID2019200100", "ID2019200200");


            // pipeline aufsetzen (nur EINMAL!!) sonst gibts probleme wenns im loop zweimal ist. (danke daniel)
            pipeline.setupPipeline(); // remote


            for (String id : ids) {

                // die docs mit der id aus db laden mit dbhandler
                Document doc = dbHandler.findDocument("Reden", "_id", id);

                // wenn gefunden (sollte eigentlich, habe es im compass passend gefunden) dann mongo impl bauen

                // mogno impls aus doc machen
                Rede_MongoDB_Impl rede = new Rede_MongoDB_Impl(dbHandler, doc);

                // neue toCAS methode verwednen für mongo_impl -> jCAS
                JCas cas = rede.toCAS();

                // video in video view einbauen
                String videopath = "src/main/resources/videos/" + id +".mp4";
                pipeline.makeVideoView(cas, videopath);

//
//                JCas testCas = JCasFactory.createJCas();
//                testCas.setDocumentText("Ich bin Max. ich lebe in Deutschland. Frankfurt ist toll. Anton ist ganz doof");
//                testCas.setDocumentLanguage("de");
//
//                // text view erstellen und von initial view kopieren
//                JCas textView = testCas.createView("Text");
//                String initialText = testCas.getView("_InitialView").getDocumentText();
//                textView.setDocumentText(initialText);
//
//                System.out.println("Text in der Text-View vor der Pipeline: " + textView.getDocumentText());
//
//
//                // Pipeline setup und run
//                pipeline.setupPipeline();
//
//                pipeline.runPipeline(testCas);
//
//
//                // views zeigen
//                System.out.println(" views Nach pipeline: ");
//                testCas.getViewIterator().forEachRemaining(view -> System.out.println(view.getViewName()));
//
//
//                // annotations von spccy (in text view) anzeigen)
//            System.out.println("annotations nach  pipeline (in text view):");
//            textView.getAnnotationIndex().forEach(annotation -> {
//                System.out.println("Typ: " + annotation.getType().getName());
//                System.out.println("Text: " + annotation.getCoveredText());
//            });
//
//
//
//
//
//            // Annotationen aus der Text-View ausgeben
//
//
//            // Tokens ausgeben
//            List<String> tokens = new ArrayList<>();
//            for (Token token : JCasUtil.select(textView, Token.class)) {
//                tokens.add(token.getCoveredText());
//            }
//            System.out.println("Tokens: " + tokens);
//
//            // Sätze ausgeben
//            List<String> sentences = new ArrayList<>();
//            for (Sentence sentence : JCasUtil.select(textView, Sentence.class)) {
//                sentences.add(sentence.getCoveredText());
//            }
//            System.out.println("Sentences: " + sentences);
//


                // -------------------- hier jetzt mit den richtigen reden --------------------

                // pipilne mit spacy und vader auf cas objekt laufen lassen (siehe nlpPipepline.java)
                pipeline.runPipeline(cas);





                // results ausgeben (z. B. extrahierte sentences)
                // pipeline.printPipelineRes(testCas);

                // Ergebnisse auslesen



                // noch textview, transcriptview extrahieren und dann damit weiterarbeiten
                JCas textView = cas.getView("Text");

                JCas transcriptView = cas.getView("Transcript");


                // views zeigen
                System.out.println(" views Nach pipeline: ");
                cas.getViewIterator().forEachRemaining(view -> System.out.println(view.getViewName()));



                System.out.println("Text-View text: " + textView.getDocumentText());
                System.out.println("Transcript-View text: " + transcriptView.getDocumentText());



                // hier annotations angucken (klappt irgendwie nicht mehr, komischer index fehler,
                // !! auskommentiert lassen, sonst fehler!!

//                System.out.println("text view annotations: ");
//                textView.getAnnotationIndex().forEach(annotation -> {
//                    System.out.println("Typ: " + annotation.getType().getName());
//                    System.out.println("Text: " + annotation.getCoveredText());
//                });
//
//
//                System.out.println("transcript view annotations: ");
//                transcriptView.getAnnotationIndex().forEach(annotation -> {
//                    System.out.println("Typ: " + annotation.getType().getName());
//                    System.out.println("Text: " + annotation.getCoveredText());
//                });




            // ------------ text annotations rausziehen als string listen für db (einfache struktur) ----------------

                // hier jetzt die tokens speichern aus der pipeline analyse
                List<String> textTokens = new ArrayList<>();
                // alle tokens im cas holen und den text zur liste
                for (Token token : JCasUtil.select(textView, Token.class)) {
                    textTokens.add(token.getCoveredText());
                }


                // hier die pos tags holen
                List<String> textPosTags = new ArrayList<>();

                for (POS pos : JCasUtil.select(textView, POS.class)) {
                    textPosTags.add(pos.getCoveredText() + " " + pos.getPosValue());
                }

                // dependencies aus cas holen wie sonst auch
                List<String> textDependencies = new ArrayList<>();
                // hier mit allen möglichen coolen sachen, governor, dependent und type, alles gefunden durch . und mal gucken was das ist.
                for (Dependency dependency : JCasUtil.select(textView, Dependency.class)) {
                    textDependencies.add(dependency.getGovernor().getCoveredText() + "[<- governor] -> [dependent ->]"
                            + dependency.getDependent().getCoveredText() + " Typ: " + dependency.getDependencyType());
                }



                // jetzt sencteces rausholen aus cas
                List<String> textSentences = new ArrayList<>();
                for (Sentence sentence : JCasUtil.select(textView, Sentence.class)) {
                    textSentences.add(sentence.getCoveredText());
                }




                // jetzt sentiment analysiern, pro Sentence auslesen
                List<String> textSentenceSentiments = new ArrayList<>();

                // alle sätze loopen
                for (Sentence sentence : JCasUtil.select(textView, Sentence.class)) {

                    // sentiments pro satz holen (eigentlich immer nur einer, aber irgendwie gehts trotzdem mit select Covered besser.)
                    // ah der holt dann die liste mit allen sachen, brauche ja aber nur sentiment
                    List<Sentiment> sentiments = JCasUtil.selectCovered(Sentiment.class, sentence);

                    // sentiment value aus dem ganzen vader annotations ziehen
                    double sentimentValue = sentiments.get(0).getSentiment();

                    // satz und average senti zur liste hinzufügen (später dann in db)
                    String result = sentence.getCoveredText() + " [" + sentimentValue + "]";
                    textSentenceSentiments.add(result);

                }


                // hier noch named entities zum schluss
                List<String> textNamedEntities = new ArrayList<>();
                for (NamedEntity namedEntity : JCasUtil.select(textView, NamedEntity.class)) {
                    textNamedEntities.add(namedEntity.getCoveredText() + " [ " + namedEntity.getValue() + " ]");
                }













                // --------------------- hier transcript view annotations rausziehen als string listen -------------





                // hier jetzt die tokens speichern aus der pipeline analyse
                List<String> transcriptTokens = new ArrayList<>();
                // alle tokens im cas holen und den text zur liste
                for (Token token : JCasUtil.select(transcriptView, Token.class)) {
                    transcriptTokens.add(token.getCoveredText());
                }


                // hier die pos tags holen
                List<String> transcriptPosTags = new ArrayList<>();

                for (POS pos : JCasUtil.select(transcriptView, POS.class)) {
                    transcriptPosTags.add(pos.getCoveredText() + " " + pos.getPosValue());
                }

                // dependencies aus cas holen wie sonst auch
                List<String> transcriptDependencies = new ArrayList<>();
                // hier mit allen möglichen coolen sachen, governor, dependent und type, alles gefunden durch . und mal gucken was das ist.
                for (Dependency dependency : JCasUtil.select(transcriptView, Dependency.class)) {
                    transcriptDependencies.add(dependency.getGovernor().getCoveredText() + "[<- governor] -> [dependent ->]"
                            + dependency.getDependent().getCoveredText() + " Typ: " + dependency.getDependencyType());
                }



                // jetzt sencteces rausholen aus cas
                List<String> transcriptSentences = new ArrayList<>();
                for (Sentence sentence : JCasUtil.select(transcriptView, Sentence.class)) {
                    transcriptSentences.add(sentence.getCoveredText());
                }




                // jetzt sentiment analysiern, pro Sentence auslesen
                List<String> transcriptSentenceSentiments = new ArrayList<>();

                // alle sätze loopen
                for (Sentence sentence : JCasUtil.select(transcriptView, Sentence.class)) {

                    // sentiments pro satz holen (eigentlich immer nur einer, aber irgendwie gehts trotzdem mit select Covered besser.)
                    // ah der holt dann die liste mit allen sachen, brauche ja aber nur sentiment
                    List<Sentiment> sentiments = JCasUtil.selectCovered(Sentiment.class, sentence);

                    // sentiment value aus dem ganzen vader annotations ziehen
                    double sentimentValue = sentiments.get(0).getSentiment();

                    // satz und average senti zur liste hinzufügen (später dann in db)
                    String result = sentence.getCoveredText() + " [" + sentimentValue + "]";
                    transcriptSentenceSentiments.add(result);

                }


                // hier noch named entities zum schluss
                List<String> transcriptNamedEntities = new ArrayList<>();
                for (NamedEntity namedEntity : JCasUtil.select(transcriptView, NamedEntity.class)) {
                    transcriptNamedEntities.add(namedEntity.getCoveredText() + " [ " + namedEntity.getValue() + " ]");
                }





//                // hier als test einfach mal alles ausgeben obs klappt
//                System.out.println("Analysen für Rede-ID: " + id);
//                System.out.println("Tokens: " + tokens);
//                System.out.println("Sentences: " + sentences);
//                System.out.println("Named Entities: " + namedEntities);
//                System.out.println("POS-Tags: " + posTags);
//                System.out.println("Dependencies: " + dependencies);
//                System.out.println("Sentiments: " + sentenceSentiments);





                // jetzt text view analysis als neues unterdoc in die db dazu bauen
                Document textAnalysis = new Document()
                        .append("tokens", textTokens)
                        .append("pos_tags", textPosTags)
                        .append("dependencies", textDependencies)
                        .append("named_entities", textNamedEntities)
                        .append("sentences", textSentences)
                        .append("sentence_sentiments", textSentenceSentiments);

                // jetzt transcript view analysis als neues unterdoc in die db dazu bauen
                Document transcriptAnalysis = new Document()
                        .append("tokens", transcriptTokens)
                        .append("pos_tags", transcriptPosTags)
                        .append("dependencies", transcriptDependencies)
                        .append("named_entities", transcriptNamedEntities)
                        .append("sentences", transcriptSentences)
                        .append("sentence_sentiments", transcriptSentenceSentiments);


                Document analysis = new Document()
                        .append("_id", id)
                        .append("textView", textAnalysis)
                        .append("transcriptView", transcriptAnalysis);
                dbHandler.addDocument("NLP_Analysen", analysis);

            }






            // Verbindung schließen
            dbHandler.closeConnection();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
