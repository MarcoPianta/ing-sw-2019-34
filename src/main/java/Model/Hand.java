package Model;

import java.util.ArrayList;

public class Hand {
    private ArrayList<CardWeapon> playerWeapons;
    private ArrayList<CardPowerUp> playerPowerUps;
    private int[] ammoRYB;
    private PlayerBoard playerBoard;

    public Hand(PlayerBoard playerBoard){
        this.playerWeapons = new ArrayList<>();
        this.playerPowerUps = new ArrayList<CardPowerUp>();
        this.ammoRYB= new int[3];
        this.playerBoard=playerBoard;
        for( int i = 0 ; i<3;i++)
            ammoRYB[i]=0;
    }

    public ArrayList<CardPowerUp> getPlayerPowerUps() {
        return playerPowerUps;
    }

    public ArrayList<CardWeapon> getPlayerWeapons() {
        return playerWeapons;
    }

    public int[] getAmmoRYB() {
        return ammoRYB;
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
     * this method set charge true in a weapon and  decrement its cost
     * */
    public void  chargeWeapon(CardWeapon weapon,int red, int yellow, int blue){
        weapon.setCharge(true);
        decrementAmmo(red,yellow,blue);
    }

    /**
     *this method remove an offload weapon and pay its cost with ammo and power up
     * */
    public void  chargeWeapon(CardWeapon weapon,int red, int yellow, int blue, CardPowerUp powerUp){
        weapon.setCharge(true);
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
    public void usePowerUp(CardPowerUp powerUp, Player targetPlayer){
        //TODO CALLED CONTROLLER FOR USE
        removePowerUp(powerUp);
    }

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
}
