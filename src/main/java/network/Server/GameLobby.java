package network.Server;

import Controller.GameHandler;
import Model.CardPowerUp;
import Model.NormalSquare;
import Model.Player;
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

    public GameLobby(ArrayList<Integer> clients, int skullNumber, String map, Server server){
        this.clients = clients;
        this.server = server;
        this.currentPlayer = null;
        this.players = new HashMap<>();
        try {
            this.gameHandler = new GameHandler(skullNumber, this.clients, map);
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
        server.send(new StartMessage(token, "turn"));
        currentPlayer = token;
    }

    public ArrayList<Integer> getClients() {
        return clients;
    }

    public HashMap<Integer, Player> getPlayers() {
        return players;
    }

    public void receiveMessage(Message message){

        if (message.getActionType().getAbbreviation().equals(ActionType.POSSIBLETARGETSHOT.getAbbreviation())) {

            ArrayList<Player> targets = gameHandler.receiveTarget(((PossibleTargetShot) message));
            server.send(new UpdateClient(message.getToken(), targets));
        }

        else if (message.getActionType().getAbbreviation().equals(ActionType.POSSIBLEMOVE.getAbbreviation())) {

            List<NormalSquare> squares = gameHandler.receiveSquare((PossibleMove) message);
            server.send(new UpdateClient(message.getToken(), squares));
        }

        else if (message.getActionType().getAbbreviation().equals(ActionType.SHOT.getAbbreviation())) {

            boolean done = gameHandler.receiveServerMessage(message);
            Shot m = (Shot) message;
            if (done){
                for (Player p: m.getTargets()) {
                    server.send(new UpdateClient(p.getPlayerID(), p.getPlayerBoard().getHealthPlayer().getDamageBar().toArray(new Player[12])));
                }
            }
        }

        else if (message.getActionType().getAbbreviation().equals(ActionType.MOVE.getAbbreviation())) {

            boolean done = gameHandler.receiveServerMessage(message);
            MoveMessage m = (MoveMessage) message;
            if (done)
                server.send(new UpdateClient(message.getToken(), m.getNewSquare()));

        }

        else if (message.getActionType().getAbbreviation().equals(ActionType.RELOAD.getAbbreviation())) {

            boolean done = gameHandler.receiveServerMessage(message);
            ReloadedMessage m = (ReloadedMessage) message;
            server.send(new ReloadedMessage(message.getToken(), m.getWeapon(), done));
        }

        else if (message.getActionType().getAbbreviation().equals(ActionType.PASS.getAbbreviation())) {

            gameHandler.receiveServerMessage(message);
        }

        else if (message.getActionType().getAbbreviation().equals(ActionType.GRABWEAPON.getAbbreviation())) {

            boolean done = gameHandler.receiveServerMessage(message);
            GrabWeapon m = (GrabWeapon) message;
            server.send(new GrabWeaponResponse(m.getToken(), m.getPositionWeapon(), done));
        }

        else if (message.getActionType().getAbbreviation().equals(ActionType.GRABAMMO.getAbbreviation())) {

            boolean done = gameHandler.receiveServerMessage(message);
            GrabAmmo m = (GrabAmmo) message;
            //server.send(new GrabResponse(message.getToken(), m.getCard(), done));
        }

        else if (message.getActionType().getAbbreviation().equals(ActionType.GRABNOTONLYAMMO.getAbbreviation())) {

            boolean done = gameHandler.receiveServerMessage(message);
            GrabNotOnlyAmmo m = (GrabNotOnlyAmmo) message;
            //server.send(new GrabResponse(message.getToken(), m.getCard(), done));
        }

        else if (message.getActionType().getAbbreviation().equals(ActionType.USEPOWERUP.getAbbreviation())) {
            gameHandler.receiveServerMessage(message);
        }

        else if (message.getActionType().getAbbreviation().equals(ActionType.RESPAWN.getAbbreviation())) {
            RespawnMessage respawnMessage = (RespawnMessage) message;
            players.get(respawnMessage.getToken()).calculateNewPosition(respawnMessage.getPowerUp());
            server.send(new UpdateClient(respawnMessage.getToken(), players.get(respawnMessage.getToken()).getPosition()));
        }
    }

    public void respawn(Integer token, CardPowerUp powerUp){
        server.send(new UpdateClient(token, powerUp));
    }

    public void canUseTagBack(Integer player){
        server.send(new CanUseTagBack(player));
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