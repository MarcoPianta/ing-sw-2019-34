package Model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NormalSquareTest {
    @Test
    public void testConstructor(){
        NormalSquare normalSquare;

        normalSquare = new NormalSquare(null, null, null, null, null);
        assertTrue(normalSquare instanceof NormalSquare);
    }

    @Test
    public void AddAmmoGetItemTest(){
        CardNotOnlyAmmo Ammo = new CardNotOnlyAmmo();
        NormalSquare normalSquare = new NormalSquare(null, null, null, null, null);
        normalSquare.addAmmo(Ammo);
        assertTrue(normalSquare.getItem() instanceof CardAmmo);
    }

    @Test
    public void GrabItemTest(){
        CardNotOnlyAmmo Ammo = new CardNotOnlyAmmo();
        NormalSquare normalSquare = new NormalSquare(null, null, null, null, null);

        assertTrue(normalSquare.grabItem(0) instanceof CardAmmo || normalSquare.grabItem(0) == null);
        assertTrue(normalSquare.getItem() == null);
    }
}

