package Interfaces;

import File_Impls.Abgeordneter_File_Impl;
import File_Impls.Rede_File_Impl;
import java.util.List;

/**
 * Interface für die Klasse Redner_File_Impl.
 */
public interface IRedner<T, S> {


    String getRednerID();
    void setRednerID(String rednerID);

    T getAbgeordneter_ref();
    void setAbgeordneter_ref(T abgeordneter_FileImpl_ref);

    String getVorname();
    void setVorname(String vorname);

    String getNachname();
    void setNachname(String nachname);

    String getFraktion();
    void setFraktion(String fraktion);

    List<S> getReden();

    // add reden zum redner
    void addRede(S redeFileImpl);
}