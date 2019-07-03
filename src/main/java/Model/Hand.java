package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Hand implements Serializable {
    private ArrayList<CardWeapon> playerWeapons;
    private ArrayList<CardPowerUp> playerPowerUps;
    private int[] ammoRYB;
    private  PlayerBoard playerBoard;

    public Hand(PlayerBoard playerBoard){
        this.playerWeapons = new ArrayList<>();
        this.playerPowerUps = new ArrayList<>();
        this.ammoRYB= new int[3];
        this.playerBoard=playerBoard;
    }

    public List<CardPowerUp> getPlayerPowerUps() {
        return playerPowerUps;
    }

    public List<CardWeapon> getPlayerWeapons() {
        return playerWeapons;
    }

    public int[] getAmmoRYB() {
        return ammoRYB;
    }

    public PlayerBoard getPlayerBoard() {
        return playerBoard;
    }

    /**
     * this method add new weapon after grab cardWeapon and decrement ammo
     * */
    public void addWeapon(CardWeapon weapon) {
        playerWeapons.add(playerWeapons.size(),weapon);

    }

    /**
     * this method set charge true in a weapon and  decrement its cost
     * */
    public void  chargeWeapon(CardWeapon weapon,int red, int yellow, int blue){
        weapon.setCharge(true);
    }

    /**
     *this method remove an offload weapon and pay its cost with ammo and power up
     * */
     public void  chargeWeapon(CardWeapon weapon,int red, int yellow, int blue, List<CardPowerUp> powerUp){
        weapon.setCharge(true);
        for(CardPowerUp p:powerUp){
            if(p.getColor()==AmmoColors.RED)
                red--;
            else if(p.getColor()==AmmoColors.YELLOW)
                yellow--;
            else if(p.getColor()==AmmoColors.BLUE)
                blue--;
            removePowerUp(getPlayerPowerUps().indexOf(p));
        }
        decrementAmmo(red,yellow,blue);
    }

    /**
     *this method substitute weapons when the player has three weapons and wants a new weapon
     * */
    public void substituteWeapons(int weaponPosition){
        playerWeapons.remove(weaponPosition);

    }

    /**
     * this method add a power up after when the player grabs its when he doesn't have more than three power up
     * */
    public void addPowerUp(CardPowerUp powerUp) {
        playerPowerUps.add(powerUp);// we can delete index
    }

    /**
     * this method use power up and remove it
     * */
    public void usePowerUp(CardPowerUp cardPowerUp,Player target,NormalSquare square,int pos){
        if(cardPowerUp.getDamage()==1)
            target.getPlayerBoard().getHealthPlayer().addDamage(getPlayerBoard().getPlayer(),1);
        /*else if(cardPowerUp.getMark()==1){
            target.getPlayerBoard().getHealthPlayer().addMark(getPlayerBoard().getPlayer(),1);//il target è luser
        }*/
        else if(cardPowerUp.getMyMove()==-1){
            getPlayerBoard().getPlayer().newPosition(square);}
        else if(cardPowerUp.getOtherMove()==2)
            new Move(target,square,2).execute();

        removePowerUp(pos);
    }

    /**
     * This method remove a power up after use its
     * */
    public void removePowerUp(int position){
        System.out.println(getPlayerPowerUps().size());
        getPlayerPowerUps().remove(position);
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
    public void decrementAmmo(int red,int yellow,int blue){
        ammoRYB[0]-=red;
        ammoRYB[1]-=yellow;
        ammoRYB[2]-=blue;
    }
}
