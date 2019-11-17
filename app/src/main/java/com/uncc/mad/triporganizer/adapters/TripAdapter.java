package com.uncc.mad.triporganizer.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.uncc.mad.triporganizer.models.Trip;

import androidx.annotation.NonNull;

public class TripAdapter extends ArrayAdapter<Trip> {
    public TripAdapter(@NonNull Context context, int resource, @NonNull Trip[] objects) {
        super(context, resource, objects);
    }
}
