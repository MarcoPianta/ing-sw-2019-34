package Model;

public abstract class CardAmmo implements Card {
    public CardAmmo copyCardAmmo() {
        return null;
    }
    public Enum[] getEnumeration(){
        return AmmoEnum.values();
    }

    @Override
    public String getName() {
        return null;
    }
}
