package pl.edu.wat.msk.communication.tank;

import hla.rti.AttributeHandleSet;
import hla.rti.AttributeNotDefined;
import hla.rti.AttributeNotOwned;
import hla.rti.ConcurrentAccessAttempted;
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
import pl.edu.wat.msk.object.Target;
import pl.edu.wat.msk.util.Vec3;

/**
 * Created by Kamil Przyborowski
 * Wojskowa Akademia Techniczna im. Jarosława Dąbrowskiego, Warszawa 2018.
 */
public class TankFederate extends BaseFederate<TankAmbassador> {

    private int tankObj = 0;


    @Override
    protected void update(double time) throws Exception {
        if(this.federationAmbassador.targetClassFlag_newInstance) {
            // pojawił się nowy obiekty typu Cel
            Target target = this.federationAmbassador.getObjectInstance(Target.class);
            this.federationAmbassador.targetClassFlag_newInstance = false;
        }

        if(this.federationAmbassador.targetClassFlag_attrsUpdated) {
            // zaktualizowano atrybut obiektu Cel
            Target target = this.federationAmbassador.getObjectInstance(Target.class);
            this.federationAmbassador.targetClassFlag_attrsUpdated = false;
        }

        // Tu jakaś symulacja a jej wyniki można przesłać za pomocą metod
//        updateTankObj_Polozenie(new Vec3(1,1,1), time);
//        updateTankObj_Rodzaj("af", time);
//        updateTankObj_Wielkosc(new Vec3(1, 2, 3), time);
//        updateTankObj_WRuchu(true, time);
//        updateTankObj_WystrzeleniePocisku(false, time);
    }

    // ====================================================================================================

    @Override
    protected void init() throws Exception {
        this.tankObj = createObj("Czolg");
    }

    @Override
    protected void publishAndSubscribe() throws RTIexception {
        // rejestracj obj Czolg
        int tankClass                        = rtiAmbassador.getObjectClassHandle("ObjectRoot.Czolg");
        int tankClassAttrRodzaj              = rtiAmbassador.getAttributeHandle( "Rodzaj", tankClass );
        int tankClassAttrWielkosc            = rtiAmbassador.getAttributeHandle( "Wielkosc", tankClass );
        int tankClassAttrPolozenie           = rtiAmbassador.getAttributeHandle( "Polozenie", tankClass );
        int tankClassAttrWRuchu              = rtiAmbassador.getAttributeHandle( "WRuchu", tankClass );
        int tankClassAttrWystrzeleniePocisku = rtiAmbassador.getAttributeHandle( "WystrzeleniePocisku", tankClass );

        AttributeHandleSet attributes = RtiFactoryFactory.getRtiFactory().createAttributeHandleSet();
        attributes.add( tankClassAttrRodzaj );
        attributes.add( tankClassAttrWielkosc );
        attributes.add( tankClassAttrPolozenie );
        attributes.add( tankClassAttrWRuchu );
        attributes.add( tankClassAttrWystrzeleniePocisku );

        rtiAmbassador.publishObjectClass(tankClass, attributes);


        // subskrypcja obj Cel
        this.federationAmbassador.targetClass = rtiAmbassador.getObjectClassHandle("ObjectRoot.Cel");
        this.federationAmbassador.targetAttrPolozenie = rtiAmbassador.getAttributeHandle( "Polozenie", this.federationAmbassador.targetClass );
        this.federationAmbassador.targetAttrPoziomUszkodzen = rtiAmbassador.getAttributeHandle( "PoziomUszkodzen", this.federationAmbassador.targetClass );
        this.federationAmbassador.targetAttrNiezdatny = rtiAmbassador.getAttributeHandle( "Niezdatny", this.federationAmbassador.targetClass );

        AttributeHandleSet attributes2 = RtiFactoryFactory.getRtiFactory().createAttributeHandleSet();
        attributes2.add( this.federationAmbassador.targetAttrPolozenie );
        attributes2.add( this.federationAmbassador.targetAttrPoziomUszkodzen );
        attributes2.add( this.federationAmbassador.targetAttrNiezdatny );

        rtiAmbassador.subscribeObjectClassAttributes(this.federationAmbassador.targetClass, attributes2);
    }

    @Override
    protected void deleteObjectsAndInteractions() throws Exception {
        this.removeObj(this.tankObj);
    }

    private void updateTankObj_Rodzaj(String value, double time) throws RTIinternalError, NameNotFound, FederateNotExecutionMember, ObjectClassNotDefined, ObjectNotKnown, RestoreInProgress, AttributeNotOwned, AttributeNotDefined, SaveInProgress, InvalidFederationTime, ConcurrentAccessAttempted {
        SuppliedAttributes attributes = RtiFactoryFactory.getRtiFactory().createSuppliedAttributes();
        int classHandle = rtiAmbassador.getObjectClass(this.tankObj);
        int attrHandle = rtiAmbassador.getAttributeHandle( "Rodzaj", classHandle );
        attributes.add(attrHandle, EncodingHelpers.encodeString(value));
        rtiAmbassador.updateAttributeValues(this.tankObj, attributes, "actualize".getBytes(), convertTime(time));
    }

    private void updateTankObj_Wielkosc(Vec3 value, double time) throws RTIinternalError, NameNotFound, FederateNotExecutionMember, ObjectClassNotDefined, ObjectNotKnown, RestoreInProgress, AttributeNotOwned, AttributeNotDefined, SaveInProgress, InvalidFederationTime, ConcurrentAccessAttempted {
        SuppliedAttributes attributes = RtiFactoryFactory.getRtiFactory().createSuppliedAttributes();
        int classHandle = rtiAmbassador.getObjectClass(this.tankObj);
        int attrHandle = rtiAmbassador.getAttributeHandle( "Wielkosc", classHandle );
        attributes.add(attrHandle, value.getBytes());
        rtiAmbassador.updateAttributeValues(this.tankObj, attributes, "actualize".getBytes(), convertTime(time));
    }

    private void updateTankObj_Polozenie(Vec3 value, double time) throws RTIinternalError, NameNotFound, FederateNotExecutionMember, ObjectClassNotDefined, ObjectNotKnown, RestoreInProgress, AttributeNotOwned, AttributeNotDefined, SaveInProgress, InvalidFederationTime, ConcurrentAccessAttempted {
        SuppliedAttributes attributes = RtiFactoryFactory.getRtiFactory().createSuppliedAttributes();
        int classHandle = rtiAmbassador.getObjectClass(this.tankObj);
        int attrHandle = rtiAmbassador.getAttributeHandle( "Polozenie", classHandle );
        attributes.add(attrHandle, value.getBytes());
        rtiAmbassador.updateAttributeValues(this.tankObj, attributes, "actualize".getBytes(), convertTime(time));
    }

    private void updateTankObj_WRuchu(boolean value, double time) throws RTIinternalError, NameNotFound, FederateNotExecutionMember, ObjectClassNotDefined, ObjectNotKnown, RestoreInProgress, AttributeNotOwned, AttributeNotDefined, SaveInProgress, InvalidFederationTime, ConcurrentAccessAttempted {
        SuppliedAttributes attributes = RtiFactoryFactory.getRtiFactory().createSuppliedAttributes();
        int classHandle = rtiAmbassador.getObjectClass(this.tankObj);
        int attrHandle = rtiAmbassador.getAttributeHandle( "WRuchu", classHandle );
        attributes.add(attrHandle, EncodingHelpers.encodeBoolean(value));
        rtiAmbassador.updateAttributeValues(this.tankObj, attributes, "actualize".getBytes(), convertTime(time));
    }

    private void updateTankObj_WystrzeleniePocisku(boolean value, double time) throws RTIinternalError, NameNotFound, FederateNotExecutionMember, ObjectClassNotDefined, ObjectNotKnown, RestoreInProgress, AttributeNotOwned, AttributeNotDefined, SaveInProgress, InvalidFederationTime, ConcurrentAccessAttempted {
        SuppliedAttributes attributes = RtiFactoryFactory.getRtiFactory().createSuppliedAttributes();
        int classHandle = rtiAmbassador.getObjectClass(this.tankObj);
        int attrHandle = rtiAmbassador.getAttributeHandle( "WystrzeleniePocisku", classHandle );
        attributes.add(attrHandle, EncodingHelpers.encodeBoolean(value));
        rtiAmbassador.updateAttributeValues(this.tankObj, attributes, "actualize".getBytes(), convertTime(time));
    }

    public static void main(String[] args) {
        try {
            new TankFederate().runFederate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
