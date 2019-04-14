package Model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NormalSquareTest {
    @Test
    public void testConstructor(){
        NormalSquare normalSquareTest;
        normalSquareTest = new NormalSquare(null, null, null, null, null, null);
        assertTrue(normalSquareTest instanceof NormalSquare);
    }

    @Test
    public void AddAmmoGetItemTest(){
        CardNotOnlyAmmo ammoTest = new CardNotOnlyAmmo();
        NormalSquare normalSquareTest = new NormalSquare(null, null, null, null, null, null);
        normalSquareTest.addAmmo(ammoTest);
        assertEquals(normalSquareTest.getItem().getName(), ammoTest.getName());
    }

    @Test
    public void GrabItemTest(){
        CardNotOnlyAmmo ammoTest = new CardNotOnlyAmmo();
        NormalSquare normalSquareTest = new NormalSquare(null, null, null, null, null, ammoTest);
        assertEquals(ammoTest, normalSquareTest.grabItem());
        assertEquals(null, normalSquareTest.getItem());
    }
}

