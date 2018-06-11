package pl.edu.wat.msk.target;

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
public class TargetFederate extends BaseFederate<TargetAmbassador> {

    protected int celObjHandle = 0;

    @Override
    protected void update() throws Exception {
        double timeToAdvance = fedamb.federateTime + timeStep;
        advanceTime(timeToAdvance);

        if(fedamb.getGrantedTime() == timeToAdvance) {
            timeToAdvance += fedamb.federateLookahead;
            log("Updating celObj at time: " + timeToAdvance);

            SuppliedAttributes attributes = RtiFactoryFactory.getRtiFactory().createSuppliedAttributes();

            int classHandle = rtiamb.getObjectClass(celObjHandle);
            int stockHandle = rtiamb.getAttributeHandle( "Polozenie", classHandle );
            byte[] stockValue = EncodingHelpers.encodeInt(123);

            attributes.add(stockHandle, stockValue);
            LogicalTime logicalTime = convertTime( timeToAdvance );
            rtiamb.updateAttributeValues( celObjHandle, attributes, "actualize cel".getBytes(), logicalTime );

            fedamb.federateTime = timeToAdvance;
        }
    }

    @Override
    protected void registerObjects() throws RTIexception {
        int classHandle = rtiamb.getObjectClassHandle("ObjectRoot.Cel");
        this.celObjHandle = rtiamb.registerObjectInstance(classHandle);
    }

    @Override
    protected void publishAndSubscribe() throws RTIexception {
        int classHandle = rtiamb.getObjectClassHandle("ObjectRoot.Cel");
        int polozenieHandle = rtiamb.getAttributeHandle( "Polozenie", classHandle );
        int poziomUszkodzenHandle = rtiamb.getAttributeHandle( "PoziomUszkodzen", classHandle );
        int niezdatnyHandle = rtiamb.getAttributeHandle( "Niezdatny", classHandle );

        AttributeHandleSet attributes = RtiFactoryFactory.getRtiFactory().createAttributeHandleSet();
        attributes.add( polozenieHandle );
        attributes.add( poziomUszkodzenHandle );
        attributes.add( niezdatnyHandle );

        rtiamb.publishObjectClass(classHandle, attributes);
    }

    @Override
    protected String getName() {
        return "Target";
    }

    public static void main(String[] args) {
        try {
            new TargetFederate().runFederate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
