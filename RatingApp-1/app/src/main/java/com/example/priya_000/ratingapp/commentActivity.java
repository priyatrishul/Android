package com.example.priya_000.ratingapp;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.SimpleAdapter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class commentActivity extends ActionBarActivity {

    ArrayList<HashMap<String, String>> commentList = new ArrayList<HashMap<String, String>>();
    SimpleAdapter mSchedule;
    String userAgent = null;
    AndroidHttpClient httpclient = AndroidHttpClient.newInstance(userAgent);
    ListView list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        list = (ListView) findViewById(R.id.listView);
        Bundle data = getIntent().getExtras();
        int id = data.getInt("Position");
        HttpClientTask task = new HttpClientTask();
        String url = "http://bismarck.sdsu.edu/rateme/comments/"+(id + 1);
        task.execute(url);
       mSchedule = new SimpleAdapter(this, commentList,R.layout.list_layout,
                new String[] {"comment","date"}, new int[] {R.id.FirstText, R.id.SecondText});
        mSchedule.notifyDataSetChanged();

    }

    @Override
    protected void onResume() {
        super.onResume();


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_comment, menu);
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

    public void postComment(View v) {


        RequestQueue queue = Volley.newRequestQueue(this);

        Bundle data = getIntent().getExtras();
        int id = data.getInt("Position");
        EditText postText = (EditText) findViewById(R.id.post_txt);
        String post=postText.getText().toString();
        if (!TextUtils.isEmpty(post) && post.trim().length()>0)
        {

          StringRequest postComment = new StringRequest(Request.Method.POST, "http://bismarck.sdsu.edu/rateme/comment/" + (id + 1),
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
                            // error

                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    EditText postText = (EditText) findViewById(R.id.post_txt);
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(postText.getText().toString(), "text");


                    return params;
                }
            };

            queue.add(postComment);

        }

    }

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
                        JSONObject object = (JSONObject)data.get(i);

                        String Text = object.getString("text");
                        String Time = object.getString("date");
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("comment", Text);
                        map.put("date",Time);
                        commentList.add(map);


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

            list.setAdapter(mSchedule);


        }
    }



}
