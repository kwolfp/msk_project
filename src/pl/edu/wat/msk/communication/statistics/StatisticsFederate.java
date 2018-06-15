package pl.edu.wat.msk.communication.statistics;

import hla.rti.RTIexception;
import pl.edu.wat.msk.BaseFederate;

/**
 * Created by Kamil Przyborowski
 * Wojskowa Akademia Techniczna im. Jarosława Dąbrowskiego, Warszawa 2018.
 */
public class StatisticsFederate extends BaseFederate<StatisticsAmbassador> {
    @Override
    protected void update(double time) throws Exception {

    }

    @Override
    protected void publishAndSubscribe() throws RTIexception {

    }

    @Override
    protected void deleteObjectsAndInteractions() {

    }

    public static void main(String[] args) {
        try {
            new StatisticsFederate().runFederate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
