package Model;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

public class DeckCollectorTest {

    /**
     * This method tests if deck collector is created correctly
     * */
    @Test
    public void createDecksTest() throws FileNotFoundException {
        DeckCollector deckCollector = new Game(5, "map1").getDeckCollector();
        Deck<CardWeapon> cardWeaponDeck = deckCollector.getCardWeaponDeck();
        Deck<CardPowerUp> cardPowerUpDeck = deckCollector.getCardPowerUpDeck();
        Deck<CardAmmo> cardAmmoDeck = deckCollector.getCardAmmoDeck();

        assertEquals(1, deckCollector.getCardWeaponDeck().getSize()); //TODO Now expected is 2 but must be changed when all cardWeapon json file will be checked
        assertEquals(12, deckCollector.getCardPowerUpDeck().getSize()); //Expected value for PowerUp deck size is 12
        assertEquals(36, deckCollector.getCardAmmoDeck().getSize()); //Expected value for Ammo deck size is 36

        assertNotNull(deckCollector.getCardWeaponDrawer().draw());
        assertNotNull(deckCollector.getCardPowerUpDrawer().draw());
        assertNotNull(deckCollector.getCardAmmoDrawer().draw());

        assertEquals(0, deckCollector.getCardWeaponDeck().getSize());
        assertEquals(11, deckCollector.getCardPowerUpDeck().getSize());
        assertEquals(35, deckCollector.getCardAmmoDeck().getSize());

        assertNotNull(deckCollector.getCardPowerUpDrawer().draw());
        assertNotNull(deckCollector.getCardAmmoDrawer().draw());

        assertEquals(10, deckCollector.getCardPowerUpDeck().getSize());
        assertEquals(34, deckCollector.getCardAmmoDeck().getSize());
    }
}
