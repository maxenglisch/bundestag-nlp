package Interfaces;

import File_Impls.Kommentar_File_Impl;
import File_Impls.Redner_File_Impl;
import Klassen.RedeAbschnitt;

import java.util.List;

/**
 * Interface für die Klasse Rede_File_Impl.
 */
public interface IRede<T, S> {

    // get set wie immer
    String getRedID();
    void setRedID(String redID);

    String getTopID();
    void setTopID(String topID);

//    String getRedInhalt();
//    void setRedInhalt(String redInhalt);

    S getRedner();
    void setRedner(S redner);

    // List<T> getRedKommentare();

    // add statt set macht mehr sinn
    // void addredKommentar(T kommentar);



    // neue methoden für abschnitte
}