package Klassen;

public abstract class Person {

    // privat, aber abgeordneter kann get/set über methoden public machen
    private String vorname;
    private String nachname;
    private String geschlecht;
    private String gebDatum;
    private String gebOrt;

    // Klassen.Person protected zugägnlich machen, weil nicht in factory
    protected Person(String vorname, String nachname, String geschlecht,
                  String gebDatum, String gebOrt) {
        this.vorname = vorname;
        this.nachname = nachname;
        this.geschlecht = geschlecht;
        this.gebDatum = gebDatum;
        this.gebOrt = gebOrt;
    }

    // alle getter/setter von person
    public String getName() {
        return vorname + " " + nachname;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getGeschlecht() {
        return geschlecht;
    }

    public void setGeschlecht(String geschlecht) {
        this.geschlecht = geschlecht;
    }

    public String getGebDatum() {
        return gebDatum;
    }

    public void setGebDatum(String gebDatum) {
        this.gebDatum = gebDatum;
    }

    public String getGebOrt() {
        return gebOrt;
    }

    public void setGebOrt(String gebOrt) {
        this.gebOrt = gebOrt;
    }

}
