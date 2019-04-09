package Model;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class CardPowerUp implements Card {
    private String name;
    private int cost;
    private AmmoColors color;
    private int damage;
    private int mark;
    private int myMove;
    private int otherMove;
    private int target;
    private boolean vision;
    private String when;

    /**
     * The constructor read from JSON file the specs and create a power up with that specs
     * */
    public CardPowerUp(String file) throws FileNotFoundException{
        JsonObject jsonValues; /* this variable contains the JsonObject created from JSON file*/
        File fileJson = new File(getClass().getResource("/PowerUp/"+file+".json").getFile());
        InputStream fis = new FileInputStream(fileJson);
        JsonReader reader = Json.createReader(fis);
        jsonValues = reader.readObject();
        reader.close();
        name = jsonValues.getString("name");
        cost = jsonValues.getInt("cost");
        color = AmmoColors.valueOf(jsonValues.getString("color"));
        damage = jsonValues.getInt("damage");
        mark = jsonValues.getInt("mark");
        myMove = jsonValues.getInt("myMove");
        otherMove = jsonValues.getInt("otherMove");
        target = jsonValues.getInt("target");
        vision = jsonValues.getBoolean("vision");
        when = jsonValues.getString("when");
    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public AmmoColors getColor() {
        return color;
    }

    public int getDamage() {
        return damage;
    }

    public int getMark() {
        return mark;
    }

    public int getMyMove() {
        return myMove;
    }

    public int getOtherMove() {
        return otherMove;
    }

    public int getTarget() {
        return target;
    }

    public String getWhen() {
        return when;
    }

    public boolean getVision() {
        return vision;
    }
}
