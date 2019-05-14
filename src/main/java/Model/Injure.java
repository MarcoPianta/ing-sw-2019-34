package Model;

import java.util.ArrayList;
/**
 * This class implements Action
 */
public class Injure implements Action {
    private Player shooterPlayer;
    private ArrayList<Player> targetPlayers;
    private Effect injurerEffect;

    /**
     * @param shooterPlayer     The actorPlayer who use the Injure Action
     * @param target            The list of Player that are targeted by the actorPlayer
     * @param effect            The effect used by the actorPlayer to invoke Injure Action
     */
    public Injure(Player shooterPlayer, ArrayList<Player> target, Effect effect) {
        this.shooterPlayer = shooterPlayer; //devo ricevere la copia
        targetPlayers = target; //devo ricevere l'originale
        injurerEffect = effect; //devo ricevere la copia
    }

    /**
     * Invoke the isValid method that control the Pre-condition of the action
     * This method execute the Injure Action
     *
     * @return true if the action has been executed, false otherwise
     */
    public boolean execute() {
        if (isValid())  {
            for (int playerCounter = 0; playerCounter < targetPlayers.size(); playerCounter++) {
                if (injurerEffect.isAllTarget()) {
                    injureTarget(targetPlayers.get(playerCounter), injurerEffect.getDamage().get(0), injurerEffect.getPostCondition());
                } else {
                    injureTarget(targetPlayers.get(playerCounter), injurerEffect.getDamage().get(playerCounter), injurerEffect.getPostCondition());
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Control the Pre-condition of the Injure Action
     * This method invoke reachableSquare method
     *
     * @return true if the action invocation respect the condition, false otherwise
     */
    public boolean isValid(){
        ArrayList<NormalSquare> reachable = reachableSquare();
        int playerCounter = 0;
        while(playerCounter < targetPlayers.size()){
            if(reachable.contains(targetPlayers.get(playerCounter).getPosition())){
                if(injurerEffect.getPreCondition().isEnemiesDifferentSquare()) {
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
     * @param damage        The number of damage that must be dealt to the targetPlayer
     * @param postCondition Post-condition that must be respected after the injuring
     */
    private void injureTarget(Player targetPlayer, int damage, Effect.PostCondition postCondition){
        targetPlayer.getPlayerBoard().getHealthPlayer().addDamage(shooterPlayer, damage);
        if(postCondition.getTargetMove() != 0)
        {
            //TODO throw shooterHasToMoveTargetException
            // --> receives targetNewSquare
            Move action = new Move(shooterPlayer, targetPlayer, injurerEffect, null);
        }
    }

    /**
     * This method is
     * @return the list of Square reachable from the startSquare with at least movePass step
     */
    public ArrayList<NormalSquare> reachableSquare() {
        Effect.PreCondition preCondition = injurerEffect.getPreCondition();
        ArrayList<NormalSquare> reachableSquare = new ArrayList<>();
        ArrayList<NormalSquare> thisStepSquare = new ArrayList<>();
        ArrayList<NormalSquare> allStepSquare = new ArrayList<>();
        ArrayList<Colors> colors = new ArrayList<>();
        thisStepSquare.add(shooterPlayer.getPosition());
        allStepSquare.add(shooterPlayer.getPosition());
        int thisStep;
        if (0 == preCondition.getMinRange())
            reachableSquare.add(shooterPlayer.getPosition());
        if (preCondition.isVision()) {
            colors.add(shooterPlayer.getPosition().getColor());
            if (shooterPlayer.getPosition().getN().getColor() != shooterPlayer.getPosition().getColor())
                colors.add(shooterPlayer.getPosition().getN().getColor());
            if (shooterPlayer.getPosition().getE().getColor() != shooterPlayer.getPosition().getColor())
                colors.add(shooterPlayer.getPosition().getE().getColor());
            if (shooterPlayer.getPosition().getS().getColor() != shooterPlayer.getPosition().getColor())
                colors.add(shooterPlayer.getPosition().getS().getColor());
            if (shooterPlayer.getPosition().getW().getColor() != shooterPlayer.getPosition().getColor())
                colors.add(shooterPlayer.getPosition().getW().getColor());
        }
        int j;
        for (int i = 1; i <= preCondition.getMaxRange(); i++) {
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
            if ((!preCondition.isVision() || colors.contains(thisSquare.getColor())) && i >= preCondition.getMinRange() && !reachableSquare.contains(thisSquare))
                reachableSquare.add(reachableSquare.size(), thisSquare);
        }
    }

    /**
     * @return the list of Player that can be targeted by the actorPlayer
     */
    public ArrayList<Player> targetablePlayer(){
        ArrayList<Player> reachablePlayer = new ArrayList<>(shooterPlayer.getGameId().getPlayers());
        int i = 0;
        while(i < reachablePlayer.size()){
            if((!reachableSquare().contains(reachablePlayer.get(i).getPosition()) || (reachablePlayer.get(i) == shooterPlayer)))
                reachablePlayer.remove(i);
            else
                i++;
        }
        return reachablePlayer;
    }
    //TODO se la view mostra solo gli Square raggiungibili, allora devo togliere dei passaggi,
    // perchè in teoria ottengo come scelta del player solo Square accettabili
}
