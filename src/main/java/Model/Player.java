package Model;

import java.util.ArrayList;

public class Player {

    private String playerID;
    private String name;
    private int actionCounter;
    private PlayerBoard playerBoard;
    private Square position;
    private Game gameId;

    public Player(String playerID,Game gameId, Colors color,String name) {
        this.playerID=playerID;
        this.name=name;
        this.playerBoard=new PlayerBoard(color , name, this);
        this.actionCounter=2;
        this.position=null;
        this.gameId=gameId;
    }

    public Player(){}

    public PlayerBoard getPlayerBoard() {
        return playerBoard;
    }

    public Square getPosition() {
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
    public ArrayList<Colors> calculatePoints(ArrayList<Colors> playersMurder){
        int counter;
        int i;
        boolean isPresent;
        ArrayList<Colors> bestMurder=new ArrayList<>();
        for(int n=0;n<4 ;n++)
            bestMurder.add(null);
        ArrayList<Integer> points= new ArrayList<>();
        for(int n=0;n<4 ;n++)
            points.add(0);

        for(Colors color:playersMurder){
            if(notAlreadyView(color,bestMurder)){
                isPresent=false;
                counter=countColors(color,playersMurder);
                i=0;
                while ((i<4) && (!isPresent)){
                    if(counter>points.get(i)){
                        points.add(i,counter);
                        bestMurder.add(i,color);
                        isPresent=true;
                    }
                    else if(counter==points.get(i)){
                        while(counter==points.get(i)&&(!isPresent)){ //if there are more player with same counter
                            if(firstDamage(color,bestMurder.get(i),playersMurder)==color){
                                points.add(i,counter);
                                bestMurder.add(i,color);
                                isPresent=true;
                            }
                            else{
                                i++;
                            }
                        }
                        if(!isPresent){
                            points.add(i,counter);
                            bestMurder.add(i,color);
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
    public boolean notAlreadyView(Colors color,ArrayList<Colors> colors){
        boolean isNotPresent=true;
        int i=0;
        while(i<colors.size()&&(isNotPresent)){
            if(color==colors.get(i)){
                isNotPresent=false;
                i++;

            }
            else
                i++;
        }
        return isNotPresent;
    }

    public Colors firstDamage(Colors color1,Colors color2, ArrayList<Colors> playerMurder){
        Colors firstDamage=null;
        Boolean isPresent=false;
        int i=0;
        while (!isPresent) {
            if(playerMurder.get(i)==color1){
                firstDamage=color1;
                isPresent=true;
            }
            else if(playerMurder.get(i)==color2){
                firstDamage=color2;
                isPresent=true;
            }
            else
                i++;
        }
        return firstDamage;
    }


    public int countColors(Colors color,ArrayList<Colors> playersMurder){
        int counterColors=0;
        for( Colors colors : playersMurder ){
            if (colors==color)
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
    public void newPosition(Square newPosition){
        position=newPosition;

    }

}
