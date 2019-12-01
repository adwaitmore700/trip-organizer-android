package com.uncc.mad.triporganizer.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.uncc.mad.triporganizer.R;
import com.uncc.mad.triporganizer.adapters.UserAdapter;
import com.uncc.mad.triporganizer.models.UserProfile;

import java.util.ArrayList;

public class AddUsers extends AppCompatActivity {
    RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;
    public static RecyclerView.Adapter mAdapter;
    ProgressDialog pb;
    ArrayList<UserProfile> userList = new ArrayList<>();
    ArrayList<String> tripMembers;
    // ArrayList<UserProfile> addedUsers = new ArrayList<>();

    int flag = 0;
    public static String tripID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_users);
        Intent i = getIntent();
        tripID = i.getStringExtra("TRIPID");
        setCustomActionBar();
        initialize();

        findViewById(R.id.btnSaveTrip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("ADDEDUSERS", UserAdapter.userList);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });
    }

    private void setCustomActionBar() {
        ActionBar action = getSupportActionBar();
        action.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        action.setDisplayShowCustomEnabled(true);
        action.setCustomView(R.layout.custom_action_bar);
        ImageView imageButton = (ImageView) action.getCustomView().findViewById(R.id.btn_logout);
        TextView pageTitle = action.getCustomView().findViewById(R.id.action_bar_title);
        pageTitle.setText("INVITE USERS");
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(AddUsers.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        ImageView profileImage = action.getCustomView().findViewById(R.id.iv_profile_photo);
        Uri uri = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();
        if (uri == null) {
            profileImage.setImageDrawable(getDrawable(R.drawable.default_avatar_icon));
        } else {
            Picasso.get().load(uri).into(profileImage);
        }
        ConstraintLayout profileContainer = action.getCustomView().findViewById(R.id.my_profile);
        profileContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddUsers.this, UserProfileActivity.class);
                startActivity(intent);
            }
        });
        Toolbar toolbar = (Toolbar) action.getCustomView().getParent();
        toolbar.setContentInsetsAbsolute(0, 0);
        toolbar.getContentInsetEnd();
        toolbar.setPadding(0, 0, 0, 0);
        getWindow().setStatusBarColor(getColor(R.color.primaryDarkColor));
    }

    public void initialize() {
        recyclerView = findViewById(R.id.usersRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(AddUsers.this);

        pb = ProgressDialog.show(AddUsers.this, "", "Getting Users...", true);
//        FirebaseFirestore.getInstance().collection("Trips").document(tripID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if(task.isSuccessful()){
//                    DocumentSnapshot document = task.getResult();
//                   tripMembers = (ArrayList<String>) document.get("authUsersId");
//                }
//            }
//        });
        TripProfileActivity.db.collection("Users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            userList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                UserProfile userProfile = document.toObject(UserProfile.class);
                                userList.add(userProfile);
                            }

                            if (flag == 0) {
                                recyclerView.setLayoutManager(layoutManager);
                                mAdapter = new UserAdapter(userList,getApplicationContext());
                                recyclerView.setAdapter(mAdapter);
                            }
                            mAdapter.notifyDataSetChanged();
                            flag = 1;
                            pb.dismiss();
                        } else {
                            Log.d("demo", "Error getting User list: ", task.getException());
                        }
                    }
                });
    }
}
