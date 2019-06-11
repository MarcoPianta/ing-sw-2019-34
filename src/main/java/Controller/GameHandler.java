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

    /**
     * The constructor of gameHandler
     * @param n the numbers of skulls
     * @param players the list of tokens' player
     * @param file  the map's file
     * @throws FileNotFoundException
     */
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

    /**
     * This method directs the course of the turn based on the parameter
     * @param message contains the type of action to be performed
     * @return true if the action was completed correctly
     */
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

    /**
     *this method calculates the last planks and the death route
     */
    public void endGame(){
        for(Player p:getGame().getPlayers())
            p.getPlayerBoard().getHealthPlayer().death();
        getGame().calculatePoints(getGame().getDeadRoute().getMurders(),true,null);
        //messaggio server partita finita
    }

    /**
     * this method calculates the possible targets
     * @param message contains the effect
     * @return a list of possible targets
     */
    public ArrayList<Player> receiveTarget(PossibleTargetShot message){
        ArrayList<Player> targets;
        targets=(ArrayList<Player>) new Shoot(message.getEffect(),getGame().getCurrentPlayer(),(List<Player>) null).targetablePlayer();
        return  targets;
    }

    /**
     * this method indicates all the possible squares that the player can reach
     * @param message  contains the max move
     * @return the list of possible square
     */
    public List<NormalSquare> receiveSquare(PossibleMove message){
        ArrayList<NormalSquare> squares;
        squares=new Move(getGame().getCurrentPlayer(),null,message.getMaxMove()).reachableSquare();
        return  squares;
    }

    /**
     * This method verifies the validity of the move action
     * @param target    the Player that is target by the action
     * @param square    the NormalSquare selected by the actor as the final NormalSquare for the target
     * @param maxMove   the max number of pass that the actorPlayer can do
     * @return true if the action is valid
     */
    public boolean actionValid(Player target,NormalSquare square,int maxMove){
        return  new Move(target,square,maxMove).isValid();
    }

    /**
     * this method verifies the validity of the shoot action with targets
     * @param targets  the list of targets who received damage or mark
     * @param effect   the effect uses for the shoot
     * @param powerUp  the possible powerUp sight
     * @return true if the action is valid
     */
    public boolean actionValid(ArrayList<Player> targets,Effect effect, int powerUp){
        boolean valueReturn;
        if(powerUp==-1){
            if(game.getCurrentPlayer().isValidCost(effect.getBonusCost(),false))
                valueReturn=new Shoot(effect,game.getCurrentPlayer(),targets).isValid();
            else
                valueReturn=false;
        }
        else{
            if(game.getCurrentPlayer().isValidCost(effect.getBonusCost(),true)&&game.getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerPowerUps().get(powerUp).getWhen().equals("get"))
                valueReturn=new Shoot(effect,game.getCurrentPlayer(),targets).isValid();
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
    public boolean actionValid(NormalSquare square,Effect effect,int powerUp){
        boolean valueReturn;
        if(powerUp==-1){
            if(game.getCurrentPlayer().isValidCost(effect.getBonusCost(),false))
                valueReturn=new Shoot(effect,game.getCurrentPlayer(),square).isValid();
            else
                valueReturn=false;
        }
        else{
            if(game.getCurrentPlayer().isValidCost(effect.getBonusCost(),true)&&game.getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerPowerUps().get(powerUp).getWhen().equals("get"))
                valueReturn=new Shoot(effect,game.getCurrentPlayer(),square).isValid();
            else
                valueReturn=false;
        }
        return valueReturn;
    }

    /**
     * this method verifies the validity of the shoot action with room
     * @param room     room where is the targets who received damage or mark
     * @param effect   the effect uses for the shoot
     * @param powerUp  the possible powerUp sight
     * @return true if the action is valid
     */
    public boolean actionValid(Room room,Effect effect,int powerUp){
        boolean valueReturn;
        if(powerUp==-1){
            if(game.getCurrentPlayer().isValidCost(effect.getBonusCost(),false))
                valueReturn=new Shoot(effect,game.getCurrentPlayer(),room.getColor()).isValid();
            else
                valueReturn=false;
        }
        else{
            if(game.getCurrentPlayer().isValidCost(effect.getBonusCost(),true) &&game.getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerPowerUps().get(powerUp).getWhen().equals("get"))
                valueReturn=new Shoot(effect,game.getCurrentPlayer(),room.getColor()).isValid();
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
        return game.getCurrentPlayer().isValidCost(cost,false);
    }

    /**
     * this method verified the validity of the reload action
     * @param i   the position of the weapon that must be recharged
     * @return true if the action is valid
     */
    public boolean actionValid(int i){
        ArrayList<Integer> cost=new ArrayList<>();
        cost.add(game.getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(i).getBlueCost());
        cost.add(game.getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(i).getYellowCost());
        cost.add(game.getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(i).getRedCost());
        return game.getCurrentPlayer().isValidCost(cost,false);
    }

    /**
     * this method  verified the first action that the player can do, in basic adrenaline action and final turn
     * @param message  indicates the type of action that the player want do
     * @return a updateClient message with a list of possible target or possible move
     */
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

    /**
     * this method  verified the first action shoot that the player can do, in basic adrenaline action and final turn
     * @param message  indicates only the token for updateClient
     * @return a updateClient message with a list of possible target or possible move
     */
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
            if(game.getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerPowerUps().get(i).getWhen().equals("get")){
                isFind=true;
                //canUseScope(game.getCurrentPlayer());
            }
        }
        return messageReturn;
    }

    /**
     * this method  verified the first action grab that the player can do, in basic adrenaline action and final turn
     * @param   message  indicates only the token for updateClient
     * @return a updateClient message with possible move
     */
    private UpdateClient receiveGrab(ReceiveTargetSquare message){
        UpdateClient messageReturn=new UpdateClient(getGame().getCurrentPlayer().getPlayerID(),(ArrayList<NormalSquare>) null);
        if(game.getCurrentPlayer().getPlayerBoard().getHealthPlayer().getAdrenalineAction()==0
                &&!game.getDeadRoute().isFinalTurn())
            messageReturn= new UpdateClient(message.getToken(),new Move(getGame().getCurrentPlayer(),null,1).reachableSquare());
        else if((getGame().getCurrentPlayer().getPlayerBoard().getHealthPlayer().getAdrenalineAction()==1
                ||game.getCurrentPlayer().getPlayerBoard().getHealthPlayer().getAdrenalineAction()==2 ) &&!game.getDeadRoute().isFinalTurn())
            messageReturn= new UpdateClient(message.getToken(),new Move(getGame().getCurrentPlayer(),null,1).reachableSquare());
        else if (game.getDeadRoute().isFinalTurn() && getFinalTurnHandler().isAlreadyFirstPlayer())
            messageReturn=new UpdateClient(message.getToken(),new Move(getGame().getCurrentPlayer(),null,3).reachableSquare());
        else if (game.getDeadRoute().isFinalTurn() && !getFinalTurnHandler().isAlreadyFirstPlayer())
            messageReturn=new UpdateClient(message.getToken(),new Move(getGame().getCurrentPlayer(),null,2).reachableSquare());
        return  messageReturn;
    }

    /**
     * this method  verified the first action move that the player can do, in basic adrenaline action and final turn
     * @param   message  indicates only the token for updateClient
     * @return a updateClient message with possible move
     */
    private UpdateClient receiveMove(ReceiveTargetSquare message){
        //case  final turn e Already firstPlayer
        UpdateClient messageReturn=new UpdateClient(getGame().getCurrentPlayer().getPlayerID(),(ArrayList<NormalSquare>)null);
        if((game.getCurrentPlayer().getPlayerBoard().getHealthPlayer().getAdrenalineAction()==1
                ||game.getCurrentPlayer().getPlayerBoard().getHealthPlayer().getAdrenalineAction()==2 ||game.getCurrentPlayer().getPlayerBoard().getHealthPlayer().getAdrenalineAction()==0)
                &&!game.getDeadRoute().isFinalTurn())
            messageReturn= new UpdateClient(message.getToken(),new Move(getGame().getCurrentPlayer(),null,1).reachableSquare());
        else if (game.getDeadRoute().isFinalTurn() && !getFinalTurnHandler().isAlreadyFirstPlayer())
            messageReturn=new UpdateClient(message.getToken(),new Move(getGame().getCurrentPlayer(),null,4).reachableSquare());
        return  messageReturn;
    }

    /**
     * this method values who are/is win the game
     * @return the list of the winner
     */
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

    /**
     * this method counts the points in the death route for the parameter
     * @param player  player who is evaluated
     * @return the count of points
     */
    public int countPlayer(Player player){
        int counterPlayer=0;
        for( Player p : getGame().getDeadRoute().getMurders() ){
            if (p==player)
                counterPlayer++;
        }
        return counterPlayer;
    }

    /**
     *
     * @param player
     * @param winnerList
     */
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

    /**
     * this method serves to pay the effect o reload, with powerUp
     * @param cost    is the cost to pay
     * @param powerUp the list of powerUp with which to pay
     */
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

    /**
     * this method serves to pay the effect o reload,
     * @param cost is the cost to pay
     */
    public void payment(List<Integer> cost){
        game.getCurrentPlayer().getPlayerBoard().getHandPlayer().decrementAmmo(cost.get(0),cost.get(1),cost.get(2));
    }

    /**
     * this methos serves to pay the powerUp with an other powerUp
     * @param powerUp power with which to pay
     * @return true if the action is possible
     */
    public boolean paymentPowerUp(int powerUp){
        boolean valueReturn;
        if(game.getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerPowerUps().get(powerUp)!=null){
            game.getCurrentPlayer().getPlayerBoard().getHandPlayer().removePowerUp(powerUp);
            valueReturn=true;
        }
        else
            valueReturn=false;
        return  valueReturn;
    }

    /**
     * this methos serves to pay the powerUp
     * @param ammo indicates the color of ammo to be removed
     * @return true if the action is possible
     */
    public boolean paymentPowerUp(String ammo){
        //r,y,b
        boolean valueReturn=false;
        if(ammo.equals("r") && game.getCurrentPlayer().getPlayerBoard().getHandPlayer().getAmmoRYB()[0]!=0 ){
            game.getCurrentPlayer().getPlayerBoard().getHandPlayer().decrementAmmo(1,0,0);
            valueReturn=true;
        }
        else if(ammo.equals("y") && game.getCurrentPlayer().getPlayerBoard().getHandPlayer().getAmmoRYB()[1]!=0 ){
            game.getCurrentPlayer().getPlayerBoard().getHandPlayer().decrementAmmo(0,1,0);
            valueReturn=true;
        }
        else if(ammo.equals("b") && game.getCurrentPlayer().getPlayerBoard().getHandPlayer().getAmmoRYB()[2]!=0 ){
            game.getCurrentPlayer().getPlayerBoard().getHandPlayer().decrementAmmo(0,0,1);
            valueReturn=true;
        }
        return valueReturn;
    }
}

