package com.uncc.mad.triporganizer.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.uncc.mad.triporganizer.R;
import com.uncc.mad.triporganizer.adapters.TripAdapter;
import com.uncc.mad.triporganizer.adapters.UserAdapter;
import com.uncc.mad.triporganizer.models.Trip;
import com.uncc.mad.triporganizer.models.UserProfile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class TripProfileActivity extends AppCompatActivity {
    TextView title,lati,longi;
    ImageView tripImage;
    ProgressDialog pb;
    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String ADDUSER = "AddUser";
    TextView tvfirstName,tvlastName;
    RadioGroup rg;
    RadioButton male,female;
    String gender = null;
    String userID = null;
    ImageView iv_TakePhoto;
    static SharedPreferences sp=null;
    DocumentReference docRef = null;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Bitmap bitmapUpload = null;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    String path = null;
    public static RecyclerView.Adapter mAdapter;
    public RecyclerView.LayoutManager layoutManager;
    ArrayList<UserProfile> userList = new ArrayList<>();
    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_profile);
        initialize();
        findViewById(R.id.btnSignOut).setOnClickListener(new View.OnClickListener() {
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

        findViewById(R.id.tripImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhotoIntent();
            }
        });

        findViewById(R.id.btnSaveTrip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage(getBitmapCamera());
            }
        });
    }

    private void takePhotoIntent() {
        Intent photo = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (photo.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(photo, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            tripImage.setImageBitmap(imageBitmap);
            bitmapUpload = imageBitmap;
        }
    }

    private Bitmap getBitmapCamera() {
        if (bitmapUpload == null){
            return ((BitmapDrawable) iv_TakePhoto.getDrawable()).getBitmap();
        }
        return bitmapUpload;
    }

    public void initialize(){
        title = findViewById(R.id.tripTitle);
        lati = findViewById(R.id.tripLat);
        longi = findViewById(R.id.tripLng);
        tripImage = findViewById(R.id.tripImage);

        setUserAdapter();
        }

    private void uploadImage(Bitmap photoBitmap){
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        String path = "TripImages/" + title.getText().toString() + ".png";
        final StorageReference storageReference = firebaseStorage.getReference(path);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photoBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = storageReference.putBytes(data);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()){
                    throw task.getException();
                }
                return storageReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()){
                    String imageURL = task.getResult().toString();
                    String tripTitle = title.getText().toString();
                    Trip trip = new Trip();
                    trip.setId(tripTitle);
                    trip.setTitle(tripTitle);
                    trip.setTripImageUrl(imageURL);
                    trip.setLocationLatitude(Double.parseDouble(lati.getText().toString()));
                    trip.setLocationLongitude(Double.parseDouble(longi.getText().toString()));
                    db.collection("Trips").document(tripTitle).set(trip);
                 //   sp.getString("LoggedInUser","");
                   // Gson gson = new Gson();
                    //String json = gson.toJson(user);
                    //sp = getPreferences(Context.MODE_PRIVATE);
                    //SharedPreferences.Editor editor = sp.edit();
                    //editor.clear();
                    //editor.putString("LoggedInUser", json);
                    //editor.commit();
                    Intent intent = new Intent(TripProfileActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    //finish();
                }
            }
        });
    }

    public void setUserAdapter(){
        recyclerView = findViewById(R.id.usersRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(TripProfileActivity.this);

        pb = ProgressDialog.show(TripProfileActivity.this,"","Getting Users...",true);
        db.collection("Users")
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