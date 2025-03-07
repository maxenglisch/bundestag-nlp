package Interfaces;

import File_Impls.Abgeordneter_File_Impl;
import java.util.List;

/**
 * Interface für die Klasse Fraktion.
 */
public interface IFraktion<T> {

    String getfrakName();
    void setfrakName(String fraktionName);

    List<T> getfrakMitglieder();
    void setfrakMitglieder(List<T> mitglieder);

    // adder für mitglieder
    void addfrakMitglied(T abgeordneter);
}