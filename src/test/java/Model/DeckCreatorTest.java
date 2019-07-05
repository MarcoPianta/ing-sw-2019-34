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
        assertEquals(20, deck.getSize()); //Weapon card deck should be of 21 card (Now is 4 because not all json file are fixed)
        assertEquals(WeaponDictionary.CYBERBLADE.getAbbreviation(), deck.getCard(0).getName()); //Deck is created in alphabetical order so first card should be cyberblade
        //assertEquals(0, deck.getSize()); // After getting a card this should be removed from the deck
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

        assertTrue(deckCreator.createDeck("AMMO").getCard(0) instanceof CardAmmo); //Check if card inserted in deck are of the correct type
        assertEquals(36, deck.getSize()); //Ammo card deck should be of 36 card
        deck.getCard(0);
        assertEquals(35, deck.getSize()); // After getting a card this should be removed from the deck

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

        assertTrue(deckCreator.createDeck("POWERUP").getCard(0) instanceof CardPowerUp); //Check if card inserted in deck are of the correct type
        assertEquals(12, deck.getSize()); //Ammo card deck should be of 12 card
        deck.getCard(0);
        assertEquals(11, deck.getSize()); // After getting a card this should be removed from the deck

    }
}
