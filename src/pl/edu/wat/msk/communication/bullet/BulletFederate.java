package pl.edu.wat.msk.communication.bullet;

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
import pl.edu.wat.msk.object.DefenseSystemBullet;
import pl.edu.wat.msk.object.Tank;
import pl.edu.wat.msk.object.Target;
import pl.edu.wat.msk.object.Weather;
import pl.edu.wat.msk.util.Vec3;

/**
 * Created by Kamil Przyborowski
 * Wojskowa Akademia Techniczna im. Jarosława Dąbrowskiego, Warszawa 2018.
 */
public class BulletFederate extends BaseFederate<BulletAmbassador> {

    private int bulletObj = 0;


    @Override
    protected void update(double time) throws Exception {
        // Target ---------------------------------------------------------------------------------
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

        // Weather ---------------------------------------------------------------------------------
        if(this.federationAmbassador.weatherClassFlag_newInstance) {
            // pojawił się nowy obiekty typu Pogoda
            Weather weather = this.federationAmbassador.getObjectInstance(Weather.class);
            this.federationAmbassador.weatherClassFlag_newInstance = false;
        }

        if(this.federationAmbassador.weatherClassFlag_attrsUpdated) {
            // zaktualizowano atrybut obiektu Pogoda
            Weather weather = this.federationAmbassador.getObjectInstance(Weather.class);
            this.federationAmbassador.weatherClassFlag_attrsUpdated = false;
        }

        // Tank ---------------------------------------------------------------------------------
        if(this.federationAmbassador.tankClassFlag_newInstance) {
            // pojawił się nowy obiekty typu Czolg
            Tank tank = this.federationAmbassador.getObjectInstance(Tank.class);
            this.federationAmbassador.tankClassFlag_newInstance = false;
        }

        if(this.federationAmbassador.tankClassFlag_attrsUpdated) {
            // zaktualizowano atrybut obiektu Czolg
            Tank tank = this.federationAmbassador.getObjectInstance(Tank.class);
            this.federationAmbassador.tankClassFlag_attrsUpdated = false;
        }

        // DefenseSystemBullet ---------------------------------------------------------------------------------
        if(this.federationAmbassador.defenseSystemBulletClassFlag_newInstance) {
            // pojawił się nowy obiekty typu Czolg
            DefenseSystemBullet defenseSystemBullet = this.federationAmbassador.getObjectInstance(DefenseSystemBullet.class);
            this.federationAmbassador.defenseSystemBulletClassFlag_newInstance = false;
        }

        if(this.federationAmbassador.defenseSystemBulletClassFlag_attrsUpdated) {
            // zaktualizowano atrybut obiektu Czolg
            DefenseSystemBullet defenseSystemBullet = this.federationAmbassador.getObjectInstance(DefenseSystemBullet.class);
            this.federationAmbassador.defenseSystemBulletClassFlag_attrsUpdated = false;
        }


        // Tu jakaś symulacja a jej wyniki można przesłać za pomocą metod
//        updateTankObj_Polozenie(new Vec3(1,1,1), time);
//        updateTankObj_Rodzaj("af", time);
//        updateTankObj_Wielkosc(new Vec3(1, 2, 3), time);
//        updateTankObj_WRuchu(true, time);
    }

    @Override
    protected void init() throws Exception {
        this.bulletObj = createObj("Pocisk");
    }

    @Override
    protected void publishAndSubscribe() throws RTIexception {
        // rejestracj obj Pocisk
        int bulletClass                        = rtiAmbassador.getObjectClassHandle("ObjectRoot.Pocisk");
        int bulletClassAttrRodzaj              = rtiAmbassador.getAttributeHandle( "Rodzaj", bulletClass );
        int bulletClassAttrWielkosc            = rtiAmbassador.getAttributeHandle( "Wielkosc", bulletClass );
        int bulletClassAttrPolozenie           = rtiAmbassador.getAttributeHandle( "Polozenie", bulletClass );
        int bulletClassAttrWRuchu              = rtiAmbassador.getAttributeHandle( "WRuchu", bulletClass );

        AttributeHandleSet attributes = RtiFactoryFactory.getRtiFactory().createAttributeHandleSet();
        attributes.add( bulletClassAttrRodzaj );
        attributes.add( bulletClassAttrWielkosc );
        attributes.add( bulletClassAttrPolozenie );
        attributes.add( bulletClassAttrWRuchu );

        rtiAmbassador.publishObjectClass(bulletClass, attributes);



        // subscribe Target
        this.federationAmbassador.targetClass               = rtiAmbassador.getObjectClassHandle("ObjectRoot.Cel");
        this.federationAmbassador.targetAttrPolozenie       = rtiAmbassador.getAttributeHandle( "Polozenie", this.federationAmbassador.targetClass );
        this.federationAmbassador.targetAttrPoziomUszkodzen = rtiAmbassador.getAttributeHandle( "PoziomUszkodzen", this.federationAmbassador.targetClass );
        this.federationAmbassador.targetAttrNiezdatny       = rtiAmbassador.getAttributeHandle( "Niezdatny", this.federationAmbassador.targetClass );

        attributes = RtiFactoryFactory.getRtiFactory().createAttributeHandleSet();
        attributes.add( this.federationAmbassador.targetAttrPolozenie );
        attributes.add( this.federationAmbassador.targetAttrPoziomUszkodzen );
        attributes.add( this.federationAmbassador.targetAttrNiezdatny );

        rtiAmbassador.subscribeObjectClassAttributes(this.federationAmbassador.targetClass, attributes);


        // subscribe Weather
        this.federationAmbassador.weatherClass              = rtiAmbassador.getObjectClassHandle("ObjectRoot.Pogoda");
        this.federationAmbassador.weatherAttrWielkoscOpadow = rtiAmbassador.getAttributeHandle( "WielkoscOpadow", this.federationAmbassador.weatherClass );
        this.federationAmbassador.weatherAttrSilaWiatru     = rtiAmbassador.getAttributeHandle( "SilaWiatru", this.federationAmbassador.weatherClass );
        this.federationAmbassador.weatherAttrKierunekWiatru = rtiAmbassador.getAttributeHandle( "KierunekWiatru", this.federationAmbassador.weatherClass );

        attributes = RtiFactoryFactory.getRtiFactory().createAttributeHandleSet();
        attributes.add( this.federationAmbassador.weatherAttrWielkoscOpadow );
        attributes.add( this.federationAmbassador.weatherAttrSilaWiatru );
        attributes.add( this.federationAmbassador.weatherAttrKierunekWiatru );

        rtiAmbassador.subscribeObjectClassAttributes(this.federationAmbassador.weatherClass, attributes);


        // subscribe Tank
        this.federationAmbassador.tankClass                   = rtiAmbassador.getObjectClassHandle("ObjectRoot.Czolg");
        this.federationAmbassador.tankAttrRodzaj              = rtiAmbassador.getAttributeHandle( "Rodzaj", this.federationAmbassador.tankClass );
        this.federationAmbassador.tankAttrWielkosc            = rtiAmbassador.getAttributeHandle( "Wielkosc", this.federationAmbassador.tankClass );
        this.federationAmbassador.tankAttrPolozenie           = rtiAmbassador.getAttributeHandle( "Polozenie", this.federationAmbassador.tankClass );
        this.federationAmbassador.tankAttrWRuchu              = rtiAmbassador.getAttributeHandle( "WRuchu", this.federationAmbassador.tankClass );
        this.federationAmbassador.tankAttrWystrzeleniePocisku = rtiAmbassador.getAttributeHandle( "WystrzeleniePocisku", this.federationAmbassador.tankClass );

        attributes = RtiFactoryFactory.getRtiFactory().createAttributeHandleSet();
        attributes.add( this.federationAmbassador.tankAttrRodzaj );
        attributes.add( this.federationAmbassador.tankAttrWielkosc );
        attributes.add( this.federationAmbassador.tankAttrPolozenie );
        attributes.add( this.federationAmbassador.tankAttrWRuchu );
        attributes.add( this.federationAmbassador.tankAttrWystrzeleniePocisku );

        rtiAmbassador.subscribeObjectClassAttributes(this.federationAmbassador.tankClass, attributes);


        // subscribe DefenseSystemBullet
        this.federationAmbassador.defenseSystemBulletClass         = rtiAmbassador.getObjectClassHandle("ObjectRoot.PociskSystemuObronyWroga");
        this.federationAmbassador.defenseSystemBulletAttrPolozenie = rtiAmbassador.getAttributeHandle( "Polozenie", this.federationAmbassador.defenseSystemBulletClass );
        this.federationAmbassador.defenseSystemBulletAttrWRuchu    = rtiAmbassador.getAttributeHandle( "WRuchu", this.federationAmbassador.defenseSystemBulletClass );

        attributes = RtiFactoryFactory.getRtiFactory().createAttributeHandleSet();
        attributes.add( this.federationAmbassador.defenseSystemBulletAttrPolozenie );
        attributes.add( this.federationAmbassador.defenseSystemBulletAttrWRuchu );

        rtiAmbassador.subscribeObjectClassAttributes(this.federationAmbassador.defenseSystemBulletClass, attributes);
    }

    @Override
    protected void deleteObjectsAndInteractions() throws Exception {
        this.removeObj(this.bulletObj);
    }

    private void updateTankObj_Rodzaj(String value, double time) throws RTIinternalError, NameNotFound, FederateNotExecutionMember, ObjectClassNotDefined, ObjectNotKnown, RestoreInProgress, AttributeNotOwned, AttributeNotDefined, SaveInProgress, InvalidFederationTime, ConcurrentAccessAttempted {
        SuppliedAttributes attributes = RtiFactoryFactory.getRtiFactory().createSuppliedAttributes();
        int classHandle = rtiAmbassador.getObjectClass(this.bulletObj);
        int attrHandle = rtiAmbassador.getAttributeHandle( "Rodzaj", classHandle );
        attributes.add(attrHandle, EncodingHelpers.encodeString(value));
        rtiAmbassador.updateAttributeValues(this.bulletObj, attributes, "actualize".getBytes(), convertTime(time));
    }

    private void updateTankObj_Wielkosc(Vec3 value, double time) throws RTIinternalError, NameNotFound, FederateNotExecutionMember, ObjectClassNotDefined, ObjectNotKnown, RestoreInProgress, AttributeNotOwned, AttributeNotDefined, SaveInProgress, InvalidFederationTime, ConcurrentAccessAttempted {
        SuppliedAttributes attributes = RtiFactoryFactory.getRtiFactory().createSuppliedAttributes();
        int classHandle = rtiAmbassador.getObjectClass(this.bulletObj);
        int attrHandle = rtiAmbassador.getAttributeHandle( "Wielkosc", classHandle );
        attributes.add(attrHandle, value.getBytes());
        rtiAmbassador.updateAttributeValues(this.bulletObj, attributes, "actualize".getBytes(), convertTime(time));
    }

    private void updateTankObj_Polozenie(Vec3 value, double time) throws RTIinternalError, NameNotFound, FederateNotExecutionMember, ObjectClassNotDefined, ObjectNotKnown, RestoreInProgress, AttributeNotOwned, AttributeNotDefined, SaveInProgress, InvalidFederationTime, ConcurrentAccessAttempted {
        SuppliedAttributes attributes = RtiFactoryFactory.getRtiFactory().createSuppliedAttributes();
        int classHandle = rtiAmbassador.getObjectClass(this.bulletObj);
        int attrHandle = rtiAmbassador.getAttributeHandle( "Polozenie", classHandle );
        attributes.add(attrHandle, value.getBytes());
        rtiAmbassador.updateAttributeValues(this.bulletObj, attributes, "actualize".getBytes(), convertTime(time));
    }

    private void updateTankObj_WRuchu(boolean value, double time) throws RTIinternalError, NameNotFound, FederateNotExecutionMember, ObjectClassNotDefined, ObjectNotKnown, RestoreInProgress, AttributeNotOwned, AttributeNotDefined, SaveInProgress, InvalidFederationTime, ConcurrentAccessAttempted {
        SuppliedAttributes attributes = RtiFactoryFactory.getRtiFactory().createSuppliedAttributes();
        int classHandle = rtiAmbassador.getObjectClass(this.bulletObj);
        int attrHandle = rtiAmbassador.getAttributeHandle( "WRuchu", classHandle );
        attributes.add(attrHandle, EncodingHelpers.encodeBoolean(value));
        rtiAmbassador.updateAttributeValues(this.bulletObj, attributes, "actualize".getBytes(), convertTime(time));
    }


    public static void main(String[] args) {
        try {
            new BulletFederate().runFederate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
