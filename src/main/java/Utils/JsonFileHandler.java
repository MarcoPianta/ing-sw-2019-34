package Utils;

import Model.Card;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public final class JsonFileHandler {

    public static JsonObject openFile(String directory, String file) throws FileNotFoundException {
        JsonObject jsonValues; /* this variable contains the JsonObject created from JSON file*/
        File fileJson = new File(Card.class.getResource('/'+directory+'/' + file + ".json").getFile());
        InputStream fis = new FileInputStream(fileJson);
        JsonReader reader = Json.createReader(fis);
        jsonValues = reader.readObject();
        reader.close();
        return jsonValues;
    }
}
