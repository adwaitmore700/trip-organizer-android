package com.uncc.mad.triporganizer.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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
//import android.content.SharedPreferences;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {
    ProgressDialog pb;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView recyclerView;
    String path = null;
    public static RecyclerView.Adapter mAdapter;
    public RecyclerView.LayoutManager layoutManager;
    ArrayList<Trip> tripList = new ArrayList<>();
    int flag = 0;
    Boolean joinTrip = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        setCustomActionBar();
        initialize();


        findViewById(R.id.db_add_trip_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, TripProfileActivity.class);
                startActivity(intent);
            }
        });

    }

    private void setCustomActionBar(){
        ActionBar action = getSupportActionBar();
        action.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        action.setDisplayShowCustomEnabled(true);
        action.setCustomView(R.layout.custom_action_bar);
        ImageView imageButton= (ImageView)action.getCustomView().findViewById(R.id.btn_logout);
        TextView pageTitle = action.getCustomView().findViewById(R.id.action_bar_title);
        pageTitle.setText("DASHBOARD");

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
//                SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
//                String sigin  = sharedPref.getString("GoogleAuth","");
//                Object signin = sigin;
//                GoogleSignInClient googleSignInClient = (GoogleSignInClient) signin;
//                googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
//                   @Override
//                   public void onComplete(@NonNull Task<Void> task) {
//
//                   }
//               });
            }
        });
        ImageView profileImage = action.getCustomView().findViewById(R.id.iv_profile_photo);
        Uri uri = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();
        if(uri == null){
            profileImage.setImageDrawable(getDrawable(R.drawable.default_avatar_icon));
        }
        else{
            Picasso.get().load(uri).into(profileImage);
        }
        ConstraintLayout profileContainer = action.getCustomView().findViewById(R.id.my_profile);
        profileContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, UserProfileActivity.class);
                startActivity(intent);
            }
        });
        Toolbar toolbar=(Toolbar)action.getCustomView().getParent();
        toolbar.setContentInsetsAbsolute(0, 0);
        toolbar.getContentInsetEnd();
        toolbar.setPadding(0, 0, 0, 0);
        getWindow().setStatusBarColor(getColor(R.color.primaryDarkColor));
    }

    @SuppressLint("WrongViewCast")
    public void initialize(){
        recyclerView = findViewById(R.id.usersRecyclerView);
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
                                  mAdapter = new TripAdapter(tripList,joinTrip);
                                recyclerView.setAdapter(mAdapter);
                            }
                            mAdapter.notifyDataSetChanged();
                            flag = 1;
                            pb.dismiss();
                        } else {
                          Log.d("demo", "Error getting documents: ", task.getException());
                        }
                    setUserTripCounts();
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUserTripCounts();
    }

    private void setUserTripCounts(){
        TextView userCountDescription = findViewById(R.id.db_tv_user_trip_count);
        try{
            int count = 0;
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            for (Trip t:
                    tripList) {
                List<String> authUsersTrip = t.getAuthUsersId();
                for (String s:
                        authUsersTrip) {
                    if(s.equals(userId)){
                        count++;
                    }
                }
            }

            if(count > 0){
                userCountDescription.setText("You are currently going to "+count+" trips");
            }
            else{
                userCountDescription.setText("You are currently not going to any trip");
            }
        }
        catch(Exception ex) {
            userCountDescription.setText("");
        }
    }
}
