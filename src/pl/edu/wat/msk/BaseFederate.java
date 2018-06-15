package pl.edu.wat.msk;

import hla.rti.FederationExecutionAlreadyExists;
import hla.rti.LogicalTime;
import hla.rti.LogicalTimeInterval;
import hla.rti.RTIambassador;
import hla.rti.RTIexception;
import hla.rti.ResignAction;
import hla.rti.jlc.RtiFactoryFactory;
import org.portico.impl.hla13.types.DoubleTime;
import org.portico.impl.hla13.types.DoubleTimeInterval;

import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.net.MalformedURLException;

/**
 * Created by Kamil Przyborowski
 * Wojskowa Akademia Techniczna im. Jarosława Dąbrowskiego, Warszawa 2018.
 */
public abstract class BaseFederate<T extends BaseAmbassador> {

    protected static final String FED_DEFAULT_FILE = "msk.fed";
    protected static final String FEDERATION_NAME  = "MskProjectFederation";

    protected RTIambassador rtiAmbassador;
    protected T federationAmbassador;
    protected final double timeStep                = 10.0;


    public void runFederate() throws Exception {
        this.rtiAmbassador = RtiFactoryFactory.getRtiFactory().createRtiAmbassador();

        this.tryCreateFederation(FED_DEFAULT_FILE);

        this.createAmbassadorAndJoin();

        this.waitForSimulationStart();

        this.enableTimePolicy();

        this.publishAndSubscribeDefault();
        this.publishAndSubscribe();

        this.init();

        while (federationAmbassador.running) {
            this.mainLoop();
            rtiAmbassador.tick();
        }

        this.destroyFederation();
    }

    protected void init() throws Exception {

    }

    protected void mainLoop() throws Exception {
        double timeToAdvance = federationAmbassador.federateTime + timeStep;
        advanceTime(timeStep);

        if(federationAmbassador.getGrantedTime() == timeToAdvance) {
            timeToAdvance += federationAmbassador.federateLookahead;
            update(timeToAdvance);
        }

        federationAmbassador.federateTime = timeToAdvance;
    }

    protected void tryCreateFederation(String fedFilePath) throws Exception {
        try {
            File fom = new File( fedFilePath );
            rtiAmbassador.createFederationExecution( FEDERATION_NAME, fom.toURI().toURL() );
            log( "Created Federation" );
        } catch( FederationExecutionAlreadyExists exists ) {
            log( "Didn't create federation, it already existed" );
        } catch( MalformedURLException urle ) {
            log( "Exception processing fom: " + urle.getMessage() );
            urle.printStackTrace();
        }
    }

    protected void createAmbassadorAndJoin() throws Exception {
        this.federationAmbassador = getInstanceOfAmbassador();
        rtiAmbassador.joinFederationExecution( getName(), FEDERATION_NAME, federationAmbassador);
        log( "Joined Federation as "+getName());
    }

    protected void waitForSimulationStart() throws Exception {
        int startSimulation = rtiAmbassador.getInteractionClassHandle("InteractionRoot.PoczatekSymulacji");
        federationAmbassador.setStartSimulationInteractionHandle(startSimulation);
        rtiAmbassador.subscribeInteractionClass(startSimulation);

        log("Waiting for simulation start interaction...");
        while(!federationAmbassador.isReadyToRun) {
            rtiAmbassador.tick();
        }
    }

    protected void enableTimePolicy() throws RTIexception {
        LogicalTime currentTime = convertTime(federationAmbassador.federateTime);
        LogicalTimeInterval lookahead = convertInterval(federationAmbassador.federateLookahead);

        this.rtiAmbassador.enableTimeRegulation(currentTime, lookahead);
        while(!federationAmbassador.isRegulating) {
            rtiAmbassador.tick();
        }

        this.rtiAmbassador.enableTimeConstrained();
        while(!federationAmbassador.isConstrained) {
            rtiAmbassador.tick();
        }
    }

    protected void publishAndSubscribeDefault() throws RTIexception {
        int endSimulation = rtiAmbassador.getInteractionClassHandle("InteractionRoot.KoniecSymulacji");
        federationAmbassador.setEndSimulationInteractionHandle(endSimulation);
        rtiAmbassador.subscribeInteractionClass(endSimulation);
    }

    protected void advanceTime( double timestep ) throws RTIexception {
//        log("requesting time advance for: " + (federationAmbassador.federateTime + timestep));
        // request the advance
        federationAmbassador.isAdvancing = true;
        LogicalTime newTime = convertTime( federationAmbassador.federateTime + timestep );
        rtiAmbassador.timeAdvanceRequest( newTime );
        while(federationAmbassador.isAdvancing && federationAmbassador.running) {
            rtiAmbassador.tick();
        }
    }

    @SuppressWarnings("unchecked")
    protected T getInstanceOfAmbassador() {
        try {
            ParameterizedType superClass = (ParameterizedType) getClass().getGenericSuperclass();
            Class<T> type = (Class<T>) superClass.getActualTypeArguments()[0];
            return type.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected abstract void update(double time) throws Exception;

    protected abstract void publishAndSubscribe() throws RTIexception;

    protected LogicalTimeInterval convertInterval(double time ) {
        return new DoubleTimeInterval( time );
    }

    protected LogicalTime convertTime( double time ) {
        return new DoubleTime( time );
    }

    protected void log( String message ) {
        System.out.printf("%s: %s%n", getName(), message);
    }

    protected String getName() {
        return this.getClass().getSimpleName();
    }

    protected void destroyFederation() throws Exception {
        deleteObjectsAndInteractions();
        rtiAmbassador.resignFederationExecution(ResignAction.NO_ACTION);
        rtiAmbassador.destroyFederationExecution(FEDERATION_NAME);
    }

    protected void deleteObjectsAndInteractions() throws Exception {

    }

    protected int createObj(String name) throws Exception {
        int classHandle = rtiAmbassador.getObjectClassHandle("ObjectRoot."+name);
        return rtiAmbassador.registerObjectInstance(classHandle);
    }

    protected void removeObj(int instance) throws Exception {
        rtiAmbassador.deleteObjectInstance(instance, "tag".getBytes());
    }

}
