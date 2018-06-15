package pl.edu.wat.msk.communication.weather;

import hla.rti.AttributeHandleSet;
import hla.rti.ConcurrentAccessAttempted;
import hla.rti.DeletePrivilegeNotHeld;
import hla.rti.FederateNotExecutionMember;
import hla.rti.LogicalTime;
import hla.rti.ObjectNotKnown;
import hla.rti.RTIexception;
import hla.rti.RTIinternalError;
import hla.rti.RestoreInProgress;
import hla.rti.SaveInProgress;
import hla.rti.SuppliedAttributes;
import hla.rti.jlc.EncodingHelpers;
import hla.rti.jlc.RtiFactoryFactory;
import pl.edu.wat.msk.BaseFederate;
import pl.edu.wat.msk.util.Vec3;

public class WeatherFederate extends BaseFederate<WeatherAmbassador> {

    private int weatherObj = 0;


    @Override
    protected void update(double time) throws Exception {
        // Tu jakaś symulacja a jej wyniki można przesłać za pomocą metod
//        updateWeatherObj_WielkoscOpadow(1, time);
//        updateWeatherObj_WielkoscOpadow(1, time);
//        updateWeatherObj_KierunekWiatru(new Vec3(1, 2, 3), time);
    }

    // ===============================================================================================

    @Override
    protected void init() throws Exception {
        this.weatherObj = createObj("Pogoda");
    }

    @Override
    protected void publishAndSubscribe() throws RTIexception {
        int classHandle = rtiAmbassador.getObjectClassHandle("ObjectRoot.Pogoda");
        int wielkoscOpadowHandle = rtiAmbassador.getAttributeHandle( "WielkoscOpadow", classHandle );
        int silaWiatruHandle = rtiAmbassador.getAttributeHandle( "SilaWiatru", classHandle );
        int kierunekWiatruHandle = rtiAmbassador.getAttributeHandle( "KierunekWiatru", classHandle );

        AttributeHandleSet attributes = RtiFactoryFactory.getRtiFactory().createAttributeHandleSet();
        attributes.add( wielkoscOpadowHandle );
        attributes.add( silaWiatruHandle );
        attributes.add( kierunekWiatruHandle );

        rtiAmbassador.publishObjectClass(classHandle, attributes);
    }

    @Override
    protected void deleteObjectsAndInteractions() throws Exception {
        this.removeObj(this.weatherObj);
    }

    private void updateWeatherObj_WielkoscOpadow(int value, double time) throws Exception {
        SuppliedAttributes attributes = RtiFactoryFactory.getRtiFactory().createSuppliedAttributes();
        int classHandle = rtiAmbassador.getObjectClass(this.weatherObj);
        int attrHandle = rtiAmbassador.getAttributeHandle("WielkoscOpadow", classHandle);
        attributes.add(attrHandle, EncodingHelpers.encodeInt(value));
        rtiAmbassador.updateAttributeValues(this.weatherObj, attributes, "actualize".getBytes(), convertTime(time));
    }

    private void updateWeatherObj_SilaWiatru(int value, double time) throws Exception {
        SuppliedAttributes attributes = RtiFactoryFactory.getRtiFactory().createSuppliedAttributes();
        int classHandle = rtiAmbassador.getObjectClass(this.weatherObj);
        int attrHandle = rtiAmbassador.getAttributeHandle("SilaWiatru", classHandle);
        attributes.add(attrHandle, EncodingHelpers.encodeInt(value));
        rtiAmbassador.updateAttributeValues(this.weatherObj, attributes, "actualize".getBytes(), convertTime(time));
    }

    private void updateWeatherObj_KierunekWiatru(Vec3 value, double time) throws Exception {
        SuppliedAttributes attributes = RtiFactoryFactory.getRtiFactory().createSuppliedAttributes();
        int classHandle = rtiAmbassador.getObjectClass(this.weatherObj);
        int attrHandle = rtiAmbassador.getAttributeHandle("KierunekWiatru", classHandle);
        attributes.add(attrHandle, value.getBytes());
        rtiAmbassador.updateAttributeValues(this.weatherObj, attributes, "actualize".getBytes(), convertTime(time));
    }

    public static void main(String[] args) {
        try {
            new WeatherFederate().runFederate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
