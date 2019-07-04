package network.Server;

import Controller.ActionValidController;
import Controller.GameHandler;
import Model.*;
import network.messages.*;

import javax.swing.event.InternalFrameEvent;
import java.io.FileNotFoundException;
import java.util.*;

public class GameLobby {
    private HashMap<Integer, Player> players;
    private HashMap<Colors, Integer> playersColor;
    private ArrayList<Integer> clients;
    private Server server;
    private GameHandler gameHandler;
    private Integer currentPlayer; //token of the current player
    private ArrayList<Message> historyMessage;
    private ArrayList<Message> shootHistoryMessage;
    private boolean useScoop = false;
    private HashMap<Integer, Boolean> actionPerformed;
    private ActionValidController actionValidController;
    private String currentSquare;
    private Timer timer;
    private HashMap<Integer, Boolean> pinged;
    private ArrayList<Integer> disconnected;
    private static final int PINGTIME = 15000;
/*Qui metto gli attributi che servono per salvare i dati dello shooter mentre controllo le isValid*/
    private ArrayList<Player> targetList = new ArrayList<>();
    private HashMap<Integer, NormalSquare> movedPlayer = new HashMap<>();
    private int scopePosition;
    private Colors scopeTarget;


    public GameLobby(ArrayList<Integer> clients, int skullNumber, String map, Server server){
        this.clients = clients;
        this.server = server;
        this.currentPlayer = null;
        this.players = new HashMap<>();
        this.playersColor=new HashMap<>();
        this.historyMessage = new ArrayList<>();
        this.actionPerformed = new HashMap<>();
        this.shootHistoryMessage = new ArrayList<>();
        this.clients.forEach(x -> actionPerformed.put(x, false));
        this.timer = new Timer();
        this.pinged = new HashMap<>();
        this.disconnected = new ArrayList<>();
        try {
            this.gameHandler = new GameHandler(1, this.clients, map, this);
            for(Player p: gameHandler.getGame().getPlayers()){
                players.put(p.getPlayerID(), p);
                playersColor.put(p.getColor(),p.getPlayerID());
            }
        }catch (FileNotFoundException e){
            for (Integer i: clients){
                server.send(new UpdateClient(i, "Server internal error, unable to create a new game"));
            }
        }
        clients.forEach(x -> pinged.put(x, true));
        this.actionValidController = gameHandler.getActionValidController();
        clients.parallelStream()
                .forEach(x -> send(new StartMessage(x, "game", skullNumber, map, players.get(x).getColor())));
        gameHandler.fillSquare();
        gameHandler.getTurnHandler().start();
        //new Thread(this::ping).start();
    }

    private void ping(){
        try {
            Thread.sleep(30000);
        }catch (InterruptedException e){}
        timer.schedule((new TimerTask() { //Start a timer and Wait for 15 seconds for clients response, if not
                    @Override
                    public void run() {
                        ArrayList<Integer> integers = new ArrayList<>(pinged.keySet());
                        for (Integer i: integers){
                            if (pinged.get(i)) {
                                pinged.put(i, false);
                                server.send(new Ping(i));
                            }
                            else {
                                remove(i);
                            }
                        }
                    }
                })
                ,0 ,PINGTIME);
    }

    public void remove(int i){
        disconnected.add(i);
        System.out.println(i + "disconnesso");
        if (clients.size() - disconnected.size() == 1) {
            gameHandler.winner();
        }
        pinged.remove(i);
    }

    public void startTurn(Integer token){
        System.out.println("current player: " + currentPlayer + " new player: " + token);
        actionPerformed.replace(token, false);
        if (currentPlayer != null)
            server.send(new EndMessage(currentPlayer));
        currentPlayer = token;
        if (!disconnected.contains(currentPlayer)) {
            server.send(new StartMessage(token, "turn", 0, "", null));
            //startTimer();
        }
        else{
            gameHandler.receiveServerMessage(new Pass(currentPlayer));
        }
    }

    private void startTimer(){
        int player = currentPlayer;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (actionPerformed.get(player)){
                    startTimer();
                }
                else {
                    server.send(new TimeOut(player));
                    if (player == currentPlayer)
                        gameHandler.receiveServerMessage(new Pass(player));
                }
            }
        }, 30000);
    }

    public ArrayList<Integer> getClients() {
        return clients;
    }

    public HashMap<Integer, Player> getPlayers() {
        return players;
    }

    public void receiveMessage(Message message){
        if (message.getActionType().getAbbreviation().equals(ActionType.PING.getAbbreviation())) {
            pinged.put(message.getToken(), true);
        }
        else {
            historyMessage.add(message);
            System.out.println("Ho ricevuto un messaggio " + message.getActionType().getAbbreviation());
            if (message.getActionType().getAbbreviation().equals(ActionType.MESSAGE.getAbbreviation())) {
                ChatMessage chatMessage = (ChatMessage) message;
                clients.forEach(x -> server.send(new ChatMessage(x, chatMessage.getMessage())));
                historyMessage.remove(historyMessage.size() - 1);
            } else if (message.getActionType().equals(ActionType.RECEIVETARGETSQUARE)) {
                ReceiveTargetSquare receiveTargetSquare = (ReceiveTargetSquare) message;
                gameHandler.firstPartAction(receiveTargetSquare);
            } else if (message.getActionType().equals(ActionType.CANUSESCOOPRESPONSE)) {
                CanUseScoopResponse canUseScoopResponse = (CanUseScoopResponse) message;
                useScoop = canUseScoopResponse.isUse();
                scopePosition = canUseScoopResponse.getPosition();
            }
            else if (message.getActionType().equals(ActionType.SHOOTRESPONSEP)){
                ReceiveTargetSquare receiveTargetSquare = (ReceiveTargetSquare) historyMessage.get(0);
                System.out.println(receiveTargetSquare.getPosEffect() + " <-effect  weapon-> " + receiveTargetSquare.getPosWeapon());
                ShootResponsep shootResponsep = (ShootResponsep) message;
                ArrayList<Player> targetPlayer = new ArrayList<>();
                Effect effect = gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(receiveTargetSquare.getPosWeapon()).getEffects().get(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(receiveTargetSquare.getPosWeapon()).getActionSequences().indexOf(receiveTargetSquare.getPosEffect()));
                for (Colors color: shootResponsep.getTargetPlayer()) {
                    targetPlayer.add(players.get(playersColor.get(color)));
                }
                System.out.println("Chiamo la action valid con effetto" + receiveTargetSquare.getPosEffect());
                effect.getpDamage().stream().
                        filter(x -> (x > 0) && !targetList.contains(targetPlayer.get(effect.getpDamage().indexOf(x)))).
                        forEach(x -> targetList.add(targetPlayer.get(effect.getpDamage().indexOf(x))));
                if(actionValidController.actionValid(targetPlayer, effect, -1)){
                    shootHistoryMessage.add(new Shot(targetPlayer, receiveTargetSquare.getPosEffect(), receiveTargetSquare.getPosWeapon()));
                    shootActionSequences(receiveTargetSquare);
                }
                else {
                    historyMessage = new ArrayList<>();
                    shootHistoryMessage = new ArrayList<>();
                    server.send(new UpdateClient(message.getToken(), "Action not valid"));
                }
                //devo vuotare la history e la shoothistory e mandare il messaggio di errore
            /*if(players.get(message.getToken()).getPlayerBoard().getHandPlayer().getPlayerWeapons().get(receiveTargetSquare.getPosWeapon()).getEffects().get(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(receiveTargetSquare.getPosWeapon()).getActionSequences().indexOf(receiveTargetSquare.getPosEffect())).getActionSequence().length() == shootHistoryMessage.size()){
                historyMessage = new ArrayList<>(shootHistoryMessage);
                shootHistoryMessage = new ArrayList<>();
            }*/
            } else if (message.getActionType().equals(ActionType.SHOOTRESPONSES)) {
                ReceiveTargetSquare receiveTargetSquare = (ReceiveTargetSquare) historyMessage.get(0);
                ShootResponses shootResponses = (ShootResponses) message;
                ArrayList<NormalSquare> targetSquare = new ArrayList<>();
                Effect effect = gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(receiveTargetSquare.getPosWeapon()).getEffects().get(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(receiveTargetSquare.getPosWeapon()).getActionSequences().indexOf(receiveTargetSquare.getPosEffect()));
                targetSquare.add(gameHandler.getGame().getMap().getSquareFromId(shootResponses.getTargetSquare()));
                if (actionValidController.actionValid(targetSquare, effect, -1)) {
                    effect.getsDamage().stream().filter(x -> (x > 0)).forEach(x -> gameHandler.getGame().getPlayers().stream().filter(y -> y.getPosition() == targetSquare.get(0) && !targetList.contains(y)).forEach(y -> targetList.add(y)));

                    shootHistoryMessage.add(new Shot(targetSquare, receiveTargetSquare.getPosEffect(), receiveTargetSquare.getPosWeapon()));
                    shootActionSequences(receiveTargetSquare);
                } else {
                    historyMessage = new ArrayList<>();
                    shootHistoryMessage = new ArrayList<>();
                    server.send(new UpdateClient(message.getToken(), "Action not valid"));
                }
            /*if(players.get(currentPlayer).getPlayerBoard().getHandPlayer().getPlayerWeapons().get(receiveTargetSquare.getPosWeapon()).getEffects().get(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(receiveTargetSquare.getPosWeapon()).getActionSequences().indexOf(receiveTargetSquare.getPosEffect())).getActionSequence().length() == shootHistoryMessage.size()){
                historyMessage = new ArrayList<>(shootHistoryMessage);
                shootHistoryMessage = new ArrayList<>();
            }*/
            } else if (message.getActionType().equals(ActionType.SHOOTRESPONSER)) {
                ReceiveTargetSquare receiveTargetSquare = (ReceiveTargetSquare) historyMessage.get(0);
                ShootResponser shootResponser = (ShootResponser) message;
                Room targetRoom = null;
                for (Room room : gameHandler.getGame().getMap().getRooms()) {
                    if (room.getColor() == gameHandler.getGame().getMap().getSquareFromId(shootResponser.getTargetRoom()).getColor())
                        targetRoom = room;
                }
                Effect effect = gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(receiveTargetSquare.getPosWeapon()).getEffects().get(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(receiveTargetSquare.getPosWeapon()).getActionSequences().indexOf(receiveTargetSquare.getPosEffect()));
                if (actionValidController.actionValid(targetRoom, effect, -1)){
                    shootHistoryMessage.add(new Shot(targetRoom, receiveTargetSquare.getPosEffect(), receiveTargetSquare.getPosWeapon()));
                    shootActionSequences(receiveTargetSquare);
                }
                else {
                    historyMessage = new ArrayList<>();
                    shootHistoryMessage = new ArrayList<>();
                    server.send(new UpdateClient(message.getToken(), "Action not valid"));
                }
            /*if(players.get(currentPlayer).getPlayerBoard().getHandPlayer().getPlayerWeapons().get(receiveTargetSquare.getPosWeapon()).getEffects().get(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(receiveTargetSquare.getPosWeapon()).getActionSequences().indexOf(receiveTargetSquare.getPosEffect())).getActionSequence().length() == shootHistoryMessage.size()){
                historyMessage = new ArrayList<>(shootHistoryMessage);
                shootHistoryMessage = new ArrayList<>();
                server.send(new Payment(currentPlayer, effect.getBonusCost(), false)); //TODO gestire PowerUp
            }*/
            } else if (message.getActionType().equals(ActionType.TARGETMOVERESPONSE)) {
                ReceiveTargetSquare receiveTargetSquare = (ReceiveTargetSquare) historyMessage.get(0);
                TargetMoveResponse targetMoveResponse = (TargetMoveResponse) message;
                Shot lastShotMessage = (Shot) shootHistoryMessage.get(shootHistoryMessage.size() - 1);
                gameHandler.receiveServerMessage(new MoveMessage(message.getToken(), lastShotMessage.getTargets().get(0), gameHandler.getGame().getMap().getSquareFromId(targetMoveResponse.getTargetSquare())));

                Effect effect = gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(receiveTargetSquare.getPosWeapon()).getEffects().get(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(receiveTargetSquare.getPosWeapon()).getActionSequences().indexOf(receiveTargetSquare.getPosEffect()));
                if(!movedPlayer.containsKey(lastShotMessage.getTargets().get(0).getPlayerID()))
                    movedPlayer.put(lastShotMessage.getTargets().get(0).getPlayerID(), lastShotMessage.getTargets().get(0).getPosition());
                server.send(new UpdateClient(receiveTargetSquare.getToken(), new Move(lastShotMessage.getTargets().get(0), gameHandler.getGame().getMap().getSquareFromId(targetMoveResponse.getTargetSquare()), effect.getTargetMove()).reachableSquare()));
                shootHistoryMessage.add(new MoveMessage(receiveTargetSquare.getToken(), lastShotMessage.getTargets().get(0), gameHandler.getGame().getMap().getSquareFromId(targetMoveResponse.getTargetSquare())));
                shootActionSequences(receiveTargetSquare);

            } else if (message.getActionType().equals(ActionType.MOVERESPONSE)) { // anche per grab se spawn quale carta vuole, se shot chiedere chi vuole sparare chiamando receive target
                MoveResponse moveResponse = (MoveResponse) message;
                ReceiveTargetSquare receiveTargetSquare = (ReceiveTargetSquare) historyMessage.get(0);
                if (receiveTargetSquare.getType().equals("grab")) {
                    if (!gameHandler.getGame().getMap().getSquareFromId(moveResponse.getSquareId()).isSpawn()) {
                        gameHandler.receiveServerMessage(new MoveMessage(message.getToken(), gameHandler.getGame().getCurrentPlayer(), gameHandler.getGame().getMap().getSquareFromId(moveResponse.getSquareId())));
                        if (gameHandler.receiveServerMessage(new GrabAmmo(message.getToken()))) {
                            server.send(new UpdateClient(message.getToken(), "You grab ammos"));
                        }
                        historyMessage = new ArrayList<>();
                    } else {
                        server.send(new UpdateClient(message.getToken(), gameHandler.getGame().getMap().getSquareFromId(moveResponse.getSquareId())));
                        clients.parallelStream().
                                filter(x -> (!x.equals(message.getToken()))).
                                forEach(x -> gameHandler.getGameLobby().send(new UpdateClient(x, players.get(moveResponse.getToken()).getColor(), players.get(moveResponse.getToken()).getPosition())));

                        server.send(new GrabWeaponRequest(message.getToken()));
                    }
                } else if (receiveTargetSquare.getType().equals("move")) {
                    gameHandler.receiveServerMessage(new MoveMessage(message.getToken(), gameHandler.getGame().getCurrentPlayer(), gameHandler.getGame().getMap().getSquareFromId(moveResponse.getSquareId())));
                    historyMessage = new ArrayList<>();
                    System.out.println("Ho azzerato la HISTORY");
                } else if (receiveTargetSquare.getType().equals("shoot")) {
                    if (players.get(message.getToken()).getPlayerBoard().getHealthPlayer().getAdrenalineAction() == 2 && !gameHandler.isActionAdrenalineDone()) {
                        gameHandler.setActionAdrenalineDone(true);
                        gameHandler.receiveServerMessage(new MoveMessage(message.getToken(), gameHandler.getGame().getCurrentPlayer(), gameHandler.getGame().getMap().getSquareFromId(moveResponse.getSquareId())));
                        gameHandler.getGameLobby().send(new UpdateClient(message.getToken(), moveResponse.getSquareId()));
                        gameHandler.getGameLobby().getClients()
                                .parallelStream().
                                filter(x -> (!x.equals(message.getToken()))).
                                forEach(x -> gameHandler.getGameLobby().send(new UpdateClient(x, players.get(message.getToken()).getColor(), gameHandler.getGame().getMap().getSquareFromId(moveResponse.getSquareId()))));
                    }
                    else {
                        System.out.println("MOVE x la Shoot");
                        if (!movedPlayer.containsKey(message.getToken()))
                            movedPlayer.put(message.getToken(), players.get(message.getToken()).getPosition());
                        gameHandler.receiveServerMessage(new MoveMessage(message.getToken(), gameHandler.getGame().getCurrentPlayer(), gameHandler.getGame().getMap().getSquareFromId(moveResponse.getSquareId())));
                        shootHistoryMessage.add(new MoveMessage(message.getToken(), gameHandler.getGame().getCurrentPlayer(), gameHandler.getGame().getMap().getSquareFromId(moveResponse.getSquareId())));
                    }
                    shootActionSequences(receiveTargetSquare);
                }
            } else if (message.getActionType().getAbbreviation().equals(ActionType.GRABWEAPON.getAbbreviation())) {
                MoveResponse moveResponse = (MoveResponse) historyMessage.get(1);
                GrabWeapon grabWeapon = (GrabWeapon) message;

                Integer[] cost = new Integer[3];
                cost[0] = gameHandler.getGame().getMap().getSquareFromId(moveResponse.getSquareId()).getWeapons().get(((GrabWeapon) message).getPositionWeapon()).getRedCost();
                cost[1] = gameHandler.getGame().getMap().getSquareFromId(moveResponse.getSquareId()).getWeapons().get(((GrabWeapon) message).getPositionWeapon()).getYellowCost();
                cost[2] = gameHandler.getGame().getMap().getSquareFromId(moveResponse.getSquareId()).getWeapons().get(((GrabWeapon) message).getPositionWeapon()).getBlueCost();
                if (gameHandler.getGame().getMap().getSquareFromId(moveResponse.getSquareId()).getWeapons().get(((GrabWeapon) message).getPositionWeapon()).getColor().getAbbreviation().equals("red") && cost[0] > 0) {
                    cost[0]--;
                } else if (gameHandler.getGame().getMap().getSquareFromId(moveResponse.getSquareId()).getWeapons().get(((GrabWeapon) message).getPositionWeapon()).getColor().getAbbreviation().equals("yellow") && cost[1] > 0) {
                    cost[1]--;
                } else if (cost[2] > 0) {
                    cost[2]--;
                }
                boolean ex = gameHandler.getGame().getCurrentPlayer().isValidCost(cost, false);
                if (!ex) {
                    server.send(new UpdateClient(message.getToken(), "Action not done"));
                    server.send(new UpdateClient(message.getToken(), gameHandler.getGame().getCurrentPlayer().getPosition()));

                    historyMessage = new ArrayList<>();
                } else {

                    MoveMessage moveMessage = new MoveMessage(message.getToken(), gameHandler.getGame().getCurrentPlayer(), gameHandler.getGame().getMap().getSquareFromId(moveResponse.getSquareId()));
                    historyMessage = new ArrayList<>();
                    historyMessage.add(moveMessage);
                    historyMessage.add(grabWeapon);
                    server.send(new Payment(grabWeapon.getToken(), cost, -1));
                }
            } else if (message.getActionType().getAbbreviation().equals(ActionType.RESPAWN.getAbbreviation())) {
                RespawnMessage respawnMessage = (RespawnMessage) message;
                players.get(respawnMessage.getToken()).calculateNewPosition(players.get(respawnMessage.getToken()).getPlayerBoard().getHandPlayer().getPlayerPowerUps().get(respawnMessage.getPowerUp()));
                server.send(new UpdateClient(respawnMessage.getToken(), players.get(respawnMessage.getToken()).getPosition()));
                clients.parallelStream()
                        .filter(x -> (!x.equals(respawnMessage.getToken())))
                        .forEach(x -> send(new UpdateClient(x, players.get(respawnMessage.getToken())
                                .getColor(), players.get(respawnMessage.getToken()).getPosition())));
                server.send(new UpdateClient(message.getToken(), players.get(message.getToken()).getPlayerBoard().getHandPlayer().getAmmoRYB()[0], players.get(message.getToken()).getPlayerBoard().getHandPlayer().getAmmoRYB()[1], players.get(message.getToken()).getPlayerBoard().getHandPlayer().getAmmoRYB()[2], players.get(message.getToken()).getPlayerBoard().getHandPlayer().getPlayerWeapons(), new ArrayList(players.get(message.getToken()).getPlayerBoard().getHandPlayer().getPlayerPowerUps())));
                historyMessage = new ArrayList<>();
            } else if (message.getActionType().getAbbreviation().equals(ActionType.RELOAD.getAbbreviation())) {
                ReloadMessage reloadMessage = (ReloadMessage) message;
                if (gameHandler.getActionValidController().actionValid(reloadMessage.getWeapon())) {
                    Integer[] cost = new Integer[3];
                    cost[0] = gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(reloadMessage.getWeapon()).getRedCost();
                    cost[1] = gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(reloadMessage.getWeapon()).getYellowCost();
                    cost[2] = gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(reloadMessage.getWeapon()).getBlueCost();
                    server.send(new Payment(reloadMessage.getToken(), cost, -1));
                    //there is only reload's message in history
                } else
                    server.send(new UpdateClient(reloadMessage.getToken(), "you can't recharge the weapon"));
            }
            //players.get(players.getId) in ingresso il token
            else if (message.getActionType().getAbbreviation().equals(ActionType.USEPOWERUPRESPONSE.getAbbreviation())) {
                UsePowerUpResponse usePowerUpResponse = (UsePowerUpResponse) message;
                if (gameHandler.receiveServerMessage(new UsePowerUp(message.getToken(), usePowerUpResponse.getPowerUp(), players.get(usePowerUpResponse.getUser()), players.get(playersColor.get(usePowerUpResponse.getTarget())), gameHandler.getGame().getMap().getSquareFromId(usePowerUpResponse.getSquare())))) {
                    server.send(new UpdateClient(message.getToken(), "The use of the power up was successful"));
                    historyMessage = new ArrayList<>();
                } else {
                    server.send(new UpdateClient(message.getToken(), "The use of the power up was failure, PLEASE READ REGULATION"));
                    historyMessage = new ArrayList<>();
                }
            } else if(message.getActionType().getAbbreviation().equals(ActionType.PASS.getAbbreviation())){
                gameHandler.receiveServerMessage(message);
                historyMessage=new ArrayList<>();

            }
            else if(message.getActionType().getAbbreviation().equals(ActionType.PAYMENTRESPONSE.getAbbreviation())) {
                PaymentResponse paymentResponse = (PaymentResponse) message;
                paymentServer(paymentResponse);
            }

            else if(message.getActionType().getAbbreviation().equals(ActionType.SUBSTITUTEWEAPONRESPONSE.getAbbreviation())){
                SubstituteWeaponResponse substituteWeaponResponse=(SubstituteWeaponResponse) message;
                int pos=gameHandler.getGame().getCurrentPlayer().getPosition().setItems(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(substituteWeaponResponse.getWeapon()));
                for(Player p: gameHandler.getGame().getPlayers()){
                    gameHandler.getGameLobby().send(new UpdateClient(p.getPlayerID(),gameHandler.getGame().getCurrentPlayer().getPosition().getId(),gameHandler.getGame().getCurrentPlayer().getPosition().getWeapons().get(pos),pos));
                }
                players.get(substituteWeaponResponse.getToken()).getPlayerBoard().getHandPlayer().substituteWeapons(substituteWeaponResponse.getWeapon());
                server.send(new UpdateClient(gameHandler.getGame().getCurrentPlayer().getPlayerID(),gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getAmmoRYB()[0],gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getAmmoRYB()[1],gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getAmmoRYB()[2],new ArrayList<>(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons()), new ArrayList<>(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerPowerUps())));
                historyMessage=new ArrayList<>();
            }
            else if(message.getActionType().getAbbreviation().equals(ActionType.SCOPETARGETRESPONSE.getAbbreviation())){
                ScopeTargetResponse scopeTargetResponse=(ScopeTargetResponse)message;
                ReceiveTargetSquare receiveTargetSquare=(ReceiveTargetSquare)historyMessage.get(0);
                gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(receiveTargetSquare.getPosWeapon()).setCharge(false);
                server.send(new UpdateClient(gameHandler.getGame().getCurrentPlayer().getPlayerID(),gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getAmmoRYB()[0],gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getAmmoRYB()[1],gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getAmmoRYB()[2],new ArrayList<>(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons()), new ArrayList<>(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerPowerUps())));
                scopeTarget=scopeTargetResponse.getTarget();

                //new history list with a fail shot message with power up
                historyMessage = new ArrayList<>(shootHistoryMessage);
                shootHistoryMessage = new ArrayList<>();

                server.send(new Payment(currentPlayer, gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(receiveTargetSquare.getPosWeapon()).getEffects().get(receiveTargetSquare.getPosEffect()).getBonusCost(), scopePosition));

            }
        }
    }

    public void paymentServer(PaymentResponse paymentResponse){
            boolean valueReturn;
            boolean scoopReturn;
            //No powerUp, no scoop
            if((!paymentResponse.isUsePowerUp()&& !paymentResponse.isScoop())) {
                if (gameHandler.getGame().getCurrentPlayer().isValidCostWeapon(paymentResponse.getCost())) {
                    valueReturn = gameHandler.getPaymentController().payment(paymentResponse.getCost());
                    if (valueReturn) {
                        for (Message m : historyMessage)
                            gameHandler.receiveServerMessage(m);
                        if (historyMessage.get(0).getActionType() != ActionType.RELOAD || historyMessage.get(0).getActionType() != ActionType.USEPOWERUP) {
                            gameHandler.getTurnHandler().endAction();
                            server.send(new FinalAction(paymentResponse.getToken()));
                        }
                        historyMessage = new ArrayList<>();//reset hystoryMessage fare update

                    }
                    else {
                        server.send(new Payment(paymentResponse.getToken(), paymentResponse.getCost(), -1));
                        server.send(new UpdateClient(paymentResponse.getToken(), "Payment failure:use the correct powerUp"));
                    }
                }
                else{
                    server.send(new UpdateClient(paymentResponse.getToken(), "Payment failure:use  powerUp for payment"));
                    server.send(new Payment(paymentResponse.getToken(), paymentResponse.getCost(), -1));
                }
                players.get(paymentResponse.getToken()).getPlayerBoard().getHandPlayer().getPlayerWeapons().forEach(x -> System.out.println(x.getName()));
                server.send(new UpdateClient(paymentResponse.getToken(), players.get(paymentResponse.getToken()).getPlayerBoard().getHandPlayer().getAmmoRYB()[0], players.get(paymentResponse.getToken()).getPlayerBoard().getHandPlayer().getAmmoRYB()[1], players.get(paymentResponse.getToken()).getPlayerBoard().getHandPlayer().getAmmoRYB()[2], new ArrayList<>(players.get(paymentResponse.getToken()).getPlayerBoard().getHandPlayer().getPlayerWeapons()), new ArrayList<>(players.get(paymentResponse.getToken()).getPlayerBoard().getHandPlayer().getPlayerPowerUps())));
            }
            //no powerUp, si scoop
            else if(!paymentResponse.isUsePowerUp()&& paymentResponse.isScoop()){
                System.out.println("ewfjnewiuongungugeoienoiwen pago scoop no powerUp");
                if (gameHandler.getGame().getCurrentPlayer().isValidCostWeapon(paymentResponse.getCost())) {
                    valueReturn = gameHandler.getPaymentController().payment(paymentResponse.getCost());

                    if (valueReturn) {
                        System.out.println("demskoaniog"+paymentResponse.getPowerUpScoop());
                        if (paymentResponse.getPowerUpScoop()==1 || paymentResponse.getPowerUpScoop()==2 ||paymentResponse.getPowerUpScoop()==3){
                            System.out.println("demskoaniog"+paymentResponse.getPowerUpScoop());
                            valueReturn = gameHandler.getPaymentController().paymentPowerUp(paymentResponse.getPowerUpScoop());
                        }
                        else
                            valueReturn = gameHandler.getPaymentController().paymentPowerUp(paymentResponse.getColorScoop());

                        if (valueReturn) {//if both payment are successful
                            for (Message m : historyMessage)
                                gameHandler.receiveServerMessage(m);
                            //use powerUp
                            players.get(playersColor.get(scopeTarget)).getPlayerBoard().getHealthPlayer().addMark(gameHandler.getGame().getCurrentPlayer(),1);
                            gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().removePowerUp(scopePosition);

                            if (historyMessage.get(0).getActionType() != ActionType.RELOAD || historyMessage.get(0).getActionType() != ActionType.USEPOWERUP) {
                                gameHandler.getTurnHandler().endAction();
                                server.send(new FinalAction(paymentResponse.getToken()));
                            }
                            historyMessage = new ArrayList<>();
                        }
                        else {//powerUp in not used
                            for (Message m : historyMessage)
                                gameHandler.receiveServerMessage(m);
                            if (historyMessage.get(0).getActionType() != ActionType.RELOAD || historyMessage.get(0).getActionType() != ActionType.USEPOWERUP) {
                                gameHandler.getTurnHandler().endAction();
                                server.send(new FinalAction(paymentResponse.getToken()));
                            }
                            System.out.println("serwhwehwhedjwr è fallito");

                                                         historyMessage = new ArrayList<>();
                            server.send(new UpdateClient(paymentResponse.getToken(), "Scoop is not used"));
                        }
                    } else {
                        server.send(new UpdateClient(paymentResponse.getToken(), "Payment failure:use the correct powerUp"));
                        server.send(new Payment(paymentResponse.getToken(), paymentResponse.getCost(), scopePosition));
                    }
                }
                else{
                    server.send(new UpdateClient(paymentResponse.getToken(), "Payment failure:use the powerUp for payment"));
                    server.send(new Payment(paymentResponse.getToken(), paymentResponse.getCost(),scopePosition));
                }
                players.get(paymentResponse.getToken()).getPlayerBoard().getHandPlayer().getPlayerWeapons().forEach(x -> System.out.println(x.getName()));
                server.send(new UpdateClient(paymentResponse.getToken(), players.get(paymentResponse.getToken()).getPlayerBoard().getHandPlayer().getAmmoRYB()[0], players.get(paymentResponse.getToken()).getPlayerBoard().getHandPlayer().getAmmoRYB()[1], players.get(paymentResponse.getToken()).getPlayerBoard().getHandPlayer().getAmmoRYB()[2], new ArrayList<>(players.get(paymentResponse.getToken()).getPlayerBoard().getHandPlayer().getPlayerWeapons()), new ArrayList<>(players.get(paymentResponse.getToken()).getPlayerBoard().getHandPlayer().getPlayerPowerUps())));
            }
            //si powerUp, si Scoop
            else if(paymentResponse.isUsePowerUp()&& paymentResponse.isScoop()){
                System.out.println("ewfjnewiuongungugeoienoiwen pago scoop si powerUp");
                valueReturn=gameHandler.getPaymentController().payment( Arrays.copyOf(paymentResponse.getCost(),3),paymentResponse.getPowerUp());
                if(valueReturn){
                    if(paymentResponse.getPowerUpScoop()!=-1)
                        valueReturn=gameHandler.getPaymentController().paymentPowerUp(paymentResponse.getPowerUpScoop());
                    else
                        valueReturn=gameHandler.getPaymentController().paymentPowerUp(paymentResponse.getColorScoop());
                    if(valueReturn){
                        for (Message m : historyMessage)
                            gameHandler.receiveServerMessage(m);

                        //use powerUp
                        players.get(playersColor.get(scopeTarget)).getPlayerBoard().getHealthPlayer().addMark(gameHandler.getGame().getCurrentPlayer(),1);
                        gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().removePowerUp(scopePosition);


                        if(historyMessage.get(0).getActionType()!=ActionType.RELOAD || historyMessage.get(0).getActionType()!=ActionType.USEPOWERUP){
                            gameHandler.getTurnHandler().endAction();
                            server.send(new FinalAction(paymentResponse.getToken()));
                            //increment  numerAction in view
                        }

                        historyMessage=new ArrayList<>();
                    }
                    else{
                        for (Message m : historyMessage)
                            gameHandler.receiveServerMessage(m);

                        if (historyMessage.get(0).getActionType() != ActionType.RELOAD || historyMessage.get(0).getActionType() != ActionType.USEPOWERUP) {
                            gameHandler.getTurnHandler().endAction();
                            server.send(new FinalAction(paymentResponse.getToken()));
                        }

                        historyMessage=new ArrayList<>();
                        server.send(new UpdateClient(paymentResponse.getToken(), "Scoop is not used"));//reset update
                    }
                }
                else{
                    server.send(new UpdateClient(paymentResponse.getToken(), "Payment failure:use the correct powerUp"));
                    server.send(new Payment(paymentResponse.getToken(),paymentResponse.getCost(),scopePosition));
                }
                players.get(paymentResponse.getToken()).getPlayerBoard().getHandPlayer().getPlayerWeapons().forEach(x -> System.out.println(x.getName()));
                server.send(new UpdateClient(paymentResponse.getToken(), players.get(paymentResponse.getToken()).getPlayerBoard().getHandPlayer().getAmmoRYB()[0], players.get(paymentResponse.getToken()).getPlayerBoard().getHandPlayer().getAmmoRYB()[1], players.get(paymentResponse.getToken()).getPlayerBoard().getHandPlayer().getAmmoRYB()[2], new ArrayList<>(players.get(paymentResponse.getToken()).getPlayerBoard().getHandPlayer().getPlayerWeapons()), new ArrayList<>(players.get(paymentResponse.getToken()).getPlayerBoard().getHandPlayer().getPlayerPowerUps())));
            }
            //si powerUp no scoop
            else if((paymentResponse.isUsePowerUp()&& !paymentResponse.isScoop())){
                valueReturn=gameHandler.getPaymentController().payment( Arrays.copyOf(paymentResponse.getCost(),3),paymentResponse.getPowerUp());
                if(valueReturn){
                    for(Message m:historyMessage)
                        gameHandler.receiveServerMessage(m);
                    if(historyMessage.get(0).getActionType()!=ActionType.RELOAD || historyMessage.get(0).getActionType()!=ActionType.USEPOWERUP){
                        gameHandler.getTurnHandler().endAction();
                        server.send(new FinalAction(paymentResponse.getToken()));
                    }
                    historyMessage=new ArrayList<>(); //reset hystoryMessage fare update
                }
                else{
                    server.send(new UpdateClient(paymentResponse.getToken(), "Payment failure:use the correct powerUp"));
                    server.send(new Payment(paymentResponse.getToken(),paymentResponse.getCost(),-1));
                }
                players.get(paymentResponse.getToken()).getPlayerBoard().getHandPlayer().getPlayerWeapons().forEach(x -> System.out.println(x.getName()));
                server.send(new UpdateClient(paymentResponse.getToken(), players.get(paymentResponse.getToken()).getPlayerBoard().getHandPlayer().getAmmoRYB()[0], players.get(paymentResponse.getToken()).getPlayerBoard().getHandPlayer().getAmmoRYB()[1], players.get(paymentResponse.getToken()).getPlayerBoard().getHandPlayer().getAmmoRYB()[2], new ArrayList<>(players.get(paymentResponse.getToken()).getPlayerBoard().getHandPlayer().getPlayerWeapons()), new ArrayList<>(players.get(paymentResponse.getToken()).getPlayerBoard().getHandPlayer().getPlayerPowerUps())));
            }
    }

    public void canUseScoop(Integer player){
        server.send(new CanUseScoop(player));
    }

    public void canUseTagBack(Integer player,Colors playerShooter){
        server.send(new CanUseTagBack(player,playerShooter));
    }

    public void endGame(List<Integer> winner){
        for (Integer i: clients){
            if (!(winner.contains(i)))
                server.send(new WinnerMessage(i, false));
            else
                server.send(new WinnerMessage(i, true));
        }
    }

    public void shootActionSequences(ReceiveTargetSquare receiveTargetSquare){
        Effect effect = gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(receiveTargetSquare.getPosWeapon()).getEffects().get(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(receiveTargetSquare.getPosWeapon()).getActionSequences().indexOf(receiveTargetSquare.getPosEffect()));
        String actionSequence = effect.getActionSequence();
        System.out.println("-----------------------\nl'arma scelta ha actionSequense -> " + actionSequence + "\nShootHistoryMessage.size = " + shootHistoryMessage.size() + "\nActionSequence.length = " + actionSequence.length() + "\n--------------------");
        int i = shootHistoryMessage.size();
        if(i < actionSequence.length()){

            if (actionSequence.charAt(i) == 'p') {
                System.out.println("Nella shootActionSequences ho un "+actionSequence.charAt(i));
                ArrayList<Colors> targetToken = new ArrayList<>();
                for (Player target: new Shoot(effect, gameHandler.getGame().getCurrentPlayer(), null).targetablePlayer()) {
                    targetToken.add(target.getColor());
                }
                server.send(new ShootRequestp(receiveTargetSquare.getToken(), effect.getTargetNumber(), targetToken));

            } else if (actionSequence.charAt(i) == 's') {
                System.out.println("Nella shootActionSequences ho un "+actionSequence.charAt(i));
                ArrayList<String> targetID = new ArrayList<>();
                for (NormalSquare target: new Shoot(effect, gameHandler.getGame().getCurrentPlayer(), null).reachableSquare()) {
                    targetID.add(target.getId());
                }
                server.send(new ShootRequests(receiveTargetSquare.getToken(), targetID));

            } else if (actionSequence.charAt(i) == 'r') {
                System.out.println("Nella shootActionSequences ho un "+actionSequence.charAt(i));
                ArrayList<String> targetID = new ArrayList<>();
                for (NormalSquare target: new Shoot(effect, gameHandler.getGame().getCurrentPlayer(), null).reachableRoom()) {
                    targetID.add(target.getId());
                }
                server.send(new ShootRequestr(receiveTargetSquare.getToken(), targetID));

            } else if (actionSequence.charAt(i) == 'm') {
                System.out.println("Nella shootActionSequences ho un "+actionSequence.charAt(i));
                ArrayList<String> squareList = new ArrayList<>();
                Player target = players.get(shootHistoryMessage.get(shootHistoryMessage.size() - 1).getToken());
                Integer move = gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(receiveTargetSquare.getPosWeapon()).getEffects().get(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(receiveTargetSquare.getPosWeapon()).getActionSequences().indexOf(receiveTargetSquare.getPosEffect())).getTargetMove();
                for (NormalSquare square: new Move(target, null, move).reachableSquare()) {
                    squareList.add(square.getId());
                }
                server.send(new TargetMoveRequest(receiveTargetSquare.getToken(), squareList));

            } else if (actionSequence.charAt(i) == 'M') {
                System.out.println("Nella shootActionSequences ho un " + actionSequence.charAt(i));
                server.send(new UpdateClient(receiveTargetSquare.getToken(), new Move(gameHandler.getGame().getCurrentPlayer(), null, effect.getMyMove()).reachableSquare()));

            }
        }
        else{
            System.out.println("L'azione è finita, qui inverto le history e invio il payment");
            //scoop
            if(useScoop){
                ArrayList<Colors> targetsColor=new ArrayList<>();
                for(Player p:targetList)
                    targetsColor.add(p.getColor());
                server.send(new ScopeTargetRequest(gameHandler.getGame().getCurrentPlayer().getPlayerID(),targetsColor));
            }
            else{
                gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(receiveTargetSquare.getPosWeapon()).setCharge(false);
                server.send(new UpdateClient(gameHandler.getGame().getCurrentPlayer().getPlayerID(),gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getAmmoRYB()[0],gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getAmmoRYB()[1],gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getAmmoRYB()[2],new ArrayList<>(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons()), new ArrayList<>(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerPowerUps())));
                historyMessage = new ArrayList<>(shootHistoryMessage);
                shootHistoryMessage = new ArrayList<>();
                server.send(new Payment(currentPlayer, effect.getBonusCost(), -1));
            }
            //CanBackTag
            for(Player p:targetList){
                System.out.println("fmeksldflknadkka");
                for (CardPowerUp powerUp : p.getPlayerBoard().getHandPlayer().getPlayerPowerUps()) {
                    if (powerUp.getWhen().equals("deal")) {
                        System.out.println("f,lyuppjtommteopmtjopmtdmjoemtopumeoptmuoepmopmoeptmop");
                        gameHandler.getGameLobby().canUseTagBack(p.getPlayerID(),gameHandler.getGame().getCurrentPlayer().getColor());
                    }
                }
            }
        }
    }

    public void send(Message message){
        server.send(message);
    }
}
