package Model;

import javax.json.JsonObject;
import java.io.FileNotFoundException;

public class CardOnlyAmmo extends CardAmmo {
    private AmmoColors item1;
    private AmmoColors item2;
    private AmmoColors item3;

    public CardOnlyAmmo(String file) throws FileNotFoundException {
        JsonObject jsonValues = Utils.JsonFileHandler.openFile("OnlyAmmo", file); /* this variable contains the JsonObject created from JSON file*/
        name = jsonValues.getString("name");
        item1 = AmmoColors.valueOf(jsonValues.getString("item1"));
        item2 = AmmoColors.valueOf(jsonValues.getString("item2"));
        item3 = AmmoColors.valueOf(jsonValues.getString("item3"));
        withPowerUp = false;
    }

    public CardOnlyAmmo() {
        item1 = null;
        item2 = null;
        item3 = null;
        withPowerUp = false;
    }
    @Override
    public CardOnlyAmmo copyCardAmmo (){
        CardOnlyAmmo copy = new CardOnlyAmmo();
        copy.item1 = this.getItem1();
        copy.item2 = this.getItem2();
        copy.item3 = this.getItem3();
        return copy;
    }

    public AmmoColors getItem1() {
        return item1;
    }

    public AmmoColors getItem2() {
        return item2;
    }

    public AmmoColors getItem3() {
        return item3;
    }
}
