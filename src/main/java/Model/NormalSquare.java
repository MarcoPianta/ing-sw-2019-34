package Model;

public class NormalSquare {
    private CardAmmo ammo;
    protected NormalSquare N;
    protected NormalSquare E;
    protected NormalSquare S;
    protected NormalSquare W;
    protected Colors color;
    protected boolean spawn;

    public NormalSquare(NormalSquare SideN, NormalSquare SideE, NormalSquare SideS, NormalSquare SideW, Colors color, CardAmmo Ammo) {
        N = SideN;
        E = SideE;
        S = SideS;
        W = SideW;
        ammo = Ammo;
        this.color = color;
        spawn = false;
    }

    public NormalSquare(){
        N = null;
        E = null;
        S = null;
        W = null;
        ammo = null;
        this.color = null;
    }

    public void addAmmo(CardAmmo ammo) {
        this.ammo = ammo;
    }

    /**
     * This method is used to get the ammo on the NormalSquare(not to grab it)
     * */
    public CardAmmo getItem() {
        if (this.ammo == null)
            return null;
        return ammo.copyCardAmmo();
    }

    private CardAmmo removeAmmo(){
        if (this.getItem() == null)
            return null;
        CardAmmo card = ammo;
        addAmmo(null);
        return card;
    }

    /**
     * This method Override the method of NormalSquare, it allows a BoardPlayer to grab one of the Item content by the NormalSquare
     * */
    public CardAmmo grabItem() {
        return this.removeAmmo();
    }

    public boolean isSpawn() {
        return spawn;
    }

    public NormalSquare getN() {
        return N;
    }

    public NormalSquare getE() {
        return E;
    }

    public NormalSquare getS() {
        return S;
    }

    public NormalSquare getW() {
        return W;
    }

    public Colors getColor() {
        return color;
    }
}
