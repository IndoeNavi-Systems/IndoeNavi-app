package com.indoenavisystems.indoenavi.models;

public class Vec2 {
    private double x;
    private double y;

    public Vec2(double x, double y){
        this.x = x;
        this.y = y;
    }
    public Vec2(Vec2 vec){
        x = vec.x;
        y = vec.y;
    }
    public double getX(){
        return x;
    }
    public void setX(double x){
        this.x = x;
    }
    public double getY(){
        return y;
    }
    public void setY(double y){
        this.y = y;
    }
}
