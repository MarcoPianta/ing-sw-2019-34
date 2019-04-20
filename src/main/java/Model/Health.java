package Model;

import java.util.ArrayList;

public class Health {
    private ArrayList<Colors> damageBar;
    private int adrenalineAction;
    private ArrayList<Colors> mark;
    private PlayerBoard playerBoard;

    public Health( PlayerBoard playerBoard){
        damageBar= new ArrayList<Colors>();
        mark=new ArrayList<Colors>();
        this.playerBoard=playerBoard;
        adrenalineAction=0;
    }

    public ArrayList<Colors> getDamageBar() {
        return damageBar;
    }

    public int getAdrenalineAction() {
        return adrenalineAction;
    }

    public ArrayList<Colors> getMark() {
        return mark;
    }

    public PlayerBoard getPlayerBoard() {
        return playerBoard;
    }

    /**
     * this method set a death
     * */
    public void death(){
        ArrayList<Colors> oldDamageBar= new ArrayList<>();
        resetDamageBar();
        getPlayerBoard().getPlayer().calculatePoints(oldDamageBar);
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
    public int countMarks(Colors color){
        int counterMarks=0;
        for( Colors colors : mark ){
            if (colors==color)
                counterMarks++;
        }
        return counterMarks;
    }

    /**
     * This method adds colors to the damagedBar based on damage and increment adrenalineAction
     * */
    public void addDamage(Colors color, int d) {

        for( int i = 0 ; (i < d + countMarks(color)) && (damageBar.size() < 12);i++){
            damageBar.add(color);
        }
        resetMark(color);

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
    public void addMark(Colors color, int m) {
        int i=0;
        while( (i<m) && (countMarks(color)<3)){
            mark.add(color);
            i++;
        }
    }

    /**
     * reset mark after add damage
     * */
    private void resetMark(Colors color){
        int i=0;
        while(countMarks(color)!=0) {
            if(mark.get(i)==color)
                mark.remove(i);
        }
    }
}
