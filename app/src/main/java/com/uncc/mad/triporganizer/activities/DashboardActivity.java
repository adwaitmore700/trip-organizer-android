package com.uncc.mad.triporganizer.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.uncc.mad.triporganizer.R;
import com.uncc.mad.triporganizer.adapters.TripAdapter;
import com.uncc.mad.triporganizer.models.Trip;
import com.uncc.mad.triporganizer.models.UserProfile;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {
    TextView fullName;
    TextView gender;
    ImageView myImage;
    ProgressDialog pb;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView recyclerView;
    String path = null;
    public static RecyclerView.Adapter mAdapter;
    public RecyclerView.LayoutManager layoutManager;
    ArrayList<Trip> tripList = new ArrayList<>();
    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        initialize();


        findViewById(R.id.db_iv_edit_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, ChatRoomActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.db_signOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                LoginActivity.mGoogleSignInClient.signOut();
                SharedPreferences.Editor editor = UserProfileActivity.sp.edit();
                editor.clear().commit();
                GoogleSignInAccount account = null;
                Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.db_btn_add_trip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, TripProfileActivity.class);
                startActivity(intent);
            }
        });

    }

    @SuppressLint("WrongViewCast")
    public void initialize(){
        gender = findViewById(R.id.db_tv_gender_text);
        fullName = findViewById(R.id.db_tv_fullName_text);
        myImage = findViewById(R.id.db_iv_profile_photo);
        Gson gson = new Gson();
        UserProfile loggedinUser = gson.fromJson(UserProfileActivity.sp.getString("LoggedInUser", ""),UserProfile.class);
        gender.setText(loggedinUser.getUserGender());
        fullName.setText(loggedinUser.getFirstName()+ " " + loggedinUser.getLastName());
        Picasso.get().load(loggedinUser.getImageUrl()).into(myImage);
        recyclerView = findViewById(R.id.db_lv_trips);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(DashboardActivity.this);

        pb = ProgressDialog.show(DashboardActivity.this,"","Getting Trips...",true);
        db.collection("Trips")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Trip trip = document.toObject(Trip.class);
                                tripList.add(trip);
                            }
                            if(flag == 0) {
                                recyclerView.setLayoutManager(layoutManager);
                                  mAdapter = new TripAdapter(tripList);
                                recyclerView.setAdapter(mAdapter);
                            }
                            mAdapter.notifyDataSetChanged();
                            flag = 1;
                            pb.dismiss();
                        } else {
                          Log.d("demo", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
