package Model;

public enum Colors {
    BLUE("B"),
    GREEN("GN"),
    GREY("GY"),
    YELLOW("Y"),
    VIOLET("V");

    private String abbreviation;
    private Colors(String abbreviation) {
        this.abbreviation=abbreviation;
    }
    public String getAbbreviation(){
        return abbreviation;
    }
}
