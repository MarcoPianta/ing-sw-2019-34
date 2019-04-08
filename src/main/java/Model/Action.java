package Model;
/**
 * This class is the collection of the method that implements the Action  
 * */
public final class Action{
    /**
     * The Grab class has 2 Overloaded definition
     * the first that represents the grabbing action in a NormalSquare, in witch you has to grab a CardAmmo
     * the second that represents the grabbing action in a SpawnSquare, in witch you has to grab a CardWeapon
     * */
    public boolean Grab(NormalSquare playerSquare, PlayerBoard actor){
        CardAmmo Item = playerSquare.grabItem(0);
        //TODO come differenzio il tipo di CardAmmo ???
/*
        int R;
        int Y;
        int B;
        actor.addAmmo(R,Y,B);
*/
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
    //TODO combine this method with the one who calculate the distance and realize the List of reachable Square
        return true;
    }


}