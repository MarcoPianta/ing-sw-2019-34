package Controller;

import Model.*;
import network.messages.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class TurnHandlerTest {
    @Test
    public void actionStateTest() throws FileNotFoundException {
        ArrayList<Player> players=new ArrayList<>();
        List<NormalSquare> squares;
        PossibleMove possibleMove=new PossibleMove(3);
        Player player1=new Player("playertest2830", Colors.GREEN, "playerTest1");
        Player player2=new Player("playertest2831", Colors.RED, "playerTest2");
        Player player3=new Player("playertest2832", Colors.BLUE, "playerTest3");
        players.add(player1);
        players.add(player2);
        players.add(player3);
        GameHandler gameHandler=new GameHandler(5,players,"map1");
        //move test+ adrenaline 0
        squares=gameHandler.receiveSquare(possibleMove);
        NormalSquare newSquare= squares.get(0);
        MoveMessage message=new MoveMessage(newSquare);
        assertTrue(gameHandler.getTurnHandler().actionState(message));
        assertEquals(StateMachineEnumerationTurn.ACTION1,gameHandler.getGame().getCurrentPlayer().getState());
        assertEquals(StateMachineEnumerationTurn.ACTION2,gameHandler.getTurnHandler().getNextState());
        String file= WeaponDictionary.ELECTROSCYTE.getAbbreviation();
        CardWeapon cardWeapon=new CardWeapon(file);
        gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().addAmmo(3,3,3);
        gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().addWeapon(cardWeapon);
        //shot test+ adrenaline 2
        PossibleTargetShot shotMessage= new PossibleTargetShot(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(0).getEffects().get(0));
        possibleMove=new PossibleMove(1);
        //test gameHandler receive square
        squares=gameHandler.receiveSquare(possibleMove);
        if(player1==gameHandler.getGame().getCurrentPlayer())
            player2.newPosition(squares.get(0));
        else
            player1.newPosition(squares.get(0));
        Shot shot;

        gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHealthPlayer().addDamage(player1,6);//adrenalineAction
        //test gameHandler receiveTarget
        players=gameHandler.receiveTarget(shotMessage);
        if(player1==gameHandler.getGame().getCurrentPlayer())
            shot=new Shot(players,gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(0).getEffects().get(0),1,player2.getPosition(),null,false);
        else
            shot=new Shot(players,gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(0).getEffects().get(0),1,player1.getPosition(),null,false);
        assertTrue(gameHandler.getTurnHandler().actionState(shot));

        assertEquals(StateMachineEnumerationTurn.ACTION2,gameHandler.getGame().getCurrentPlayer().getState());
        assertEquals(StateMachineEnumerationTurn.RELOAD,gameHandler.getTurnHandler().getNextState());
        assertFalse(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(0).isCharge());
    }
    /*@Test
    public void actionReloadMessage() throws  FileNotFoundException{
        ArrayList<Player> players=new ArrayList<>();
        Player player1=new Player("playertest2830", Colors.GREEN, "playerTest1");
        players.add(player1);
        GameHandler gameHandler=new GameHandler(5,players,"map1");
        String file= WeaponDictionary.ELECTROSCYTE.getAbbreviation();
        CardWeapon cardWeapon=new CardWeapon(file);
        gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().addAmmo(3,3,3);
        gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().addWeapon(cardWeapon);
        gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(0).setCharge(false);

        ReloadMessage reloadMessage=new ReloadMessage(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(0),null);
        assertTrue(gameHandler.receiveServerMessage(reloadMessage));
    }*/
}
