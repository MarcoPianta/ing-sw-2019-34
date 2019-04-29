package Utils;

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

        String path = "." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + directory + File.separatorChar + file + ".json";
        File fileJson = new File(path);
        InputStream fis = new FileInputStream(fileJson);
        JsonReader reader = Json.createReader(fis);
        jsonValues = reader.readObject();
        reader.close();
        return jsonValues;
    }

    private JsonFileHandler(){}
}
