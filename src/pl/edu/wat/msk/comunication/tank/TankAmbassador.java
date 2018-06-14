package pl.edu.wat.msk.comunication.tank;

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

    private int celObjHandle = 0;
    private int celAttrPolozenieHandle       = 0;
    private int celAttrPoziomUszkodzenHandle = 0;
    private int celAttrNiezdatnyHandle       = 0;
    private Target targetObj = new Target();


    @Override
    public void receiveInteraction(int interactionClass, ReceivedInteraction theInteraction, byte[] tag, LogicalTime theTime, EventRetractionHandle eventRetractionHandle) {
        super.receiveInteraction(interactionClass, theInteraction, tag, theTime, eventRetractionHandle);
    }

    @Override
    public void reflectAttributeValues(int theObject, ReflectedAttributes theAttributes, byte[] tag, LogicalTime theTime, EventRetractionHandle retractionHandle) {
        if(this.objs.get(theObject) == celObjHandle) {
            for (int i = 0; i < theAttributes.size(); i++) {
                try {
                    int handle = theAttributes.getAttributeHandle(i);
                    byte[] value = theAttributes.getValue(i);

                    if(handle == celAttrPolozenieHandle && value != null) {
                        Vec3 pos = new Vec3(value);
                        targetObj.setPolozenie(pos);
                    } else if(handle == celAttrPoziomUszkodzenHandle && value != null) {
                        int lvl = EncodingHelpers.decodeInt(value);
                        targetObj.setPoziomUszkodzen(lvl);
                    } else if(handle == celAttrNiezdatnyHandle && value != null) {
                        boolean nzd = EncodingHelpers.decodeBoolean(value);
                        targetObj.setNiezdatny(nzd);
                    }
                } catch (ArrayIndexOutOfBounds aioob) {
                    // won't happen
                }
            }

            System.out.println(targetObj.getPolozenie());
            System.out.println(targetObj.getPoziomUszkodzen());
            System.out.println(targetObj.getNiezdatny());
        }
    }

    public void setCelAttrPolozenieHandle(int celAttrPolozenieHandle) {
        this.celAttrPolozenieHandle = celAttrPolozenieHandle;
    }

    public void setCelAttrPoziomUszkodzenHandle(int celAttrPoziomUszkodzenHandle) {
        this.celAttrPoziomUszkodzenHandle = celAttrPoziomUszkodzenHandle;
    }

    public void setCelAttrNiezdatnyHandle(int celAttrNiezdatnyHandle) {
        this.celAttrNiezdatnyHandle = celAttrNiezdatnyHandle;
    }

    public void setCelObjHandle(int celObjHandle) {
        this.celObjHandle = celObjHandle;
    }
}
