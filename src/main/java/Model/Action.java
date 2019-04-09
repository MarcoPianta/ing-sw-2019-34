package Model;
/**
 * This class is the collection of the method that implements the Action  
 * */
public final class Action{
    /**
     * The Grab class has 2 Overloaded definition
     * the first that represents the grabbing action in a NormalSquare, in which you has to grab a CardAmmo
     * the second that represents the grabbing action in a SpawnSquare, in which you has to grab a CardWeapon
     * */
    public boolean Grab(NormalSquare playerSquare, PlayerBoard actor){
        //TODO we have to generalize the 2 case in which we grab a CardNotOnlyAmmo or a CardOnlyAmmo,
        // because we can't access to the method of the extended classes!!!

        return true;
    }

    public boolean Grab(SpawnSquare playerSquare, PlayerBoard actor, int position){
        CardWeapon Item = playerSquare.grabItem(position);
        if(actor.getPlayerWeapons().size() < 3)
        {
            actor.addWeapon(Item);
            return true;
        }
        return false;
    }

    public boolean Injure(PlayerBoard target, PlayerBoard actor, Effect injurerEffect){
        int damage = injurerEffect.getDamage();
        target.addDamage(actor.getColor(), damage);
        return true;
    }

    public boolean Mark(PlayerBoard target, PlayerBoard actor, Effect markerEffect){
        int mark = markerEffect.getMark();
        target.addMark(actor.getColor(), mark);
        return true;
    }

    public boolean Move(PlayerBoard target, int moveNumber){
    //TODO combine this method with the one who calculate the distance and realize the ArrayList of reachable Square
        return true;
    }


}