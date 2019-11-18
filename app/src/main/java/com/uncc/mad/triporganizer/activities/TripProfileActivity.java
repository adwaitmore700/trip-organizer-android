package com.uncc.mad.triporganizer.activities;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.uncc.mad.triporganizer.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TripProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_profile);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                LoginActivity.mGoogleSignInClient.signOut();
                GoogleSignInAccount account = null;
                Intent intent = new Intent(TripProfileActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }}