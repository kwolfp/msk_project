package pl.edu.wat.msk.object;

import pl.edu.wat.msk.util.Vec3;

/**
 * Created by Kamil Przyborowski
 * Wojskowa Akademia Techniczna im. Jarosława Dąbrowskiego, Warszawa 2018.
 */
public class DefenseSystemBullet extends BaseObject {
    private Vec3 polozenie;
    private boolean wRuchu;

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
