package Controller;

import Model.Move;
import Model.Player;
import Model.Reload;
import network.messages.*;

/**
 * This class is used to handle the final frenzy turn
 */
public class FinalTurnHandler extends TurnHandler {
    private Player firstFinalTurnPlayer;
    private GameHandler gameHandler;
    private EndFinalTurnChecks endFinalTurnChecks;
    private boolean alreadyFirstPlayer;
    private ActionValidController actionValidController;
    private PaymentController paymentController;

    /**
     * The constructor is called from a gamehandler and create the class used to check final frenzy rules
     * @param gameHandler the gameHandler which creates the handler
     */
    public FinalTurnHandler(GameHandler gameHandler) {
        super(gameHandler);
        this.gameHandler = gameHandler;
        this.firstFinalTurnPlayer=null;
        endFinalTurnChecks=new EndFinalTurnChecks();
        alreadyFirstPlayer=false;
        this.actionValidController = gameHandler.getActionValidController();
        this.paymentController = gameHandler.getPaymentController();
    }

    /**
     * This method sets the first player of the final turn, the player is saved beacause when is his turn the game ends
     * @param firstFinalTurnPlayer the player which is the first in the final turn
     */
    public void setFirstFinalTurnPlayer(Player firstFinalTurnPlayer) {
        this.firstFinalTurnPlayer = firstFinalTurnPlayer;
    }


    /**
     * This method return true if the first player has yet played
     */
    public boolean isAlreadyFirstPlayer() {
        return alreadyFirstPlayer;
    }

    public Player getFirstFinalTurnPlayer() {
        return firstFinalTurnPlayer;
    }

    public void setAlreadyFirsPlayer(boolean alreadyFirsPlayer) {
        this.alreadyFirstPlayer = alreadyFirsPlayer;
    }

    public EndFinalTurnChecks getEndFinalTurnChecks() {
        return endFinalTurnChecks;
    }

    /**
     * This method execute the appropriate action based on player's position in turn
     * @param message a message that contains the action to be done
     * @return true if the action is done
     */
    public boolean actionFinalTurn(Message message){
        boolean valueReturn;

        if(alreadyFirstPlayer)
            valueReturn=actionAfterFirstPlayer(message);
        else
            valueReturn=actionBeforeFirstPlayer(message);

        return  valueReturn;
    }

    /**
     * This method execute actions for the first player
     */
    private boolean actionBeforeFirstPlayer(Message message){
        boolean valueReturn;
        if(message.getActionType()==ActionType.MOVE){
            MoveMessage newMessage=(MoveMessage)message;
            valueReturn= new Move(gameHandler.getGame().getCurrentPlayer(),newMessage.getNewSquare(), 4).execute();
            updateClients(valueReturn, newMessage, gameHandler);

        }
        else if(message.getActionType()==ActionType.SHOT){
            Shot newMessage=(Shot)message;
            valueReturn= actionShot(newMessage);
        }
        else
            valueReturn=actionGrab(message);
        return valueReturn;
    }

    /**
     * This method send updates to the clients
     */
    static void updateClients(boolean valueReturn, MoveMessage newMessage, GameHandler gameHandler) {
        if(valueReturn) {
            gameHandler.getGameLobby().send(new UpdateClient(newMessage.getPlayerTarget().getPlayerID(), newMessage.getPlayerTarget().getPosition()));
            gameHandler.getGameLobby().getClients()
                    .parallelStream().
                    filter(x -> (!x.equals(newMessage.getPlayerTarget().getPlayerID()))).
                    forEach(x -> gameHandler.getGameLobby().send(new UpdateClient(x, newMessage.getPlayerTarget().getColor(), newMessage.getPlayerTarget().getPosition())));
        }
    }

    /**
     * This method execute actions for players which are not the firsts
     */
    private boolean actionAfterFirstPlayer(Message message){
        boolean valueReturn;
        if(message.getActionType()==ActionType.SHOT){
            Shot newMessage=(Shot)message;
            valueReturn= actionShot(newMessage);
        }
        else
            valueReturn=actionGrab(message);
        return valueReturn;
    }

    /**
     * This method controll the validation of Reload action
     * If it's possible execute the action and return true, otherwise don't execute the action and return false
     *
     * @param message   is the ReloadMessage obtained by the Server
     * */
    @Override
    public boolean actionReload(ReloadMessage message){
        boolean valueReturn;

        int[] cost = {0, 0, 0};
        cost[0] = gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(message.getWeapon()).getRedCost();
        cost[1] = gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(message.getWeapon()).getYellowCost();
        cost[2] = gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(message.getWeapon()).getBlueCost();

        valueReturn = new Reload(gameHandler.getGame().getCurrentPlayer(), gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(message.getWeapon()), cost[0], cost[1], cost[2]).execute();
        return valueReturn;

    }

    /**
     * This method execute a check routine when turn is end
     */
    public void endTurn(){

        endFinalTurnChecks.playerIsDead(gameHandler.getGame());
        gameHandler.getGame().getCurrentPlayer().setState(StateMachineEnumerationTurn.WAIT);
        gameHandler.getGame().incrementCurrentPlayer();
        gameHandler.getGame().getCurrentPlayer().setState(StateMachineEnumerationTurn.START);
        if(endFinalTurnChecks.finalTurnIsEnd())
            getGameHandler().endGame();
        else{
            endFinalTurnChecks.fillSquare(gameHandler.getGame());
            endFinalTurnChecks.isFirstPlayer();
            start();
        }

    }

    /**
     * This nested class is used to execute final turns checks
     */
    class EndFinalTurnChecks extends EndTurnChecks{
        public void isFirstPlayer(){
            if(getGameHandler().getGame().getCurrentPlayer()==getGameHandler().getGame().getFirstPlayer() )
                setAlreadyFirsPlayer(true);
        }

        public boolean finalTurnIsEnd(){
            boolean valueReturn=false;
            if(getGameHandler().getGame().getCurrentPlayer()==getFirstFinalTurnPlayer())
                valueReturn=true;
            return valueReturn;
        }

    }
}
