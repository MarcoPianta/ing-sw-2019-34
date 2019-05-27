package Controller;

import Model.Injure;
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

    public FinalTurnHandler(GameHandler gameHandler) {
        super(gameHandler);
        this.firstFinalTurnPlayer=null;
        endFinalTurnChecks=new EndFinalTurnChecks();
        alreadyFirstPlayer=false;
    }

    public void setFirstFinalTurnPlayer(Player firstFinalTurnPlayer) {
        this.firstFinalTurnPlayer = firstFinalTurnPlayer;
    }

    public Player getFirstFinalTurnPlayer() {
        return firstFinalTurnPlayer;
    }

    public void setAlreadyFirsPlayer(boolean alreadyFirsPlayer) {
        this.alreadyFirstPlayer = alreadyFirsPlayer;
    }

    public boolean actionFinalTurn(Message message){
        boolean valueReturn=false;
        if (gameHandler.getGame().getCurrentPlayer().getState()==StateMachineEnumerationTurn.START){
            gameHandler.getGame().getCurrentPlayer().setState(StateMachineEnumerationTurn.ACTION1);
            setNextState(StateMachineEnumerationTurn.ACTION2);
        }
        else{
            gameHandler.getGame().getCurrentPlayer().setState(StateMachineEnumerationTurn.ACTION2);
            setNextState(StateMachineEnumerationTurn.ENDTURN);
        }

        if(alreadyFirstPlayer)
            valueReturn=actionAfterFirstPlayer(message);
        else
            valueReturn=actionBeforeFirstPlayer(message);

        return  valueReturn;
    }

    private boolean actionBeforeFirstPlayer(Message message){
        boolean valueReturn=false;
        if(message.getActionType()==ActionType.MOVE){
            MoveMessage newMessage=(MoveMessage)message;
            valueReturn= new Move(gameHandler.getGame().getCurrentPlayer(),newMessage.getNewSquare(), 4).execute();}
        else if(message.getActionType()==ActionType.SHOT){
            Shot newMessage=(Shot)message;
            if(newMessage.getMaxMove()==1)
                new Move(gameHandler.getGame().getCurrentPlayer(),newMessage.getNewSquare(), 1).execute();
            if(newMessage.isReload())
                new Reload(gameHandler.getGame().getCurrentPlayer(), newMessage.getWeapon()).execute();
            valueReturn= actionShot(newMessage);
        }
        else
            valueReturn=actionGrab(message,2);
        return valueReturn;
    }

    private boolean actionAfterFirstPlayer(Message message){
        boolean valueReturn=false;
        if(message.getActionType()==ActionType.SHOT){
            Shot newMessage=(Shot)message;
            if(newMessage.getMaxMove()>=1)
                new Move(gameHandler.getGame().getCurrentPlayer(),newMessage.getNewSquare(), 2).execute();
            if(newMessage.isReload())
                new Reload(gameHandler.getGame().getCurrentPlayer(), newMessage.getWeapon()).execute();
            valueReturn= actionShot(newMessage);
        }
        else
            valueReturn=actionGrab(message,3);
        return valueReturn;
    }


    public void endTurn(Pass message){
        //TODO messaggio che è finito turno
        endFinalTurnChecks.playerIsDead(gameHandler.getGame());
        if(endFinalTurnChecks.finalTurnIsEnd())
            getGameHandler().endGame();
        else{
            gameHandler.getGame().getCurrentPlayer().setState(StateMachineEnumerationTurn.WAIT);
            gameHandler.getGame().incrementCurrentPlayer();
            gameHandler.getGame().getCurrentPlayer().setState(StateMachineEnumerationTurn.START);
            endFinalTurnChecks.fillSquare(gameHandler.getGame());
            endFinalTurnChecks.playerIsDead(gameHandler.getGame());
            endFinalTurnChecks.isFirstPlayer();
            start();
        }

    }

    private class EndFinalTurnChecks extends EndTurnChecks{
        private void isFirstPlayer(){
            if(getGameHandler().getGame().getCurrentPlayer()==getGameHandler().getFinalTurnHandler().getFirstFinalTurnPlayer())
                setAlreadyFirsPlayer(true);
        }

        private boolean finalTurnIsEnd(){
            boolean valueReturn=false;
            if(getGameHandler().getGame().getCurrentPlayer()==getFirstFinalTurnPlayer())
                valueReturn=true;
            return valueReturn;
        }

    }
}
