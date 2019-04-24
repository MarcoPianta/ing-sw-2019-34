package Model;

import java.security.SecureRandom;
import java.util.ArrayList;

public class Game {
    private  String gameId;
    private ArrayList<Player> players;
    private Player firstPlayer;
    private Player currentPlayer;
    private DeadRoute deadRoute;

    public Game(String gameId,int n) {
        this.gameId=gameId;
        this.players=new ArrayList<Player>();
        this.currentPlayer=null;
        deadRoute=new DeadRoute(n,this);
    }

    public DeadRoute getDeadRoute() {
        return deadRoute;
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

    /*public ArrayList<UtilPlayer> addPlayerInUtilPlayer(ArrayList<Colors> players){
        int counter;
        boolean isPresent;
        for(UtilPlayer player: players){
            counter=0;
            isPresent=false;
            while(counter < getPlayers().size() || (!isPresent)){
                if(player.getColor()==getPlayers().get(counter).getColor()){
                    player.setPlayer(getPlayers().get(counter));
                    isPresent=true;
                }
                else
                    counter++;
            }
        }
        return players;
    }
*/
    public void addPlayerPoints(ArrayList<Player> bestMurder,Player thisPlayer){
        int reward=thisPlayer.getPlayerBoard().getMaxReward();
        int newPoints=0;
        for (Player player: players){
            if(bestMurder.indexOf(player)!=-1){
                newPoints=thisPlayer.getPlayerBoard().getMaxReward()-2*(bestMurder.indexOf(player));
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

