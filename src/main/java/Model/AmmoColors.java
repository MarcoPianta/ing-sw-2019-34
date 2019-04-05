package Model;

public enum AmmoColors {
    BLUE("B"),
    RED("R"),
    YELLOW("Y");

    private String abbreviation;
    private AmmoColors(String abbreviation) {
        this.abbreviation=abbreviation;
    }
    public String getAbbreviation(){
        return abbreviation;
    }
}

