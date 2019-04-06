package Model;

import javax.json.JsonObject;

public class Effect {
    private int targetNumber;
    private boolean allTarget;
    private int damage;
    private int mark;
    private int myMove;
    private int otherMove;
    private int iteration;

    public Effect(JsonObject jsonValues, int effect){
        //int jsonEffect = effect + 1;
        String jsonString = "Effect" + effect;
        targetNumber = jsonValues.getJsonArray(jsonString).getJsonObject(0).getInt("target");
        allTarget = jsonValues.getJsonArray(jsonString).getJsonObject(0).getBoolean("allTarget");
        damage = jsonValues.getJsonArray(jsonString).getJsonObject(0).getInt("damage");
        mark = jsonValues.getJsonArray(jsonString).getJsonObject(0).getInt("mark");
        myMove = jsonValues.getJsonArray(jsonString).getJsonObject(0).getInt("myMove");
        otherMove = jsonValues.getJsonArray(jsonString).getJsonObject(0).getInt("otherMove");
        iteration = jsonValues.getJsonArray(jsonString).getJsonObject(0).getInt("iteration");
    }

    public int getTargetNumber() {
        return targetNumber;
    }

    public boolean isAllTarget() {
        return allTarget;
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

    public int getIteration() {
        return iteration;
    }
}
