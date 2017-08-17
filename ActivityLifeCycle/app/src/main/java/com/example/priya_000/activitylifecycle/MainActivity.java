package com.example.priya_000.activitylifecycle;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    private static final String TAG="Activity Life Cycle ----";

    TextView TextOutput;
    Button button;
    CharSequence textValue=null;

    //OnCreate Method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // To Restore TextView Value when activity is recreated

        if(savedInstanceState!=null)

            textValue = savedInstanceState.getCharSequence("savedText");

        setContentView(R.layout.activity_main);
        TextOutput = (TextView) this.findViewById(R.id.textView);
        TextOutput.setText(textValue);
        TextOutput.append(getString(R.string.on_create));

         button = (Button) findViewById(R.id.button);
         button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "Text View Cleared");
                    TextOutput.setText("");
                }
         });

         Log.i(TAG, "OnCreate Method called");
}

    //OnStart Method
    @Override
    protected void onStart() {
        super.onStart();
        TextOutput.append(getString(R.string.on_start));
        Log.i(TAG, "OnStart Method called");

    }

   /* @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");

    }*/


    @Override
    protected void onRestart() {
        super.onRestart();
        TextOutput.append(getString(R.string.on_restart));
        Log.i(TAG, "OnRestart Method called");
    }



    @Override
    protected void onResume() {
        super.onResume();
        TextOutput.append(getString(R.string.on_resume));
        Log.i(TAG, "OnResume Method called");
    }


    @Override
    protected void onPause() {
        super.onPause();
        TextOutput.append(getString(R.string.on_pause));
        Log.i(TAG, "OnPause Method called");
    }


   /* @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }*/


    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {

       TextView text = (TextView)findViewById(R.id.textView); //explicitly saving the TextView Value
        CharSequence userText = text.getText();
        savedInstanceState.putCharSequence("savedText", userText);
        super.onSaveInstanceState(savedInstanceState);
        TextOutput.append(getString(R.string.on_saveInstanceState));
        Log.i(TAG, "OnSaveInstanceState Method called");
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        /* TextView text = (TextView)findViewById(R.id.textView);
        CharSequence userText = savedInstanceState.getCharSequence("savedText");
        text.setText(userText);*/
        TextOutput.append(getString(R.string.on_restoreInstanceState));
        Log.i(TAG, "OnRestoreInstanceState Method called");
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
