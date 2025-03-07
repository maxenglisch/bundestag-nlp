package nlp;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import org.apache.uima.cas.CASException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.fit.util.JCasUtil;
import org.texttechnologylab.DockerUnifiedUIMAInterface.DUUIComposer;
import org.texttechnologylab.DockerUnifiedUIMAInterface.driver.DUUIDockerDriver;
import org.texttechnologylab.DockerUnifiedUIMAInterface.driver.DUUIRemoteDriver;
import org.texttechnologylab.DockerUnifiedUIMAInterface.driver.DUUIUIMADriver;
import org.texttechnologylab.DockerUnifiedUIMAInterface.lua.DUUILuaContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;



import org.apache.commons.io.FileUtils;
import java.io.File;
import java.util.Base64;


public class nlpPipeline {

    // paar finals für später
    private final DUUIComposer composer;
    private final int iWorkers = 1; // anzahl an threads oder sowas?

    // konstruktor für pipeline mit composer
    public nlpPipeline(DUUIComposer composer) {
        this.composer = composer;
    }


    /**
     * die wichtigste methode, komplettes setup von composer, DUUI, spacy, gervader und whisper
     * (hauptsächlich von daniel und abrami übernommen)
     * @throws Exception
     */
    public void setupPipeline() throws Exception {


        // komischen lua kontext machen
        DUUILuaContext ctx = new DUUILuaContext().withJsonLibrary();



        composer.withSkipVerification(true) // verifikation skippen
                .withLuaContext(ctx) // ctx von abrami
                .withWorkers(iWorkers); // ja, anzahl threads für den composer i guess (nach abramis comments)



        // erstmal mit remote driver probieren (docker lokal war irgendwie komisch)
        DUUIRemoteDriver remoteDriver = new DUUIRemoteDriver();
        // DUUIDockerDriver dockerDriver = new DUUIDockerDriver();
        DUUIUIMADriver uima_driver = new DUUIUIMADriver();

        // driver zum composer hinzufügen
        composer.addDriver(remoteDriver, uima_driver);


        // ---------- erstmal text analyse mit text aus db -----------------


        // spaCy für tokens, pos, named entities etc.
        composer.add(new DUUIRemoteDriver.Component("http://spacy.lehre.texttechnologylab.org/")
                .withScale(iWorkers)
                .withSourceView("Text")
                .withTargetView("Text")
                .build());

        // GerVader für sentiment analysis
        composer.add(new DUUIRemoteDriver.Component("http://gervader.lehre.texttechnologylab.org/")
                .withScale(iWorkers)
                .withSourceView("Text")
                .withTargetView("Text")
                .withParameter("selection", "text") // Parameter stand ja in dem beispielcode von abrami, hoffe das passt so
                .build());







        // ---------------------- hier jetzt Whisper für Video transkription analyse ---------------------

        composer.add(new DUUIRemoteDriver.Component("http://whisperx.lehre.texttechnologylab.org")
                .withScale(1)
                .withSourceView("Video")        // View wo jetzt der video string drin ist
                .withTargetView("Transcript")  // target dann neuer view transcript für nochmal spacy + gervader
                .build());


        composer.add(new DUUIRemoteDriver.Component("http://spacy.lehre.texttechnologylab.org")
                .withScale(1)
                .withSourceView("Transcript") // diesmal beides als transcript,
                .withTargetView("Transcript") // also source und target, dann haben wir da alle annotations
                .build());

        composer.add(new DUUIRemoteDriver.Component("http://gervader.lehre.texttechnologylab.org")
                .withScale(1)
                .withSourceView("Transcript") // wie text halt vorher
                .withTargetView("Transcript") // könnte auch als ein ding, aber so mag ichs lieber
                .withParameter("selection", "text")
                .build());






    }


    /**
     * einfach nochmal schöne kleine funktion für run pipeline
     * @param cas cas das man will
     * @throws Exception joa exception halt
     */
    public void runPipeline(JCas cas) throws Exception {
        composer.run(cas);
    }


    /**
     * mach ich vielleicht noch, aber habs jetzt eigentlich auch in der main
     * @param cas
     * @throws CASException
     */
    public void printPipelineRes(JCas cas) throws CASException {
        // sentences und tokens printen


    }

    /**
     * Kleiner helper funktion für video view erstllen und byte string codierung
     * @param cas das benutzte cas für die video view
     * @param videoPath filepath zum video
     * @throws IOException wurde von intellij gesagt
     * @throws CASException ""
     */
    public void makeVideoView(JCas cas, String videoPath) throws IOException, CASException {

        // video in byte array base64 konvertieren (String) (von Abrami beispiel)
        File videoFile = new File(videoPath);
        byte[] fileBytes = FileUtils.readFileToByteArray(videoFile);
        String base64Video = Base64.getEncoder().encodeToString(fileBytes);

        // neue view für video machen und video reinpacken
        JCas videoView = cas.createView("Video");
        videoView.setSofaDataString(base64Video, "video/mp4");
        videoView.setDocumentLanguage("de");
    }


}
