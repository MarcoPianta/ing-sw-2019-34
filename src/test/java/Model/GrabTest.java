package Model;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

public class GrabTest {
    /**
     * This method test all the three constructor methods
     */

    @Test
    public void ConstructorTest() throws FileNotFoundException {
        //Test 1
        Colors color = null;
        Game testGame = new Game("gameID", 8);
        Player testPlayer = new Player("playerID", testGame, color, "name" );
        CardWeapon testWeapon = new CardWeapon (WeaponDictionary.CYBERBLADE.getAbbreviation());
        Grab action = new Grab(testPlayer, testWeapon);
        assertTrue(action instanceof Grab );
        //Test 2
        CardOnlyAmmo testOnlyAmmo= new CardOnlyAmmo (AmmoEnum.AMMO1.getAbbreviation());
        action = new Grab(testPlayer, testOnlyAmmo);
        assertTrue(action instanceof Grab );
        //Test 3
        CardNotOnlyAmmo testNotOnlyAmmo= new CardNotOnlyAmmo (AmmoEnum.AMMO1.getAbbreviation());
        action = new Grab(testPlayer, testNotOnlyAmmo);
        assertTrue(action instanceof Grab );
    }

    @Test
    public void isValidTest() throws FileNotFoundException {
        Colors color = null;
        Game testGame = new Game("gameID", 8);
        Player testPlayer = new Player("playerID", testGame, color, "name" );
        GameBoard testGameBoard = new GameBoard("map1");
        SpawnSquare testSpawnSquare = (SpawnSquare) testGameBoard.getRooms().get(0).getNormalSquares().get(2);
        CardWeapon testWeapon = new CardWeapon (WeaponDictionary.CYBERBLADE.getAbbreviation());
        testPlayer.newPosition(testSpawnSquare);
        testSpawnSquare.addWeapon(testWeapon);
        Grab action = new Grab(testPlayer, testWeapon);
        assertTrue(action.isValid());
        //Test 2
        CardOnlyAmmo testOnlyAmmo = new CardOnlyAmmo (AmmoEnum.AMMO1.getAbbreviation());
        action = new Grab(testPlayer, testOnlyAmmo);
        assertFalse(action.isValid());
        //Test 3
        CardNotOnlyAmmo testNotOnlyAmmo = new CardNotOnlyAmmo (AmmoEnum.AMMO1.getAbbreviation());
        action = new Grab(testPlayer, testNotOnlyAmmo);
        assertFalse(action.isValid());
    }

    @Test
    public void executeTest() throws FileNotFoundException {
        //TODO the method execute has to be finished
        Colors color = null;
        Game testGame = new Game("gameID", 8);
        Player testPlayer = new Player("playerID", testGame, color, "name" );
        GameBoard testGameBoard = new GameBoard("map1");
        SpawnSquare testSpawnSquare = (SpawnSquare) testGameBoard.getRooms().get(0).getNormalSquares().get(2);
        CardWeapon testWeapon = new CardWeapon (WeaponDictionary.CYBERBLADE.getAbbreviation());
        testPlayer.newPosition(testSpawnSquare);
        testSpawnSquare.addWeapon(testWeapon);
        Grab action = new Grab(testPlayer, testWeapon);
        assertTrue(action.execute());
        //Test 2
        CardOnlyAmmo testOnlyAmmo = new CardOnlyAmmo (AmmoEnum.AMMO1.getAbbreviation());
        action = new Grab(testPlayer, testOnlyAmmo);
        assertFalse(action.execute());
        //Test 3
        CardNotOnlyAmmo testNotOnlyAmmo = new CardNotOnlyAmmo (AmmoEnum.AMMO1.getAbbreviation());
        action = new Grab(testPlayer, testNotOnlyAmmo);
        assertFalse(action.execute());
    }
}
