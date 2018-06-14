package pl.edu.wat.msk.comunication.bullet;

import hla.rti.RTIexception;
import pl.edu.wat.msk.BaseFederate;

/**
 * Created by Kamil Przyborowski
 * Wojskowa Akademia Techniczna im. Jarosława Dąbrowskiego, Warszawa 2018.
 */
public class BulletFederate extends BaseFederate<BulletAmbassador> {
    @Override
    protected void update(double time) throws Exception {

    }

    @Override
    protected void registerObjects() throws RTIexception {

    }

    @Override
    protected void publishAndSubscribe() throws RTIexception {

    }

    public static void main(String[] args) {
        try {
            new BulletFederate().runFederate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
