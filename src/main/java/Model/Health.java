package Model;

import java.util.ArrayList;
import java.util.List;

public class Health {
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
    private void death(){

        getPlayerBoard().getPlayer().getGameId().calculatePoints(getDamageBar(), false,getPlayerBoard().getPlayer());
        getDamageBar().get(0).getPlayerBoard().addPoints(1);
        if(getDamageBar().size()==12){
            getPlayerBoard().getPlayer().getGameId().getDeadRoute().addMurders(getDamageBar().get(10),2);
            getDamageBar().get(11).getPlayerBoard().getHealthPlayer().addMark(this.getPlayerBoard().getPlayer(),1);
        }
        else
            getPlayerBoard().getPlayer().getGameId().getDeadRoute().addMurders(getDamageBar().get(10),1);
        //TODO respawn getPlayerBoard().getHandPlayer().addPowerUp(getPlayerBoard().getPlayer().getGameId().getDeckCollector().getCardPowerUpDrawer().draw());
        resetDamageBar();
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
            adrenalineAction++;
        if(damageBar.size()>=6)
            adrenalineAction++;
        if(damageBar.size()>=11) {
            death();
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
        int i=0;
        while(countMarks(player)!=0) {
            if(mark.get(i)==player)
                mark.remove(i);
        }
    }
}
