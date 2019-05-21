package Model;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReloadTest {

    @Test
    public void constructorTest() throws FileNotFoundException {
        Player testPlayer = new Player(564347347, null);
        CardWeapon testWeapon = new CardWeapon(WeaponDictionary.CYBERBLADE.getAbbreviation());
        testPlayer.getPlayerBoard().getHandPlayer().addWeapon(testWeapon);
        testPlayer.getPlayerBoard().getHandPlayer().addAmmo(testWeapon.getRedCost(), testWeapon.getYellowCost(), testWeapon.getBlueCost());
        Reload action = new Reload(testPlayer, testWeapon);
        assertTrue(action instanceof Reload);

        CardPowerUp testPowerUp = new CardPowerUp("newton_R");
        action = new Reload(testPlayer, testWeapon, testPowerUp);
        assertTrue(action instanceof Reload);
    }

    @Test
    public void isValidTest() throws FileNotFoundException {
        Player testPlayer = new Player(76586485, null);
        CardWeapon testWeapon = new CardWeapon(WeaponDictionary.CYBERBLADE.getAbbreviation());
        testPlayer.getPlayerBoard().getHandPlayer().addWeapon(testWeapon);
        testPlayer.getPlayerBoard().getHandPlayer().addAmmo(testWeapon.getRedCost(), testWeapon.getYellowCost(), testWeapon.getBlueCost());
        Reload action = new Reload(testPlayer, testWeapon);
        assertTrue(action.isValid());
    }

    @Test
    public void executeTest() throws FileNotFoundException {
        Player testPlayer = new Player(56485, null);
        CardWeapon testWeapon = new CardWeapon(WeaponDictionary.CYBERBLADE.getAbbreviation());
        testPlayer.getPlayerBoard().getHandPlayer().addWeapon(testWeapon);
        testPlayer.getPlayerBoard().getHandPlayer().addAmmo(testWeapon.getRedCost(), testWeapon.getYellowCost(), testWeapon.getBlueCost());
        Reload action = new Reload(testPlayer, testWeapon);
        assertTrue(action.execute());
        assertTrue(testWeapon.isCharge());
    }
}
