package Model;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MarkTest {
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
        Mark action = new Mark();
        action.Mark(testPlayer1, testPlayer2, testEffect);
        assertTrue(action instanceof Mark);
    }

    @Test
    public void executeTest() throws FileNotFoundException {
        /**
         * This method test the execute test of the Mark class
         */
        Colors color1 = Colors.RED, color2 = Colors.BLUE;
        Player testPlayer1 = new Player("playerID", "gameID", color1, "name" );
        Player testPlayer2 = new Player("playerID", "gameID", color2, "name" );
        CardWeapon testWeapon = new CardWeapon (WeaponDictionary.FURNACE.getAbbreviation());
        ArrayList<Effect> testEffect = new ArrayList<>(testWeapon.getEffects());
        Mark action = new Mark();
        action.Mark(testPlayer1, testPlayer2, testEffect.get(1));
        action.execute();
        Injure injure = new Injure();
        injure.Injure(testPlayer1, testPlayer2, testEffect.get(0));
        action.execute();
        assertEquals((testEffect.get(0).getDamage()+testEffect.get(1).getMark()) ,testPlayer2.getPlayerBoard().getDamageBar().size());
        //TODO --> there isn't a markBar to control so I can't assert anythings

    }


    @Test
    public void isValidTest(){
        //TODO the method isValid has to be finished
    }
}
