package File_Impls;

import Interfaces.IFraktion;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class Fraktion_File_Impl implements IFraktion<Abgeordneter_File_Impl> {
    private String frakName;
    private List<Abgeordneter_File_Impl> frakMitglieder;


    // normaler constructor (public)
    public Fraktion_File_Impl(String frakName) {
        this.frakName = frakName;
        this.frakMitglieder = new ArrayList<>();
    }


    public Document toDoc() {

        // hier fraktionsname als key, weil ist auch eindeutig, also key = _id = frakName
        Document frakDoc = new Document("_id", this.frakName);

        // mitglieder loopen und dann nur IDs holen (weil mitglieder sind abgeorndete file impls
        List<String> mitgliederIDs = new ArrayList<>();
        for (Abgeordneter_File_Impl mitglied : this.frakMitglieder) {
            // bei gastrednern/Fraktionslos einfach statt id gastredner nehmen
            if (mitglied == null){
                mitgliederIDs.add("Gastredner");
            }
            else {
                mitgliederIDs.add(mitglied.getAbgeordnetenID());
            }
        }
        frakDoc.append("mitglieder", mitgliederIDs); // liste der ids dann appenden noch
        return frakDoc;
    }




    public String getfrakName() {
        return frakName;
    }

    public void setfrakName(String frakName) {
        this.frakName = frakName;
    }


    public void setfrakMitglieder(List<Abgeordneter_File_Impl> mitglieder) {
        this.frakMitglieder = mitglieder;
    }
    public List<Abgeordneter_File_Impl> getfrakMitglieder() {
        return frakMitglieder;
    }
    public void addfrakMitglied(Abgeordneter_File_Impl abgeordneterFileImpl) {
        frakMitglieder.add(abgeordneterFileImpl);
    }
}
