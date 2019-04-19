package Model;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class HandTest {
     @Test
     public void constructorTest(){
         Player playerTest=new Player();
         PlayerBoard playerBoard= new PlayerBoard(Colors.GREEN, "playerTest",playerTest) ;
         Hand hand=new Hand(playerBoard);
         assertTrue(hand instanceof Hand);

     }
    /**
     * this method tests the adding of a new weapon and the removal of  ammo
     */
    @Test
    public void testAddWeapon() throws FileNotFoundException {
        Player playerTest=new Player();
        PlayerBoard playerBoard= new PlayerBoard(Colors.GREEN, "playerTest",playerTest) ;
        Hand hand=new Hand(playerBoard);
        String file= WeaponDictionary.FURNACE.getAbbreviation();
        CardWeapon cardWeapon=new CardWeapon(file);
        hand.addAmmo(3,3,3);
        hand.addWeapon(cardWeapon);
        assertFalse(hand.getPlayerWeapons().isEmpty());
    }

    /**
     * this method tests the adding of a new weapon when there are already three weapons
     * */
    @Test
    public void testSubstituteWeapons() throws FileNotFoundException{
        Player playerTest=new Player();
        PlayerBoard playerBoard= new PlayerBoard(Colors.GREEN, "playerTest",playerTest) ;
        Hand hand=new Hand(playerBoard);
        String file1= WeaponDictionary.ELECTROSCYTE.getAbbreviation();
        CardWeapon cardWeapon1=new CardWeapon(file1);
        hand.addWeapon(cardWeapon1);

        String file2= WeaponDictionary.CYBERBLADE.getAbbreviation();
        CardWeapon cardWeapon2=new CardWeapon(file2);
        hand.addWeapon(cardWeapon2);

        String file3= WeaponDictionary.FLAMETHROWER.getAbbreviation();
        CardWeapon cardWeapon3=new CardWeapon(file3);
        hand.addWeapon(cardWeapon3);

        String file4= WeaponDictionary.FURNACE.getAbbreviation();
        CardWeapon cardWeapon4=new CardWeapon(file4);

        ArrayList<CardWeapon> newPlayerWeapons= new ArrayList<CardWeapon>();
        newPlayerWeapons.add(cardWeapon1);
        newPlayerWeapons.add(cardWeapon2);
        newPlayerWeapons.add(cardWeapon4);

        hand.substituteWeapons(newPlayerWeapons);
        assertEquals(newPlayerWeapons,hand.getPlayerWeapons());
    }

    /**
     * this method tests the adding of power up
     * */
    @Test
    public void testAddPowerUp() throws FileNotFoundException {
        Player playerTest=new Player();
        PlayerBoard playerBoard= new PlayerBoard(Colors.GREEN, "playerTest",playerTest) ;
        Hand hand=new Hand(playerBoard);

        String file= PowerUpEnum.NEWTON_B.getAbbreviation();
        CardPowerUp cardPowerUp=new CardPowerUp(file);
        hand.addPowerUp(cardPowerUp);
        assertFalse(hand.getPlayerPowerUps().isEmpty());
    }

    /**
     * this method tests the charge of a weapon
     * */
    @Test
    public void testChargeWeapon() throws FileNotFoundException{
        Player playerTest=new Player();
        PlayerBoard playerBoard= new PlayerBoard(Colors.GREEN, "playerTest",playerTest) ;
        Hand hand=new Hand(playerBoard);

        String file1= WeaponDictionary.FURNACE.getAbbreviation();
        CardWeapon cardWeapon1=new CardWeapon(file1);
        String file2= WeaponDictionary.ELECTROSCYTE.getAbbreviation();
        CardWeapon cardWeapon2=new CardWeapon(file2);
        String file= PowerUpEnum.NEWTON_B.getAbbreviation();
        CardPowerUp cardPowerUp=new CardPowerUp(file);
        int red=3;
        int yellow=3;
        int blue=3;

        hand.addAmmo(red,yellow,blue);
        hand.addWeapon(cardWeapon1);
        hand.addWeapon(cardWeapon2);
        hand.getPlayerWeapons().get(0).setCharge(false);
        hand.getPlayerWeapons().get(1).setCharge(false);
        hand.addPowerUp(cardPowerUp);

        hand.chargeWeapon(cardWeapon1, 1,0,1);
        assertTrue(hand.getPlayerWeapons().get(0).isCharge());
        assertEquals(2, hand.getAmmoRYB()[0]);
        assertEquals(3, hand.getAmmoRYB()[1]);
        assertEquals(2, hand.getAmmoRYB()[2]);

        hand.chargeWeapon(cardWeapon2,0,0,1,cardPowerUp);
        assertTrue(hand.getPlayerWeapons().get(1).isCharge());
        assertEquals(2, hand.getAmmoRYB()[0]);
        assertEquals(3, hand.getAmmoRYB()[1]);
        assertEquals(2, hand.getAmmoRYB()[2]);
        assertEquals(0,hand.getPlayerPowerUps().size());
    }

    /**
     * this method tests the adding off ammo
     * */
    @Test
    public  void testAddAmmo(){
        Player playerTest=new Player();
        PlayerBoard playerBoard= new PlayerBoard(Colors.GREEN, "playerTest",playerTest) ;
        Hand hand=new Hand(playerBoard);

        int red=2;
        int yellow=1;
        int blue=0;
        hand.addAmmo(red,yellow,blue);
        assertEquals(2, hand.getAmmoRYB()[0]);
        assertEquals(1, hand.getAmmoRYB()[1]);
        assertEquals(0, hand.getAmmoRYB()[2]);

        red=2;
        hand.addAmmo(red,yellow,blue);
        assertEquals(3,hand.getAmmoRYB()[0]);
    }

}
