package Controller;

import Model.*;
import network.messages.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class TurnHandler {
    private StateMachineEnumerationTurn nextState;
    private EndTurnChecks endTurnChecks;
    private GameHandler gameHandler;
    private ActionValidController actionValidController;
    private PaymentController paymentController;

    public TurnHandler(GameHandler gameHandler){
        this.gameHandler=gameHandler;
        this.endTurnChecks=new EndTurnChecks();
        this.nextState=null;
        this.actionValidController = gameHandler.getActionValidController();
        this.paymentController = gameHandler.getPaymentController();
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

    /**
     * this method is the start of turn, and there is the first action of the player's current
     */
    public void start(){

        gameHandler.getGame().getCurrentPlayer().setState(StateMachineEnumerationTurn.START);
        ArrayList<Player> deadPlayerCopy=new ArrayList<>(gameHandler.getGame().getDeadPlayer());
        for(Player p:deadPlayerCopy)
            gameHandler.getGame().getDeadPlayer().remove(p);
        gameHandler.getGameLobby().startTurn(gameHandler.getGame().getCurrentPlayer().getPlayerID());


        if(gameHandler.getGame().getCurrentPlayer().getPosition()==null && gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getMaxReward()==8) {
            gameHandler.getGame().getCurrentPlayer().spawn(1);//first spawn
            gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().addAmmo(1, 1, 1);
            gameHandler.getGame().getCurrentPlayer().spawn(1);

            gameHandler.getGameLobby().send(new UpdateClient(gameHandler.getGame().getCurrentPlayer().getPlayerID(),gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getAmmoRYB()[0],gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getAmmoRYB()[1],gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getAmmoRYB()[2],gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons(), new ArrayList<>(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerPowerUps())));
            gameHandler.getGameLobby().send(new UpdateClient(gameHandler.getGame().getCurrentPlayer().getPlayerID()));
        }
       /* setNextState(StateMachineEnumerationTurn.ACTION1);
        // remove playerValid??
        gameHandler.setPlayerValid(gameHandler.getGame().getCurrentPlayer());*/
        //gameHandler.getGameLobby().send(new UpdateClient(gameHandler.getGame().getCurrentPlayer().getPlayerID(),gameHandler.getGame().getCurrentPlayer().getPosition()));
        gameHandler.getGame().getCurrentPlayer().setState(StateMachineEnumerationTurn.ACTION1);

    }

    /**
     *
     * this method set the state after a action
     */
    public void endAction(){
        if(gameHandler.getGame().getCurrentPlayer().getState()==StateMachineEnumerationTurn.ACTION1)
            gameHandler.getGame().getCurrentPlayer().setState(StateMachineEnumerationTurn.ACTION2);
        else if(gameHandler.getGame().getCurrentPlayer().getState()==StateMachineEnumerationTurn.ACTION2)
            gameHandler.getGame().getCurrentPlayer().setState(StateMachineEnumerationTurn.RELOAD);
    }

    /*public boolean actionState(Message message){
        boolean valueReturn;
        if (gameHandler.getGame().getCurrentPlayer().getState()==StateMachineEnumerationTurn.START){
            gameHandler.getGame().getCurrentPlayer().setState(StateMachineEnumerationTurn.ACTION1);
            setNextState(StateMachineEnumerationTurn.ACTION2);
        }
        else{
            gameHandler.getGame().getCurrentPlayer().setState(StateMachineEnumerationTurn.ACTION2);
            setNextState(StateMachineEnumerationTurn.RELOAD);
        }

        valueReturn=actionAdrenaline012(message);
        return  valueReturn;

    }*/

    /**
     * this method is a switch of the three principal action, and execute the move action
     * @param message indicate the type of actiom
     * @return true if the action has been execute
     */
    public boolean actionAdrenaline012(Message message){
        boolean valueReturn;
        if(message.getActionType()==ActionType.MOVE){
            MoveMessage newMessage=(MoveMessage)message;
            valueReturn= new Move(newMessage.getPlayerTarget(),newMessage.getNewSquare(), 3).execute();
            FinalTurnHandler.updateClients(valueReturn, newMessage, gameHandler);
        }
        else if(message.getActionType()==ActionType.SHOT){
            Shot newMessage=(Shot)message;
            if(newMessage.getPowerUp()!= -1){
                usePowerUp(message);
            }
            valueReturn= actionShot(newMessage);
        }
        else
            valueReturn=actionGrab(message);
        return valueReturn;
    }

    /*private boolean actionAdrenaline12(Message message){
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
            valueReturn=actionGrab(message);
        return valueReturn;
    }*/

    /*private boolean actionAdrenaline2(Message message){
        boolean valueReturn=false;
        if(message.getActionType()==ActionType.MOVE){
            MoveMessage newMessage=(MoveMessage)message;
            valueReturn= new Move(gameHandler.getGame().getCurrentPlayer(),newMessage.getNewSquare(), 3).execute();}
        else if(message.getActionType()==ActionType.SHOT){
            Shot newMessage=(Shot)message;
            valueReturn= actionShot(newMessage);
        }
        else
            valueReturn=actionGrab(message);
        return valueReturn;
    }*/

    /**
     * this method  implements action shoot
     * @param message is the shot message
     * @return true if the action has been execute
     */
    protected boolean actionShot(Shot message){
        boolean valueReturn;
        if(message.getSquare()==null && message.getRoom()==null&& message.getPowerUp()==-1) {
            valueReturn = new Shoot(getGameHandler().getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(message.getWeapon()).getEffects().get(message.getPosEffect()), gameHandler.getGame().getCurrentPlayer(), message.getTargets(), null, false).execute();
            if(valueReturn){
                for(Player p:message.getTargets()){
                    gameHandler.getGameLobby().send(new UpdateClient(p.getPlayerID(),new ArrayList<>(p.getPlayerBoard().getHealthPlayer().getDamageBar()),new ArrayList<>(p.getPlayerBoard().getHealthPlayer().getMark())));
                    gameHandler.getGameLobby().send(new UpdateClient(p.getPlayerID(),"you have been attacked by"+gameHandler.getGame().getCurrentPlayer().getColor()));
                    gameHandler.getGameLobby().getClients().forEach(x -> gameHandler.getGameLobby().send(new UpdateClient(x, p.getColor(), new ArrayList<>(p.getPlayerBoard().getHealthPlayer().getDamageBar()),new ArrayList<>(p.getPlayerBoard().getHealthPlayer().getMark()))));
                }
            }
        }
        else if(message.getSquare()==null && message.getTargets()==null) {
            valueReturn = new Shoot(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(message.getWeapon()).getEffects().get(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(message.getWeapon()).getActionSequences().indexOf(message.getPosEffect())),
                    gameHandler.getGame().getCurrentPlayer(),
                    message.getRoom().getColor()).execute();
            ArrayList<Player> players=new ArrayList<>();
            if (valueReturn){
                for(Player p:gameHandler.getGame().getPlayers()) {
                    if (p != gameHandler.getGame().getCurrentPlayer() && p.getPosition().getColor().getAbbreviation().equals(message.getRoom().getColor().getAbbreviation()))
                        players.add(p);
                }
                for(Player p:players){
                    gameHandler.getGameLobby().send(new UpdateClient(p.getPlayerID(),new ArrayList<>(p.getPlayerBoard().getHealthPlayer().getDamageBar()),new ArrayList<>(p.getPlayerBoard().getHealthPlayer().getMark())));
                    gameHandler.getGameLobby().send(new UpdateClient(p.getPlayerID(),"you have been attacked by"+gameHandler.getGame().getCurrentPlayer().getColor()));
                    gameHandler.getGameLobby().getClients().forEach(x -> gameHandler.getGameLobby().send(new UpdateClient(x, p.getColor(), new ArrayList<>(p.getPlayerBoard().getHealthPlayer().getDamageBar()),new ArrayList<>(p.getPlayerBoard().getHealthPlayer().getMark()))));
                }
            }
        }
        else {
            valueReturn = new Shoot(getGameHandler().getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(message.getWeapon()).getEffects()
                                    .get(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(message.getWeapon()).getActionSequences().indexOf(message.getPosEffect())), gameHandler.getGame().getCurrentPlayer(), null, message.getSquare(), true).execute();
            if (valueReturn){
                ArrayList<Player> players=new ArrayList<>();
                for(Player p:gameHandler.getGame().getPlayers()){
                    if(p!=gameHandler.getGame().getCurrentPlayer() &&(p.getPosition()==message.getSquare().get(0)||p.getPosition()==message.getSquare().get(1)))
                        players.add(p);
                }
                for(Player p:players){
                    gameHandler.getGameLobby().send(new UpdateClient(p.getPlayerID(),new ArrayList<>(p.getPlayerBoard().getHealthPlayer().getDamageBar()),new ArrayList<>(p.getPlayerBoard().getHealthPlayer().getMark())));
                    gameHandler.getGameLobby().send(new UpdateClient(p.getPlayerID(),"you have been attacked by"+gameHandler.getGame().getCurrentPlayer().getColor()));
                    gameHandler.getGameLobby().getClients().forEach(x -> gameHandler.getGameLobby().send(new UpdateClient(x, p.getColor(), new ArrayList<>(p.getPlayerBoard().getHealthPlayer().getDamageBar()),new ArrayList<>(p.getPlayerBoard().getHealthPlayer().getMark()))));
                }

            }
        }
        //use scoop
        if(valueReturn && message.getPowerUp()!=-1 && gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerPowerUps().get(message.getPowerUp()).getWhen().equals("get")){
            usePowerUp(message);
        }
        return valueReturn;
    }

    //creare messaggio diverso per weapon =3, stesso messaggio ma costruttore diverso, se ho 3 armi grabbo normalmente  e elimino lintero per rimuovere
    /**
     * this method  implements action grab
     * @param message is the shot message
     * @return true if the action has been execute
     */
    protected boolean actionGrab(Message message){
        boolean valueReturn=false;
        if(message.getActionType()==ActionType.GRABWEAPON ){
            GrabWeapon newMessage=(GrabWeapon) message;
            valueReturn= new Grab(gameHandler.getGame().getCurrentPlayer(),gameHandler.getGame().getCurrentPlayer().getPosition().getWeapons().get(newMessage.getPositionWeapon())).execute();
            if(valueReturn){
                try {
                    gameHandler.getGameLobby().send(new UpdateClient(gameHandler.getGame().getCurrentPlayer().getPlayerID(), gameHandler.getGame().getCurrentPlayer().getPosition().getId(), new CardWeapon("void"), newMessage.getPositionWeapon()));
                }catch(FileNotFoundException exception){}
                //weapon charge and update
                gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().size()-1).setCharge(true);
                gameHandler.getGameLobby().send(new UpdateClient(gameHandler.getGame().getCurrentPlayer().getPlayerID(),gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getAmmoRYB()[0],gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getAmmoRYB()[1],gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getAmmoRYB()[2],new ArrayList<>(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons()), new ArrayList<>(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerPowerUps())));
            }


        }
        else /*if(message.getActionType()==ActionType.GRABAMMO)*/{
            if(gameHandler.getGame().getCurrentPlayer().getPosition().getItem().isWithPowerUp())
                valueReturn= new Grab(gameHandler.getGame().getCurrentPlayer(), (CardNotOnlyAmmo) gameHandler.getGame().getCurrentPlayer().getPosition().getItem()).execute();
            else
                valueReturn= new Grab(gameHandler.getGame().getCurrentPlayer(), (CardOnlyAmmo) gameHandler.getGame().getCurrentPlayer().getPosition().getItem()).execute();
            if(valueReturn){
                endTurnChecks.getEmptySquares().add(gameHandler.getGame().getCurrentPlayer().getPosition());
                try {
                    gameHandler.getGameLobby().send(new UpdateClient(gameHandler.getGame().getCurrentPlayer().getPlayerID(), gameHandler.getGame().getCurrentPlayer().getPosition().getId(), new CardOnlyAmmo("back")));
                }catch(FileNotFoundException exception){}

                gameHandler.getGameLobby().send(new UpdateClient(gameHandler.getGame().getCurrentPlayer().getPlayerID(),gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getAmmoRYB()[0],gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getAmmoRYB()[1],gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getAmmoRYB()[2],new ArrayList<>(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons()), new ArrayList<>(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerPowerUps())));

            }
        }/*
        else{
            valueReturn= new Grab(gameHandler.getGame().getCurrentPlayer(), (CardNotOnlyAmmo) gameHandler.getGame().getCurrentPlayer().getPosition().getItem()).execute();
            if(valueReturn){
                endTurnChecks.getEmptySquares().add(gameHandler.getGame().getCurrentPlayer().getPosition());
                gameHandler.getGameLobby().send(new UpdateClient(gameHandler.getGame().getCurrentPlayer().getPlayerID(),gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer()));
            }

        }
*/
        if(valueReturn && gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().size()==4){
            gameHandler.getGameLobby().send(new SubstituteWeapon(gameHandler.getGame().getCurrentPlayer().getPlayerID()));
        }
        else
            endTurnChecks.getEmptySquares().add(gameHandler.getGame().getCurrentPlayer().getPosition());

        return valueReturn;
    }

    private boolean powerUpIsValid(Message message){
        boolean valueReturn=false;
        if(message.getActionType()==ActionType.USEPOWERUP){
            UsePowerUp newMessage=(UsePowerUp)message;
            if((((UsePowerUp) message).getUser().getPlayerBoard().getHandPlayer().getPlayerPowerUps().get(newMessage.getPowerUp()).getWhen().equals("during")
                    &&(newMessage.getUser()==gameHandler.getGame().getCurrentPlayer())
                    &&(gameHandler.getGame().getCurrentPlayer().getState()!=StateMachineEnumerationTurn.WAIT)&&(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerPowerUps().get(newMessage.getPowerUp()).getOtherMove()==0
                    &&(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerPowerUps().get(newMessage.getPowerUp()).getMyMove()==-1)))
                    || //teleporter
                    (newMessage.getUser().getPlayerBoard().getHandPlayer().getPlayerPowerUps().get(newMessage.getPowerUp()).getWhen().equals("deal")
                            &&(newMessage.getUser()!=gameHandler.getGame().getCurrentPlayer()))
                    ||//tagback
                    (gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerPowerUps().get(newMessage.getPowerUp()).getWhen().equals("during")
                    &&(newMessage.getUser()==gameHandler.getGame().getCurrentPlayer())
                    &&(gameHandler.getGame().getCurrentPlayer().getState()!=StateMachineEnumerationTurn.WAIT)&&gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerPowerUps().get(newMessage.getPowerUp()).getOtherMove()!=0
                    && new Move(newMessage.getTarget(),newMessage.getSquare(),2).isValid()
                            && gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerPowerUps().get(newMessage.getPowerUp()).getOtherMove()==2 &&
                            (newMessage.getTarget().getPosition().getId().charAt(0)==newMessage.getSquare().getId().charAt(0)||newMessage.getTarget().getPosition().getId().charAt(2)==newMessage.getSquare().getId().charAt(2 ))))
            valueReturn=true;
        }
        else if(message.getActionType()==ActionType.SHOT && gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerPowerUps().get(((Shot) message).getPowerUp()).getWhen().equals("get")
                    &&((gameHandler.getGame().getCurrentPlayer().getState()==StateMachineEnumerationTurn.ACTION1)
                    ||(gameHandler.getGame().getCurrentPlayer().getState()==StateMachineEnumerationTurn.ACTION2))
                    && gameHandler.getGame().getCurrentPlayer().isValidCost(1)){
                //target powerUp is one of the shoot's target
                for(Player p:((Shot) message).getTargets()){
                    if(p==((Shot) message).getTargetPowerUp())
                        valueReturn=true;
                }
        }
        return valueReturn;
    }
    /**
     * this method  implements  the use of power up
     * @param message is the use powerUp message
     * @return true if the action has been execute
     */
    public boolean usePowerUp(Message message){
        boolean valueReturn=false;
        if(message.getActionType()==ActionType.SHOT && powerUpIsValid(message)){
            Shot newMessage=(Shot) message;
            gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().usePowerUp(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerPowerUps().get(((Shot) message).getPowerUp()),newMessage.getTargetPowerUp(),null,newMessage.getPowerUp());
            gameHandler.getGameLobby().send(new UpdateClient(newMessage.getTargetPowerUp().getPlayerID(),newMessage.getTargetPowerUp().getPlayerBoard().getHealthPlayer().getDamageBar(),newMessage.getTargetPowerUp().getPlayerBoard().getHealthPlayer().getMark()));
            valueReturn=true;
        }
        else if(powerUpIsValid(message)){
            UsePowerUp newMessage=(UsePowerUp) message;
            CardPowerUp powerUp=convertedPlayer(newMessage.getUser()).getPlayerBoard().getHandPlayer().getPlayerPowerUps().get(newMessage.getPowerUp());
            valueReturn=true;
            //update of powerUp
            if(newMessage.getUser()!=gameHandler.getGame().getCurrentPlayer()) {

                gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHealthPlayer().addMark(newMessage.getUser(),1);
                newMessage.getUser().getPlayerBoard().getHandPlayer().removePowerUp(newMessage.getPowerUp());

                gameHandler.getGameLobby().send(new UpdateClient(newMessage.getUser().getPlayerID(),newMessage.getUser().getPlayerBoard().getHandPlayer().getAmmoRYB()[0],newMessage.getUser().getPlayerBoard().getHandPlayer().getAmmoRYB()[1],newMessage.getUser().getPlayerBoard().getHandPlayer().getAmmoRYB()[2],new ArrayList<>(newMessage.getUser().getPlayerBoard().getHandPlayer().getPlayerWeapons()), new ArrayList<>(newMessage.getUser().getPlayerBoard().getHandPlayer().getPlayerPowerUps())));

                gameHandler.getGameLobby().send(new UpdateClient(gameHandler.getGame().getCurrentPlayer().getPlayerID(),new ArrayList<>(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHealthPlayer().getDamageBar()), new ArrayList<>(getGameHandler().getGame().getCurrentPlayer().getPlayerBoard().getHealthPlayer().getMark())));
                gameHandler.getGameLobby().send(new UpdateClient(newMessage.getTarget().getPlayerID(),newMessage.getUser().getColor()+"used grenade tag back"));

            }
            else if(newMessage.getUser()==gameHandler.getGame().getCurrentPlayer()&&
                    powerUp.getOtherMove()==0)//teleporter
            {gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().usePowerUp(powerUp,gameHandler.getGame().getCurrentPlayer(),newMessage.getSquare(),newMessage.getPowerUp());
                gameHandler.getGameLobby().send( new UpdateClient(newMessage.getUser().getPlayerID(),newMessage.getUser().getPosition()));
                gameHandler.getGameLobby().send(new UpdateClient(gameHandler.getGame().getCurrentPlayer().getPlayerID(),gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getAmmoRYB()[0],gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getAmmoRYB()[1],gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getAmmoRYB()[2],new ArrayList<>(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons()), new ArrayList<>(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerPowerUps())));

                gameHandler.getGameLobby().getClients()
                        .parallelStream().
                        filter(x -> (!x.equals(newMessage.getUser().getPlayerID()))).
                        forEach(x -> gameHandler.getGameLobby().send(new UpdateClient(x, newMessage.getUser().getColor(), newMessage.getUser().getPosition())));
            }
            else {//altro
                gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().usePowerUp(powerUp,convertedPlayer(newMessage.getTarget()),newMessage.getSquare(),newMessage.getPowerUp());
                gameHandler.getGameLobby().send(new UpdateClient(gameHandler.getGame().getCurrentPlayer().getPlayerID(),gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getAmmoRYB()[0],gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getAmmoRYB()[1],gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getAmmoRYB()[2],new ArrayList<>(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons()), new ArrayList<>(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerPowerUps())));
                gameHandler.getGameLobby().send(new UpdateClient(newMessage.getTarget().getPlayerID(), newMessage.getTarget().getPosition()));
                gameHandler.getGameLobby().getClients()
                        .parallelStream().
                        filter(x -> (!x.equals(newMessage.getTarget().getPlayerID()))).
                        forEach(x -> gameHandler.getGameLobby().send(new UpdateClient(x, newMessage.getTarget().getColor(), newMessage.getTarget().getPosition())));
            }
        }
        return valueReturn;

    }
    /**
     * this method  implements action reload
     * @param message is the reload message
     * @return true if the action has been execute
     */
    public boolean actionReload(ReloadMessage message){
        boolean valueReturn;
        //valueReturn = new Reload(gameHandler.getGame().getCurrentPlayer(), gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(message.getWeapon()), cost[0], cost[1], cost[2]).execute();
        gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(message.getWeapon()).setCharge(true);
        if(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(message.getWeapon()).isCharge())
            valueReturn=true;
        else
            valueReturn=false;
        if(valueReturn) {
            gameHandler.getGameLobby().send(new UpdateClient(gameHandler.getGame().getCurrentPlayer().getPlayerID(),gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getAmmoRYB()[0],gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getAmmoRYB()[1],gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getAmmoRYB()[2],new ArrayList<>(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons()), new ArrayList<>(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerPowerUps())));
        }
        return valueReturn;
    }

    public void endTurn(){
        gameHandler.getGame().getCurrentPlayer().setState(StateMachineEnumerationTurn.WAIT);
        gameHandler.getGame().incrementCurrentPlayer();
        setNextState(StateMachineEnumerationTurn.START);
        endTurnChecks.fillSquare(gameHandler.getGame());
        endTurnChecks.playerIsDead(gameHandler.getGame());
        endTurnChecks.isFinalTurn(gameHandler.getGame());
        start();

    }
    //for security
    private Player convertedPlayer(Player player){
        Player playerConverted=new Player(null,null);
        for(Player p:gameHandler.getGame().getPlayers()){
            if(p.getPlayerID().equals(player.getPlayerID()))
               playerConverted=p;
        }
        return playerConverted;
    }

    private ArrayList<Player> convertedPlayer(List<Player> players){
        ArrayList<Player> playerConverted=new ArrayList<>();
        for(Player player:players){
            for(Player p:gameHandler.getGame().getPlayers()){
                if(p.getPlayerID().equals(player.getPlayerID()))
                    playerConverted.add(p);
            }
        }
        return playerConverted;
    }


    class EndTurnChecks {
        private ArrayList<NormalSquare> emptySquares=new ArrayList<>();

        public List<NormalSquare> getEmptySquares() {
            return emptySquares;
        }

        public void fillSquare(Game game) {
            int i = emptySquares.size() - 1;
            while (!emptySquares.isEmpty()) {
                if (emptySquares.get(i).isSpawn()){
                    int pos=emptySquares.get(i).setItems(game.getDeckCollector().getCardWeaponDrawer().draw());
                    for(Player p: game.getPlayers()){
                        gameHandler.getGameLobby().send(new UpdateClient(p.getPlayerID(),emptySquares.get(i).getId(),((SpawnSquare)emptySquares.get(i)).getWeapons().get(pos),pos));
                        }
                }
                else{
                    CardAmmo ammo = game.getDeckCollector().getCardAmmoDrawer().draw();
                    emptySquares.get(i).setItems(ammo);
                    for(Player p: game.getPlayers()){
                        gameHandler.getGameLobby().send(new UpdateClient(p.getPlayerID(),emptySquares.get(i).getId(), ammo));
                    }
                }
                emptySquares.remove(i);
                i--;
            }
        }

        public void playerIsDead(Game game) {
            int i=game.getDeadPlayer().size()-1;
            while(i>=0) {
                game.getDeadPlayer().get(i).getPlayerBoard().getHealthPlayer().death();
                game.getDeadPlayer().get(i).spawn(1);//extract powerUp
                //update player
                gameHandler.getGameLobby().send(new UpdateClient(game.getDeadPlayer().get(i).getPlayerID(),gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getAmmoRYB()[0],gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getAmmoRYB()[1],gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getAmmoRYB()[2],new ArrayList<>(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons()), new ArrayList<>(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerPowerUps())));
                gameHandler.getGameLobby().send(new UpdateClient(game.getDeadPlayer().get(i).getPlayerID()));

                gameHandler.getGameLobby().getClients().forEach(x -> gameHandler.getGameLobby().send(new UpdateClient(x, gameHandler.getGameLobby().getPlayers().get(x).getPlayerBoard().getPoints())));
                //TODO problema !!!!!!!!!!!!!!!!!!-----------------!!!!!!!!!!!!!!!!!!! manda 2 volte l'update della end
                //gameHandler.getGameLobby().send(new UpdateClient(gameHandler.getGame().getCurrentPlayer().getPlayerID(),gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getAmmoRYB()[0],gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getAmmoRYB()[1],gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getAmmoRYB()[2],gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons(),gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerPowerUps()));
                i--;

            }

        }

        public void isFinalTurn(Game game){
            if(game.getDeadRoute().isFinalTurn()){
                game.getDeadRoute().setFinalTurnPlayer();
                for(Player p:game.getPlayers()) {
                    gameHandler.getGameLobby().send(new FinalTurnMessage(p.getPlayerID(), p.getPlayerID().equals(gameHandler.getGame().getFirstPlayer().getPlayerID())));
                    gameHandler.getGameLobby().send(new UpdateClient(p.getPlayerID(), "Is final Turn, the rule of the action have changed"));
                    gameHandler.getGameLobby().send(new UpdateClient(p.getPlayerID(), p.getPlayerBoard().getHandPlayer().getAmmoRYB()[0], p.getPlayerBoard().getHandPlayer().getAmmoRYB()[1], p.getPlayerBoard().getHandPlayer().getAmmoRYB()[2], new ArrayList<>(p.getPlayerBoard().getHandPlayer().getPlayerWeapons()), new ArrayList<>(p.getPlayerBoard().getHandPlayer().getPlayerPowerUps())));
                    gameHandler.getGameLobby().send(new UpdateClient(p.getPlayerID(), p.getPlayerBoard().getHealthPlayer().getDamageBar(), p.getPlayerBoard().getHealthPlayer().getMark()));
                }
                getGameHandler().getFinalTurnHandler().setFirstFinalTurnPlayer(getGameHandler().getGame().getCurrentPlayer());
                if(getGameHandler().getGame().getCurrentPlayer()==getGameHandler().getGame().getFirstPlayer())
                    getGameHandler().getFinalTurnHandler().setAlreadyFirsPlayer(true);
                //update plance

                gameHandler.getGameLobby().getClients().forEach(x -> gameHandler.getGameLobby().send(new UpdateClient(x,gameHandler.getGameLobby().getPlayers().get(x).getPlayerBoard().getHandPlayer().getAmmoRYB()[0],gameHandler.getGameLobby().getPlayers().get(x).getPlayerBoard().getHandPlayer().getAmmoRYB()[1],gameHandler.getGameLobby().getPlayers().get(x).getPlayerBoard().getHandPlayer().getAmmoRYB()[2],new ArrayList<>(gameHandler.getGameLobby().getPlayers().get(x).getPlayerBoard().getHandPlayer().getPlayerWeapons()),new ArrayList<>(gameHandler.getGameLobby().getPlayers().get(x).getPlayerBoard().getHandPlayer().getPlayerPowerUps()))));

                gameHandler.getGameLobby().getClients().stream().filter(x ->!gameHandler.getGameLobby().getPlayers().get(x).getPlayerBoard().isFinalTurn()).forEach(x->gameHandler.getGameLobby().send(new UpdateClient(x,new ArrayList<>(gameHandler.getGameLobby().getPlayers().get(x).getPlayerBoard().getHealthPlayer().getDamageBar()),new ArrayList<>(gameHandler.getGameLobby().getPlayers().get(x).getPlayerBoard().getHealthPlayer().getMark()))));
            }

        }
    }
}
