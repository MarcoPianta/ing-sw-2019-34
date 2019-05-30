package Model;

/**
 * This enumeration lists the color used in the game (for example for players or squares)
 */
public enum Colors {
    BLUE("blue"),
    GREEN("green"),
    WHITE("white"),
    YELLOW("yellow"),
    VIOLET("violet"),
    RED("red"),
    NULL("null"); //This value is used only from view to indicates that there is no color set

    private String abbreviation;
    private Colors(String abbreviation) {
        this.abbreviation=abbreviation;
    }
    public String getAbbreviation(){
        return abbreviation;
    }
}
