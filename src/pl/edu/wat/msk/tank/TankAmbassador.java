package pl.edu.wat.msk.tank;

import hla.rti.EventRetractionHandle;
import hla.rti.LogicalTime;
import hla.rti.ReceivedInteraction;
import hla.rti.ReflectedAttributes;
import pl.edu.wat.msk.BaseAmbassador;

/**
 * Created by Kamil Przyborowski
 * Wojskowa Akademia Techniczna im. Jarosława Dąbrowskiego, Warszawa 2018.
 */
public class TankAmbassador extends BaseAmbassador {

    @Override
    public void receiveInteraction(int interactionClass, ReceivedInteraction theInteraction, byte[] tag, LogicalTime theTime, EventRetractionHandle eventRetractionHandle) {
        super.receiveInteraction(interactionClass, theInteraction, tag, theTime, eventRetractionHandle);
    }

    @Override
    public void reflectAttributeValues(int theObject, ReflectedAttributes theAttributes, byte[] tag, LogicalTime theTime, EventRetractionHandle retractionHandle) {

    }

    @Override
    public String getName() {
        return "Tank";
    }

}
