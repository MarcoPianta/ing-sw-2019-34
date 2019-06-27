package network.messages;

public enum ActionType {

    START("Start"),
    CONNECTIONREQUEST("Connection request"),
    CONNECTIONRESPONSE("Connection response"),
    RECONNECTIONREQUEST("Reconnection request"),
    RECONNECTIONRESPONSE("Reconnection response"),
    POSSIBLETARGETSHOT("Possible target shot"),
    SHOT("Shot"),
    POSSIBLEMOVE("Possible move"),
    MOVE("Move"),
    RELOAD("Reload"),
    RELOADED("Reloaded"),
    GRABWEAPON("Grab weapon"),
    GRABWEAPONRESPONSE("Grab weapon response"),
    GRABAMMO("Grab ammo"),
    GRABNOTONLYAMMO("Grab notOnlyAmmo"),
    GRABRESPONSE("Grab response"),
    USEPOWERUP("Use powerup"),
    UPDATECLIENTS("Update clients"),
    PASS("Pass"),
    TIMEOUT("Time out"),
    GAMESETTINGSREQUEST("Game settings request"),
    GAMESETTINGSRESPONSE("Game settings response"),
    STARTTURNMESSAGE("Start turn message"),
    SUBSTITUTEWEAPON("Substitute weapon"),
    SUBSTITUTEWEAPONRESPONSE("Substitute weapon response"),
    CANUSEVENOM("Can use venom"),
    CANUSESCOOP("Can use scoop"),
    RECEIVETARGETSQUARE("Receive target square"),
    USEPOWERUPRESPONSE("Use power up response"),
    RESPAWN("Respwan"),
    WINNER("Winner"),
    END("End"),
    SHOTRESPONSE("Shot response"),
    MOVERESPONSE("Move response"),
    GRABWEAPONREQUEST("Grab weapon request"),
    DISCONNECT("Disconnect"),
    PAYMENT("payment"),
    PAYMENTRESPONSE("Payment Response");


    private String abbreviation;
    private ActionType(String abbreviation) {
        this.abbreviation=abbreviation;
    }
    public String getAbbreviation(){
        return abbreviation;
    }
}
