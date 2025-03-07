package Interfaces;

import java.util.List;

/**
 * Interface für die Klasse Kommentar.
 */
public interface IKommentar {


    String getkID();
    void setkID(String kID);

    String getRedeID();
    void setRedeID(String redeID);

    String getkInhalt();
    void setkInhalt(String kInhalt);

    List<String> getAbgeordnetenIDs();
    void addAbgeordneterID(String abgeordneterID);

    List<String> getFraktionen();
    void addFraktion(String fraktion);
}