package com.example.priya_000.circles;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;


public class MainActivity extends ActionBarActivity {

    SimpleDrawing v;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        v= new SimpleDrawing(this);
        setContentView(v);
    }



   /* @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.i("rew", event.toString());
        logTouchType(event);
        Log.i("rew", "number of touches; " + event.getPointerCount());
        Log.i("rew", "x; " + event.getX() + " y: " + event.getY());
        for (int k = 1; k < event.getPointerCount();k++ )
            Log.i("rew", "x; " + event.getX(k) + " y: " + event.getY(k));
        return true;
    }

    private void logTouchType(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i("rew", "down");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i("rew", "move " + event.getHistorySize() );
                break;
            case MotionEvent.ACTION_UP:
                Log.i("rew", "UP");
                break;
            default:
                Log.i("rew","other action " + event.getAction());
        }
    }
     <View
        android:id="@+id/touch"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.clear_btn) {
           /* View v=findViewById(R.id.touch);
            v.clearAnimation();*/

            return true;
        }


        return super.onOptionsItemSelected(item);
    }
    }

