package com.example.priya_000.ratingapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.AndroidHttpClient;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class InstructorDetails extends ActionBarActivity {

    TextView profName,profFName,profLName,phoneNo,officeNo,emailId,averageRating,totalRatings;
    String fName,lName,phone,email,office,rating,average,totalRating,pid;
    ArrayList<String> commentList,dateList;
    ListView commentView,dateView;
    final int selectRating=0;

    String userAgent = null;
    AndroidHttpClient httpclient = AndroidHttpClient.newInstance(userAgent);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_details);
        Bundle data = getIntent().getExtras();
        int id = data.getInt("Position");
        commentList=new ArrayList<String>();
        dateList=new ArrayList<String>();
        profName=(TextView)findViewById(R.id.profName);
        profFName=(TextView)findViewById(R.id.profFName);
        profLName=(TextView)findViewById(R.id.profLName);
        phoneNo=(TextView)findViewById(R.id.phone_id);
        emailId=(TextView)findViewById(R.id.email_id);
        officeNo=(TextView)findViewById(R.id.office_id);
        averageRating=(TextView)findViewById(R.id.average_id);
        totalRatings=(TextView)findViewById(R.id.totalRating_id);


                RequestQueue mRequestQueue = Volley.newRequestQueue(this);
                JSONObject j=null;
                JsonObjectRequest infoReq = new JsonObjectRequest(Request.Method.GET, "http://bismarck.sdsu.edu/rateme/instructor/" + (id + 1),j,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    Log.i("rew", response.toString() + "inside Volley Request");
                                    pid = response.getString("id");
                                    fName = response.getString("firstName");
                                    lName = response.getString("lastName");
                                    phone = response.getString("phone");
                                    email = response.getString("email");
                                    office = response.getString("office");
                                    rating = response.getString("rating");
                                    JSONObject ratings = new JSONObject(rating);
                                    average = ratings.getString("average");
                                    totalRating = ratings.getString("totalRatings");
                                    profName.setText(fName + " " + lName);
                                    profFName.setText(fName);
                                    profLName.setText(lName);
                                    phoneNo.setText(phone);
                                    emailId.setText(email);
                                    officeNo.setText(office);
                                    averageRating.setText(average + "/5.0");
                                    totalRatings.setText(totalRating);


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
    protected void onResume() {
        super.onResume();


    }


    public void seeComments(View v){

        Intent comment= new Intent(this,commentActivity.class);
        Bundle data = getIntent().getExtras();
        int id = data.getInt("Position");
        comment.putExtra("Position",id);
        startActivity(comment);
    }


    public void addRating(View v){
        final RequestQueue queue = Volley.newRequestQueue(this);
        Bundle data = getIntent().getExtras();
        final int id = data.getInt("Position");
        int check=-1;

        LayoutInflater inflater = getLayoutInflater();
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Select Rating");
        alertDialogBuilder.setSingleChoiceItems(R.array.Ratings,check,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
               TextView rate=(TextView)findViewById(R.id.rateText);
                rate.setText(String.valueOf(which+1));

            }
        });


        alertDialogBuilder.setPositiveButton(R.string.set_button,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int i) {


                        TextView rate = (TextView) findViewById(R.id.rateText);
                        String rateText=rate.getText().toString();
                        if (!TextUtils.isEmpty(rateText) && rateText.trim().length()>0) {

                            StringRequest postRating = new StringRequest(Request.Method.POST,
                                    "http://bismarck.sdsu.edu/rateme/rating/" + (id + 1) + "/" + Integer.valueOf(rate.getText().toString()),
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            // response
                                            Log.d("Response", response);
                                            finish();
                                            startActivity(getIntent());
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {

                                        }
                                    }
                            ) {
                                @Override
                                protected Map<String, String> getParams() {
                                    EditText postText = (EditText) findViewById(R.id.post_txt);
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("", "rate");


                                    return params;
                                }
                            };

                            queue.add(postRating);
                        }

                    }
                });
        alertDialogBuilder.setNegativeButton(R.string.cancel_button,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_instructor_details, menu);
        return true;
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


}
