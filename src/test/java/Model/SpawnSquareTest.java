package Model;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class SpawnSquareTest {
    @Test
    public void testConstructor(){
        SpawnSquare spawnSquare;
        spawnSquare = new SpawnSquare(null, null, null, null, null, new ArrayList<>());
        assertTrue(spawnSquare instanceof SpawnSquare);
        assertTrue(spawnSquare.isSpawn());
    }

    @Test
    public void AddWeaponGetWeaponsTest() throws FileNotFoundException{
        ArrayList<CardWeapon> weaponsTest = new ArrayList<>();
        weaponsTest.add(null);
        CardWeapon weaponTest;
        weaponTest = new CardWeapon(WeaponDictionary.ELECTROSCYTE.getAbbreviation());

        SpawnSquare spawnSquare = new SpawnSquare(null, null, null, null, null, weaponsTest);
        spawnSquare.addWeapon(weaponTest, 0);
        assertEquals(weaponTest, spawnSquare.getWeapons().get(0));
    }

    @Test
    public void GrabItemTest() throws FileNotFoundException{
        ArrayList<CardWeapon> weapons = new ArrayList<>();
        weapons.add(new CardWeapon(WeaponDictionary.CYBERBLADE.getAbbreviation()));
        SpawnSquare spawnSquare = new SpawnSquare(null, null, null, null, null, weapons);
        assertEquals(weapons.get(0), spawnSquare.grabItem(0));
        assertNull(spawnSquare.grabItem(0));
        assertNull(spawnSquare.getWeapons().get(0));
    }
}
