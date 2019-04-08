package Model;

public class CardPowerUp implements Card {
    private String name;
    private Action bonusEffect;
    private AmmoColors color;
    public Action getBonusEffect() {
        return bonusEffect;
    }

    public String getName() {
        return name;
    }
}
