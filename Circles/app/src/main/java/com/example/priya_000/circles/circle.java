package com.example.priya_000.circles;

import android.graphics.PointF;

public class Circle {
    private PointF mOrigin;
    private PointF mCurrent;
    private float mRadius;
    public Circle(PointF origin,float radius) {
        mOrigin = mCurrent = origin;
        mRadius=radius;
    }
    public PointF getCurrent() {
        return mCurrent;
    }
    public void setCurrent(PointF current,float radius) {
        mCurrent = current;
        mRadius=radius;

    }
    public PointF getOrigin() {
        return mOrigin;
    }
    public float getRadius(){
        return mRadius;
    }
    public void setRadius(float radius){
        mRadius=radius;

    }

}
