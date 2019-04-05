package Model;

import java.util.ArrayList;

public class SpawnSquare extends Square{

    private CardWeapon weapons[];

    public SpawnSquare(Square SideN, Square SideE, Square SideS, Square SideW, CardWeapon Weapons[]) {
        N = SideN;
        E = SideE;
        S = SideS;
        W = SideW;
        weapons = Weapons;
    }

    private void setWeapon(CardWeapon weapon, int position) {
        this.weapons[position] = weapon;
    }

    public void addWeapon(CardWeapon weapon, int position) {
        if (this.weapons == null){
            CardWeapon[] newWeapons;
            newWeapons = new CardWeapon[2];
            newWeapons[0] = weapon;
            this.weapons = newWeapons;
        }
        else
            this.weapons[position] = weapon;
    }

    public CardWeapon[] getWeapons() {
        if (weapons==null)
            return null;
        return weapons;
    }

    private CardWeapon removeWeapon(int position){
        if (this.getWeapons() == null)
            return null;
        CardWeapon remove = this.weapons[position];
        this.setWeapon(null, position);
        return remove;
    }

    @Override
    public CardWeapon grabItem(int position)  {
        if (this.weapons[position] == null){
            return null;
        }
        CardWeapon Weapon;
        Weapon = this.weapons[position];
        removeWeapon(position);
        return Weapon;
    }
}