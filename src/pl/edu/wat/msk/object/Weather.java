package pl.edu.wat.msk.object;

import pl.edu.wat.msk.util.Vec3;

/**
 * Created by Kamil Przyborowski
 * Wojskowa Akademia Techniczna im. Jarosława Dąbrowskiego, Warszawa 2018.
 */
public class Weather extends BaseObject {
    private int wielkoscOpadow;
    private int silaWiatru;
    private Vec3 kierunekWiatru;

    public int getWielkoscOpadow() {
        return wielkoscOpadow;
    }

    public void setWielkoscOpadow(int wielkoscOpadow) {
        this.wielkoscOpadow = wielkoscOpadow;
    }

    public int getSilaWiatru() {
        return silaWiatru;
    }

    public void setSilaWiatru(int silaWiatru) {
        this.silaWiatru = silaWiatru;
    }

    public Vec3 getKierunekWiatru() {
        return kierunekWiatru;
    }

    public void setKierunekWiatru(Vec3 kierunekWiatru) {
        this.kierunekWiatru = kierunekWiatru;
    }
}
