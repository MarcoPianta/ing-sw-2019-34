package Model;

import java.io.Serializable;

public enum AmmoColors implements Serializable {
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

