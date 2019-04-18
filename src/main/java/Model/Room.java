package Model;

import java.util.ArrayList;

public class Room{

    private ArrayList<NormalSquare> normalSquares;
    private Colors color;

    public Room(ArrayList<NormalSquare> normalSquares, Colors color) {
        this.normalSquares = new ArrayList<>(normalSquares);
        this.color = color;
    }

    public Room(int dimension){
        normalSquares = new ArrayList<>();
        while (normalSquares.size() < dimension)
            normalSquares.add(new NormalSquare());
    }

    public ArrayList<NormalSquare> getNormalSquares() {
        return new ArrayList<>(normalSquares);
    }

    public Colors getColor() {
        return color;
    }
}
