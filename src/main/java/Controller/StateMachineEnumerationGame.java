package Controller;

public enum StateMachineEnumerationGame {
    START("Start");

    private String abbreviation;
    private StateMachineEnumerationGame(String abbreviation) {
        this.abbreviation=abbreviation;
    }
    public String getAbbreviation(){
        return abbreviation;
    }
}
