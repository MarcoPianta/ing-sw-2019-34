package Model;

public enum Colors {
    BLUE("blue"),
    GREEN("green"),
    WHITE("white"),
    YELLOW("yellow"),
    VIOLET("purple"),
    RED("red");

    private String abbreviation;
    private Colors(String abbreviation) {
        this.abbreviation=abbreviation;
    }
    public String getAbbreviation(){
        return abbreviation;
    }
}
