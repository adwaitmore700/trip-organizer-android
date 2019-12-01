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

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.squareup.picasso.Picasso;
import com.uncc.mad.triporganizer.R;
import com.uncc.mad.triporganizer.activities.AddUsers;
import com.uncc.mad.triporganizer.activities.TripProfileActivity;
import com.uncc.mad.triporganizer.models.UserProfile;

import java.util.ArrayList;

public class AddedUserAdapter extends RecyclerView.Adapter<AddedUserAdapter.UserViewHolder> {
    public  static ArrayList<UserProfile> userList;
    private Context context;
    Boolean addflag = true;
    public AddedUserAdapter(ArrayList<UserProfile> userList, Context c) {
        this.userList = userList;
        context = c;
    }

    @NonNull
    @Override
    public AddedUserAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_list_item,parent,false);
        AddedUserAdapter.UserViewHolder userViewHolder = new AddedUserAdapter.UserViewHolder(view);
        return userViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        final UserProfile u1 = userList.get(position);
        holder.userListFullName.setText(u1.getFirstName()+" " + u1.getLastName());
        if (u1.getImageUrl() == null || u1.getImageUrl() == "") {
            holder.userphoto.setImageDrawable(context.getDrawable(R.drawable.default_avatar_icon));
        } else {
            Picasso.get().load(u1.getImageUrl()).into(holder.userphoto);
        }
        //Picasso.get().load(u1.getImageUrl()).into(holder.userphoto);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder{
        public TextView userListFullName;
        public ImageView userphoto;
        public Button add;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userListFullName = itemView.findViewById(R.id.list_user_userName);
            userphoto = itemView.findViewById(R.id.userImage);
            add = itemView.findViewById(R.id.userAddBtn);
            add.setVisibility(View.INVISIBLE);
        }
    }
}
