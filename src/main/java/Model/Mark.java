package Model;

import java.util.ArrayList;

public class Mark implements Action {
    private Player markerPlayer;
    private ArrayList<Player> targetPlayers;
    private Effect markerEffect;

    public Mark(Player marker, ArrayList<Player> target, Effect effect){
        markerPlayer = marker; //devo ricevere la copia
        targetPlayers = target; //devo ricevere l'originale
        markerEffect = effect; //devo ricevere la copia
    }

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

    private void markTarget(Player target, int damage, Effect.PostCondition postCondition){
        target.getPlayerBoard().getHealthPlayer().addDamage(markerPlayer, damage);
        if(postCondition.getTargetMove() != 0)
        {
            //TODO throw shooterHasToMoveTargetException
            // --> receives targetNewSquare
            Move action = new Move(markerPlayer, target, markerEffect, null);
        }
    }


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

    private void isAlreadyReachable(ArrayList<NormalSquare> allStepSquare, ArrayList<NormalSquare> thisStepSquare, ArrayList<NormalSquare> reachableSquare, NormalSquare thisSquare, ArrayList<Colors> colors, Effect.PreCondition preCondition, int i){
        if (!allStepSquare.contains(thisSquare)) {
            thisStepSquare.add(thisSquare);
            allStepSquare.add(thisSquare);
            if ((!preCondition.isVision() || colors.contains(thisSquare.getColor())) && i > preCondition.getMinRange() && !reachableSquare.contains(thisSquare))
                reachableSquare.add(reachableSquare.size(), thisSquare);
        }
    }
}
