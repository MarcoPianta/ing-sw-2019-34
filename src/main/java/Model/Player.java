package Model;

public class Player {

    private String playerID;
    private String name;
    private int actionCounter;
    private PlayerBoard playerBoard;
    private Square position;

    public Player(String playerID, Colors color,String name) {
        this.playerID=playerID;
        this.name=name;
        this.playerBoard=new PlayerBoard(color , name);
        this.actionCounter=2;
        this.position=null;
    }
    public String getPlayerID() {
        return playerID;
    }

    public PlayerBoard getPlayerBoard() {
        return playerBoard;
    }

    public Square getPosition() {
        return position;
    }

    public int getActionCounter() {
        return actionCounter;
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
    }

    /*
     *this method modified a player's position
     * */
    public void newPosition(Square newPosition){
        position=newPosition;

    }

}
