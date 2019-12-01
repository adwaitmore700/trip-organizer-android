package com.uncc.mad.triporganizer.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.uncc.mad.triporganizer.R;
import com.uncc.mad.triporganizer.models.UserProfile;

import java.io.ByteArrayOutputStream;

public class UserProfileActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ProgressDialog loader;
    TextView tvfirstName, tvlastName;
    RadioGroup rg;
    RadioButton male, female;
    String gender = null;
    public static String userID = null;
    ImageView iv_TakePhoto;
    DocumentReference docRef = null;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Bitmap bitmapUpload = null;
    boolean imageCaptured = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        initialize();
        userID = mAuth.getCurrentUser().getUid();
        docRef = db.collection("Users").document(userID);
        setProfile();
        findViewById(R.id.add_edit_view_iv_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhotoIntent();
            }
        });

        findViewById(R.id.add_edit_view_btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateFormData()) {
                    onSubmitClick();
                } else {
                    Toast.makeText(UserProfileActivity.this, "Fields cannot be blank", Toast.LENGTH_SHORT).show();
                }

            }
        });
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.add_edit_view_rb_male:
                        gender = "MALE";
                        break;
                    case R.id.add_edit_view_rb_female:
                        gender = "FEMALE";
                        break;
                }
            }
        });
    }

    private void takePhotoIntent() {
        Intent photo = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (photo.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(photo, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void initialize() {
        tvfirstName = findViewById(R.id.add_edit_view_tv_fName_value);
        tvlastName = findViewById(R.id.add_edit_view_tv_lName_value);
        iv_TakePhoto = findViewById(R.id.add_edit_view_iv_photo);
        male = findViewById(R.id.add_edit_view_rb_male);
        female = findViewById(R.id.add_edit_view_rb_female);
        rg = findViewById(R.id.add_edit_view_rg);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            iv_TakePhoto.setImageBitmap(imageBitmap);
            bitmapUpload = imageBitmap;
            imageCaptured = true;
        }

    }

    private Bitmap getBitmapCamera() {
        if (bitmapUpload == null) {
            return ((BitmapDrawable) iv_TakePhoto.getDrawable()).getBitmap();
        }
        return bitmapUpload;
    }

    public void setProfile() {
        loader = ProgressDialog.show(UserProfileActivity.this, "", "Initializing ...", true);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserProfile setUser = documentSnapshot.toObject(UserProfile.class);
                if (setUser == null) {
                    iv_TakePhoto.setImageDrawable(getDrawable(R.drawable.default_avatar_icon));
                } else {
                    String imgUrl = setUser.getImageUrl();
                    if (imageCaptured) {
                        iv_TakePhoto.setImageBitmap(bitmapUpload);
                    } else if (imgUrl == null) {
                        iv_TakePhoto.setImageDrawable(getDrawable(R.drawable.default_avatar_icon));
                    } else {
                        imageCaptured = true;
                        Picasso.get().load(imgUrl).into(iv_TakePhoto);
                    }
                    tvfirstName.setText(setUser.getFirstName());
                    tvlastName.setText(setUser.getLastName());
                    Log.d("demo", setUser.getUserGender());
                    String gen = "MALE";
                    if (setUser.getUserGender().equals(gen)) {
                        male.setChecked(true);
                    } else {
                        female.setChecked(true);
                    }
                }
                loader.dismiss();
            }
        });
    }

    private void onSubmitClick() {
        if (isConnected()) {
            loader = ProgressDialog.show(UserProfileActivity.this, "", "Saving user information ...", true);
            if (imageCaptured) {
                uploadImage(getBitmapCamera(), userID);
            } else {
                storeUserProfileToServer(null);
            }
        } else {
            Toast.makeText(UserProfileActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected() || (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                && networkInfo.getType() != connectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }

    private void storeUserProfileToServer(@Nullable Uri uri) {
        try {
            UserProfile user = new UserProfile();
            user.setFirstName(tvfirstName.getText().toString());
            user.setLastName(tvlastName.getText().toString());
            user.setUserGender(gender);
            user.setImageUrl(uri == null ? null : uri.toString());
            user.setUserUID(userID);
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(tvfirstName.getText().toString() + " " + tvlastName.getText().toString())
                    .setPhotoUri(uri)
                    .build();

            mAuth.getCurrentUser().updateProfile(profileUpdates);
            db.collection("Users").document(userID).set(user);
            loader.dismiss();
            Intent intent = new Intent(UserProfileActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        } catch (Exception ex) {
            int t = 0;
        }
    }

    private void uploadImage(Bitmap photoBitmap, final String UID) {
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        String path = "UserImages/" + UID + ".png";
        final StorageReference storageReference = firebaseStorage.getReference(path);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photoBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = storageReference.putBytes(data);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return storageReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    storeUserProfileToServer(task.getResult());
                }
            }
        });
    }

    private boolean validateFormData() {
        if (tvfirstName.getText().toString().trim().equals("") || tvlastName.getText().toString().trim().equals("") || gender == null || gender.equals("")) {
            return false;
        } else {
            return true;
        }
    }
}
