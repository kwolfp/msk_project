package pl.edu.wat.msk.comunication.weather;

import hla.rti.AttributeHandleSet;
import hla.rti.LogicalTime;
import hla.rti.RTIexception;
import hla.rti.SuppliedAttributes;
import hla.rti.jlc.EncodingHelpers;
import hla.rti.jlc.RtiFactoryFactory;
import pl.edu.wat.msk.BaseFederate;

public class WeatherFederate extends BaseFederate<WeatherAmbassador> {

    private int pogodaObjHandle = 0;


    @Override
    protected void update(double time) throws Exception {
        SuppliedAttributes attributes = RtiFactoryFactory.getRtiFactory().createSuppliedAttributes();

        int classHandle = rtiamb.getObjectClass(pogodaObjHandle);
        int stockHandle = rtiamb.getAttributeHandle( "WielkoscOpadow", classHandle );
        byte[] stockValue = EncodingHelpers.encodeInt(123);

        attributes.add(stockHandle, stockValue);
        LogicalTime logicalTime = convertTime( time );
        rtiamb.updateAttributeValues( pogodaObjHandle, attributes, "actualize stock".getBytes(), logicalTime );
    }

    @Override
    protected void registerObjects() throws RTIexception {
        int clazzHandle = rtiamb.getObjectClassHandle("ObjectRoot.Pogoda");
        this.pogodaObjHandle = rtiamb.registerObjectInstance(clazzHandle);
    }

    @Override
    protected void publishAndSubscribe() throws RTIexception {
        int classHandle = rtiamb.getObjectClassHandle("ObjectRoot.Pogoda");
        int wielkoscOpadowHandle = rtiamb.getAttributeHandle( "WielkoscOpadow", classHandle );
        int silaWiatruHandle = rtiamb.getAttributeHandle( "SilaWiatru", classHandle );
        int kierunekWiatruHandle = rtiamb.getAttributeHandle( "KierunekWiatru", classHandle );

        AttributeHandleSet attributes = RtiFactoryFactory.getRtiFactory().createAttributeHandleSet();
        attributes.add( wielkoscOpadowHandle );
        attributes.add( silaWiatruHandle );
        attributes.add( kierunekWiatruHandle );

        rtiamb.publishObjectClass(classHandle, attributes);
    }

    public static void main(String[] args) {
        try {
            new WeatherFederate().runFederate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
