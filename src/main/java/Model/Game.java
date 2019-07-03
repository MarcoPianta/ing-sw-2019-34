package Model;

import Controller.StateMachineEnumerationTurn;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class Game implements Serializable {
    private ArrayList<Player> players;
    private Player firstPlayer;
    private Player currentPlayer;
    private DeadRoute deadRoute;
    private DeckCollector deckCollector;
    private ArrayList<Player> deadPlayer;
    private GameBoard map;

    public Game(int n,String file) throws FileNotFoundException {
        this.players=new ArrayList<>();
        this.currentPlayer=null;
        deadRoute=new DeadRoute(n,this);
        deckCollector= new DeckCollector();
        deadPlayer=new ArrayList<>();
        map=new GameBoard(file);
    }

    public GameBoard getMap() {
        return map;
    }

    public DeadRoute getDeadRoute() {
        return deadRoute;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Player getFirstPlayer() {
        return firstPlayer;
    }

    public List<Player> getDeadPlayer() {
        return deadPlayer;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    //for test
    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public  DeckCollector getDeckCollector() {
        return deckCollector;
    }

    /**
     * this method calculates who received points after kill
     * */
    public void calculatePoints(List<Player> damagedBar, boolean deadRoute, Player playerDead){
        int counter;
        int i=0;
        ArrayList<UtilPlayer> bestMurder=new ArrayList<>();
        while(bestMurder.size()<5)
            bestMurder.add(new UtilPlayer(null,0));

        for(Player player:damagedBar){
            if(notAlreadyView(player,bestMurder)){
                counter=countPlayers(player,damagedBar);
                bestMurder.set(i,new UtilPlayer(player,counter));
                i++;
            }
        }
        insertionSort(bestMurder,new ArrayList<>(bestMurder));

        if(!deadRoute)
            addPlayerPoints(createPlayers(bestMurder),playerDead);
        else
            addDeadRoutePoints(createPlayers(bestMurder));
    }

    /*
     * This method sorts the input array based on who has done the most damage
     * */
    private void insertionSort (List<UtilPlayer> playersMurder,List<UtilPlayer> damagedBar){
        for (int i = 1; i < playersMurder.size(); i++) {
            for (int j = i; j > 0; j--) {

                if((playersMurder.get(j - 1).getCounter() <  playersMurder.get(j).getCounter()) ||
                        ((playersMurder.get(j - 1).getCounter()== playersMurder.get(j).getCounter()) &&
                                (firstDamage(playersMurder.get(j - 1).getPlayer(),playersMurder.get(j).getPlayer(),damagedBar)==playersMurder.get(j).getPlayer()))){
                    setUtilPlayer(playersMurder,j);
                }
            }
        }
    }

    /*
     * this method set the players in order if they satisfy the insertionSort's conditions
     * */
    private void setUtilPlayer(List<UtilPlayer> playersMurder, int j){
        UtilPlayer temp;
        temp = playersMurder.get(j);
        playersMurder.set(j,playersMurder.get(j-1));
        playersMurder.set(j - 1, temp);
    }

    /*
     * this method creates a list of players from a list of UtilPlayer
     * */
    private  List<Player> createPlayers(List<UtilPlayer> players) {
        ArrayList<Player> newPlayers = new ArrayList<>();
        for (UtilPlayer player : players)
            newPlayers.add(player.getPlayer());
        return newPlayers;
    }

    /*
     * this method evaluates who did damaged first
     * */
    private Player firstDamage(Player player1,Player player2, List<UtilPlayer> playerMurder){
        Player firstDamage=null;
        boolean isPresent=false;
        int i=0;
        while (!isPresent) {
            if(playerMurder.get(i).getPlayer()==player1){
                firstDamage=player1;
                isPresent=true;
            }
            else if(playerMurder.get(i).getPlayer()==player2){
                firstDamage=player2;
                isPresent=true;
            }
            else
                i++;
        }
        return firstDamage;
    }

    /*
     * this method evaluates whether a player has already been considered in the array creation
     * */
    private boolean notAlreadyView(Player player,List<UtilPlayer> players){
        boolean isNotPresent=true;
        int i=0;
        while(i<players.size()&&(isNotPresent)){
            if(player==players.get(i).getPlayer()){
                isNotPresent=false;
                i++;
            }
            else
                i++;
        }
        return isNotPresent;
    }

    /*
     * this method counts how many damage does a  single player
     * */
    private int countPlayers(Player player,List<Player> playersMurder){
        int counterPlayer=0;
        for( Player p : playersMurder ){
            if (p==player)
                counterPlayer++;
        }
        return counterPlayer;
    }

    /*
     * this method adds players' points after a calculation of points after a player's death
     * */
    private void addPlayerPoints(List<Player> bestMurder,Player thisPlayer){
        int newPoints=0;
        for (Player player: players){
            if(bestMurder.indexOf(player)!=-1){
                newPoints=thisPlayer.getPlayerBoard().getMaxReward()-2*(bestMurder.indexOf(player));
                if(newPoints<=0)
                    newPoints=1;
            }
            else
                newPoints=0;
            player.getPlayerBoard().addPoints(newPoints);
        }
        if(getDeadRoute().isFinalTurn()) {
            thisPlayer.getPlayerBoard().setMaxReward(2);
            thisPlayer.getPlayerBoard().setFinalTurn(true);
        }
        thisPlayer.getPlayerBoard().decrementMaxReward();
    }

    /*
     * This method adds points when the dead route is empty
     * */
    private void addDeadRoutePoints(List<Player> bestMurder){
        int newPoints=0;
        int reward=8;
        for (Player player: players){
            if(bestMurder.indexOf(player)!=-1){
                newPoints=reward-2*(bestMurder.indexOf(player));
                if(newPoints<=0)
                    newPoints=1;
            }
            else
                newPoints=0;
            player.getPlayerBoard().addPoints(newPoints);
        }
    }

    /**
     * this method add a new player
     * */
    public void addPlayer(Player newPlayer){
        players.add(newPlayer);
        newPlayer.setGameId(this);
    }

    /**
     * this method increment the current player
     * */
    public void incrementCurrentPlayer(){
        System.out.println("qui1 " + players);
        if(players.size()==(players.indexOf(currentPlayer)+1)) {
            currentPlayer = players.get(0);
            System.out.println("qui2");
        }
        else {
            currentPlayer = players.get(players.indexOf(currentPlayer) + 1);
            System.out.println("qui3");
        }
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
        currentPlayer.setState(StateMachineEnumerationTurn.START);
    }

    /*
     * This nested class is used for simplify the calculation of points
     * */
    class UtilPlayer{
        private  Player player;
        private int counter;

        public UtilPlayer(Player player, int counter){
            this.player=player;
            this.counter=counter;
        }

        public int getCounter() {
            return counter;
        }

        public Player getPlayer() {
            return player;
        }
    }
}

