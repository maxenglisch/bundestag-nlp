package Interfaces;

import File_Impls.Tagesordnungspunkt_File_Impl;

import java.util.List;

public interface ISitzung<T> {

    String getsID();
    void setsID(String sID);

    int getwpNr();
    void setwpNr(int wp);

    String getsDatum();
    void setsDatum(String datum);

    String getsOrt();
    void setsOrt(String ort);

    String getsStartzeit();
    void setsStartzeit(String startzeit);

    String getsEndzeit();
    void setsEndzeit(String endzeit);

    boolean getsOeffentlichkeit();
    void setsOeffentlichkeit(boolean oeffentlich);

    List<T> getTagesordnungspunkte();
    void addTagesordnungspunkt(T top);
}