package com.uncc.mad.triporganizer.models;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Trip {
    private String Id;
    private String Title;
    public String adminId;

    public ArrayList<String> getAuthUsersId() {
        return authUsersId;
    }

    public void setAuthUsersId(ArrayList<String> authUsersId) {
        this.authUsersId = authUsersId;
    }

    public ArrayList<String> authUsersId;
    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getTripImageUrl() {
        return tripImageUrl;
    }

    public void setTripImageUrl(String tripImageUrl) {
        this.tripImageUrl = tripImageUrl;
    }

    private String tripImageUrl;
   // private Bitmap CoverPhoto;
   // private String LocationName;
    private double LocationLatitude;
    private double LocationLongitude;
    //private com.uncc.mad.triporganizer.models.ChatRoom ChatRoom;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
//
//    public Bitmap getCoverPhoto() {
//        return CoverPhoto;
//    }

//    public void setCoverPhoto(Bitmap coverPhoto) {
//        CoverPhoto = coverPhoto;
//    }
//
//    public String getLocationName() {
//        return LocationName;
//    }
//
//    public void setLocationName(String locationName) {
//        LocationName = locationName;
//    }

    public double getLocationLatitude() {
        return LocationLatitude;
    }

    public void setLocationLatitude(double locationLatitude) {
        LocationLatitude = locationLatitude;
    }

    public double getLocationLongitude() {
        return LocationLongitude;
    }

    public void setLocationLongitude(double locationLongitude) {
        LocationLongitude = locationLongitude;
    }

//    public com.uncc.mad.triporganizer.models.ChatRoom getChatRoom() {
//        return ChatRoom;
//    }
//
//    public void setChatRoom(com.uncc.mad.triporganizer.models.ChatRoom chatRoom) {
//        ChatRoom = chatRoom;
//    }
}
