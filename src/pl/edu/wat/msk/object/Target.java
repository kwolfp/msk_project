package pl.edu.wat.msk.object;

import pl.edu.wat.msk.util.Vec3;

public class Target {

    private Vec3 polozenie;
    private Integer poziomUszkodzen;
    private Boolean niezdatny;

    public Vec3 getPolozenie() {
        return polozenie;
    }

    public void setPolozenie(Vec3 polozenie) {
        this.polozenie = polozenie;
    }

    public Integer getPoziomUszkodzen() {
        return poziomUszkodzen;
    }

    public void setPoziomUszkodzen(Integer poziomUszkodzen) {
        this.poziomUszkodzen = poziomUszkodzen;
    }

    public Boolean getNiezdatny() {
        return niezdatny;
    }

    public void setNiezdatny(Boolean niezdatny) {
        this.niezdatny = niezdatny;
    }
}
