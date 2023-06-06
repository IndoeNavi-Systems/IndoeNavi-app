package com.IndoeNaviSystems.indoenavi.models;

public class TrackedSPE {
    private String macAddress;
    private double distance;
    private SPE spe;

    public TrackedSPE(String macAddress, double distance){
        this.macAddress = macAddress;
        this.distance = distance;
        this.spe = new SPE(0, 0, macAddress, "unknown");
    }

    public String getMacAddress() {
        return macAddress;
    }
    public double getDistance() {
        return distance;
    }
    public SPE getSpe() {
        return spe;
    }
    public void setSpe(SPE spe) {
        this.spe = spe;
    }
}
