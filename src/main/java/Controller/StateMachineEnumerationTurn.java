package Controller;

/**
 * This class is used to handle the action of a player in a turn
 */
public enum StateMachineEnumerationTurn {
    START("Start"),
    ACTION1("Action1"),
    ACTION2("Action2"),
    RELOAD("Reload"),
    ENDTURN("End"),
    WAIT("Wait");

    private String abbreviation;
    private StateMachineEnumerationTurn(String abbreviation) {
        this.abbreviation=abbreviation;
    }
    public String getAbbreviation(){
        return abbreviation;
    }
}
