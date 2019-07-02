package Model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class extends NormalSquare and describe a SpawnSquare type
 * */
public class SpawnSquare extends NormalSquare{
    private ArrayList<CardWeapon> weapons;

    public SpawnSquare(NormalSquare sideN, NormalSquare sideE, NormalSquare sideS, NormalSquare sideW, Colors color, List<CardWeapon> weapons) {
        super(sideN, sideE, sideS, sideW, color, null);
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

    @Override
    public void setItems(Card card) {
        if (weapons.size() < 3)
            if(card != null)
                for(int i=0;i<3;i++)
                    if(weapons.get(i) == null){
                        weapons.set(i,(CardWeapon)card);
                        return;
                    }

        else {
            //TODO throws new FullWeaponSpaceException
        }
    }

    public List<CardWeapon> getWeapons() {
        if (weapons.isEmpty())
            return new ArrayList<>();
        return new ArrayList<>(weapons);
    }

    private CardWeapon removeWeapon(int position){
        if (this.getWeapons().isEmpty())
            return null;
        CardWeapon card = weapons.get(position);
        this.weapons.set(position, null);
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