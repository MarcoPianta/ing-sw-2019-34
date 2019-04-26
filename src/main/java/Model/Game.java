package Model;

import java.security.SecureRandom;
import java.util.ArrayList;

public class Game {
    private  String gameId;
    private ArrayList<Player> players;
    private Player firstPlayer;
    private Player currentPlayer;
    private DeadRoute deadRoute;
    private DeckCollector deckCollector;

    public Game(String gameId,int n) {
        this.gameId=gameId;
        this.players=new ArrayList<>();
        this.currentPlayer=null;
        deadRoute=new DeadRoute(n,this);
        deckCollector= new DeckCollector();

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

    /**
     * this calculate who received points after kill
     * */
    public ArrayList<Player> calculatePoints(ArrayList<Player> playersMurder, boolean deadRoute, Player playerDead){
        int counter;
        int i;
        boolean isPresent;
        ArrayList<Player> bestMurder=new ArrayList<>(); //5 is right for both
        for(int n=0;n<5 ;n++)
            bestMurder.add(null);
        ArrayList<Integer> points= new ArrayList<>();
        for(int n=0;n<5 ;n++)
            points.add(0);

        for(Player player:playersMurder){
            if(notAlreadyView(player,bestMurder)){
                isPresent=false;
                counter=countPlayers(player,playersMurder);
                i=0;
                while ((i<4) && (!isPresent)){
                    if(counter>points.get(i)){
                        points.add(i,counter);
                        bestMurder.add(i,player);
                        isPresent=true;
                    }
                    else if(counter==points.get(i)){
                        while(counter==points.get(i)&&(!isPresent)){ //if there are more player with same counter
                            if(firstDamage(player,bestMurder.get(i),playersMurder)==player){
                                points.add(i,counter);
                                bestMurder.add(i,player);
                                isPresent=true;
                            }
                            else{
                                i++;
                            }
                        }
                        if(!isPresent){
                            points.add(i,counter);
                            bestMurder.add(i,player);
                            isPresent=true;
                        }
                    }
                    else
                        i++;
                }
            }
        }
        if(!deadRoute)
            addPlayerPoints(bestMurder,playerDead);
        else
            addDeadRoutePoints(bestMurder);
        return bestMurder;
    }
    public boolean notAlreadyView(Player player,ArrayList<Player> players){
        boolean isNotPresent=true;
        int i=0;
        while(i<players.size()&&(isNotPresent)){
            if(player==players.get(i)){
                isNotPresent=false;
                i++;

            }
            else
                i++;
        }
        return isNotPresent;
    }

    public Player firstDamage(Player player1,Player player2, ArrayList<Player> playerMurder){
        Player firstDamage=null;
        Boolean isPresent=false;
        int i=0;
        while (!isPresent) {
            if(playerMurder.get(i)==player1){
                firstDamage=player1;
                isPresent=true;
            }
            else if(playerMurder.get(i)==player2){
                firstDamage=player2;
                isPresent=true;
            }
            else
                i++;
        }
        return firstDamage;
    }


    public int countPlayers(Player player,ArrayList<Player> playersMurder){
        int counterPlayer=0;
        for( Player players : playersMurder ){
            if (players==player)
                counterPlayer++;
        }
        return counterPlayer;

    }
    public void addPlayerPoints(ArrayList<Player> bestMurder,Player thisPlayer){
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

    public void addDeadRoutePoints(ArrayList<Player> bestMurder){
        int newPoints=0;
        int reward=8;
        for (Player player: players){
            if(bestMurder.indexOf(player)!=-1){
                newPoints=reward-2*(bestMurder.indexOf(player));
                if(newPoints<=0)
                    newPoints=1;
            }
            player.getPlayerBoard().addPoints(newPoints);
        }
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

