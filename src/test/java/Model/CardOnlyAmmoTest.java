package Model;

import org.junit.jupiter.api.Test;
import java.io.FileNotFoundException;
import static org.junit.jupiter.api.Assertions.*;

public class CardOnlyAmmoTest {

    /**
     * This method tests if the constructor read from json file and create the card correctly
     * */
    @Test
    public void constructorsTest() throws FileNotFoundException {
        CardOnlyAmmo cardOnlyAmmo;
        cardOnlyAmmo = new CardOnlyAmmo(AmmoEnum.AMMO1.getAbbreviation());

        assertEquals(AmmoColors.YELLOW, cardOnlyAmmo.getItem1());
        assertEquals(AmmoColors.BLUE, cardOnlyAmmo.getItem2());
        assertEquals(AmmoColors.BLUE, cardOnlyAmmo.getItem3());
        assertEquals("A1", cardOnlyAmmo.getName());

        //Test of default constructor only to get 100% coverage
        cardOnlyAmmo = new CardOnlyAmmo();

        assertNull(cardOnlyAmmo.getItem1());
    }

    @Test
    public void copyCardAmmoTest() throws FileNotFoundException{
        CardOnlyAmmo cardOnlyAmmo;
        CardOnlyAmmo copy;

        cardOnlyAmmo = new CardOnlyAmmo(AmmoEnum.AMMO1.getAbbreviation());
        copy = cardOnlyAmmo.copyCardAmmo();

        assertEquals(cardOnlyAmmo.getItem1(), copy.getItem1());
        assertEquals(cardOnlyAmmo.getItem2(), copy.getItem2());
        assertEquals(cardOnlyAmmo.getItem3(), copy.getItem3());
    }
}
