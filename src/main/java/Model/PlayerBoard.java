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

    /**
     * this method reset damageBar after a death
     * */
    private void resetDamageBar() {
        int i=0;
        while( damageBar.size()!=0)
            damageBar.remove(i);
        adrenalineAction=0;
        decrementMaxReward();

    }

    /**
     * this method add new weapon after grab cardWeapon and decrement ammo
     * */
    public void addWeapon(CardWeapon weapon) {
        if(playerWeapons.size()==3) {
            //TODO throws exception
        }
        else
            playerWeapons.add(playerWeapons.size(),weapon);

    }

    /**
     * This method add an offload weapon after shot with it
     * */
    public void addOffloadWeapon(CardWeapon weapon){
        playerOffloadWeapons.add(weapon);
    }

    /**
     * This method remove an offload weapon after  charge it
     * */
    private void removeOffloadWeapon(CardWeapon weapon){
        boolean isPresent=false ;
        int i=0;
        while((!isPresent)){
            if(getPlayerOffloadWeapons().get(i).equals(weapon)){
                getPlayerOffloadWeapons().remove(i);
                isPresent=true;
                }
            i++;
        }
    }

    /**
     * this method remove an offload weapon e decrement its cost
     * */
    public void  chargeWeapon(CardWeapon weapon,int red, int yellow, int blue){
        removeOffloadWeapon(weapon);

        decrementAmmo(red,yellow,blue);
    }

    /**
     *this method remove an offload weapon and pay its cost with ammo and power up
     * */
    public void  chargeWeapon(CardWeapon weapon,int red, int yellow, int blue, CardPowerUp powerUp){
        removeOffloadWeapon(weapon);
        if(powerUp.getColor()==AmmoColors.RED)
            red--;
        else if(powerUp.getColor()==AmmoColors.YELLOW)
            yellow--;
        else if(powerUp.getColor()==AmmoColors.BLUE)
            blue--;
        decrementAmmo(red,yellow,blue);
        removePowerUp(powerUp);
    }



    /**
     *this method substitute weapons when the player has three weapons and wants a new weapon
     * */
    public void substituteWeapons(ArrayList<CardWeapon> newPlayerWeapons){

        for(int i=0; i<3 ;i++){
            playerWeapons.set(i,newPlayerWeapons.get(i));
        }
    }

    /**
     * this method add a power up after when the player grabs its when he doesn't have more than three power up
     * */
    public void addPowerUp(CardPowerUp powerUp) {
        playerPowerUps.add(playerPowerUps.size(),powerUp);// we can delete index

    }

    /**
     * this method use power up and remove it
     * */
/*    public void usePowerUp(Action action,CardPowerUp powerUp){
        action.runAction();
        removePowerUp(powerUp);

    }
    */

    /**
     * This method remove a power up after use its
     * */
    private void removePowerUp(CardPowerUp powerUp){
        boolean isPresent=false ;
        int i=0;
        while(!isPresent ){
            if(getPlayerPowerUps().get(i)== powerUp) {
                getPlayerPowerUps().remove(i);
                isPresent=true;
            }
            i++;
        }
    }

    /**
     * This method add new ammo after grab cardAmmo
     * */
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

    /**
     * This method decrement the value of array ammoRYB, exception by controller
     * */
    private void decrementAmmo(int red,int yellow,int blue){
        ammoRYB[0]-=red;
        ammoRYB[1]-=yellow;
        ammoRYB[2]-=blue;
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
            //TODO  Throws exception death send player's damageboard and maxrewards
            resetDamageBar();
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

    /**
     * decrement maxReward after death
     * */
    public void decrementMaxReward(){
        if(getMaxReward()!=1)
            maxReward-=2;
    }

    /**
     * this method add point after a other player's death
     * */
    public void addPoints(int newPoints){
        points+= newPoints;
    }



}