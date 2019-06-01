package Model;

import javax.json.JsonArray;
import javax.json.JsonObject;
import java.util.ArrayList;
import java.util.List;

/**
 * This Class contains the possible effect of a CardWeapon
 * */
public class Effect {
    ArrayList<Integer> bonusCost = new ArrayList<>();
    private int targetNumber;
    private String actionSequence;
    private ArrayList<Integer> damage;
    private ArrayList<Integer> mark;
    private int myMove;
    private int targetMove;
     private PreCondition preCondition;
    private PostCondition postCondition;

    public Effect(JsonObject jsonValues, int effect){
        String jsonString = "Effect" + effect;
        JsonArray cost = jsonValues.getJsonArray(jsonString).getJsonObject(0).getJsonArray("bonusCost");
        JsonArray damageArray;
        JsonArray markArray;
        String actionSequence;
        for (int i = 0; i < cost.size(); i++)
            this.bonusCost.add(cost.getInt(i));
        targetNumber = jsonValues.getJsonArray(jsonString).getJsonObject(0).getInt("targetNumber");
        actionSequence = jsonValues.getJsonArray(jsonString).getJsonObject(0).getString("actionSequence");
        damageArray = jsonValues.getJsonArray(jsonString).getJsonObject(0).getJsonArray("damage");
        markArray = jsonValues.getJsonArray(jsonString).getJsonObject(0).getJsonArray("mark");
        damage = new ArrayList<>();
        while (damage.size() < damageArray.size())
            damage.add(damageArray.getInt(damage.size()));
        mark = new ArrayList<>();
        while (mark.size() < markArray.size())
            mark.add(damageArray.getInt(mark.size()));
        myMove = jsonValues.getJsonArray(jsonString).getJsonObject(0).getInt("myMove");
        targetMove = jsonValues.getJsonArray(jsonString).getJsonObject(0).getInt("targetMove");
        preCondition = new PreCondition(jsonValues.getJsonArray(jsonString).getJsonObject(0).getJsonArray("preCondition").getJsonObject(0));
        postCondition = new PostCondition(jsonValues.getJsonArray(jsonString).getJsonObject(0).getJsonArray("postCondition").getJsonObject(0));
    }

    public ArrayList<Integer> getBonusCost(){
        return bonusCost;
    }

    public int getTargetNumber() {
        return targetNumber;
    }

    public String getActionSequence(){
        return actionSequence;
    }

    public List<Integer> getDamage() {
        return damage;
    }

    public List<Integer> getMark() {
        return mark;
    }

    public int getMyMove() {
        return myMove;
    }

    public int getTargetMove() {
        return targetMove;
    }

    public PreCondition getPreCondition() {
        return new PreCondition(preCondition);
    }

    public PostCondition getPostCondition() {
        return new PostCondition(postCondition);
    }

    /**
     * This inner class is used to contains the pre condition read from Weapon json file
     * */
    class PreCondition{
        private boolean vision;
        private boolean blind;
        private boolean enemiesDifferentSquare;
        private int minRange;
        private int maxRange;
        private boolean cardinal;

        public PreCondition(JsonObject jsonValues){
            vision = jsonValues.getBoolean("vision");
            blind = jsonValues.getBoolean("blind");
            enemiesDifferentSquare = jsonValues.getBoolean("enemiesDifferentSquare");
            minRange = jsonValues.getInt("minRange");
            maxRange = jsonValues.getInt("maxRange");
            cardinal = jsonValues.getBoolean("cardinal");
        }

        /**
         * This constructor can be used to create copy of a PreCondition object
         * */
        public PreCondition(PreCondition preCondition){
            this.vision = preCondition.isVision();
            this.blind = preCondition.isBlind();
            this.enemiesDifferentSquare = preCondition.isEnemiesDifferentSquare();
            this.minRange = preCondition.getMinRange();
            this.maxRange = preCondition.getMaxRange();
            this.cardinal = preCondition.isCardinal();
        }

        public boolean isVision() {
            return vision;
        }

        public boolean isBlind() {
            return blind;
        }

        public boolean isCardinal() {
            return cardinal;
        }

        public boolean isEnemiesDifferentSquare() {
            return enemiesDifferentSquare;
        }

        public int getMaxRange() {
            return maxRange;
        }

        public int getMinRange() {
            return minRange;
        }
    }

    /**
     * This inner class is used to contains the post condition read from Weapon json file
     * */
    class PostCondition{
        private int targetMove;

        public PostCondition(JsonObject jsonValues){
            targetMove = jsonValues.getInt("targetMove");
        }

        /**
         * This constructor can be used to create copy of a PostCondition object
         * */
        public PostCondition(PostCondition postCondition){
            this.targetMove = postCondition.getTargetMove();
        }

        public int getTargetMove() {
            return targetMove;
        }
    }
}
