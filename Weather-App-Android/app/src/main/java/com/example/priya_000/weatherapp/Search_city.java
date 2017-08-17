package com.example.priya_000.weatherapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Search_city extends ActionBarActivity {

    final String API_KEY="b37dd8ecfd0c6b9a21a49ef54661c";
    AutoCompleteTextView autoText;
     ArrayAdapter adapter1;
    ArrayList<String> nameArray,regionArray,countryArray,latArray,lngArray,cityArray;
    Button cancelBtn;
    ListView cityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_city);
        autoText=(AutoCompleteTextView)findViewById(R.id.acText);
        cancelBtn=(Button)findViewById(R.id.cancelBtn);
        cityList=(ListView)findViewById(R.id.listView);



        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        autoText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                    RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());

                    String url = "http://api.worldweatheronline.com/free/v2/search.ashx?q=" + s + "&format=json&key=" + API_KEY;
                    url = url.replace(" ", "%20");


                    JsonObjectRequest infoReq = new JsonObjectRequest(Request.Method.GET, url, null,
                            new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {

                                    try {

                                        Log.i("rew", response.toString() + "inside Volley Request");
                                        nameArray = new ArrayList<String>();
                                        regionArray = new ArrayList<String>();
                                        countryArray = new ArrayList<String>();
                                        latArray = new ArrayList<String>();
                                        lngArray = new ArrayList<String>();
                                        cityArray = new ArrayList<String>();

                                        JSONArray cityNames = new JSONArray(new JSONObject(response.getString("search_api")).getString("result"));
                                        Log.i("rew", String.valueOf(cityNames.length()) + " value of the name array");
                                        for (int i = 0; i < cityNames.length(); i++) {
                                            JSONObject object = (JSONObject) cityNames.get(i);

                                            String city = ((JSONObject) new JSONArray(object.getString("areaName")).get(0)).getString("value");
                                            String country = ((JSONObject) new JSONArray(object.getString("country")).get(0)).getString("value");
                                            String region = ((JSONObject) new JSONArray(object.getString("region")).get(0)).getString("value");
                                            String lat = object.getString("latitude");
                                            String lng = object.getString("longitude");


                                            nameArray.add(city);
                                            countryArray.add(country);
                                            regionArray.add(region);
                                            latArray.add(lat);
                                            lngArray.add(lng);
                                            cityArray.add(city + "," + country);


                                        }
                                         adapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, cityArray);
                                        cityList.setAdapter(adapter1);

                                        adapter1.notifyDataSetChanged();


                                    } catch (JSONException e) {

                                        adapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, cityArray);

                                        cityList.setAdapter(adapter1);

                                        Log.i("error-----", "Unable to find any matching weather location to the query submitted! ");

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
            public void afterTextChanged(Editable s) {




        }


    });

        cityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent();
                intent.putExtra("city",nameArray.get(position));
                intent.putExtra("country",countryArray.get(position));
                intent.putExtra("region",regionArray.get(position));
                intent.putExtra("lat",latArray.get(position));
                intent.putExtra("lng",lngArray.get(position));
                setResult(1, intent);
                finish();

            }
        });



        autoText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_SEARCH){


                }

                return false;
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_search_city, menu);
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
