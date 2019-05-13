package Model;

public enum AmmoColors{
    BLUE("blue"),
    RED("red"),
    YELLOW("yellow");

    private String abbreviation;
    private AmmoColors(String abbreviation) {
        this.abbreviation=abbreviation;
    }
    public String getAbbreviation(){
        return abbreviation;
    }
}

