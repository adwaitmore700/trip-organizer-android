package com.uncc.mad.triporganizer.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.uncc.mad.triporganizer.R;
import com.uncc.mad.triporganizer.adapters.AddedUserAdapter;
import com.uncc.mad.triporganizer.adapters.TripAdapter;
import com.uncc.mad.triporganizer.adapters.UserAdapter;
import com.uncc.mad.triporganizer.models.ChatRoom;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class TripProfileActivity extends AppCompatActivity {
    TextView title,lati,longi;
    RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;
    public  RecyclerView.Adapter Adapter;
    ImageView tripImage;
   public static FirebaseFirestore db = FirebaseFirestore.getInstance();
    String ADDUSER = "AddUser";
  // ImageView iv_TakePhoto;
    Button invite,chatRoom,joinBtn;
    static SharedPreferences sp=null;
    DocumentReference docRef = null;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    ArrayList<String> addedUserID = new ArrayList<>();
    Bitmap bitmapUpload = null;
    String path = null;
    static String tripID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_profile);
        setCustomActionBar();

        initialize();

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

        findViewById(R.id.join).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              //  startActivityForResult(intent,22);
            }
        });

        findViewById(R.id.inviteUsers).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TripProfileActivity.this,AddUsers.class);
                intent.putExtra("TRIPID",tripID);
                startActivityForResult(intent,22);
            }
        });

        findViewById(R.id.navigateChatRoom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TripProfileActivity.this,ChatRoomActivity.class);
                intent.putExtra("TRIPID",tripID);
                startActivity(intent);
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
            return ((BitmapDrawable) tripImage.getDrawable()).getBitmap();
        }
        return bitmapUpload;
    }

    public void initialize(){
        title = findViewById(R.id.tripTitle);
        lati = findViewById(R.id.tripLat);
        longi = findViewById(R.id.tripLng);
       //iv_TakePhoto = findViewById(R.id.tripImage);
        tripImage = findViewById(R.id.tripImage);
        invite = findViewById(R.id.inviteUsers);
        chatRoom = findViewById(R.id.navigateChatRoom);
        recyclerView = findViewById(R.id.usersRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(TripProfileActivity.this);
        joinBtn = findViewById(R.id.join);
        Intent i1 = getIntent();
        tripID = i1.getStringExtra("TRIPID");
        if(tripID==null){

        }
        else{
            setTrip(tripID);
        }
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
                    trip.setAuthUsersId(addedUserID);
                    trip.setAdminId(MainActivity.mAuth.getCurrentUser().getUid());
                    trip.setLocationLatitude(Double.parseDouble(lati.getText().toString()));
                    trip.setLocationLongitude(Double.parseDouble(longi.getText().toString()));
                    db.collection("Trips").document(tripTitle).set(trip);
                    Intent intent = new Intent(TripProfileActivity.this, DashboardActivity.class);
                   // intent.putExtra("TRIPID",tripTitle);
                    startActivity(intent);
                    finish();
                }
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
        pageTitle.setText("TRIP DETAILS");
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                MainActivity.mGoogleSignInClient.signOut();
                SharedPreferences.Editor editor = UserProfileActivity.sp.edit();
                editor.clear().commit();
                GoogleSignInAccount account = null;
                Intent intent = new Intent(TripProfileActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        Toolbar toolbar=(Toolbar)action.getCustomView().getParent();
        toolbar.setContentInsetsAbsolute(0, 0);
        toolbar.getContentInsetEnd();
        toolbar.setPadding(0, 0, 0, 0);
        getWindow().setStatusBarColor(getColor(R.color.primaryDarkColor));
    }
    public void setTrip(final String trip){
        TripProfileActivity.db.collection("Trips").document(trip)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Trip trip = documentSnapshot.toObject(Trip.class);
                title.setText(trip.getTitle());
                lati.setText(String.valueOf(trip.getLocationLatitude()));
                longi.setText(String.valueOf(trip.getLocationLongitude()));
                Picasso.get().load(trip.getTripImageUrl()).into(tripImage);
                if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(trip.adminId)){
                    joinBtn.setVisibility(View.INVISIBLE);
                }
                else
                {
                    invite.setVisibility(View.INVISIBLE);
                }
            }
        });
//        TripProfileActivity.db.collection("Trips").document(trip)
//                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    Log.d("demo",trip+"");
//                    if (document.exists()) {
//                        document.getData().get()
//                        listOfAuthUsers = (ArrayList<String>) document.getData().get("authorizedUsers");
//                        for (String id:listOfAuthUsers) {
//                            if (id.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
//                                chat = true;
//                                break;
//                            }
//                        }
//                        if(chat){
//                            Intent i = new Intent(view.getContext(), ChatRoomActivity.class);
//                            i.putExtra("TRIPID", t1.getId());
//                            chat =false;
//                            view.getContext().startActivity(i);
//
//                        }
//                        else{
//                            Toast.makeText(view.getContext(), "You are not added in trip yet!", Toast.LENGTH_SHORT).show();
//                        }
//                    } else {
//                        Log.d("demo", "No such document");
//                    }
//                } else {
//                    Log.d("demo", "get failed with ", task.getException());
//                }
//
//            }
//        });
    }
}