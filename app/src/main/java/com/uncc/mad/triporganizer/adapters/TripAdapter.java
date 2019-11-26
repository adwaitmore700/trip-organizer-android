package com.uncc.mad.triporganizer.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.uncc.mad.triporganizer.R;
import com.uncc.mad.triporganizer.activities.ChatRoomActivity;
import com.uncc.mad.triporganizer.activities.DashboardActivity;
import com.uncc.mad.triporganizer.activities.MainActivity;
import com.uncc.mad.triporganizer.activities.TripProfileActivity;
import com.uncc.mad.triporganizer.models.Trip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripVIewHolder> {
    public  static ArrayList<Trip> tripList;
    public static Boolean flag;
    Boolean joinFlag = true,chat=false;

    ArrayList<String> listOfAuthUsers = new ArrayList<>();

    public TripAdapter(ArrayList<Trip> tripList,Boolean flag) {
        this.tripList = tripList;
        this.flag = flag;
    }

    @NonNull
    @Override
    public TripVIewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trip_list_item_row,parent,false);
        TripVIewHolder tripVIewHolder = new TripVIewHolder(view);

        return tripVIewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final TripVIewHolder holder, final int position) {
        final Trip t1 = tripList.get(position);
        holder.title.setText(t1.getTitle());
        holder.location.setText("Latitude : " + t1.getLocationLatitude()+"  ,  "+"Longitude : "+t1.getLocationLongitude());
        Integer size = t1.getAuthUsersId().size();
        holder.memberCount.setText(size.toString());
        Picasso.get().load(t1.getTripImageUrl()).into(holder.tripImage);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Are you sure you want to delete ?").setTitle("Delete Trip")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteTrip(t1.getId(),position);
                                Toast.makeText(view.getContext(), "Trip Deleted", Toast.LENGTH_SHORT).show();

                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
               AlertDialog alert = builder.create();
               if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(t1.getAdminId()))
                alert.show();
               else
                   Toast.makeText(view.getContext(), "You can't delete this trip.", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

//        holder.joinLeaveBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                DocumentReference tripRef = TripProfileActivity.db.collection("Trips").document(t1.getId());
//                if(joinFlag){
//                    tripRef.update("authorizedUsers", FieldValue.arrayUnion(FirebaseAuth.getInstance().getCurrentUser().getUid()));
//                    holder.joinLeaveBtn.setText("Leave");
//                    joinFlag =false;
//                    //chat = true;
//                }
//                else{
//                    tripRef.update("authorizedUsers", FieldValue.arrayRemove(FirebaseAuth.getInstance().getCurrentUser().getUid()));
//                    joinFlag = true;
//                    chat = false;
//                    listOfAuthUsers = null;
//                    holder.joinLeaveBtn.setText("Join");
//                }
//            }
//        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Intent i = new Intent(view.getContext(), TripProfileActivity.class);
                i.putExtra("TRIPID", t1.getId());
                view.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    public static class TripVIewHolder extends RecyclerView.ViewHolder{
        public TextView title,location,memberCount;
        public ImageView tripImage;
        public Button joinLeaveBtn;

    //    AlertDialog alert;
        public TripVIewHolder(@NonNull final View itemView) {
            super(itemView);
            //joinLeaveBtn = itemView.findViewById(R.id.buttonJoinLeave);
            //joinLeaveBtn.setVisibility(View.INVISIBLE);
            title = itemView.findViewById(R.id.trip_item_title);
            location = itemView.findViewById(R.id.trip_item_location);
            memberCount = itemView.findViewById(R.id.trip_item_memberCount);
            tripImage = itemView.findViewById(R.id.trip_item_photo);
//            if(flag){
//                joinLeaveBtn.setVisibility(View.VISIBLE);
//            }
        }
    }
    public void deleteTrip(String document,int position){
        tripList.remove(position);
        TripProfileActivity.db.collection("Trips").document(document).delete();
        DashboardActivity.mAdapter.notifyDataSetChanged();
        FirebaseDatabase.getInstance().getReference(document).removeValue();
    }
}
