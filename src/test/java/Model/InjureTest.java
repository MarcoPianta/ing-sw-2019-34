package Model;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InjureTest {
    /**
     * This method test all the three constructor methods
     */
    @Test
    public void ConstructorTest() throws FileNotFoundException {
        Colors color1 = Colors.RED, color2 = Colors.BLUE;
        Player testPlayer1 = new Player("playerID", "gameID", color1, "name" );
        Player testPlayer2 = new Player("playerID", "gameID", color2, "name" );
        CardWeapon testWeapon = new CardWeapon (WeaponDictionary.CYBERBLADE.getAbbreviation());
        Effect testEffect = testWeapon.getEffects().get(1);
        Injure action = new Injure();
        action.Injure(testPlayer1, testPlayer2, testEffect);
        assertTrue(action instanceof Injure);
    }

    @Test
    public void executeTest() throws FileNotFoundException {
        /**
         * This method test the execute test of the Injure class
         */
        Colors color1 = Colors.RED, color2 = Colors.BLUE;
        Player testPlayer1 = new Player("playerID", "gameID", color1, "name" );
        Player testPlayer2 = new Player("playerID", "gameID", color2, "name" );
        CardWeapon testWeapon = new CardWeapon (WeaponDictionary.CYBERBLADE.getAbbreviation());
        Effect testEffect = testWeapon.getEffects().get(0);
        Injure action = new Injure();
        action.Injure(testPlayer1, testPlayer2, testEffect);
        action.execute();
        assertEquals(testEffect.getDamage() ,testPlayer2.getPlayerBoard().getDamageBar().size());
        assertEquals(Colors.RED, testPlayer2.getPlayerBoard().getDamageBar().get(0));
    }

    @Test
    public void isValidTest(){
        //TODO the method isValid has to be finished
    }
}
