package MongoDB_Impls;



import Database.MongoDBHandler;
import Interfaces.INLPAnalyse;
import org.bson.Document;

import java.util.List;


/**
 * klasse für die analyse mit gettern sehr nice
 */
public class NLPAnalyse_MongoDB_Impl implements INLPAnalyse<String, String, String, String, String, String> {

    private final MongoDBHandler dbHandler;
    private final Document doc;
    private final String COLLECTION_NAME = "NLP_Analysen";

    private String id;

    // unter docs für textView und transcriptView
    private Document textView;
    private Document transcriptView;

    // konstruktor für mongo impl mit handler und id um zu suchen
    public NLPAnalyse_MongoDB_Impl(MongoDBHandler dbHandler, String id) {
        this.dbHandler = dbHandler;
        this.doc = dbHandler.findDocument(COLLECTION_NAME, "_id", id);

        // views aus dem main doc holen
        this.id = doc.getString("_id");
        this.textView = doc.get("textView", Document.class);
        this.transcriptView = doc.get("transcriptView", Document.class);
    }

    // getter für die ID
    @Override
    public String getId() {
        return id;
    }

    // getter für tokens, POS, etc. aus textView
    @Override
    public List<String> getTokens() {
        return textView.getList("tokens", String.class);
    }

    @Override
    public List<String> getPosTags() {
        return textView.getList("pos_tags", String.class);
    }

    @Override
    public List<String> getDependencies() {
        return textView.getList("dependencies", String.class);
    }

    @Override
    public List<String> getNamedEntities() {
        return textView.getList("named_entities", String.class);
    }

    @Override
    public List<String> getSentences() {
        return textView.getList("sentences", String.class);
    }

    @Override
    public List<String> getSentenceSentiments() {
        return textView.getList("sentence_sentiments", String.class);
    }

    // getter für tokens, POS, etc. aus transcriptView
    public List<String> getTranscriptTokens() {
        return transcriptView.getList("tokens", String.class);
    }

    public List<String> getTranscriptPosTags() {
        return transcriptView.getList("pos_tags", String.class);
    }

    public List<String> getTranscriptDependencies() {
        return transcriptView.getList("dependencies", String.class);
    }

    public List<String> getTranscriptNamedEntities() {
        return transcriptView.getList("named_entities", String.class);
    }

    public List<String> getTranscriptSentences() {
        return transcriptView.getList("sentences", String.class);
    }

    public List<String> getTranscriptSentenceSentiments() {
        return transcriptView.getList("sentence_sentiments", String.class);
    }
}
