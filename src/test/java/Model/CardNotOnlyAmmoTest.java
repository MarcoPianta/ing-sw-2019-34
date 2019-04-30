package Model;

import org.junit.jupiter.api.Test;
import java.io.FileNotFoundException;
import static org.junit.jupiter.api.Assertions.*;

public class CardNotOnlyAmmoTest {

    /**
     * This method tests if CardNotOnlyAmmo read form json file and initialize attributes correctly.
     * */
    @Test
    public void constructorTest() throws FileNotFoundException {
        CardNotOnlyAmmo cardNotOnlyAmmo;
        Deck<CardPowerUp> deck, deckCopy;
        DeckCreator deckCreator = new DeckCreator();
        Drawer<CardPowerUp> drawer;

        cardNotOnlyAmmo = new CardNotOnlyAmmo(AmmoEnum.AMMO1.getAbbreviation());
        deck = deckCreator.createDeck("POWERUP");
        drawer = deck.createDrawer();
        deckCopy = deck.copy();

        assertEquals(AmmoColors.BLUE, cardNotOnlyAmmo.getItem2());
        assertEquals(AmmoColors.BLUE, cardNotOnlyAmmo.getItem3());
        cardNotOnlyAmmo.getItem1(drawer);
        assertEquals(deck.getSize(), deckCopy.getSize()-1);
    }

    @Test
    public void copyCardTest() throws FileNotFoundException{
        CardNotOnlyAmmo cardNotOnlyAmmo, cardNotOnlyAmmoCopy;

        cardNotOnlyAmmo = new CardNotOnlyAmmo(AmmoEnum.AMMO1.getAbbreviation());
        cardNotOnlyAmmoCopy = cardNotOnlyAmmo.copyCardAmmo();

        assertEquals(cardNotOnlyAmmo.getItem2(), cardNotOnlyAmmo.getItem2());
        assertEquals(cardNotOnlyAmmo.getItem3(), cardNotOnlyAmmo.getItem3());
    }
}
