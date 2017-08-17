package com.example.priya_000.weatherapp;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class Details extends ActionBarActivity {

    final String API_KEY="b37dd8ecfd0c6b9a21a49ef54661c";
    TextView statusText,tempText,humidText,cityText,dateText,minmaxView,sunsetView,sunriseView;
    ImageView imgView,imgView1,imgView2,imgView3,imgView4;
    ArrayList<String> imgArray;
    Bitmap bitmap;

    TextView[] dayView,maxView,minView;
    ImageView [] imageView;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        dayView= new TextView[5];
        maxView= new TextView[5];
        minView=new TextView[5];
        imageView=new ImageView[5];


        final String lat=getIntent().getStringExtra("lat");
        final String lng=getIntent().getStringExtra("lng");
        final String city=getIntent().getStringExtra("city");
        final String country=getIntent().getStringExtra("country");
        final String region=getIntent().getStringExtra("region");
        final boolean fahrenheit=getIntent().getBooleanExtra("fahrenheit",true);


        pDialog = new ProgressDialog(Details.this);
        pDialog.setMessage("Loading....");
        pDialog.show();
        statusText=(TextView)findViewById(R.id.statusView);
        tempText=(TextView)findViewById(R.id.tempView);
        humidText=(TextView)findViewById(R.id.humidView);
        cityText=(TextView)findViewById(R.id.cityView);
        dateText=(TextView)findViewById(R.id.timeView);
        sunriseView=(TextView)findViewById(R.id.sunriseView);
        sunsetView=(TextView)findViewById(R.id.sunsetView);
        imgView=(ImageView)findViewById(R.id.imageView);
        minmaxView=(TextView)findViewById(R.id.minmaxView);
        imgView=(ImageView)findViewById(R.id.imageView);



        String url="http://api.worldweatheronline.com/free/v2/weather.ashx?q="+lat+","+lng+"&tp=24&format=json&num_of_days=5&key="+API_KEY;
        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        Log.i("Details", url);
        JsonObjectRequest infoReq = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("Details", response.toString());

                        try {

                            String temp_c= ((JSONObject)new JSONArray(new JSONObject(response.getString("data")).getString("current_condition")).get(0)).getString("temp_C");
                            String temp_f= ((JSONObject)new JSONArray(new JSONObject(response.getString("data")).getString("current_condition")).get(0)).getString("temp_F");
                            String humidity=((JSONObject)new JSONArray(new JSONObject(response.getString("data")).getString("current_condition")).get(0)).getString("humidity");
                            String weather=((JSONObject)new JSONArray(((JSONObject)new JSONArray(new JSONObject(response.getString("data")).getString("current_condition")).get(0)).getString("weatherDesc")).get(0)).getString("value");
                            String imgUrl=((JSONObject)new JSONArray(((JSONObject)new JSONArray(new JSONObject(response.getString("data")).getString("current_condition")).get(0)).getString("weatherIconUrl")).get(0)).getString("value");
                            imgUrl=imgUrl.replace("\\","");

                            String date= ((JSONObject)new JSONArray(new JSONObject(response.getString("data")).getString("weather")).get(0)).getString("date");
                            JSONObject astronomy=(JSONObject) new JSONArray(((JSONObject)new JSONArray(new JSONObject(response.getString("data")).getString("weather")).get(0)).getString("astronomy")).get(0);
                            String minF= ((JSONObject)new JSONArray(new JSONObject(response.getString("data")).getString("weather")).get(0)).getString("mintempF");
                            String maxF=((JSONObject)new JSONArray(new JSONObject(response.getString("data")).getString("weather")).get(0)).getString("maxtempF");
                            String minC= ((JSONObject)new JSONArray(new JSONObject(response.getString("data")).getString("weather")).get(0)).getString("mintempC");
                            String maxC= ((JSONObject)new JSONArray(new JSONObject(response.getString("data")).getString("weather")).get(0)).getString("maxtempC");

                            sunriseView.setText("SUNRISE"+" "+astronomy.getString("sunrise"));
                            sunsetView.setText( "SUNSET "+" "+astronomy.getString("sunset"));

                            try {
                                Date  temp = new SimpleDateFormat("yyyy-MM-dd").parse(date);
                                dateText.setText(temp.toString().substring(0, 10) + " " + temp.toString().substring(24, 28));

                            }
                            catch (ParseException pe)
                            {
                                Log.i("ParseException",pe.toString());
                            }
                            if(fahrenheit) {
                                minmaxView.setText("\u2191" + maxF + "\u2109" + " " + "\u2193" + minF + "\u2109");
                                tempText.setText(temp_f + "\u2109");
                            }
                            else {
                                minmaxView.setText("\u2191" + maxC + "\u2103" + " " + "\u2193" + minC + "\u2103");
                                tempText.setText(temp_c + "\u2103");
                            }
                            cityText.setText(city);
                            statusText.setText(weather);


                            humidText.setText("Humidity " + humidity + "%");

                            new DisplayImage(imgView.getId(),Details.this).execute(imgUrl,Details.this);


                            JSONArray forecastArray=new JSONArray(new JSONObject(response.getString("data")).getString("weather"));
                            imgArray = new ArrayList<String>();

                            for(int i=1;i<forecastArray.length();i++){

                                JSONObject obj=(JSONObject)forecastArray.get(i);


                                String dayId="dayView"+(i);
                                String minId="tempminView"+(i);
                                String maxId="tempmaxView"+(i);
                                String imgId="imgView"+(i);
                                int resminID = getResources().getIdentifier(minId, "id", getPackageName());
                                int resmaxID = getResources().getIdentifier(maxId, "id", getPackageName());
                                int resdayID = getResources().getIdentifier(dayId, "id", getPackageName());
                                int resimgID = getResources().getIdentifier(imgId, "id", getPackageName());
                                dayView[i]=(TextView)findViewById(resdayID);
                                maxView[i]=(TextView)findViewById(resmaxID);
                                minView[i]=(TextView)findViewById(resminID);
                                imageView[i]=(ImageView)findViewById(resimgID);
                                try {
                                    Date  temp1 = new SimpleDateFormat("yyyy-MM-dd").parse(obj.getString("date"));
                                    dayView[i].setText(temp1.toString().substring(0,4));


                                }
                                catch (ParseException pe)
                                {
                                    Log.i("ParseException",pe.toString());
                                }
                                if(fahrenheit) {
                                    maxView[i].setText(obj.getString("maxtempF") + "\u2109");
                                    minView[i].setText(obj.getString("mintempF") + "\u2109");
                                }
                                else{
                                    maxView[i].setText(obj.getString("maxtempC") + "\u2103");
                                    minView[i].setText(obj.getString("mintempC") + "\u2103");
                                }
                                String iUrl=(((JSONObject)(new JSONArray(((JSONObject) new JSONArray(obj.getString("hourly")).get(0)).getString("weatherIconUrl")).get(0))).getString("value"));
                                iUrl=iUrl.replace("\\","");
                                imgArray.add(iUrl);
                                new DisplayImage(imageView[i].getId(),Details.this).execute(iUrl, Details.this);

                            }


                            pDialog.dismiss();
                        }

                        catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });


        mRequestQueue.add(infoReq);



    }


       class LoadImage extends AsyncTask<String, String, Bitmap> {
        @Override

        protected Bitmap doInBackground(String... args) {
            try {
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap image) {

            if (image != null) {
                imgView.setImageBitmap(image);

            }
        }
    }

    public void setImage(int id,Bitmap image){

        ImageView i=(ImageView)findViewById(id);
        if(image!=null)
            i.setImageBitmap(image);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
