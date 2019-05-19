package network.messages;

/**
 * This message is sent from client to server.
 * The message is used from the client to tell the server which are the preferences for the Game, preferences are:
 * skullNumber: the number of skulls the player want to use
 * mapNumber: the map (GameBoard) the player want to use
 */
public class GameSettingsResponse extends Message{
    private int skullNumber;
    private int mapNumber;

    public GameSettingsResponse(Integer token, int skullNumber, int mapNumber){
        this.actionType = ActionType.GAMESETTINGSRESPONSE;
        this.token = token;
        this.skullNumber = skullNumber;
        this.mapNumber = mapNumber;
    }

    public int getSkullNumber() {
        return skullNumber;
    }

    public int getMapNumber() {
        return mapNumber;
    }
}
