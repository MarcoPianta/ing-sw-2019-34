package Model;

import Controller.StateMachineEnumerationTurn;

import java.util.ArrayList;

public class Player {

    private String playerID;
    private String name;
    private Colors color;
    private int actionCounter;
    private PlayerBoard playerBoard;
    private NormalSquare position;
    private Game gameId;
    private StateMachineEnumerationTurn state;

    public Player(String playerID, Colors color, String name) {
        this.playerID=playerID;
        this.name=name;
        this.color=color;
        this.playerBoard=new PlayerBoard( this);
        this.actionCounter=2;
        this.position=null;
        this.gameId=gameId;
        this.state= StateMachineEnumerationTurn.WAIT;
    }

    public void setGameId(Game gameId) {
        this.gameId = gameId;
    }

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

    public void setState(StateMachineEnumerationTurn state) {
        this.state = state;
    }
    public StateMachineEnumerationTurn getState() {
        return state;
    }

    public Colors getColor() {
        return color;
    }

    /*
     *this method  return true o false if the player can do an action
     * */
    public boolean canAct(){
        return this.getActionCounter() != 0;
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
     *this method is a set position
     * */
    public void newPosition(NormalSquare newPosition){
        position=newPosition;
    }
    /*
     * this method is used when a player is dead and must spawn
     * */
    public void spawn(){
        CardPowerUp newPowerUp;
        newPowerUp=getGameId().getDeckCollector().getCardPowerUpDrawer().draw();
        getPlayerBoard().getHandPlayer().addPowerUp(newPowerUp);
        newPosition(calculateNewPosition(newPowerUp));
    }

    /*
     *this method is called by spawn and calculate the new position
     * */
    public NormalSquare calculateNewPosition(CardPowerUp powerUp){
        boolean isNotFind=true;
        int i=0;
        int j=0;
        while((i<getGameId().getMap().getRooms().size()) &&(isNotFind)){
            j=0;
            while((j<getGameId().getMap().getRooms().get(i).getNormalSquares().size())&&(isNotFind)){
                 //assert(getGameId().getMap().getRooms().get(i).getNormalSquares().get(j).getColor()!=null);
                if(getGameId().getMap().getRooms().get(i).getNormalSquares().get(j).isSpawn()&&
                        (getGameId().getMap().getRooms().get(i).getColor().getAbbreviation().equals(powerUp.getColor().getAbbreviation())))
                    isNotFind=false;
                else
                    j++;
            }
            i++;
        }
        return getGameId().getMap().getRooms().get(i-1).getNormalSquares().get(j);
    }
}

