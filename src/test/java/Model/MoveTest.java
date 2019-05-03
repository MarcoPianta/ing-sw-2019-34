package Model;

import org.junit.jupiter.api.Test;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

public class MoveTest {

    @Test
    public void constructorTestWithEffect() throws FileNotFoundException {
        GameBoard testGameBoard = new GameBoard("map1");
        Player testShooterPlayer = new Player("shooterID", null, null, "name" );
        Player testTargetPlayer = new Player("targetID", null, null, "name" );
        testShooterPlayer.newPosition(testGameBoard.getRooms().get(1).getNormalSquares().get(0));
        testTargetPlayer.newPosition(testGameBoard.getRooms().get(1).getNormalSquares().get(0));
        CardWeapon testWeapon = new CardWeapon (WeaponDictionary.CYBERBLADE.getAbbreviation());
        Effect testEffect = testWeapon.getEffects().get(1);
        Move action = new Move(testShooterPlayer, testTargetPlayer, testEffect, null);
        assertTrue(action instanceof Move);
    }

    @Test
    public void constructorTestWithoutEffect() throws FileNotFoundException {
        GameBoard testGameBoard = new GameBoard("map1");
        Player testMoverPlayer = new Player("targetID", null, null, "name" );
        testMoverPlayer.newPosition(testGameBoard.getRooms().get(1).getNormalSquares().get(0));
        Move action = new Move(testMoverPlayer, testGameBoard.getRooms().get(1).getNormalSquares().get(2), 2);
        assertTrue(action instanceof Move);
    }

    @Test
    public void executeTest() throws FileNotFoundException {
        Colors color1 = Colors.RED, color2 = Colors.BLUE;
        Game game = new Game( 8);
        GameBoard testGameBoard = new GameBoard("map1");
        Player testShooterPlayer = new Player("shooterID", game, color1, "name" );
        Player testTargetPlayer = new Player("targetID", game, color2, "name" );
        testShooterPlayer.newPosition(testGameBoard.getRooms().get(1).getNormalSquares().get(0));
        testTargetPlayer.newPosition(testGameBoard.getRooms().get(1).getNormalSquares().get(0));
        CardWeapon testWeapon = new CardWeapon (WeaponDictionary.CYBERBLADE.getAbbreviation());
        Effect testEffect = testWeapon.getEffects().get(1);
        Move action = new Move(testShooterPlayer, testTargetPlayer, testEffect, testGameBoard.getRooms().get(1).getNormalSquares().get(1));
        assertFalse(action.execute());
    }

    @Test
    public void isValidTest() throws FileNotFoundException {
        Colors color1 = Colors.RED, color2 = Colors.BLUE;
        Game game = new Game( 8);
        GameBoard testGameBoard = new GameBoard("map1");
        Player testShooterPlayer = new Player("shooterID", game, color1, "name" );
        Player testTargetPlayer = new Player("targetID", game, color2, "name" );
        testShooterPlayer.newPosition(testGameBoard.getRooms().get(0).getNormalSquares().get(0));
        testTargetPlayer.newPosition(testGameBoard.getRooms().get(0).getNormalSquares().get(0));
        CardWeapon testWeapon = new CardWeapon (WeaponDictionary.CYBERBLADE.getAbbreviation());
        Effect testEffect = testWeapon.getEffects().get(1);
        Move action = new Move(testShooterPlayer, testShooterPlayer, testEffect, testGameBoard.getRooms().get(0).getNormalSquares().get(1));
        //TODO fix when GameBoard is fixed
        /*
            System.out.println("--TEST-- " + testGameBoard.getRooms().get(0).getNormalSquares().get(0));
            System.out.println("n " + testGameBoard.getRooms().get(0).getNormalSquares().get(0).getN());
            System.out.println("e " + testGameBoard.getRooms().get(0).getNormalSquares().get(1));
            System.out.println("s " + testGameBoard.getRooms().get(1).getNormalSquares().get(0));
            System.out.println("w " + testGameBoard.getRooms().get(0).getNormalSquares().get(0).getW());
            System.out.println(action.calculateReachableSquare());
        */
        assertTrue(action.isValid());

        action = new Move(testShooterPlayer, testTargetPlayer, testEffect, testGameBoard.getRooms().get(0).getNormalSquares().get(0));
        assertFalse(action.isValid());
    }

    @Test
    public  void calculateReachableSquareTest() throws FileNotFoundException {
        Colors color1 = Colors.RED, color2 = Colors.BLUE;
        Game game = new Game( 8);
        GameBoard testGameBoard = new GameBoard("map1");
        Player testShooterPlayer = new Player("shooterID", game, color1, "name" );
        Player testTargetPlayer = new Player("targetID", game, color2, "name" );
        testShooterPlayer.newPosition(testGameBoard.getRooms().get(1).getNormalSquares().get(0));
        testTargetPlayer.newPosition(testGameBoard.getRooms().get(1).getNormalSquares().get(0));
        CardWeapon testWeapon = new CardWeapon (WeaponDictionary.CYBERBLADE.getAbbreviation());
        Effect testEffect = testWeapon.getEffects().get(1);
        Move action = new Move(testShooterPlayer, testShooterPlayer, testEffect, testGameBoard.getRooms().get(1).getNormalSquares().get(1));
        assertEquals(2, action.calculateReachableSquare().size());
    }
}
