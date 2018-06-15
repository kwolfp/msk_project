package pl.edu.wat.msk.object;

import pl.edu.wat.msk.util.Vec3;

/**
 * Created by Kamil Przyborowski
 * Wojskowa Akademia Techniczna im. Jarosława Dąbrowskiego, Warszawa 2018.
 */
public class Bullet extends BaseObject {
    private String rodzaj;
    private Vec3 wielkosc;
    private Vec3 polozenie;
    private boolean wRuchu;

    public String getRodzaj() {
        return rodzaj;
    }

    public void setRodzaj(String rodzaj) {
        this.rodzaj = rodzaj;
    }

    public Vec3 getWielkosc() {
        return wielkosc;
    }

    public void setWielkosc(Vec3 wielkosc) {
        this.wielkosc = wielkosc;
    }

    public Vec3 getPolozenie() {
        return polozenie;
    }

    public void setPolozenie(Vec3 polozenie) {
        this.polozenie = polozenie;
    }

    public boolean iswRuchu() {
        return wRuchu;
    }

    public void setwRuchu(boolean wRuchu) {
        this.wRuchu = wRuchu;
    }
}
