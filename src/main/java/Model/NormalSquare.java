package Model;

public class NormalSquare extends Square {
    private CardAmmo ammo;

    public NormalSquare(Square SideN, Square SideE, Square SideS, Square SideW, CardAmmo Ammo) {
        N = SideN;
        E = SideE;
        S = SideS;
        W = SideW;
        ammo = Ammo;
    }

    private void setAmmo(CardAmmo ammo) {
        this.ammo = ammo;
    }

    public void addAmmo(CardAmmo ammo) {
        this.setAmmo(ammo);
    }

    public CardAmmo getItem() {
        if (this.ammo==null)
            return null;
        CardAmmo Ammo = this.ammo.copyCardAmmo(); // testare se lo legge come padre o come figlio
        return Ammo;
    }

    private CardAmmo removeAmmo(){
        if (this.getItem() == null)
            return null;
        CardAmmo remove = this.getItem();
        this.setAmmo(null);
        return remove;
    }
    @Override
    public CardAmmo grabItem(int position) {
        CardAmmo grabbed = this.removeAmmo();
        return grabbed;
    }
}
