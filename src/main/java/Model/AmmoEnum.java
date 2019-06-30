package Model;

import java.io.Serializable;

/**
 * This enumeration is used to list the name that other classes use to refer to ammo cards
 * */
public enum AmmoEnum implements Serializable {
    AMMO1("A1"),
    AMMO2("A2"),
    AMMO3("A3"),
    AMMO4("A4"),
    AMMO5("A5"),
    AMMO6("A6"),
    AMMO7("A7"),
    AMMO8("A8"),
    AMMO9("A9"),
    AMMO10("A10"),
    AMMO11("A11"),
    AMMO12("A12"),
    AMMO13("A13"),
    AMMO14("A14"),
    AMMO15("A15"),
    AMMO16("A16"),
    AMMO17("A17"),
    AMMO18("A18");

    private String abbreviation;
    private AmmoEnum(String abbreviation) {
        this.abbreviation=abbreviation;
    }
    public String getAbbreviation(){
        return abbreviation;
    }
}
