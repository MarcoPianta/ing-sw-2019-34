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

    /**This test method is used to check if json object contains the correct values*/
    @Test
    public void testGetJson(){
        CardWeapon cardWeapon;
        try {
            cardWeapon = new CardWeapon("electroscyte.json");
        }catch (FileNotFoundException e){
            return;
        }
        assertTrue(cardWeapon.getJsonValues().getBoolean("effectOrder")); //Excepted value from effectOrder is true
        assertEquals(0,cardWeapon.getJsonValues().getJsonArray("basicEffect").getJsonObject(0).getInt("mark")); //Test to get a JSONObject inside of an array
    }
}
