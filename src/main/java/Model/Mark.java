package Model;

import java.util.ArrayList;
/**
 * This class implements Action
 */
public class Mark implements Action {
    private Player markerPlayer;
    private ArrayList<Player> targetPlayers;
    private Effect markerEffect;

    /**
     * @param markerPlayer  The actorPlayer who use the Mark Action
     * @param targets       The list of Player that are targeted by the actorPlayer
     * @param effect        The effect used by the actorPlayer to invoke Injure Action
     */
    public Mark(Player markerPlayer, ArrayList<Player> targets, Effect effect){
        this.markerPlayer = markerPlayer; //devo ricevere la copia
        targetPlayers = targets; //devo ricevere l'originale
        markerEffect = effect; //devo ricevere la copia
    }

    /**
     * Invoke the isValid method that control the Pre-condition of the action
     * This method execute the Mark Action
     *
     * @return true if the action has been executed, false otherwise
     */
    public boolean execute() {
        if (isValid())  {
            for (int playerCounter = 0; playerCounter < targetPlayers.size(); playerCounter++) {
                if (markerEffect.isAllTarget()) {
                    markTarget(targetPlayers.get(playerCounter), markerEffect.getDamage().get(0), markerEffect.getPostCondition());
                } else {
                    markTarget(targetPlayers.get(playerCounter), markerEffect.getDamage().get(playerCounter), markerEffect.getPostCondition());
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Control the Pre-condition of the Mark Action
     * This method invoke reachableSquare method
     *
     * @return true if the action invocation respect the condition, false otherwise
     */
    public boolean isValid(){
        ArrayList<NormalSquare> reachable = reachableSquare();
        int playerCounter = 0;
        while(playerCounter < targetPlayers.size()){
            if(reachable.contains(targetPlayers.get(playerCounter).getPosition())){
                if(markerEffect.getPreCondition().isEnemiesDifferentSquare()) {
                    reachable.remove(targetPlayers.get(playerCounter).getPosition());
                }
                playerCounter++;
            }
            else{
                return false;
            }
        }
        return true;
    }

    /**
     * @param targetPlayer  The Player that is targeted in this step
     * @param damage        The number of mark that must be dealt to the targetPlayer
     * @param postCondition Post-condition that must be respected after the marking
     */
    private void markTarget(Player targetPlayer, int damage, Effect.PostCondition postCondition){
        targetPlayer.getPlayerBoard().getHealthPlayer().addDamage(markerPlayer, damage);
        if(postCondition.getTargetMove() != 0)
        {
            //TODO throw shooterHasToMoveTargetException
            // --> receives targetNewSquare
            Move action = new Move(markerPlayer, targetPlayer, markerEffect, null);
        }
    }

    /**
     * This method is
     * @return the list of Square reachable from the startSquare with at least movePass step
     */
    public ArrayList<NormalSquare> reachableSquare() {
        Effect.PreCondition preCondition = markerEffect.getPreCondition();
        ArrayList<NormalSquare> reachableSquare = new ArrayList<>();
        ArrayList<NormalSquare> thisStepSquare = new ArrayList<>();
        ArrayList<NormalSquare> allStepSquare = new ArrayList<>();
        ArrayList<Colors> colors = new ArrayList<>();
        thisStepSquare.add(markerPlayer.getPosition());
        allStepSquare.add(markerPlayer.getPosition());
        int thisStep;
        if (0 == preCondition.getMinRange())
            reachableSquare.add(markerPlayer.getPosition());
        if (preCondition.isVision()) {
            colors.add(markerPlayer.getPosition().getColor());
            if (markerPlayer.getPosition().getN().getColor() != markerPlayer.getPosition().getColor())
                colors.add(markerPlayer.getPosition().getN().getColor());
            if (markerPlayer.getPosition().getE().getColor() != markerPlayer.getPosition().getColor())
                colors.add(markerPlayer.getPosition().getE().getColor());
            if (markerPlayer.getPosition().getS().getColor() != markerPlayer.getPosition().getColor())
                colors.add(markerPlayer.getPosition().getS().getColor());
            if (markerPlayer.getPosition().getW().getColor() != markerPlayer.getPosition().getColor())
                colors.add(markerPlayer.getPosition().getW().getColor());
        }
        for (int i = 0, j; i < preCondition.getMaxRange(); i++) {
            thisStep = thisStepSquare.size();
            j = 0;
            while (j < thisStep) {
                isAlreadyReachable(allStepSquare, thisStepSquare, reachableSquare, thisStepSquare.get(0).getN(), colors, preCondition, i);
                isAlreadyReachable(allStepSquare, thisStepSquare, reachableSquare, thisStepSquare.get(0).getE(), colors, preCondition, i);
                isAlreadyReachable(allStepSquare, thisStepSquare, reachableSquare, thisStepSquare.get(0).getS(), colors, preCondition, i);
                isAlreadyReachable(allStepSquare, thisStepSquare, reachableSquare, thisStepSquare.get(0).getW(), colors, preCondition, i);
                thisStepSquare.remove(0);
                j++;
            }
        }
        return reachableSquare;
    }

    /**
     * This method is invoked by reachableSquare method
     */
    private void isAlreadyReachable(ArrayList<NormalSquare> allStepSquare, ArrayList<NormalSquare> thisStepSquare, ArrayList<NormalSquare> reachableSquare, NormalSquare thisSquare, ArrayList<Colors> colors, Effect.PreCondition preCondition, int i){
        if (!allStepSquare.contains(thisSquare)) {
            thisStepSquare.add(thisSquare);
            allStepSquare.add(thisSquare);
            if ((!preCondition.isVision() || colors.contains(thisSquare.getColor())) && i > preCondition.getMinRange() && !reachableSquare.contains(thisSquare))
                reachableSquare.add(reachableSquare.size(), thisSquare);
        }
    }
}
