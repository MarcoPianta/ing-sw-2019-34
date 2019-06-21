package Model;

public enum AmmoColors{
    BLUE("BLUE"),
    YELLOW("YELLOW"),
    RED("RED");

    private String abbreviation;

    AmmoColors(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getAbbreviation(){
        return abbreviation;
    }
}

