package Parser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Paths;

public class bildParser {

    private final String filepath;

    /**
     * Konstruktor für den bildParser
     * @param filepath path zur JSON mit den bildern
     */
    public bildParser(String filepath) {
        this.filepath = filepath;
    }

    /**
     * Holt die Bild-URL für die gegebene RednerID aus der JSON
     * @param rednerID halt die id des redners
     * @return Die url oder null wenns kein bild gibt
     */
    public String getBildURL(String rednerID) {
        try {
            // json content als string auslesen aus datei
            String jsonContent = Files.readString(Paths.get(filepath));

            // dann json array aus dem content bauen
            JSONArray jsonArray = new JSONArray(jsonContent);

            // durch das array iterieren mit standard loop
            for (int i = 0; i < jsonArray.length(); i++) {

                // hier das jeweilige objekt aus der liste (array holen mit index)
                JSONObject object = jsonArray.getJSONObject(i);

                // gucken, ob das object die redner ID beinhaltet
                if (object.has(rednerID)) {
                    // dann bilderliste bauen mit redner id
                    JSONArray bilderArray = object.getJSONArray(rednerID);

                    if (!bilderArray.isEmpty()) {
                        // hier auf 0tes element zugreifen vom bildern (einfach das erste nehmen, da sind ja manchmal mehrere)
                        JSONObject bildObject = bilderArray.getJSONObject(0);
                        return bildObject.getString("hp_picture"); // hp bild für schnelleres laden
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Fehler beim parsen der json: " + e.getMessage());
        }
        return "https://global.discourse-cdn.com/spiceworks/original/4X/e/f/0/ef05a2121b0835756a2b20ff15e7bc6b57258081.jpeg"; // wenn kein bild da dann einfach standard bild (fehler)
    }
}