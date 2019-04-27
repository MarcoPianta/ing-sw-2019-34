package Model;

import org.junit.jupiter.api.Test;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class GameBoardTest {

    /**
     * This method tests if GameBoard constructor read the map and store it in data structures correctly.
     * The method check only the second room and its side of map1.json
     * */
    @Test
    public void constructorTest() throws FileNotFoundException {
        GameBoard gameBoard = new GameBoard("map1");

        assertEquals("Map1", gameBoard.getName());
        assertEquals(3, gameBoard.getRooms().get(0).getNormalSquares().size());

        //The following assertion check the first square
        assertEquals(gameBoard.getRooms().get(0).getNormalSquares().get(0), gameBoard.getRooms().get(1).getNormalSquares().get(0).getN());
        assertEquals(gameBoard.getRooms().get(1).getNormalSquares().get(1), gameBoard.getRooms().get(1).getNormalSquares().get(0).getE());
        assertEquals(gameBoard.getRooms().get(1).getNormalSquares().get(0), gameBoard.getRooms().get(1).getNormalSquares().get(0).getS());
        assertEquals(gameBoard.getRooms().get(1).getNormalSquares().get(0), gameBoard.getRooms().get(1).getNormalSquares().get(0).getW());

        //The following assertion check the second square
        assertEquals(gameBoard.getRooms().get(1).getNormalSquares().get(1), gameBoard.getRooms().get(1).getNormalSquares().get(1).getN());
        assertEquals(gameBoard.getRooms().get(1).getNormalSquares().get(2), gameBoard.getRooms().get(1).getNormalSquares().get(1).getE());
        assertEquals(gameBoard.getRooms().get(3).getNormalSquares().get(0), gameBoard.getRooms().get(1).getNormalSquares().get(1).getS());
        assertEquals(gameBoard.getRooms().get(1).getNormalSquares().get(0), gameBoard.getRooms().get(1).getNormalSquares().get(1).getW());

        //The following assertion check the third square
        assertEquals(gameBoard.getRooms().get(0).getNormalSquares().get(2), gameBoard.getRooms().get(1).getNormalSquares().get(2).getN());
        assertEquals(gameBoard.getRooms().get(2).getNormalSquares().get(0), gameBoard.getRooms().get(1).getNormalSquares().get(2).getE());
        assertEquals(gameBoard.getRooms().get(1).getNormalSquares().get(2), gameBoard.getRooms().get(1).getNormalSquares().get(2).getS());
        assertEquals(gameBoard.getRooms().get(1).getNormalSquares().get(1), gameBoard.getRooms().get(1).getNormalSquares().get(2).getW());
    }
}
