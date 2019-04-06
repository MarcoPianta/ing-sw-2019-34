package Model;

public class CardOnlyAmmo extends CardAmmo {
    private Colors item1;
    private Colors item2;
    private Colors item3;

    @Override
    public CardOnlyAmmo copyCardAmmo (){
        CardOnlyAmmo copy = new CardOnlyAmmo();
        copy.item1 = this.getItem1();
        copy.item2 = this.getItem2();
        copy.item3 = this.getItem3();
        return copy;
    }

    public Colors getItem1() {
        return item1;
    }

    public Colors getItem2() {
        return item2;
    }

    public Colors getItem3() {
        return item3;
    }
}
