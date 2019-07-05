package Controller;

import Model.CardWeapon;
import Model.NormalSquare;
import Model.Player;
import Model.WeaponDictionary;
import network.messages.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class FinalTurnHandlerTest {
    @Test
    public  void isFirstPlayerTest()throws FileNotFoundException {
        ArrayList<Integer> players=new ArrayList<>();
        players.add(32413);
        players.add(4324525);
        GameHandler gameHandler=new GameHandler(5,players,"map1",null);
        gameHandler.getGame().setCurrentPlayer(gameHandler.getGame().getFirstPlayer());
        gameHandler.getGame().incrementCurrentPlayer();
        gameHandler.getFinalTurnHandler().getEndFinalTurnChecks().isFirstPlayer();
        assertFalse(gameHandler.getFinalTurnHandler().isAlreadyFirstPlayer());

        gameHandler.getGame().incrementCurrentPlayer();
        gameHandler.getFinalTurnHandler().getEndFinalTurnChecks().isFirstPlayer();
        assertTrue(gameHandler.getFinalTurnHandler().isAlreadyFirstPlayer());
    }

    @Test
    public void finalTurnIsEndTest() throws FileNotFoundException{
        ArrayList<Integer> players=new ArrayList<>();
        players.add(32413);
        players.add(4324525);
        GameHandler gameHandler=new GameHandler(5,players,"map1",null);
        gameHandler.getGame().setCurrentPlayer(gameHandler.getGame().getFirstPlayer());

        gameHandler.getFinalTurnHandler().setFirstFinalTurnPlayer(gameHandler.getGame().getFirstPlayer());
        assertTrue(gameHandler.getFinalTurnHandler().getEndFinalTurnChecks().finalTurnIsEnd());
        gameHandler.getGame().incrementCurrentPlayer();
        assertFalse(gameHandler.getFinalTurnHandler().getEndFinalTurnChecks().finalTurnIsEnd());
    }
    @Test
    public void actionFinalTurnTest()throws FileNotFoundException{
        ArrayList<Integer> players=new ArrayList<>();
        List<NormalSquare> squares;

        players.add(32413);
        players.add(4324525);
        GameHandler gameHandler=new GameHandler(5,players,"map1",null);
        PossibleMove possibleMove=new PossibleMove(gameHandler.getGame().getPlayers().get(0).getPlayerID(),4);
        gameHandler.getGame().getDeadRoute().setFinalTurn(true);
        gameHandler.getGame().setCurrentPlayer(gameHandler.getGame().getPlayers().get(0));
        gameHandler.getFinalTurnHandler().setAlreadyFirsPlayer(false);
        //move test no firstPlayer
        //squares=gameHandler.receiveSquare(possibleMove);
        //NormalSquare newSquare= squares.get(squares.size()-1);
        //MoveMessage message=new MoveMessage(gameHandler.getGame().getPlayers().get(0).getPlayerID(),newSquare);
        //assertTrue(gameHandler.receiveServerMessage(message));

        String file= WeaponDictionary.ELECTROSCYTE.getAbbreviation();
        CardWeapon cardWeapon=new CardWeapon(file);
        gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().addAmmo(3,3,3);
        gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().addWeapon(cardWeapon);
        //shot test no firstPlayer
        PossibleTargetShot shotMessage= new PossibleTargetShot(gameHandler.getGame().getPlayers().get(0).getPlayerID(),gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(0).getEffects().get(0));


        gameHandler.getGame().getPlayers().get(1).newPosition(gameHandler.getGame().getCurrentPlayer().getPosition());

        ArrayList<Player>  playersTarget;

        playersTarget=gameHandler.receiveTarget(shotMessage);
        gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(0).setCharge(true);
        //errror effetto è dato a pezzi
        //Shot shot=new Shot(playersTarget,gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(0).getEffects().get(0),0);
        //assertTrue(gameHandler.getTurnHandler().actionState(shot));

        //test Grab firstPlayer
        gameHandler.getFinalTurnHandler().setAlreadyFirsPlayer(true);
        gameHandler.getGame().getCurrentPlayer().setState(StateMachineEnumerationTurn.ACTION1);

        GrabNotOnlyAmmo grabNotOnlyAmmo=new GrabNotOnlyAmmo(gameHandler.getGame().getCurrentPlayer().getPlayerID());
        //assertTrue(gameHandler.receiveServerMessage(grabNotOnlyAmmo));
    }
}
