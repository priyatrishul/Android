package com.example.priya_000.weatherapp;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.Switch;


public class MainActivity extends ActionBarActivity {

    final String API_KEY="b37dd8ecfd0c6b9a21a49ef54661c";
    ArrayList<HashMap<String, String>> cityList;
    SimpleAdapter adapter;
    ListView cityListView;
    ProgressDialog pDialog;
    Switch tempSwitch;
    CheckBox locationCheck;
    boolean fahrenheit=true,currentLocation=false,entry1;
    CurrentLocationListener locatn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locatn=new CurrentLocationListener(this);
        Log.i("classinfo", String.valueOf(locatn.isEnabled()) + " " + String.valueOf(locatn.getLatitude()) + " " + String.valueOf(locatn.getLongitude()));

        cityList = new ArrayList<HashMap<String, String>>();
        tempSwitch=(Switch)findViewById(R.id.switchTemp);
        locationCheck=(CheckBox)findViewById(R.id.checkBox);
        tempSwitch.setChecked(fahrenheit);
        locationCheck.setChecked(currentLocation);
        cityListView=(ListView)findViewById(R.id.cityList);

        //when loading check in preference if fahrenheit is checked or not

       if(fahrenheit)
            adapter = new SimpleAdapter(getApplicationContext(),cityList,R.layout.list_item,new String[]{"name","tempF"},new int []{R.id.FstTxt,R.id.ScdTxt});
        else
            adapter = new SimpleAdapter(getApplicationContext(),cityList,R.layout.list_item,new String[]{"name","tempC"},new int []{R.id.FstTxt,R.id.ScdTxt});

       SharedPreferences sharedPreferences = getSharedPreferences("cities", Context.MODE_PRIVATE);

            fahrenheit=sharedPreferences.getBoolean("degree",true);
            tempSwitch.setChecked(fahrenheit);
            currentLocation=sharedPreferences.getBoolean("location",false);
            locationCheck.setChecked(currentLocation);



//load all saved cities from the preference

        if(sharedPreferences.getString("array",null)!=null){

            try {
                JSONArray array = new JSONArray(sharedPreferences.getString("array",null));

                HashMap<String, String> item = null;
                for(int i =0; i<array.length(); i++){
                    JSONObject obj = (JSONObject) array.get(i);

                    Iterator<String> it = obj.keys();
                    item = new HashMap<String, String>();
                    while(it.hasNext()){
                        String key = it.next();
                        item.put(key, (String)obj.get(key));
                    }
                    cityList.add(item);
                    Log.e("Error",  String.valueOf(cityList));
                }



            } catch (JSONException e) {
                Log.e("Error", "while parsing", e);
            }


        }

//each list view has menu
        registerForContextMenu(cityListView);

//if fahrenheit is true or not



        if(fahrenheit)
            adapter = new SimpleAdapter(getApplicationContext(),cityList,R.layout.list_item,new String[]{"name","tempF"},new int []{R.id.FstTxt,R.id.ScdTxt});
        else
            adapter = new SimpleAdapter(getApplicationContext(),cityList,R.layout.list_item,new String[]{"name","tempC"},new int []{R.id.FstTxt,R.id.ScdTxt});
        cityListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();



// if clicked on any lisi item taken to next details page

        cityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> entry = cityList.get(position);
                String lat = entry.get("lat");
                String lng = entry.get("lng");
                String city = entry.get("city");
                String country = entry.get("country");
                String region = entry.get("region");

                Intent details = new Intent(getApplicationContext(), Details.class);
                details.putExtra("lat", lat);
                details.putExtra("lng", lng);
                details.putExtra("city", city);
                details.putExtra("country", country);
                details.putExtra("region", region);
                details.putExtra("fahrenheit", fahrenheit);

                startActivity(details);
            }
        });


        //if temp switch is changed listener

        tempSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    fahrenheit = true;
                    SharedPreferences sharedPreferences = getSharedPreferences("cities", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("degree", fahrenheit);
                    editor.commit();
                    adapter = new SimpleAdapter(getApplicationContext(), cityList, R.layout.list_item, new String[]{"name", "tempF"}, new int[]{R.id.FstTxt, R.id.ScdTxt});
                    cityListView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();


                } else {
                    fahrenheit = false;
                    SharedPreferences sharedPreferences = getSharedPreferences("cities", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("degree", fahrenheit);
                    editor.commit();
                    adapter = new SimpleAdapter(getApplicationContext(), cityList, R.layout.list_item, new String[]{"name", "tempC"}, new int[]{R.id.FstTxt, R.id.ScdTxt});
                    cityListView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }


            }
        });



        //location check listener

        locationCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    entry1=true;
                    SharedPreferences sharedPreferences = getSharedPreferences("cities", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("location", isChecked);
                    editor.commit();

                    if(locatn.isEnabled) {

                        locationAccess(locatn.getLatitude(),locatn.getLongitude());
                    }
                    else{

                       showAlert();

                    }

                }
                else {


                    cityList.remove(0);
                    adapter.notifyDataSetChanged();
                    SharedPreferences sharedPreferences = getSharedPreferences("cities", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("location", isChecked);
                    Gson gson = new Gson();
                    String jsonString = gson.toJson(cityList);
                    editor.putString("array", jsonString);
                    editor.commit();


                }


            }
        });

    }




    @Override
    protected void onResume() {
        super.onResume();
        if(locationCheck.isChecked())
        {

            CurrentLocationListener locatn1;
            locatn1=new CurrentLocationListener(this);
            if(locatn1.isEnabled) {

                locationAccess(locatn1.getLatitude(),locatn1.getLongitude());
            }
            else {
                showAlert();
            }
        }

    }



    @Override
    protected void onPause() {
        super.onPause();
        locatn.stopUsingGPS();
    }

    public void showAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("GPS settings");
        alertDialog.setMessage("GPS is not enabled. Do you want to change GPS settings?");
        alertDialog.setPositiveButton("Change Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);

            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                locationCheck.setChecked(false);
                dialog.cancel();
            }
        });

        alertDialog.show();
        pDialog.dismiss();

    }
    public void locationAccess(double lat,double lng){
        pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMessage("Loading....");
       // pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();
        String url = "http://api.worldweatheronline.com/free/v2/search.ashx?q=" + lat + "," + lng + "&num_of_results=1&format=json&key=" + API_KEY;

        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        Log.i("rew", url);
        JsonObjectRequest infoReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {


                            final String city = ((JSONObject) new JSONArray(((JSONObject) new JSONArray(new JSONObject(response.getString("search_api")).getString("result")).get(0)).getString("areaName")).get(0)).getString("value");
                            final String country = ((JSONObject) new JSONArray(((JSONObject) new JSONArray(new JSONObject(response.getString("search_api")).getString("result")).get(0)).getString("country")).get(0)).getString("value");
                            final String region = ((JSONObject) new JSONArray(((JSONObject) new JSONArray(new JSONObject(response.getString("search_api")).getString("result")).get(0)).getString("region")).get(0)).getString("value");
                            final String lat = ((JSONObject) new JSONArray(new JSONObject(response.getString("search_api")).getString("result")).get(0)).getString("latitude");
                            final String lng = ((JSONObject) new JSONArray(new JSONObject(response.getString("search_api")).getString("result")).get(0)).getString("longitude");

                            Log.i("rew", city + " " + country + " " + region + " " + lat + " " + lng);

                            String url1 = "http://api.worldweatheronline.com/free/v2/weather.ashx?q=" + lat + "," + lng + "&tp=24&format=json&num_of_days=1&key=" + API_KEY;
                            RequestQueue mRequestQueue1 = Volley.newRequestQueue(getApplicationContext());
                            Log.i("rew", url1);
                            JsonObjectRequest infoReq = new JsonObjectRequest(Request.Method.GET, url1, null,
                                    new Response.Listener<JSONObject>() {

                                        @Override
                                        public void onResponse(JSONObject response) {

                                            try {
                                                Log.i("rew", response.toString());

                                                String temp_c = ((JSONObject) new JSONArray(new JSONObject(response.getString("data")).getString("current_condition")).get(0)).getString("temp_C");

                                                String temp_f = ((JSONObject) new JSONArray(new JSONObject(response.getString("data")).getString("current_condition")).get(0)).getString("temp_F");


                                                HashMap<String, String> entry = new HashMap<String, String>();
                                                entry.put("name", city);
                                                entry.put("city", city);
                                                entry.put("country", country);
                                                entry.put("region", region);
                                                entry.put("lat", lat);
                                                entry.put("lng", lng);
                                                entry.put("tempC", temp_c + "\u2103");
                                                entry.put("tempF", temp_f + "\u2109");

                                                if(entry1) {
                                                        cityList.add(0, entry);
                                                        entry1=false;
                                                    }
                                                  else
                                                {
                                                    cityList.set(0, entry);
                                                }




                                                adapter.notifyDataSetChanged();
                                                pDialog.dismiss();



                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }


                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                }
                            });


                            mRequestQueue1.add(infoReq);


                        } catch (JSONException e) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_add) {
           Intent searchCity=new Intent(this,Search_city.class);
            startActivityForResult(searchCity, 1);
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==1)
        {

            final String city=data.getStringExtra("city");
            final String country=data.getStringExtra("country");
            final String region=data.getStringExtra("region");
            final String lat=data.getStringExtra("lat");
            final String lng=data.getStringExtra("lng");


            Log.i("OnActivityResult ",city+"  "+country+"  "+region+"  "+lat+"  "+lng );

            String url1="http://api.worldweatheronline.com/free/v2/weather.ashx?q="+lat+","+lng+"&tp=24&format=json&num_of_days=1&key="+API_KEY;
            RequestQueue mRequestQueue1 = Volley.newRequestQueue(getApplicationContext());
            Log.i("rew", url1);
            JsonObjectRequest infoReq = new JsonObjectRequest(Request.Method.GET, url1,null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                            try {

                                String temp_c= ((JSONObject)new JSONArray(new JSONObject(response.getString("data")).getString("current_condition")).get(0)).getString("temp_C");

                                String temp_f= ((JSONObject)new JSONArray(new JSONObject(response.getString("data")).getString("current_condition")).get(0)).getString("temp_F");

                                Log.i("rew", temp_c + " " + temp_f);


                                HashMap<String,String> entry= new HashMap<String, String>();
                                entry.put("name",city);
                                entry.put("city",city);
                                entry.put("country", country);
                                entry.put("region", region);
                                entry.put("lat", lat);
                                entry.put("lng", lng);
                                entry.put("tempC", temp_c + "\u2103");
                                entry.put("tempF", temp_f + "\u2109");


                                cityList.add(entry);
                               /* for(int i=0;i<cityList.size();i++){
                                    //HashMap<String,String> entry1=new HashMap<String, String>();
                                    //entry1=cityList.get(i);
                                    if(cityList.get(i).equals(entry)){
                                        Log.i("equals","");
                                    }
                                }*/



                                adapter.notifyDataSetChanged();

                                SharedPreferences sharedPreferences = getSharedPreferences("cities", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                Gson gson = new Gson();
                                String jsonString = gson.toJson(cityList);
                                editor.putString("array", jsonString);
                                editor.commit();
                               /* if(locationCheck.isChecked()) {
                                    ArrayList<HashMap<String, String>> anotherList = new ArrayList<HashMap<String, String>>(cityList.subList(1, cityList.size()));
                                    String jsonString = gson.toJson(anotherList);
                                    editor.putString("array", jsonString);
                                    editor.commit();
                                }
                                else{
                                    String jsonString = gson.toJson(cityList);
                                    editor.putString("array", jsonString);
                                    editor.commit();

                                }*/





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


            mRequestQueue1.add(infoReq);


        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.citylist, menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.delete:
               cityList.remove(info.position);
                adapter.notifyDataSetChanged();
                SharedPreferences sharedPreferences = getSharedPreferences("cities", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();
                String jsonString = gson.toJson(cityList);
                editor.putString("array", jsonString);

                editor.commit();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


}

