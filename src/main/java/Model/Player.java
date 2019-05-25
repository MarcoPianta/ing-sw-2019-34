package Model;

import Controller.StateMachineEnumerationTurn;

import java.util.ArrayList;

public class Player {

    private Integer playerID;
    private Colors color;
    private PlayerBoard playerBoard;
    private NormalSquare position;
    private Game gameId;
    private StateMachineEnumerationTurn state;

    public Player(Integer playerID, Colors color) {
        this.playerID=playerID;
        this.color=color;
        this.playerBoard=new PlayerBoard( this);
        this.position=null;
        this.gameId=null;
        this.state= StateMachineEnumerationTurn.WAIT;
    }

    public Integer getPlayerID() {
        return playerID;
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
     *this method is a set position
     * */
    public void newPosition(NormalSquare newPosition){
        position=newPosition;
    }
    /*
     * this method is used when a player is dead and must spawn
     * */
    public void spawn(int counter){
        CardPowerUp newPowerUp;
        while(counter!=0){
            newPowerUp=getGameId().getDeckCollector().getCardPowerUpDrawer().draw();
            getPlayerBoard().getHandPlayer().addPowerUp(newPowerUp);
        }
    }

    /*
     *this method is called by spawn and calculate the new position
     * */
    public void calculateNewPosition(CardPowerUp powerUp){
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
        newPosition(getGameId().getMap().getRooms().get(i-1).getNormalSquares().get(j));
        getPlayerBoard().getHandPlayer().removePowerUp(getPlayerBoard().getHandPlayer().getPlayerPowerUps().indexOf(powerUp));
    }
}

