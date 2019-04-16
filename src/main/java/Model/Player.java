package Model;

public class Player {

    private String playerID;
    private String name;
    private int actionCounter;
    private PlayerBoard playerBoard;
    private NormalSquare position;
    private String gameId;

    public Player(String playerID,String gameId, Colors color,String name) {
        this.playerID=playerID;
        this.name=name;
        this.playerBoard=new PlayerBoard(color , name);
        this.actionCounter=2;
        this.position=null;
        this.gameId=gameId;
    }
    public String getPlayerID() {
        return playerID;
    }

    public PlayerBoard getPlayerBoard() {
        return playerBoard;
    }

    public NormalSquare getPosition() {
        return position;
    }

    public int getActionCounter() {
        return actionCounter;
    }

    public String getGameId() {
        return gameId;
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
