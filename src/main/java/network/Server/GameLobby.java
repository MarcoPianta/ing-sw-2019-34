package network.Server;

import Controller.GameHandler;
import Model.CardPowerUp;
import Model.Move;
import Model.Player;
import Model.SpawnSquare;
import network.messages.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

public class GameLobby {
    private HashMap<Integer, Player> players;
    private ArrayList<Integer> clients;
    private Server server;
    private GameHandler gameHandler;
    private Integer currentPlayer; //token of the current player
    private ArrayList<Message> historyMessage;
    private boolean useScoop;

    private String currentSquare;
    private

    public GameLobby(ArrayList<Integer> clients, int skullNumber, String map, Server server){
        this.clients = clients;
        this.server = server;
        this.currentPlayer = null;
        this.players = new HashMap<>();
        this.historyMessage = new ArrayList<>();
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
            }
        }

        else if (message.getActionType().getAbbreviation().equals(ActionType.GRABWEAPON.getAbbreviation())){
            MoveResponse receiveTargetSquare = (MoveResponse) historyMessage.get(1);
            GrabWeapon grabWeapon = (GrabWeapon) message;
            boolean ex = gameHandler.getActionValidController().actionValid((SpawnSquare) gameHandler.getGame().getMap().getSquareFromId(receiveTargetSquare.getSquareId()) , ((GrabWeapon) message).getPositionWeapon());
            if (!ex)server.send(new UpdateClient(message.getToken(), "Action not done"));
        }

        else if (message.getActionType().getAbbreviation().equals(ActionType.RESPAWN.getAbbreviation())) {
            RespawnMessage respawnMessage = (RespawnMessage) message;
            //players.get(respawnMessage.getToken()).calculateNewPosition(respawnMessage.getPowerUp());
            server.send(new UpdateClient(respawnMessage.getToken(), players.get(respawnMessage.getToken()).getPosition()));
        }
    }

    public void respawn(Integer token, CardPowerUp powerUp){
        server.send(new UpdateClient(token, powerUp));
    }

    public void canuseScoop(Integer player){
        useScoop = true;
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