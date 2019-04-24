package Model;

import java.util.ArrayList;

/**
 * This class implements Action
 */
public class Move implements Action {
    private Effect moveEffect;
    private Player targetPlayer;
    private NormalSquare startSquare;
    private NormalSquare selectedSquare;
    private int movePass;
    /**
     * @param actor     the Player that call the action
     * @param target    the Player that is target by the action
     * @param effect    the Effect that implements the action
     * @param square    the NormalSquare selected by the actor as the final NormalSquare for the target
     */
    public void Move(Player actor, Player target, Effect effect, NormalSquare square){
        targetPlayer = target;
        moveEffect = effect;
        selectedSquare = square;
        if(actor == targetPlayer){
            movePass = moveEffect.getMyMove();
        }
        else{
            movePass = moveEffect.getOtherMove();
            startSquare = targetPlayer.getPosition();
        }
    }

    public ArrayList<NormalSquare> calculateReachableSquare(){
        /**
         * This method return true if the selectedSquare is reachable
         * In reachableSquare I put the Square directly connected with the Square that I take into consideration
         * In reachable I put all the Square reachable from startSquare with a number of step equal or less then "move"
         *  */
        //TODO this method has to explore the tree of Square
        ArrayList<NormalSquare> reachableSquare = new ArrayList<>();
        ArrayList<NormalSquare> reachable = new ArrayList<>();
        reachableSquare.add(0,startSquare);
        for(int i = 0; i < movePass; i++) {
            while(!reachableSquare.isEmpty()) {
                if (reachableSquare.get(0).getN() != null){
                    reachableSquare.add(reachableSquare.get(0).getN());
                    if(!reachable.contains(reachableSquare.get(0).getN()))
                        reachable.add(reachable.size(), reachableSquare.get(0).getN());
                }
                if (reachableSquare.get(0).getE() != null){
                    reachableSquare.add(reachableSquare.get(0).getE());
                    if(!reachable.contains(reachableSquare.get(0).getE()))
                        reachable.add(reachable.size(), reachableSquare.get(0).getE());
                }
                if (reachableSquare.get(0).getS() != null){
                    reachableSquare.add(reachableSquare.get(0).getS());
                    if(!reachable.contains(reachableSquare.get(0).getS()))
                        reachable.add(reachable.size(), reachableSquare.get(0).getS());
                }
                if (reachableSquare.get(0).getW() != null) {
                    reachableSquare.add(reachableSquare.get(0).getW());
                    if(!reachable.contains(reachableSquare.get(0).getW()))
                        reachable.add(reachable.size(), reachableSquare.get(0).getW());
                }
                reachableSquare.remove(0);
            }
        }
        return reachable;
    }

    public boolean execute(){
        if(isValid()) {
            targetPlayer.newPosition(selectedSquare);
            return true;
        }
        return false;
    }

    public boolean isValid(){
        return !calculateReachableSquare().contains(selectedSquare);
    }
}
