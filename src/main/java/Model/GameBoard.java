package Model;

import javax.json.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class GameBoard {
    private int DeadRoute;
    private String name;
    private ArrayList<Room> rooms;

    public GameBoard(String file) throws FileNotFoundException {
        int roomNumber;
        int roomDimension;
        Colors color;
        ArrayList<NormalSquare> squares;
        JsonObject currentRoom;
        JsonObject currentSquare;
        JsonObject jsonValues; /* this variable contains the JsonObject created from JSON file*/
        ArrayList<String> currentReference = new ArrayList<>(); //This variable is used to save the string that represent reference of other squares
        while (currentReference.size() <4 ) currentReference.add(""); //Initialize current reference with 4 empty strings
        String[] side = {"N", "E", "S", "W"};

        File fileJson = new File(getClass().getResource("/Map/"+file+".json").getFile());
        InputStream fis = new FileInputStream(fileJson);
        JsonReader reader = Json.createReader(fis);
        jsonValues = reader.readObject();
        reader.close();
        name = jsonValues.getString("name");
        roomNumber = jsonValues.getInt("roomNumber");
        rooms = new ArrayList<>(roomNumber);
        while (rooms.size() < roomNumber ) rooms.add(new Room(jsonValues.getJsonArray("Room" + (rooms.size()+1)).getJsonObject(0).getInt("dimension")));
        for (int i = 0; i < roomNumber; i++) {
            squares = new ArrayList<>();
            currentRoom = jsonValues.getJsonArray("Room" + (i+1)).getJsonObject(0);
            roomDimension = currentRoom.getInt("dimension");
            color = Colors.valueOf(currentRoom.getString("color"));

            for (int j = 0; j < roomDimension; j++){
                currentSquare = currentRoom.getJsonArray("Square").getJsonObject(j);//Get the j-th square inside Room array in json file
                for (int s = 0; s < 4; s++) {
                    if (!currentSquare.getString("Side" + side[s]).equals(""))
                        currentReference.set(s, currentSquare.getString("Side" + side[s]));
                    else
                        currentReference.set(s, ""+i+","+j);
                }
                if(!currentSquare.getBoolean("spawn"))
                    squares.add(new NormalSquare(rooms.get(Character.getNumericValue(currentReference.get(0).charAt(0))).getNormalSquares().get(Character.getNumericValue(currentReference.get(0).charAt(2))),
                                                 rooms.get(Character.getNumericValue(currentReference.get(1).charAt(0))).getNormalSquares().get(Character.getNumericValue(currentReference.get(1).charAt(2))),
                                                 rooms.get(Character.getNumericValue(currentReference.get(2).charAt(0))).getNormalSquares().get(Character.getNumericValue(currentReference.get(2).charAt(2))),
                                                 rooms.get(Character.getNumericValue(currentReference.get(3).charAt(0))).getNormalSquares().get(Character.getNumericValue(currentReference.get(3).charAt(2))),
                                                null,
                                                null));
                else {
                    squares.add(new SpawnSquare(rooms.get(Character.getNumericValue(currentReference.get(0).charAt(0))).getNormalSquares().get(Character.getNumericValue(currentReference.get(0).charAt(2))),
                                                rooms.get(Character.getNumericValue(currentReference.get(1).charAt(0))).getNormalSquares().get(Character.getNumericValue(currentReference.get(1).charAt(2))),
                                                rooms.get(Character.getNumericValue(currentReference.get(2).charAt(0))).getNormalSquares().get(Character.getNumericValue(currentReference.get(2).charAt(2))),
                                                rooms.get(Character.getNumericValue(currentReference.get(3).charAt(0))).getNormalSquares().get(Character.getNumericValue(currentReference.get(3).charAt(2))),
                                                null,
                                                new ArrayList<>(3)));
                }
            }
            rooms.set(i, new Room(squares, color));
        }
    }

    public List<Room> getRooms() {
        return new ArrayList<>(rooms);
    }

    public String getName() {
        return name;
    }
}