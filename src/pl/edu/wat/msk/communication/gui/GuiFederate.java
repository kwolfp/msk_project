package pl.edu.wat.msk.communication.gui;

import hla.rti.ConcurrentAccessAttempted;
import hla.rti.FederateLoggingServiceCalls;
import hla.rti.FederateNotExecutionMember;
import hla.rti.InteractionClassNotDefined;
import hla.rti.InteractionClassNotPublished;
import hla.rti.InteractionParameterNotDefined;
import hla.rti.InvalidFederationTime;
import hla.rti.LogicalTime;
import hla.rti.NameNotFound;
import hla.rti.RTIexception;
import hla.rti.RTIinternalError;
import hla.rti.ResignAction;
import hla.rti.RestoreInProgress;
import hla.rti.SaveInProgress;
import hla.rti.SuppliedParameters;
import hla.rti.jlc.RtiFactoryFactory;
import pl.edu.wat.msk.BaseFederate;

/**
 * Created by Kamil Przyborowski
 * Wojskowa Akademia Techniczna im. Jarosława Dąbrowskiego, Warszawa 2018.
 */
public class GuiFederate extends BaseFederate<GuiAmbassador> {

    private int sendStartIter = 0;
    private boolean stopSimulation = false;

    @Override
    protected void waitForSimulationStart() throws RTIinternalError, ConcurrentAccessAttempted, FederateLoggingServiceCalls, SaveInProgress, InteractionClassNotDefined, RestoreInProgress, FederateNotExecutionMember, NameNotFound {

    }

    public void stopSimulation() {
        this.stopSimulation = true;
    }

    @Override
    protected void update(double time) throws Exception {
        if((sendStartIter ++) % 100 == 0) {
            sendStartInteraction(time);
        }

        if(stopSimulation) {
            sendStopInteraction(time);
            federationAmbassador.running = false;
        }
    }

    @Override
    protected void publishAndSubscribe() throws RTIexception {
        int startSimulation = rtiAmbassador.getInteractionClassHandle("InteractionRoot.PoczatekSymulacji");
        rtiAmbassador.publishInteractionClass(startSimulation);

        int stopSimulation = rtiAmbassador.getInteractionClassHandle("InteractionRoot.KoniecSymulacji");
        rtiAmbassador.publishInteractionClass(stopSimulation);
    }

    @Override
    protected void deleteObjectsAndInteractions() {

    }

    private void sendStartInteraction(double t) {
        sendInteraction("PoczatekSymulacji", t);
    }

    private void sendStopInteraction(double t) {
        sendInteraction("KoniecSymulacji", t);
    }

    private void sendInteraction(String interactionClass, double t) {
        try {
            SuppliedParameters parameters = RtiFactoryFactory.getRtiFactory().createSuppliedParameters();
            int interactionHandle = this.rtiAmbassador.getInteractionClassHandle("InteractionRoot."+interactionClass);
            LogicalTime time = convertTime( t );
            this.rtiAmbassador.sendInteraction( interactionHandle, parameters, "tag".getBytes(), time );
        } catch (InteractionClassNotDefined interactionClassNotDefined) {
            interactionClassNotDefined.printStackTrace();
        } catch (InteractionParameterNotDefined interactionParameterNotDefined) {
            interactionParameterNotDefined.printStackTrace();
        } catch (SaveInProgress saveInProgress) {
            saveInProgress.printStackTrace();
        } catch (FederateNotExecutionMember federateNotExecutionMember) {
            federateNotExecutionMember.printStackTrace();
        } catch (NameNotFound nameNotFound) {
            nameNotFound.printStackTrace();
        } catch (RestoreInProgress restoreInProgress) {
            restoreInProgress.printStackTrace();
        } catch (InteractionClassNotPublished interactionClassNotPublished) {
            interactionClassNotPublished.printStackTrace();
        } catch (InvalidFederationTime invalidFederationTime) {
            invalidFederationTime.printStackTrace();
        } catch (RTIinternalError rtIinternalError) {
            rtIinternalError.printStackTrace();
        } catch (ConcurrentAccessAttempted concurrentAccessAttempted) {
            concurrentAccessAttempted.printStackTrace();
        } catch (RTIexception rtIexception) {
            rtIexception.printStackTrace();
        }
    }
}
