package com.uncc.mad.triporganizer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.uncc.mad.triporganizer.R;
import com.uncc.mad.triporganizer.activities.AddUsers;
import com.uncc.mad.triporganizer.activities.TripProfileActivity;
import com.uncc.mad.triporganizer.models.UserProfile;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    public static ArrayList<UserProfile> userList;
    public static String tripID;
    // ArrayList<String> tripMembers;
    public static ArrayList<UserProfile> addedUsers = new ArrayList<>();
    private Context c;

    public UserAdapter(ArrayList<UserProfile> userList, Context context) {
        this.userList = userList;
        c = context;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        addedUsers = new ArrayList<>();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_list_item, parent, false);
        UserViewHolder userViewHolder = new UserViewHolder(view);
        return userViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final UserViewHolder holder, int position) {
        final UserProfile u1 = userList.get(position);
        holder.userListFullName.setText(u1.getFirstName() + " " + u1.getLastName());
        if (u1.getImageUrl() == null || u1.getImageUrl() == "") {
            holder.userphoto.setImageDrawable(c.getDrawable(R.drawable.default_avatar_icon));
        } else {
            Picasso.get().load(u1.getImageUrl()).into(holder.userphoto);
        }

//        for(int i=0;i<tripMembers.size();i++){
//            if(tripMembers.get(i).equals(u1.getUserUID())){
//                holder.addUser.setText("Remove");
//            }
//            else{
//                holder.addUser.setText("Add");
//            }
//        }

        if (u1.getUserUID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            holder.addUser.setVisibility(View.INVISIBLE);
        }

        holder.addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.addUser.getText().toString().equals("Add")) {
                    addedUsers.add(u1);
                    holder.addUser.setText("Remove");
                    holder.canBeAdded = false;// addflag =false;
                } else {
                    addedUsers.remove(addedUsers.indexOf(u1));
                    holder.canBeAdded = true;
                    holder.addUser.setText("Add");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView userListFullName;
        public Button addUser;
        public ImageView userphoto;
        public Boolean canBeAdded;
        // ArrayList<String> tripMembers = new ArrayList<>();

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            canBeAdded = true;
            userListFullName = itemView.findViewById(R.id.list_user_userName);
            addUser = itemView.findViewById(R.id.userAddBtn);
            userphoto = itemView.findViewById(R.id.userImage);
//        FirebaseFirestore.getInstance().collection("Trips").document(tripID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if(task.isSuccessful()){
//                    DocumentSnapshot document = task.getResult();
//                    tripMembers = (ArrayList<String>) document.get("authUsersId");
//                }
//            }
//        });
//
//        for(int i=0;i<tripMembers.size();i++){
//            for (int j=0;j<addedUsers.size();j++) {
//                 if(tripMembers.get(i).equals(addedUsers.get(j).getUserUID())){
//                  if(true){
//                addUser.setText("Remove");
//                 }
//                 else{
//                   addUser.setText("Add");
//                 }
//            }
//        }
//    }

        }
    }
}

