package Model;

import java.util.ArrayList;

public class Health {
    private ArrayList<Player> damageBar;
    private int adrenalineAction;
    private ArrayList<Player> mark;
    private PlayerBoard playerBoard;

    public Health( PlayerBoard playerBoard){
        damageBar= new ArrayList<Player>();
        mark=new ArrayList<Player>();
        this.playerBoard=playerBoard;
        adrenalineAction=0;
    }

    public ArrayList<Player> getDamageBar() {
        return damageBar;
    }

    public int getAdrenalineAction() {
        return adrenalineAction;
    }

    public ArrayList<Player> getMark() {
        return mark;
    }

    public PlayerBoard getPlayerBoard() {
        return playerBoard;
    }

    /**
     * this method set a death
     * */
    public void death(){
        ArrayList<Player> oldDamageBar= new ArrayList<>();
        oldDamageBar=getDamageBar();
        resetDamageBar();
        getPlayerBoard().getPlayer().calculatePoints(oldDamageBar);
        /*getPlayerBoard().getPlayer().getGameId().firstBlood(oldDamageBar.get(0));
        if(oldDamageBar.get(10)==oldDamageBar.get(11))
            getPlayerBoard().getPlayer().getGameId().getDeadRoute().addMurders(new UtilPlayer(null,oldDamageBar.get(10),2));
        else
            getPlayerBoard().getPlayer().getGameId().getDeadRoute().addMurders(new UtilPlayer(null,oldDamageBar.get(10),1));
*/

    }

    /**
     * this method reset damageBar after a death
     * */
    private void resetDamageBar() {
        int i=0;
        while( damageBar.size()!=0)
            damageBar.remove(i);
        adrenalineAction=0;
    }

    /**
     * This method returns the number of the mark for the input color
     * */
    public int countMarks(Player player){
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

        if(damageBar.size()>=3 || damageBar.size()<6 )
            adrenalineAction++;
        if(damageBar.size()>=6)
            adrenalineAction++;
        if(damageBar.size()>=11) {
            death();

            // va in metodo morte che restituisce damagebar come copia o faccio conto io e restituisco array(TODOdiventerà hash), metto boolean detah a true
            //resetto damage bar e decremento max reward
            //e aggiungo un metodo in player per sapere se è morto o no
            //nel game  aggiungo scalata teschi come damage e contatore teschi
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
