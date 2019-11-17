package com.uncc.mad.triporganizer.models;


//This model consists of the User Auth data along with user profile data

import android.graphics.Bitmap;

public class UserProfile {
    private UserAuth UserModel;    //Optional for other users, Mandatory for logged in user
    private String FirstName;
    private String LastName;
    private int GenderId;       //optional
    private String Gender;
    private Bitmap ProfilePhoto;
    private boolean IsRegistered;
}
