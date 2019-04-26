package Model;

import java.util.ArrayList;

public class Player {

    private String playerID;
    private String name;
    private Colors color;
    private int actionCounter;
    private PlayerBoard playerBoard;
    private NormalSquare position;
    private Game gameId;

    public Player(String playerID,Game gameId, Colors color,String name) {
        this.playerID=playerID;
        this.name=name;
        this.color=color;
        this.playerBoard=new PlayerBoard(color , name, this);
        this.actionCounter=2;
        this.position=null;
        this.gameId=gameId;
    }

    public Player(){}

    public PlayerBoard getPlayerBoard() {
        return playerBoard;
    }

    public NormalSquare getPosition() {
        return position;
    }

    public int getActionCounter() {
        return actionCounter;
    }

    public Game getGameId() {
        return gameId;
    }

    /*
     *this method  return true o false if the player can do an action
     * */
    public boolean canAct(){
        if(this.getActionCounter() == 0)
            return false;
        else
            return true;
    }

    /*
     * this method increment action counter
     * */
    public void decrementActionCounter(){
        actionCounter-=1;
        if (actionCounter==0) {//TODO called controller for change game(currentPlayer)
        }
    }

    /*
     *this method modified a player's position
     * */
    public void newPosition(NormalSquare newPosition){
        position=newPosition;

    }

}
