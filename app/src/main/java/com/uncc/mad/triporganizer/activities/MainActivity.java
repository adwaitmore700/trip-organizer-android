package com.uncc.mad.triporganizer.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.uncc.mad.triporganizer.R;


//Starting point of the application, this will also be the login activity
public class MainActivity extends AppCompatActivity {

    private ProgressDialog loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loader = ProgressDialog.show(MainActivity.this, "", "Initializing ...", true);
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        loader.dismiss();
                        finish();
                    }
                }, 3000);
    }
}

