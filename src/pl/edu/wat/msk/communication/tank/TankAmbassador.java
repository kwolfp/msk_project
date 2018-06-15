package pl.edu.wat.msk.communication.tank;

import hla.rti.*;
import hla.rti.jlc.EncodingHelpers;
import pl.edu.wat.msk.BaseAmbassador;
import pl.edu.wat.msk.object.Target;
import pl.edu.wat.msk.util.Vec3;

/**
 * Created by Kamil Przyborowski
 * Wojskowa Akademia Techniczna im. Jarosława Dąbrowskiego, Warszawa 2018.
 */
public class TankAmbassador extends BaseAmbassador {

    public int targetClass               = 0;
    public int targetAttrPolozenie       = 0;
    public int targetAttrPoziomUszkodzen = 0;
    public int targetAttrNiezdatny       = 0;
    public boolean targetClassFlag_newInstance  = false;
    public boolean targetClassFlag_attrsUpdated = false;


    @Override
    public void reflectAttributeValues(int theObject, ReflectedAttributes theAttributes, byte[] tag, LogicalTime theTime, EventRetractionHandle retractionHandle) {
        if(this.objs.get(theObject) == this.targetClass) {
            Target target = getObjectInstance(Target.class);
            for (int i = 0; i < theAttributes.size(); i++) {
                try {
                    int handle = theAttributes.getAttributeHandle(i);
                    byte[] value = theAttributes.getValue(i);

                    if(handle == targetAttrPolozenie && value != null) {
                        target.setPolozenie(new Vec3(value));
                    } else if(handle == targetAttrPoziomUszkodzen && value != null) {
                        target.setPoziomUszkodzen(EncodingHelpers.decodeInt(value));
                    } else if(handle == targetAttrNiezdatny && value != null) {
                        target.setNiezdatny(EncodingHelpers.decodeBoolean(value));
                    }
                } catch (ArrayIndexOutOfBounds aioob) {
                    // won't happen
                }
            }
            this.objectsInstance.replace(Target.class, target);
            this.targetClassFlag_attrsUpdated = true;
        }
    }

    @Override
    public void discoverObjectInstance(int theObject, int theObjectClass, String objectName) throws CouldNotDiscover, ObjectClassNotKnown, FederateInternalError {
        super.discoverObjectInstance(theObject, theObjectClass, objectName);

        if(theObjectClass == this.targetClass) {
            Target target = new Target();
            target.setInstance(theObject);
            this.objectsInstance.put(Target.class, target);
            this.targetClassFlag_newInstance = true;
        }
    }

}
