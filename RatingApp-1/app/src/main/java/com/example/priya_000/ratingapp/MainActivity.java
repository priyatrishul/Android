package com.example.priya_000.ratingapp;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.AndroidHttpClient;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CacheResponse;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {


    String userAgent = null;
    AndroidHttpClient httpclient = AndroidHttpClient.newInstance(userAgent);
    ArrayList<String> instructors;
    int total_id;
    ListView listView;
    ArrayAdapter<String> arrayAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            File httpCacheDir = new File(getApplicationContext().getCacheDir(), "http");
            long httpCacheSize = 2 * 1024 * 1024; // 10 MiB
            HttpResponseCache.install(httpCacheDir, httpCacheSize);
        }
        catch(IOException e){
            Log.i("Rew", "HTTP response cache installation failed:" + e);
        }

        HttpResponseCache cache = HttpResponseCache.getInstalled();
        if(cache != null) {
            //   If cache is present, flush it to the filesystem.
            //   Will be used when activity starts again.
            cache.flush();
        }
        listView=(ListView)findViewById(R.id.list);
        instructors=new ArrayList<String>();
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        final Intent instructorInfo = new Intent(this,InstructorDetails.class);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                instructorInfo.putExtra("Position",position);
               startActivity(instructorInfo);
            }
        });


       HttpClientTask task = new HttpClientTask();
        String url = "http://bismarck.sdsu.edu/rateme/list";
        task.execute(url);

        arrayAdapter = new ArrayAdapter<String>(this,R.layout.single_list, instructors);
        listView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        arrayAdapter.notifyDataSetChanged();



    }

    @Override
    protected void onPause() {
        super.onPause();
       httpclient.getConnectionManager().shutdown();
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

        return super.onOptionsItemSelected(item);
    }

//get list of instructors
   class HttpClientTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
           try {

                ResponseHandler<String> responseHandler =
                        new BasicResponseHandler();
                HttpGet getMethod = new HttpGet(urls[0]);

                String responseBody = httpclient.execute(getMethod,
                        responseHandler);
                try {

                    JSONArray data = new JSONArray(responseBody);
                    for(int i=0;i<data.length();i++) {
                        total_id=data.length();
                        JSONObject firstPerson = (JSONObject) data.get(i);
                        String firstName = firstPerson.getString("firstName");
                        String lastName = firstPerson.getString("lastName");
                        int id = firstPerson.getInt("id");
                        instructors.add(firstName+" "+lastName);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return responseBody;
            } catch (Throwable t) {
                Log.i("rew", "did not work", t);
            }


            return null;
        }

        public void onPostExecute(String jsonString) {

                listView.setAdapter(arrayAdapter);
                arrayAdapter.notifyDataSetChanged();


        }
    }


}
