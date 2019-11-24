package com.uncc.mad.triporganizer.models;

import java.util.Date;
import java.util.List;

public class ChatRoom {
    private String TripId;
    private String UserId;
    private String uId;

//    public String getChatPhoto() {
//        return chatPhoto;
//    }
//
//    public void setChatPhoto(String chatPhoto) {
//        this.chatPhoto = chatPhoto;
//    }
//
//    private String chatPhoto;
    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    private String Messages;
    private long time;

    public long getTime() {
        return time;
    }

    public void setTime() {
        this.time = new Date().getTime();
    }

    public String getTripId() {
        return TripId;
    }

    public void setTripId(String tripId) {
        TripId = tripId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getMessages() {
        return Messages;
    }

    public void setMessages(String messages) {
        Messages = messages;
    }
}
