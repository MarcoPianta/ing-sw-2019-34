package network.Server;

import Controller.ActionValidController;
import Controller.GameHandler;
import Model.*;
import network.messages.*;

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

    public GameLobby(ArrayList<Integer> clients, int skullNumber, String map, Server server){
        this.clients = clients;
        this.server = server;
        this.currentPlayer = null;
        this.players = new HashMap<>();
        this.playersColor=new HashMap<>();
        this.historyMessage = new ArrayList<>();
        this.actionPerformed = new HashMap<>();
        this.clients.forEach(x -> actionPerformed.put(x, false));
        try {
            this.gameHandler = new GameHandler(skullNumber, this.clients, map, this);
            for(Player p: gameHandler.getGame().getPlayers()){
                players.put(p.getPlayerID(), p);
                playersColor.put(p.getColor(),p.getPlayerID());
            }
        }catch (FileNotFoundException e){
            for (Integer i: clients){
                server.send(new UpdateClient(i, "Server internal error, unable to create a new game"));
            }
        }
        this.actionValidController = gameHandler.getActionValidController();
        clients.parallelStream()
                .forEach(x -> send(new StartMessage(x, "game", skullNumber, map, players.get(x).getColor())));
        gameHandler.fillSquare();
        gameHandler.getTurnHandler().start();
    }

    public void startTurn(Integer token){
        actionPerformed.replace(token, false);
        if (currentPlayer != null)
            server.send(new EndMessage(currentPlayer));
        currentPlayer = token;
        server.send(new StartMessage(token, "turn", 0, "", null));
        //startTimer();
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
        System.out.println("Ho ricevuto un messaggio " + historyMessage.get(0).getActionType().getAbbreviation());
        if(message.getActionType().getAbbreviation().equals(ActionType.MESSAGE.getAbbreviation())) {
            ChatMessage chatMessage = (ChatMessage) message;
            clients.forEach(x -> server.send(new ChatMessage(x, chatMessage.getMessage())));
            historyMessage.remove(historyMessage.size()-1);
        }
        else if (message.getActionType().equals(ActionType.RECEIVETARGETSQUARE)) {
            ReceiveTargetSquare receiveTargetSquare = (ReceiveTargetSquare) message;
            gameHandler.firstPartAction(receiveTargetSquare);
        }
        else if(message.getActionType().equals(ActionType.CANUSESCOOPRESPONSE)){
            useScoop=true;
        }

        else if (message.getActionType().equals(ActionType.SHOOTRESPONSEP)){
            ReceiveTargetSquare receiveTargetSquare = (ReceiveTargetSquare) historyMessage.get(0);
            ShootResponsep shootResponsep = (ShootResponsep) message;
            ArrayList<Player> targetPlayer = new ArrayList<>();
            Effect effect = gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(receiveTargetSquare.getPosWeapon()).getEffects().get(receiveTargetSquare.getPosEffect());
            for (Colors color: shootResponsep.getTargetPlayer()) {
                targetPlayer.add(players.get(playersColor.get(color)));
            }
            if(actionValidController.actionValid(targetPlayer, effect, -1)){
                shootHistoryMessage.add(new Shot(targetPlayer, receiveTargetSquare.getPosEffect(), receiveTargetSquare.getPosWeapon()));
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
            targetSquare.add(gameHandler.getGame().getMap().getSquareFromId(shootResponses.getTargetSquare()));
            if (actionValidController.actionValid(targetSquare, effect, -1)){
                shootHistoryMessage.add(new Shot(targetSquare, receiveTargetSquare.getPosEffect(), receiveTargetSquare.getPosWeapon()));
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
            if (actionValidController.actionValid(targetRoom, effect, -1)){
                shootHistoryMessage.add(new Shot(targetRoom, receiveTargetSquare.getPosEffect(), receiveTargetSquare.getPosWeapon()));
                shootActionSequences(receiveTargetSquare);
            }
            //TODO else --> dico che non è valida
            if(gameHandler.getGame().getPlayers().get(currentPlayer).getPlayerBoard().getHandPlayer().getPlayerWeapons().get(receiveTargetSquare.getPosWeapon()).getEffects().get(receiveTargetSquare.getPosEffect()).getActionSequence().length() == shootHistoryMessage.size()){
                historyMessage = new ArrayList<>(shootHistoryMessage);
                shootHistoryMessage = null;
                server.send(new Payment(currentPlayer, effect.getBonusCost(), false)); //TODO gestire PowerUp
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
                    if (gameHandler.receiveServerMessage(new GrabAmmo(message.getToken()))) {
                        server.send(new UpdateClient(message.getToken(), "You grab ammos"));
                    }
                    historyMessage = new ArrayList<>();
                }
                else{
                   server.send(new UpdateClient(message.getToken(), gameHandler.getGame().getMap().getSquareFromId(moveResponse.getSquareId())));
                   clients.parallelStream().
                           filter(x -> (!x.equals(message.getToken()))).
                           forEach(x -> gameHandler.getGameLobby().send(new UpdateClient(x, players.get(moveResponse.getToken()).getColor(), players.get(moveResponse.getToken()).getPosition())));

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
            MoveResponse moveResponse = (MoveResponse) historyMessage.get(1);
            GrabWeapon grabWeapon = (GrabWeapon) message;
            boolean ex = gameHandler.getActionValidController().actionValid((SpawnSquare) gameHandler.getGame().getMap().getSquareFromId(moveResponse.getSquareId()) , ((GrabWeapon) message).getPositionWeapon());
            if (!ex){
                server.send(new UpdateClient(message.getToken(), "Action not done"));
                historyMessage=new ArrayList<>();
            }
            else{
                Integer[] cost = new Integer[3];
                cost[0]=gameHandler.getGame().getMap().getSquareFromId(moveResponse.getSquareId()).getWeapons().get(((GrabWeapon) message).getPositionWeapon()).getRedCost();
                cost[1]=gameHandler.getGame().getMap().getSquareFromId(moveResponse.getSquareId()).getWeapons().get(((GrabWeapon) message).getPositionWeapon()).getYellowCost();
                cost[2]=gameHandler.getGame().getMap().getSquareFromId(moveResponse.getSquareId()).getWeapons().get(((GrabWeapon) message).getPositionWeapon()).getBlueCost();
                if(gameHandler.getGame().getMap().getSquareFromId(moveResponse.getSquareId()).getWeapons().get(((GrabWeapon) message).getPositionWeapon()).getColor().getAbbreviation().equals("red") && cost[0] > 0){
                    cost[0]--;
                }
                else if(gameHandler.getGame().getMap().getSquareFromId(moveResponse.getSquareId()).getWeapons().get(((GrabWeapon) message).getPositionWeapon()).getColor().getAbbreviation().equals("yellow") && cost[1] > 0){
                    cost[1]--;
                }
                else if(cost[2] > 0){
                    cost[2]--;
                }
                server.send(new Payment(grabWeapon.getToken(),cost,false));
                historyMessage=new ArrayList<>();
                historyMessage.add(grabWeapon);
            }
        }

        else if (message.getActionType().getAbbreviation().equals(ActionType.RESPAWN.getAbbreviation())) {
            RespawnMessage respawnMessage = (RespawnMessage) message;
            players.get(message.getToken()).getPlayerBoard().getHandPlayer().getPlayerPowerUps().stream().forEach(x -> System.out.println(x.getName()));
            System.out.println("Dopo ");
            players.get(respawnMessage.getToken()).calculateNewPosition(players.get(respawnMessage.getToken()).getPlayerBoard().getHandPlayer().getPlayerPowerUps().get(respawnMessage.getPowerUp()));
            server.send(new UpdateClient(respawnMessage.getToken(), players.get(respawnMessage.getToken()).getPosition()));
            clients.parallelStream()
                    .filter(x -> (!x.equals(respawnMessage.getToken())))
                    .forEach(x -> send(new UpdateClient(x, players.get(respawnMessage.getToken())
                            .getColor(), players.get(respawnMessage.getToken()).getPosition())));
            players.get(message.getToken()).getPlayerBoard().getHandPlayer().getPlayerPowerUps().stream().forEach(x -> System.out.println(x.getName()));
            server.send(new UpdateClient(message.getToken(), players.get(message.getToken()).getPlayerBoard().getHandPlayer().getAmmoRYB()[0], players.get(message.getToken()).getPlayerBoard().getHandPlayer().getAmmoRYB()[1], players.get(message.getToken()).getPlayerBoard().getHandPlayer().getAmmoRYB()[2], players.get(message.getToken()).getPlayerBoard().getHandPlayer().getPlayerWeapons(), players.get(message.getToken()).getPlayerBoard().getHandPlayer().getPlayerPowerUps()));

            historyMessage=new ArrayList<>();
        }
        else if(message.getActionType().getAbbreviation().equals(ActionType.RELOAD.getAbbreviation())){
            ReloadMessage reloadMessage=(ReloadMessage) message;
            if(gameHandler.getActionValidController().actionValid(reloadMessage.getWeapon())){
                Integer[] cost = new Integer[3];
                cost[0]=gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(reloadMessage.getWeapon()).getRedCost();
                cost[1]=gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(reloadMessage.getWeapon()).getYellowCost();
                cost[2]=gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(reloadMessage.getWeapon()).getBlueCost();
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
                    if(historyMessage.get(0).getActionType()!=ActionType.RELOAD || historyMessage.get(0).getActionType()!=ActionType.USEPOWERUP){
                        gameHandler.getTurnHandler().endAction();
                        server.send(new FinalAction(paymentResponse.getToken()));
                    }
                    historyMessage=new ArrayList<>();//reset hystoryMessage fare update

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
                        if(historyMessage.get(0).getActionType()!=ActionType.RELOAD || historyMessage.get(0).getActionType()!=ActionType.USEPOWERUP){
                            gameHandler.getTurnHandler().endAction();
                            server.send(new FinalAction(paymentResponse.getToken()));
                        }
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
                        if(historyMessage.get(0).getActionType()!=ActionType.RELOAD || historyMessage.get(0).getActionType()!=ActionType.USEPOWERUP){
                            gameHandler.getTurnHandler().endAction();
                            server.send(new FinalAction(paymentResponse.getToken()));
                            //increment  numerAction in view
                        }
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
                    if(historyMessage.get(0).getActionType()!=ActionType.RELOAD || historyMessage.get(0).getActionType()!=ActionType.USEPOWERUP){
                        gameHandler.getTurnHandler().endAction();
                        server.send(new FinalAction(paymentResponse.getToken()));
                    }
                    historyMessage=new ArrayList<>(); //reset hystoryMessage fare update
                }
                else{
                    server.send(new UpdateClient(paymentResponse.getToken(), "Payment failure:use the correct powerUp"));
                    server.send(new Payment(paymentResponse.getToken(),paymentResponse.getCost(),paymentResponse.isScoop()));
                }
            }

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
            ArrayList<Colors> targetToken = new ArrayList<>();
            for (Player target: new Shoot(effect, gameHandler.getGame().getCurrentPlayer(), null).targetablePlayer()) {
                targetToken.add(target.getColor());
            }
            server.send(new ShootRequestp(receiveTargetSquare.getToken(), effect.getTargetNumber(), targetToken));

        } else if (actionSequence.charAt(i) == 's') {
            ArrayList<String> targetID = new ArrayList<>();
            for (NormalSquare target: new Shoot(effect, gameHandler.getGame().getCurrentPlayer(), null).reachableSquare()) {
                targetID.add(target.getId());
            }
            server.send(new ShootRequests(receiveTargetSquare.getToken(), targetID));

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
            server.send(new UpdateClient(receiveTargetSquare.getToken(), new Move(gameHandler.getGame().getCurrentPlayer(), null, effect.getMyMove()).reachableSquare()));

        }
    }

    public void send(Message message){
        server.send(message);
    }
}