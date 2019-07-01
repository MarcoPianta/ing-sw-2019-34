package Model;

import javax.json.*;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public final class GameBoard implements Serializable {
    private String name;
    private ArrayList<Room> rooms;

    public GameBoard(String file) throws FileNotFoundException {
        JsonObject jsonValues = Utils.JsonFileHandler.openFile("Map", file); // this variable contains the JsonObject created from JSON file
        name = jsonValues.getString("name");

        createRooms(jsonValues);
    }

    public List<Room> getRooms() {
        return new ArrayList<>(rooms);
    }

    public String getName() {
        return name;
    }

    public NormalSquare getSquareFromId(String id){
        for (Room r: rooms)
            for (NormalSquare s: r.getNormalSquares())
                if (s.getId().equals(id))
                    return s;
        return null;
    }

    private void createRooms(JsonObject jsonValues){
        int roomNumber;
        int roomDimension;
        JsonObject currentRoom;
        JsonObject currentSquare;
        NormalSquare square;
        ArrayList<String> currentReference = new ArrayList<>(); //This variable is used to save the string that represent reference of other squares
        String[] side = {"N", "E", "S", "W"};

        while (currentReference.size() < 4 ) currentReference.add(""); //Initialize current reference with 4 empty strings
        roomNumber = jsonValues.getInt("roomNumber");
        rooms = new ArrayList<>(roomNumber);

        while (rooms.size() < roomNumber ) //Initialize rooms with squares that have null sides
            rooms.add(new Room(jsonValues.getJsonArray("Room" + (rooms.size()+1)).getJsonObject(0).getInt("dimension"),
                Colors.valueOf(jsonValues.getJsonArray("Room" + (rooms.size()+1)).getJsonObject(0).getString("color"))));

        for (int i = 0; i < roomNumber; i++) {
            currentRoom = jsonValues.getJsonArray("Room" + (i+1)).getJsonObject(0);
            roomDimension = currentRoom.getInt("dimension");

            for (int j = 0; j < roomDimension; j++){
                currentSquare = currentRoom.getJsonArray("Square").getJsonObject(j);//Get the j-th square inside Room array in json file
                square = rooms.get(i).getNormalSquares().get(j);
                for (int s = 0; s < 4; s++) {
                    if (!currentSquare.getString("Side" + side[s]).equals(""))
                        currentReference.set(s, currentSquare.getString("Side" + side[s]));
                    else
                        currentReference.set(s, ""+i+","+j);
                }
                if(!currentSquare.getBoolean("spawn")){
                    setSides(square, currentReference);
                    square.setId(currentSquare.getString("ID"));
                }
                else {
                    ArrayList<NormalSquare> tmpRoom;
                    SpawnSquare newSquare =  new SpawnSquare();
                    newSquare.setId(currentSquare.getString("ID"));
                    tmpRoom = rooms.get(i).getNormalSquares();
                    tmpRoom.set(j, newSquare);
                    rooms.set(i, new Room(tmpRoom, rooms.get(i).getColor()));

                    setSides(newSquare, currentReference);

                    updateRoomReferences(newSquare);
                }
            }
        }
    }

    private void setSides(NormalSquare square, ArrayList<String> currentReference){
        System.out.println("current ref " + currentReference);
        System.out.println("Adding " + square.getId());
        square.setN(rooms.get(Character.getNumericValue(currentReference.get(0).charAt(0))).getNormalSquares().get(Character.getNumericValue(currentReference.get(0).charAt(2))));
        square.setE(rooms.get(Character.getNumericValue(currentReference.get(1).charAt(0))).getNormalSquares().get(Character.getNumericValue(currentReference.get(1).charAt(2))));
        System.out.println(rooms.size());
        System.out.println(rooms.get(Character.getNumericValue(currentReference.get(2).charAt(0))).getNormalSquares());
        System.out.println(Character.getNumericValue(currentReference.get(2).charAt(2)));
        System.out.println(rooms.get(Character.getNumericValue(currentReference.get(2).charAt(0))).getNormalSquares().get(Character.getNumericValue(currentReference.get(2).charAt(2))));
        square.setS(rooms.get(Character.getNumericValue(currentReference.get(2).charAt(0))).getNormalSquares().get(Character.getNumericValue(currentReference.get(2).charAt(2))));
        square.setW(rooms.get(Character.getNumericValue(currentReference.get(3).charAt(0))).getNormalSquares().get(Character.getNumericValue(currentReference.get(3).charAt(2))));

    }

    private void updateRoomReferences(NormalSquare square){
        NormalSquare nextSquare = square.getN();
        if ((nextSquare != square) && (nextSquare.getS() != square))
            nextSquare.setS(square);

        nextSquare = square.getE();
        if ((nextSquare != square) && (nextSquare.getW() != square))
            nextSquare.setW(square);

        nextSquare = square.getS();
        if ((nextSquare != square) && (nextSquare.getN() != square))
            nextSquare.setN(square);

        nextSquare = square.getW();
        if ((nextSquare != square) && (nextSquare.getE() != square))
            nextSquare.setE(square);
    }
}