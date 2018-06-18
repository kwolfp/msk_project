package pl.edu.wat.msk.communication.statistics;

import hla.rti.AttributeHandleSet;
import hla.rti.RTIexception;
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
public class StatisticsFederate extends BaseFederate<StatisticsAmbassador> {

    private int liczbaOddanychStrzalow = 0;
    private int liczbaZestrzelonychStrzalow = 0;
    private int liczbaCelnychStrzalow = 0;

    private double poczatek;
    private double koniec;


    @Override
    protected void update(double time) throws Exception {
        // Bullet ---------------------------------------------------------------------------------
        if(this.federationAmbassador.bulletClassFlag_newInstance) {
            // pojawił się nowy obiekty typu Cel
            Bullet bullet = this.federationAmbassador.getObjectInstance(Bullet.class);
            poczatek = time-timeStep;
            liczbaOddanychStrzalow ++;
            this.federationAmbassador.bulletClassFlag_newInstance = false;
        }

        if(this.federationAmbassador.bulletClassFlag_attrsUpdated) {
            // zaktualizowano atrybut obiektu Cel
            Bullet bullet = this.federationAmbassador.getObjectInstance(Bullet.class);
            if(bullet.isZestrzelony()) {
                liczbaZestrzelonychStrzalow ++;
            }
            this.federationAmbassador.bulletClassFlag_attrsUpdated = false;
        }

        // Target ---------------------------------------------------------------------------------
        if(this.federationAmbassador.targetClassFlag_newInstance) {
            // pojawił się nowy obiekty typu Cel
            Target target = this.federationAmbassador.getObjectInstance(Target.class);
            this.federationAmbassador.targetClassFlag_newInstance = false;
        }

        if(this.federationAmbassador.targetClassFlag_attrsUpdated) {
            // zaktualizowano atrybut obiektu Cel
            Target target = this.federationAmbassador.getObjectInstance(Target.class);
            if(target.getNiezdatny()) {
                liczbaCelnychStrzalow ++;
                koniec = time-timeStep;

                System.out.println("++++ Statystyki ++++");
                System.out.printf("Czas potrzebny do zestrzelenia: %.3f%n", (koniec-poczatek));
                System.out.printf("Aktualna liczba oddanych strzałów: %d%n", liczbaOddanychStrzalow);
                System.out.printf("Aktualna liczba zestrzelonych strzałów: %d%n", liczbaZestrzelonychStrzalow);
                System.out.printf("Aktualna liczba zestrzelonych celów/li: %d%n", liczbaCelnychStrzalow);
                System.out.println();
            }
            this.federationAmbassador.targetClassFlag_attrsUpdated = false;
        }

        // Weather ---------------------------------------------------------------------------------
        if(this.federationAmbassador.weatherClassFlag_newInstance) {
            // pojawił się nowy obiekty typu Pogoda
            Weather weather = this.federationAmbassador.getObjectInstance(Weather.class);
            this.federationAmbassador.weatherClassFlag_newInstance = false;
        }

        if(this.federationAmbassador.weatherClassFlag_attrsUpdated) {
            // zaktualizowano atrybut obiektu Pogoda
            Weather weather = this.federationAmbassador.getObjectInstance(Weather.class);
            this.federationAmbassador.weatherClassFlag_attrsUpdated = false;
        }

        // Tank ---------------------------------------------------------------------------------
        if(this.federationAmbassador.tankClassFlag_newInstance) {
            // pojawił się nowy obiekty typu Czolg
            Tank tank = this.federationAmbassador.getObjectInstance(Tank.class);
            this.federationAmbassador.tankClassFlag_newInstance = false;
        }

        if(this.federationAmbassador.tankClassFlag_attrsUpdated) {
            // zaktualizowano atrybut obiektu Czolg
            Tank tank = this.federationAmbassador.getObjectInstance(Tank.class);
            this.federationAmbassador.tankClassFlag_attrsUpdated = false;
        }

        // DefenseSystemBullet ---------------------------------------------------------------------------------
        if(this.federationAmbassador.defenseSystemBulletClassFlag_newInstance) {
            // pojawił się nowy obiekty typu Czolg
            DefenseSystemBullet defenseSystemBullet = this.federationAmbassador.getObjectInstance(DefenseSystemBullet.class);
            this.federationAmbassador.defenseSystemBulletClassFlag_newInstance = false;
        }

        if(this.federationAmbassador.defenseSystemBulletClassFlag_attrsUpdated) {
            // zaktualizowano atrybut obiektu Czolg
            DefenseSystemBullet defenseSystemBullet = this.federationAmbassador.getObjectInstance(DefenseSystemBullet.class);
            this.federationAmbassador.defenseSystemBulletClassFlag_attrsUpdated = false;
        }
    }

    @Override
    protected void publishAndSubscribe() throws RTIexception {
        // subscribe Bullet
        this.federationAmbassador.bulletClass = rtiAmbassador.getObjectClassHandle("ObjectRoot.Pocisk");
        this.federationAmbassador.bulletAttr_Rodzaj = rtiAmbassador.getAttributeHandle( "Rodzaj", this.federationAmbassador.bulletClass);
        this.federationAmbassador.bulletAttr_Wielkosc = rtiAmbassador.getAttributeHandle( "Wielkosc", this.federationAmbassador.bulletClass);
        this.federationAmbassador.bulletAttr_Polozenie = rtiAmbassador.getAttributeHandle( "Polozenie", this.federationAmbassador.bulletClass);
        this.federationAmbassador.bulletAttr_WRuchu = rtiAmbassador.getAttributeHandle( "WRuchu", this.federationAmbassador.bulletClass);
        this.federationAmbassador.bulletAttr_Zestrzelony = rtiAmbassador.getAttributeHandle( "Zestrzelony", this.federationAmbassador.bulletClass);

        AttributeHandleSet attributes = RtiFactoryFactory.getRtiFactory().createAttributeHandleSet();
        attributes.add( this.federationAmbassador.bulletAttr_Rodzaj);
        attributes.add( this.federationAmbassador.bulletAttr_Wielkosc);
        attributes.add( this.federationAmbassador.bulletAttr_Polozenie);
        attributes.add( this.federationAmbassador.bulletAttr_WRuchu);
        attributes.add( this.federationAmbassador.bulletAttr_Zestrzelony);

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

    public static void main(String[] args) {
        try {
            new StatisticsFederate().runFederate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
