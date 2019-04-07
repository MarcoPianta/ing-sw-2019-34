package Model;

import org.junit.jupiter.api.Test;
import java.io.FileNotFoundException;
import static org.junit.jupiter.api.Assertions.*;

public class CardWeaponTest {

    /**
     * This method test the constructor of CardWeapon
     * */
    @Test
    public void testConstructor(){
        CardWeapon weapon;
        try{
            weapon = new CardWeapon(WeaponDictionary.CYBERBLADE.getAbbreviation());
        }catch (FileNotFoundException e) {
            return;
        }
        assertEquals(WeaponDictionary.CYBERBLADE.getAbbreviation(), weapon.getName());
        assertEquals(AmmoColors.YELLOW, weapon.getColor());
        assertEquals(3,weapon.getEffectsNumber());
        assertTrue(weapon instanceof CardWeapon);
    }

    /**
     * This test method is used to check if effects ArrayList is created and "stored" in weapon class correctly
     * */
    @Test
    public void testGetEffects(){
        CardWeapon cardWeapon;
        try {
            cardWeapon = new CardWeapon(WeaponDictionary.ELECTROSCYTE.getAbbreviation());
        }catch (FileNotFoundException e){
            return;
        }
        assertTrue(cardWeapon.getEffects().size() == 1); //Expected size value  of effects for electroscyte is 1
        assertEquals(4 ,cardWeapon.getEffects().get(0).getTargetNumber()); //Expected value of target is 4
    }

    /**
     * Also if not necessary this test is used to check only getter and setter just to cover line of code
     * */
    @Test
    public void getterAndSetterTest(){
        CardWeapon card;
        try {
            card = new CardWeapon(WeaponDictionary.CYBERBLADE.getAbbreviation());
        }catch (FileNotFoundException e){
            return;
        }
        card.setCharge(true);
        assertTrue(card.isCharge());
    }
}
