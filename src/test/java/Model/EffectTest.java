package Model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class EffectTest {

    /**
     * This method is used to test if constructor read values from json file correctly.
     * The assertions are based on cyberblade card, so they test if values in attributes match values in json file
     * */
    @Test
    public void constructorTest() throws FileNotFoundException {
        JsonObject jsonValues;
        String file = WeaponDictionary.CYBERBLADE.getAbbreviation();
        File fileJson = new File(getClass().getResource("/Weapon/"+file+".json").getFile());
        InputStream fis = new FileInputStream(fileJson);
        JsonReader reader = Json.createReader(fis);
        jsonValues = reader.readObject();
        reader.close();

        Effect effect = new Effect(jsonValues, 1);
        assertEquals(0, effect.getBonusCost()[0]);
        assertEquals(1, effect.getTargetNumber());
        assertEquals(0, effect.getSquareNumber());
        assertEquals("pM", effect.getActionSequence());
        assertEquals(2, effect.getpDamage().get(0));
        assertEquals(2, effect.getpMark().get(0));
        assertEquals(0, effect.getsDamage().get(0));
        assertEquals(0, effect.getsMark().get(0));
        assertEquals(0, effect.getrDamage());
        assertEquals(0, effect.getrMark());
        assertEquals(1, effect.getMyMove());
        assertEquals(0, effect.getTargetMove());
    }

    /**
     * The following methods are used to test inner class in Effect class.
     * Inner class are PreCondition and Post Condition
     */
    @Test
    public void preConditionTest() throws FileNotFoundException{
        JsonObject jsonValues;
        String file = WeaponDictionary.CYBERBLADE.getAbbreviation();
        File fileJson = new File(getClass().getResource("/Weapon/"+file+".json").getFile());
        InputStream fis = new FileInputStream(fileJson);
        JsonReader reader = Json.createReader(fis);
        jsonValues = reader.readObject();
        reader.close();

        Effect effect = new Effect(jsonValues, 1);

        assertTrue(effect.getPreCondition().isVision());
        assertFalse(effect.getPreCondition().isBlind());
        assertTrue(effect.getPreCondition().isMelee());
        assertEquals(0, effect.getPreCondition().getMinRange());
        assertEquals(0, effect.getPreCondition().getMaxRange());
        assertFalse(effect.getPreCondition().isCardinal());
        assertFalse(effect.getPreCondition().isEnemiesDifferentSquare());
        assertFalse(effect.getPreCondition().isSameSquare());
    }
}
