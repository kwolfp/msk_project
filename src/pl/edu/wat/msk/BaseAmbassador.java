package pl.edu.wat.msk;

import hla.rti.EventRetractionHandle;
import hla.rti.LogicalTime;
import hla.rti.ReceivedInteraction;
import hla.rti.ReflectedAttributes;
import hla.rti.jlc.NullFederateAmbassador;
import org.portico.impl.hla13.types.DoubleTime;

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

    public boolean isAnnounced        = false;
    public boolean isReadyToRun       = false;

    public boolean running 			   = true;

    protected int endSimulationInteractionHendle = 0;


    protected double convertTime( LogicalTime logicalTime ) {
        return ((DoubleTime)logicalTime).getTime();
    }

    protected void log( String message ) {
        System.out.println( getName()+"Ambassador: " + message );
    }

    public void synchronizationPointRegistrationFailed( String label ) {
        log( "Failed to register sync point: " + label );
    }

    public void synchronizationPointRegistrationSucceeded( String label ) {
        log( "Successfully registered sync point: " + label );
    }

    public void announceSynchronizationPoint( String label, byte[] tag ) {
        log( "Synchronization point announced: " + label );
        if( label.equals(BaseFederate.READY_TO_RUN) )
            this.isAnnounced = true;
    }

    public void federationSynchronized( String label ) {
        log( "Federation Synchronized: " + label );
        if( label.equals(BaseFederate.READY_TO_RUN) )
            this.isReadyToRun = true;
    }

    public void receiveInteraction( int interactionClass, ReceivedInteraction theInteraction, byte[] tag ) {
        receiveInteraction(interactionClass, theInteraction, tag, null, null);
    }

    public void receiveInteraction( int interactionClass, ReceivedInteraction theInteraction, byte[] tag, LogicalTime theTime, EventRetractionHandle eventRetractionHandle ) {

    }

    public void reflectAttributeValues(int theObject, ReflectedAttributes theAttributes, byte[] tag) {
        reflectAttributeValues(theObject, theAttributes, tag, null, null);
    }

    public void reflectAttributeValues(int theObject, ReflectedAttributes theAttributes, byte[] tag, LogicalTime theTime, EventRetractionHandle retractionHandle) {

    }

    protected abstract String getName();

    public void setEndSimulationInteractionHandle(int handle) {
        this.endSimulationInteractionHendle = handle;
    }

    public void timeRegulationEnabled( LogicalTime theFederateTime ) {
        this.federateTime = convertTime( theFederateTime );
        this.isRegulating = true;
    }

    public void timeConstrainedEnabled( LogicalTime theFederateTime ) {
        this.federateTime = convertTime( theFederateTime );
        this.isConstrained = true;
    }

    public void timeAdvanceGrant( LogicalTime theTime ) {
        this.federateTime = convertTime( theTime );
        this.grantedTime = convertTime(theTime);
        this.isAdvancing = false;
    }

    public double getGrantedTime() {
        return grantedTime;
    }
}
