package Model;

import java.security.SecureRandom;

/**
 * This class is an iterator used to randomly extract cards
 * */
public class Drawer<E> {
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
