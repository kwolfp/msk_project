package pl.edu.wat.msk;

import hla.rti.FederationExecutionAlreadyExists;
import hla.rti.LogicalTime;
import hla.rti.LogicalTimeInterval;
import hla.rti.RTIambassador;
import hla.rti.RTIexception;
import hla.rti.jlc.RtiFactoryFactory;
import org.portico.impl.hla13.types.DoubleTime;
import org.portico.impl.hla13.types.DoubleTimeInterval;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.ParameterizedType;
import java.net.MalformedURLException;

/**
 * Created by Kamil Przyborowski
 * Wojskowa Akademia Techniczna im. Jarosława Dąbrowskiego, Warszawa 2018.
 */
public abstract class BaseFederate<T extends BaseAmbassador> {

    protected static final String FEDERATION_NAME = "MskProjectFederation";
    protected static final String READY_TO_RUN    = "ReadyToRun";

    protected RTIambassador rtiamb;
    protected T fedamb;
    private final double timeStep           = 10.0;


    public void runFederate() throws Exception {
        rtiamb = RtiFactoryFactory.getRtiFactory().createRtiAmbassador();

        try
        {
            File fom = new File( "msk.fed" );
            rtiamb.createFederationExecution( FEDERATION_NAME,
                    fom.toURI().toURL() );
            log( "Created Federation" );
        }
        catch( FederationExecutionAlreadyExists exists )
        {
            log( "Didn't create federation, it already existed" );
        }
        catch( MalformedURLException urle )
        {
            log( "Exception processing fom: " + urle.getMessage() );
            urle.printStackTrace();
            return;
        }

        fedamb = getInstanceOfAmbassador();
        rtiamb.joinFederationExecution( getName()+"Federate", FEDERATION_NAME, fedamb );
        log( "Joined Federation as "+getName()+"Federate");

        rtiamb.registerFederationSynchronizationPoint( READY_TO_RUN, null );

        while(!fedamb.isAnnounced)
        {
            rtiamb.tick();
        }

        waitForUser();

        rtiamb.synchronizationPointAchieved( READY_TO_RUN );
        log( "Achieved sync point: " +READY_TO_RUN+ ", waiting for federation..." );
        while(!fedamb.isReadyToRun)
        {
            rtiamb.tick();
        }

        enableTimePolicy();

        publishAndSubscribeDefault();
        publishAndSubscribe();

        registerObjects();

        while (fedamb.running) {
            double timeToAdvance = fedamb.federateTime + timeStep;
            advanceTime(timeToAdvance);

            if(fedamb.grantedTime == timeToAdvance) {
                timeToAdvance += fedamb.federateLookahead;
                log("Updating stock at time: " + timeToAdvance);
                onUpdate(timeToAdvance);
                fedamb.federateTime = timeToAdvance;
            }

            rtiamb.tick();
        }
    }

    protected abstract void onUpdate(double time) throws Exception;

    protected abstract void registerObjects() throws RTIexception;

    protected void waitForUser() {
        log( " >>>>>>>>>> Press Enter to Continue <<<<<<<<<<" );
        BufferedReader reader = new BufferedReader( new InputStreamReader(System.in) );
        try
        {
            reader.readLine();
        }
        catch( Exception e )
        {
            log( "Error while waiting for user input: " + e.getMessage() );
            e.printStackTrace();
        }
    }

    protected void enableTimePolicy() throws RTIexception {
        LogicalTime currentTime = convertTime( fedamb.federateTime );
        LogicalTimeInterval lookahead = convertInterval( fedamb.federateLookahead );

        this.rtiamb.enableTimeRegulation( currentTime, lookahead );

        while(!fedamb.isRegulating)
        {
            rtiamb.tick();
        }

        this.rtiamb.enableTimeConstrained();

        while(!fedamb.isConstrained)
        {
            rtiamb.tick();
        }
    }

    protected abstract void publishAndSubscribe() throws RTIexception;

    protected void publishAndSubscribeDefault() throws RTIexception {
        int endSimulation = rtiamb.getInteractionClassHandle("InteractionRoot.KoniecSymulacji");
        fedamb.setEndSimulationInteractionHandle(endSimulation);
        rtiamb.subscribeInteractionClass(endSimulation);
    }

    protected void advanceTime( double timestep ) throws RTIexception {
        log("requesting time advance for: " + timestep);
        // request the advance
        fedamb.isAdvancing = true;
        LogicalTime newTime = convertTime( fedamb.federateTime + timestep );
        rtiamb.timeAdvanceRequest( newTime );
        while( fedamb.isAdvancing )
        {
            rtiamb.tick();
        }
    }

    protected LogicalTime convertTime( double time ) {
        return new DoubleTime( time );
    }

    protected LogicalTimeInterval convertInterval( double time ) {
        // PORTICO SPECIFIC!!
        return new DoubleTimeInterval( time );
    }

    protected void log( String message ) {
        System.out.println( getName()+"Federate   : " + message );
    }

    protected String getName() {
        return "Base";
    }

    @SuppressWarnings("unchecked")
    private T getInstanceOfAmbassador() {
        try {
            ParameterizedType superClass = (ParameterizedType) getClass().getGenericSuperclass();
            Class<T> type = (Class<T>) superClass.getActualTypeArguments()[0];
            return type.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
