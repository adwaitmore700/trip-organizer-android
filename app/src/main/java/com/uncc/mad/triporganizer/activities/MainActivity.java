package com.uncc.mad.triporganizer.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.uncc.mad.triporganizer.R;
import com.uncc.mad.triporganizer.interfaces.IData;
import com.uncc.mad.triporganizer.models.ServiceHelper;


//Starting point of the application, this will also be the login activity
public class MainActivity extends AppCompatActivity implements IData {

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
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(currentUser !=null || account!=null){
            Intent intent = new Intent(MainActivity.this, TripProfileActivity.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(MainActivity.this, "You must be logged in", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        loader.dismiss();
        finish();

//        new android.os.Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
//                        //Log.i("tag","A Kiss after 5 seconds");
//
//
//
//                    }
//                }, 3000);
    }

    @Override
    public void sendResponse(ServiceHelper response, String objectTypeModel) {

    }
}
