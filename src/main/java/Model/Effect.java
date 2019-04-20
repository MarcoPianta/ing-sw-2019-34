package Model;

import javax.json.JsonObject;

/**
 * This Class contains the possible effect of a CardWeapon
 * */
public class Effect {
    private int targetNumber;
    private boolean allTarget;
    private int damage;
    private int mark;
    private int myMove;
    private int otherMove;
    private int iteration;
    private PreCondition preCondition;
    private PostCondition postCondition;

    public Effect(JsonObject jsonValues, int effect){
        String jsonString = "Effect" + effect;
        targetNumber = jsonValues.getJsonArray(jsonString).getJsonObject(0).getInt("target");
        allTarget = jsonValues.getJsonArray(jsonString).getJsonObject(0).getBoolean("allTarget");
        damage = jsonValues.getJsonArray(jsonString).getJsonObject(0).getInt("damage");
        mark = jsonValues.getJsonArray(jsonString).getJsonObject(0).getInt("mark");
        myMove = jsonValues.getJsonArray(jsonString).getJsonObject(0).getInt("myMove");
        otherMove = jsonValues.getJsonArray(jsonString).getJsonObject(0).getInt("otherMove");
        iteration = jsonValues.getJsonArray(jsonString).getJsonObject(0).getInt("iteration");
        preCondition = new PreCondition(jsonValues.getJsonArray(jsonString).getJsonObject(0).getJsonArray("preCondition").getJsonObject(0));
        postCondition = new PostCondition(jsonValues.getJsonArray(jsonString).getJsonObject(0).getJsonArray("postCondition").getJsonObject(0));
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
        private boolean enemiesDifferentSquare;
        private int minRange;
        private int maxRange;
        private boolean cardinal;

        public PreCondition(JsonObject jsonValues){
            vision = jsonValues.getBoolean("vision");
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
            this.enemiesDifferentSquare = preCondition.isEnemiesDifferentSquare();
            this.minRange = preCondition.getMinRange();
            this.maxRange = preCondition.getMaxRange();
            this.cardinal = preCondition.isCardinal();
        }

        public boolean isVision() {
            return vision;
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
