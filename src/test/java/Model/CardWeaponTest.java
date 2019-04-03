package Model;

import org.junit.jupiter.api.Test;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

public class CardWeaponTest {
    @Test
    public void testConstructor(){
        CardWeapon weapon;
        try{
            weapon = new CardWeapon("electroscyte.json");
        }catch (FileNotFoundException e) {
            return;
        }
        assertEquals("electroscyte", weapon.getName());
        assertEquals(Colors.BLUE, weapon.getColor());
        assertEquals(1,weapon.getEffectsNumber());
        assertTrue(weapon instanceof CardWeapon);

    }
}
