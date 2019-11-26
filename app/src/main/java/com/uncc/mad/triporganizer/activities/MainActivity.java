package com.uncc.mad.triporganizer.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.uncc.mad.triporganizer.R;


//Starting point of the application, this will also be the login activity
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ProgressDialog loader = ProgressDialog.show(MainActivity.this, "", "Initializing ...", true);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (currentUser == null && account == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            loader.dismiss();
        } else {
            DocumentReference docRef = FirebaseFirestore.getInstance().collection("Users").document(currentUser.getUid());
            if (docRef == null) {
                Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
                startActivity(intent);
                loader.dismiss();
            } else {
                Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                startActivity(intent);
                loader.dismiss();
            }
        }
        finish();
//        new android.os.Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
//                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                        startActivity(intent);
//                        loader.dismiss();
//                        finish();
//                    }
//                }, 3000);
    }
}

