package Model;

import org.junit.jupiter.api.Test;
import java.io.FileNotFoundException;
import static org.junit.jupiter.api.Assertions.*;

public class DrawerTest {

    @Test
    public void drawTest()throws FileNotFoundException {
        Deck<CardWeapon> deck = new Deck<>(false);
        Drawer<CardWeapon> drawer = deck.createDrawer();

        deck.add(new CardWeapon(WeaponDictionary.CYBERBLADE.getAbbreviation()));
        assertTrue(drawer.hasNext());
        CardWeapon c = drawer.draw();
    }
}
