package Model;

import java.util.ArrayList;

public class PlayerBoard {

    private Colors color;
    private String playerName;
    private int maxReward;// indicates the maximum reward obtainable
    private ArrayList<Colors> damageBar;
    private int damegeBarCounter;
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
        this.damegeBarCounter = 0;
        this.adrenalineAction = 0;
        this.playerWeapons = new ArrayList<CardWeapon>();
        this.playerPowerUps = new ArrayList<CardPowerUp>();
        this.points = 0;
        this.mark = new ArrayList<Colors>();
        this.ammoRYB= new int[3];
        for( int i = 0 ; i<3;i++)
            ammoRYB[i]=0;
    }

    public Colors getColor() {
        return color;
    }

    public int getDamegeBarCounter() {
        return damegeBarCounter;
    }
    //send damageBar to the controller for damage calculation and reset damageBar
    public ArrayList<Colors> getDamageBar() {
        resetDamageBar();
        return damageBar;

    }

    public ArrayList<CardWeapon> getPlayerWeapons() {
        return playerWeapons;
    }

    //reset damageBar after a dead
    private void resetDamageBar() {

        for( int i = 0 ; i<damageBar.size();i++)
            damageBar.remove(i);

    }
    //add new weapon after grad cardWeapon
    public void addWeapon(CardWeapon weapon) {
        if(playerWeapons.size()==3)
            //TODO throws exception
        playerWeapons.add(playerWeapons.size(),weapon);// we can delete index

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
    private int countMarks(Colors color){
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

        for( int i = 0 ; (i<d + counterMark) && (damegeBarCounter <= 12);i++){
            damageBar.add(damegeBarCounter,color);
            damegeBarCounter++;
        }
        if(damegeBarCounter>=3)
            adrenalineAction++;
        if(damegeBarCounter>=6)
            adrenalineAction++;
        if(damegeBarCounter==11) {
            //TODO  Throws exception death
        }
    }

    //addMark adds colors to arraylist mark based on the input marks
    public void addMark(Colors color, int m) {

        for( int i = 0 ; i<m;i++){
            mark.add(mark.size(),color);
        }

    }



}