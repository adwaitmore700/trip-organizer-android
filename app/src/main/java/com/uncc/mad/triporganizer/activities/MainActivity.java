package com.uncc.mad.triporganizer.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.uncc.mad.triporganizer.R;


//Starting point of the application, this will also be the login activity
public class MainActivity extends AppCompatActivity {

    private ProgressDialog loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loader = ProgressDialog.show(MainActivity.this, "", "Initializing ...", true);
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        //Log.i("tag","A Kiss after 5 seconds");

                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        loader.dismiss();
                        //finish();

                    }
                }, 3000);
    }
}
