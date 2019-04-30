package Model;

import java.util.ArrayList;

/**
 * This class extends NormalSquare and describe a NormalSquare type
 * */
public class SpawnSquare extends NormalSquare{

    private ArrayList<CardWeapon> weapons;

    public SpawnSquare(NormalSquare SideN, NormalSquare SideE, NormalSquare SideS, NormalSquare SideW, Colors color, ArrayList<CardWeapon> weapons) {
        super(SideN, SideE, SideS, SideW, color, null);
        this.weapons = new ArrayList<>();
        for (CardWeapon c: weapons){
            if (c != null)
                this.weapons.add(c);
        }
        spawn = true;
    }

    public SpawnSquare(){
        super();
        this.weapons = new ArrayList<>();
        spawn = true;
    }

    public void addWeapon(CardWeapon weapon) {
        if (weapons.size() < 3)
            if(weapon != null)
                weapons.add(weapon);
        else {
            //TODO throws new FullWeaponSpaceException
        }
    }

    public ArrayList<CardWeapon> getWeapons() {
        if (weapons.isEmpty())
            return new ArrayList<>();
        return new ArrayList<>(weapons);
    }

    private CardWeapon removeWeapon(int position){
        if (this.getWeapons().isEmpty())
            return null;
        CardWeapon card = weapons.get(position);
        this.weapons.remove(position);
        return card;
    }

    /**
     * This method Override the method of NormalSquare, it allows a BoardPlayer to grab one of the Item content by the NormalSquare
     * */

    public CardWeapon grabItem(int position)  {
        CardWeapon weapon;
        if (weapons.size() > position){
            weapon = weapons.get(position);
            removeWeapon(position);
        }
        else
            weapon = null;
        return weapon;
    }
}