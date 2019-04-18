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

    public void addPlayerPoints(ArrayList<Colors> bestMurder,Player thisPlayer){
        int reward=thisPlayer.getPlayerBoard().getMaxReward();
        int newPoints=0;
        for (Player player: players){
            if(bestMurder.indexOf(player.getPlayerBoard().getColor())!=-1){
                newPoints=thisPlayer.getPlayerBoard().getMaxReward()-2*(bestMurder.indexOf(player.getPlayerBoard().getColor()));
                if(newPoints<=0)
                    newPoints=1;
            }
            player.getPlayerBoard().addPoints(newPoints);
        }
        thisPlayer.getPlayerBoard().decrementMaxReward();
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

