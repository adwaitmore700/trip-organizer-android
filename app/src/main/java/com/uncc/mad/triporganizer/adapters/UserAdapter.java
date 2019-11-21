package com.uncc.mad.triporganizer.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.uncc.mad.triporganizer.R;
import com.uncc.mad.triporganizer.models.UserProfile;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
public  static ArrayList<UserProfile> userList;

public UserAdapter(ArrayList<UserProfile> userList) {
        this.userList = userList;
        }

@NonNull
@Override
public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.user_list_item,parent,false);
        UserViewHolder userViewHolder = new UserViewHolder(view);
        return userViewHolder;
        }

@Override
public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserProfile u1 = userList.get(position);
        holder.userListFullName.setText(u1.getFirstName()+" " + u1.getLastName());
        Picasso.get().load(u1.getImageUrl()).into(holder.userphoto);

        }

@Override
public int getItemCount() {
        return userList.size();
        }

public static class UserViewHolder extends RecyclerView.ViewHolder{
    public TextView userListFullName;
    public Button addUser;
    public ImageView userphoto;

    public UserViewHolder(@NonNull View itemView) {
        super(itemView);
        userListFullName = itemView.findViewById(R.id.list_user_userName);
       addUser = itemView.findViewById(R.id.userAddBtn);
        userphoto = itemView.findViewById(R.id.userImage);
    }
}
}

