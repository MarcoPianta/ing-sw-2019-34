package Model;

public abstract class CardAmmo implements Card {
    protected boolean withPowerUp;
    public CardAmmo copyCardAmmo() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    public boolean isWithPowerUp() {
        return withPowerUp;
    }
}
