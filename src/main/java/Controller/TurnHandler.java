package Controller;

import Model.*;
import network.messages.*;

import java.util.ArrayList;
import java.util.List;

public class TurnHandler {
    private StateMachineEnumerationTurn nextState;
    private EndTurnChecks endTurnChecks;
    private GameHandler gameHandler;

    public TurnHandler(GameHandler gameHandler){
        this.gameHandler=gameHandler;
        this.endTurnChecks=new EndTurnChecks();
        this.nextState=null;
    }

    public GameHandler getGameHandler() {
        return gameHandler;
    }

    public void setNextState(StateMachineEnumerationTurn nextState) {
        this.nextState = nextState;
    }

    public StateMachineEnumerationTurn getNextState() {
        return nextState;
    }

    public void start(){
        //TODO messaggio che inizia turno
        setNextState(StateMachineEnumerationTurn.ACTION1);
        for(Player p:getGameHandler().getGame().getPlayers()){
            if(p.getPosition()==null && p.getPlayerBoard().getMaxReward()!=8)//if player is dead
                p.spawn();
            else if(p.getPosition()==null && p==getGameHandler().getGame().getCurrentPlayer())//if is the first turn of player
            {
                p.spawn();
            }
        }
    }
    public boolean actionState(Message message){
        boolean valueReturn=false;
        if (gameHandler.getGame().getCurrentPlayer().getState()==StateMachineEnumerationTurn.START){
            gameHandler.getGame().getCurrentPlayer().setState(StateMachineEnumerationTurn.ACTION1);
            setNextState(StateMachineEnumerationTurn.ACTION2);
        }
        else{
            gameHandler.getGame().getCurrentPlayer().setState(StateMachineEnumerationTurn.ACTION2);
            setNextState(StateMachineEnumerationTurn.RELOAD);
        }
        if(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHealthPlayer().getAdrenalineAction()==1)
            valueReturn=actionAdrenaline1(message);
        else if (gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHealthPlayer().getAdrenalineAction()==2)
            valueReturn=actionAdrenaline2(message);
        else
            valueReturn=actionAdrenaline0(message);
        return  valueReturn;
    }
    private boolean actionAdrenaline0(Message message){
        boolean valueReturn=false;
        if(message.getActionType()==ActionType.MOVE){
            MoveMessage newMessage=(MoveMessage)message;
            valueReturn= new Move(gameHandler.getGame().getCurrentPlayer(),newMessage.getNewSquare(), 3).execute();}
        else if(message.getActionType()==ActionType.SHOT){
            Shot newMessage=(Shot)message;
            valueReturn= new Injure(gameHandler.getGame().getCurrentPlayer(), (ArrayList<Player>) newMessage.getTargets(),newMessage.getEffect()).execute();
        }
        else
            valueReturn=actionGrab(message,1);
        return valueReturn;
    }

    private boolean actionAdrenaline1(Message message){
        boolean valueReturn=false;
        if(message.getActionType()==ActionType.MOVE){
            MoveMessage newMessage=(MoveMessage)message;
            valueReturn= new Move(gameHandler.getGame().getCurrentPlayer(),newMessage.getNewSquare(), 3).execute();}
        else if(message.getActionType()==ActionType.SHOT){
            Shot newMessage=(Shot)message;
            valueReturn= new Injure(gameHandler.getGame().getCurrentPlayer(), (ArrayList<Player>) newMessage.getTargets(),newMessage.getEffect()).execute();
        }
        else
            valueReturn=actionGrab(message,2);
        return valueReturn;
    }

    private boolean actionAdrenaline2(Message message){
        boolean valueReturn=false;
        if(message.getActionType()==ActionType.MOVE){
            MoveMessage newMessage=(MoveMessage)message;
            valueReturn= new Move(gameHandler.getGame().getCurrentPlayer(),newMessage.getNewSquare(), 3).execute();}
        else if(message.getActionType()==ActionType.SHOT){
            Shot newMessage=(Shot)message;
            if(newMessage.getMaxMove()==1)
                new Move(gameHandler.getGame().getCurrentPlayer(),newMessage.getNewSquare(), 1).execute();
            valueReturn= new Injure(gameHandler.getGame().getCurrentPlayer(), (ArrayList<Player>) newMessage.getTargets(),newMessage.getEffect()).execute();
        }
        else
            valueReturn=actionGrab(message,2);
        if(thereAreOnlyChargeWeapon()&&valueReturn)
            endTurn();
        return valueReturn;
    }

    protected boolean actionGrab(Message message,int maxMove){
        boolean valueReturn=false;
        if(message.getActionType()==ActionType.GRABWEAPON ){
            GrabWeapon newMessage=(GrabWeapon) message;
            if(newMessage.getMaxMove()>=1)
                new Move(gameHandler.getGame().getCurrentPlayer(),newMessage.getNewSquare(), maxMove).execute();
            valueReturn= new Grab(gameHandler.getGame().getCurrentPlayer(),newMessage.getCard()).execute();
        }
        else if(message.getActionType()==ActionType.GRABAMMO){
            GrabAmmo newMessage=(GrabAmmo) message;
            if(newMessage.getMaxMove()>=1)
                new Move(gameHandler.getGame().getCurrentPlayer(),newMessage.getNewSquare(), maxMove).execute();
            valueReturn= new Grab(gameHandler.getGame().getCurrentPlayer(),newMessage.getCard()).execute();
        }
        else{
            GrabNotOnlyAmmo newMessage=(GrabNotOnlyAmmo) message;
            if(newMessage.getMaxMove()>=1)
                new Move(gameHandler.getGame().getCurrentPlayer(),newMessage.getNewSquare(), maxMove).execute();
            valueReturn= new Grab(gameHandler.getGame().getCurrentPlayer(),newMessage.getCard()).execute();
        }
        return valueReturn;
    }

    private boolean thereAreOnlyChargeWeapon(){
        boolean valueReturn=true;
        for(CardWeapon c: getGameHandler().getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons()){
            if(!c.isCharge())
                valueReturn=false;
        }
        return valueReturn;
    }

    public boolean actionReload(ReloadMessage message){
        boolean valueReturn=false;
        gameHandler.getGame().getCurrentPlayer().setState(StateMachineEnumerationTurn.RELOAD);
        setNextState(StateMachineEnumerationTurn.ENDTURN);
        if(message.getPowerUp()==null)
            valueReturn=new Reload(gameHandler.getGame().getCurrentPlayer(),message.getWeapon()).execute();
        valueReturn=new Reload(gameHandler.getGame().getCurrentPlayer(),message.getWeapon(),message.getPowerUp()).execute();
        return valueReturn;
    }

    public void endTurn(){
        //TODO messaggio che è finito turno
        endTurnChecks.playerIsDead(gameHandler.getGame());
        gameHandler.getGame().getCurrentPlayer().setState(StateMachineEnumerationTurn.WAIT);
        gameHandler.getGame().incrementCurrentPlayer();
        gameHandler.getGame().getCurrentPlayer().setState(StateMachineEnumerationTurn.START);
        endTurnChecks.fillSquare(gameHandler.getGame());
        endTurnChecks.isFinalTurn(gameHandler.getGame());
        start();

    }
/*
    public List<Player> receiveTarget(PossibleTargetShot message){
        ArrayList<Player> targets=new ArrayList<>();
        if(gameHandler.getGame().getCurrentPlayer().getState()==StateMachineEnumerationTurn.ACTION1 || gameHandler.getGame().getCurrentPlayer().getState()==StateMachineEnumerationTurn.ACTION2)
             targets=new Injure(gameHandler.getGame().getCurrentPlayer(),null, message.getEffect()).possibleTarget();
        return  targets;
    }

    public List<NormalSquare> receiveSquare(PossibleMove message){
        ArrayList<NormalSquare> squares=new ArrayList<>();
        if(gameHandler.getGame().getCurrentPlayer().getState()==StateMachineEnumerationTurn.ACTION1 || gameHandler.getGame().getCurrentPlayer().getState()==StateMachineEnumerationTurn.ACTION2)
            squares=new Move(gameHandler.getGame().getCurrentPlayer(),null,message.getMaxMove()).possibleSquare();
        return  squares;
    }
*/

    class EndTurnChecks {
        private ArrayList<NormalSquare> emptySquares;

        public List<NormalSquare> getEmptySquares() {
            return emptySquares;
        }

        public void fillSquare(Game game) {
            int i = emptySquares.size() - 1;
            while (emptySquares.size() == 0) {
                if (emptySquares.get(i).isSpawn())
                    emptySquares.get(i).setItems(game.getDeckCollector().getCardWeaponDrawer().draw());
                else
                    emptySquares.get(i).setItems(game.getDeckCollector().getCardAmmoDrawer().draw());
                emptySquares.remove(i);
                i--;
            }
        }

        public void playerIsDead(Game game) {
            int i=game.getDeadPlayer().size()-1;
            while(game.getDeadPlayer().size() == 0) {
                game.getDeadPlayer().get(i).getPlayerBoard().getHealthPlayer().death();
                i--;
            }
        }

        private void isFinalTurn(Game game){
            if(game.getDeadRoute().isFinalTurn())
                game.getDeadRoute().finalTurnPlayer();
            getGameHandler().getFinalTurnHandler().setFirstFinalTurnPlayer(getGameHandler().getGame().getCurrentPlayer());
            if(getGameHandler().getGame().getCurrentPlayer()==getGameHandler().getFinalTurnHandler().getFirstFinalTurnPlayer())
                getGameHandler().getFinalTurnHandler().setAlreadyFirsPlayer(true);
        }
    }
}
