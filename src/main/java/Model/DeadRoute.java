package Model;

import java.util.ArrayList;
import java.util.List;

public class DeadRoute {
    private int skulls;
    private ArrayList<Player> murders;
    private Game gameId;
    private boolean finalTurn;

    public DeadRoute(int n, Game gameId){
        skulls=n;
        murders=new ArrayList<>();
        this.gameId=gameId;
    }

    public Game getGameId() {
        return gameId;
    }

    public int getSkulls() {
        return skulls;
    }

    public List<Player> getMurders() {
        return murders;
    }

    public boolean isFinalTurn() {
        return finalTurn;
    }

    public void setFinalTurn(boolean finalTurn) {
        this.finalTurn = finalTurn;
    }

    /*
     *This method adds a murder in the dead route
     * */
    public void addMurders(Player player, int counter){
        while(counter >0){
            getMurders().add(player);
            counter--;
        }
        skulls--;
        if(skulls<=0){
            finalTurn=true;
        }
    }

    public void setFinalTurnPlayer(){
        for(Player p:getGameId().getPlayers()){
            if(p.getPlayerBoard().getHealthPlayer().getDamageBar().isEmpty())
                p.getPlayerBoard().setMaxReward(2);
        }
    }
    //start finalTurn
}
