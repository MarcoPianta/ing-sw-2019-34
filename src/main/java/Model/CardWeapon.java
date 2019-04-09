package Model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
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
    private boolean charge;
    private ArrayList<Effect> effects;

    /**
     * The constructor read from JSON file the specs and create a weapon with that specs
     * */
    public CardWeapon(String file) throws FileNotFoundException{ //file variable contains the weapon name
        JsonObject jsonValues; /* this variable contains the JsonObject created from JSON file*/
        File fileJson = new File(getClass().getResource("/Weapon/"+file+".json").getFile());
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
        effects = new ArrayList<>();
        setEffects(jsonValues);
    }

    public String getName() {
        return name;
    }

    public int getBlueCost() {
        return blueCost;
    }

    public int getRedCost() {
        return redCost;
    }

    public int getYellowCost() {
        return yellowCost;
    }

    public AmmoColors getColor(){
        return color;
    }

    public int getEffectsNumber() {
        return effectsNumber;
    }

    /**
     * This method return the effects ArrayList of a weapon but to avoid exposing the data structure to an external
     * observer, who could potentially change the values in it, it returns a copy of it
     * */
    public List<Effect> getEffects() {
        return new ArrayList<>(effects);
    }

    public boolean isCharge() {
        return charge;
    }

    /**
     * This method read the effects of a weapon from the jsonObject and create an arrayList of Effect
     * */
    private void setEffects(JsonObject jsonValues){
        int i = 0;
        while (i < effectsNumber){
            i++;
            this.effects.add(new Effect(jsonValues, i));
        }
    }

    public void setCharge(boolean charge) {
        this.charge = charge;
    }

}
