package Model;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class SpawnSquare extends Square{

    private ArrayList<CardWeapon> weapons;

    public SpawnSquare(Square SideN, Square SideE, Square SideS, Square SideW) {
        N = SideN;
        E = SideE;
        S = SideS;
        W = SideW;
    }

    private void addWeapon(CardWeapon weapon, int position) {
        this.weapons.set(position, weapon);
    }

    public ArrayList<CardWeapon> getWeapons() {
        ArrayList<CardWeapon> Weapons;
        Weapons = this.weapons;
        return Weapons;
    }

    @Override
    public CardWeapon grabItem(int position)  {
        CardWeapon Weapon;
        Weapon = this.weapons.get(position);
        return Weapon;
    }
}