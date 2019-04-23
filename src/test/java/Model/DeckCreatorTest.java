package Model;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import static org.junit.jupiter.api.Assertions.*;

public class DeckCreatorTest {

    /**
     * This method test DeckCreator class when is used to create a WeaponCard's deck
     * */
    @Test
    public void createDeckWeaponTest() throws FileNotFoundException{
        Deck<CardWeapon> deck;
        DeckCreator deckCreator;

        deckCreator = new DeckCreator();
        deck = deckCreator.createDeck("WEAPON");

        assertTrue(deckCreator.createDeck("WEAPON").getCard(0) instanceof CardWeapon); //Check if card inserted in deck are of the correct type
        assertEquals(2, deck.getSize()); //Weapon card deck should be of 21 card (Now is 4 because not all json file are fixed)
        assertEquals(WeaponDictionary.CYBERBLADE.getAbbreviation(), deck.getCard(0).getName()); //Deck is created in alphabetical order so first card should be cyberblade
        assertEquals(1, deck.getSize()); // After getting a card this should be removed from the deck
    }

    /**
     * This method test DeckCreator class when is used to create a Ammo's deck
     * */
    @Test
    public void createDeckAmmoTest() throws FileNotFoundException{
        Deck<CardAmmo> deck;
        DeckCreator deckCreator;

        deckCreator = new DeckCreator();
        deck = deckCreator.createDeck("AMMO");

        //The following TO-DO statement should be uncommented when all json file for ammo will be created
        //TODO assertTrue(deckCreator.createDeck("AMMO").getCard(0) instanceof CardAmmo); //Check if card inserted in deck are of the correct type
        //TODO assertEquals(36, deck.getSize()); //Ammo card deck should be of 36 card
        //TODO deck.getCard();
        //TODO assertEquals(35, deck.getSize()); // After getting a card this should be removed from the deck

        //The following statement MUST be removed when TO-DO will be uncommented
        assertEquals(36, deck.getSize());
    }

    /**
     * This method test DeckCreator class when is used to create a Ammo's deck
     * */
    @Test
    public void createDeckPowerUpTest() throws FileNotFoundException{
        Deck<CardPowerUp> deck;
        DeckCreator deckCreator;

        deckCreator = new DeckCreator();
        deck = deckCreator.createDeck("POWERUP");

        //The following TO-DO statement should be uncommented when all json file for power ups will be created
        //TODO assertTrue(deckCreator.createDeck("POWERUP").getCard(0) instanceof CardPowerUp); //Check if card inserted in deck are of the correct type
        //TODO assertEquals(12, deck.getSize()); //Ammo card deck should be of 12 card
        //TODO deck.getCard();
        //TODO assertEquals(11, deck.getSize()); // After getting a card this should be removed from the deck

        //The following statement MUST be removed when TO-DO will be uncommented
        assertEquals(12, deck.getSize());
    }
}
