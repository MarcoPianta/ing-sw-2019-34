package Model;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

public class NormalSquareTest {
    @Test
    public void testConstructor(){
        NormalSquare normalSquareTest;
        normalSquareTest = new NormalSquare(null, null, null, null, null, null);
        assertTrue(normalSquareTest instanceof NormalSquare);
        assertTrue(normalSquareTest.getWeapons().isEmpty());
    }

    @Test
    public void setItemsGetItemTest() throws FileNotFoundException{
        CardNotOnlyAmmo ammoTest = new CardNotOnlyAmmo(AmmoEnum.AMMO1.getAbbreviation());
        NormalSquare normalSquareTest = new NormalSquare(null, null, null, null, null, null);
        normalSquareTest.setItems(ammoTest);
        assertEquals(normalSquareTest.getItem().getName(), ammoTest.getName());
    }

    @Test
    public void GrabItemTest() throws FileNotFoundException {
        CardNotOnlyAmmo ammoTest = new CardNotOnlyAmmo(AmmoEnum.AMMO1.getAbbreviation());
        NormalSquare normalSquareTest = new NormalSquare(null, null, null, null, null, ammoTest);
        assertEquals(ammoTest, normalSquareTest.grabItem());
        assertNull(normalSquareTest.getItem());
    }
}

