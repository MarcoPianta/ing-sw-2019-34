package network.messages;

/**
 * This Class is a message used to ask the client which Game configuration want to use, Map, number of skulls.
 */
public class GameSettingsRequest extends Message{

    public GameSettingsRequest(Integer token){
        actionType = ActionType.GAMESETTINGSREQUEST;
        this.token = token;
    }
}
