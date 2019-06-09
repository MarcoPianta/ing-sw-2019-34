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
                valueReturn=turnHandler.actionState(message);
            else if(message.getActionType()==ActionType.PASS){
                turnHandler.endTurn();
                valueReturn=true;
            }
            else if(message.getActionType()==ActionType.RELOAD)
                valueReturn=turnHandler.actionReload((ReloadMessage) message);
            else if(message.getActionType()==ActionType.USEPOWERUP)
                valueReturn=turnHandler.usePowerUp(message);
        }
        else{
            if((message.getActionType()==ActionType.GRABAMMO) ||(message.getActionType()==ActionType.MOVE)||
                    (message.getActionType()==ActionType.SHOT)||(message.getActionType()==ActionType.GRABWEAPON)||(message.getActionType()==ActionType.GRABNOTONLYAMMO))
                valueReturn=finalTurnHandler.actionFinalTurn(message);
            else if(message.getActionType()==ActionType.PASS){
                finalTurnHandler.endTurn();
                valueReturn=true;
            }
            else if(message.getActionType()==ActionType.RELOAD)
                valueReturn=finalTurnHandler.actionReload((ReloadMessage)message);
            else if(message.getActionType()==ActionType.USEPOWERUP)
                valueReturn=finalTurnHandler.usePowerUp(message);

        }
        return valueReturn;
    }

    public void endGame(){
        for(Player p:getGame().getPlayers())
            p.getPlayerBoard().getHealthPlayer().death();
        getGame().calculatePoints(getGame().getDeadRoute().getMurders(),true,null);
        //messaggio server partita finita
    }
    public List<Player> receiveTarget(PossibleTargetShot message){
        List<Player> targets;
        targets=new Shoot(message.getEffect(),getGame().getCurrentPlayer(),(List<Player>) null).targetablePlayer();
        return  targets;
    }

    public List<NormalSquare> receiveSquare(PossibleMove message){
        ArrayList<NormalSquare> squares;
        squares=new Move(getGame().getCurrentPlayer(),null,message.getMaxMove()).reachableSquare();
        return  squares;
    }
    //move
    public boolean actionValid(Player target,NormalSquare square,int maxMove){
        return  new Move(target,square,maxMove).isValid();
    }
    //shot
    public boolean actionValid(ArrayList<Player> targets,Effect effect, boolean powerUp){
        boolean valueReturn;
        if(!powerUp){
            if(game.getCurrentPlayer().isValidCost(effect.getBonusCost(),powerUp))
                valueReturn=new Shoot(effect,game.getCurrentPlayer(),targets).isValid();
            else
                valueReturn=false;
        }
        else{
            if(game.getCurrentPlayer().isValidCost(effect.getBonusCost(),powerUp))
                valueReturn=new Shoot(effect,game.getCurrentPlayer(),targets).isValid();
            else
                valueReturn=false;
        }
        return valueReturn;
    }
    public boolean actionValid(NormalSquare square,Effect effect,boolean powerUp){
        boolean valueReturn;
        if(!powerUp){
            if(game.getCurrentPlayer().isValidCost(effect.getBonusCost(),false))
                valueReturn=new Shoot(effect,game.getCurrentPlayer(),square).isValid();
            else
                valueReturn=false;
        }
        else{
            if(game.getCurrentPlayer().isValidCost(effect.getBonusCost(),true))
                valueReturn=new Shoot(effect,game.getCurrentPlayer(),square).isValid();
            else
                valueReturn=false;
        }
        return valueReturn;
    }

    public boolean actionValid(Room room,Effect effect,boolean powerUp){
        boolean valueReturn;
        if(!powerUp){
            if(game.getCurrentPlayer().isValidCost(effect.getBonusCost(),powerUp))
                valueReturn=new Shoot(effect,game.getCurrentPlayer(),room.getColor()).isValid();
            else
                valueReturn=false;
        }
        else{
            if(game.getCurrentPlayer().isValidCost(effect.getBonusCost(),powerUp))
                valueReturn=new Shoot(effect,game.getCurrentPlayer(),room.getColor()).isValid();
            else
                valueReturn=false;
        }
        return valueReturn;
    }

    //grab
    public boolean actionValid(SpawnSquare square, int i){
        ArrayList<Integer> cost=new ArrayList<>();
        cost.add(square.getWeapons().get(i).getBlueCost());
        cost.add(square.getWeapons().get(i).getYellowCost());
        cost.add(square.getWeapons().get(i).getRedCost());
        return game.getCurrentPlayer().isValidCost(cost,false);
    }
    //reload
    public boolean actionValid(int i){
        ArrayList<Integer> cost=new ArrayList<>();
        cost.add(game.getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(i).getBlueCost());
        cost.add(game.getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(i).getYellowCost());
        cost.add(game.getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(i).getRedCost());
        return game.getCurrentPlayer().isValidCost(cost,false);
    }

    public UpdateClient firstPartAction(ReceiveTargetSquare message){
        UpdateClient messageReturn;
        if(message.getType().equals("shoot"))
            messageReturn=receiveShoot(message);
        else if(message.getType().equals("grab"))
            messageReturn=receiveGrab(message);
        else
            messageReturn=receiveMove(message);
        return messageReturn;
    }
    private UpdateClient receiveShoot(ReceiveTargetSquare message){
        UpdateClient messageReturn=new UpdateClient(null,(ArrayList<NormalSquare>)null);
        if(getGame().getCurrentPlayer().getPlayerBoard().getHealthPlayer().getAdrenalineAction()==2
                &&!getGame().getDeadRoute().isFinalTurn())
            messageReturn= new UpdateClient(message.getToken(),new Move(getGame().getCurrentPlayer(),null,1).reachableSquare());
        else if((getGame().getCurrentPlayer().getPlayerBoard().getHealthPlayer().getAdrenalineAction()==0
                ||getGame().getCurrentPlayer().getPlayerBoard().getHealthPlayer().getAdrenalineAction()==1 ) &&!getGame().getDeadRoute().isFinalTurn())
            messageReturn= new UpdateClient(message.getToken(),(ArrayList<Player>)new  Shoot(message.getEffect(),getGame().getCurrentPlayer(),(List<Player>) null).targetablePlayer());
        else if (getGame().getDeadRoute().isFinalTurn() && getFinalTurnHandler().isAlreadyFirstPlayer())
            messageReturn=new UpdateClient(message.getToken(),new Move(getGame().getCurrentPlayer(),null,2).reachableSquare());
        else if (getGame().getDeadRoute().isFinalTurn() && !getFinalTurnHandler().isAlreadyFirstPlayer())
            messageReturn=new UpdateClient(message.getToken(),new Move(getGame().getCurrentPlayer(),null,1).reachableSquare());
        //verified if there is sight power up
        boolean isFind=false;
        for(int i=0;i<3 && !isFind;i++){
            if(getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerPowerUps().get(i).getWhen().equals("get")){
                isFind=true;
                //TODO chiamata per uso mirino
            }
        }
        return messageReturn;
    }
    private UpdateClient receiveGrab(ReceiveTargetSquare message){
        UpdateClient messageReturn=new UpdateClient(getGame().getCurrentPlayer().getPlayerID(),(ArrayList<NormalSquare>) null);
        if(getGame().getCurrentPlayer().getPlayerBoard().getHealthPlayer().getAdrenalineAction()==0
                &&!getGame().getDeadRoute().isFinalTurn())
            messageReturn= new UpdateClient(message.getToken(),new Move(getGame().getCurrentPlayer(),null,1).reachableSquare());
        else if((getGame().getCurrentPlayer().getPlayerBoard().getHealthPlayer().getAdrenalineAction()==1
                ||getGame().getCurrentPlayer().getPlayerBoard().getHealthPlayer().getAdrenalineAction()==2 ) &&!getGame().getDeadRoute().isFinalTurn())
            messageReturn= new UpdateClient(message.getToken(),new Move(getGame().getCurrentPlayer(),null,1).reachableSquare());
        else if (getGame().getDeadRoute().isFinalTurn() && getFinalTurnHandler().isAlreadyFirstPlayer())
            messageReturn=new UpdateClient(message.getToken(),new Move(getGame().getCurrentPlayer(),null,3).reachableSquare());
        else if (getGame().getDeadRoute().isFinalTurn() && !getFinalTurnHandler().isAlreadyFirstPlayer())
            messageReturn=new UpdateClient(message.getToken(),new Move(getGame().getCurrentPlayer(),null,2).reachableSquare());
        return  messageReturn;
    }

    private UpdateClient receiveMove(ReceiveTargetSquare message){
        //case  final turn e Already firstPlayer
        UpdateClient messageReturn=new UpdateClient(getGame().getCurrentPlayer().getPlayerID(),(ArrayList<NormalSquare>)null);
        if((getGame().getCurrentPlayer().getPlayerBoard().getHealthPlayer().getAdrenalineAction()==1
                ||getGame().getCurrentPlayer().getPlayerBoard().getHealthPlayer().getAdrenalineAction()==2 ||getGame().getCurrentPlayer().getPlayerBoard().getHealthPlayer().getAdrenalineAction()==0)
                &&!getGame().getDeadRoute().isFinalTurn())
            messageReturn= new UpdateClient(message.getToken(),new Move(getGame().getCurrentPlayer(),null,1).reachableSquare());
        else if (getGame().getDeadRoute().isFinalTurn() && !getFinalTurnHandler().isAlreadyFirstPlayer())
            messageReturn=new UpdateClient(message.getToken(),new Move(getGame().getCurrentPlayer(),null,4).reachableSquare());
        return  messageReturn;
    }




    public List<Player> winner(){
        int massimo=0;
        ArrayList<Player> winnerList=new ArrayList<>();
        for(Player player:getGame().getPlayers()){
            if(player.getPlayerBoard().getPoints()>massimo) {
                while(!winnerList.isEmpty()){
                    winnerList.remove(0);
                    assert (winnerList.isEmpty());
                }
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
            while(!winnerList.isEmpty()){
                winnerList.remove(0);
            }
            winnerList.add(player);
        }
        else if(countPlayer(player)==countPlayer(winnerList.get(0)))
            winnerList.add(player);
    }

    public void payment(List<Integer> cost, List<Integer> powerUp){
        ArrayList<CardPowerUp> powerUps=new ArrayList<>();
        ArrayList<Integer> ammo=new ArrayList<>();
        for(Integer i:powerUp)
            powerUps.add(getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerPowerUps().get(i));
        for(CardPowerUp p:powerUps){
            if(p.getColor()==AmmoColors.RED)
                ammo.add(cost.get(0) -1);
            else if(p.getColor()==AmmoColors.YELLOW)
                ammo.add(cost.get(1) -1);
            else if(p.getColor()==AmmoColors.BLUE)
                ammo.add(cost.get(2) -1);
            game.getCurrentPlayer().getPlayerBoard().getHandPlayer().removePowerUp(game.getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerPowerUps().indexOf(p));
        }
        game.getCurrentPlayer().getPlayerBoard().getHandPlayer().decrementAmmo(ammo.get(0),ammo.get(1),ammo.get(2));
    }
    public void payment(List<Integer> cost){
        getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().decrementAmmo(cost.get(0),cost.get(1),cost.get(2));
    }
}

