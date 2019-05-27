package Model;

import java.util.ArrayList;
import java.util.List;

public class Shoot {
    private Effect shootEffect;
    private Player shooterPlayer;
    private List<Player> targets;
    private NormalSquare targetSquare;
    private Colors roomColor;

    public Shoot(Effect effect, Player shooter, List<Player> target){
        this.shootEffect = effect;
        this.shooterPlayer = shooter;
        this.targets = target;
    }

    public Shoot(Effect effect, Player shooter, NormalSquare square){
        this.shootEffect = effect;
        this.shooterPlayer = shooter;
        this.targetSquare = square;
    }

    public Shoot(Effect effect, Player shooter, Colors color){
        this.shootEffect = effect;
        this.shooterPlayer = shooter;
        this.roomColor = color;
    }

    public boolean execute(){
        if(isValid()) {

            return true;
        }
        return false;
    }

    public boolean isValid(){
        switch (shootEffect.getTarget()){
            case ('p'):
       //         if (targets.size() <= shootEffect.getTargetNumber()){
                    ArrayList<Player> visibleTarget = targetablePlayer();
                    for (Player target : targets) {
                        if ((shootEffect.getPreCondition().isBlind() && visibleTarget.contains(target)) || (!shootEffect.getPreCondition().isBlind() && !(visibleTarget.contains(target)))) {
                            return false;
                        }
                    }
                    return true;
       //         }
                //TODO there are too much target, is it possible to happen??        if not erase the first if condition
       //         return false;

            case ('s'):
     //           if(shootEffect.getTargetNumber() == 0)  //TODO useless condition??
                    return reachableSquare().contains(targetSquare);
     //           return false;

            case ('r'):
     //           if(shootEffect.getTargetNumber() == 0)  //TODO useless condition??
                    for (NormalSquare normalSquare : reachableSquare()){
                        if(normalSquare.getColor() == roomColor)
                            return true;
                    }
                return false;
        }
        return true;
    }

    /**
     * This method control the isVision PreCondition
     * @return the list of Square reachable from the startSquare with at least movePass step
     */
    public ArrayList<NormalSquare> reachableSquare() {
        Effect.PreCondition preCondition = shootEffect.getPreCondition();
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
}
