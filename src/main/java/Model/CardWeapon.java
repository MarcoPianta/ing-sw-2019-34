package Model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class CardWeapon implements Card {
    private String name;
    private AmmoColors color;
    private int redCost;
    private int blueCost;
    private int yellowCost;
    private int effectsNumber;
    private JsonObject jsonValues; /**this variable contains the JsonObject created from JSON file*/

    //The constructor read from JSON file the specs and create a weapon with that specs
    public CardWeapon(String file) throws FileNotFoundException{ //file variable contains the weapon name
        File fileJson = new File(getClass().getResource("/Weapon/"+file).getFile());
        InputStream fis = new FileInputStream(fileJson);
        JsonReader reader = Json.createReader(fis);
        jsonValues = reader.readObject();
        reader.close();
        name = jsonValues.getString("name");
        color = AmmoColors.valueOf(jsonValues.getString("color"));
        redCost = jsonValues.getJsonArray("cost").getInt(0);
        yellowCost = jsonValues.getJsonArray("cost").getInt(1);
        blueCost = jsonValues.getJsonArray("cost").getInt(2);
        effectsNumber = jsonValues.getInt("effectNumber");
    }

    public String getName() {
        return name;
    }
    public AmmoColors getColor(){
        return color;
    }
    public int getEffectsNumber() {
        return effectsNumber;
    }
    public JsonObject getJsonValues() {
        return jsonValues;
    }
    @Override
    public Enum[] getEnumeration(){
        return WeaponDictionary.values();
    }
}
