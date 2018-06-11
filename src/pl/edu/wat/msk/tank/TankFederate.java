package pl.edu.wat.msk.tank;

import hla.rti.AttributeHandleSet;
import hla.rti.LogicalTime;
import hla.rti.RTIexception;
import hla.rti.SuppliedAttributes;
import hla.rti.jlc.EncodingHelpers;
import hla.rti.jlc.RtiFactoryFactory;
import pl.edu.wat.msk.BaseFederate;

/**
 * Created by Kamil Przyborowski
 * Wojskowa Akademia Techniczna im. Jarosława Dąbrowskiego, Warszawa 2018.
 */
public class TankFederate extends BaseFederate<TankAmbassador> {

    private int czolObjHandle = 0;


    @Override
    protected void update() throws Exception {
        double timeToAdvance = fedamb.federateTime + timeStep;
        advanceTime(timeToAdvance);

        if(fedamb.getGrantedTime() == timeToAdvance) {
            timeToAdvance += fedamb.federateLookahead;
            log("Updating czolObj at time: " + timeToAdvance);

            SuppliedAttributes attributes = RtiFactoryFactory.getRtiFactory().createSuppliedAttributes();

            int classHandle = rtiamb.getObjectClass(czolObjHandle);
            int stockHandle = rtiamb.getAttributeHandle( "Polozenie", classHandle );
            byte[] stockValue = EncodingHelpers.encodeInt(123);

            attributes.add(stockHandle, stockValue);
            LogicalTime logicalTime = convertTime( timeToAdvance );
            rtiamb.updateAttributeValues( czolObjHandle, attributes, "actualize stock".getBytes(), logicalTime );

            fedamb.federateTime = timeToAdvance;
        }
    }

    protected void sendInteraction(double timeStep) throws RTIexception {

    }

    @Override
    protected void registerObjects() throws RTIexception {
        int classHandle = rtiamb.getObjectClassHandle("ObjectRoot.Czolg");
        this.czolObjHandle = rtiamb.registerObjectInstance(classHandle);
    }

    @Override
    protected void publishAndSubscribe() throws RTIexception {
        // rejestracj obj Czolg
        int classHandle = rtiamb.getObjectClassHandle("ObjectRoot.Czolg");
        int rodzajHandle = rtiamb.getAttributeHandle( "Rodzaj", classHandle );
        int wielkoscHandle = rtiamb.getAttributeHandle( "Wielkosc", classHandle );
        int polozenieHandle = rtiamb.getAttributeHandle( "Polozenie", classHandle );
        int wWRuchuHandle = rtiamb.getAttributeHandle( "WRuchu", classHandle );
        int wystrzeleniePociskuHandle = rtiamb.getAttributeHandle( "WystrzeleniePocisku", classHandle );

        AttributeHandleSet attributes = RtiFactoryFactory.getRtiFactory().createAttributeHandleSet();
        attributes.add( rodzajHandle );
        attributes.add( wielkoscHandle );
        attributes.add( polozenieHandle );
        attributes.add( wWRuchuHandle );
        attributes.add( wystrzeleniePociskuHandle );

        rtiamb.publishObjectClass(classHandle, attributes);

        // subskrypcja obj Cel
        int celHandle = rtiamb.getObjectClassHandle("ObjectRoot.Cel");
        int polozenieHandle2 = rtiamb.getAttributeHandle( "Polozenie", celHandle );
        int poziomUszkodzenHandle2 = rtiamb.getAttributeHandle( "PoziomUszkodzen", celHandle );
        int niezdatnyHandle2 = rtiamb.getAttributeHandle( "Niezdatny", celHandle );

        AttributeHandleSet attributes2 = RtiFactoryFactory.getRtiFactory().createAttributeHandleSet();
        attributes2.add( polozenieHandle2 );
        attributes2.add( poziomUszkodzenHandle2 );
        attributes2.add( niezdatnyHandle2 );

        rtiamb.subscribeObjectClassAttributes(celHandle, attributes2);
    }

    @Override
    public String getName() {
        return "Tank";
    }

    public static void main(String[] args) {
        try {
            new TankFederate().runFederate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
