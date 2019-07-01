package Model;

import Controller.StateMachineEnumerationTurn;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Player implements Serializable {

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
            counter--;
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

    public Integer[] calculateMaxAmmo(boolean set){
        Integer[] ammoRyb=new Integer[3];
        int i=0;
        while(i<3){
            ammoRyb[i]=getPlayerBoard().getHandPlayer().getAmmoRYB()[i];
            i++;
        }
        i=0;
        while(i<getPlayerBoard().getHandPlayer().getPlayerPowerUps().size()){
            if(!set){
                if(getPlayerBoard().getHandPlayer().getPlayerPowerUps().get(i).getColor().getAbbreviation().equals(Colors.RED.getAbbreviation())){
                    ammoRyb[0]++;
                    i++;
                }
                else if(getPlayerBoard().getHandPlayer().getPlayerPowerUps().get(i).getColor().getAbbreviation().equals(Colors.YELLOW.getAbbreviation())){
                    ammoRyb[1]++;
                    i++;
                }
                else{
                    ammoRyb[2]++;
                    i++;
                }
            }
            else{
                if(!getPlayerBoard().getHandPlayer().getPlayerPowerUps().get(i).getWhen().equals("get") &&getPlayerBoard().getHandPlayer().getPlayerPowerUps().get(i).getColor().getAbbreviation().equals(Colors.RED.getAbbreviation())){
                    ammoRyb[0]++;
                    i++;
                }
                else if(!getPlayerBoard().getHandPlayer().getPlayerPowerUps().get(i).getWhen().equals("get") &&getPlayerBoard().getHandPlayer().getPlayerPowerUps().get(i).getColor().getAbbreviation().equals(Colors.YELLOW.getAbbreviation())){
                    ammoRyb[1]++;
                    i++;
                }
                else if(!getPlayerBoard().getHandPlayer().getPlayerPowerUps().get(i).getWhen().equals("get")  &&getPlayerBoard().getHandPlayer().getPlayerPowerUps().get(i).getColor().getAbbreviation().equals(Colors.BLUE.getAbbreviation())){
                    ammoRyb[2]++;
                    i++;
                }
                else{//player had scope and use it
                    set=false;
                    i++;
                }
            }

        }

        return ammoRyb;
    }


    public boolean isValidCost(Integer[] cost, boolean set){
        boolean valueReturn=true;
        if(!set) {
            Integer[] ammoRyb = calculateMaxAmmo(false);
            for (int i = 0; i < 3; i++) {
                if (ammoRyb[i] < cost[i])
                    valueReturn = false;
            }
        }
        else{
            Integer[] ammoRyb=calculateMaxAmmo(true);
            for(int i=0;i<3;i++){
                if(ammoRyb[i]< cost[i])
                    valueReturn=false;
            }
            if(valueReturn && (ammoRyb[0]+ammoRyb[1]+ammoRyb[2] - (cost[0]+cost[1]+cost[2])==0))
                valueReturn=false;
        }
        return valueReturn;
    }
    public boolean isValidCost(int i){
        boolean valueReturn=false;
        if(getPlayerBoard().getHandPlayer().getAmmoRYB()[0]>=i|| getPlayerBoard().getHandPlayer().getAmmoRYB()[1]>=i||getPlayerBoard().getHandPlayer().getAmmoRYB()[2]>=i)
            valueReturn=true;

        return  valueReturn;
    }

}

