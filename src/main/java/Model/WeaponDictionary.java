package Model;

/**
 * This enumeration contains the name's list of all weapon cards
 * */
public enum WeaponDictionary {
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
    POWER_GLOVE("power_glove"),
    RAIL_GUN("rail_gun"),
    ROCKETLAUNCHER("rocketLauncher"),
    SHOKWAVE("shokwave"),
    SHOTGUN("shotgun"),
    SLEDGEHAMMER("sledgehammer"),
    THOR("thor"),
    TRACTORBEAM("tractorBeam"),
    VORTEXCANNON("vortexCannon"),
    WHISPER("whisper"),
    ZX_2("ZX-2");

    private String abbreviation;
    private WeaponDictionary(String abbreviation) {
        this.abbreviation=abbreviation;
    }
    public String getAbbreviation(){
        return abbreviation;
    }
}