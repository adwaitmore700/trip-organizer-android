package com.uncc.mad.triporganizer.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.uncc.mad.triporganizer.R;
import com.uncc.mad.triporganizer.adapters.AddedUserAdapter;
import com.uncc.mad.triporganizer.models.Trip;
import com.uncc.mad.triporganizer.models.UserProfile;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class TripProfileActivity extends AppCompatActivity {
    TextView title,lati,longi,setTitle;
    RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;
    public  RecyclerView.Adapter Adapter;
    ImageView tripImage;
    Boolean joinFlag = true,chat=false;
   public static FirebaseFirestore db = FirebaseFirestore.getInstance();
    String ADDUSER = "AddUser";
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

  // ImageView iv_TakePhoto;
    Button invite,chatRoom,joinBtn,delete;
   // static SharedPreferences sp=null;
    DocumentReference docRef = null;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    ArrayList<String> addedUserID = new ArrayList<>();
    static ArrayList<UserProfile> addedUsers = new ArrayList<>();
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
                DocumentReference tripRef = TripProfileActivity.db.collection("Trips").document(tripID);
                if(joinFlag){
                    tripRef.update("authUsersId", FieldValue.arrayUnion(currentUser.getUid()));
                    joinBtn.setText("Leave");
                  //  Adapter.notifyDataSetChanged();
                    joinFlag =false;
                    chat = true;
                }
                else{
                    tripRef.update("authUsersId", FieldValue.arrayRemove(currentUser.getUid()));
                    joinFlag = true;
                    chat = false;
                    joinBtn.setText("Join");
                }
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
                if(chat){
                Intent intent = new Intent(TripProfileActivity.this,ChatRoomActivity.class);
                intent.putExtra("TRIPID",tripID);
                startActivity(intent);}
                else{
                    Toast.makeText(TripProfileActivity.this, "You are not added in trip yet!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        findViewById(R.id.deleteTrip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Are you sure you want to delete ?").setTitle("Delete Trip")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                TripProfileActivity.db.collection("Trips").document(tripID).delete();
                                DashboardActivity.mAdapter.notifyDataSetChanged();
                                FirebaseDatabase.getInstance().getReference(tripID).removeValue();
                                Intent intent = new Intent(TripProfileActivity.this,DashboardActivity.class);
                                startActivity(intent);
                                Toast.makeText(TripProfileActivity.this, "Trip Deleted", Toast.LENGTH_SHORT).show();
                                finish();

                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.create().show();
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
        if(requestCode == 22 && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            ArrayList<UserProfile> users = (ArrayList<UserProfile>) extras.get("ADDEDUSERS");
            for (int i=0;i<users.size();i++){
                addedUserID.add(users.get(i).getUserUID());
            }
            recyclerView.setLayoutManager(layoutManager);
            Adapter = new AddedUserAdapter(users);
            recyclerView.setAdapter(Adapter);
           Adapter.notifyDataSetChanged();

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
        delete = findViewById(R.id.deleteTrip);
        addedUsers = new ArrayList<>();
      //  joinBtn.setVisibility(View.INVISIBLE);
        tripImage = findViewById(R.id.tripImage);
        invite = findViewById(R.id.inviteUsers);
        chatRoom = findViewById(R.id.navigateChatRoom);
        recyclerView = findViewById(R.id.usersRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(TripProfileActivity.this);
        joinBtn = findViewById(R.id.join);
        setTitle = findViewById(R.id.tripTitleOnCover);
        Intent i1 = getIntent();
        tripID = i1.getStringExtra("TRIPID");
        if(tripID==null){
            joinBtn.setVisibility(View.INVISIBLE);
            invite.setVisibility(View.INVISIBLE);
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
                    addedUserID.add(currentUser.getUid());
                    trip.setAuthUsersId(addedUserID);
                    trip.setAdminId(currentUser.getUid());
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
        pageTitle.setText("DASHBOARD");
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(TripProfileActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
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
                Intent intent = new Intent(TripProfileActivity.this, UserProfileActivity.class);
                startActivity(intent);
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
                setTitle.setText(trip.getTitle());
                title.setText(trip.getTitle());
                lati.setText(String.valueOf(trip.getLocationLatitude()));
                longi.setText(String.valueOf(trip.getLocationLongitude()));
                Picasso.get().load(trip.getTripImageUrl()).into(tripImage);
                final ArrayList<String> authIDs = trip.getAuthUsersId();
                TripProfileActivity.db.collection("Users")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        UserProfile userProfile = document.toObject(UserProfile.class);
                                       for(int i=0;i<authIDs.size();i++){
                                           if(userProfile.getUserUID().equals(authIDs.get(i)))
                                               addedUsers.add(userProfile);
                                       }
                                    }
                                        recyclerView.setLayoutManager(layoutManager);
                                        Adapter = new AddedUserAdapter(addedUsers);
                                        recyclerView.setAdapter(Adapter);
                                    }
                                for (int i=0;i<authIDs.size();i++){
                                    if(authIDs.get(i).equals(currentUser.getUid())){
                                        chat = true;
                                        joinBtn.setText("Leave");
                                        joinFlag =false;
                                    }
                                }
                                    Adapter.notifyDataSetChanged();
                            }
                        });

                if(currentUser.getUid().equals(trip.adminId)){
                    joinBtn.setVisibility(View.INVISIBLE);
                    invite.setVisibility(View.VISIBLE);
                    delete.setVisibility(View.VISIBLE);
                }
                else {
                    delete.setVisibility(View.INVISIBLE);
                    joinBtn.setVisibility(View.VISIBLE);
                    invite.setVisibility(View.INVISIBLE);
                }

            }

//             for(int i=0;i<ad;i++){
//                if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(addedUsers.get(i).getUserUID()))
//                    chat = true;
//            }
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