package com.example.priya_000.weatherapp;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;

public class DisplayImage extends AsyncTask<Object, Void, Bitmap>{

    Context c;
    Bitmap bitmap;
    Details d;
    int id;

    DisplayImage(int ViewId,Context cnxt){

        this.c=cnxt;
        this.id=ViewId;


    }


    @Override
    protected Bitmap doInBackground(Object... args) {
        try {
            String Url=(String)args[0];
            bitmap = BitmapFactory.decodeStream((InputStream) new URL(Url).getContent());
            //bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());
            d=(Details)args[1];

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    @Override
    protected void onPostExecute(Bitmap image) {
        if(image==null)
            Log.i("imagenull","");

        if (image != null) {
            //v.setImageBitmap(image);

            d.setImage(this.id, image);


        }
    }
}