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
        Player testPlayer = new Player(243, color);
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
        Player testPlayer = new Player(37834782, color);
        GameBoard testGameBoard = new GameBoard("map1");
        SpawnSquare testSpawnSquare = (SpawnSquare) testGameBoard.getRooms().get(0).getNormalSquares().get(2);
        CardWeapon testWeapon = new CardWeapon (WeaponDictionary.CYBERBLADE.getAbbreviation());
        testPlayer.newPosition(testSpawnSquare);
        testSpawnSquare.setItems(testWeapon);
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
        Colors color = null;
        Game testGame = new Game( 8,"map1");
        Player testPlayer = new Player(4767745, color);;
        SpawnSquare testSpawnSquare = (SpawnSquare) testGame.getMap().getRooms().get(0).getNormalSquares().get(2);
        CardWeapon testWeapon = new CardWeapon (WeaponDictionary.CYBERBLADE.getAbbreviation());
        testPlayer.newPosition(testSpawnSquare);
        testSpawnSquare.setItems(testWeapon);
        Grab action = new Grab(testPlayer, testWeapon);
        assertTrue(action.execute());
        assertEquals(testWeapon,testPlayer.getPlayerBoard().getHandPlayer().getPlayerWeapons().get(0));
    }
}
