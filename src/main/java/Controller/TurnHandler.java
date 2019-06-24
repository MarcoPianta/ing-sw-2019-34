package Controller;

import Model.*;
import network.messages.*;
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

    public void start(){
        gameHandler.getGame().getCurrentPlayer().setState(StateMachineEnumerationTurn.START);
        //TODO messaggio che inizia turno
        if(gameHandler.getGame().getCurrentPlayer().getPosition()==null && gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getMaxReward()==8){
            gameHandler.getGame().getCurrentPlayer().spawn(2);//first spawn
            gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().addAmmo(1,1,1);
        }
        setNextState(StateMachineEnumerationTurn.ACTION1);
        gameHandler.setPlayerValid(gameHandler.getGame().getCurrentPlayer());
    }
    public boolean actionState(Message message){
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
        if(gameHandler.getGame().getCurrentPlayer().getState()==StateMachineEnumerationTurn.ACTION1)
            setNextState(StateMachineEnumerationTurn.ACTION2);
        else
            setNextState(StateMachineEnumerationTurn.RELOAD);
        return  valueReturn;
    }
    private boolean actionAdrenaline012(Message message){
        boolean valueReturn;
        if(message.getActionType()==ActionType.MOVE){
            MoveMessage newMessage=(MoveMessage)message;
            valueReturn= new Move(gameHandler.getGame().getCurrentPlayer(),newMessage.getNewSquare(), 3).execute();}
        else if(message.getActionType()==ActionType.SHOT){
            Shot newMessage=(Shot)message;
            if(newMessage.getPowerUp()!=null){
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
    protected boolean actionShot(Shot message){
        boolean valueReturn;
        if(message.getSquare()==null && message.getRoom()==null)
            valueReturn=new Shoot(message.getEffect(),gameHandler.getGame().getCurrentPlayer(),convertedPlayer(message.getTargets()), null, false).execute();
        else if(message.getSquare()==null && message.getTargets()==null)
            valueReturn=new Shoot(message.getEffect(),gameHandler.getGame().getCurrentPlayer(),message.getRoom().getColor()).execute();
        else
            valueReturn=new Shoot(message.getEffect(),gameHandler.getGame().getCurrentPlayer(), null, null /*message.getSquare()*/, true).execute();
        //use venom
        if(valueReturn && message.getPowerUp()!=-1 && gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerPowerUps().get(message.getPowerUp()).getWhen().equals("get")){
            usePowerUp(message);
        }
        if(valueReturn){
            for(Player p:message.getTargets()) {
                for (CardPowerUp powerUp : p.getPlayerBoard().getHandPlayer().getPlayerPowerUps()) {
                    if (powerUp.getWhen().equals("get")) {
                        //canUseTagback(p.getPlayerID());
                    }
                }
            }
        }
        if(valueReturn)
            gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(message.getWeapon()).setCharge(false);
        return valueReturn;
    }

    //creare messaggio diverso per weapon =3, stesso messaggio ma costruttore diverso, se ho 3 armi grabbo normalmente  e elimino lintero per rimuovere
    protected boolean actionGrab(Message message){
        boolean valueReturn=false;
        if(message.getActionType()==ActionType.GRABWEAPON ){
            GrabWeapon newMessage=(GrabWeapon) message;
            ArrayList<Integer> cost=new ArrayList<>();
            cost.add(gameHandler.getGame().getCurrentPlayer().getPosition().getWeapons().get(newMessage.getPositionWeapon()).getBlueCost());
            cost.add(gameHandler.getGame().getCurrentPlayer().getPosition().getWeapons().get(newMessage.getPositionWeapon()).getYellowCost());
            cost.add(gameHandler.getGame().getCurrentPlayer().getPosition().getWeapons().get(newMessage.getPositionWeapon()).getRedCost());
            if(gameHandler.getGame().getCurrentPlayer().isValidCost(cost,false)){
                valueReturn= new Grab(gameHandler.getGame().getCurrentPlayer(),gameHandler.getGame().getCurrentPlayer().getPosition().getWeapons().get(newMessage.getPositionWeapon())).execute();
                if(valueReturn)
                    endTurnChecks.getEmptySquares().add(gameHandler.getGame().getCurrentPlayer().getPosition());
            }
        }
        else if(message.getActionType()==ActionType.GRABAMMO){
            valueReturn= new Grab(gameHandler.getGame().getCurrentPlayer(), (CardOnlyAmmo) gameHandler.getGame().getCurrentPlayer().getPosition().getItem()).execute();
            if(valueReturn)
                endTurnChecks.getEmptySquares().add(gameHandler.getGame().getCurrentPlayer().getPosition());
        }
        else{
            valueReturn= new Grab(gameHandler.getGame().getCurrentPlayer(), (CardNotOnlyAmmo) gameHandler.getGame().getCurrentPlayer().getPosition().getItem()).execute();
            if(valueReturn)
                endTurnChecks.getEmptySquares().add(gameHandler.getGame().getCurrentPlayer().getPosition());
        }

        if(valueReturn && gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().size()==4)
            {//substituteWeapons(gameHandler.getGame().getCurrentPlayer());
            }
        return valueReturn;
    }

    private boolean powerUpIsValid(Message message){
        boolean valueReturn=false;
        if(message.getActionType()==ActionType.USEPOWERUP){
            UsePowerUp newMessage=(UsePowerUp)message;
            if((gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerPowerUps().get(newMessage.getPowerUp()).getWhen().equals("during")
                    &&(newMessage.getUser()==gameHandler.getGame().getCurrentPlayer())
                    &&(gameHandler.getGame().getCurrentPlayer().getState()!=StateMachineEnumerationTurn.WAIT)) ||
                    (gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerPowerUps().get(newMessage.getPowerUp()).getWhen().equals("dealing")
                            &&(newMessage.getUser()!=gameHandler.getGame().getCurrentPlayer())))
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

    public boolean usePowerUp(Message message){
        boolean valueReturn=false;
        if(message.getActionType()==ActionType.SHOT && powerUpIsValid(message)){
            Shot newMessage=(Shot) message;
            gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().usePowerUp(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerPowerUps().get(((Shot) message).getPowerUp()),newMessage.getTargetPowerUp(),null);
            valueReturn=true;
        }
        else if(powerUpIsValid(message)){
            UsePowerUp newMessage=(UsePowerUp) message;
            convertedPlayer(newMessage.getUser()).getPlayerBoard().getHandPlayer().usePowerUp(convertedPlayer(newMessage.getUser()).getPlayerBoard().getHandPlayer().getPlayerPowerUps().get(newMessage.getPowerUp()),convertedPlayer(newMessage.getTarget()),newMessage.getSquare());
            valueReturn=true;
        }
        return valueReturn;

    }

    public boolean actionReload(ReloadMessage message){
        boolean valueReturn;
        ArrayList<CardPowerUp> powerUps = new ArrayList<>();
        int[] cost = {0, 0, 0};
        cost[0] = gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(message.getWeapon()).getRedCost();
        cost[1] = gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(message.getWeapon()).getYellowCost();
        cost[2] = gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(message.getWeapon()).getBlueCost();
        for(Integer i:message.getPowerUp())
            powerUps.add(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerPowerUps().get(i));

        gameHandler.getGame().getCurrentPlayer().setState(StateMachineEnumerationTurn.RELOAD);
        if(message.getPowerUp() == null) {
            if (paymentController.payment(cost))
                valueReturn = new Reload(gameHandler.getGame().getCurrentPlayer(), gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(message.getWeapon())).execute();
            else
                valueReturn = false;
        }else {
            if(actionValidController.actionValid(message.getWeapon()) && paymentController.payment(cost, powerUps)) {
                    valueReturn = new Reload(gameHandler.getGame().getCurrentPlayer(), gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(message.getWeapon()), cost[0], cost[1], cost[2]).execute();
                }
            else
                valueReturn = false;
        }
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
                game.getDeadPlayer().get(i).spawn(1);//extract powerUp
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
