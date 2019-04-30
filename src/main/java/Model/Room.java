package Model;

import java.util.ArrayList;
import java.util.List;

public class Room{

    private ArrayList<NormalSquare> normalSquares;
    private Colors color;

    public Room(List<NormalSquare> normalSquares, Colors color) {
        this.normalSquares = new ArrayList<>(normalSquares);
        this.color = color;
    }

    public Room(int dimension, Colors color){
        normalSquares = new ArrayList<>();
        while (normalSquares.size() < dimension)
            normalSquares.add(new NormalSquare());
        this.color = color;
    }

    public ArrayList<NormalSquare> getNormalSquares() {
        return new ArrayList<>(normalSquares);
    }

    public Colors getColor() {
        return color;
    }
}
