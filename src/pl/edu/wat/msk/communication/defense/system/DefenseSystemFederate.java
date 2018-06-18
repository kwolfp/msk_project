package pl.edu.wat.msk.communication.defense.system;

import hla.rti.AttributeHandleSet;
import hla.rti.AttributeNotDefined;
import hla.rti.AttributeNotOwned;
import hla.rti.ConcurrentAccessAttempted;
import hla.rti.DeletePrivilegeNotHeld;
import hla.rti.FederateNotExecutionMember;
import hla.rti.InvalidFederationTime;
import hla.rti.LogicalTime;
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
public class DefenseSystemFederate extends BaseFederate<DefenseSystemAmbassador> {

    private int defenseSystemBulletObj = -1;

    private float speed = 20f;
    private Vec3 dir;
    private Vec3 posStart;
    private Vec3 pos;
    private Random random = new Random();

    @Override
    protected void update(double time) throws Exception {
        if(this.federationAmbassador.bulletClassFlag_newInstance) {
            // pojawił się nowy obiekty typu Pocisk
            Bullet bullet = this.federationAmbassador.getObjectInstance(Bullet.class);

            System.out.println("Nowy pocisk!");

            if(this.defenseSystemBulletObj != -1) {
                this.defenseSystemBulletObj = createObj("PociskSystemuObronyWroga");

                Vec3 targetPos = bullet.getPolozenie();
                this.dir = new Vec3(targetPos.getX()-pos.getX(), targetPos.getY()-pos.getY(), targetPos.getZ()-pos.getZ()).normalize();
                this.pos = new Vec3(posStart.getX(), posStart.getY(), posStart.getZ());
                speed = random.nextFloat()*30f;

                updateDefenseSystemBulletObj_Polozenie(pos, time);
                updateDefenseSystemBulletObj_WRuchu(true, time);
            }

            this.federationAmbassador.bulletClassFlag_newInstance = false;
        }

        if(this.federationAmbassador.bulletClassFlag_attrsUpdated) {
            // zaktualizowano atrybut obiektu Pocisk
            Bullet bullet = this.federationAmbassador.getObjectInstance(Bullet.class);
            this.federationAmbassador.bulletClassFlag_attrsUpdated = false;
        }
    }

    // =======================================================================================================================================================

    @Override
    protected void init() throws Exception {
        posStart = new Vec3(random.nextInt(1000), random.nextInt(1000), random.nextInt(1000));
    }

    @Override
    protected void publishAndSubscribe() throws RTIexception {
        int classHandle1 = rtiAmbassador.getObjectClassHandle("ObjectRoot.PociskSystemuObronyWroga");
        int polozenieHandle1 = rtiAmbassador.getAttributeHandle( "Polozenie", classHandle1 );
        int wRuchuHandle1 = rtiAmbassador.getAttributeHandle( "WRuchu", classHandle1 );

        AttributeHandleSet attributes1 = RtiFactoryFactory.getRtiFactory().createAttributeHandleSet();
        attributes1.add( polozenieHandle1 );
        attributes1.add( wRuchuHandle1 );

        rtiAmbassador.publishObjectClass(classHandle1, attributes1);


        this.federationAmbassador.bulletClass = rtiAmbassador.getObjectClassHandle("ObjectRoot.Pocisk");
        this.federationAmbassador.bulletAttr_Rodzaj = rtiAmbassador.getAttributeHandle( "Rodzaj", this.federationAmbassador.bulletClass);
        this.federationAmbassador.bulletAttr_Wielkosc = rtiAmbassador.getAttributeHandle( "Wielkosc", this.federationAmbassador.bulletClass);
        this.federationAmbassador.bulletAttr_Polozenie = rtiAmbassador.getAttributeHandle( "Polozenie", this.federationAmbassador.bulletClass);
        this.federationAmbassador.bulletAttr_WRuchu = rtiAmbassador.getAttributeHandle( "WRuchu", this.federationAmbassador.bulletClass);

        AttributeHandleSet attributes = RtiFactoryFactory.getRtiFactory().createAttributeHandleSet();
        attributes.add( this.federationAmbassador.bulletAttr_Rodzaj);
        attributes.add( this.federationAmbassador.bulletAttr_Wielkosc);
        attributes.add( this.federationAmbassador.bulletAttr_Polozenie);
        attributes.add( this.federationAmbassador.bulletAttr_WRuchu);

        rtiAmbassador.subscribeObjectClassAttributes(this.federationAmbassador.bulletClass, attributes);
    }

    @Override
    protected void deleteObjectsAndInteractions() throws Exception {
        this.removeObj(this.defenseSystemBulletObj);
    }

    private void updateDefenseSystemBulletObj_Polozenie(Vec3 value, double time) throws ObjectNotKnown, FederateNotExecutionMember, RTIinternalError, NameNotFound, ObjectClassNotDefined, RestoreInProgress, AttributeNotOwned, AttributeNotDefined, SaveInProgress, InvalidFederationTime, ConcurrentAccessAttempted {
        SuppliedAttributes attributes = RtiFactoryFactory.getRtiFactory().createSuppliedAttributes();

        int classHandle = rtiAmbassador.getObjectClass(this.defenseSystemBulletObj);
        int nzdHandle = rtiAmbassador.getAttributeHandle("Polozenie", classHandle);

        attributes.add(nzdHandle, value.getBytes());
        LogicalTime logicalTime = convertTime(time);
        rtiAmbassador.updateAttributeValues(this.defenseSystemBulletObj, attributes, "actualize".getBytes(), logicalTime);
    }

    private void updateDefenseSystemBulletObj_WRuchu(boolean value, double time) throws RTIinternalError, NameNotFound, FederateNotExecutionMember, ObjectClassNotDefined, ObjectNotKnown, RestoreInProgress, AttributeNotOwned, AttributeNotDefined, SaveInProgress, InvalidFederationTime, ConcurrentAccessAttempted {
        SuppliedAttributes attributes = RtiFactoryFactory.getRtiFactory().createSuppliedAttributes();

        int classHandle = rtiAmbassador.getObjectClass(this.defenseSystemBulletObj);
        int nzdHandle = rtiAmbassador.getAttributeHandle("WRuchu", classHandle);
        byte[] nzdValue = EncodingHelpers.encodeBoolean(value);

        attributes.add(nzdHandle, nzdValue);
        LogicalTime logicalTime = convertTime( time );
        rtiAmbassador.updateAttributeValues(this.defenseSystemBulletObj, attributes, "actualize".getBytes(), logicalTime);
    }

    public static void main(String[] args) {
        try {
            new DefenseSystemFederate().runFederate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
