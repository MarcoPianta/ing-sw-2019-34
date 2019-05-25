package Model;

public enum Colors {
    BLUE("blue"),
    GREEN("green"),
    WHITE("white"),
    YELLOW("yellow"),
    VIOLET("violet"),
    RED("red");

    private String abbreviation;
    private Colors(String abbreviation) {
        this.abbreviation=abbreviation;
    }
    public String getAbbreviation(){
        return abbreviation;
    }
}
