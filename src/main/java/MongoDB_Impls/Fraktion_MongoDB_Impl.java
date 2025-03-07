package MongoDB_Impls;

import Interfaces.IFraktion;
import Database.MongoDBHandler;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class Fraktion_MongoDB_Impl implements IFraktion<String> {

    private final MongoDBHandler dbHandler;
    private final String collectionName = "Fraktionen";
    private final Document doc;

    private String frakName;
    private List<String> frakMitglieder; // list von abgeIDs

    public Fraktion_MongoDB_Impl(MongoDBHandler dbHandler, Document document) {
        this.dbHandler = dbHandler;
        this.doc = document;

        // konstruktion mit mongoDB doc
        this.frakName = doc.getString("_id");
        this.frakMitglieder = doc.getList("mitglieder", String.class);
    }




    // alle getter und setter auf db basis, getter returnen attribute von oben (aus db)
    // und setter updaten das feld in dem doc der db
    @Override
    public String getfrakName() {
        return frakName;
    }

    @Override
    public void setfrakName(String frakName) {
        dbHandler.updateField(collectionName, "_id", this.frakName, "_id", frakName);
        doc.put("_id", frakName);
        this.frakName = frakName;
    }

    @Override
    public List<String> getfrakMitglieder() {
        return frakMitglieder;
    }

    @Override
    public void setfrakMitglieder(List<String> mitglieder) {
        doc.put("mitglieder", mitglieder);
        this.frakMitglieder = mitglieder;
    }

    @Override
    public void addfrakMitglied(String abgeordneterID) {
        if (!frakMitglieder.contains(abgeordneterID)) {
            frakMitglieder.add(abgeordneterID);
            doc.put("mitglieder", frakMitglieder);
        }
    }

    public String toHTML() {
        StringBuilder html = new StringBuilder();

        html.append("<div class='fraktion'>")
                .append("<h2>Fraktion: ").append(frakName).append("</h2>");
        return html.toString();
    }




}