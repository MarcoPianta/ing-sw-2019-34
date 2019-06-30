package Model;

import java.io.Serializable;

public enum AmmoColors implements Serializable {
    BLUE("blue"),
    YELLOW("yellow"),
    RED("red");

    private String abbreviation;

    AmmoColors(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getAbbreviation(){
        return abbreviation;
    }
}

