package pl.edu.wat.msk;

import hla.rti.CouldNotDiscover;
import hla.rti.EventRetractionHandle;
import hla.rti.FederateInternalError;
import hla.rti.InvalidFederationTime;
import hla.rti.LogicalTime;
import hla.rti.ObjectClassNotKnown;
import hla.rti.ObjectNotKnown;
import hla.rti.ReceivedInteraction;
import hla.rti.ReflectedAttributes;
import hla.rti.jlc.NullFederateAmbassador;
import org.portico.impl.hla13.types.DoubleTime;
import pl.edu.wat.msk.object.BaseObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kamil Przyborowski
 * Wojskowa Akademia Techniczna im. Jarosława Dąbrowskiego, Warszawa 2018.
 */
public abstract class BaseAmbassador extends NullFederateAmbassador {

    protected double grantedTime      = 0.0;

    public double federateTime        = 0.0;
    public double federateLookahead   = 1.0;

    public boolean isRegulating       = false;
    public boolean isConstrained      = false;
    public boolean isAdvancing        = false;

    public boolean isReadyToRun       = false;

    public boolean running 			   = true;

    protected int endSimulationInteractionHandle   = 0;
    protected int startSimulationInteractionHandle = 0;

    protected Map<Integer, Integer> objs = new HashMap<>();
    protected Map<Class<? extends BaseObject>, BaseObject> objectsInstance = new HashMap<>();


    @Override
    public void receiveInteraction( int interactionClass, ReceivedInteraction theInteraction, byte[] tag ) {
        receiveInteraction(interactionClass, theInteraction, tag, null, null);
    }

    @Override
    public void receiveInteraction( int interactionClass, ReceivedInteraction theInteraction, byte[] tag, LogicalTime theTime, EventRetractionHandle eventRetractionHandle ) {
        System.out.printf("%s: odebranie interacji %d%n", getName(), interactionClass);
        if(interactionClass == this.startSimulationInteractionHandle) {
            this.isReadyToRun = true;
        } else if (interactionClass == this.endSimulationInteractionHandle) {
            this.running = false;
        }
    }

    @Override
    public void reflectAttributeValues(int theObject, ReflectedAttributes theAttributes, byte[] tag) {
        reflectAttributeValues(theObject, theAttributes, tag, null, null);
    }

    @Override
    public void reflectAttributeValues(int theObject, ReflectedAttributes theAttributes, byte[] tag, LogicalTime theTime, EventRetractionHandle retractionHandle) {

    }

    @Override
    public void discoverObjectInstance(int theObject, int theObjectClass, String objectName) throws CouldNotDiscover, ObjectClassNotKnown, FederateInternalError {
        if(!this.objs.containsKey(theObject)) {
            this.objs.put(theObject, theObjectClass);
        }
    }

    @Override
    public void removeObjectInstance(int theObject, byte[] userSuppliedTag) throws ObjectNotKnown, FederateInternalError {
        try {
            removeObjectInstance(theObject, userSuppliedTag, null, null);
        } catch (InvalidFederationTime invalidFederationTime) {
            invalidFederationTime.printStackTrace();
        }
    }

    @Override
    public void removeObjectInstance(int theObject, byte[] userSuppliedTag, LogicalTime theTime, EventRetractionHandle retractionHandle) throws ObjectNotKnown, InvalidFederationTime, FederateInternalError {
        this.objs.remove(theObject);
        this.objectsInstance.remove(theObject);
    }

    public void setEndSimulationInteractionHandle(int handle) {
        this.endSimulationInteractionHandle = handle;
    }

    public void setStartSimulationInteractionHandle(int startSimulationInteractionHandle) {
        this.startSimulationInteractionHandle = startSimulationInteractionHandle;
    }

    @Override
    public void timeRegulationEnabled(LogicalTime theFederateTime ) {
        this.federateTime = convertTime( theFederateTime );
        this.isRegulating = true;
    }

    @Override
    public void timeConstrainedEnabled( LogicalTime theFederateTime ) {
        this.federateTime = convertTime( theFederateTime );
        this.isConstrained = true;
    }

    @Override
    public void timeAdvanceGrant( LogicalTime theTime ) {
        this.federateTime = convertTime( theTime );
        this.grantedTime = convertTime(theTime);
        this.isAdvancing = false;
    }

    public double getGrantedTime() {
        return grantedTime;
    }

    public <T extends BaseObject> T getObjectInstance(Class<T> tClass) {
        return tClass.cast(this.objectsInstance.get(tClass));
    }

    protected double convertTime(LogicalTime logicalTime ) {
        return ((DoubleTime)logicalTime).getTime();
    }

    protected void log( String message ) {
        System.out.println( getName()+": " + message );
    }

    protected String getName() {
        return this.getClass().getSimpleName();
    }
}
