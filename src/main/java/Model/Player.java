package Model;

import java.util.ArrayList;

public class Player {

    private String playerID;
    private String name;
    private Colors color;
    private int actionCounter;
    private PlayerBoard playerBoard;
    private NormalSquare position;
    private Game gameId;

    public Player(String playerID,Game gameId, Colors color,String name) {
        this.playerID=playerID;
        this.name=name;
        this.color=color;
        this.playerBoard=new PlayerBoard(color , name, this);
        this.actionCounter=2;
        this.position=null;
        this.gameId=gameId;
    }

    public Player(){}

    public PlayerBoard getPlayerBoard() {
        return playerBoard;
    }

    public NormalSquare getPosition() {
        return position;
    }

    public int getActionCounter() {
        return actionCounter;
    }

    public Game getGameId() {
        return gameId;
    }

    /**
     * this calculate who received points after kill
     * */
    public ArrayList<Player> calculatePoints(ArrayList<Player> playersMurder){
        int counter;
        int i;
        boolean isPresent;
        ArrayList<Player> bestMurder=new ArrayList<>();
        for(int n=0;n<4 ;n++)
            bestMurder.add(null);
        ArrayList<Integer> points= new ArrayList<>();
        for(int n=0;n<4 ;n++)
            points.add(0);

        for(Player player:playersMurder){
            if(notAlreadyView(player,bestMurder)){
                isPresent=false;
                counter=countColors(player,playersMurder);
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
        //getGameId().addPlayerPoints(bestMurder,this);
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


    public int countColors(Player player,ArrayList<Player> playersMurder){
        int counterColors=0;
        for( Player players : playersMurder ){
            if (players==player)
                counterColors++;
        }
        return counterColors;

    }

    /*
     *this method  return true o false if the player can do an action
     * */
    public boolean canAct(){
        if(this.getActionCounter() == 0)
            return false;
        else
            return true;
    }

    /*
     * this method increment action counter
     * */
    public void decrementActionCounter(){
        actionCounter-=1;
        if (actionCounter==0) {//TODO called controller for change game(currentPlayer)
        }
    }

    /*
     *this method modified a player's position
     * */
    public void newPosition(NormalSquare newPosition){
        position=newPosition;

    }

}
