package network.Server;

import Controller.GameHandler;
import Model.NormalSquare;
import Model.Player;
import network.messages.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class GameLobby {
    private ArrayList<Player> players;
    private ArrayList<Integer> clients;
    private Server server;
    private GameHandler gameHandler;

    public GameLobby(ArrayList<Integer> clients, int skullNumber, String map, Server server){
        this.clients = clients;
        this.server = server;
        try {
            this.gameHandler = new GameHandler(skullNumber, this.clients, map);
        }catch (FileNotFoundException e){
            //TODO error message to clients
        }
    }

    public ArrayList<Integer> getClients() {
        return clients;
    }

    public ArrayList<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    public void receiveMessage(Message message){

        if (message.getActionType().getAbbreviation().equals(ActionType.POSSIBLETARGETSHOT.getAbbreviation())) {

            ArrayList<Player> targets = gameHandler.receiveTarget(((PossibleTargetShot) message));
            server.send(new UpdateClient(message.getToken()));
        }

        else if (message.getActionType().getAbbreviation().equals(ActionType.POSSIBLEMOVE.getAbbreviation())) {

            List<NormalSquare> squares = gameHandler.receiveSquare((PossibleMove) message);
            server.send(new UpdateClient(message.getToken()));
        }

        else if (message.getActionType().getAbbreviation().equals(ActionType.SHOT.getAbbreviation())) {

            boolean done = gameHandler.receiveServerMessage(message);
            Shot m = (Shot) message;
            if (done){
                for (Player p: m.getTargets()) {
                    server.send(new UpdateClient(p.getPlayerID(), p.getPlayerBoard().getHealthPlayer().getDamageBar()));
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
            ReloadMessage m = (ReloadMessage) message;
            server.send(new ReloadedMessage(message.getToken(), m.getWeapon(), done));
        }

        else if (message.getActionType().getAbbreviation().equals(ActionType.PASS.getAbbreviation())) {

            gameHandler.receiveServerMessage(message);
        }

        else if (message.getActionType().getAbbreviation().equals(ActionType.GRABWEAPON.getAbbreviation())) {

            boolean done = gameHandler.receiveServerMessage(message);
            GrabWeapon m = (GrabWeapon) message;
            server.send(new GrabWeaponResponse(message.getToken(), m.getCard(), done));
        }

        else if (message.getActionType().getAbbreviation().equals(ActionType.GRABAMMO.getAbbreviation())) {

            boolean done = gameHandler.receiveServerMessage(message);
            GrabAmmo m = (GrabAmmo) message;
            server.send(new GrabResponse(message.getToken(), m.getCard(), done));
        }

        else if (message.getActionType().getAbbreviation().equals(ActionType.GRABNOTONLYAMMO.getAbbreviation())) {

            boolean done = gameHandler.receiveServerMessage(message);
            GrabNotOnlyAmmo m = (GrabNotOnlyAmmo) message;
            server.send(new GrabResponse(message.getToken(), m.getCard(), done));
        }

        else if (message.getActionType().getAbbreviation().equals(ActionType.USEPOWERUP.getAbbreviation())) {

        }
    }

    //TODO add method to receive from controller
}
/*
* quando in start arriva messagio con giocatore corrente e lista giocatori morti, faccio rinascere tutti e poi faccio iniziare il turno
* */