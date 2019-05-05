package network.Server;

import Model.Player;
import network.messages.Message;

import java.util.ArrayList;

public class GameLobby {
    private ArrayList<Player> players;
    private ArrayList<Client> clients;

    public GameLobby(ArrayList<Player> players, ArrayList<Client> clients){
        this.players = players;
        this.clients = clients;
    }

    public ArrayList<Client> getClients() {
        return clients;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void receiveMessage(Message message){

    }
}
