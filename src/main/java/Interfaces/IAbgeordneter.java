package Interfaces;

import java.util.List;
import Klassen.Funktion;

public interface IAbgeordneter<T> {



    String getBildURL();
    void setBildURL(String bildURL);
    // Methoden von Abgeordneter
    String getAbgeordnetenID();
    void setAbgeordnetenID(String abgeordnetenID);
    String getFraktion();
    void setFraktion(String fraktion);
    String getAnrede();
    void setAnrede(String anrede);
    String getAkademTitel();
    void setAkademTitel(String akademTitel);
    String getFamilienstand();
    void setFamilienstand(String familienstand);
    String getReligion();
    void setReligion(String religion);
    String getBeruf();
    void setBeruf(String beruf);
    String getWkrLand();
    void setWkrLand(String wkrLand);
    int getWkrNummer();
    void setWkrNummer(int wkrNummer);
    String getMandatsArt();
    void setMandatsArt(String mandatsArt);
    List<T> getFunktionen();
    void setFunktionen(List<Funktion> funktionen);
}