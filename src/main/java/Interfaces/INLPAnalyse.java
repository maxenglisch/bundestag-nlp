package Interfaces;

import java.util.List;

/**
 * Interface für meine neuen nlp analyse klassen
 * @param <T> hier wieder generisch für String oder eben die ganzen Klassen
 */

public interface INLPAnalyse<T, S, N, P, D, SS> {

    String getId();

    List<T> getTokens();

    List<S> getSentences();

    List<N> getNamedEntities();

    List<P> getPosTags();

    List<D> getDependencies();

    List<SS> getSentenceSentiments();
}
