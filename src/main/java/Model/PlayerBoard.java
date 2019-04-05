package Model;

import java.util.ArrayList;

public class PlayerBoard {

    private Colors color;
    private String playerName;
    private int maxReward;// indicates the maximum reward obtainable
    private ArrayList<Colors> damageBar;
    private int adrenalineAction; //the only values are 0,1,2
    private ArrayList<CardWeapon> playerWeapons;
    private ArrayList<CardPowerUp> playerPowerUps;
    private int points;
    private ArrayList<Colors> mark;
    private int[] ammoRYB;

    public PlayerBoard(Colors color,String playerName){
        this.color=color;
        this.playerName = playerName;
        this.damageBar = new ArrayList<Colors>();
        this.adrenalineAction = 0;
        this.playerWeapons = new ArrayList<CardWeapon>();
        this.playerPowerUps = new ArrayList<CardPowerUp>();
        this.maxReward=8;
        this.points = 0;
        this.mark = new ArrayList<Colors>();
        this.ammoRYB= new int[3];
        for( int i = 0 ; i<3;i++)
            ammoRYB[i]=0;
    }

    public Colors getColor() {
        return color;
    }

    public String getPlayerName() {
        return playerName;
    }


    //send damageBar to the controller for damage calculation and reset damageBar
    public ArrayList<Colors> getDamageBar() {
        return damageBar;

    }

    public int getMaxReward() {
        return maxReward;
    }


    public int getAdrenalineAction() {
        return adrenalineAction;
    }

    public int getPoints() {
        return points;
    }

    public ArrayList<CardWeapon> getPlayerWeapons() {

        return playerWeapons;
    }



    public ArrayList<CardPowerUp> getPlayerPowerUps() {
        return playerPowerUps;
    }

    //reset damageBar after a dead
    protected void resetDamageBar() {
        int i=0;
        while( damageBar.size()!=0)
            damageBar.remove(i);
        adrenalineAction=0;
        decrementMaxReward();

    }
    //add new weapon after grad cardWeapon
    public void addWeapon(CardWeapon weapon) {
        if(playerWeapons.size()==3){}
            //TODO throws exception
        playerWeapons.add(weapon);// we can delete index

    }

    public void substituteWeapons(ArrayList<CardWeapon> newPlayerWeapons){
        for(int i=0; i<3 ;i--){
            playerWeapons.set(i,newPlayerWeapons.get(i));
        }
    }

    public void addPowerUp(CardPowerUp powerUp) {
        if(playerPowerUps.size()==3)
            //TODO throws exception but this condition is checked by controller
        playerPowerUps.add(playerPowerUps.size(),powerUp);// we can delete index

    }
    //add new ammo after grab cardAmmo
    public void addAmmo(int r, int y, int b) {
        ammoRYB[0]= ammoRYB[0] +r;
        if(ammoRYB[0]>3)
            ammoRYB[0]=3;
        ammoRYB[1]=ammoRYB[1] +y;
        if(ammoRYB[1]>3)
            ammoRYB[1]=3;
        ammoRYB[2]=ammoRYB[2] +b;
        if(ammoRYB[2]>3)
            ammoRYB[2]=3;

    }
    //countMarks returns the number of the mark for the input color
    public int countMarks(Colors color){
        int counterMarks=0;

        for( Colors colors : mark ){
            if (colors==color)
                counterMarks++;
        }
        return counterMarks;
    }
    // addDamage adds colors to the damagedBar based on damage and increment adrenalineAction
    public void addDamage(Colors color, int d) {
        int counterMark = countMarks(color);
        resetMark(color);

        for( int i = 0 ; (i < d + counterMark) && (damageBar.size() < 12);i++){
            damageBar.add(color);
        }
        if(damageBar.size()>=3 || damageBar.size()<6 )
            adrenalineAction++;
        if(damageBar.size()>=6)
            adrenalineAction++;
        if(damageBar.size()==11) {
            //TODO  Throws exception death
        }
    }

    //addMark adds colors to arraylist mark based on the input marks
    public void addMark(Colors color, int m) {

        for( int i = 0 ; i<m;i++){
            mark.add(mark.size(),color);
        }

    }

    //reset mark after adddamage
    public void resetMark(Colors color){
        int i=0;
        while(countMarks(color)!=0) {
            if(mark.get(i)==color)
                mark.remove(i);
        }

    }
    //decrement maxReward after death
    public void decrementMaxReward(){
        if(getMaxReward()!=1)
            maxReward-=2;
    }

    public void addPoints(int newPoints){
        points+= newPoints;
    }



}