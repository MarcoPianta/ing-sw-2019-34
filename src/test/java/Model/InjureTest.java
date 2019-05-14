package Model;

import org.junit.jupiter.api.Test;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class InjureTest {

    @Test
    public void constructorTest() throws FileNotFoundException {
        Player testShooterPlayer = new Player("shooterID", null, "name" );
        Player testTargetPlayer = new Player("targetID", null, "name" );
        ArrayList<Player> testList = new ArrayList<>();
        testList.add(testTargetPlayer);
        CardWeapon testWeapon = new CardWeapon (WeaponDictionary.CYBERBLADE.getAbbreviation());
        Effect testEffect = testWeapon.getEffects().get(1);
        Injure action = new Injure(testShooterPlayer, testList, testEffect);
        assertTrue(action instanceof Injure);
    }

    @Test
    public void executeTest() throws FileNotFoundException {
        /**
         * This method test execute
         * */
        Colors color1 = Colors.RED, color2 = Colors.BLUE;
        GameBoard testGameBoard = new GameBoard("map1");
        Player testShooterPlayer = new Player("shooterID", color1, "name" );
        Player testTargetPlayer = new Player("targetID", color2, "name" );
        ArrayList<Player> testList = new ArrayList<>();
        testList.add(testTargetPlayer);
        testShooterPlayer.newPosition(testGameBoard.getRooms().get(1).getNormalSquares().get(0));
        testTargetPlayer.newPosition(testGameBoard.getRooms().get(1).getNormalSquares().get(0));
        CardWeapon testWeapon = new CardWeapon (WeaponDictionary.CYBERBLADE.getAbbreviation());
        Effect testEffect = testWeapon.getEffects().get(1);
        Injure action = new Injure(testShooterPlayer, testList, testEffect);
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
        Colors color1 = Colors.RED, color2 = Colors.BLUE;
        GameBoard testGameBoard = new GameBoard("map1");
        Player testShooterPlayer = new Player("shooterID", color1, "name" );
        Player testTargetPlayer = new Player("targetID", color2, "name" );
        ArrayList<Player> testList = new ArrayList<>();
        testList.add(testTargetPlayer);
        testShooterPlayer.newPosition(testGameBoard.getRooms().get(1).getNormalSquares().get(0));
        testTargetPlayer.newPosition(testGameBoard.getRooms().get(1).getNormalSquares().get(0));
        CardWeapon testWeapon = new CardWeapon (WeaponDictionary.CYBERBLADE.getAbbreviation());
        Effect testEffect = testWeapon.getEffects().get(1);
        Injure action = new Injure(testShooterPlayer, testList, testEffect);
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
         * First case: 2 players in the same Square, CyberBlade works
         * Second case: 2 players in different Square, CyberBlade don't works
         * Third case: 2 players selected, FlameThrower works
         * Forth case: 2 target in different square, FlameThrower works
         */
        //  Test 1
        Colors color1 = Colors.RED, color2 = Colors.BLUE;
        GameBoard testGameBoard = new GameBoard("map1");
        Player testShooterPlayer = new Player("shooterID", color1, "name" );
        Player testTargetPlayer = new Player("targetID", color2, "name" );
        ArrayList<Player> testList = new ArrayList<>();
        testList.add(testTargetPlayer);
        testShooterPlayer.newPosition(testGameBoard.getRooms().get(1).getNormalSquares().get(0));
        testTargetPlayer.newPosition(testGameBoard.getRooms().get(1).getNormalSquares().get(0));
        CardWeapon testWeapon = new CardWeapon (WeaponDictionary.CYBERBLADE.getAbbreviation());
        Effect testEffect = testWeapon.getEffects().get(1);
        Injure action = new Injure(testShooterPlayer, testList, testEffect);
        assertTrue(action.reachableSquare().contains(testTargetPlayer.getPosition()));
        //  Test 2
        testShooterPlayer.newPosition(testGameBoard.getRooms().get(1).getNormalSquares().get(1));
        assertFalse(action.reachableSquare().contains(testTargetPlayer.getPosition()));
        //Test 3
        CardWeapon testOtherWeapon= new CardWeapon (WeaponDictionary.FLAMETHROWER.getAbbreviation());
        Effect testOtherEffect = testOtherWeapon.getEffects().get(0);
        action = new Injure(testShooterPlayer, testList, testOtherEffect);
        assertTrue(action.reachableSquare().contains(testTargetPlayer.getPosition()));
        //Test 4
        Player testTargetPlayer2 = new Player("targetID", color2, "name" );
        testList.add(testTargetPlayer2);
        testTargetPlayer2.newPosition(testGameBoard.getRooms().get(1).getNormalSquares().get(2));
        action = new Injure(testShooterPlayer, testList, testOtherEffect);
        assertTrue(action.reachableSquare().contains(testTargetPlayer2.getPosition()));
    }

    @Test
    public void targetablePlayerTest() throws FileNotFoundException {
        //initialization of the test game
        Game game = new Game( 8, "map1");
        GameBoard testGameBoard = game.getMap();
        Colors color1 = Colors.YELLOW, color2 = Colors.BLUE, color3 = Colors.VIOLET, color4 = Colors.GREEN, color5 = Colors.WHITE;
        Player testShooterPlayer = new Player("shooterID",color1, "shooter" );
        Player testTargetPlayer1 = new Player("targetID1",color2, "target1" );
        Player testTargetPlayer2= new Player("targetID2",color3, "target2" );
        Player testTargetPlayer3 = new Player("targetID3",color4, "target3" );
        Player testTargetPlayer4 = new Player("targetID4",color5, "target4" );
        testShooterPlayer.newPosition(testGameBoard.getRooms().get(0).getNormalSquares().get(0));
        testTargetPlayer1.newPosition(testGameBoard.getRooms().get(0).getNormalSquares().get(0));
        testTargetPlayer2.newPosition(testGameBoard.getRooms().get(1).getNormalSquares().get(0));
        testTargetPlayer3.newPosition(testGameBoard.getRooms().get(1).getNormalSquares().get(1));
        testTargetPlayer4.newPosition(testGameBoard.getRooms().get(3).getNormalSquares().get(0));
        game.addPlayer(testShooterPlayer);
        game.addPlayer(testTargetPlayer1);
        game.addPlayer(testTargetPlayer2);
        game.addPlayer(testTargetPlayer3);
        game.addPlayer(testTargetPlayer4);
        //initialization of the action
        ArrayList<Player> testList = new ArrayList<Player>(game.getPlayers());
        CardWeapon testWeapon = new CardWeapon (WeaponDictionary.CYBERBLADE.getAbbreviation());
        Effect testEffect = testWeapon.getEffects().get(1);
        Injure action = new Injure(testShooterPlayer, testList, testEffect);
        ArrayList<Player> correctTargetList = new ArrayList<Player>();
        correctTargetList.add(testTargetPlayer1);
        assertEquals(correctTargetList, action.targetablePlayer());
    }
}
