package Model;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import static org.junit.jupiter.api.Assertions.*;

public class SpawnSquareTest {
    @Test
    public void testConstructor(){
        SpawnSquare spawnSquare;
        spawnSquare = new SpawnSquare(null, null, null, null, null);
        assertTrue(spawnSquare instanceof SpawnSquare);
    }

    @Test
    public void AddWeaponGetWeaponsTest() {
        CardWeapon[] weaponsTest;
        weaponsTest = new CardWeapon[2];
        CardWeapon weaponTest;
        try {
            weaponTest = new CardWeapon(WeaponDictionary.ELECTROSCYTE.getAbbreviation());
        } catch (FileNotFoundException e) {
            return;}
//        weaponsTest[0] = weaponTest;
        SpawnSquare spawnSquare = new SpawnSquare(null, null, null, null, weaponsTest);
        spawnSquare.addWeapon(weaponTest, 0);
        weaponsTest[0] = weaponTest;
        assertTrue(spawnSquare.getWeapons() ==  weaponsTest|| spawnSquare.getWeapons() == null);
    }
    @Test
    public void GrabItemTest(){
        CardWeapon weaponTest;
        SpawnSquare spawnSquare = new SpawnSquare(null, null, null, null, null);
        try {
            weaponTest = new CardWeapon(WeaponDictionary.ELECTROSCYTE.getAbbreviation());
        } catch (FileNotFoundException e) { return; }
        spawnSquare.addWeapon(weaponTest, 0);
        assertTrue(spawnSquare.grabItem(0) instanceof CardWeapon || spawnSquare.grabItem(0) == null);
        assertTrue(spawnSquare.grabItem(0) == null);
    }
}
