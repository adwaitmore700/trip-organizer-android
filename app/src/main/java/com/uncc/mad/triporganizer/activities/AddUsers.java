package com.uncc.mad.triporganizer.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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
    int flag = 0;
    public static String tripID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_users);

        Intent i = getIntent();
         tripID = i.getStringExtra("TRIPID");
        initialize();
    }

    public void initialize(){

        recyclerView = findViewById(R.id.usersRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(AddUsers.this);

        pb = ProgressDialog.show(AddUsers.this,"","Getting Users...",true);
        TripProfileActivity.db.collection("Users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                UserProfile userProfile = document.toObject(UserProfile.class);
                                userList.add(userProfile);
                            }
                            if(flag == 0) {
                                recyclerView.setLayoutManager(layoutManager);
                                mAdapter = new UserAdapter(userList);
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
