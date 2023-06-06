package com.indoenavisystems.indoenavi;

import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class DynamicView extends View  {
    public class Vec2 {
        public float x = 0;
        public float y = 0;

        public Vec2(float x, float y){
            this.x = x;
            this.y = y;
        }
    }


    Vec2[] speCoords = new Vec2[] { new Vec2(0, 0), new Vec2(2, 0)  };
    static float d1 = 4;
    static float d2 = 5;
    static Vec2 currentPos;

    public DynamicView(Context context) {
        super(context);
    }

    float cosRelation(float a, float b, float c)
    {
        return (float)Math.acos(((float)Math.pow(a, 2) + (float)Math.pow(b, 2) - (float)Math.pow(c, 2)) / (2 * a * b));
    }
    float distanceToPoint(Vec2 p1, Vec2 p2)
    {
        return (float)Math.sqrt(Math.abs(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2)));
    }

    public static void SetDistance1(float d1){
        Log.d("UWB2", Float.toString(d1));
        DynamicView.d1 = d1;
    }
    public static void SetDistance2(float d2){
        Log.d("UWB2", Float.toString(d2));
        DynamicView.d2 = d2;
    }
    public static Vec2 GetPosition(){
        return currentPos;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        Random random = new Random();
        paint.setColor(Color.argb(255, 0, 0, 255));
        paint.setStrokeWidth(32);

        int wallWidth = 800;
        int wallHeight = 800;
        canvas.drawLine(0, 0, wallWidth, 0, paint);
        canvas.drawLine(0, wallHeight, wallWidth, wallHeight, paint);
        canvas.drawLine(0, 0, 0, wallHeight, paint);
        canvas.drawLine(wallWidth, 0, wallWidth, wallHeight, paint);

        float speDistance = distanceToPoint(speCoords[0], speCoords[1]);
        float angle = cosRelation(d1, speDistance, d2);
        Vec2 pos = new Vec2(d1 * (float)Math.cos(angle) + speCoords[0].x,d1 * (float)Math.sin(angle) + speCoords[0].y);
        currentPos = pos;
        paint.setColor(Color.argb(255, 0, 255, 0));
        canvas.drawCircle(pos.x * 100, pos.y * 100, 16, paint);

        //Draw spe's
        canvas.drawCircle(speCoords[0].x * 100, speCoords[0].y * 100, 16, paint);
        canvas.drawCircle(speCoords[1].x * 100, speCoords[1].y * 100, 16, paint);
        invalidate();
    }
}
