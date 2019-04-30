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
        Player testMarkerPlayer = new Player("shooterID", null, null, "name" );
        Player testTargetPlayer = new Player("targetID", null, null, "name" );
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
        GameBoard testGameBoard = new GameBoard("map1");
        Player testMarkerPlayer = new Player("shooterID", null, null, "name" );
        Player testTargetPlayer = new Player("targetID", null, null, "name" );
        ArrayList<Player> testList = new ArrayList<>();
        testList.add(testTargetPlayer);
        testMarkerPlayer.newPosition(testGameBoard.getRooms().get(1).getNormalSquares().get(0));
        testTargetPlayer.newPosition(testGameBoard.getRooms().get(1).getNormalSquares().get(0));
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
        GameBoard testGameBoard = new GameBoard("map1");
        Player testMarkerPlayer = new Player("shooterID", null, null, "name" );
        Player testTargetPlayer = new Player("targetID", null, null, "name" );
        ArrayList<Player> testList = new ArrayList<>();
        testList.add(testTargetPlayer);
        testMarkerPlayer.newPosition(testGameBoard.getRooms().get(1).getNormalSquares().get(0));
        testTargetPlayer.newPosition(testGameBoard.getRooms().get(1).getNormalSquares().get(0));
        CardWeapon testWeapon = new CardWeapon (WeaponDictionary.CYBERBLADE.getAbbreviation());
        Effect testEffect = testWeapon.getEffects().get(1);
        Mark action = new Mark(testMarkerPlayer, testList, testEffect);
        assertTrue(action.isValid());
        //Test 2
        testTargetPlayer.newPosition(testGameBoard.getRooms().get(1).getNormalSquares().get(2));
        assertFalse(action.isValid());

    }



}
