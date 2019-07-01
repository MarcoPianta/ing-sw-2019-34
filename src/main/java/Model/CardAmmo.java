package Model;

public abstract class CardAmmo extends Card {
    protected boolean withPowerUp;
    protected String name;

    public abstract CardAmmo copyCardAmmo();

    @Override
    public String getName(){
        return name;
    }

    public boolean isWithPowerUp() {
        return withPowerUp;
    }
}
