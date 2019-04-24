package Model;

import org.junit.jupiter.api.Test;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

public class InjureTest {

    @Test
    public void constructorTest() throws FileNotFoundException {
        Colors color1 = Colors.RED, color2 = Colors.BLUE;
        Game game = new Game("gameID", 8);
        Player testShooterPlayer = new Player("shooterID", game, color1, "name" );
        Player testTargetPlayer = new Player("targetID", game, color2, "name" );
        CardWeapon testWeapon = new CardWeapon (WeaponDictionary.CYBERBLADE.getAbbreviation());
        Effect testEffect = testWeapon.getEffects().get(1);
        Injure action = new Injure();
        action.Injure(testShooterPlayer, testTargetPlayer, testEffect);
        assertTrue(action instanceof Injure);
    }

    @Test
    public void executeTest() throws FileNotFoundException {
        /**
         * This method test execute
         * */
        Colors color1 = Colors.RED, color2 = Colors.BLUE;
        Game game = new Game("gameID", 8);
        GameBoard testGameBoard = new GameBoard("map1");
        Player testShooterPlayer = new Player("shooterID", game, color1, "name" );
        Player testTargetPlayer = new Player("targetID", game, color2, "name" );
        testShooterPlayer.newPosition(testGameBoard.getRooms().get(1).getNormalSquares().get(0));
        testTargetPlayer.newPosition(testGameBoard.getRooms().get(1).getNormalSquares().get(0));
        CardWeapon testWeapon = new CardWeapon (WeaponDictionary.CYBERBLADE.getAbbreviation());
        Effect testEffect = testWeapon.getEffects().get(1);
        Injure action = new Injure();
        action.Injure(testShooterPlayer, testTargetPlayer, testEffect);
        assertTrue(action.execute());
    }

    @Test
    public void isValidTest() throws FileNotFoundException {
        /**
         * This method test isValid
         * First case: CyberBlade's precondition respected
         * Second case: CyberBlade's precondition not respected
         * */
        //Test 2
        Colors color1 = Colors.RED, color2 = Colors.BLUE;
        Game game = new Game("gameID", 8);
        GameBoard testGameBoard = new GameBoard("map1");
        Player testShooterPlayer = new Player("shooterID", game, color1, "name" );
        Player testTargetPlayer = new Player("targetID", game, color2, "name" );
        testShooterPlayer.newPosition(testGameBoard.getRooms().get(1).getNormalSquares().get(0));
        testTargetPlayer.newPosition(testGameBoard.getRooms().get(1).getNormalSquares().get(0));
        CardWeapon testWeapon = new CardWeapon (WeaponDictionary.CYBERBLADE.getAbbreviation());
        Effect testEffect = testWeapon.getEffects().get(1);
        Injure action = new Injure();
        action.Injure(testShooterPlayer, testTargetPlayer, testEffect);
        assertTrue(action.isValid());
        //Test 2
        testTargetPlayer.newPosition(testGameBoard.getRooms().get(1).getNormalSquares().get(2));
        assertFalse(action.isValid());

    }

    @Test
    public void reachableSquareTest() throws FileNotFoundException {
        /**
         * This method test the execute test of the Injure class
         * create a game with one of the real map, two player and take one of a weapon's effect that use injure
         * First case: 2 player in the same Square, CyberBlade works
         * Second Case: 2 player in different Square, CyberBlade don't works
         */
        //  Test 1
        Colors color1 = Colors.RED, color2 = Colors.BLUE;
        Game game = new Game("gameID", 8);
        GameBoard testGameBoard = new GameBoard("map1");
        Player testShooterPlayer = new Player("shooterID", game, color1, "name" );
        Player testTargetPlayer = new Player("targetID", game, color2, "name" );
        testShooterPlayer.newPosition(testGameBoard.getRooms().get(1).getNormalSquares().get(0));
        testTargetPlayer.newPosition(testGameBoard.getRooms().get(1).getNormalSquares().get(0));
        CardWeapon testWeapon = new CardWeapon (WeaponDictionary.CYBERBLADE.getAbbreviation());
        Effect testEffect = testWeapon.getEffects().get(1);
        Injure action = new Injure();
        action.Injure(testShooterPlayer, testTargetPlayer, testEffect);
        assertTrue(action.reachableSquare().contains(testTargetPlayer.getPosition()));
        //  Test 2
        testShooterPlayer.newPosition(testGameBoard.getRooms().get(3).getNormalSquares().get(0));
        assertFalse(action.reachableSquare().contains(testTargetPlayer.getPosition()));
    }
}
