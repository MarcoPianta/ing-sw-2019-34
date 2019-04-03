package Model;

public class Player {

    private int playerID;
    private String Name;
    private int actionCounter;
    private PlayerBoard playerBoard;

    public Player() {

    }
    public int getPlayerID() {
        return playerID;
    }

    public PlayerBoard getPlayerBoard() {
        return playerBoard;
    }
    public int getActionCounter() {
        return actionCounter;
    }
    //canAct is a method that return true o false if the player can do an action
    public boolean canAct(){
        if(this.getActionCounter() == 0)
            return false;
        else
            return true;

    }

}
