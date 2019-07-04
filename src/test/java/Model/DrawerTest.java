package Model;

import org.junit.jupiter.api.Test;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

public class DrawerTest {

    @Test
    public void drawWeaponTest()throws FileNotFoundException {
        DeckCreator deckCreator = new DeckCreator();
        Deck<CardWeapon> deck = deckCreator.createDeck("WEAPON");
        Drawer<CardWeapon> drawer = deck.createDrawer();

        assertTrue(drawer.hasNext());
        CardWeapon c = drawer.draw();
        assertTrue(() -> {
            for (int i=0; i < WeaponDictionary.values().length; i++){
                if (c.getName().toUpperCase().equals(WeaponDictionary.values()[i].toString()))
                    return true;
            }
            return false;
        });
        assertTrue(drawer.hasNext());
    }
}