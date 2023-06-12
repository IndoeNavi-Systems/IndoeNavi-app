package com.indoenavisystems.indoenavi.handlers;

import android.util.Log;

import com.indoenavisystems.indoenavi.models.Map;
import com.indoenavisystems.indoenavi.models.TrackedSPE;
import com.indoenavisystems.indoenavi.models.Vec2;
import com.indoenavisystems.indoenavi.utilities.MathEx;

public class MapHandler {
    private static MapHandler instance;
    private Map map = new Map();
    private Vec2 indoorPosition = new Vec2();

    private MapHandler(){}
    static {
        instance = new MapHandler();
    }
    public static MapHandler getInstance() {
        return instance;
    }

    public void setMap(Map map){
        this.map = map;
    }
    public Map getMap(){
        return map;
    }
    public Vec2 getIndoorPosition(){
        return indoorPosition;
    }
    public void calculateIndoorPosition(TrackedSPE trackedSpe1, TrackedSPE trackedSpe2, TrackedSPE trackedSpe3){
        trackedSpe1.setSpe(map.getSpeFromMacAddress(trackedSpe1.getMacAddress()));
        trackedSpe2.setSpe(map.getSpeFromMacAddress(trackedSpe2.getMacAddress()));
        trackedSpe3.setSpe(map.getSpeFromMacAddress(trackedSpe3.getMacAddress()));

        if (trackedSpe1.getSpe() == null || trackedSpe2.getSpe() == null || trackedSpe3.getSpe() == null){
            Log.e("MapHandler", String.format("Failed to calculate indoor position with %1$s, %2$s, %3$s due to invalid MAC addresses.",
                    trackedSpe1.getMacAddress(), trackedSpe2.getMacAddress(), trackedSpe3.getMacAddress()));
            return;
        }
        indoorPosition = calculateIndoorPosition(
                trackedSpe1.getDistance(), trackedSpe1.getSpe().getPosition(),
                trackedSpe2.getDistance(), trackedSpe2.getSpe().getPosition(),
                trackedSpe3.getDistance(), trackedSpe3.getSpe().getPosition());
    }
    private Vec2 calculateIndoorPosition(double d1, Vec2 p1, double d2, Vec2 p2, double d3, Vec2 p3){
        d1 /= map.getMeterPerPixel();
        d2 /= map.getMeterPerPixel();
        d3 /= map.getMeterPerPixel();

        double ab = MathEx.distanceToPoint(p1, p2);
        double angle = MathEx.cosRelation(d1, ab, d2);
        Vec2 result = new Vec2(d1 * Math.cos(angle) + p1.x, d1 * Math.sin(angle) + p1.y);

        double n1 = MathEx.distanceToPoint(indoorPosition, p3);
        double n2 = d3;
        if (n2 - n1 > 1 || n2 - n1 < -1)
        {
            //result = new Vec2(d1 * Math.cos(-angle) + p1.x, d1 * Math.sin(-angle) + p1.y);
        }

        return result;
    }

}
