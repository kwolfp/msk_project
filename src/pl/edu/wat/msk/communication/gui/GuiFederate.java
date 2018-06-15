package pl.edu.wat.msk.communication.gui;

import hla.rti.AttributeHandleSet;
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
import hla.rti.RestoreInProgress;
import hla.rti.SaveInProgress;
import hla.rti.SuppliedParameters;
import hla.rti.jlc.RtiFactoryFactory;
import javafx.application.Platform;
import pl.edu.wat.msk.BaseFederate;
import pl.edu.wat.msk.object.Bullet;
import pl.edu.wat.msk.object.DefenseSystemBullet;
import pl.edu.wat.msk.object.Tank;
import pl.edu.wat.msk.object.Target;
import pl.edu.wat.msk.object.Weather;

/**
 * Created by Kamil Przyborowski
 * Wojskowa Akademia Techniczna im. Jarosława Dąbrowskiego, Warszawa 2018.
 */
public class GuiFederate extends BaseFederate<GuiAmbassador> {

    private int sendStartIter = 0;
    private boolean stopSimulation = false;

    private GuiApplication guiApplication;

    public GuiFederate(GuiApplication guiApplication) {
        this.guiApplication = guiApplication;
    }

    public void stopSimulation() {
        this.stopSimulation = true;
    }

    @Override
    protected void waitForSimulationStart() throws RTIinternalError, ConcurrentAccessAttempted, FederateLoggingServiceCalls, SaveInProgress, InteractionClassNotDefined, RestoreInProgress, FederateNotExecutionMember, NameNotFound {

    }

    @Override
    protected void update(double time) throws Exception {
        Platform.runLater(() -> {
            this.guiApplication.simTime.setText(this.federationAmbassador.federateTime+"");
        });

        // Bullet ---------------------------------------------------------------------------------
        if(this.federationAmbassador.bulletClassFlag_newInstance) {
            // pojawił się nowy obiekty typu Cel
            Bullet bullet = this.federationAmbassador.getObjectInstance(Bullet.class);
            this.federationAmbassador.bulletClassFlag_newInstance = false;
            Platform.runLater(() -> {
                this.guiApplication.bulletPolozenie.setText(bullet.getPolozenie()+"");
                this.guiApplication.bulletRodzaj.setText(bullet.getRodzaj());
                this.guiApplication.bulletWielkosc.setText(bullet.getWielkosc()+"");
                this.guiApplication.bulletWRuchu.setText(bullet.iswRuchu()+"");
            });
        }

        if(this.federationAmbassador.bulletClassFlag_attrsUpdated) {
            // zaktualizowano atrybut obiektu Cel
            Bullet bullet = this.federationAmbassador.getObjectInstance(Bullet.class);
            this.federationAmbassador.bulletClassFlag_attrsUpdated = false;
            Platform.runLater(() -> {
                this.guiApplication.bulletPolozenie.setText(bullet.getPolozenie()+"");
                this.guiApplication.bulletRodzaj.setText(bullet.getRodzaj());
                this.guiApplication.bulletWielkosc.setText(bullet.getWielkosc()+"");
                this.guiApplication.bulletWRuchu.setText(bullet.iswRuchu()+"");
            });
        }

        // Target ---------------------------------------------------------------------------------
        if(this.federationAmbassador.targetClassFlag_newInstance) {
            // pojawił się nowy obiekty typu Cel
            Target target = this.federationAmbassador.getObjectInstance(Target.class);
            this.federationAmbassador.targetClassFlag_newInstance = false;
            Platform.runLater(() -> {
                this.guiApplication.targetPolozenie.setText(target.getPolozenie()+"");
                this.guiApplication.targetPoziomUszkodzen.setText(target.getPoziomUszkodzen()+"");
                this.guiApplication.targetNiezdatny.setText(target.getNiezdatny()+"");
            });
        }

        if(this.federationAmbassador.targetClassFlag_attrsUpdated) {
            // zaktualizowano atrybut obiektu Cel
            Target target = this.federationAmbassador.getObjectInstance(Target.class);
            this.federationAmbassador.targetClassFlag_attrsUpdated = false;
            Platform.runLater(() -> {
                this.guiApplication.targetPolozenie.setText(target.getPolozenie()+"");
                this.guiApplication.targetPoziomUszkodzen.setText(target.getPoziomUszkodzen()+"");
                this.guiApplication.targetNiezdatny.setText(target.getNiezdatny()+"");
            });
        }

        // Weather ---------------------------------------------------------------------------------
        if(this.federationAmbassador.weatherClassFlag_newInstance) {
            // pojawił się nowy obiekty typu Pogoda
            Weather weather = this.federationAmbassador.getObjectInstance(Weather.class);
            this.federationAmbassador.weatherClassFlag_newInstance = false;
            Platform.runLater(() -> {
                this.guiApplication.weatherKierunekWiatru.setText(weather.getKierunekWiatru()+"");
                this.guiApplication.weatherSilaWiatru.setText(weather.getSilaWiatru()+"");
                this.guiApplication.weatherWielkoscOpadow.setText(weather.getWielkoscOpadow()+"");
            });
        }

        if(this.federationAmbassador.weatherClassFlag_attrsUpdated) {
            // zaktualizowano atrybut obiektu Pogoda
            Weather weather = this.federationAmbassador.getObjectInstance(Weather.class);
            this.federationAmbassador.weatherClassFlag_attrsUpdated = false;
            Platform.runLater(() -> {
                this.guiApplication.weatherKierunekWiatru.setText(weather.getKierunekWiatru()+"");
                this.guiApplication.weatherSilaWiatru.setText(weather.getSilaWiatru()+"");
                this.guiApplication.weatherWielkoscOpadow.setText(weather.getWielkoscOpadow()+"");
            });
        }

        // Tank ---------------------------------------------------------------------------------
        if(this.federationAmbassador.tankClassFlag_newInstance) {
            // pojawił się nowy obiekty typu Czolg
            Tank tank = this.federationAmbassador.getObjectInstance(Tank.class);
            this.federationAmbassador.tankClassFlag_newInstance = false;
            Platform.runLater(() -> {
                this.guiApplication.tankPolozenie.setText(tank.getPolozenie()+"");
                this.guiApplication.tankRodzaj.setText(tank.getRodzaj());
                this.guiApplication.tankWielkosc.setText(tank.getWielkosc()+"");
                this.guiApplication.tankWRuchu.setText(tank.iswRuchu()+"");
                this.guiApplication.tankWystrzeleniePocisku.setText(tank.isWystrzeleniePocisku()+"");
            });
        }

        if(this.federationAmbassador.tankClassFlag_attrsUpdated) {
            // zaktualizowano atrybut obiektu Czolg
            Tank tank = this.federationAmbassador.getObjectInstance(Tank.class);
            this.federationAmbassador.tankClassFlag_attrsUpdated = false;
            Platform.runLater(() -> {
                this.guiApplication.tankPolozenie.setText(tank.getPolozenie()+"");
                this.guiApplication.tankRodzaj.setText(tank.getRodzaj());
                this.guiApplication.tankWielkosc.setText(tank.getWielkosc()+"");
                this.guiApplication.tankWRuchu.setText(tank.iswRuchu()+"");
                this.guiApplication.tankWystrzeleniePocisku.setText(tank.isWystrzeleniePocisku()+"");
            });
        }

        // DefenseSystemBullet ---------------------------------------------------------------------------------
        if(this.federationAmbassador.defenseSystemBulletClassFlag_newInstance) {
            // pojawił się nowy obiekty typu Czolg
            DefenseSystemBullet defenseSystemBullet = this.federationAmbassador.getObjectInstance(DefenseSystemBullet.class);
            this.federationAmbassador.defenseSystemBulletClassFlag_newInstance = false;
            Platform.runLater(() -> {
                this.guiApplication.defenseSystemBulletPolozenie.setText(defenseSystemBullet.getPolozenie()+"");
                this.guiApplication.defenseSystemBulletWRuchu.setText(defenseSystemBullet.iswRuchu()+"");
            });
        }

        if(this.federationAmbassador.defenseSystemBulletClassFlag_attrsUpdated) {
            // zaktualizowano atrybut obiektu Czolg
            DefenseSystemBullet defenseSystemBullet = this.federationAmbassador.getObjectInstance(DefenseSystemBullet.class);
            this.federationAmbassador.defenseSystemBulletClassFlag_attrsUpdated = false;
            Platform.runLater(() -> {
                this.guiApplication.defenseSystemBulletPolozenie.setText(defenseSystemBullet.getPolozenie()+"");
                this.guiApplication.defenseSystemBulletWRuchu.setText(defenseSystemBullet.iswRuchu()+"");
            });
        }

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

        // subscribe Bullet
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


        // subscribe Target
        this.federationAmbassador.targetClass               = rtiAmbassador.getObjectClassHandle("ObjectRoot.Cel");
        this.federationAmbassador.targetAttrPolozenie       = rtiAmbassador.getAttributeHandle( "Polozenie", this.federationAmbassador.targetClass );
        this.federationAmbassador.targetAttrPoziomUszkodzen = rtiAmbassador.getAttributeHandle( "PoziomUszkodzen", this.federationAmbassador.targetClass );
        this.federationAmbassador.targetAttrNiezdatny       = rtiAmbassador.getAttributeHandle( "Niezdatny", this.federationAmbassador.targetClass );

        attributes = RtiFactoryFactory.getRtiFactory().createAttributeHandleSet();
        attributes.add( this.federationAmbassador.targetAttrPolozenie );
        attributes.add( this.federationAmbassador.targetAttrPoziomUszkodzen );
        attributes.add( this.federationAmbassador.targetAttrNiezdatny );

        rtiAmbassador.subscribeObjectClassAttributes(this.federationAmbassador.targetClass, attributes);


        // subscribe Weather
        this.federationAmbassador.weatherClass              = rtiAmbassador.getObjectClassHandle("ObjectRoot.Pogoda");
        this.federationAmbassador.weatherAttrWielkoscOpadow = rtiAmbassador.getAttributeHandle( "WielkoscOpadow", this.federationAmbassador.weatherClass );
        this.federationAmbassador.weatherAttrSilaWiatru     = rtiAmbassador.getAttributeHandle( "SilaWiatru", this.federationAmbassador.weatherClass );
        this.federationAmbassador.weatherAttrKierunekWiatru = rtiAmbassador.getAttributeHandle( "KierunekWiatru", this.federationAmbassador.weatherClass );

        attributes = RtiFactoryFactory.getRtiFactory().createAttributeHandleSet();
        attributes.add( this.federationAmbassador.weatherAttrWielkoscOpadow );
        attributes.add( this.federationAmbassador.weatherAttrSilaWiatru );
        attributes.add( this.federationAmbassador.weatherAttrKierunekWiatru );

        rtiAmbassador.subscribeObjectClassAttributes(this.federationAmbassador.weatherClass, attributes);


        // subscribe Tank
        this.federationAmbassador.tankClass                   = rtiAmbassador.getObjectClassHandle("ObjectRoot.Czolg");
        this.federationAmbassador.tankAttrRodzaj              = rtiAmbassador.getAttributeHandle( "Rodzaj", this.federationAmbassador.tankClass );
        this.federationAmbassador.tankAttrWielkosc            = rtiAmbassador.getAttributeHandle( "Wielkosc", this.federationAmbassador.tankClass );
        this.federationAmbassador.tankAttrPolozenie           = rtiAmbassador.getAttributeHandle( "Polozenie", this.federationAmbassador.tankClass );
        this.federationAmbassador.tankAttrWRuchu              = rtiAmbassador.getAttributeHandle( "WRuchu", this.federationAmbassador.tankClass );
        this.federationAmbassador.tankAttrWystrzeleniePocisku = rtiAmbassador.getAttributeHandle( "WystrzeleniePocisku", this.federationAmbassador.tankClass );

        attributes = RtiFactoryFactory.getRtiFactory().createAttributeHandleSet();
        attributes.add( this.federationAmbassador.tankAttrRodzaj );
        attributes.add( this.federationAmbassador.tankAttrWielkosc );
        attributes.add( this.federationAmbassador.tankAttrPolozenie );
        attributes.add( this.federationAmbassador.tankAttrWRuchu );
        attributes.add( this.federationAmbassador.tankAttrWystrzeleniePocisku );

        rtiAmbassador.subscribeObjectClassAttributes(this.federationAmbassador.tankClass, attributes);


        // subscribe DefenseSystemBullet
        this.federationAmbassador.defenseSystemBulletClass         = rtiAmbassador.getObjectClassHandle("ObjectRoot.PociskSystemuObronyWroga");
        this.federationAmbassador.defenseSystemBulletAttrPolozenie = rtiAmbassador.getAttributeHandle( "Polozenie", this.federationAmbassador.defenseSystemBulletClass );
        this.federationAmbassador.defenseSystemBulletAttrWRuchu    = rtiAmbassador.getAttributeHandle( "WRuchu", this.federationAmbassador.defenseSystemBulletClass );

        attributes = RtiFactoryFactory.getRtiFactory().createAttributeHandleSet();
        attributes.add( this.federationAmbassador.defenseSystemBulletAttrPolozenie );
        attributes.add( this.federationAmbassador.defenseSystemBulletAttrWRuchu );

        rtiAmbassador.subscribeObjectClassAttributes(this.federationAmbassador.defenseSystemBulletClass, attributes);
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
