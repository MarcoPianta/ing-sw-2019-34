package Utils;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.*;
import java.net.URL;

public final class JsonFileHandler {

    public static JsonObject openFile(String directory, String file) throws FileNotFoundException {
        JsonObject jsonValues = null; /* this variable contains the JsonObject created from JSON file*/

        URL path =  JsonFileHandler.class.getClassLoader().getResource( directory + "/" + file + ".json");
        try {
            InputStream fis = path.openStream();
            JsonReader reader = Json.createReader(fis);
            jsonValues = reader.readObject();
            reader.close();
            return jsonValues;
        }catch (IOException e){}
        return jsonValues;
    }

    private JsonFileHandler(){}
}
