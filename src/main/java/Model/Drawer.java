package Model;

import java.io.Serializable;
import java.security.SecureRandom;

/**
 * This class is an iterator used to randomly extract cards.
 * Drawer is created by deck.
 * */
public class Drawer<E> implements Serializable {
    private Deck deck;

    public Drawer(Deck deck){
        this.deck = deck;
    }

    public boolean hasNext(){
        return deck.getSize() > 0;
    }

    public E draw(){ // This is next method
        SecureRandom rand = new SecureRandom();
        int index;

        index = rand.nextInt(deck.getSize());
        return (E) deck.getCard(index);
    }
}
