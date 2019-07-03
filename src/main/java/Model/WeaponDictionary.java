package Model;

import java.io.Serializable;

/**
 * This enumeration contains the name's list of all weapon cards
 * */
public enum WeaponDictionary implements Serializable {
    CYBERBLADE("cyberblade"),
    ELECTROSCYTE("electroscyte"),
    FLAMETHROWER("flamethrower"),
    FURNACE("furnace"),
    GRENADELAUNCHER("grenadeLauncher"),
    HEATSEEKER("heatseeker"),
    HELLION("hellion"),
    LOCKRIFLE("lockRifle"),
    MACHINEGUN("machineGun"),
    PLASMAGUN("plasmaGun"),
    POWERGLOVE("powerGlove"),
    RAILGUN("railGun"),
    ROCKETLAUNCHER("rocketLauncher"),
    SHOCKWAVE("shockwave"),
    SHOTGUN("shotgun"),
    SLEDGEHAMMER("sledgehammer"),
    THOR("thor"),
    TRACTORBEAM("tractorBeam"),
    WHISPER("whisper"),
    ZX_2("ZX-2"),
    VORTEXCANNON("vortexCannon");

    private String abbreviation;
    private WeaponDictionary(String abbreviation) {
        this.abbreviation=abbreviation;
    }
    public String getAbbreviation(){
        return abbreviation;
    }
}