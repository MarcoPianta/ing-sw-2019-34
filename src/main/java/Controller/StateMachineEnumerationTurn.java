package Controller;

public enum StateMachineEnumerationTurn {
    START("Start"),
    ACTIONMOVE("Move"),
    ACTIONGRAB("Grab"),
    ACTIONSHOT("Shot"),
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
