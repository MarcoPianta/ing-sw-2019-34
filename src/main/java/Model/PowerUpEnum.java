package Model;

import java.io.Serializable;

/**
 * This enumeration contains the name's list of all weapon cards
 * */
public enum PowerUpEnum implements Serializable {
    NEWTON_B            ("newton_B"),
    NEWTON_R            ("newton_R"),
    NEWTON_Y            ("newton_Y"),
    TAGBACKGRANADE_B    ("tagbackGranade_B"),
    TAGBACKGRANADE_R    ("tagbackGranade_R"),
    TAGBACKGRANADE_Y    ("tagbackGranade_Y"),
    TARGETTINGSCOPE_B   ("targettingScope_B"),
    TARGETTINGSCOPE_R   ("targettingScope_R"),
    TARGETTINGSCOPE_Y   ("targettingScope_Y"),
    TELEPORTER_B        ("teleporter_B"),
    TELEPORTER_R        ("teleporter_R"),
    TELEPORTER_Y        ("teleporter_Y");

    private String abbreviation;
    private PowerUpEnum(String abbreviation) {
        this.abbreviation=abbreviation;
    }
    public String getAbbreviation(){
        return abbreviation;
    }
}
