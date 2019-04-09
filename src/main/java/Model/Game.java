package Model;

import java.security.SecureRandom;
import java.util.ArrayList;

public class Game {
    private  String gameId;
    private ArrayList<Player> players;
    private Player firstPlayer;
    private Player currentPlayer;

    public Game(String gameId) {
        this.gameId=gameId;
        this.players=new ArrayList<Player>();
        this.currentPlayer=null;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Player getFirstPlayer() {
        return firstPlayer;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * this method add a new player
     * */
    public void addPlayer(Player newPlayer){
        //TODO  SERVER MUST CREATE A NEW PLAYER
        players.add(newPlayer);
    }

    /**
     * this method increment the current player
     * */
    public void incrementCurrentPlayer(){
        if(players.size()==(players.indexOf(currentPlayer)+1))
            currentPlayer=players.get(0);
        else
            currentPlayer=players.get(players.indexOf(currentPlayer)+1);
    }

    /**
     *this method choose the first player
     * */
    public void chooseFirstPlayer(){
        SecureRandom rand = new SecureRandom();
        int index;
        index = rand.nextInt(players.size());
        firstPlayer=players.get(index);
        currentPlayer=players.get(index);
    }
}

