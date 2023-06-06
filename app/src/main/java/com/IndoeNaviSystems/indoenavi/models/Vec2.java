package com.IndoeNaviSystems.indoenavi.models;

public class Vec2 {
    public double x = 0;
    public double y = 0;

    public Vec2(){
    }

    public Vec2(double x, double y){
        this.x = x;
        this.y = y;
    }
    public Vec2(Vec2 vec){
        x = vec.x;
        y = vec.y;
    }
}
