package Model;

public enum WeaponDictionary {
    CYBERBLADE("cyberblade.json"),
    ELECTROSCYTE("electroscyte.json"),
    FLAMETHROWER("flamethrower.json"),
    FURNACE("furnace.json"),
    GRENADELAUNCHER("grenadeLauncher.json"),
    HEATSEEKER("heatseeker.json"),
    HELLION("hellion.json"),
    LOCKRIFLE("lockRifle.json"),
    MACHINEGUN("machineGun.json"),
    PLASMAGUN("plasmaGun.json"),
    POWER_GLOVE("power_glove.json"),
    RAIL_GUN("rail_gun.json"),
    ROCKETLAUNCHER("rocketLauncher.json"),
    SHOKWAVE("shokwave.json"),
    SHOTGUN("shotgun.json"),
    SLEDGEHAMMER("sledgehammer.json"),
    THOR("thor.json"),
    TRACTORBEAM("tractorBeam.json"),
    VORTEXCANNON("vortexCannon.json"),
    WHISPER("whisper.json"),
    ZX_2("ZX-2.json");

    private String abbreviation;
    private WeaponDictionary(String abbreviation) {
        this.abbreviation=abbreviation;
    }
    public String getAbbreviation(){
        return abbreviation;
    }
}