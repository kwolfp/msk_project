package pl.edu.wat.msk.communication.defense.system;

import hla.rti.ArrayIndexOutOfBounds;
import hla.rti.CouldNotDiscover;
import hla.rti.EventRetractionHandle;
import hla.rti.FederateInternalError;
import hla.rti.LogicalTime;
import hla.rti.ObjectClassNotKnown;
import hla.rti.ReflectedAttributes;
import hla.rti.jlc.EncodingHelpers;
import pl.edu.wat.msk.BaseAmbassador;
import pl.edu.wat.msk.object.Bullet;
import pl.edu.wat.msk.util.Vec3;

/**
 * Created by Kamil Przyborowski
 * Wojskowa Akademia Techniczna im. Jarosława Dąbrowskiego, Warszawa 2018.
 */
public class DefenseSystemAmbassador extends BaseAmbassador {

    public int bulletClass          = 0;
    public int bulletAttr_Rodzaj    = 0;
    public int bulletAttr_Wielkosc  = 0;
    public int bulletAttr_Polozenie = 0;
    public int bulletAttr_WRuchu    = 0;

    public boolean bulletClassFlag_newInstance  = false;
    public boolean bulletClassFlag_attrsUpdated = false;


    @Override
    public void reflectAttributeValues(int theObject, ReflectedAttributes theAttributes, byte[] tag, LogicalTime theTime, EventRetractionHandle retractionHandle) {
        if(this.objs.get(theObject) == this.bulletClass) {
            Bullet bullet = getObjectInstance(Bullet.class);
            for (int i = 0; i < theAttributes.size(); i++) {
                try {
                    int handle = theAttributes.getAttributeHandle(i);
                    byte[] value = theAttributes.getValue(i);

                    if(handle == bulletAttr_Rodzaj && value != null) {
                        bullet.setRodzaj(EncodingHelpers.decodeString(value));
                    } else if(handle == bulletAttr_Wielkosc && value != null) {
                        bullet.setWielkosc(new Vec3(value));
                    } else if(handle == bulletAttr_Polozenie && value != null) {
                        bullet.setPolozenie(new Vec3(value));
                    } else if(handle == bulletAttr_WRuchu && value != null) {
                        bullet.setwRuchu(EncodingHelpers.decodeBoolean(value));
                    }
                } catch (ArrayIndexOutOfBounds aioob) {
                    // won't happen
                }
            }
            this.objectsInstance.replace(Bullet.class, bullet);
            this.bulletClassFlag_attrsUpdated = true;
        }
    }

    @Override
    public void discoverObjectInstance(int theObject, int theObjectClass, String objectName) throws CouldNotDiscover, ObjectClassNotKnown, FederateInternalError {
        super.discoverObjectInstance(theObject, theObjectClass, objectName);

        if(theObjectClass == this.bulletClass) {
            Bullet bullet = new Bullet();
            bullet.setInstance(theObject);
            this.objectsInstance.put(Bullet.class, bullet);
            this.bulletClassFlag_newInstance = true;
        }
    }
}
