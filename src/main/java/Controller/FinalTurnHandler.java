package Controller;

import Model.CardPowerUp;
import Model.Move;
import Model.Player;
import Model.Reload;
import network.messages.*;

import java.util.ArrayList;

public class FinalTurnHandler extends TurnHandler {
    private Player firstFinalTurnPlayer;
    private GameHandler gameHandler;
    private EndFinalTurnChecks endFinalTurnChecks;
    private boolean alreadyFirstPlayer;
    private ActionValidController actionValidController;
    private PaymentController paymentController;

    public FinalTurnHandler(GameHandler gameHandler) {
        super(gameHandler);
        this.firstFinalTurnPlayer=null;
        endFinalTurnChecks=new EndFinalTurnChecks();
        alreadyFirstPlayer=false;
        this.actionValidController = gameHandler.getActionValidController();
        this.paymentController = gameHandler.getPaymentController();
    }

    public void setFirstFinalTurnPlayer(Player firstFinalTurnPlayer) {
        this.firstFinalTurnPlayer = firstFinalTurnPlayer;
    }

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

    public boolean actionFinalTurn(Message message){
        boolean valueReturn;

        if(alreadyFirstPlayer)
            valueReturn=actionAfterFirstPlayer(message);
        else
            valueReturn=actionBeforeFirstPlayer(message);

        return  valueReturn;
    }

    private boolean actionBeforeFirstPlayer(Message message){
        boolean valueReturn;
        if(message.getActionType()==ActionType.MOVE){
            MoveMessage newMessage=(MoveMessage)message;
            valueReturn= new Move(gameHandler.getGame().getCurrentPlayer(),newMessage.getNewSquare(), 4).execute();}
        else if(message.getActionType()==ActionType.SHOT){
            Shot newMessage=(Shot)message;
            valueReturn= actionShot(newMessage);
        }
        else
            valueReturn=actionGrab(message);
        return valueReturn;
    }

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
