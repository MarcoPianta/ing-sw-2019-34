package Model;

import javax.json.JsonArray;
import javax.json.JsonObject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This Class contains the possible effect of a CardWeapon
 * */
public class Effect implements Serializable {
    Integer[] bonusCost = {0, 0, 0};
    private int targetNumber;
    private int squareNumber;
    private String actionSequence;
    private ArrayList<Integer> pDamage;
    private ArrayList<Integer> pMark;
    private ArrayList<Integer> sDamage;
    private ArrayList<Integer> sMark;
    private int rDamage;
    private int rMark;
    private int myMove;
    private int targetMove;
    private PreCondition preCondition;

    public Effect(JsonObject jsonValues, int effect){
        String jsonString = "Effect" + effect;
        JsonArray cost = jsonValues.getJsonArray(jsonString).getJsonObject(0).getJsonArray("bonusCost");
        JsonArray pDamageArray;
        JsonArray pMarkArray;
        JsonArray sDamageArray;
        JsonArray sMarkArray;
        actionSequence = jsonValues.getJsonArray(jsonString).getJsonObject(0).getString("actionSequence");
        for (int i = 0; i < cost.size(); i++)
            this.bonusCost[i] = cost.getInt(i);
        targetNumber = jsonValues.getJsonArray(jsonString).getJsonObject(0).getInt("targetNumber");
        squareNumber = jsonValues.getJsonArray(jsonString).getJsonObject(0).getInt("squareNumber");
        pDamageArray = jsonValues.getJsonArray(jsonString).getJsonObject(0).getJsonArray("pDamage");
        pMarkArray = jsonValues.getJsonArray(jsonString).getJsonObject(0).getJsonArray("pMark");
        pDamage = new ArrayList<>();
        while (pDamage.size() < pDamageArray.size())
            pDamage.add(pDamageArray.getInt(pDamage.size()));
        pMark = new ArrayList<>();
        while (pMark.size() < pMarkArray.size())
            pMark.add(pDamageArray.getInt(pMark.size()));
        sDamageArray = jsonValues.getJsonArray(jsonString).getJsonObject(0).getJsonArray("sDamage");
        sMarkArray = jsonValues.getJsonArray(jsonString).getJsonObject(0).getJsonArray("sMark");
        sDamage = new ArrayList<>();
        while (sDamage.size() < sDamageArray.size())
            sDamage.add(sDamageArray.getInt(sDamage.size()));
        sMark = new ArrayList<>();
        while (sMark.size() < sMarkArray.size())
            sMark.add(sDamageArray.getInt(sMark.size()));
        rDamage = jsonValues.getJsonArray(jsonString).getJsonObject(0).getInt("rDamage");
        rMark = jsonValues.getJsonArray(jsonString).getJsonObject(0).getInt("rMark");
        myMove = jsonValues.getJsonArray(jsonString).getJsonObject(0).getInt("myMove");
        targetMove = jsonValues.getJsonArray(jsonString).getJsonObject(0).getInt("targetMove");
        preCondition = new PreCondition(jsonValues.getJsonArray(jsonString).getJsonObject(0).getJsonArray("preCondition").getJsonObject(0));
    }

    public Integer[] getBonusCost(){
        return bonusCost;
    }

    public int getTargetNumber() {
        return targetNumber;
    }

    public int getSquareNumber() {
        return squareNumber;
    }

    public String getActionSequence(){
        return actionSequence;
    }

    public List<Integer> getpDamage() {
        return pDamage;
    }

    public List<Integer> getpMark() {
        return pMark;
    }

    public List<Integer> getsDamage() {
        return sDamage;
    }

    public List<Integer> getsMark() {
        return sMark;
    }

    public int getrDamage() {
        return rDamage;
    }

    public int getrMark() {
        return rMark;
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


    /**
     * This inner class is used to contains the pre condition read from Weapon json file
     * */
    class PreCondition{
        private boolean vision;
        private boolean blind;
        private boolean melee;
        private boolean enemiesDifferentSquare;
        private int minRange;
        private int maxRange;
        private boolean cardinal;
        private boolean sameSquare;

        public PreCondition(JsonObject jsonValues){
            vision = jsonValues.getBoolean("vision");
            blind = jsonValues.getBoolean("blind");
            melee = jsonValues.getBoolean("melee");
            enemiesDifferentSquare = jsonValues.getBoolean("enemiesDifferentSquare");
            minRange = jsonValues.getInt("minRange");
            maxRange = jsonValues.getInt("maxRange");
            cardinal = jsonValues.getBoolean("cardinal");
            sameSquare = jsonValues.getBoolean("sameSquare");
        }

        /**
         * This constructor can be used to create copy of a PreCondition object
         * */
        public PreCondition(PreCondition preCondition){
            this.vision = preCondition.isVision();
            this.blind = preCondition.isBlind();
            this.melee = preCondition.isMelee();
            this.enemiesDifferentSquare = preCondition.isEnemiesDifferentSquare();
            this.minRange = preCondition.getMinRange();
            this.maxRange = preCondition.getMaxRange();
            this.cardinal = preCondition.isCardinal();
            this.sameSquare = preCondition.isSameSquare();
        }

        public boolean isVision() {
            return vision;
        }

        public boolean isBlind() {
            return blind;
        }

        public boolean isMelee() {
            return melee;
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

        public boolean isSameSquare() { return sameSquare;}
    }
}
