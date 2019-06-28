package network.Server;

import Controller.GameHandler;
import Model.*;
import network.messages.*;

import java.io.FileNotFoundException;
import java.util.*;

public class GameLobby {
    private HashMap<Integer, Player> players;
    private ArrayList<Integer> clients;
    private Server server;
    private GameHandler gameHandler;
    private Integer currentPlayer; //token of the current player
    private ArrayList<Message> historyMessage;
    private ArrayList<Message> shootHistoryMessage;
    private boolean useScoop;
    private HashMap<Integer, Boolean> actionPerformed;

    private String currentSquare;

    public GameLobby(ArrayList<Integer> clients, int skullNumber, String map, Server server){
        this.clients = clients;
        this.server = server;
        this.currentPlayer = null;
        this.players = new HashMap<>();
        this.historyMessage = new ArrayList<>();
        this.actionPerformed = new HashMap<>();
        this.clients.forEach(x -> actionPerformed.put(x, false));
        try {
            this.gameHandler = new GameHandler(skullNumber, this.clients, map, this);
            for(Player p: gameHandler.getGame().getPlayers()){
                players.put(p.getPlayerID(), p);
            }
        }catch (FileNotFoundException e){
            for (Integer i: clients){
                server.send(new UpdateClient(i, "Server internal error, unable to create a new game"));
            }
        }
    }

    public void startTurn(Integer token){
        actionPerformed.replace(token, false);
        if (currentPlayer != null)
            server.send(new EndMessage(currentPlayer));
        server.send(new StartMessage(token, "turn", 0, ""));
        currentPlayer = token;
        startTimer();
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
        historyMessage.add(message);

        if (message.getActionType().equals(ActionType.RECEIVETARGETSQUARE)) {
            ReceiveTargetSquare receiveTargetSquare = (ReceiveTargetSquare) message;
            gameHandler.firstPartAction(receiveTargetSquare);
        }

        else if (message.getActionType().equals(ActionType.SHOOTRESPONSEP)){
            ReceiveTargetSquare receiveTargetSquare = (ReceiveTargetSquare) historyMessage.get(0);
            ShootResponsep shootResponsep = (ShootResponsep) message;
            ArrayList<Player> targetPlayer = new ArrayList<>();
            Effect effect = gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(receiveTargetSquare.getPosWeapon()).getEffects().get(receiveTargetSquare.getPosEffect());
            for (Integer token: shootResponsep.getTargetPlayer()) {
                targetPlayer.add(players.get(token));
            }
            if(new Shoot(effect, gameHandler.getGame().getCurrentPlayer(), targetPlayer, null, false).isValid()){
                shootHistoryMessage.add(new Shot(targetPlayer, effect, receiveTargetSquare.getPosWeapon()));
                shootActionSequences(receiveTargetSquare);
            }
            //TODO else --> dico che non è valida
            if(gameHandler.getGame().getPlayers().get(currentPlayer).getPlayerBoard().getHandPlayer().getPlayerWeapons().get(receiveTargetSquare.getPosWeapon()).getEffects().get(receiveTargetSquare.getPosEffect()).getActionSequence().length() == shootHistoryMessage.size()){
                historyMessage = new ArrayList<>(shootHistoryMessage);
                shootHistoryMessage = null;
            }
        }

        else if (message.getActionType().equals(ActionType.SHOOTRESPONSES)) {
            ReceiveTargetSquare receiveTargetSquare = (ReceiveTargetSquare) historyMessage.get(0);
            ShootResponses shootResponses = (ShootResponses) message;
            ArrayList<NormalSquare> targetSquare = new ArrayList<>();
            Effect effect = gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(receiveTargetSquare.getPosWeapon()).getEffects().get(receiveTargetSquare.getPosEffect());
            for (String id : shootResponses.getTargetSquare()) {
                targetSquare.add(gameHandler.getGame().getMap().getSquareFromId(id));
            }
            if (new Shoot(effect, gameHandler.getGame().getCurrentPlayer(), null, targetSquare, false).isValid()){
                shootHistoryMessage.add(new Shot(targetSquare, effect, receiveTargetSquare.getPosWeapon()));
                shootActionSequences(receiveTargetSquare);
            }
            //TODO else --> dico che non è valida
            if(gameHandler.getGame().getPlayers().get(currentPlayer).getPlayerBoard().getHandPlayer().getPlayerWeapons().get(receiveTargetSquare.getPosWeapon()).getEffects().get(receiveTargetSquare.getPosEffect()).getActionSequence().length() == shootHistoryMessage.size()){
                historyMessage = new ArrayList<>(shootHistoryMessage);
                shootHistoryMessage = null;
            }
        }

        else if (message.getActionType().equals(ActionType.SHOOTRESPONSER)) {
            ReceiveTargetSquare receiveTargetSquare = (ReceiveTargetSquare) historyMessage.get(0);
            ShootResponser shootResponser = (ShootResponser) message;
            Room targetRoom = null;
            for (Room room : gameHandler.getGame().getMap().getRooms()) {
                if (room.getColor() == gameHandler.getGame().getMap().getSquareFromId(shootResponser.getTargetRoom()).getColor())
                    targetRoom = room;
            }
            Effect effect = gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(receiveTargetSquare.getPosWeapon()).getEffects().get(receiveTargetSquare.getPosEffect());
            if (new Shoot(effect, gameHandler.getGame().getCurrentPlayer(), targetRoom.getColor()).isValid()){
                shootHistoryMessage.add(new Shot(targetRoom, effect, receiveTargetSquare.getPosWeapon()));
                shootActionSequences(receiveTargetSquare);
            }
            //TODO else --> dico che non è valida
            if(gameHandler.getGame().getPlayers().get(currentPlayer).getPlayerBoard().getHandPlayer().getPlayerWeapons().get(receiveTargetSquare.getPosWeapon()).getEffects().get(receiveTargetSquare.getPosEffect()).getActionSequence().length() == shootHistoryMessage.size()){
                historyMessage = new ArrayList<>(shootHistoryMessage);
                shootHistoryMessage = null;
            }
        }

        else if (message.getActionType().equals(ActionType.TARGETMOVERESPONSE)){
            ReceiveTargetSquare receiveTargetSquare = (ReceiveTargetSquare) historyMessage.get(0);
            TargetMoveResponse targetMoveResponse = (TargetMoveResponse) message;
            Player target = players.get(targetMoveResponse.getTargetPlayer());
            Effect effect = gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(receiveTargetSquare.getPosWeapon()).getEffects().get(receiveTargetSquare.getPosEffect());
            server.send(new UpdateClient(targetMoveResponse.getToken(), new Move(target, null, effect.getTargetMove()).reachableSquare()));
            shootHistoryMessage.add(new MoveMessage(target.getPlayerID(), null, null));

        }

        else if (message.getActionType().equals(ActionType.MOVERESPONSE)){ // anche per grab se spawn quale carta vuole, se shot chiedere chi vuole sparare chiamando receive target
            MoveResponse moveResponse = (MoveResponse) message;
            ReceiveTargetSquare receiveTargetSquare = (ReceiveTargetSquare) historyMessage.get(0);
            if (receiveTargetSquare.getType().equals("grab")){
                if (!gameHandler.getGame().getMap().getSquareFromId(moveResponse.getSquareId()).isSpawn()) {
                    gameHandler.receiveServerMessage(new MoveMessage(message.getToken(), gameHandler.getGame().getCurrentPlayer(),gameHandler.getGame().getMap().getSquareFromId(moveResponse.getSquareId())));
                    gameHandler.receiveServerMessage(new GrabAmmo(message.getToken()));
                    historyMessage = new ArrayList<>();
                }
                else{
                    server.send(new GrabWeaponRequest(message.getToken()));
                }
            }
            else if (receiveTargetSquare.getType().equals("move")){
                gameHandler.receiveServerMessage(new MoveMessage(message.getToken(),gameHandler.getGame().getCurrentPlayer(),gameHandler.getGame().getMap().getSquareFromId(moveResponse.getSquareId())));
                historyMessage= new ArrayList<>();
            }
            else if (receiveTargetSquare.getType().equals("shoot")){
                gameHandler.receiveServerMessage(new MoveMessage(message.getToken(),gameHandler.getGame().getCurrentPlayer(),gameHandler.getGame().getMap().getSquareFromId(moveResponse.getSquareId())));
                historyMessage= new ArrayList<>();
                shootActionSequences(receiveTargetSquare);
            }
        }

        else if (message.getActionType().getAbbreviation().equals(ActionType.GRABWEAPON.getAbbreviation())){
            MoveResponse receiveTargetSquare = (MoveResponse) historyMessage.get(1);
            GrabWeapon grabWeapon = (GrabWeapon) message;
            boolean ex = gameHandler.getActionValidController().actionValid((SpawnSquare) gameHandler.getGame().getMap().getSquareFromId(receiveTargetSquare.getSquareId()) , ((GrabWeapon) message).getPositionWeapon());
            if (!ex){
                server.send(new UpdateClient(message.getToken(), "Action not done"));
                historyMessage=new ArrayList<>();
            }
            else{
                Integer[] cost = new Integer[3];
                cost[0]=gameHandler.getGame().getMap().getSquareFromId(receiveTargetSquare.getSquareId()).getWeapons().get(((GrabWeapon) message).getPositionWeapon()).getBlueCost();
                cost[1]=gameHandler.getGame().getMap().getSquareFromId(receiveTargetSquare.getSquareId()).getWeapons().get(((GrabWeapon) message).getPositionWeapon()).getYellowCost();
                cost[2]=gameHandler.getGame().getMap().getSquareFromId(receiveTargetSquare.getSquareId()).getWeapons().get(((GrabWeapon) message).getPositionWeapon()).getRedCost();
                server.send(new Payment(grabWeapon.getToken(),cost,false));
                historyMessage=new ArrayList<>();
                historyMessage.add(grabWeapon);
            }
        }

        else if (message.getActionType().getAbbreviation().equals(ActionType.RESPAWN.getAbbreviation())) {
            RespawnMessage respawnMessage = (RespawnMessage) message;
            //HASH table per repawn message
            players.get(respawnMessage.getToken()).calculateNewPosition(players.get(respawnMessage.getToken()).getPlayerBoard().getHandPlayer().getPlayerPowerUps().get(respawnMessage.getPowerUp()));
            server.send(new UpdateClient(respawnMessage.getToken(), players.get(respawnMessage.getToken()).getPosition()));
            historyMessage=new ArrayList<>();
        }
        else if(message.getActionType().getAbbreviation().equals(ActionType.RELOAD.getAbbreviation())){
            ReloadMessage reloadMessage=(ReloadMessage) message;
            if(gameHandler.getActionValidController().actionValid(reloadMessage.getWeapon())){
                Integer[] cost = new Integer[3];
                cost[0]=gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(reloadMessage.getWeapon()).getBlueCost();
                cost[1]=gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(reloadMessage.getWeapon()).getYellowCost();
                cost[2]=gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(reloadMessage.getWeapon()).getRedCost();
                server.send(new Payment(reloadMessage.getToken(), cost,false));
                //there is only reload's message in history
            }
            else
                server.send( new UpdateClient(reloadMessage.getToken(),"you can't recharge the weapon"));
        }
        //players.get(players.getId) in ingresso il token
        else if(message.getActionType().getAbbreviation().equals(ActionType.USEPOWERUPRESPONSE.getAbbreviation())){
            UsePowerUpResponse usePowerUpResponse=(UsePowerUpResponse) message;
            if(gameHandler.receiveServerMessage(new UsePowerUp(message.getToken(),usePowerUpResponse.getPowerUp(),players.get(usePowerUpResponse.getUser()),players.get(usePowerUpResponse.getUser()),usePowerUpResponse.getSquare()))){
                server.send( new UpdateClient(message.getToken(),"The use of the power up was successful"));
                historyMessage=new ArrayList<>();
            }
            else{
                server.send( new UpdateClient(message.getToken(),"The use of the power up was failure, PLEASE READ REGULATION"));
                historyMessage=new ArrayList<>();
            }
        }
        else if(message.getActionType().getAbbreviation().equals(ActionType.PASS.getAbbreviation())){
            gameHandler.receiveServerMessage(message);
            server.send( new UpdateClient(message.getToken(),"your turn is finish"));
            historyMessage=new ArrayList<>();

        }
        else if(message.getActionType().getAbbreviation().equals(ActionType.PAYMENTRESPONSE.getAbbreviation())) {
            PaymentResponse paymentResponse = (PaymentResponse) message;
            paymentServer(paymentResponse);
        }
        else if(message.getActionType().getAbbreviation().equals(ActionType.MESSAGE.getAbbreviation())) {
            ChatMessage chatMessage = (ChatMessage) message;
            clients.forEach(x -> server.send(new ChatMessage(x, chatMessage.getMessage())));
        }
        else if(message.getActionType().getAbbreviation().equals(ActionType.SUBSTITUTEWEAPONRESPONSE.getAbbreviation())){
            SubstituteWeaponResponse substituteWeaponResponse=(SubstituteWeaponResponse) message;
            players.get(substituteWeaponResponse.getToken()).getPlayerBoard().getHandPlayer().substituteWeapons(substituteWeaponResponse.getWeapon());
        }
    }

    public void paymentServer(PaymentResponse paymentResponse){
            boolean valueReturn;
            //No powerUp, no scoop
            if((!paymentResponse.isUsePowerUp()&& !paymentResponse.isScoop())){
                valueReturn=gameHandler.getPaymentController().payment(paymentResponse.getCost());
                if(valueReturn){
                    for(Message m:historyMessage)
                        gameHandler.receiveServerMessage(m);
                    historyMessage=new ArrayList<>(); //reset hystoryMessage fare update
                }
                else{
                    server.send(new Payment(paymentResponse.getToken(),paymentResponse.getCost(),paymentResponse.isScoop()));
                    server.send(new UpdateClient(paymentResponse.getToken(), "Payment failure:use the correct powerUp"));
                }
            }
            //no powerUp, si scoop
            else if(!paymentResponse.isUsePowerUp()&& paymentResponse.isScoop()){
                valueReturn=gameHandler.getPaymentController().payment(paymentResponse.getCost());
                if(valueReturn){
                    if(paymentResponse.getColorScoop()==null)
                        valueReturn=gameHandler.getPaymentController().paymentPowerUp(paymentResponse.getPowerUpScoop());
                    else
                        valueReturn=gameHandler.getPaymentController().paymentPowerUp(paymentResponse.getColorScoop());
                    if(valueReturn){ //if both payment are successful
                        gameHandler.receiveServerMessage(historyMessage.get(0));
                        historyMessage=new ArrayList<>();
                    }
                    else{//powerUp in not used
                        Shot messageResponse=(Shot) historyMessage.get(0);
                        messageResponse.setPowerUp(-1);
                        gameHandler.receiveServerMessage((messageResponse));
                        historyMessage=new ArrayList<>();
                        server.send(new UpdateClient(paymentResponse.getToken(), "Scoop is not used"));
                    }
                }
                else{
                    server.send(new Payment(paymentResponse.getToken(),paymentResponse.getCost(),paymentResponse.isScoop()));
                    server.send(new UpdateClient(paymentResponse.getToken(), "Payment failure:use the correct powerUp"));
                }
            }
            //si powerUp, si Scoop
            else if(paymentResponse.isUsePowerUp()&& paymentResponse.isScoop()){
                valueReturn=gameHandler.getPaymentController().payment(paymentResponse.getCost(),paymentResponse.getPowerUp());
                if(valueReturn){
                    if(paymentResponse.getColorScoop()==null)
                        valueReturn=gameHandler.getPaymentController().paymentPowerUp(paymentResponse.getPowerUpScoop());
                    else
                        valueReturn=gameHandler.getPaymentController().paymentPowerUp(paymentResponse.getColorScoop());
                    if(valueReturn){
                        gameHandler.receiveServerMessage(historyMessage.get(0));
                        historyMessage=new ArrayList<>();
                    }
                    else{
                        Shot messageResponse=(Shot) historyMessage.get(0);
                        messageResponse.setPowerUp(-1);
                        gameHandler.receiveServerMessage((messageResponse));
                        historyMessage=new ArrayList<>();
                        server.send(new UpdateClient(paymentResponse.getToken(), "Scoop is not used"));//reset update
                    }
                }
                else{
                    server.send(new Payment(paymentResponse.getToken(),paymentResponse.getCost(),paymentResponse.isScoop()));
                    server.send(new UpdateClient(paymentResponse.getToken(), "Payment failure:use the correct powerUp"));
                }
            }
            //si powerUp no scoop
            else if((paymentResponse.isUsePowerUp()&& !paymentResponse.isScoop())){
                valueReturn=gameHandler.getPaymentController().payment(paymentResponse.getCost(),paymentResponse.getPowerUp());
                if(valueReturn){
                    for(Message m:historyMessage)
                        gameHandler.receiveServerMessage(m);
                    historyMessage=new ArrayList<>(); //reset hystoryMessage fare update
                }
                else{
                    server.send(new UpdateClient(paymentResponse.getToken(), "Payment failure:use the correct powerUp"));
                    server.send(new Payment(paymentResponse.getToken(),paymentResponse.getCost(),paymentResponse.isScoop()));
                }
            }

    }

    public void respawn(Integer token, CardPowerUp powerUp){
        server.send(new UpdateClient(token, powerUp));
    }

    public void canUseScoop(Integer player){
        server.send(new CanUseScoop(player));
    }

    public void canUseTagBack(Integer player,Colors playerShooter){
        server.send(new CanUseTagBack(player,playerShooter));
    }

    public void endGame(Integer winner){
        for (Integer i: clients){
            if (!(i.equals(winner)))
                server.send(new WinnerMessage(i, false));
            else
                server.send(new WinnerMessage(i, true));
        }
    }

    public void shootActionSequences(ReceiveTargetSquare receiveTargetSquare){
        Effect effect = gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(receiveTargetSquare.getPosWeapon()).getEffects().get(receiveTargetSquare.getPosEffect());
        String actionSequence = effect.getActionSequence();
        int i = shootHistoryMessage.size() + 1;
        if (actionSequence.charAt(i) == 'p') {
            ArrayList<Integer> targetToken = new ArrayList<>();
            for (Player target: new Shoot(effect, gameHandler.getGame().getCurrentPlayer(), null).targetablePlayer()) {
                targetToken.add(target.getPlayerID());
            }
            server.send(new ShootRequestp(receiveTargetSquare.getToken(), effect.getTargetNumber(), targetToken));

        } else if (actionSequence.charAt(i) == 's') {
            ArrayList<String> targetID = new ArrayList<>();
            for (NormalSquare target: new Shoot(effect, gameHandler.getGame().getCurrentPlayer(), null).reachableSquare()) {
                targetID.add(target.getId());
            }
            server.send(new ShootRequests(receiveTargetSquare.getToken(), effect.getSquareNumber(), targetID));

        } else if (actionSequence.charAt(i) == 'r') {
            ArrayList<String> targetID = new ArrayList<>();
            for (NormalSquare target: new Shoot(effect, gameHandler.getGame().getCurrentPlayer(), null).reachableRoom()) {
                targetID.add(target.getId());
            }
            server.send(new ShootRequestr(receiveTargetSquare.getToken(), targetID));

        } else if (actionSequence.charAt(i) == 'm') {
            ArrayList<String> squareList = new ArrayList<>();
            Player target = players.get(shootHistoryMessage.get(shootHistoryMessage.size()).getToken());
            Integer move = gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(receiveTargetSquare.getPosWeapon()).getEffects().get(receiveTargetSquare.getPosEffect()).getTargetMove();
            for (NormalSquare square: new Move(target, null, move).reachableSquare()) {
                squareList.add(square.getId());
            }
            server.send(new TargetMoveRequest(receiveTargetSquare.getToken(), squareList));

        } else if (actionSequence.charAt(i) == 'M') {
            ArrayList<String> targetID = new ArrayList<>();
            for (NormalSquare target: new Move(gameHandler.getGame().getCurrentPlayer(), null, effect.getMyMove()).reachableSquare()) {
                targetID.add(target.getId());
            }
            server.send(new UpdateClient(receiveTargetSquare.getToken(), targetID));

        }
    }

    public void send(Message message){
        server.send(message);
    }
}