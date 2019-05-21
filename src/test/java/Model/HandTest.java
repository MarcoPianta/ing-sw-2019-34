package Model;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class HandTest {
    /*
     *This method tests the constructor
     * */
     @Test
     public void constructorTest(){
         Player player=new Player(832, Colors.GREEN);
         assertNotNull(player.getPlayerBoard().getHandPlayer());
     }
    /**
     * this method tests the adding of a new weapon and the removal of  ammo
     */
    @Test
    public void testAddWeapon() throws FileNotFoundException {
        Player player=new Player(2832, Colors.GREEN);
        String file= WeaponDictionary.FURNACE.getAbbreviation();
        CardWeapon cardWeapon=new CardWeapon(file);
        player.getPlayerBoard().getHandPlayer().addAmmo(3,3,3);
        player.getPlayerBoard().getHandPlayer().addWeapon(cardWeapon);
        assertFalse(player.getPlayerBoard().getHandPlayer().getPlayerWeapons().isEmpty());
    }

    /**
     * this method tests the adding of a new weapon when there are already three weapons
     * */
    @Test
    public void testSubstituteWeapons() throws FileNotFoundException{
        Player player=new Player(832, Colors.GREEN);
        String file1= WeaponDictionary.ELECTROSCYTE.getAbbreviation();
        CardWeapon cardWeapon1=new CardWeapon(file1);
        player.getPlayerBoard().getHandPlayer().addWeapon(cardWeapon1);

        String file2= WeaponDictionary.CYBERBLADE.getAbbreviation();
        CardWeapon cardWeapon2=new CardWeapon(file2);
        player.getPlayerBoard().getHandPlayer().addWeapon(cardWeapon2);

        String file3= WeaponDictionary.FLAMETHROWER.getAbbreviation();
        CardWeapon cardWeapon3=new CardWeapon(file3);
        player.getPlayerBoard().getHandPlayer().addWeapon(cardWeapon3);

        String file4= WeaponDictionary.FURNACE.getAbbreviation();
        CardWeapon cardWeapon4=new CardWeapon(file4);

        ArrayList<CardWeapon> newPlayerWeapons= new ArrayList<>();
        newPlayerWeapons.add(cardWeapon1);
        newPlayerWeapons.add(cardWeapon2);
        newPlayerWeapons.add(cardWeapon4);

        player.getPlayerBoard().getHandPlayer().substituteWeapons(newPlayerWeapons);
        assertEquals(newPlayerWeapons,player.getPlayerBoard().getHandPlayer().getPlayerWeapons());
    }

    /**
     * this method tests the adding of power up
     * */
    @Test
    public void testAddPowerUp() throws FileNotFoundException {
        Player player=new Player(2832, Colors.GREEN);

        String file= PowerUpEnum.NEWTON_B.getAbbreviation();
        CardPowerUp cardPowerUp=new CardPowerUp(file);
        player.getPlayerBoard().getHandPlayer().addPowerUp(cardPowerUp);
        assertFalse(player.getPlayerBoard().getHandPlayer().getPlayerPowerUps().isEmpty());
    }

    /**
     * this method tests the charge of a weapon
     * */
    @Test
    public void testChargeWeapon() throws FileNotFoundException{
        Player player=new Player(2832, Colors.GREEN);

        String file1= WeaponDictionary.FURNACE.getAbbreviation();
        CardWeapon cardWeapon1=new CardWeapon(file1);
        String file2= WeaponDictionary.ELECTROSCYTE.getAbbreviation();
        CardWeapon cardWeapon2=new CardWeapon(file2);
        String file= PowerUpEnum.NEWTON_B.getAbbreviation();
        CardPowerUp cardPowerUp=new CardPowerUp(file);
        int red=3;
        int yellow=3;
        int blue=3;

        player.getPlayerBoard().getHandPlayer().addAmmo(red,yellow,blue);
        player.getPlayerBoard().getHandPlayer().addWeapon(cardWeapon1);
        player.getPlayerBoard().getHandPlayer().addWeapon(cardWeapon2);
        player.getPlayerBoard().getHandPlayer().getPlayerWeapons().get(0).setCharge(false);
        player.getPlayerBoard().getHandPlayer().getPlayerWeapons().get(1).setCharge(false);
        player.getPlayerBoard().getHandPlayer().addPowerUp(cardPowerUp);

        player.getPlayerBoard().getHandPlayer().chargeWeapon(cardWeapon1, 1,0,1);
        assertTrue(player.getPlayerBoard().getHandPlayer().getPlayerWeapons().get(0).isCharge());
        assertEquals(2, player.getPlayerBoard().getHandPlayer().getAmmoRYB()[0]);
        assertEquals(3, player.getPlayerBoard().getHandPlayer().getAmmoRYB()[1]);
        assertEquals(2, player.getPlayerBoard().getHandPlayer().getAmmoRYB()[2]);

        /*TODO player.getPlayerBoard().getHandPlayer().chargeWeapon(cardWeapon2,0,0,1,cardPowerUp);
        assertTrue(player.getPlayerBoard().getHandPlayer().getPlayerWeapons().get(1).isCharge());
        assertEquals(2, player.getPlayerBoard().getHandPlayer().getAmmoRYB()[0]);
        assertEquals(3, player.getPlayerBoard().getHandPlayer().getAmmoRYB()[1]);
        assertEquals(2, player.getPlayerBoard().getHandPlayer().getAmmoRYB()[2]);
        assertEquals(0,player.getPlayerBoard().getHandPlayer().getPlayerPowerUps().size());*/
    }

    /**
     * this method tests the adding off ammo
     * */
    @Test
    public  void testAddAmmo(){
        Player player=new Player(832, Colors.GREEN);

        int red=2;
        int yellow=1;
        int blue=0;
        player.getPlayerBoard().getHandPlayer().addAmmo(red,yellow,blue);
        assertEquals(2, player.getPlayerBoard().getHandPlayer().getAmmoRYB()[0]);
        assertEquals(1, player.getPlayerBoard().getHandPlayer().getAmmoRYB()[1]);
        assertEquals(0, player.getPlayerBoard().getHandPlayer().getAmmoRYB()[2]);

        red=2;
        player.getPlayerBoard().getHandPlayer().addAmmo(red,yellow,blue);
        assertEquals(3,player.getPlayerBoard().getHandPlayer().getAmmoRYB()[0]);
    }

}
