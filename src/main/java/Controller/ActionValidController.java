package Controller;

import Model.*;

import java.util.ArrayList;
import java.util.List;

public class ActionValidController {
    private GameHandler gameHandler;

    public ActionValidController(GameHandler gameHandler){
        this.gameHandler=gameHandler;
    }

    /**
     * This method verifies the validity of the move action
     * @param target    the Player that is target by the action
     * @param square    the NormalSquare selected by the actor as the final NormalSquare for the target
     * @param maxMove   the max number of pass that the actorPlayer can do
     * @return true if the action is valid
     */
    public boolean actionValid(Player target, NormalSquare square, int maxMove){
        boolean valueReturn;
        if(new Move(target,square,maxMove).isValid()){
            valueReturn=true;
            if(target.getPlayerID().equals(gameHandler.getPlayerValid().getPlayerID()))
                new Move(gameHandler.getPlayerValid(),square,maxMove).execute();
        }
        else
            valueReturn=false;
        return valueReturn;
    }

    /**
     * this method verifies the validity of the shoot action with targets
     * @param targets  the list of targets who received damage or mark
     * @param effect   the effect uses for the shoot
     * @param powerUp  the possible powerUp sight
     * @return true if the action is valid
     */
    public boolean actionValid(List<Player> targets, Effect effect, int powerUp){
        boolean valueReturn;
        if(powerUp==-1){
            if(gameHandler.getGame().getCurrentPlayer().isValidCost(effect.getBonusCost(),false))
                valueReturn=new Shoot(effect, gameHandler.getPlayerValid(), targets, null, false).isValid();
            else
                valueReturn=false;
        }
        else{
            if(gameHandler.getGame().getCurrentPlayer().isValidCost(effect.getBonusCost(),true)&&gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerPowerUps().get(powerUp).getWhen().equals("get"))
                valueReturn=new Shoot(effect, gameHandler.getPlayerValid(), targets, null, false).isValid();
            else
                valueReturn=false;
        }
        return valueReturn;
    }

    /**
     * this method verifies the validity of the shoot action with square
     * @param square   square where is the targets who received damage or mark
     * @param effect   the effect uses for the shoot
     * @param powerUp  the possible powerUp sight
     * @return true if the action is valid
     */
    public boolean actionValid(ArrayList<NormalSquare> square,Effect effect,int powerUp){
        boolean valueReturn;
        if(powerUp==-1){
            if(gameHandler.getGame().getCurrentPlayer().isValidCost(effect.getBonusCost(),false))
                valueReturn=new Shoot(effect, gameHandler.getPlayerValid(), null, square, true).isValid();
            else
                valueReturn=false;
        }
        else{
            if(gameHandler.getGame().getCurrentPlayer().isValidCost(effect.getBonusCost(),true)&&gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerPowerUps().get(powerUp).getWhen().equals("get"))
                valueReturn=new Shoot(effect, gameHandler.getPlayerValid(), null, square, true).isValid();
            else
                valueReturn=false;
        }
        return valueReturn;
    }

    /**
     * this method verifies the validity of the shoot action with room
     * @param room     room where is the targets who received damage or mark
     * @param effect   the effect uses for the shoot
     * @param powerUp  the possible powerUp sight, -1 if is not present
     * @return true if the action is valid
     */
    public boolean actionValid(Room room,Effect effect,int powerUp){
        boolean valueReturn;
        if(powerUp==-1){
            if(gameHandler.getGame().getCurrentPlayer().isValidCost(effect.getBonusCost(),false))
                valueReturn=new Shoot(effect,gameHandler.getPlayerValid(),room.getColor()).isValid();
            else
                valueReturn=false;
        }
        else{
            if(gameHandler.getGame().getCurrentPlayer().isValidCost(effect.getBonusCost(),true) &&gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerPowerUps().get(powerUp).getWhen().equals("get"))
                valueReturn=new Shoot(effect,gameHandler.getPlayerValid(),room.getColor()).isValid();
            else
                valueReturn=false;
        }
        return valueReturn;
    }

    /**
     * this method verified the validity of the grab weapon action
     * @param square the square where grab the action
     * @param i      the position of the weapon
     * @return true if the action is valid
     */
    public boolean actionValid(SpawnSquare square, int i){
        ArrayList<Integer> cost=new ArrayList<>();
        cost.add(square.getWeapons().get(i).getBlueCost());
        cost.add(square.getWeapons().get(i).getYellowCost());
        cost.add(square.getWeapons().get(i).getRedCost());
        return gameHandler.getGame().getCurrentPlayer().isValidCost(cost,false);
    }

    /**
     * this method verified the validity of the reload action
     * @param i   the position of the weapon that must be recharged
     * @return true if the action is valid
     */
    public boolean actionValid(int i){
        ArrayList<Integer> cost=new ArrayList<>();
        cost.add(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(i).getBlueCost());
        cost.add(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(i).getYellowCost());
        cost.add(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(i).getRedCost());
        return gameHandler.getGame().getCurrentPlayer().isValidCost(cost,false);
    }
}
