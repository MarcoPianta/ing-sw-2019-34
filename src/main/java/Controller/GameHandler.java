package Controller;

import Model.*;
import network.Server.GameLobby;
import network.messages.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class GameHandler {
    private Game game;
    private FinalTurnHandler finalTurnHandler;
    private TurnHandler turnHandler;
    private Player playerValid;// save player's position for action shot's valid ????
    private ActionValidController actionValidController;
    private PaymentController paymentController;
    private GameLobby gameLobby;
    private boolean actionAdrenalineDone;

    public void setActionAdrenalineDone(boolean actionAdrenalineDone) {
        this.actionAdrenalineDone = actionAdrenalineDone;
    }

    public boolean isActionAdrenalineDone() {
        return actionAdrenalineDone;
    }

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
            game.addPlayer(new Player(players.get(i),colors[i]));
            i++;
        }
        finalTurnHandler=new FinalTurnHandler(this);
        turnHandler=new TurnHandler(this);
        paymentController=new PaymentController(this);
        getGame().chooseFirstPlayer();
        playerValid=game.getCurrentPlayer();
        this.actionValidController = new ActionValidController(this);
        this.gameLobby=gameLobby;
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

    /**
     * this method fill the spawn square and normal square at the start of the game
     */
    public void  fillSquare(){
        int i=0;
        int j;
        while((i<game.getMap().getRooms().size())){
            j=0;
            while((j<game.getMap().getRooms().get(i).getNormalSquares().size())){
                if(game.getMap().getRooms().get(i).getNormalSquares().get(j).isSpawn()){
                    for(int w=0;w<3;w++){
                        game.getMap().getRooms().get(i).getNormalSquares().get(j).setItems(game.getDeckCollector().getCardWeaponDrawer().draw());
                        for(Player p: game.getPlayers()){
                            gameLobby.send(new UpdateClient(p.getPlayerID(),game.getMap().getRooms().get(i).getNormalSquares().get(j).getId(),game.getMap().getRooms().get(i).getNormalSquares().get(j).getWeapons().get(w),w));
                        }
                    }
                }
                else{
                    CardAmmo ammo = game.getDeckCollector().getCardAmmoDrawer().draw();
                    game.getMap().getRooms().get(i).getNormalSquares().get(j).setItems(ammo);
                    for(Player p: game.getPlayers()){
                        gameLobby.send(new UpdateClient(p.getPlayerID(),game.getMap().getRooms().get(i).getNormalSquares().get(j).getId(), ammo));
                    }
                }
                j++;
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
     */
    public void firstPartAction(ReceiveTargetSquare message){
        if(message.getType().equals("shoot"))
            receiveShoot(message);
        else if(message.getType().equals("grab"))
            receiveGrab(message);
        else
            receiveMove(message);
    }

    /**
     * this method  verified the first action shoot that the player can do, in basic adrenaline action and final turn
     * @param message  indicates only the token for updateClient
     */
    private void receiveShoot(ReceiveTargetSquare message){
        if(!getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(message.getPosWeapon()).isCharge())
            gameLobby.send(new UpdateClient(message.getToken(),"the weapon in not charge "));
        else{
            if(game.getCurrentPlayer().getPlayerBoard().getHealthPlayer().getAdrenalineAction()==2
                    &&!getGame().getDeadRoute().isFinalTurn() && !actionAdrenalineDone)
                gameLobby.send(new UpdateClient(message.getToken(),new Move(getGame().getCurrentPlayer(),null,1).reachableSquare()));
            else if((getGame().getCurrentPlayer().getPlayerBoard().getHealthPlayer().getAdrenalineAction()==0
                    ||getGame().getCurrentPlayer().getPlayerBoard().getHealthPlayer().getAdrenalineAction()==1 || actionAdrenalineDone)
                    &&!getGame().getDeadRoute().isFinalTurn()) {

                actionAdrenalineDone = false;
                Effect effect = game.getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(message.getPosWeapon()).getEffects().get(getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(message.getPosWeapon()).getActionSequences().indexOf(message.getPosEffect()));
                String actionSequence = effect.getActionSequence();
                if (actionSequence.charAt(0) == 'p') {
                    System.out.println("Nella receiveShoot ho un "+actionSequence.charAt(0));

                    ArrayList<Colors> targetToken = new ArrayList<>();
                    for (Player target: new Shoot(effect, game.getCurrentPlayer(), null).targetablePlayer()) {
                        targetToken.add(target.getColor());
                    }
                    gameLobby.send(new ShootRequestp(message.getToken(), effect.getTargetNumber(), targetToken));

                } else if (actionSequence.charAt(0) == 's') {
                    System.out.println("Nella receiveShoot ho un "+actionSequence.charAt(0));
                    ArrayList<String> targetID = new ArrayList<>();
                    for (NormalSquare target: new Shoot(effect, game.getCurrentPlayer(), null).reachableSquare()) {
                        targetID.add(target.getId());
                    }
                    gameLobby.send(new ShootRequests(message.getToken(), targetID));

                } else if (actionSequence.charAt(0) == 'r') {
                    System.out.println("Nella receiveShoot ho un "+actionSequence.charAt(0));
                    ArrayList<String> targetID = new ArrayList<>();
                    for (NormalSquare target: new Shoot(effect, game.getCurrentPlayer(), null).reachableRoom()) {
                        targetID.add(target.getId());
                    }
                    gameLobby.send(new ShootRequestr(message.getToken(), targetID));

                }else if (actionSequence.charAt(0) == 'M') {
                    System.out.println("Nella receiveShoot ho un "+actionSequence.charAt(0));
                    gameLobby.send(new UpdateClient(message.getToken(), new Move(getGame().getCurrentPlayer(), null, effect.getMyMove()).reachableSquare()));

                }
            }
            else if (getGame().getDeadRoute().isFinalTurn() && getFinalTurnHandler().isAlreadyFirstPlayer())
                gameLobby.send(new UpdateClient(message.getToken(),new Move(getGame().getCurrentPlayer(),null,2).reachableSquare()));
            else if (getGame().getDeadRoute().isFinalTurn() && !getFinalTurnHandler().isAlreadyFirstPlayer())
                gameLobby.send(new UpdateClient(message.getToken(),new Move(getGame().getCurrentPlayer(),null,1).reachableSquare()));


            //verified if there is sight power up
            boolean isFind=false;
            for(int i=0;i< game.getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerPowerUps().size()&& !isFind;i++){
                System.out.println("trrreydrey hai power up??");
                System.out.println(game.getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerPowerUps().get(i).getWhen());
                if(game.getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerPowerUps().get(i).getWhen().equals("get")
                        && game.getCurrentPlayer().isValidCost(game.getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(message.getPosWeapon()).getEffects().get(message.getPosEffect()).getBonusCost(),true)){
                    isFind=true;

                    getGameLobby().canUseScoop(game.getCurrentPlayer().getPlayerID());
                }
           }
        }
    }

    /**
     * this method  verified the first action grab that the player can do, in basic adrenaline action and final turn
     * @param   message  indicates only the token for updateClient
     */
    private void receiveGrab(ReceiveTargetSquare message){
        System.out.println(game.getCurrentPlayer().getPlayerBoard().getHealthPlayer().getAdrenalineAction());
        if(game.getCurrentPlayer().getPlayerBoard().getHealthPlayer().getAdrenalineAction()==0
                &&!game.getDeadRoute().isFinalTurn())
            gameLobby.send(new UpdateClient(message.getToken(),new Move(getGame().getCurrentPlayer(),null,1).reachableSquare()));
        else if((getGame().getCurrentPlayer().getPlayerBoard().getHealthPlayer().getAdrenalineAction()==1
                ||game.getCurrentPlayer().getPlayerBoard().getHealthPlayer().getAdrenalineAction()==2 ) &&!game.getDeadRoute().isFinalTurn())
            gameLobby.send(new UpdateClient(message.getToken(),new Move(getGame().getCurrentPlayer(),null,2).reachableSquare()));
        else if (game.getDeadRoute().isFinalTurn() && getFinalTurnHandler().isAlreadyFirstPlayer())
            gameLobby.send(new UpdateClient(message.getToken(),new Move(getGame().getCurrentPlayer(),null,3).reachableSquare()));
        else if (game.getDeadRoute().isFinalTurn() && !getFinalTurnHandler().isAlreadyFirstPlayer())
            gameLobby.send(new UpdateClient(message.getToken(),new Move(getGame().getCurrentPlayer(),null,2).reachableSquare()));
    }

    /**
     * this method  verified the first action move that the player can do, in basic adrenaline action and final turn
     * @param   message  indicates only the token for updateClient
     */
    private void receiveMove(ReceiveTargetSquare message){
        //case  final turn e Already firstPlayer
        if((game.getCurrentPlayer().getPlayerBoard().getHealthPlayer().getAdrenalineAction()==1
                ||game.getCurrentPlayer().getPlayerBoard().getHealthPlayer().getAdrenalineAction()==2 ||game.getCurrentPlayer().getPlayerBoard().getHealthPlayer().getAdrenalineAction()==0)
                &&!game.getDeadRoute().isFinalTurn())
            gameLobby.send(new UpdateClient(message.getToken(),new Move(getGame().getCurrentPlayer(),null,3).reachableSquare()));
        else if (game.getDeadRoute().isFinalTurn() && !getFinalTurnHandler().isAlreadyFirstPlayer())
            gameLobby.send(new UpdateClient(message.getToken(),new Move(getGame().getCurrentPlayer(),null,4).reachableSquare()));
    }

    /**
     * this method values who are/is win the game
     */
    public void winner(){
        int massimo=0;
        List<Player> winnerList=new ArrayList<>();
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
        List<Integer> winners = winnerList.stream().map(Player::getPlayerID).collect(toList());
        gameLobby.endGame(winners);
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
     * this method modified the winner list
     * @param player
     * @param winnerList
     */
    private void modifiedWinnerList(Player player, List<Player> winnerList){
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


}

