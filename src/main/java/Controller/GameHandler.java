package Controller;

import Model.*;
import network.Server.GameLobby;
import network.messages.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class GameHandler {
    private Game game;
    private FinalTurnHandler finalTurnHandler;
    private TurnHandler turnHandler;
    private Player playerValid;// save player's position for action shot's valid ????
    private ActionValidController actionValidController;
    private PaymentController paymentController;
    private GameLobby gameLobby;


    /**
     * The constructor of gameHandler
     * @param n the numbers of skulls
     * @param players the list of tokens' player
     * @param file  the map's file
     * @throws FileNotFoundException
     */
    public GameHandler(int n, List<Integer> players, String file, GameLobby gameLobby) throws FileNotFoundException {
        this.game = new Game(n,file);
        Colors[] colors=Colors.values();
        int i=0;
        while(i<players.size()){
            game.addPlayer(new Player(players.get(0),colors[i]));
            i++;
        }
        finalTurnHandler=new FinalTurnHandler(this);
        turnHandler=new TurnHandler(this);
        paymentController=new PaymentController(this);
        getGame().chooseFirstPlayer();
        playerValid=game.getCurrentPlayer();
        getTurnHandler().start();
        this.actionValidController = new ActionValidController(this);
        this.gameLobby=gameLobby;
        fillSquare();
        getTurnHandler().start();

    }

    public PaymentController getPaymentController() {
        return paymentController;
    }

    public GameLobby getGameLobby() {
        return gameLobby;
    }

    public void setPlayerValid (Player playerValid) {
        this.playerValid = playerValid;
    }

    public Player getPlayerValid() {
        return playerValid;
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

    public void  fillSquare(){
        int i=0;
        int j=0;
        while((i<game.getMap().getRooms().size())){
            j=0;
            while((j<game.getMap().getRooms().get(i).getNormalSquares().size())){
                if(game.getMap().getRooms().get(i).getNormalSquares().get(j).isSpawn()){
                    for(i=0;i<3;i++){
                        game.getMap().getRooms().get(i).getNormalSquares().get(j).setItems(game.getDeckCollector().getCardWeaponDrawer().draw());
                        for(Player p: game.getPlayers()){
                            gameLobby.send(p.getPlayerID(),game.getMap().getRooms().get(i).getNormalSquares().get(j).getId(),game.getMap().getRooms().get(i).getNormalSquares().get(j).getWeapons().get(i));
                        }
                    }


                }
                else{
                    game.getMap().getRooms().get(i).getNormalSquares().get(j).setItems(game.getDeckCollector().getCardAmmoDrawer().draw());
                    for(Player p: game.getPlayers()){
                    gameLobby.send(p.getPlayerID(),game.getMap().getRooms().get(i).getNormalSquares().get(j).getId(),game.getMap().getRooms().get(i).getNormalSquares().get(j).getItem());
                    }
                }
            }
            i++;
        }

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
                valueReturn=turnHandler.actionAdrenaline012(message);
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
        winner();
    }

    /**
     * this method calculates the possible targets
     * @param message contains the effect
     * @return a list of possible targets
     */
    public ArrayList<Player> receiveTarget(PossibleTargetShot message){
        ArrayList<Player> targets;
        targets=(ArrayList<Player>) new Shoot(message.getEffect(),getGame().getCurrentPlayer(), null, null, false).targetablePlayer();
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
            messageReturn= new UpdateClient(message.getToken(),(ArrayList<Player>)new  Shoot(getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(message.getPosWepon()).getEffects().get(message.getPosEffect()),getGame().getCurrentPlayer(), null, null, false).targetablePlayer());
        else if (getGame().getDeadRoute().isFinalTurn() && getFinalTurnHandler().isAlreadyFirstPlayer())
            messageReturn=new UpdateClient(message.getToken(),new Move(getGame().getCurrentPlayer(),null,2).reachableSquare());
        else if (getGame().getDeadRoute().isFinalTurn() && !getFinalTurnHandler().isAlreadyFirstPlayer())
            messageReturn=new UpdateClient(message.getToken(),new Move(getGame().getCurrentPlayer(),null,1).reachableSquare());
        //verified if there is sight power up
        boolean isFind=false;
        for(int i=0;i<3 && !isFind;i++){
            if(game.getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerPowerUps().get(i).getWhen().equals("get")
                    && game.getCurrentPlayer().isValidCost(game.getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(message.getPosWepon()).getEffects().get(message.getPosEffect()).getBonusCost(),true)){
                isFind=true;
                getGameLobby().canUseScoop(game.getCurrentPlayer().getPlayerID());
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
     */
    public void winner(){
        int massimo=0;
        ArrayList<Player> winnerList=new ArrayList<>();
        for(Player player:getGame().getPlayers()){
            if(player.getPlayerBoard().getPoints()>massimo) {
                while(!winnerList.isEmpty()){
                    winnerList.remove(winnerList.size()-1);
                }
                massimo=player.getPlayerBoard().getPoints();
                winnerList.add(player);
            }
            else if(player.getPlayerBoard().getPoints()==massimo && massimo!=0) { //if the firstPlayer do 0 points caused index bound exception
                modifiedWinnerList(player, winnerList);
                massimo=winnerList.get(0).getPlayerBoard().getPoints(); //if winnerList is a new list
            }
        }
        /*for(Player p:game.getPlayers()){
            if(winnerList.contains(p))
                gameLobby.send(new WinnerMessage(p.getPlayerID(),true));
            else
                gameLobby.send(new WinnerMessage(p.getPlayerID(),false));
        }*/
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
            while(!winnerList.isEmpty())
                winnerList.remove(winnerList.size()-1);
            winnerList.add(player);
        }
        else if(countPlayer(player)==countPlayer(winnerList.get(0)))
            winnerList.add(player);
    }

    public ActionValidController getActionValidController() {
        return actionValidController;
    }

    /**
     * this method serves to pay the effect o reload, with powerUp
     * @param cost    is the cost to pay
     * @param powerUp the list of powerUp with which to pay
     */

}

