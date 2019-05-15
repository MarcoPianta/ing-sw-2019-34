package Model;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class MarkTest {
    /**
     * This method test all the three constructor methods
     */

    @Test
    public void constructorTest() throws FileNotFoundException {
        Player testMarkerPlayer = new Player("shooterID", null, "name" );
        Player testTargetPlayer = new Player("targetID", null, "name" );
        ArrayList<Player> testList = new ArrayList<>();
        testList.add(testTargetPlayer);
        CardWeapon testWeapon = new CardWeapon (WeaponDictionary.CYBERBLADE.getAbbreviation());
        Effect testEffect = testWeapon.getEffects().get(1);
        Mark action = new Mark(testMarkerPlayer, testList, testEffect);
        assertTrue(action instanceof Mark);
    }

    @Test
    public void executeTest() throws FileNotFoundException {
        /**
         * This method test execute
         * */
        Game game=new Game(5,"map1");
        Player testMarkerPlayer = new Player("shooterID", null, "name" );
        Player testTargetPlayer = new Player("targetID", null, "name" );
        game.addPlayer(testMarkerPlayer);
        game.addPlayer(testTargetPlayer);
        ArrayList<Player> testList = new ArrayList<>();
        testList.add(testTargetPlayer);
        testMarkerPlayer.newPosition(game.getMap().getRooms().get(1).getNormalSquares().get(0));
        testTargetPlayer.newPosition(game.getMap().getRooms().get(1).getNormalSquares().get(0));
        CardWeapon testWeapon = new CardWeapon (WeaponDictionary.CYBERBLADE.getAbbreviation());
        Effect testEffect = testWeapon.getEffects().get(0);
        Mark action = new Mark(testMarkerPlayer, testList, testEffect);
        assertTrue(action.execute());
    }

    @Test
    public void isValidTest() throws FileNotFoundException {
        /**
         * This method test isValid
         * First case: CyberBlade's precondition respected
         * Second case: CyberBlade's precondition not respected
         * */
        //Test 1
        Game game=new Game(5,"map1");
        Player testMarkerPlayer = new Player("shooterID", null, "name" );
        Player testTargetPlayer = new Player("targetID", null, "name" );
        game.addPlayer(testMarkerPlayer);
        game.addPlayer(testTargetPlayer);
        ArrayList<Player> testList = new ArrayList<>();
        testList.add(testTargetPlayer);
        testMarkerPlayer.newPosition(game.getMap().getRooms().get(1).getNormalSquares().get(0));
        testTargetPlayer.newPosition(game.getMap().getRooms().get(1).getNormalSquares().get(0));
        CardWeapon testWeapon = new CardWeapon (WeaponDictionary.CYBERBLADE.getAbbreviation());
        Effect testEffect = testWeapon.getEffects().get(1);
        Mark action = new Mark(testMarkerPlayer, testList, testEffect);
        assertTrue(action.isValid());
        //Test 2
        testTargetPlayer.newPosition(game.getMap().getRooms().get(1).getNormalSquares().get(2));
        assertFalse(action.isValid());

    }



}
