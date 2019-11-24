package com.uncc.mad.triporganizer.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.auth.User;
import com.uncc.mad.triporganizer.R;

import java.util.HashMap;
import java.util.Map;


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
//              DocumentReference check =  TripProfileActivity.db.collection("Users").document("7rokiBWVG0cp8ZbI0J7lELs1");
//              check.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                  @Override
//                  public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                      if (task.isSuccessful()) {
//                          DocumentSnapshot document = task.getResult();
//                          if (document.exists()) {
//                              Log.d("demo", "DocumentSnapshot data: " + document.getData());
//                          } else {
//                              Log.d("demo", "No such document");
//                          }
//                      } else {
//                          Log.d("demo", "get failed with ", task.getException());
//                      }
//                  }
//              });
////                washingtonRef.update("authorizedUsers", FieldValue.arrayUnion("Pushp"));
////               washingtonRef.update("authorizedUsers", FieldValue.arrayRemove("Pushpaadeepa"));
//////                Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
//////                startActivity(intent);
//////                finish();
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

