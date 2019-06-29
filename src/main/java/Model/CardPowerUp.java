package Model;

import javax.json.JsonObject;
import java.io.FileNotFoundException;

public class CardPowerUp extends Card {
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
    private static final String DIRECTORY = "PowerUp";

    /**
     * The constructor read from JSON file the specs and create a power up with that specs
     * */
    public CardPowerUp(String file) throws FileNotFoundException{
        JsonObject jsonValues = Utils.JsonFileHandler.openFile(DIRECTORY, file); /* this variable contains the JsonObject created from JSON file*/
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
