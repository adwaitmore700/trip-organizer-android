package com.uncc.mad.triporganizer.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uncc.mad.triporganizer.R;
import com.uncc.mad.triporganizer.models.ChatRoom;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ChatRoomActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    String tripDB = null;
    private FirebaseListAdapter<ChatRoom> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        Intent intent = getIntent();
        if(intent!=null && intent.getExtras()!=null){
           tripDB = intent.getStringExtra("TRIPID");
        }
        final DatabaseReference tripChat = database.getReference(tripDB);

        FloatingActionButton fab =
                (FloatingActionButton)findViewById(R.id.cr_iv_send_message);

        findViewById(R.id.cr_iv_capture_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText)findViewById(R.id.cr_et_enter_message);
                ChatRoom chat = new ChatRoom();
                chat.setMessages(input.getText().toString());
                chat.setTime();
                chat.setuId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                chat.setUserId(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                tripChat.push()
                        .setValue(chat);

                input.setText("");
            }
        });
        ListView listOfMessages = (ListView)findViewById(R.id.cr_rv_messages_list);
        FirebaseListOptions<ChatRoom> options = new FirebaseListOptions.Builder<ChatRoom>()
                .setLayout(R.layout.message_list_item_row)//Note: The guide doesn't mention this method, without it an exception is thrown that the layout has to be set.
                .setLifecycleOwner(this)
                .setQuery(database.getReference(tripDB),ChatRoom.class)
                .build();
            adapter = new FirebaseListAdapter<ChatRoom>(options) {
                @Override
                protected void populateView(@NonNull View v, @NonNull ChatRoom model, int position) {
                    TextView messageText = (TextView)v.findViewById(R.id.message_item_body_text);
                    TextView messageUser = (TextView)v.findViewById(R.id.message_item_sender_name);
                    TextView messageTime = (TextView)v.findViewById(R.id.message_item_datetime);
                    TextView status = v.findViewById(R.id.message_item_status);
                    ImageView chatImages = v.findViewById(R.id.message_item_photo);
                    messageText.setText(model.getMessages());
                    messageUser.setText(model.getUserId());
                    messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getTime()));
                    String temp = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    if(model.getuId().equals(temp)){
                        status.setText("Sent");
                    }
                    else{
                        status.setText("Recieved");
                    }
                }
            };

        listOfMessages.setAdapter(adapter);
    }

}
