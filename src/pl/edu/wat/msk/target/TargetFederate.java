package pl.edu.wat.msk.target;

import hla.rti.*;
import hla.rti.jlc.EncodingHelpers;
import hla.rti.jlc.RtiFactoryFactory;
import pl.edu.wat.msk.BaseFederate;
import pl.edu.wat.msk.util.Vec3;

/**
 * Created by Kamil Przyborowski
 * Wojskowa Akademia Techniczna im. Jarosława Dąbrowskiego, Warszawa 2018.
 */
public class TargetFederate extends BaseFederate<TargetAmbassador> {

    protected int celObjHandle = 0;


    @Override
    protected void update(double time) throws Exception {
        log("Updating celObj at time: " + time);

        updatePos(new Vec3(2, 2,4), time);
        updatePoziomUszkodzen(1337, time);
        updateNiezdatny(false, time);
    }

    private void updatePos(Vec3 vec3, double t) throws ObjectNotKnown, FederateNotExecutionMember, RTIinternalError, NameNotFound, ObjectClassNotDefined, RestoreInProgress, AttributeNotOwned, AttributeNotDefined, SaveInProgress, InvalidFederationTime, ConcurrentAccessAttempted {
        if(vec3 == null) {
            return;
        }

        SuppliedAttributes attributes = RtiFactoryFactory.getRtiFactory().createSuppliedAttributes();

        int classHandle = rtiamb.getObjectClass(celObjHandle);
        int stockHandle = rtiamb.getAttributeHandle( "Polozenie", classHandle );
        byte[] posValue = vec3.getBytes();

        attributes.add(stockHandle, posValue);
        LogicalTime logicalTime = convertTime( t );
        rtiamb.updateAttributeValues( celObjHandle, attributes, "actualize cel".getBytes(), logicalTime );
    }

    private void updatePoziomUszkodzen(int lvl, double t) throws ObjectNotKnown, FederateNotExecutionMember, RTIinternalError, NameNotFound, ObjectClassNotDefined, RestoreInProgress, AttributeNotOwned, AttributeNotDefined, SaveInProgress, InvalidFederationTime, ConcurrentAccessAttempted {
        SuppliedAttributes attributes = RtiFactoryFactory.getRtiFactory().createSuppliedAttributes();

        int classHandle = rtiamb.getObjectClass(celObjHandle);
        int lvlHandle = rtiamb.getAttributeHandle( "PoziomUszkodzen", classHandle );
        byte[] lvlValue = EncodingHelpers.encodeInt(lvl);

        attributes.add(lvlHandle, lvlValue);
        LogicalTime logicalTime = convertTime( t );
        rtiamb.updateAttributeValues( celObjHandle, attributes, "actualize cel".getBytes(), logicalTime );
    }

    private void updateNiezdatny(boolean nzd, double t) throws ObjectNotKnown, FederateNotExecutionMember, RTIinternalError, NameNotFound, ObjectClassNotDefined, RestoreInProgress, AttributeNotOwned, AttributeNotDefined, SaveInProgress, InvalidFederationTime, ConcurrentAccessAttempted {
        SuppliedAttributes attributes = RtiFactoryFactory.getRtiFactory().createSuppliedAttributes();

        int classHandle = rtiamb.getObjectClass(celObjHandle);
        int nzdHandle = rtiamb.getAttributeHandle( "Niezdatny", classHandle );
        byte[] nzdValue = EncodingHelpers.encodeBoolean(nzd);

        attributes.add(nzdHandle, nzdValue);
        LogicalTime logicalTime = convertTime( t );
        rtiamb.updateAttributeValues( celObjHandle, attributes, "actualize cel".getBytes(), logicalTime );
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
