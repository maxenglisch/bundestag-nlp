package File_Impls;

// imports für db handler sachen, nlp pipelines und jcas kram


// alle tokens, senteces und sowas von tudarmstadt und anderen importen (von abrami + discord vorher keine ahnung wie man das holen sollte...)
import Interfaces.INLPAnalyse;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import org.hucompute.textimager.uima.type.Sentiment;
import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.dependency.Dependency;

// Standards imports
import java.util.List;



public class NLP_Analyse_File_Impl implements INLPAnalyse {


    private String redeId;
    private List<Token> tokens;
    private List<POS> posTags;
    private List<Dependency> dependencies;
    private List<Sentiment> sentiments;
    private List<NamedEntity> namedEntities;




    // hier die ganzen getter, mache ich später...
    @Override
    public String getId() {
        return "";
    }

    @Override
    public List getTokens() {
        return List.of();
    }

    @Override
    public List getSentences() {
        return List.of();
    }

    @Override
    public List getNamedEntities() {
        return List.of();
    }

    @Override
    public List getPosTags() {
        return List.of();
    }

    @Override
    public List getDependencies() {
        return List.of();
    }

    @Override
    public List getSentenceSentiments() {
        return List.of();
    }
}
