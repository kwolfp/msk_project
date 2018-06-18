package pl.edu.wat.msk.util;

import hla.rti.jlc.EncodingHelpers;

import java.nio.ByteBuffer;

public class Vec3 {

    private double x;
    private double y;
    private double z;

    public Vec3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3(byte[] data) {
        this.x = EncodingHelpers.decodeDouble(data, 0);
        this.y = EncodingHelpers.decodeDouble(data, 8);
        this.z = EncodingHelpers.decodeDouble(data, 16);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public byte[] getBytes() {
        ByteBuffer buff = ByteBuffer.allocate(24);
        buff.putDouble(x);
        buff.putDouble(y);
        buff.putDouble(z);
        return buff.array();
    }

    public Vec3 normalize() {
        double len = Math.sqrt(x*x+y*y+z*z);
        this.x = this.x/len;
        this.y = this.y/len;
        this.z = this.z/len;
        return this;
    }

    @Override
    public String toString() {
        return String.format("Vec3{x=%.3f, y=%.3f, z=%.3f}", x, y, z);
    }
}
