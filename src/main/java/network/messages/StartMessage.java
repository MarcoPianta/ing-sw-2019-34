package network.messages;

import Model.Colors;

/**
 * This message is used to communicate to the client that the Game or the turn is started.
 * To differentiate the start of the turn or the Game the String type is used
 */
public class StartMessage extends Message{
    private String type; //This String can assume values "turn" or "game"
    private int skullNumber;
    private String map;
    private Colors myColor;

    public StartMessage(Integer token, String type, int skullNumber, String map, Colors myColor){
        this.actionType = ActionType.START;
        this.token = token;
        this.type = type;
        this.skullNumber = skullNumber;
        this.map = map;
        this.myColor = myColor;
    }

    public String getType() {
        return type;
    }

    public int getSkullNumber() {
        return skullNumber;
    }

    public String getMap() {
        return map;
    }

    public Colors getMyColor() {
        return myColor;
    }
}
