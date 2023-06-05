package com.IndoeNaviSystems.indoenavi.models;

public class Map {
    private String area;
    private double meterPerPixel;
    private String imageData; // Endcoded in base64
    private SPE[] spes;

    public String getArea(){
        return area;
    }
    public double getMeterPerPixel(){
        return meterPerPixel;
    }
    public String getImageData(){
        return imageData;
    }
    public SPE[] getSpes(){
        return spes;
    }
}
