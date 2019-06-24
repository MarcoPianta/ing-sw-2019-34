package Model;

import java.util.ArrayList;

/**
 * This class implements Action
 */
public class Move implements Action {
    private Player targetPlayer;
    private NormalSquare startSquare;
    private NormalSquare selectedSquare;
    private int movePass;

    /**
     * @param target    the Player that is target by the action
     * @param square    the NormalSquare selected by the Player as the final NormalSquare for the target
     * @param move      the max number of pass that the Player can do
     */
    public Move(Player target, NormalSquare square, int move){
        targetPlayer = target;
        selectedSquare = square;
        startSquare = targetPlayer.getPosition();
        movePass = move;
    }

    /**
     * This method execute the Move Action
     *
     * @return true if the action has been executed, false otherwise
     */
    public boolean execute(){
            targetPlayer.newPosition(selectedSquare);
            return true;
    }

    /**
     * Control the Pre-condition of the Move Action
     * This method invoke reachableSquare method
     *
     * @return true if the action invocation respect the condition, false otherwise
     */
    public boolean isValid(){
        return reachableSquare().contains(selectedSquare);
    }

    /**
     * This method return all the reachable square from startSquare
     * In reachableSquare it will be putted the Square directly connected with the Square that I take into consideration in the current step
     * In reachable it will be putted all the Square reachable from startSquare with a number of step equal or less then "move"
     *
     * This method return the reachable square from the player
     * This method is invoked to show to the Player where he can go with the view
     *  */
    public ArrayList<NormalSquare> reachableSquare(){
        ArrayList<NormalSquare> reachableSquare = new ArrayList<>();
        ArrayList<NormalSquare> reachable = new ArrayList<>();
        reachableSquare.add(startSquare);
        int thisStep;
        for(int i = 0, j; i < movePass; i++) {
            thisStep = reachableSquare.size();
            j = 0;
                while(j < thisStep ){
                    isAlreadyReachable(reachableSquare.get(0).getN(), reachableSquare, reachable);
                    isAlreadyReachable(reachableSquare.get(0).getE(), reachableSquare, reachable);
                    isAlreadyReachable(reachableSquare.get(0).getS(), reachableSquare, reachable);
                    isAlreadyReachable(reachableSquare.get(0).getW(), reachableSquare, reachable);
                    reachableSquare.remove(0);
                    j++;
                }
        }
        return reachable;
    }

    /**
     * This method is invoked by reachableSquare method
     */
    private void isAlreadyReachable(NormalSquare square, ArrayList<NormalSquare> reachableSquare, ArrayList<NormalSquare> reachable){
        if (square != reachableSquare.get(0)) {
            reachableSquare.add(square);
            if(!reachable.contains(square))
                reachable.add(square);
        }
    }
}
