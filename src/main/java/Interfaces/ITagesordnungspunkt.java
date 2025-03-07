package Interfaces;

import File_Impls.Rede_File_Impl;
import java.util.List;

/**
 * Interface für die Klasse Tagesordnungspunkt_File_Impl.
 */
public interface ITagesordnungspunkt<T> {

    // getter und setter basics

    String getTopID();
    void setTopID(String topID);

    String getSitzungsID();
    void setSitzungsID(String sitzungsID);

    List<T> getTopReden();
    // add rede statt set reden, wird ja über xml oder db gefüllt dann
    void addRede(T redeFileImpl);
}