package network.Server;

import Controller.GameHandler;
import Model.*;
import network.messages.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameLobby {
    private HashMap<Integer, Player> players;
    private ArrayList<Integer> clients;
    private Server server;
    private GameHandler gameHandler;
    private Integer currentPlayer; //token of the current player
    private ArrayList<Message> historyMessage;
    private boolean useScoop;

    private String currentSquare;

    public GameLobby(ArrayList<Integer> clients, int skullNumber, String map, Server server){
        this.clients = clients;
        this.server = server;
        this.currentPlayer = null;
        this.players = new HashMap<>();
        this.historyMessage = new ArrayList<>();
        try {
            this.gameHandler = new GameHandler(skullNumber, this.clients, map,this);
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
        if (currentPlayer != null)
            server.send(new EndMessage(currentPlayer));
        server.send(new StartMessage(token, "turn", 0, ""));
        currentPlayer = token;
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
            server.send(gameHandler.firstPartAction(receiveTargetSquare));
        }

        else if (message.getActionType().equals(ActionType.SHOTRESPONSE)){
            //Alla fine dell'effetto controllo
            //Leggere effetto e chaimare is valid
        }

        else if (message.getActionType().equals(ActionType.MOVERESPONSE)){ // anche per grab se spawn quale carta vuole, se shot chiedere chi vuole sparare chiamando receive target
            MoveResponse moveResponse = (MoveResponse) message;
            ReceiveTargetSquare receiveTargetSquare = (ReceiveTargetSquare) historyMessage.get(0);
            if (receiveTargetSquare.getType().equals("grab")){
                if (!gameHandler.getGame().getMap().getSquareFromId(moveResponse.getSquareId()).isSpawn()) {
                    gameHandler.receiveServerMessage(new MoveMessage(message.getToken(), gameHandler.getGame().getMap().getSquareFromId(moveResponse.getSquareId())));
                    gameHandler.receiveServerMessage(new GrabAmmo(message.getToken()));
                    historyMessage = new ArrayList<>();
                }
                else{
                    server.send(new GrabWeaponRequest(message.getToken()));
                }
            }
            else if (receiveTargetSquare.getType().equals("move")){
                gameHandler.receiveServerMessage(new MoveMessage(message.getToken(), gameHandler.getGame().getMap().getSquareFromId(moveResponse.getSquareId())));
                historyMessage= new ArrayList<>();
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
                int[] cost = new int[3];
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
                int[] cost = new int[3];
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
}