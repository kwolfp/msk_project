package pl.edu.wat.msk.communication.gui;

import hla.rti.ArrayIndexOutOfBounds;
import hla.rti.CouldNotDiscover;
import hla.rti.EventRetractionHandle;
import hla.rti.FederateInternalError;
import hla.rti.LogicalTime;
import hla.rti.ObjectClassNotKnown;
import hla.rti.ReflectedAttributes;
import hla.rti.jlc.EncodingHelpers;
import pl.edu.wat.msk.BaseAmbassador;
import pl.edu.wat.msk.object.Bullet;
import pl.edu.wat.msk.object.DefenseSystemBullet;
import pl.edu.wat.msk.object.Tank;
import pl.edu.wat.msk.object.Target;
import pl.edu.wat.msk.object.Weather;
import pl.edu.wat.msk.util.Vec3;

/**
 * Created by Kamil Przyborowski
 * Wojskowa Akademia Techniczna im. Jarosława Dąbrowskiego, Warszawa 2018.
 */
public class GuiAmbassador extends BaseAmbassador {

    // Bullet
    public int bulletClass          = 0;
    public int bulletAttr_Rodzaj    = 0;
    public int bulletAttr_Wielkosc  = 0;
    public int bulletAttr_Polozenie = 0;
    public int bulletAttr_WRuchu    = 0;
    public boolean bulletClassFlag_newInstance  = false;
    public boolean bulletClassFlag_attrsUpdated = false;

    // Target
    public int targetClass               = 0;
    public int targetAttrPolozenie       = 0;
    public int targetAttrPoziomUszkodzen = 0;
    public int targetAttrNiezdatny       = 0;
    public boolean targetClassFlag_newInstance  = false;
    public boolean targetClassFlag_attrsUpdated = false;

    // Weather
    public int weatherClass               = 0;
    public int weatherAttrWielkoscOpadow  = 0;
    public int weatherAttrSilaWiatru      = 0;
    public int weatherAttrKierunekWiatru  = 0;
    public boolean weatherClassFlag_newInstance  = false;
    public boolean weatherClassFlag_attrsUpdated = false;

    // Tank
    public int tankClass                    = 0;
    public int tankAttrRodzaj               = 0;
    public int tankAttrWielkosc             = 0;
    public int tankAttrPolozenie            = 0;
    public int tankAttrWRuchu               = 0;
    public int tankAttrWystrzeleniePocisku  = 0;
    public boolean tankClassFlag_newInstance  = false;
    public boolean tankClassFlag_attrsUpdated = false;

    // Tank
    public int defenseSystemBulletClass         = 0;
    public int defenseSystemBulletAttrPolozenie = 0;
    public int defenseSystemBulletAttrWRuchu    = 0;
    public boolean defenseSystemBulletClassFlag_newInstance  = false;
    public boolean defenseSystemBulletClassFlag_attrsUpdated = false;


    @Override
    public void reflectAttributeValues(int theObject, ReflectedAttributes theAttributes, byte[] tag, LogicalTime theTime, EventRetractionHandle retractionHandle) {
        if(this.objs.get(theObject) == this.bulletClass) {
            Bullet bullet = getObjectInstance(Bullet.class);
            for (int i = 0; i < theAttributes.size(); i++) {
                try {
                    int handle = theAttributes.getAttributeHandle(i);
                    byte[] value = theAttributes.getValue(i);

                    if(handle == bulletAttr_Rodzaj && value != null) {
                        bullet.setRodzaj(EncodingHelpers.decodeString(value));
                    } else if(handle == bulletAttr_Wielkosc && value != null) {
                        bullet.setWielkosc(new Vec3(value));
                    } else if(handle == bulletAttr_Polozenie && value != null) {
                        bullet.setPolozenie(new Vec3(value));
                    } else if(handle == bulletAttr_WRuchu && value != null) {
                        bullet.setwRuchu(EncodingHelpers.decodeBoolean(value));
                    }
                } catch (ArrayIndexOutOfBounds aioob) {
                    // won't happen
                }
            }
            this.objectsInstance.replace(Bullet.class, bullet);
            this.bulletClassFlag_attrsUpdated = true;
        } else if(this.objs.get(theObject) == this.targetClass) {
            Target target = getObjectInstance(Target.class);
            for (int i = 0; i < theAttributes.size(); i++) {
                try {
                    int handle = theAttributes.getAttributeHandle(i);
                    byte[] value = theAttributes.getValue(i);

                    if(handle == targetAttrPolozenie && value != null) {
                        target.setPolozenie(new Vec3(value));
                    } else if(handle == targetAttrPoziomUszkodzen && value != null) {
                        target.setPoziomUszkodzen(EncodingHelpers.decodeInt(value));
                    } else if(handle == targetAttrNiezdatny && value != null) {
                        target.setNiezdatny(EncodingHelpers.decodeBoolean(value));
                    }
                } catch (ArrayIndexOutOfBounds aioob) {
                    // won't happen
                }
            }
            this.objectsInstance.replace(Target.class, target);
            this.targetClassFlag_attrsUpdated = true;
        } else if(this.objs.get(theObject) == this.weatherClass) {
            Weather weather = getObjectInstance(Weather.class);
            for (int i = 0; i < theAttributes.size(); i++) {
                try {
                    int handle = theAttributes.getAttributeHandle(i);
                    byte[] value = theAttributes.getValue(i);

                    if(handle == weatherAttrKierunekWiatru && value != null) {
                        weather.setKierunekWiatru(new Vec3(value));
                    } else if(handle == weatherAttrSilaWiatru && value != null) {
                        weather.setSilaWiatru(EncodingHelpers.decodeInt(value));
                    } else if(handle == weatherAttrWielkoscOpadow && value != null) {
                        weather.setWielkoscOpadow(EncodingHelpers.decodeInt(value));
                    }
                } catch (ArrayIndexOutOfBounds aioob) {
                    // won't happen
                }
            }
            this.objectsInstance.replace(Weather.class, weather);
            this.weatherClassFlag_attrsUpdated = true;
        } else if(this.objs.get(theObject) == this.tankClass) {
            Tank tank = getObjectInstance(Tank.class);
            for (int i = 0; i < theAttributes.size(); i++) {
                try {
                    int handle = theAttributes.getAttributeHandle(i);
                    byte[] value = theAttributes.getValue(i);

                    if(handle == tankAttrRodzaj && value != null) {
                        tank.setRodzaj(EncodingHelpers.decodeString(value));
                    } else if(handle == tankAttrWielkosc && value != null) {
                        tank.setWielkosc(new Vec3(value));
                    } else if(handle == tankAttrPolozenie && value != null) {
                        tank.setPolozenie(new Vec3(value));
                    } else if(handle == tankAttrWRuchu && value != null) {
                        tank.setwRuchu(EncodingHelpers.decodeBoolean(value));
                    } else if(handle == tankAttrWystrzeleniePocisku && value != null) {
                        tank.setWystrzeleniePocisku(EncodingHelpers.decodeBoolean(value));
                    }
                } catch (ArrayIndexOutOfBounds aioob) {
                    // won't happen
                }
            }
            this.objectsInstance.replace(Tank.class, tank);
            this.tankClassFlag_attrsUpdated = true;
        } else if(this.objs.get(theObject) == this.defenseSystemBulletClass) {
            DefenseSystemBullet defenseSystemBullet = getObjectInstance(DefenseSystemBullet.class);
            for (int i = 0; i < theAttributes.size(); i++) {
                try {
                    int handle = theAttributes.getAttributeHandle(i);
                    byte[] value = theAttributes.getValue(i);

                    if(handle == defenseSystemBulletAttrPolozenie && value != null) {
                        defenseSystemBullet.setPolozenie(new Vec3(value));
                    } else if(handle == defenseSystemBulletAttrWRuchu && value != null) {
                        defenseSystemBullet.setwRuchu(EncodingHelpers.decodeBoolean(value));
                    }
                } catch (ArrayIndexOutOfBounds aioob) {
                    // won't happen
                }
            }
            this.objectsInstance.replace(DefenseSystemBullet.class, defenseSystemBullet);
            this.defenseSystemBulletClassFlag_attrsUpdated = true;
        }
    }

    @Override
    public void discoverObjectInstance(int theObject, int theObjectClass, String objectName) throws CouldNotDiscover, ObjectClassNotKnown, FederateInternalError {
        super.discoverObjectInstance(theObject, theObjectClass, objectName);

        if(theObjectClass == this.bulletClass) {
            Bullet bullet = new Bullet();
            bullet.setInstance(theObject);
            this.objectsInstance.put(Bullet.class, bullet);
            this.bulletClassFlag_newInstance = true;
        } else if(theObjectClass == this.targetClass) {
            Target target = new Target();
            target.setInstance(theObject);
            this.objectsInstance.put(Target.class, target);
            this.targetClassFlag_newInstance = true;
        } else if(theObjectClass == this.weatherClass) {
            Weather weather = new Weather();
            weather.setInstance(theObject);
            this.objectsInstance.put(Weather.class, weather);
            this.weatherClassFlag_newInstance = true;
        } else if(theObjectClass == this.tankClass) {
            Tank tank = new Tank();
            tank.setInstance(theObject);
            this.objectsInstance.put(Tank.class, tank);
            this.tankClassFlag_newInstance = true;
        } else if(theObjectClass == this.defenseSystemBulletClass) {
            DefenseSystemBullet defenseSystemBullet = new DefenseSystemBullet();
            defenseSystemBullet.setInstance(theObject);
            this.objectsInstance.put(DefenseSystemBullet.class, defenseSystemBullet);
            this.defenseSystemBulletClassFlag_newInstance = true;
        }
    }
}
