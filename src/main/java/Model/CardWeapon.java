package Model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class CardWeapon implements Card {
    private String name;
    private Colors color;
    private int redCost;
    private int blueCost;
    private int yellowCost;
    private int effectsNumber;
    private JsonObject jsonValues; /**this variable contains the JsonObject created from JSON file*/

    //The constructor read from JSON file the specs and create a weapon with that specs
    public CardWeapon(String file) throws FileNotFoundException{ //file variable contains the weapon name
        InputStream fis = new FileInputStream("C:/Users/Mark2/Desktop/Marco/Universita/Progetto_ingegneria_del_software/ing-sw-2019-34/src/main/resources/Weapon/"+file);
        JsonReader reader = Json.createReader(fis);
        jsonValues = reader.readObject();
        reader.close();
        name = jsonValues.getString("name");
        color = Colors.valueOf(jsonValues.getString("color"));
        redCost = jsonValues.getJsonArray("cost").getInt(0);
        yellowCost = jsonValues.getJsonArray("cost").getInt(1);
        blueCost = jsonValues.getJsonArray("cost").getInt(2);
        effectsNumber = jsonValues.getInt("effectNumber");
    }

    public String getName() {
        return name;
    }
    public Colors getColor(){
        return color;
    }
    public int getEffectsNumber() {
        return effectsNumber;
    }

}
