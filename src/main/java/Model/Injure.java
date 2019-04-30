package Model;

import java.util.ArrayList;

public class Injure implements Action {
    private Player shooterPlayer;
    private Player targetPlayer;
    private Effect injurerEffect;

    /**
     * @param shooter
     * @param target
     * @param effect
     */

    public void Injure(Player shooter, Player target, Effect effect) {
        shooterPlayer = shooter; //devo ricevere la copia
        targetPlayer = target; //devo ricevere l'originale
        injurerEffect = effect; //devo ricevere la copia
    }

    public boolean execute() {
        if (isValid())  {
            //targetPlayer.getPlayerBoard().getHealthPlayer().addDamage(shooterPlayer, injurerEffect.getDamage());
            return true;
        }
        return false;
    }

    /**
     * isValid is a method that once the preCondition were verified, it tried if
     */

    public boolean isValid(){
        ArrayList<NormalSquare> reachable = reachableSquare();
        if(reachable.contains(targetPlayer.getPosition()))
            return true;
        return false;
    }

    public ArrayList<NormalSquare> reachableSquare() {
        Effect.PreCondition preCondition = injurerEffect.getPreCondition();
        Effect.PostCondition postCondition = injurerEffect.getPostCondition();
        ArrayList<NormalSquare> reachableSquare = new ArrayList<>();  //tutte le celle in cui posso usare l'effetto
        ArrayList<NormalSquare> thisStepSquare = new ArrayList<>();   //le celle che posso raggiungere con gli stessi passi
        ArrayList<NormalSquare> allStepSquare = new ArrayList<>();    //le celle che ho controllato fino ad ora
        ArrayList<Colors> colors = new ArrayList<>();                 //tutti i colori delle Room visibili
        thisStepSquare.add(0, shooterPlayer.getPosition());
        allStepSquare.add(0, shooterPlayer.getPosition());
        int thisStep;
        if (0 == preCondition.getMinRange())
            reachableSquare.add(0, shooterPlayer.getPosition());
        if (preCondition.isVision()) {
            colors.add(shooterPlayer.getPosition().getColor());
            if (shooterPlayer.getPosition().getN().getColor() != null && shooterPlayer.getPosition().getN().getColor() != shooterPlayer.getPosition().getColor())
                colors.add(shooterPlayer.getPosition().getN().getColor());
            if (shooterPlayer.getPosition().getE().getColor() != null && shooterPlayer.getPosition().getE().getColor() != shooterPlayer.getPosition().getColor())
                colors.add(shooterPlayer.getPosition().getE().getColor());
            if (shooterPlayer.getPosition().getS().getColor() != null && shooterPlayer.getPosition().getS().getColor() != shooterPlayer.getPosition().getColor())
                colors.add(shooterPlayer.getPosition().getS().getColor());
            if (shooterPlayer.getPosition().getW().getColor() != null && shooterPlayer.getPosition().getW().getColor() != shooterPlayer.getPosition().getColor())
                colors.add(shooterPlayer.getPosition().getW().getColor());
        }
        for (int i = 0, j; i < preCondition.getMaxRange(); i++) {
            thisStep = thisStepSquare.size();
            j = 0;
            while (j < thisStep) {
                if (!allStepSquare.contains(thisStepSquare.get(0).getN())) {
                    thisStepSquare.add(thisStepSquare.get(0).getN());
                    allStepSquare.add(allStepSquare.get(0).getN());
                    if ((!preCondition.isVision() || colors.contains(thisStepSquare.get(0).getN().getColor())) && i > preCondition.getMinRange() && !reachableSquare.contains(reachableSquare.get(0).getN()))
                        reachableSquare.add(reachableSquare.size(), thisStepSquare.get(0).getN());
                }
                if (!allStepSquare.contains(thisStepSquare.get(0).getE())) {
                    thisStepSquare.add(thisStepSquare.get(0).getE());
                    allStepSquare.add(allStepSquare.get(0).getE());
                    if ((!preCondition.isVision() || colors.contains(thisStepSquare.get(0).getE().getColor())) && i > preCondition.getMinRange() && !reachableSquare.contains(reachableSquare.get(0).getE()))
                        reachableSquare.add(reachableSquare.size(), thisStepSquare.get(0).getE());
                }
                if (!allStepSquare.contains(thisStepSquare.get(0).getS())) {
                    thisStepSquare.add(thisStepSquare.get(0).getS());
                    allStepSquare.add(allStepSquare.get(0).getS());
                    if ((!preCondition.isVision() || colors.contains(thisStepSquare.get(0).getS().getColor())) && i > preCondition.getMinRange() && !reachableSquare.contains(reachableSquare.get(0).getS()))
                        reachableSquare.add(reachableSquare.size(), thisStepSquare.get(0).getS());
                }
                if (!allStepSquare.contains(thisStepSquare.get(0).getW())) {
                    thisStepSquare.add(thisStepSquare.get(0).getW());
                    allStepSquare.add(allStepSquare.get(0).getW());
                    if ((!preCondition.isVision() || colors.contains(thisStepSquare.get(0).getW().getColor())) && i > preCondition.getMinRange() && !reachableSquare.contains(reachableSquare.get(0).getW()))
                        reachableSquare.add(reachableSquare.size(), thisStepSquare.get(0).getW());
                }
                thisStepSquare.remove(0);
                j++;
            }
        }
        return reachableSquare;
    }
}
