package Model;

import java.util.ArrayList;

public class PlayerBoard {

    private Colors color;
    private String playerName;
    private int maxReward;// indicates the maximum reward obtainable
    private ArrayList<Colors> damageBar;
    private int adrenalineAction; //the only values are 0,1,2
    private ArrayList<CardWeapon> playerWeapons;
    private ArrayList<CardWeapon> playerOffloadWeapons;
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
        this.playerOffloadWeapons=new ArrayList<CardWeapon>();
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


    public ArrayList<Colors> getDamageBar() {
        return damageBar;
    }

    public int[] getAmmoRYB() {
        return ammoRYB;
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

    public ArrayList<CardWeapon> getPlayerOffloadWeapons() {
        return playerOffloadWeapons;
    }

    public ArrayList<CardPowerUp> getPlayerPowerUps() {
        return playerPowerUps;
    }

    //** this method reset damageBar after a dead
    private void resetDamageBar() {
        int i=0;
        while( damageBar.size()!=0)
            damageBar.remove(i);
        adrenalineAction=0;
        decrementMaxReward();

    }

    //**this method add new weapon after grab cardWeapon and decrement ammo
    public void addWeapon(CardWeapon weapon) {
        int red=weapon.getRedCost();
        int yellow=weapon.getYellowCost();
        int blue=weapon.getBlueCost();
        if(playerWeapons.size()==3) {
            //TODO throws exception
        }
        else
            playerWeapons.add(playerWeapons.size(),weapon);
        if(weapon.getColor()==AmmoColors.BLUE)
            blue--;
        else if(weapon.getColor()==AmmoColors.YELLOW)
            yellow--;
        else if(weapon.getColor()==AmmoColors.RED)
            red--;

        decrementAmmo(red,yellow,blue);

    }

    public void addOffloadWeapon(CardWeapon weapon){
        playerOffloadWeapons.add(weapon);
    }

    private void removeOffloadWeapon(CardWeapon weapon){
        boolean isPresent=false ;
        int i=0;
        while((isPresent=false)){
            if(getPlayerOffloadWeapons().get(i).equals(weapon)){
                getPlayerOffloadWeapons().remove(i);
                isPresent=true;
                }
            i++;
        }
    }

    public void  chargeWeapon(CardWeapon weapon){
        removeOffloadWeapon(weapon);
        decrementAmmo(weapon.getRedCost(),weapon.getYellowCost(),weapon.getBlueCost());

    }

    //**this method substitute weapons when the player has three weapons and wants a new weapon
    public void substituteWeapons(ArrayList<CardWeapon> newPlayerWeapons){

        for(int i=0; i<3 ;i++){
            playerWeapons.set(i,newPlayerWeapons.get(i));
        }
    }

    public void addPowerUp(CardPowerUp powerUp) {

        if(playerPowerUps.size()==3)
            //TODO throws exception but this condition is checked by controller
        playerPowerUps.add(playerPowerUps.size(),powerUp);// we can delete index

    }

    private void removePowerUp(CardPowerUp powerUp){
        boolean isPresent=false ;
        int i=0;
        while(isPresent=false ){
            if(getPlayerPowerUps().get(i)== powerUp) {
                getPlayerPowerUps().remove(i);
                isPresent=true;
            }
            i++;
        }
    }

    //**This method add new ammo after grab cardAmmo
    public void addAmmo(int red, int yellow, int blue) {
        ammoRYB[0]= ammoRYB[0] +red;
        if(ammoRYB[0]>3)
            ammoRYB[0]=3;
        ammoRYB[1]=ammoRYB[1] +yellow;
        if(ammoRYB[1]>3)
            ammoRYB[1]=3;
        ammoRYB[2]=ammoRYB[2] +blue;
        if(ammoRYB[2]>3)
            ammoRYB[2]=3;

    }
    //** This method decrement the value of array ammoRYB, exception controllate da controller
    private void decrementAmmo(int red,int yellow,int blue){
        ammoRYB[0]-=red;
        ammoRYB[1]-=yellow;
        ammoRYB[2]-=blue;
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

        for( int i = 0 ; (i < d + countMarks(color)) && (damageBar.size() < 12);i++){
            damageBar.add(color);
        }

        resetMark(color);

        if(damageBar.size()>=3 || damageBar.size()<6 )
            adrenalineAction++;
        if(damageBar.size()>=6)
            adrenalineAction++;
        if(damageBar.size()>=11) {
            //TODO  Throws exception death send player's damageboard and maxrewards
            resetDamageBar();
        }
    }

    //addMark adds colors to arraylist mark based on the input marks
    public void addMark(Colors color, int m) {
        int i=0;
        while( (i<m) && (countMarks(color)<3)){
            mark.add(color);
            i++;
        }

    }

    //reset mark after adddamage
    private void resetMark(Colors color){
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