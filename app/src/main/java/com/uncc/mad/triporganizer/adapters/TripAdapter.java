package com.uncc.mad.triporganizer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.uncc.mad.triporganizer.R;
import com.uncc.mad.triporganizer.models.Trip;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripVIewHolder> {
    public  static ArrayList<Trip> tripList;

    public TripAdapter(ArrayList<Trip> tripList) {
        this.tripList = tripList;
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
    public void onBindViewHolder(@NonNull TripVIewHolder holder, int position) {
        Trip t1 = tripList.get(position);
        holder.title.setText(t1.getTitle());
        holder.location.setText(t1.getLocationLatitude()+","+t1.getLocationLongitude());
        Picasso.get().load(t1.getTripImageUrl()).into(holder.tripImage);

    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    public static class TripVIewHolder extends RecyclerView.ViewHolder{
        public TextView title,location;
        public ImageView tripImage;

        public TripVIewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.trip_item_title);
            location = itemView.findViewById(R.id.trip_item_location);
            tripImage = itemView.findViewById(R.id.trip_item_photo);
        }
    }
}
