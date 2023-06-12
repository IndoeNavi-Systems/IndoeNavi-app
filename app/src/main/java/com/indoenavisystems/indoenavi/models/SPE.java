package com.indoenavisystems.indoenavi.models;

import java.io.Serializable;

public class SPE implements Serializable {
    private double x;
    private double y;
    private String name;
    private String macAdress;

    SPE(){}
    SPE(double x, double y, String name, String macAddress){
    }
    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }
    public Vec2 getPosition(){
        return new Vec2(x, y);
    }
    public String getName(){
        return name;
    }
    public String getMacAddress(){
        return macAdress;
    }
}
