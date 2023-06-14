package com.indoenavisystems.indoenavi.models;

import java.io.Serializable;

public class SPE implements Serializable {
    private double x = 0;
    private double y = 0;
    private String name = "";
    private String macAdress = "";

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
