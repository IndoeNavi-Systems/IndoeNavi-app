package com.indoenavisystems.indoenavi.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import com.google.gson.Gson;

import java.io.Serializable;

public class Map implements Serializable {
    private String area = "";
    private double meterPerPixel = 1;
    private String imageData = ""; // Endcoded in base64
    private SPE[] spes = new SPE[0];

    private RouteNode[] routeNodes = new RouteNode[0];
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

    public RouteNode[] getRouteNodes(){
        return routeNodes;
    }
    public Bitmap getBitmap(){
        byte[] imageBytes = Base64.decode(getImageData(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }

    public SPE getSpeFromMacAddress(String macAddress){
        for (int i = 0; i < spes.length; i++){
            if (spes[i].getMacAddress().equals(macAddress)){
                return spes[i];
            }
        }
        return null;
    }
}
