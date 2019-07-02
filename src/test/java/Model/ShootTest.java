package Model;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ShootTest {

    @Test
    public void constructorTest() throws FileNotFoundException {
        Game game = new Game(5,"map1");
        Player testShooterPlayer = new Player(6546474, null);
        Player testTargetPlayer = new Player(6565754, null);
        game.addPlayer(testShooterPlayer);
        game.addPlayer(testTargetPlayer);
        ArrayList<Player> testTargetList = new ArrayList<>();
        testTargetList.add(testTargetPlayer);
        CardWeapon testWeapon = new CardWeapon (WeaponDictionary.CYBERBLADE.getAbbreviation());
        Effect testEffect = testWeapon.getEffects().get(1);
        Shoot action = new Shoot(testEffect, testShooterPlayer, testTargetList, null, false);
        assertTrue(action instanceof Shoot);

        ArrayList<NormalSquare> testSquareList = new ArrayList<>();
        testSquareList.add(game.getMap().getRooms().get(1).getNormalSquares().get(0));
        action = new Shoot(testEffect, testShooterPlayer, null, testSquareList, true);
        assertTrue(action instanceof Shoot);

        action = new Shoot(testEffect, testShooterPlayer, testSquareList.get(0).getColor());
        assertTrue(action instanceof Shoot);

    }


    @Test
    public void executeTest() throws FileNotFoundException {
        Colors color1 = Colors.RED, color2 = Colors.BLUE, color3 = Colors.GREEN, color4 = Colors.YELLOW, color5 = Colors.WHITE;
        Game game=new Game(5,"map1");
        Player testShooterPlayer = new Player(76765437, color1);
        Player testTargetPlayer1 = new Player(85673612, color2);
        Player testTargetPlayer2 = new Player(75743326, color3);
        Player testTargetPlayer3 = new Player(85743331, color4);
        Player testTargetPlayer4 = new Player(85744122, color5);
        game.addPlayer(testShooterPlayer);
        game.addPlayer(testTargetPlayer1);
        game.addPlayer(testTargetPlayer2);
        game.addPlayer(testTargetPlayer3);
        game.addPlayer(testTargetPlayer4);
        testShooterPlayer.newPosition(game.getMap().getRooms().get(1).getNormalSquares().get(0));
        testTargetPlayer1.newPosition(game.getMap().getRooms().get(1).getNormalSquares().get(0));
        testTargetPlayer2.newPosition(game.getMap().getRooms().get(1).getNormalSquares().get(0));
        testTargetPlayer3.newPosition(game.getMap().getRooms().get(1).getNormalSquares().get(0));
        testTargetPlayer4.newPosition(game.getMap().getRooms().get(1).getNormalSquares().get(0));
        ArrayList<Player> testTargetList = new ArrayList<>();
        testTargetList.add(testTargetPlayer1);
        CardWeapon testWeapon = new CardWeapon (WeaponDictionary.CYBERBLADE.getAbbreviation());
        Effect testEffect = testWeapon.getEffects().get(0);
        Shoot action = new Shoot(testEffect, testShooterPlayer, testTargetList, null, false);
        assertTrue(action.execute());
        //Test2
        testWeapon = new CardWeapon (WeaponDictionary.FLAMETHROWER.getAbbreviation());
        testEffect = testWeapon.getEffects().get(1);
        ArrayList<NormalSquare> testTargetSquareList = new ArrayList<>();
        testTargetSquareList.add(game.getMap().getRooms().get(1).getNormalSquares().get(1));
        testTargetSquareList.add(game.getMap().getRooms().get(1).getNormalSquares().get(2));
        action = new Shoot(testEffect, testShooterPlayer, null, testTargetSquareList, true);
        assertTrue(action.execute());
        //Test3
        testWeapon = new CardWeapon (WeaponDictionary.FURNACE.getAbbreviation());
        testEffect = testWeapon.getEffects().get(0);
        action = new Shoot(testEffect, testShooterPlayer, game.getMap().getRooms().get(1).getNormalSquares().get(0).getColor());
        assertTrue(action.execute());
    }
    @Test
    public void isValidTest() throws FileNotFoundException {
        Colors color1 = Colors.RED, color2 = Colors.BLUE, color3 = Colors.GREEN, color4 = Colors.YELLOW, color5 = Colors.WHITE;
        Game game = new Game(5,"map1");
        Player testShooterPlayer = new Player(76765437, color1);
        Player testTargetPlayer1 = new Player(85673612, color2);
        Player testTargetPlayer2 = new Player(75743326, color3);
        Player testTargetPlayer3 = new Player(85743331, color4);
        Player testTargetPlayer4 = new Player(85744122, color5);
        game.addPlayer(testShooterPlayer);
        game.addPlayer(testTargetPlayer1);
        game.addPlayer(testTargetPlayer2);
        game.addPlayer(testTargetPlayer3);
        game.addPlayer(testTargetPlayer4);
        testShooterPlayer.newPosition(game.getMap().getRooms().get(0).getNormalSquares().get(0));
        testTargetPlayer1.newPosition(game.getMap().getRooms().get(0).getNormalSquares().get(1));
        testTargetPlayer2.newPosition(game.getMap().getRooms().get(0).getNormalSquares().get(2));
        testTargetPlayer3.newPosition(game.getMap().getRooms().get(1).getNormalSquares().get(1));
        testTargetPlayer4.newPosition(game.getMap().getRooms().get(1).getNormalSquares().get(2));
        ArrayList<Player> testTargetList = new ArrayList<>();
        testTargetList.add(testTargetPlayer2);
        CardWeapon testWeapon = new CardWeapon (WeaponDictionary.SHOTGUN.getAbbreviation());
        Effect testEffect = testWeapon.getEffects().get(1);
        Shoot action = new Shoot(testEffect, testShooterPlayer, testTargetList, null, false);
        assertTrue(action.isValid());
        testTargetPlayer1.newPosition(game.getMap().getRooms().get(0).getNormalSquares().get(1));
        assertTrue(action.isValid());
        //Test2
        testWeapon = new CardWeapon (WeaponDictionary.FLAMETHROWER.getAbbreviation());
        testEffect = testWeapon.getEffects().get(1);
        ArrayList<NormalSquare> testTargetSquareList = new ArrayList<>();
        testTargetSquareList.add(game.getMap().getRooms().get(0).getNormalSquares().get(1));
        testTargetSquareList.add(game.getMap().getRooms().get(0).getNormalSquares().get(2));
        action = new Shoot(testEffect, testShooterPlayer, null, testTargetSquareList, true);
        assertTrue(action.isValid());
        //Test3
        testWeapon = new CardWeapon (WeaponDictionary.FURNACE.getAbbreviation());
        testEffect = testWeapon.getEffects().get(0);
        action = new Shoot(testEffect, testShooterPlayer, game.getMap().getRooms().get(1).getNormalSquares().get(0).getColor());
        assertTrue(action.isValid());
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
        Colors color1 = Colors.RED, color2 = Colors.BLUE, color3 = Colors.GREEN, color4 = Colors.YELLOW, color5 = Colors.WHITE;
        Game game=new Game(5,"map1");
        Player testShooterPlayer = new Player(76765437, color1);
        Player testTargetPlayer1 = new Player(85673612, color2);
        Player testTargetPlayer2 = new Player(75743326, color3);
        Player testTargetPlayer3 = new Player(85743331, color4);
        Player testTargetPlayer4 = new Player(85744122, color5);
        game.addPlayer(testShooterPlayer);
        game.addPlayer(testTargetPlayer1);
        game.addPlayer(testTargetPlayer2);
        game.addPlayer(testTargetPlayer3);
        game.addPlayer(testTargetPlayer4);
        testShooterPlayer.newPosition(game.getMap().getRooms().get(1).getNormalSquares().get(0));
        testTargetPlayer1.newPosition(game.getMap().getRooms().get(1).getNormalSquares().get(0));
        testTargetPlayer2.newPosition(game.getMap().getRooms().get(1).getNormalSquares().get(0));
        testTargetPlayer3.newPosition(game.getMap().getRooms().get(1).getNormalSquares().get(0));
        testTargetPlayer4.newPosition(game.getMap().getRooms().get(1).getNormalSquares().get(0));
        ArrayList<Player> testTargetList = new ArrayList<>();
        testTargetList.add(testTargetPlayer1);
        testTargetList.add(testTargetPlayer2);
        testTargetList.add(testTargetPlayer3);
        testTargetList.add(testTargetPlayer4);
        CardWeapon testWeapon = new CardWeapon (WeaponDictionary.CYBERBLADE.getAbbreviation());
        Effect testEffect = testWeapon.getEffects().get(1);
        Shoot action = new Shoot(testEffect, testShooterPlayer, testTargetList, null, false);
        assertTrue(action.reachableSquare().contains(testTargetPlayer1.getPosition()));
        //  Test 2
        testShooterPlayer.newPosition(game.getMap().getRooms().get(1).getNormalSquares().get(1));
        assertFalse(action.reachableSquare().contains(testTargetPlayer3.getPosition()));
        //Test 3
        CardWeapon testOtherWeapon= new CardWeapon (WeaponDictionary.FLAMETHROWER.getAbbreviation());
        Effect testOtherEffect = testOtherWeapon.getEffects().get(0);
        action = new Shoot(testOtherEffect, testShooterPlayer, testTargetList, null, false);
        assertTrue(action.reachableSquare().contains(testTargetPlayer3.getPosition()));
    }


    @Test
    public void targetablePlayerTest() throws FileNotFoundException {
        //initialization of the test game
        Game game = new Game( 8, "map1");
        Colors color1 = Colors.YELLOW, color2 = Colors.BLUE, color3 = Colors.VIOLET, color4 = Colors.GREEN, color5 = Colors.WHITE;
        Player testShooterPlayer = new Player(36563744,color1);
        Player testTargetPlayer1 = new Player(65364327,color2);
        Player testTargetPlayer2 = new Player(65474375,color3);
        Player testTargetPlayer3 = new Player(65543674,color4);
        testShooterPlayer.newPosition(game.getMap().getRooms().get(0).getNormalSquares().get(0));
        testTargetPlayer1.newPosition(game.getMap().getRooms().get(0).getNormalSquares().get(0));
        testTargetPlayer2.newPosition(game.getMap().getRooms().get(0).getNormalSquares().get(2));
        testTargetPlayer3.newPosition(game.getMap().getRooms().get(1).getNormalSquares().get(2));
        game.addPlayer(testShooterPlayer);
        game.addPlayer(testTargetPlayer1);
        game.addPlayer(testTargetPlayer2);
        game.addPlayer(testTargetPlayer3);
        //initialization of the action
        CardWeapon testWeapon = new CardWeapon (WeaponDictionary.MACHINEGUN.getAbbreviation());
        Effect testEffect = testWeapon.getEffects().get(testWeapon.getActionSequences().indexOf(1));
        Shoot action = new Shoot(testEffect, testShooterPlayer, null, null, false);
        ArrayList<Player> correctTargetList = new ArrayList<>();
        correctTargetList.add(testTargetPlayer1);
        correctTargetList.add(testTargetPlayer2);
        assertEquals(correctTargetList, action.targetablePlayer());
    }

}
