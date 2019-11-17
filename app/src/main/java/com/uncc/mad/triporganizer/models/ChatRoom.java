package com.uncc.mad.triporganizer.models;

import java.util.List;

public class ChatRoom {
    private String TripId;
    private String UserId;
    private String Id;
    List<Message> Messages;

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

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public List<Message> getMessages() {
        return Messages;
    }

    public void setMessages(List<Message> messages) {
        Messages = messages;
    }
}
