package Model;

import java.util.ArrayList;

public class Injure implements Action {
    private Player shooterPlayer;
    private ArrayList<Player> targetPlayers;
    private Effect injurerEffect;

    /**
     * @param shooter
     * @param target
     * @param effect
     */

    public Injure(Player shooter, ArrayList<Player> target, Effect effect) {
        shooterPlayer = shooter; //devo ricevere la copia
        targetPlayers = target; //devo ricevere l'originale
        injurerEffect = effect; //devo ricevere la copia
    }

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

    private void injureTarget(Player target, int damage, Effect.PostCondition postCondition){
        target.getPlayerBoard().getHealthPlayer().addDamage(shooterPlayer, damage);
        if(postCondition.getTargetMove() != 0)
        {
            //TODO throw shooterHasToMoveTargetException
            // --> receives targetNewSquare
            Move action = new Move(shooterPlayer, target, injurerEffect, null);
        }
    }


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

    private void isAlreadyReachable(ArrayList<NormalSquare> allStepSquare, ArrayList<NormalSquare> thisStepSquare, ArrayList<NormalSquare> reachableSquare, NormalSquare thisSquare, ArrayList<Colors> colors, Effect.PreCondition preCondition, int i){
        if (!allStepSquare.contains(thisSquare)) {
            thisStepSquare.add(thisSquare);
            allStepSquare.add(thisSquare);
            if ((!preCondition.isVision() || colors.contains(thisSquare.getColor())) && i >= preCondition.getMinRange() && !reachableSquare.contains(thisSquare))
                reachableSquare.add(reachableSquare.size(), thisSquare);
        }
    }

}
