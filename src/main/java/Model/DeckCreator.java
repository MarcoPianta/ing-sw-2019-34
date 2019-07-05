package Model;

import java.io.FileNotFoundException;
import java.io.Serializable;

/**
 * This class is the Factory class for decks. Using createDeck method it's returned a deck
 * */
public class DeckCreator implements Serializable {
    /**
     * Method to create a deck of card, it takes a parameter which indicates the deck type
     * */
    public Deck createDeck(String type) { //Need to solve the problem of recognize what type of card deck need to be created
        switch (type){
            case "WEAPON" :
                return createWeaponDeck();
                //break
            case "AMMO" :
                return createAmmoDeck();
                //break
            case "POWERUP" :
                return createPowerUpDeck();
                //break
            default :
                return null;
        }
    }

    /**
     * This method return a WeaponCard deck
     * */
    private Deck<CardWeapon> createWeaponDeck() {
        Deck<CardWeapon> deck = new Deck<>(false);
        for (int i = 0; i < 20 /*WeaponDictionary.values().length*/; i++){ //TODO The end condition MUST be replaced when all json file are fixed
            try {
                deck.add(new CardWeapon(WeaponDictionary.values()[i].getAbbreviation()));
            }catch (FileNotFoundException e){
                e.getMessage();
            }
        }
        return deck;
    }

    /**
     * This method return an Ammo deck and also initialize the trash deck (which is in Deck class)
     * */
    private Deck<CardAmmo> createAmmoDeck() {
        Deck<CardAmmo> deck = new Deck<>(true);
        for (int i = 0; i < AmmoEnum.values().length; i++){
            try {
                deck.add(new CardOnlyAmmo(AmmoEnum.values()[i].getAbbreviation()));
            }catch (FileNotFoundException e){
                e.getMessage();
            }
        }
        for (int i = 0; i < AmmoEnum.values().length; i++){
            try {
                deck.add(new CardNotOnlyAmmo(AmmoEnum.values()[i].getAbbreviation()));
            }catch (FileNotFoundException e){
                e.getMessage();
            }
        }
        deck.setTrashDeck();
        return deck;
    }

    /**
     * This method return a PowerUp deck and also initialize the trash deck (which is in Deck class)
     * */
    private Deck<CardPowerUp> createPowerUpDeck() {
        Deck<CardPowerUp> deck = new Deck<>(false);
        for (int i = 0; i < PowerUpEnum.values().length; i++){
            try {
                deck.add(new CardPowerUp(PowerUpEnum.values()[i].getAbbreviation()));
            }catch (FileNotFoundException e){
                e.getMessage();
            }
        }
        deck.setTrashDeck();
        return deck;
    }
}