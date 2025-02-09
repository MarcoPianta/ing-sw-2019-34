package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Health implements Serializable {
    private ArrayList<Player> damageBar;
    private int adrenalineAction;
    private ArrayList<Player> mark;
    private PlayerBoard playerBoard;

    public Health( PlayerBoard playerBoard){
        damageBar= new ArrayList<>();
        mark=new ArrayList<>();
        this.playerBoard=playerBoard;
        adrenalineAction=0;
    }

    public List<Player> getDamageBar() {
        return new ArrayList<>(damageBar);
    }

    public int getAdrenalineAction() {
        return adrenalineAction;
    }

    public List<Player> getMark() {
        return mark;
    }

    public PlayerBoard getPlayerBoard() {
        return playerBoard;
    }

    /**
     * this method set a player's death and  execute the calculation of points
     * */
    public void death(){
        getPlayerBoard().getPlayer().getGameId().calculatePoints(getDamageBar(), false,getPlayerBoard().getPlayer());
        if(!getPlayerBoard().isFinalTurn()) // first blood
            getDamageBar().get(0).getPlayerBoard().addPoints(1);

        if(getDamageBar().size()==12){
            getPlayerBoard().getPlayer().getGameId().getDeadRoute().addMurders(getDamageBar().get(10),2);
            getDamageBar().get(11).getPlayerBoard().getHealthPlayer().addMark(this.getPlayerBoard().getPlayer(),1);
        }
        else if(getDamageBar().size()==11)
            getPlayerBoard().getPlayer().getGameId().getDeadRoute().addMurders(getDamageBar().get(10),1);

        if(getPlayerBoard().getPlayer().getGameId().getDeadPlayer().size()>=2)//double kill
            getDamageBar().get(10).getPlayerBoard().addPoints(1);
        resetDamageBar();
        getPlayerBoard().getPlayer().newPosition(null);
    }

    /**
     * this method reset damageBar after a death
     * */
    private void resetDamageBar() {
        int i=0;
        while( !damageBar.isEmpty())
            damageBar.remove(i);
        adrenalineAction=0;
    }

    /**
     * This method returns the number of the mark for the input color
     * */
    private int countMarks(Player player){
        int counterMarks=0;
        for( Player players : mark ){
            if (players==player)
                counterMarks++;
        }
        return counterMarks;
    }

    /**
     * This method adds colors to the damagedBar based on damage and increment adrenalineAction
     * */
    public void addDamage(Player player, int d) {

        for( int i = 0 ; (i < d + countMarks(player)) && (damageBar.size() < 12);i++){
            damageBar.add(player);
        }
        resetMark(player);

        if(damageBar.size()>=3 && damageBar.size()<6 )
            adrenalineAction=1;
        if(damageBar.size()>=6 && damageBar.size()<11)
            adrenalineAction=2;
        if(damageBar.size()>=11) {
            getPlayerBoard().getPlayer().getGameId().getDeadPlayer().add(getPlayerBoard().getPlayer());
            adrenalineAction=2;

        }
    }

    /**
     * This method adds colors to arraylist mark based on the input marks
     * */
    public void addMark(Player player, int m) {
        int i=0;
        while( (i<m) && (countMarks(player)<3)){
            mark.add(player);
            i++;
        }
    }

    /**
     * reset mark after add damage
     * */
    private void resetMark(Player player){
        ArrayList<Player> markCopy = new ArrayList<>(mark);
        for (Player p: markCopy){
            if(p == player)
                mark.remove(p);
        }
        /*while(countMarks(player)!=0) {
            if(mark.get(i)==player)
                mark.remove(i);
        }*/
    }
}
