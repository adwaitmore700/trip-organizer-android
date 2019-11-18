package com.uncc.mad.triporganizer.controllers;

import android.graphics.Bitmap;
import com.uncc.mad.triporganizer.interfaces.IData;
import com.uncc.mad.triporganizer.models.Trip;
import com.uncc.mad.triporganizer.models.TripMember;

public class TripService {

    IData idata;

    public TripService(IData idata) {
        this.idata = idata;
    }

    private void SaveTripDetails(Trip tripData){


        //Save to firebase database
    }

    private void JoinOtherTrip(String tripId, TripMember newMember){

        //Save to firebase database
    }

    private void SaveTripCoverPhoto(Bitmap coverPhoto){

        //Save to firebase storage
    }

    private void GetListOfAllTripsCreatedByUser(String userId){

    }

    private void GetListOfAllTrips(){

        //Save to firebase storage

    }

    private void RemoveTripIfOwner(){


    }

    private void RemoveTripIfUser(){


    }
}
