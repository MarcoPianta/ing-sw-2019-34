package Model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NormalSquareTest {
    @Test
    public void testConstructor(){
        NormalSquare normalSquareTest;
        normalSquareTest = new NormalSquare(null, null, null, null, null);
        assertTrue(normalSquareTest instanceof NormalSquare);
    }

    @Test
    public void AddAmmoGetItemTest(){
        CardNotOnlyAmmo ammoTest = new CardNotOnlyAmmo();
        NormalSquare normalSquareTest = new NormalSquare(null, null, null, null, null);
        normalSquareTest.addAmmo(ammoTest);
        assertTrue(normalSquareTest.getItem() instanceof CardAmmo);
    }

    @Test
    public void GrabItemTest(){
        CardNotOnlyAmmo ammoTest = new CardNotOnlyAmmo();
        NormalSquare normalSquareTest = new NormalSquare(null, null, null, null, null);

        assertTrue(normalSquareTest.grabItem(0) instanceof CardAmmo || normalSquareTest.grabItem(0) == null);
        assertTrue(normalSquareTest.getItem() == null);
    }
}

