package Rest;

import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinFreemarker;

import java.util.Properties;
import java.io.IOException;
import java.io.FileInputStream;




public class javalinConfig extends Properties {

    // konstruktor direkt mit einelesen der config datei aus filepath
    public javalinConfig() throws IOException{

        try (FileInputStream stream = new FileInputStream("src/main/resources/config.properties")) {
            this.load(stream);
        }

    }

    public int getPort() {

        // einfach den 7070 port aus der config holen
        return Integer.parseInt(this.getProperty("port", "7070")); // hier 7070 als default wert falls was nicht klappt

    }
}
