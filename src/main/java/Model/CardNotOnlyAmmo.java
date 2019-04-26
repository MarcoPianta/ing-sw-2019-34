package Model;

import javax.json.JsonObject;
import java.io.FileNotFoundException;

public class CardNotOnlyAmmo extends CardAmmo{
    private CardPowerUp item1;
    private AmmoColors item2;
    private AmmoColors item3;

    public CardNotOnlyAmmo(String file) throws FileNotFoundException {
        JsonObject jsonValues = Utils.JsonFileHandler.openFile("NotOnlyAmmo", file); /* this variable contains the JsonObject created from JSON file*/
        //item1 = ;
        item2 = AmmoColors.valueOf(jsonValues.getString("item2"));
        item3 = AmmoColors.valueOf(jsonValues.getString("item3"));
    }

    public CardNotOnlyAmmo() {
        item1 = null;
        item2 = null;
        item3 = null;
    }
    @Override
    public CardNotOnlyAmmo copyCardAmmo (){
        CardNotOnlyAmmo copy = new CardNotOnlyAmmo();
        copy.item1 = this.getItem1();
        copy.item2 = this.getItem2();
        copy.item3 = this.getItem3();
        return copy;
    }

    public CardPowerUp getItem1() {
        return item1;
    }

    public AmmoColors getItem2() {
        return item2;
    }

    public AmmoColors getItem3() {
        return item3;
    }
}
