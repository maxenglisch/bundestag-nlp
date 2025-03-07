package Klassen;

public class Funktion {
    private String institution;
    private String funktion;


    // funktion
    public Funktion(String institution, String funktion) {
        this.institution = institution;
        this.funktion = funktion;
    }


    // toString overriden damit die Funktion auch richtig konvertiert wird und schön aussieht
    @Override
    public String toString() {
        return this.institution + " - " + this.funktion;
    }


    // getter, setter
    public String getInstitution() {
        return institution;
    }

    public String getFunktion() {
        return funktion;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public void setFunktion(String funktion) {
        this.funktion = funktion;
    }
}
