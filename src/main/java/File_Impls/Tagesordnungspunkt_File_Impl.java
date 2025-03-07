package File_Impls;

import Interfaces.ITagesordnungspunkt;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;


public class Tagesordnungspunkt_File_Impl implements ITagesordnungspunkt<Rede_File_Impl> {

    // id als String aus SitzungsID und TopNR (eindeutiger key)
    private String topID;

    // Referenz zu SitzungsID, hilfreich für zuweisung
    private String sitzungsID;

    // Liste für Reden im jeweiligen top
    private List<Rede_File_Impl> topReden;

    public Tagesordnungspunkt_File_Impl(String topID, String sitzungsID) {
        this.topID = topID;
        this.sitzungsID = sitzungsID;
        this.topReden = new ArrayList<>();
    }

   // to Doc methode für topDocs erstellen
   public Document toDoc() {

       Document topDoc = new Document("_id", this.topID) // ID von top wieder als primary key (eindeutig)
               .append("sitzungsID", this.sitzungsID); // Referenz zur Sitzung, wichtig für zuweisung


       List<String> redIDs = new ArrayList<>();
       for (Rede_File_Impl rede : this.topReden) {
           redIDs.add(rede.getRedID());
       }
       topDoc.append("reden", redIDs); // dann nur redID lsite anfügen


       return topDoc;
   }


    public void addRede(Rede_File_Impl redeFileImpl) {
        this.topReden.add(redeFileImpl);
    }
    public String getTopID() {
        return topID;
    }

    public void setTopID(String topID) {
        this.topID = topID;
    }
    public String getSitzungsID() {
        return sitzungsID;
    }

    public void setSitzungsID(String sitzungsID) {
        this.sitzungsID = sitzungsID;
    }
    public List<Rede_File_Impl> getTopReden() {
        return topReden;
    }

}
