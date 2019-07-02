package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NormalSquare implements Serializable {
    private CardAmmo ammo;
    NormalSquare n;
    NormalSquare e;
    NormalSquare s;
    NormalSquare w;
    protected Colors color;
    boolean spawn;
    private String id;

    public NormalSquare(NormalSquare sideN, NormalSquare sideE, NormalSquare sideS, NormalSquare sideW, Colors color, CardAmmo ammo) {
        n = sideN;
        e = sideE;
        s = sideS;
        w = sideW;
        this.ammo = ammo;
        this.color = color;
        spawn = false;
    }

    public NormalSquare(Colors color){
        n = null;
        e = null;
        s = null;
        w = null;
        ammo = null;
        this.color = color;
    }

    public NormalSquare(){
        n = null;
        e = null;
        s = null;
        w = null;
        ammo = null;
        this.color = null;
    }

    public int setItems(Card card) {
        this.ammo = (CardAmmo) card;
        return 0;
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
        setItems(null);
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
        return n;
    }

    public NormalSquare getE() {
        return e;
    }

    public NormalSquare getS() {
        return s;
    }

    public NormalSquare getW() {
        return w;
    }

    public Colors getColor() {
        return color;
    }

    public List<CardWeapon> getWeapons() {
        return new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setN(NormalSquare n) {
        this.n = n;
    }

    public void setE(NormalSquare e) {
        this.e = e;
    }

    public void setS(NormalSquare s) {
        this.s = s;
    }

    public void setW(NormalSquare w) {
        this.w = w;
    }

    public void setId(String id) {
        this.id = id;
    }
}
