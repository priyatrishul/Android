package com.example.priya_000.circles;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

import java.util.ArrayList;


public class SimpleDrawing extends View implements View.OnTouchListener{

    Paint black;
    private Circle circle;
    private ArrayList<Circle> CircleList = new ArrayList<Circle>();
    private GestureDetectorCompat mGestureDetector;


    private float radius=20;
    boolean pressDown=false;
    private VelocityTracker mVelocityTracker = null;


    public SimpleDrawing(Context context) {
        this(context, null);
        setOnTouchListener(this);
        mGestureDetector = new GestureDetectorCompat(context, new GestureListener());
    }

    public SimpleDrawing(Context context, AttributeSet xmlAttributes) {
        super(context, xmlAttributes);
        setOnTouchListener(this);
        black = new Paint();
        black.setColor(Color.BLUE);
        black.setStrokeWidth(12.0f);
    }


    public boolean onTouch(View arg0, MotionEvent event) {
        PointF curr = new PointF(event.getX(), event.getY());
        mGestureDetector.onTouchEvent(event);
        int action = event.getAction();
        int actionCode = action & MotionEvent.ACTION_MASK;
        switch (actionCode) {
            case MotionEvent.ACTION_DOWN:
                Log.i("", "inside Down");
                if(CircleList.isEmpty()) {
                    Log.i("", "add First" );
                    circle = new Circle(curr, radius);
                    CircleList.add(circle);
                }
                else {
                    boolean validPoint=true;
                    for (Circle c : CircleList) {
                        Log.i("", "checking the List" + c.getRadius());
                        PointF drawPoint = c.getCurrent();
                        if ((curr.x > (drawPoint.x + c.getRadius()) || curr.x < (drawPoint.x - c.getRadius()))&& (curr.y > (drawPoint.y + c.getRadius()) || curr.y > (drawPoint.y - c.getRadius())))
                            Log.i("", "valid point");
                        else{
                            validPoint=false;

                        }
                        if(!validPoint)
                            break;
                    }

                    if(validPoint){
                        circle = new Circle(curr, radius);
                        CircleList.add(circle);
                    }
                }

                if(mVelocityTracker == null) {

                    mVelocityTracker = VelocityTracker.obtain();
                }
                else {

                    mVelocityTracker.clear();
                }

                mVelocityTracker.addMovement(event);
                return true;
            case MotionEvent.ACTION_MOVE:
                mVelocityTracker.addMovement(event);

                Log.i("", "inside move");
                if(circle!=null) {
                    circle.setCurrent(curr,radius);
                    invalidate();
                }
                return true;
            case MotionEvent.ACTION_UP:
                mVelocityTracker.computeCurrentVelocity(1000);
                Log.i("", "inside Down" + event.getX() + "  " + event.getY() + "  " + radius);
                radius=20;

                pressDown=false;

                break;
            case MotionEvent.ACTION_CANCEL:

                mVelocityTracker.recycle();
                break;
        }
        return false;

    }

    public class GestureListener extends GestureDetector.SimpleOnGestureListener
    {

        private static final String TAG = "Gestures";

        @Override
        public void onLongPress(MotionEvent ev) {
            Log.d(TAG, "LongPress: " + ev.toString());

            pressDown=true;
            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        while(pressDown){
                            Thread.sleep(100);
                             radius+=2;
                            Log.i("", "thread error"+radius);
                        }

                    } catch (Exception e) {
                        Log.i("", "thread error");
                        e.printStackTrace();

                    }
                }
            };
            thread.start();


        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            Log.d(TAG, "onFling: " + event1.toString()+event2.toString());
            return true;
        }
    }


    protected void onDraw(Canvas canvas) {

        canvas.drawColor(Color.GRAY);
        for (Circle c : CircleList) {
            PointF drawPoint=c.getCurrent();
            canvas.drawCircle(drawPoint.x, drawPoint.y, c.getRadius(), black);

        }

    }
}


   /* private boolean handleActionDown(MotionEvent event) {
        Log.i("", "inside down");
        pressDown=true;
        currentX = event.getX();
        currentY = event.getY();
        PointF curr = new PointF(event.getX(), event.getY());
        mCircle = new circle(curr);
        //gestureDetector.onTouchEvent(event);
        mCircs.add(mCircle);
        while(pressDown) {
            radius += 2;
        }
        invalidate();
        handler.post(mLongPressed);
       thread.start();
        if(pressDown) {
            radius+=5;

        }
        radius+=2;
        invalidate();
        return true;
        }

   final GestureDetector gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
        public void onLongPress(MotionEvent e) {
            e.getDownTime();
            Log.e("", "Longpress detected"+e.getEventTime());
            handler.post(mLongPressed);

        }
    });
final Handler handler = new Handler();
        Runnable mLongPressed = new Runnable() {
public void run() {
        Log.i("", "Long press!");
        while (pressDown) {
        radius += 5;
        invalidate();
        }
        }
        };
        Thread thread = new Thread() {
@Override
public void run() {
        try {

        radius += 5;

        } catch (Exception e) {
        Log.i("", "thread error");
        e.printStackTrace();

        }
        }
        };*/
