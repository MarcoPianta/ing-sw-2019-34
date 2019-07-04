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
    public void setItemsGetWeaponsTest() throws FileNotFoundException{
        ArrayList<CardWeapon> weaponsTest = new ArrayList<>();
        CardWeapon weaponTest1;
        CardWeapon weaponTest2;
        CardWeapon weaponTest3;
        weaponTest1 = new CardWeapon(WeaponDictionary.ELECTROSCYTE.getAbbreviation());
        weaponTest2 = new CardWeapon(WeaponDictionary.FURNACE.getAbbreviation());
        weaponTest3 = new CardWeapon(WeaponDictionary.GRENADELAUNCHER.getAbbreviation());
        weaponsTest.add(weaponTest1);
        weaponsTest.add(weaponTest2);
        weaponsTest.add(weaponTest3);

        SpawnSquare spawnSquare = new SpawnSquare(null, null, null, null, null, weaponsTest);
        spawnSquare.setItems(weaponTest1);
        assertEquals(weaponTest1, spawnSquare.getWeapons().get(0));
    }

    @Test
    public void GrabItemTest() throws FileNotFoundException{
        ArrayList<CardWeapon> weapons = new ArrayList<>();
        weapons.add(new CardWeapon(WeaponDictionary.CYBERBLADE.getAbbreviation()));
        SpawnSquare spawnSquare = new SpawnSquare(null, null, null, null, null, weapons);
        assertEquals(weapons.get(0), spawnSquare.grabItem(0));
        assertNull(spawnSquare.grabItem(0));
        assertEquals(1, spawnSquare.getWeapons().size());
    }
}
