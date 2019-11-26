package com.uncc.mad.triporganizer.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.uncc.mad.triporganizer.R;
import com.uncc.mad.triporganizer.models.ChatRoom;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class ChatRoomActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    String tripDB = null;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private FirebaseListAdapter<ChatRoom> adapter;
    Bitmap bitmapUpload = null;
    String imageURL;
    private ProgressDialog loader;
    private ListView listOfMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        setCustomActionBar();
        showLoader(false);
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            tripDB = intent.getStringExtra("TRIPID");
        }
        findViewById(R.id.cr_iv_capture_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhotoIntent();
            }
        });
        findViewById(R.id.cr_iv_send_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                if(!((EditText) findViewById(R.id.cr_et_enter_message)).getText().toString().trim().equals("")){
                    if(isConnected()){
                        sendMessageOrAttachment("Text", null);
                    }
                    else {
                        Toast.makeText(ChatRoomActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        initializeList();
        loader.dismiss();
    }

    private void showLoader(boolean shortDuration){
        if(isConnected()){
            loader = ProgressDialog.show(ChatRoomActivity.this, "", "Loading messages...", true);
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            loader.dismiss();
                        }
                    }, shortDuration ? 1500 : 3000);
        }
        else{
            Toast.makeText(ChatRoomActivity.this, "Messages will be loaded when internet connection is restored", Toast.LENGTH_SHORT).show();
        }
    }

    private void initializeList(){
        listOfMessages = (ListView) findViewById(R.id.cr_rv_messages_list);
        FirebaseListOptions<ChatRoom> options = new FirebaseListOptions.Builder<ChatRoom>()
                .setLayout(R.layout.message_list_item_row)//Note: The guide doesn't mention this method, without it an exception is thrown that the layout has to be set.
                .setLifecycleOwner(this)
                .setQuery(database.getReference(tripDB), ChatRoom.class)
                .build();
        adapter = new FirebaseListAdapter<ChatRoom>(options) {
            @Override
            protected void populateView(@NonNull View v, @NonNull ChatRoom model, int position) {
                String temp = FirebaseAuth.getInstance().getCurrentUser().getUid();
                ConstraintLayout container1 = v.findViewById(R.id.r_message_item_container);
                ConstraintLayout container2 = v.findViewById(R.id.s_message_item_container);
                if (model.getuId().equals(temp)) {
                    container1.setVisibility(View.INVISIBLE);
                    container2.setVisibility(View.VISIBLE);
                    TextView messageText = (TextView) v.findViewById(R.id.s_message_item_body_text);
                    ImageView chatImage = v.findViewById(R.id.s_message_item_photo);
                    if (model.getMessageType().equals("Text")) {
                        chatImage.setVisibility(View.INVISIBLE);
                        messageText.setText(model.getMessages());
                    } else {
                        messageText.setVisibility(View.INVISIBLE);
                        Picasso.get().load(model.getImageUrl()).into(chatImage);
                    }
                    TextView messageUser = (TextView) v.findViewById(R.id.s_message_item_sender_name);
                    TextView messageTime = (TextView) v.findViewById(R.id.s_message_item_datetime);
                    TextView status = v.findViewById(R.id.s_message_item_status);
                    messageUser.setText(model.getUserId());
                    messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getTime()));
                    status.setText("Sent");
                } else {
                    container1.setVisibility(View.VISIBLE);
                    container2.setVisibility(View.INVISIBLE);
                    TextView messageText = (TextView) v.findViewById(R.id.r_message_item_body_text);
                    ImageView chatImage = v.findViewById(R.id.r_message_item_photo);
                    if (model.getMessageType().equals("Text")) {
                        chatImage.setVisibility(View.INVISIBLE);
                        messageText.setText(model.getMessages());
                    } else {
                        messageText.setVisibility(View.INVISIBLE);
                        Picasso.get().load(model.getImageUrl()).into(chatImage);
                    }
                    TextView messageUser = (TextView) v.findViewById(R.id.r_message_item_sender_name);
                    TextView messageTime = (TextView) v.findViewById(R.id.r_message_item_datetime);
                    TextView status = v.findViewById(R.id.r_message_item_status);
                    messageUser.setText(model.getUserId());
                    messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getTime()));
                    status.setText("Received");
                }
            }
        };
        listOfMessages.setAdapter(adapter);
        listOfMessages.scrollTo(0,listOfMessages.getMaxScrollAmount());
    }

    @Override
    protected void onResume() {
        super.onResume();
        showLoader(true);
        initializeList();
    }

    private void takePhotoIntent() {
        Intent photo = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (photo.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(photo, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void uploadImage(Bitmap photoBitmap, final String UID) {
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        String path = "ChatImage/" + UID + ".png";
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
                    imageURL = task.getResult().toString();
                    sendMessageOrAttachment("Image", imageURL);
                }
            }
        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            bitmapUpload = imageBitmap;
            if(isConnected()){
                uploadImage(imageBitmap,FirebaseAuth.getInstance().getCurrentUser().getUid());
            }
            else {
                Toast.makeText(ChatRoomActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void sendMessageOrAttachment(String messageType, @Nullable String imageUrl) {
        final DatabaseReference tripChat = database.getReference(tripDB);
        ChatRoom chat = new ChatRoom();
        chat.setTime();
        chat.setuId(FirebaseAuth.getInstance().getCurrentUser().getUid());
        chat.setUserId(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        if (messageType.equals("Text")) {
            EditText input = (EditText) findViewById(R.id.cr_et_enter_message);
            chat.setMessages(input.getText().toString().trim());
            chat.setMessageType(messageType);
            input.setText("");
        } else {
            chat.setMessageType("Image");
            chat.setImageUrl(imageUrl);
        }
        tripChat.push()
                .setValue(chat);
        listOfMessages.smoothScrollToPosition(adapter.getCount());
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
                        Intent intent = new Intent(ChatRoomActivity.this, MainActivity.class);
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
                Intent intent = new Intent(ChatRoomActivity.this, UserProfileActivity.class);
                startActivity(intent);
            }
        });
        Toolbar toolbar=(Toolbar)action.getCustomView().getParent();
        toolbar.setContentInsetsAbsolute(0, 0);
        toolbar.getContentInsetEnd();
        toolbar.setPadding(0, 0, 0, 0);
        getWindow().setStatusBarColor(getColor(R.color.primaryDarkColor));
    }
}
