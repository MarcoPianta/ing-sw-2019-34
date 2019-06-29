package Model;

import java.io.Serializable;

public abstract class CardAmmo extends Card {
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
