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

    public EndTurnChecks getEndTurnChecks() {
        return endTurnChecks;
    }

    public void start(){
        gameHandler.getGame().getCurrentPlayer().setState(StateMachineEnumerationTurn.START);
        //TODO messaggio che inizia turno
        if(gameHandler.getGame().getCurrentPlayer().getPosition()==null && gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getMaxReward()==8){
            gameHandler.getGame().getCurrentPlayer().spawn(2);//first spawn
            gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().addAmmo(1,1,1);
        }
        setNextState(StateMachineEnumerationTurn.ACTION1);
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
        if(gameHandler.getGame().getCurrentPlayer().getState()==StateMachineEnumerationTurn.ACTION1)
            setNextState(StateMachineEnumerationTurn.ACTION2);
        else
            setNextState(StateMachineEnumerationTurn.RELOAD);
        return  valueReturn;
    }
    private boolean actionAdrenaline0(Message message){
        boolean valueReturn=false;
        if(message.getActionType()==ActionType.MOVE){
            MoveMessage newMessage=(MoveMessage)message;
            valueReturn= new Move(gameHandler.getGame().getCurrentPlayer(),newMessage.getNewSquare(), 3).execute();}
        else if(message.getActionType()==ActionType.SHOT){
            Shot newMessage=(Shot)message;
            if(newMessage.getPowerUp()!=null){
                if(!usePowerUp(message)){}
                    //send message at server for failure powerup
            }
            valueReturn= actionShot(newMessage);
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
            valueReturn= actionShot(newMessage);
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
            valueReturn= actionShot(newMessage);
        }
        else
            valueReturn=actionGrab(message,2);
        return valueReturn;
    }
    protected boolean actionShot(Shot message){
        boolean valueReturn=false;
        if(message.getNewSquare()==null && message.getRoom()==null)
            valueReturn=new Shoot(message.getEffect(),gameHandler.getGame().getCurrentPlayer(), message.getTargets()).execute();
        else if(message.getNewSquare()==null && message.getTargets()==null)
            valueReturn=new Shoot(message.getEffect(),gameHandler.getGame().getCurrentPlayer(),message.getRoom().getColor()).execute();
        else
            valueReturn=new Shoot(message.getEffect(),gameHandler.getGame().getCurrentPlayer(),message.getSquare()).execute();
        //use venom
        if(valueReturn){
            for(Player p:message.getTargets()){
                for(CardPowerUp powerUp:p.getPlayerBoard().getHandPlayer().getPlayerPowerUps()){
                    CanUseVenom canUseVenom;
                    if(powerUp.getWhen().equals("get"))
                        canUseVenom=new CanUseVenom(p.getPlayerID());
                }

            }
        }
        return valueReturn;
    }
    //creare messaggio diverso per weapon =3, stesso messaggio ma costruttore diverso, se ho 3 armki grabbo normalmente  e elimino lintero per rimuovere
    protected boolean actionGrab(Message message,int maxMove){
        boolean valueReturn=false;
        if(message.getActionType()==ActionType.GRABWEAPON ){
            GrabWeapon newMessage=(GrabWeapon) message;
            if(newMessage.getMaxMove()>=1)
                new Move(gameHandler.getGame().getCurrentPlayer(),newMessage.getNewSquare(), maxMove).execute();
            valueReturn= new Grab(gameHandler.getGame().getCurrentPlayer(),newMessage.getNewSquare().getWeapons().get(newMessage.getPositionWeapon())).execute();
            if(valueReturn)
                endTurnChecks.getEmptySquares().add(newMessage.getNewSquare());
        }
        else if(message.getActionType()==ActionType.GRABAMMO){
            GrabAmmo newMessage=(GrabAmmo) message;
            if(newMessage.getMaxMove()>=1)
                new Move(gameHandler.getGame().getCurrentPlayer(),newMessage.getNewSquare(), maxMove).execute();
            valueReturn= new Grab(gameHandler.getGame().getCurrentPlayer(), (CardOnlyAmmo) newMessage.getNewSquare().getItem()).execute();
            if(valueReturn)
                endTurnChecks.getEmptySquares().add(newMessage.getNewSquare());
        }
        else{
            GrabNotOnlyAmmo newMessage=(GrabNotOnlyAmmo) message;
            if(newMessage.getMaxMove()>=1)
                new Move(gameHandler.getGame().getCurrentPlayer(),newMessage.getNewSquare(), maxMove).execute();
            valueReturn= new Grab(gameHandler.getGame().getCurrentPlayer(), (CardNotOnlyAmmo) newMessage.getNewSquare().getItem()).execute();
            if(valueReturn)
                endTurnChecks.getEmptySquares().add(newMessage.getNewSquare());
        }
        SubstituteWeapon substituteWeapon= new SubstituteWeapon(gameHandler.getGame().getCurrentPlayer().getPlayerID(),gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons());

        if(valueReturn && gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().size()==4)
            {}
        return valueReturn;
    }

    private boolean powerUpIsValid(Message message){
        boolean valueReturn=false;
        if(message.getActionType()==ActionType.USEPOWERUP){
            UsePowerUp newMessage=(UsePowerUp)message;
            if((newMessage.getPowerUp().getWhen().equals("during") &&(newMessage.getUser()==gameHandler.getGame().getCurrentPlayer())
                    &&(gameHandler.getGame().getCurrentPlayer().getState()!=StateMachineEnumerationTurn.WAIT)))
                valueReturn=true;
            else if(newMessage.getPowerUp().getWhen().equals("dealing") &&(newMessage.getUser()!=gameHandler.getGame().getCurrentPlayer()))
                valueReturn=true;

        }
        else if(message.getActionType()==ActionType.SHOT){
            Shot newMessage=(Shot)message;
            if(((newMessage.getPowerUp().getWhen().equals("get"))
                    &&((gameHandler.getGame().getCurrentPlayer().getState()==StateMachineEnumerationTurn.ACTION1)
                    ||(gameHandler.getGame().getCurrentPlayer().getState()==StateMachineEnumerationTurn.ACTION2))))
                valueReturn=true;
        }
        return valueReturn;
    }

    public boolean usePowerUp(Message message){
        boolean valueReturn=false;
        if(message.getActionType()==ActionType.SHOT && powerUpIsValid(message)){
            Shot newMessage=(Shot) message;
            gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().usePowerUp(newMessage.getPowerUp(),newMessage.getTargetPowerUp(),0,null);
            valueReturn=true;
        }
        else if(powerUpIsValid(message)){
            UsePowerUp newMessage=(UsePowerUp) message;
            newMessage.getUser().getPlayerBoard().getHandPlayer().usePowerUp(newMessage.getPowerUp(),newMessage.getTarget(),newMessage.getMaxMove(),newMessage.getSquare());
            valueReturn=true;
        }
        return valueReturn;

    }

    public boolean actionReload(ReloadMessage message){
        boolean valueReturn=false;
        gameHandler.getGame().getCurrentPlayer().setState(StateMachineEnumerationTurn.RELOAD);
        if(message.getPowerUp()==null)
            valueReturn=new Reload(gameHandler.getGame().getCurrentPlayer(),message.getWeapon()).execute();
        valueReturn=new Reload(gameHandler.getGame().getCurrentPlayer(),message.getWeapon(),message.getPowerUp()).execute();
        setNextState(StateMachineEnumerationTurn.ENDTURN);
        return valueReturn;
    }

    public void endTurn(){
        //TODO messaggio che è finito turno
        gameHandler.getGame().getCurrentPlayer().setState(StateMachineEnumerationTurn.WAIT);
        gameHandler.getGame().incrementCurrentPlayer();
        setNextState(StateMachineEnumerationTurn.START);
        endTurnChecks.fillSquare(gameHandler.getGame());
        endTurnChecks.isFinalTurn(gameHandler.getGame());
        endTurnChecks.playerIsDead(gameHandler.getGame());
        start();
    }
    class EndTurnChecks {
        private ArrayList<NormalSquare> emptySquares=new ArrayList<>();

        public List<NormalSquare> getEmptySquares() {
            return emptySquares;
        }

        public void fillSquare(Game game) {
            int i = emptySquares.size() - 1;
            while (emptySquares.isEmpty()) {
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
            while(i>=0) {
                game.getDeadPlayer().get(i). spawn(1);//extract powerUp
                game.getDeadPlayer().get(i).getPlayerBoard().getHealthPlayer().death();
                i--;
            }
        }

        public void isFinalTurn(Game game){
            if(game.getDeadRoute().isFinalTurn())
                game.getDeadRoute().setFinalTurnPlayer();
            getGameHandler().getFinalTurnHandler().setFirstFinalTurnPlayer(getGameHandler().getGame().getCurrentPlayer());
            if(getGameHandler().getGame().getCurrentPlayer()==getGameHandler().getGame().getFirstPlayer())
                getGameHandler().getFinalTurnHandler().setAlreadyFirsPlayer(true);
        }
    }
}
