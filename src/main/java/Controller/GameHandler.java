package Controller;

import Model.*;
import network.messages.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class GameHandler {
    private Game game;
    private FinalTurnHandler finalTurnHandler;
    private TurnHandler turnHandler;

    public GameHandler(int n, List<Integer> players, String file) throws FileNotFoundException {
        this.game = new Game(n,file);
        Colors[] colors=Colors.values();
        int i=0;
        while(i<players.size()){
            getGame().addPlayer(new Player(players.get(0),colors[i]));
            i++;
        }
        finalTurnHandler=new FinalTurnHandler(this);
        turnHandler=new TurnHandler(this);
        getGame().chooseFirstPlayer();
        getTurnHandler().start();
    }

    public FinalTurnHandler getFinalTurnHandler() {
        return finalTurnHandler;
    }
    public TurnHandler getTurnHandler() {
        return turnHandler;
    }
    public Game getGame() {
        return game;
    }

    public boolean receiveServerMessage(Message message){
        boolean valueReturn=false;
        if(!getGame().getDeadRoute().isFinalTurn()){
            if((message.getActionType()==ActionType.GRABAMMO) ||(message.getActionType()==ActionType.MOVE)||
                    (message.getActionType()==ActionType.SHOT)||(message.getActionType()==ActionType.GRABWEAPON)||(message.getActionType()==ActionType.GRABNOTONLYAMMO))
                valueReturn=getTurnHandler().actionState(message);
            else if(message.getActionType()==ActionType.PASS){
                getTurnHandler().endTurn();
                valueReturn=true;
            }
            else if(message.getActionType()==ActionType.RELOAD)
                valueReturn=getTurnHandler().actionReload((ReloadMessage) message);
        }
        else
        if((message.getActionType()==ActionType.GRABAMMO) ||(message.getActionType()==ActionType.MOVE)||
                (message.getActionType()==ActionType.SHOT)||(message.getActionType()==ActionType.GRABWEAPON)||(message.getActionType()==ActionType.GRABNOTONLYAMMO))
            valueReturn=getFinalTurnHandler().actionFinalTurn(message);
        else if(message.getActionType()==ActionType.PASS){
            getFinalTurnHandler().endTurn();
            valueReturn=true;
        }
        return valueReturn;
    }

    public void endGame(){
        for(Player p:getGame().getPlayers())
            p.getPlayerBoard().getHealthPlayer().death();
        getGame().calculatePoints(getGame().getDeadRoute().getMurders(),true,null);
        //messaggio server partita finita
    }
    public ArrayList<Player> receiveTarget(PossibleTargetShot message){
        ArrayList<Player> targets=new ArrayList<>();
        targets=new Injure(getGame().getCurrentPlayer(),null, message.getEffect()).targetablePlayer();
        return  targets;
    }

    public List<NormalSquare> receiveSquare(PossibleMove message){
        ArrayList<NormalSquare> squares=new ArrayList<>();
        squares=new Move(getGame().getCurrentPlayer(),null,message.getMaxMove()).reachableSquare();
        return  squares;
    }

    public List<Player> winner(){
        int massimo=0;
        ArrayList<Player> winnerList=new ArrayList<>();
        for(Player player:getGame().getPlayers()){
            if(player.getPlayerBoard().getPoints()>massimo) {
                while(winnerList.size()!=0)
                    winnerList.remove(winnerList.size()-1);
                massimo=player.getPlayerBoard().getPoints();
                winnerList.add(player);
            }
            else if(player.getPlayerBoard().getPoints()==massimo && massimo!=0) { //if the firstPlayer do 0 points caused index bound exception
                modifiedWinnerList(player, winnerList);
                massimo=winnerList.get(0).getPlayerBoard().getPoints(); //if winnerList is a new list
            }
        }

        return winnerList;
    }


    public int countPlayer(Player player){
        int counterPlayer=0;
        for( Player p : getGame().getDeadRoute().getMurders() ){
            if (p==player)
                counterPlayer++;
        }
        return counterPlayer;
    }
    private void modifiedWinnerList(Player player,ArrayList<Player> winnerList){
        if(countPlayer(player)>countPlayer(winnerList.get(0))){
            while(winnerList.size()!=0){
                winnerList.remove(winnerList.size()-1);
            }
            winnerList.add(player);
        }
        else if(countPlayer(player)==countPlayer(winnerList.get(0)))
            winnerList.add(player);
    }
}

