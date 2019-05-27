package Model;

import org.junit.jupiter.api.Test;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

public class InjureTest {

    @Test
    public void constructorTest() throws FileNotFoundException {
        Player testShooterPlayer = new Player(6546474, null);
        Player testTargetPlayer = new Player(6565754, null);
        CardWeapon testWeapon = new CardWeapon (WeaponDictionary.CYBERBLADE.getAbbreviation());
        Effect testEffect = testWeapon.getEffects().get(1);
        Injure action = new Injure(testShooterPlayer, testTargetPlayer, testEffect);
        assertTrue(action instanceof Injure);
    }

    @Test
    public void executeTest() throws FileNotFoundException {
        /**
         * This method test execute
         * */
        Colors color1 = Colors.RED, color2 = Colors.BLUE;
        Game game=new Game(5,"map1");
        Player testShooterPlayer = new Player(65754547, color1);
        Player testTargetPlayer = new Player(65457547, color2);
        testShooterPlayer.newPosition(game.getMap().getRooms().get(1).getNormalSquares().get(0));
        testTargetPlayer.newPosition(game.getMap().getRooms().get(1).getNormalSquares().get(0));
        CardWeapon testWeapon = new CardWeapon (WeaponDictionary.CYBERBLADE.getAbbreviation());
        Effect testEffect = testWeapon.getEffects().get(1);
        Injure action = new Injure(testShooterPlayer, testTargetPlayer, testEffect);
        assertTrue(action.execute());
    }

    @Test
    public void isValidTest() throws FileNotFoundException {
        /**
         * This method test isValid
         * First case: CyberBlade's precondition respected
         * Second case: CyberBlade's precondition not respected
         * */
        //Test 1
        Colors color1 = Colors.RED, color2 = Colors.BLUE;
        Game game=new Game(5,"map1");
        Player testShooterPlayer = new Player(76765437, color1);
        Player testTargetPlayer = new Player(85743326, color2);
        testShooterPlayer.newPosition(game.getMap().getRooms().get(1).getNormalSquares().get(0));
        testTargetPlayer.newPosition(game.getMap().getRooms().get(1).getNormalSquares().get(0));
        CardWeapon testWeapon = new CardWeapon (WeaponDictionary.CYBERBLADE.getAbbreviation());
        Effect testEffect = testWeapon.getEffects().get(1);
        Injure action = new Injure(testShooterPlayer, testTargetPlayer, testEffect);
        assertTrue(action.isValid());
        //Test 2
        testTargetPlayer.newPosition(game.getMap().getRooms().get(1).getNormalSquares().get(2));
        assertFalse(action.isValid());

    }
}
