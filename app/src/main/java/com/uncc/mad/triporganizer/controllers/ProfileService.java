package com.uncc.mad.triporganizer.controllers;

import android.graphics.Bitmap;

import com.uncc.mad.triporganizer.interfaces.IData;
import com.uncc.mad.triporganizer.models.UserProfile;


public class ProfileService {

    IData iData;

    public ProfileService(IData iData) {
        this.iData = iData;
    }

    private void SaveUserProfileData(UserProfile userProfile){

        //Save firebase data

    }

    private void SaveUserProfilePhoto(Bitmap photo){

        //Save to firebase storage
    }

    private void GetUsersList(){


        //Save to firebase storage
    }

    private void GetUserProfileData(String userId){


    }
}
