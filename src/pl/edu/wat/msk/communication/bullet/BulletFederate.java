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

import java.util.Random;

/**
 * Created by Kamil Przyborowski
 * Wojskowa Akademia Techniczna im. Jarosława Dąbrowskiego, Warszawa 2018.
 */
public class BulletFederate extends BaseFederate<BulletAmbassador> {

    private int bulletObj = -1;
    private Random random = new Random();

    private float speed = 20f;
    private Vec3 dir;
    private Vec3 pos;
    private Vec3 size = new Vec3(2, 0.5, 0.5);
    private boolean remove = false;

    @Override
    protected void update(double time) throws Exception {
        if(remove && bulletObj != -1) {
            remove = false;
            removeObj(bulletObj);
            bulletObj = -1;
        }

        // Target ---------------------------------------------------------------------------------
        if(this.federationAmbassador.targetClassFlag_newInstance) {
            // pojawił się nowy obiekty typu Cel
            Target target = this.federationAmbassador.getObjectInstance(Target.class);
            this.federationAmbassador.targetClassFlag_newInstance = false;
        }

        if(this.federationAmbassador.targetClassFlag_attrsUpdated) {
            // zaktualizowano atrybut obiektu Cel
            Target target = this.federationAmbassador.getObjectInstance(Target.class);

            if(target.getNiezdatny()) {
                federationAmbassador.removeObject(Target.class);
                remove = true;
            }

            if(this.bulletObj != -1) {
                Vec3 targetPos = target.getPolozenie();
                this.dir = new Vec3(targetPos.getX()-pos.getX(), targetPos.getY()-pos.getY(), targetPos.getZ()-pos.getZ()).normalize();
            }

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
            Target target = this.federationAmbassador.getObjectInstance(Target.class);

            if(this.bulletObj == -1 && target != null && tank.isWystrzeleniePocisku()) {
                this.bulletObj = createObj("Pocisk");
                Vec3 targetPos = target.getPolozenie();
                Vec3 tankPos = tank.getPolozenie();
                this.dir = new Vec3(targetPos.getX()-tankPos.getX(), targetPos.getY()-tankPos.getY(), targetPos.getZ()-tankPos.getZ()).normalize();
                this.pos = tank.getPolozenie();

                updateBulletObj_Polozenie(pos, time);
                updateBulletObj_Rodzaj("Kawa", time);
                updateBulletObj_Wielkosc(size, time);
                updateBulletObj_WRuchu(true, time);
                this.federationAmbassador.tankClassFlag_attrsUpdated = false;
            }
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
            Vec3 defenseBullet = defenseSystemBullet.getPolozenie();
            if(Math.abs(defenseBullet.getX()-pos.getX()) < size.getX()+50f &&
                Math.abs(defenseBullet.getY()-pos.getY()) < size.getY()+50f &&
                Math.abs(defenseBullet.getZ()-pos.getZ()) < size.getZ()+50f) {

                if(bulletObj != -1) {
                    System.out.println("Zestrzelony");
                    updateBulletObj_Zestrzelony(true, time);
                }

                remove = true;
            }

            this.federationAmbassador.defenseSystemBulletClassFlag_attrsUpdated = false;
        }

        if(!remove && this.bulletObj != -1) {
            Target target = federationAmbassador.getObjectInstance(Target.class);
            if(target != null) {
                Vec3 targetPos = target.getPolozenie();
                if(Math.abs(targetPos.getX()-pos.getX()) < size.getX()+20f &&
                    Math.abs(targetPos.getY()-pos.getY()) < size.getY()+20f &&
                    Math.abs(targetPos.getZ()-pos.getZ()) < size.getZ()+20f) {
                    remove = true;
                    updateBulletObj_WRuchu(false, time);
                } else {
                    Weather weather = federationAmbassador.getObjectInstance(Weather.class);
                    Vec3 air = weather.getKierunekWiatru();
                    pos = new Vec3(pos.getX()+speed*dir.getX()+air.getX(), pos.getY()+speed*dir.getY()+air.getY(), pos.getZ()+speed*dir.getZ()+air.getZ());
                    updateBulletObj_Polozenie(pos, time);
                }
            }
        }

        // Tu jakaś symulacja a jej wyniki można przesłać za pomocą metod
//        updateBulletObj_Polozenie(new Vec3(1,1,1), time);
//        updateBulletObj_Rodzaj("af", time);
//        updateBulletObj_Wielkosc(new Vec3(1, 2, 3), time);
//        updateBulletObj_WRuchu(true, time);
    }

    @Override
    protected void init() throws Exception {

    }

    @Override
    protected void publishAndSubscribe() throws RTIexception {
        // rejestracj obj Pocisk
        int bulletClass                        = rtiAmbassador.getObjectClassHandle("ObjectRoot.Pocisk");
        int bulletClassAttrRodzaj              = rtiAmbassador.getAttributeHandle( "Rodzaj", bulletClass );
        int bulletClassAttrWielkosc            = rtiAmbassador.getAttributeHandle( "Wielkosc", bulletClass );
        int bulletClassAttrPolozenie           = rtiAmbassador.getAttributeHandle( "Polozenie", bulletClass );
        int bulletClassAttrWRuchu              = rtiAmbassador.getAttributeHandle( "WRuchu", bulletClass );
        int bulletClassAttrZestrzelony         = rtiAmbassador.getAttributeHandle( "Zestrzelony", bulletClass );

        AttributeHandleSet attributes = RtiFactoryFactory.getRtiFactory().createAttributeHandleSet();
        attributes.add( bulletClassAttrRodzaj );
        attributes.add( bulletClassAttrWielkosc );
        attributes.add( bulletClassAttrPolozenie );
        attributes.add( bulletClassAttrWRuchu );
        attributes.add( bulletClassAttrZestrzelony );

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
        this.bulletObj = -1;
    }

    private void updateBulletObj_Rodzaj(String value, double time) throws RTIinternalError, NameNotFound, FederateNotExecutionMember, ObjectClassNotDefined, ObjectNotKnown, RestoreInProgress, AttributeNotOwned, AttributeNotDefined, SaveInProgress, InvalidFederationTime, ConcurrentAccessAttempted {
        SuppliedAttributes attributes = RtiFactoryFactory.getRtiFactory().createSuppliedAttributes();
        int classHandle = rtiAmbassador.getObjectClass(this.bulletObj);
        int attrHandle = rtiAmbassador.getAttributeHandle( "Rodzaj", classHandle );
        attributes.add(attrHandle, EncodingHelpers.encodeString(value));
        rtiAmbassador.updateAttributeValues(this.bulletObj, attributes, "actualize".getBytes(), convertTime(time));
    }

    private void updateBulletObj_Wielkosc(Vec3 value, double time) throws RTIinternalError, NameNotFound, FederateNotExecutionMember, ObjectClassNotDefined, ObjectNotKnown, RestoreInProgress, AttributeNotOwned, AttributeNotDefined, SaveInProgress, InvalidFederationTime, ConcurrentAccessAttempted {
        SuppliedAttributes attributes = RtiFactoryFactory.getRtiFactory().createSuppliedAttributes();
        int classHandle = rtiAmbassador.getObjectClass(this.bulletObj);
        int attrHandle = rtiAmbassador.getAttributeHandle( "Wielkosc", classHandle );
        attributes.add(attrHandle, value.getBytes());
        rtiAmbassador.updateAttributeValues(this.bulletObj, attributes, "actualize".getBytes(), convertTime(time));
    }

    private void updateBulletObj_Polozenie(Vec3 value, double time) throws RTIinternalError, NameNotFound, FederateNotExecutionMember, ObjectClassNotDefined, ObjectNotKnown, RestoreInProgress, AttributeNotOwned, AttributeNotDefined, SaveInProgress, InvalidFederationTime, ConcurrentAccessAttempted {
        SuppliedAttributes attributes = RtiFactoryFactory.getRtiFactory().createSuppliedAttributes();
        int classHandle = rtiAmbassador.getObjectClass(this.bulletObj);
        int attrHandle = rtiAmbassador.getAttributeHandle( "Polozenie", classHandle );
        attributes.add(attrHandle, value.getBytes());
        rtiAmbassador.updateAttributeValues(this.bulletObj, attributes, "actualize".getBytes(), convertTime(time));
    }

    private void updateBulletObj_WRuchu(boolean value, double time) throws RTIinternalError, NameNotFound, FederateNotExecutionMember, ObjectClassNotDefined, ObjectNotKnown, RestoreInProgress, AttributeNotOwned, AttributeNotDefined, SaveInProgress, InvalidFederationTime, ConcurrentAccessAttempted {
        SuppliedAttributes attributes = RtiFactoryFactory.getRtiFactory().createSuppliedAttributes();
        int classHandle = rtiAmbassador.getObjectClass(this.bulletObj);
        int attrHandle = rtiAmbassador.getAttributeHandle( "WRuchu", classHandle );
        attributes.add(attrHandle, EncodingHelpers.encodeBoolean(value));
        rtiAmbassador.updateAttributeValues(this.bulletObj, attributes, "actualize".getBytes(), convertTime(time));
    }

    private void updateBulletObj_Zestrzelony(boolean value, double time) throws RTIinternalError, NameNotFound, FederateNotExecutionMember, ObjectClassNotDefined, ObjectNotKnown, RestoreInProgress, AttributeNotOwned, AttributeNotDefined, SaveInProgress, InvalidFederationTime, ConcurrentAccessAttempted {
        SuppliedAttributes attributes = RtiFactoryFactory.getRtiFactory().createSuppliedAttributes();
        int classHandle = rtiAmbassador.getObjectClass(this.bulletObj);
        int attrHandle = rtiAmbassador.getAttributeHandle( "Zestrzelony", classHandle );
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
