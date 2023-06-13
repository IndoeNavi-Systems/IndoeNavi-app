package com.indoenavisystems.indoenavi.models;

import java.io.Serializable;

public class RouteNode implements Serializable {
    private int x = 0;
    private int y = 0;
    private boolean isDestination = false;
    private String name = "";

    public Vec2 getPosition(){
        return new Vec2(x, y);
    }
    public boolean getIsDestination(){
        return isDestination;
    }
    public String getName(){
        return name;
    }
}
