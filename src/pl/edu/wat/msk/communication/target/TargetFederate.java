package pl.edu.wat.msk.communication.target;

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
import pl.edu.wat.msk.object.Bullet;
import pl.edu.wat.msk.util.Vec3;

import java.util.Random;

/**
 * Created by Kamil Przyborowski
 * Wojskowa Akademia Techniczna im. Jarosława Dąbrowskiego, Warszawa 2018.
 */
public class TargetFederate extends BaseFederate<TargetAmbassador> {

    private int targetObj = -1;
    private Random random = new Random();

    private float speed = 4f;
    private Vec3 dir = new Vec3(0, 0, 0);
    private Vec3 pos = new Vec3(0, 0, 0);
    private int poziomUszkodzen = 0;
    private int max = random.nextInt(5)+1;
    private boolean remove = false;

    @Override
    protected void update(double time) throws Exception {
        if(remove) {
            remove = false;
            removeObj(targetObj);
            targetObj = -1;
            poziomUszkodzen = 0;
            federationAmbassador.removeObject(Bullet.class);
        }

        if(this.federationAmbassador.bulletClassFlag_newInstance) {
            // pojawił się nowy obiekty typu Pocisk
            Bullet bullet = this.federationAmbassador.getObjectInstance(Bullet.class);
            this.federationAmbassador.bulletClassFlag_newInstance = false;
        }

        if(this.federationAmbassador.bulletClassFlag_attrsUpdated) {
            // zaktualizowano atrybut obiektu Pocisk
            Bullet bullet = this.federationAmbassador.getObjectInstance(Bullet.class);

            if(this.targetObj != -1) {
                Vec3 bulletPos = bullet.getPolozenie();
                Vec3 bulletSize = bullet.getWielkosc();
                if(Math.abs(pos.getX()-bulletPos.getX()) < bulletSize.getX()+20f &&
                    Math.abs(pos.getY()-bulletPos.getY()) < bulletSize.getY()+20f &&
                    Math.abs(pos.getZ()-bulletPos.getZ()) < bulletSize.getZ()+20f) {
                    poziomUszkodzen ++;
                    if(poziomUszkodzen >= max) {
                        remove = true;
                        updateTargetObj_Niezdatny(true, time);
                    }else {
                        updateTargetObj_PoziomUszkodzen(poziomUszkodzen, time);
                    }
                }
            }

            this.federationAmbassador.bulletClassFlag_attrsUpdated = false;
        }

        if(this.targetObj == -1 && random.nextFloat() < 0.02 && random.nextBoolean() && !random.nextBoolean()) {
            this.targetObj = createObj("Cel");

            this.pos = new Vec3(random.nextInt(1000), random.nextInt(1000), random.nextInt(1000));
            this.dir = new Vec3(random.nextDouble(), random.nextDouble(), random.nextDouble());
            this.max = random.nextInt(5)+1;
            updateTargetObj_Polozenie(this.pos, time);
            updateTargetObj_PoziomUszkodzen(0, time);
            updateTargetObj_Niezdatny(false, time);
        }

        if(this.targetObj != -1) {
            this.pos = new Vec3(pos.getX()+speed*dir.getX(), pos.getY()+speed*dir.getY(), pos.getZ()+speed*dir.getZ());
            updateTargetObj_Polozenie(pos, time);
        }
    }

    // =======================================================================================

    @Override
    protected void init() throws Exception {
//        this.targetObj = createObj("Cel");
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


        this.federationAmbassador.bulletClass = rtiAmbassador.getObjectClassHandle("ObjectRoot.Pocisk");
        this.federationAmbassador.bulletAttr_Rodzaj = rtiAmbassador.getAttributeHandle( "Rodzaj", this.federationAmbassador.bulletClass);
        this.federationAmbassador.bulletAttr_Wielkosc = rtiAmbassador.getAttributeHandle( "Wielkosc", this.federationAmbassador.bulletClass);
        this.federationAmbassador.bulletAttr_Polozenie = rtiAmbassador.getAttributeHandle( "Polozenie", this.federationAmbassador.bulletClass);
        this.federationAmbassador.bulletAttr_WRuchu = rtiAmbassador.getAttributeHandle( "WRuchu", this.federationAmbassador.bulletClass);

        attributes = RtiFactoryFactory.getRtiFactory().createAttributeHandleSet();
        attributes.add( this.federationAmbassador.bulletAttr_Rodzaj);
        attributes.add( this.federationAmbassador.bulletAttr_Wielkosc);
        attributes.add( this.federationAmbassador.bulletAttr_Polozenie);
        attributes.add( this.federationAmbassador.bulletAttr_WRuchu);

        rtiAmbassador.subscribeObjectClassAttributes(this.federationAmbassador.bulletClass, attributes);
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
