package Model;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;

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

        ArrayList<CardPowerUp> testPowerUps = new ArrayList<>();
        CardPowerUp testPowerUp1 = new CardPowerUp("newton_R");
        CardPowerUp testPowerUp2 = new CardPowerUp("newton_Y");
        testPowerUps.add(testPowerUp1);
        testPowerUps.add(testPowerUp2);
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
