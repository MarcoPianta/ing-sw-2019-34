package Model;

import org.junit.jupiter.api.Test;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

public class MoveTest {


    @Test
    public void constructorTestWithoutEffect() throws FileNotFoundException {
        GameBoard testGameBoard = new GameBoard("map1");
        Player testMoverPlayer = new Player(2343234, null);
        testMoverPlayer.newPosition(testGameBoard.getRooms().get(1).getNormalSquares().get(0));
        Move action = new Move(testMoverPlayer, testGameBoard.getRooms().get(1).getNormalSquares().get(2), 2);
        assertTrue(action instanceof Move);
    }

    @Test
    public void executeTest() throws FileNotFoundException {
        Colors color1 = Colors.RED, color2 = Colors.BLUE;
        Game game = new Game( 8,"map1");
        Player testShooterPlayer = new Player(34243432, color1);
        Player testTargetPlayer = new Player(767676675, color2);
        game.addPlayer(testShooterPlayer);
        game.addPlayer(testTargetPlayer);
        testShooterPlayer.newPosition(game.getMap().getRooms().get(1).getNormalSquares().get(0));
        testTargetPlayer.newPosition(game.getMap().getRooms().get(1).getNormalSquares().get(0));
        CardWeapon testWeapon = new CardWeapon (WeaponDictionary.CYBERBLADE.getAbbreviation());
        Effect testEffect = testWeapon.getEffects().get(1);
        Move action = new Move(testTargetPlayer, game.getMap().getRooms().get(1).getNormalSquares().get(1), testEffect.getMyMove());
        assertFalse(action.execute());
    }

    @Test
    public void isValidTest() throws FileNotFoundException {
        Colors color1 = Colors.RED, color2 = Colors.BLUE;
        Game game = new Game( 8,"map1");
        Player testShooterPlayer = new Player(677777476, color1);
        Player testTargetPlayer = new Player(6754766, color2);
        game.addPlayer(testShooterPlayer);
        game.addPlayer(testTargetPlayer);
        testShooterPlayer.newPosition(game.getMap().getRooms().get(0).getNormalSquares().get(0));
        testTargetPlayer.newPosition(game.getMap().getRooms().get(0).getNormalSquares().get(0));
        CardWeapon testWeapon = new CardWeapon (WeaponDictionary.CYBERBLADE.getAbbreviation());
        Effect testEffect = testWeapon.getEffects().get(1);
        Move action = new Move(testTargetPlayer, game.getMap().getRooms().get(1).getNormalSquares().get(1), testEffect.getMyMove());
        //TODO fix when GameBoard is fixed
        /*
            System.out.println("--TEST-- " + testGameBoard.getRooms().get(0).getNormalSquares().get(0));
            System.out.println("n " + testGameBoard.getRooms().get(0).getNormalSquares().get(0).getN());
            System.out.println("e " + testGameBoard.getRooms().get(0).getNormalSquares().get(1));
            System.out.println("s " + testGameBoard.getRooms().get(1).getNormalSquares().get(0));
            System.out.println("w " + testGameBoard.getRooms().get(0).getNormalSquares().get(0).getW());
            System.out.println(action.reachableSquare());
        */
        assertTrue(action.isValid());

        action = new Move(testTargetPlayer, game.getMap().getRooms().get(1).getNormalSquares().get(1), testEffect.getMyMove());
        assertFalse(action.isValid());
    }

    @Test
    public  void calculateReachableSquareTest() throws FileNotFoundException {
        Colors color1 = Colors.RED, color2 = Colors.BLUE;
        Game game = new Game( 8,"map1");
        Player testShooterPlayer = new Player(67575, color1);
        Player testTargetPlayer = new Player(67655676, color2);
        game.addPlayer(testShooterPlayer);
        game.addPlayer(testTargetPlayer);
        testShooterPlayer.newPosition(game.getMap().getRooms().get(1).getNormalSquares().get(0));
        testTargetPlayer.newPosition(game.getMap().getRooms().get(1).getNormalSquares().get(0));
        CardWeapon testWeapon = new CardWeapon (WeaponDictionary.CYBERBLADE.getAbbreviation());
        Effect testEffect = testWeapon.getEffects().get(1);
        Move action = new Move(testTargetPlayer, game.getMap().getRooms().get(1).getNormalSquares().get(1), testEffect.getMyMove());
        assertEquals(2, action.reachableSquare().size());
    }
}
