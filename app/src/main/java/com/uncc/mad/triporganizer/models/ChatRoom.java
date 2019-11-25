package com.uncc.mad.triporganizer.models;

import java.util.Date;
import java.util.List;

public class ChatRoom {
    private String TripId;
    private String UserId;
    private String uId;
    private String MessageType;
    private String ImageUrl;
    private String Messages;
    private long time;

    public String getMessageType() {
        return MessageType;
    }

    public void setMessageType(String messageType) {
        MessageType = messageType;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

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
