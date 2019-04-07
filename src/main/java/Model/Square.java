package Model;

/**This class represent the generalization of the square type that are the component of the room*/
public abstract class Square {
    protected Square N;
    protected Square E;
    protected Square S;
    protected Square W;
    protected String color;

    public Square() {

    }
    /**This method will be implemented by the classes that extends Square with the Override */
    public Card grabItem(int position) {

        return null;
    }
}
