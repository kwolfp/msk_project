package pl.edu.wat.msk.communication.target;

import hla.rti.AttributeHandleSet;
import hla.rti.AttributeNotDefined;
import hla.rti.AttributeNotOwned;
import hla.rti.ConcurrentAccessAttempted;
import hla.rti.DeletePrivilegeNotHeld;
import hla.rti.FederateNotExecutionMember;
import hla.rti.InvalidFederationTime;
import hla.rti.NameNotFound;
import hla.rti.ObjectClassNotDefined;
import hla.rti.ObjectNotKnown;
import hla.rti.RTIexception;
import hla.rti.RTIinternalError;
import hla.rti.RestoreInProgress;
import hla.rti.SaveInProgress;
import hla.rti.SuppliedAttributes;
import hla.rti.jlc.EncodingHelpers;
import hla.rti.jlc.RtiFactoryFactory;
import pl.edu.wat.msk.BaseFederate;
import pl.edu.wat.msk.util.Vec3;

/**
 * Created by Kamil Przyborowski
 * Wojskowa Akademia Techniczna im. Jarosława Dąbrowskiego, Warszawa 2018.
 */
public class TargetFederate extends BaseFederate<TargetAmbassador> {

    private int targetObj = 0;

    @Override
    protected void update(double time) throws Exception {
        // Tu jakaś symulacja a jej wyniki można przesłać za pomocą metod
//        updateTargetObj_Polozenie(new Vec3(2, 2,4), time);
//        updateTargetObj_PoziomUszkodzen(1337, time);
//        updateTargetObj_Niezdatny(false, time);
    }

    // =======================================================================================

    @Override
    protected void init() throws Exception {
        this.targetObj = createObj("Cel");
    }

    @Override
    protected void publishAndSubscribe() throws RTIexception {
        int classHandle = rtiAmbassador.getObjectClassHandle("ObjectRoot.Cel");
        int polozenieHandle = rtiAmbassador.getAttributeHandle( "Polozenie", classHandle );
        int poziomUszkodzenHandle = rtiAmbassador.getAttributeHandle( "PoziomUszkodzen", classHandle );
        int niezdatnyHandle = rtiAmbassador.getAttributeHandle( "Niezdatny", classHandle );

        AttributeHandleSet attributes = RtiFactoryFactory.getRtiFactory().createAttributeHandleSet();
        attributes.add( polozenieHandle );
        attributes.add( poziomUszkodzenHandle );
        attributes.add( niezdatnyHandle );

        rtiAmbassador.publishObjectClass(classHandle, attributes);
    }

    @Override
    protected void deleteObjectsAndInteractions() throws Exception {
        this.removeObj(this.targetObj);
    }

    private void updateTargetObj_Polozenie(Vec3 vec3, double t) throws ObjectNotKnown, FederateNotExecutionMember, RTIinternalError, NameNotFound, ObjectClassNotDefined, RestoreInProgress, AttributeNotOwned, AttributeNotDefined, SaveInProgress, InvalidFederationTime, ConcurrentAccessAttempted {
        SuppliedAttributes attributes = RtiFactoryFactory.getRtiFactory().createSuppliedAttributes();
        int classHandle = rtiAmbassador.getObjectClass(this.targetObj);
        int attrHandle = rtiAmbassador.getAttributeHandle("Polozenie", classHandle);
        attributes.add(attrHandle, vec3.getBytes());
        rtiAmbassador.updateAttributeValues(this.targetObj, attributes, "actualize cel".getBytes(), convertTime(t));
    }

    private void updateTargetObj_PoziomUszkodzen(int lvl, double t) throws ObjectNotKnown, FederateNotExecutionMember, RTIinternalError, NameNotFound, ObjectClassNotDefined, RestoreInProgress, AttributeNotOwned, AttributeNotDefined, SaveInProgress, InvalidFederationTime, ConcurrentAccessAttempted {
        SuppliedAttributes attributes = RtiFactoryFactory.getRtiFactory().createSuppliedAttributes();
        int classHandle = rtiAmbassador.getObjectClass(this.targetObj);
        int attrHandle = rtiAmbassador.getAttributeHandle( "PoziomUszkodzen", classHandle );
        attributes.add(attrHandle, EncodingHelpers.encodeInt(lvl));
        rtiAmbassador.updateAttributeValues(this.targetObj, attributes, "actualize cel".getBytes(), convertTime(t));
    }

    private void updateTargetObj_Niezdatny(boolean nzd, double t) throws ObjectNotKnown, FederateNotExecutionMember, RTIinternalError, NameNotFound, ObjectClassNotDefined, RestoreInProgress, AttributeNotOwned, AttributeNotDefined, SaveInProgress, InvalidFederationTime, ConcurrentAccessAttempted {
        SuppliedAttributes attributes = RtiFactoryFactory.getRtiFactory().createSuppliedAttributes();
        int classHandle = rtiAmbassador.getObjectClass(this.targetObj);
        int attrHandle = rtiAmbassador.getAttributeHandle( "Niezdatny", classHandle );
        attributes.add(attrHandle, EncodingHelpers.encodeBoolean(nzd));
        rtiAmbassador.updateAttributeValues(this.targetObj, attributes, "actualize cel".getBytes(), convertTime(t));
    }


    public static void main(String[] args) {
        try {
            new TargetFederate().runFederate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
