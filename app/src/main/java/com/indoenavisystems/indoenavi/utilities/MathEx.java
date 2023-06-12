package com.indoenavisystems.indoenavi.utilities;

import com.indoenavisystems.indoenavi.models.Vec2;

public class MathEx {
    public static double cosRelation(double a, double b, double c)
    {
        return Math.acos((Math.pow(a, 2) + Math.pow(b, 2) - Math.pow(c, 2)) / (2 * a * b));
    }
    public static double distanceToPoint(Vec2 p1, Vec2 p2)
    {
        return Math.sqrt(Math.abs(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2)));
    }
}
